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
import org.apache.http.auth.AUTH;
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
        //创建返回对象
        OrderTradeDTO orderTradeDTO = new OrderTradeDTO();
        //先获取用户id
        String userId = AuthContext.getUserId(request);
        //需要实现的有四部分

        //2. 获取用户地址--独立模块
        List<UserAddressDTO> userAddressListByUserId = userApiClient.findUserAddressListByUserId(userId);
        orderTradeDTO.setUserAddressList(userAddressListByUserId);
        //3. 获取商品数量
        //首先获取购物车中选中的全部的商品列表
        List<CartInfoDTO> cartCheckedList = cartApiClient.getCartCheckedList(userId);
        List<OrderDetailDTO> orderDetailDTOS = cartInfoConverter.convertCartInfoDTOToOrderDetailDTOList(cartCheckedList);
        int sum =0;
        for (CartInfoDTO cartInfoDTO : cartCheckedList) {
           if (cartInfoDTO!=null && cartInfoDTO.getSkuNum()!=null)
            sum += cartInfoDTO.getSkuNum();
        }
        orderTradeDTO.setTotalNum(sum);
        //1. 全部的订单价格
        //可以利用现成的接口来实现--利用orderInfoDTO的getTotalMount来实现
        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
        orderInfoDTO.setOrderDetailList(orderDetailDTOS);
        BigDecimal totalAmount = orderInfoDTO.getTotalAmount();
        orderTradeDTO.setTotalAmount(totalAmount);
        //4. 详细的商品信息
        orderTradeDTO.setDetailArrayList(orderDetailDTOS);

        return Result.ok(orderTradeDTO);
    }

    /**
     * 我的订单：获取我的订单列表
     */
    @GetMapping("/order/auth/{page}/{limit}")
    public Result<IPage<OrderInfoDTO>> index(@PathVariable Long page, @PathVariable Long limit, HttpServletRequest request) {

        //首先获取用户的id
        String userId = AuthContext.getUserId(request);
        //返回对应page的商品数据
        //构造page对象
        Page<OrderInfoDTO> objectPage = new Page<>(page, limit);
        IPage<OrderInfoDTO> orderServicePage = orderService.getPage(objectPage,userId);
        return Result.ok(orderServicePage);
    }

    /**
     * 提交订单
     */
    @PostMapping("/order/auth/submitOrder")
    public Result<Long> submitOrder(@RequestBody OrderInfoParam orderInfoParam, HttpServletRequest request) {

        return null;
    }
}
