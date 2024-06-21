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

        return null;
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
        return null;
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
        return null;
    }


    /**
     * 我的订单：获取《我的订单》 列表
     */
    @Override
    public IPage<OrderInfoDTO> getPage(Page<OrderInfoDTO> pageParam, String userId) {
        //此处根据page对象和用户id获取对应用户的订单数据--指定页面
        IPage<OrderInfoDTO> orderInfoDTOIPage = orderInfoMapper.selectPageByUserId(pageParam, userId);
        //查询结果中需要根据每个订单的Orderstatus来设置相应的中文orderstatusName到具体的返回值中
        for (OrderInfoDTO record : orderInfoDTOIPage.getRecords()) {
            record.setOrderStatusName(OrderStatus.getStatusDescByStatus(record.getOrderStatus()));
        }
        //设置的中文状态无需保存到数据库中
        return orderInfoDTOIPage;

    }


    private void buildOrderInfo(OrderInfo orderInfo) {


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
