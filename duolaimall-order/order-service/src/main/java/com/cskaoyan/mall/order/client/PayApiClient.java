package com.cskaoyan.mall.order.client;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.pay.api.dto.PaymentInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-payment")
public interface PayApiClient {

    /**
     * 根据外部订单号 查询支付记录
     */
    @GetMapping("/api/payment/inner/getPaymentInfoByOutTradeNo/{outTradeNo}")
    PaymentInfoDTO getPaymentInfoDTOByOutTradeNo(@PathVariable(value = "outTradeNo") String outTradeNo);

    /**
     * 根据外部订单号 查询支付宝支付状态
     */
    @GetMapping("/api/payment/inner/getAlipayInfo/{outTradeNo}")
    Result getAlipayInfo(@PathVariable(value = "outTradeNo") String outTradeNo);

    /**
     * 关闭支付宝支付记录
     */
    @GetMapping("/api/payment/inner/closeAlipay/{outTradeNo}")
    Result closeAlipay(@PathVariable(value = "outTradeNo") String outTradeNo);

    /**
     * 修改paymentInfo为已关闭
     */
    @GetMapping("/api/payment/inner/closePaymentInfo/{outTradeNo}")
    Result closePaymentInfo(@PathVariable(value = "outTradeNo") String outTradeNo);


}
