package com.cskaoyan.mall.payment.alipay.factory;

import com.cskaoyan.mall.payment.alipay.AlipayHelper;
import com.cskaoyan.mall.payment.alipay.PayHelper;
import com.cskaoyan.mall.payment.constant.PaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimplePayHelperFactory implements PayHelperFactory{

    @Autowired
    AlipayHelper alipayHelper;

    @Override
    public PayHelper getPayHelper(PaymentType paymentType) {
        if (PaymentType.ALIPAY.equals(paymentType)) {
            return alipayHelper;
        }

        return null;
    }
}
