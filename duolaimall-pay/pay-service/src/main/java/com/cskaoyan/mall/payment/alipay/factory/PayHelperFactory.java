package com.cskaoyan.mall.payment.alipay.factory;

import com.cskaoyan.mall.payment.alipay.PayHelper;
import com.cskaoyan.mall.payment.constant.PaymentType;

public interface PayHelperFactory {

    PayHelper getPayHelper(PaymentType paymentType);
}
