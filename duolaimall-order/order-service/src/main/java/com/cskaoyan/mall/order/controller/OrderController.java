package com.cskaoyan.mall.order.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.cart.api.dto.CartInfoDTO;
import com.cskaoyan.mall.common.constant.ResultCodeEnum;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.common.util.AuthContext;
import com.cskaoyan.mall.order.client.CartApiClient;
import com.cskaoyan.mall.order.client.UserApiClient;
import com.cskaoyan.mall.order.client.WareApiClient;
import com.cskaoyan.mall.order.converter.CartInfoConverter;
import com.cskaoyan.mall.order.converter.OrderInfoConverter;
import com.cskaoyan.mall.order.dto.OrderDetailDTO;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.order.dto.OrderTradeDTO;
import com.cskaoyan.mall.order.model.OrderInfo;
import com.cskaoyan.mall.order.query.OrderDetailParam;
import com.cskaoyan.mall.order.query.OrderInfoParam;
import com.cskaoyan.mall.order.service.OrderService;
import com.cskaoyan.mall.user.dto.UserAddressDTO;
import io.netty.handler.codec.mqtt.MqttReasonCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    UserApiClient userApiClient;

    @Autowired
    CartApiClient cartApiClient;

    @Autowired
    CartInfoConverter cartInfoConverter;

    @Autowired
    OrderService orderService;
    @Autowired
    WareApiClient wareApiClient;

    @Autowired
    OrderInfoConverter orderInfoConverter;


    /**
     * 确认订单
     * 获取结算页信息
     */
    @GetMapping("/order/auth/trade")
    public Result<OrderTradeDTO> getTradeInfo(HttpServletRequest request){
        // 获取用户的userId
        String userId = AuthContext.getUserId(request);

        // 1. 获取用户的地址列表信息 (自己在用户服务中实现，该服务调用请求的处理)
        List<UserAddressDTO> addressList = userApiClient.findUserAddressListByUserId(userId);

        // 2. 获取待下单的商品列表
        List<CartInfoDTO> cartCheckedList = cartApiClient.getCartCheckedList(userId);
        // 待下单的购物车商品list ——> 订单明细列表
        List<OrderDetailDTO> orderDetailDTOS
                = cartInfoConverter.convertCartInfoDTOToOrderDetailDTOList(cartCheckedList);


        // 3. 计算待下单的商品总数量
        int total = cartCheckedList.stream().mapToInt(cartInfo -> cartInfo.getSkuNum()).sum();

        // 4. 计算总金额
        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
        orderInfoDTO.setOrderDetailList(orderDetailDTOS);
        // 利用OrderInfoDTO的sumTotalAmount方法计算订单总金额
        orderInfoDTO.sumTotalAmount();
        // 获取到订单总金额
        BigDecimal totalAmount = orderInfoDTO.getTotalAmount();

        // 封装包含详情页数据的OrderTradeDTO对象
        OrderTradeDTO orderTradeDTO = new OrderTradeDTO();
        orderTradeDTO.setUserAddressList(addressList);
        orderTradeDTO.setDetailArrayList(orderDetailDTOS);
        orderTradeDTO.setTotalNum(total);
        orderTradeDTO.setTotalAmount(totalAmount);

        return Result.ok(orderTradeDTO);
    }

    /**
     * 我的订单：获取我的订单列表
     */
    @GetMapping("/order/auth/{page}/{limit}")
    public Result<IPage<OrderInfoDTO>> index(@PathVariable Long page, @PathVariable Long limit, HttpServletRequest request) {


        // 1. 获取用户userId
        String userId = AuthContext.getUserId(request);

        // 2. 构造分页参数
        Page<OrderInfoDTO> pageParam = new Page<>(page, limit);

        IPage<OrderInfoDTO> orderInfoPage = orderService.getPage(pageParam, userId);

        return Result.ok(orderInfoPage);
    }

    /**
     * 提交订单
     */
    @PostMapping("/order/auth/submitOrder")
    public Result<Long> submitOrder(@RequestBody OrderInfoParam orderInfoParam, HttpServletRequest request) {

        // 1. 获取user_id
        String userId = AuthContext.getUserId(request);
        orderInfoParam.setUserId(Long.parseLong(userId));

        // 2. 校验库存
        List<OrderDetailParam> orderDetailList = orderInfoParam.getOrderDetailList();
        // 遍历订单商品列表中的每个商品
        for (OrderDetailParam detailParam :orderDetailList) {
            Result result = wareApiClient.hasStock(detailParam.getSkuId(), detailParam.getSkuNum());
            if (!ResultCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
               // 说明该商品库存不足
               return Result.fail(0L).message("对不起，" + detailParam.getSkuName() + "库存不足!");
            }
        }
        //3. 校验价格
        for (OrderDetailParam detailParam :orderDetailList) {
           // 判断每个商品的价格
            Boolean isChanged = orderService.checkPrice(detailParam.getSkuId(), detailParam.getOrderPrice());
            if (isChanged) {
                // 更新购物车商品价格 (自己去购物车服务中实现该服务调用请求的处理方法)
                cartApiClient.refreshCartPrice(userId, detailParam.getSkuId());
                return Result.fail(0L).message("对不起，" + detailParam.getSkuName() + "发生变化，请重新下单!");
            }
        }


        //4. 开始真正的下单工作
        OrderInfo orderInfo = orderInfoConverter.convertOrderInfoParam(orderInfoParam);
        Long orderId = orderService.saveOrderInfo(orderInfo);
        // 返回订单id
        return Result.ok(orderId);
    }
}
