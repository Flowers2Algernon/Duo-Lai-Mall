package com.cskaoyan.mall.payment.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.cskaoyan.mall.common.constant.RedisConst;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.pay.api.dto.PaymentInfoDTO;
import com.cskaoyan.mall.payment.alipay.CsmallAlipayConfig;
import com.cskaoyan.mall.payment.client.OrderApiClient;
import com.cskaoyan.mall.payment.constant.PaymentType;
import com.cskaoyan.mall.payment.service.PayService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by 北海 on 2023-05-19 15:26
 */
@Slf4j
@Controller
public class PayController {

    @Autowired
    PayService payService;

    @Autowired
    OrderApiClient orderApiClient;

    @Autowired
    CsmallAlipayConfig alipayConfig;

    @Autowired
    RedissonClient redissonClient;

    @GetMapping("/pay/auth")
    @ResponseBody
    public Result<OrderInfoDTO> payIndex(Long orderId){
        log.info("enter {} for {}", PayController.class.getSimpleName(), "index");
        OrderInfoDTO orderInfoDTO = orderApiClient.getOrderInfoDTO(orderId);
        log.info("before render template {} for {}", "payment/pay", "payIndex");
        return Result.ok(orderInfoDTO);
    }

    //支付宝支付，获取支付表单
    @GetMapping("/pay/alipay/submit/{orderId}")
    @ResponseBody
    public String submitOrder(@PathVariable long orderId){
        String form = payService.createAliPay(orderId);
        return form;
    }

    //同步回调
    @GetMapping("/pay/alipay/callback/return")
    public String callBack(){
        log.info("支付成功，同步回调");
        return "redirect:"+alipayConfig.getReturnOrderUrl();
    }

    //异步回调

}
