package com.cskaoyan.mall.payment.constant;

import lombok.Data;
import lombok.Getter;

@Getter
public enum PaymentType {

    ALIPAY("支付宝"),
    WEIXIN("微信" );

    private String comment ;


    PaymentType(String comment ){
        this.comment=comment;
    }
}
