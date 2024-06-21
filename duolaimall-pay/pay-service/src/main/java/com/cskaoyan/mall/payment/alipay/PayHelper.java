package com.cskaoyan.mall.payment.alipay;

import com.cskaoyan.mall.order.dto.OrderInfoDTO;

public interface PayHelper {

    /*
         预下单方法，获取交易二维码字符串，或者交易表单字符串
     */
    String prePay(OrderInfoDTO orderInfo);


    /*
         根据订单号查询订单交易状态
     */
    String queryTradeStatus(String outTradeNo);


    /*
         根据订单编号关闭订单交易
     */
    void closeTrade(String outTradeNo);

}
