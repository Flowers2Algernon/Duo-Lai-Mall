package com.cskaoyan.mall.order.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.order.client.CartApiClient;
import com.cskaoyan.mall.order.client.UserApiClient;
import com.cskaoyan.mall.order.converter.CartInfoConverter;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.order.query.OrderInfoParam;
import com.cskaoyan.mall.order.service.OrderService;
import com.cskaoyan.mall.ware.api.dto.WareOrderTaskDTO;
import com.cskaoyan.mall.ware.api.dto.WareSkuDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/api/order")
public class OrderApiController {

    @Autowired
    UserApiClient userApiClient;

    @Autowired
    CartApiClient cartApiClient;


    @Autowired
    OrderService orderService;

    @Autowired
    CartInfoConverter cartInfoConverter;




    /**
     * 根据Id获取订单信息
     */
    @GetMapping("inner/getOrderInfo/{orderId}")
    public OrderInfoDTO getOrderInfoDTO(@PathVariable(value = "orderId") Long orderId){
        return orderService.getOrderInfo(orderId);
    }
}
