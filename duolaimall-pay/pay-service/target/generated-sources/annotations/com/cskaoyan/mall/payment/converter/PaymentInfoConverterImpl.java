package com.cskaoyan.mall.payment.converter;

import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.pay.api.dto.PaymentInfoDTO;
import com.cskaoyan.mall.payment.model.PaymentInfo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-21T15:34:52+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class PaymentInfoConverterImpl implements PaymentInfoConverter {

    @Override
    public PaymentInfo contvertOrderInfoDTO2PaymentInfo(OrderInfoDTO orderInfoDTO) {
        if ( orderInfoDTO == null ) {
            return null;
        }

        PaymentInfo paymentInfo = new PaymentInfo();

        paymentInfo.setOrderId( orderInfoDTO.getId() );
        paymentInfo.setSubject( orderInfoDTO.getTradeBody() );
        paymentInfo.setTotalAmount( orderInfoDTO.getTotalAmount() );
        paymentInfo.setUserId( orderInfoDTO.getUserId() );
        paymentInfo.setOutTradeNo( orderInfoDTO.getOutTradeNo() );
        paymentInfo.setId( orderInfoDTO.getId() );
        paymentInfo.setUpdateTime( orderInfoDTO.getUpdateTime() );
        paymentInfo.setCreateTime( orderInfoDTO.getCreateTime() );

        return paymentInfo;
    }

    @Override
    public PaymentInfoDTO convertPaymentInfoToDTO(PaymentInfo paymentInfo) {
        if ( paymentInfo == null ) {
            return null;
        }

        PaymentInfoDTO paymentInfoDTO = new PaymentInfoDTO();

        paymentInfoDTO.setId( paymentInfo.getId() );
        paymentInfoDTO.setOutTradeNo( paymentInfo.getOutTradeNo() );
        paymentInfoDTO.setOrderId( paymentInfo.getOrderId() );
        paymentInfoDTO.setUserId( paymentInfo.getUserId() );
        paymentInfoDTO.setPaymentType( paymentInfo.getPaymentType() );
        paymentInfoDTO.setTradeNo( paymentInfo.getTradeNo() );
        paymentInfoDTO.setTotalAmount( paymentInfo.getTotalAmount() );
        paymentInfoDTO.setSubject( paymentInfo.getSubject() );
        paymentInfoDTO.setPaymentStatus( paymentInfo.getPaymentStatus() );
        paymentInfoDTO.setCreateTime( paymentInfo.getCreateTime() );
        paymentInfoDTO.setCallbackTime( paymentInfo.getCallbackTime() );
        paymentInfoDTO.setCallbackContent( paymentInfo.getCallbackContent() );

        return paymentInfoDTO;
    }
}
