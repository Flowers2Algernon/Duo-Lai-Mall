package com.cskaoyan.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.common.constant.RedisConst;
import com.cskaoyan.mall.common.constant.ResultCodeEnum;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.common.util.DateUtil;
import com.cskaoyan.mall.mq.constant.MqTopicConst;
import com.cskaoyan.mall.mq.producer.BaseProducer;
import com.cskaoyan.mall.order.client.CartApiClient;
import com.cskaoyan.mall.order.client.PayApiClient;
import com.cskaoyan.mall.order.client.ProductApiClient;
import com.cskaoyan.mall.order.client.WareApiClient;
import com.cskaoyan.mall.order.constant.OrderStatus;
import com.cskaoyan.mall.order.constant.OrderType;
import com.cskaoyan.mall.order.converter.OrderDetailConverter;
import com.cskaoyan.mall.order.converter.OrderInfoConverter;
import com.cskaoyan.mall.order.dto.OrderDetailDTO;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.order.mapper.OrderDetailMapper;
import com.cskaoyan.mall.order.mapper.OrderInfoMapper;
import com.cskaoyan.mall.order.model.OrderDetail;
import com.cskaoyan.mall.order.model.OrderInfo;
import com.cskaoyan.mall.order.query.OrderInfoParam;
import com.cskaoyan.mall.order.service.OrderService;
import com.cskaoyan.mall.pay.api.dto.PaymentInfoDTO;
import com.cskaoyan.mall.ware.api.constant.TaskStatus;
import com.cskaoyan.mall.ware.api.dto.WareOrderTaskDTO;
import com.cskaoyan.mall.ware.api.dto.WareSkuDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    ProductApiClient productApiClient;

    @Autowired
    CartApiClient cartApiClient;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    OrderDetailMapper orderDetailMapper;

    @Autowired
    BaseProducer baseProducer;

    @Autowired
    OrderInfoConverter orderInfoConverter;

    @Autowired
    WareApiClient wareApiClient;

    @Autowired
    OrderDetailConverter detailConverter;

    @Autowired
    PayApiClient payApiClient;

    @Autowired
    OrderDetailConverter orderDetailConverter;


    /**
     * 提交订单: 校验价格
     */
    @Override
    public Boolean checkPrice(Long skuId, BigDecimal skuPrice) {

        BigDecimal nowPrice = productApiClient.getSkuPrice(skuId);
        if (nowPrice.compareTo(skuPrice) != 0) {
            return true;
        }
        // 相等
        return false;
    }

    /**
     * 提交订单: 更新用户购物车商品价格
     */
    @Override
    public void refreshPrice(Long skuId, String userId) {

    }


    /**
     * 提交订单: 保存订单以及订单详情
     */
    @Override
    @Transactional
    public Long saveOrderInfo(OrderInfo orderInfo) {
        // 设置一些订单数据
        buildOrderInfo(orderInfo);

        // 保存订单信息(order_info，order_detail)

        // 删除购物车中已经下单的商品 (自己实现 购物车服务中处理该服务调用请求的方法)
        List<Long> skuIds = orderInfo.getOrderDetailList().stream()
                .map(orderDetail -> orderDetail.getSkuId()).collect(Collectors.toList());
        cartApiClient.removeCartProductsInOrder(orderInfo.getUserId().toString(), skuIds);


        // 发送延迟消息
        baseProducer.sendDelayMessage(MqTopicConst.DELAY_ORDER_TOPIC, orderInfo.getId(), MqTopicConst.DELAY_ORDER_LEVEL);

        // 返回订单id
        return orderInfo.getId();
    }

    @Transactional
    @Override
    public Long saveSeckillOrder(OrderInfoParam orderInfoParam) {
       return null;
    }



    /**
     * 提交订单: 根据订单id获取订单信息
     */
    @Override
    public OrderInfoDTO getOrderInfo(Long orderId) {
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        if (orderInfo == null) return null;
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);

        List<OrderDetail> orderDetails = orderDetailMapper.selectList(queryWrapper);
        orderInfo.setOrderDetailList(orderDetails);

        // 转化
        OrderInfoDTO orderInfoDTO = orderInfoConverter.convertOrderInfoToOrderInfoDTO(orderInfo);

        return orderInfoDTO;
    }


    /**
     * 我的订单：获取《我的订单》 列表
     */
    @Override
    public IPage<OrderInfoDTO> getPage(Page<OrderInfoDTO> pageParam, String userId) {

        IPage<OrderInfoDTO> orderInfoDTOIPage = orderInfoMapper.selectPageByUserId(pageParam, userId);

        orderInfoDTOIPage.getRecords().forEach(orderInfoDTO -> {
            // 获取订单状态对应的中文描述字符串，设置orderInfoDTO的orderStatusName
            orderInfoDTO.setOrderStatusName(OrderStatus.getStatusDescByStatus(orderInfoDTO.getOrderStatus()));
        });

        return orderInfoDTOIPage;
    }


    private void buildOrderInfo(OrderInfo orderInfo) {

        // 计算总金额
        orderInfo.sumTotalAmount();

        // 订单状态
        orderInfo.setOrderStatus(OrderStatus.UNPAID.name());


        // 第三方订单编号
        String outTradeNo = "CSKAOYAN" + System.currentTimeMillis() + new Random().nextInt(1000);
        // 订单号
        orderInfo.setOutTradeNo(outTradeNo);

        // 获取订单明细
        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
        StringBuffer stringBuffer = new StringBuffer();
        for (OrderDetail orderDetail : orderDetailList) {
            String skuName = orderDetail.getSkuName() + "  ";
            stringBuffer.append(skuName);
        }
        String tradeBody = stringBuffer.toString().length() > 100 ? stringBuffer.toString().substring(0, 100) : stringBuffer.toString();
        orderInfo.setTradeBody(tradeBody);
    }


    /**
     * 支付回调，支付成功，修改订单状态
     */
    @Override
    public void successPay(Long orderId) {


    }


    /**
     * 支付回调：库存扣减完成，修改订单状态
     */
    @Override
    public void successLockStock(String orderId, String taskStatus) {

    }


    /**
     * 支付回调：拆单
     */
    @Override
    public List<WareOrderTaskDTO> orderSplit(String orderId, List<WareSkuDTO> wareSkuDTOList) {
        return null;
    }


    /**
     * 订单超时取消
     */
    @Override
// 处理超时订单
    public void execExpiredOrder(Long orderId) {

    }
}
