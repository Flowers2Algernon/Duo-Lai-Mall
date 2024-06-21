package com.cskaoyan.mall.order.converter;

import com.cskaoyan.mall.order.dto.OrderDetailDTO;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.order.model.OrderDetail;
import com.cskaoyan.mall.order.model.OrderInfo;
import com.cskaoyan.mall.order.query.OrderDetailParam;
import com.cskaoyan.mall.order.query.OrderInfoParam;
import com.cskaoyan.mall.ware.api.dto.WareOrderTaskDTO;
import com.cskaoyan.mall.ware.api.dto.WareOrderTaskDetailDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-21T15:37:07+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class OrderInfoConverterImpl implements OrderInfoConverter {

    @Override
    public OrderInfoDTO convertOrderInfoToOrderInfoDTO(OrderInfo orderInfo) {
        if ( orderInfo == null ) {
            return null;
        }

        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();

        orderInfoDTO.setId( orderInfo.getId() );
        orderInfoDTO.setParentOrderId( orderInfo.getParentOrderId() );
        orderInfoDTO.setOrderStatus( orderInfo.getOrderStatus() );
        orderInfoDTO.setUserId( orderInfo.getUserId() );
        orderInfoDTO.setPaymentWay( orderInfo.getPaymentWay() );
        orderInfoDTO.setConsignee( orderInfo.getConsignee() );
        orderInfoDTO.setConsigneeTel( orderInfo.getConsigneeTel() );
        orderInfoDTO.setDeliveryAddress( orderInfo.getDeliveryAddress() );
        orderInfoDTO.setTotalAmount( orderInfo.getTotalAmount() );
        orderInfoDTO.setOriginalTotalAmount( orderInfo.getOriginalTotalAmount() );
        orderInfoDTO.setOrderComment( orderInfo.getOrderComment() );
        orderInfoDTO.setOutTradeNo( orderInfo.getOutTradeNo() );
        orderInfoDTO.setTradeBody( orderInfo.getTradeBody() );
        orderInfoDTO.setOrderType( orderInfo.getOrderType() );
        orderInfoDTO.setTrackingNo( orderInfo.getTrackingNo() );
        orderInfoDTO.setRefundableTime( orderInfo.getRefundableTime() );
        orderInfoDTO.setCreateTime( orderInfo.getCreateTime() );
        orderInfoDTO.setUpdateTime( orderInfo.getUpdateTime() );
        orderInfoDTO.setExpireTime( orderInfo.getExpireTime() );
        orderInfoDTO.setOrderDetailList( orderDetailListToOrderDetailDTOList( orderInfo.getOrderDetailList() ) );
        orderInfoDTO.setWareId( orderInfo.getWareId() );

        return orderInfoDTO;
    }

    @Override
    public OrderInfo convertOrderInfoParam(OrderInfoParam orderInfoParam) {
        if ( orderInfoParam == null ) {
            return null;
        }

        OrderInfo orderInfo = new OrderInfo();

        orderInfo.setUserId( orderInfoParam.getUserId() );
        orderInfo.setPaymentWay( orderInfoParam.getPaymentWay() );
        orderInfo.setConsignee( orderInfoParam.getConsignee() );
        orderInfo.setConsigneeTel( orderInfoParam.getConsigneeTel() );
        orderInfo.setDeliveryAddress( orderInfoParam.getDeliveryAddress() );
        orderInfo.setOrderComment( orderInfoParam.getOrderComment() );
        orderInfo.setOrderDetailList( orderDetailParamListToOrderDetailList( orderInfoParam.getOrderDetailList() ) );

        return orderInfo;
    }

    @Override
    public OrderDetail convertOrderDetailParam(OrderDetailParam orderDetailParam) {
        if ( orderDetailParam == null ) {
            return null;
        }

        OrderDetail orderDetail = new OrderDetail();

        orderDetail.setId( orderDetailParam.getId() );
        orderDetail.setOrderId( orderDetailParam.getOrderId() );
        orderDetail.setSkuId( orderDetailParam.getSkuId() );
        orderDetail.setSkuName( orderDetailParam.getSkuName() );
        orderDetail.setImgUrl( orderDetailParam.getImgUrl() );
        orderDetail.setOrderPrice( orderDetailParam.getOrderPrice() );
        orderDetail.setSkuNum( orderDetailParam.getSkuNum() );

        return orderDetail;
    }

    @Override
    public WareOrderTaskDTO convertOrderInfoToWareOrderTaskDTO(OrderInfo orderInfo) {
        if ( orderInfo == null ) {
            return null;
        }

        WareOrderTaskDTO wareOrderTaskDTO = new WareOrderTaskDTO();

        if ( orderInfo.getId() != null ) {
            wareOrderTaskDTO.setOrderId( String.valueOf( orderInfo.getId() ) );
        }
        wareOrderTaskDTO.setOrderBody( orderInfo.getTradeBody() );
        wareOrderTaskDTO.setDetails( orderDetailListToWareOrderTaskDetailDTOList( orderInfo.getOrderDetailList() ) );
        wareOrderTaskDTO.setConsignee( orderInfo.getConsignee() );
        wareOrderTaskDTO.setConsigneeTel( orderInfo.getConsigneeTel() );
        wareOrderTaskDTO.setDeliveryAddress( orderInfo.getDeliveryAddress() );
        wareOrderTaskDTO.setOrderComment( orderInfo.getOrderComment() );
        wareOrderTaskDTO.setPaymentWay( orderInfo.getPaymentWay() );
        wareOrderTaskDTO.setTrackingNo( orderInfo.getTrackingNo() );
        wareOrderTaskDTO.setWareId( orderInfo.getWareId() );

        return wareOrderTaskDTO;
    }

    @Override
    public WareOrderTaskDetailDTO convertDetail(OrderDetail orderDetail) {
        if ( orderDetail == null ) {
            return null;
        }

        WareOrderTaskDetailDTO wareOrderTaskDetailDTO = new WareOrderTaskDetailDTO();

        if ( orderDetail.getSkuId() != null ) {
            wareOrderTaskDetailDTO.setSkuId( String.valueOf( orderDetail.getSkuId() ) );
        }
        wareOrderTaskDetailDTO.setSkuName( orderDetail.getSkuName() );
        wareOrderTaskDetailDTO.setSkuNum( orderDetail.getSkuNum() );

        return wareOrderTaskDetailDTO;
    }

    @Override
    public OrderInfo copyOrderInfo(OrderInfoDTO orderInfo) {
        if ( orderInfo == null ) {
            return null;
        }

        OrderInfo orderInfo1 = new OrderInfo();

        orderInfo1.setId( orderInfo.getId() );
        orderInfo1.setCreateTime( orderInfo.getCreateTime() );
        orderInfo1.setUpdateTime( orderInfo.getUpdateTime() );
        orderInfo1.setParentOrderId( orderInfo.getParentOrderId() );
        orderInfo1.setOrderStatus( orderInfo.getOrderStatus() );
        orderInfo1.setUserId( orderInfo.getUserId() );
        orderInfo1.setPaymentWay( orderInfo.getPaymentWay() );
        orderInfo1.setConsignee( orderInfo.getConsignee() );
        orderInfo1.setConsigneeTel( orderInfo.getConsigneeTel() );
        orderInfo1.setDeliveryAddress( orderInfo.getDeliveryAddress() );
        orderInfo1.setTotalAmount( orderInfo.getTotalAmount() );
        orderInfo1.setOriginalTotalAmount( orderInfo.getOriginalTotalAmount() );
        orderInfo1.setOrderComment( orderInfo.getOrderComment() );
        orderInfo1.setOutTradeNo( orderInfo.getOutTradeNo() );
        orderInfo1.setTradeBody( orderInfo.getTradeBody() );
        orderInfo1.setOrderType( orderInfo.getOrderType() );
        orderInfo1.setTrackingNo( orderInfo.getTrackingNo() );
        orderInfo1.setRefundableTime( orderInfo.getRefundableTime() );
        orderInfo1.setExpireTime( orderInfo.getExpireTime() );
        orderInfo1.setOrderDetailList( orderDetailDTOListToOrderDetailList( orderInfo.getOrderDetailList() ) );
        orderInfo1.setWareId( orderInfo.getWareId() );

        return orderInfo1;
    }

    protected OrderDetailDTO orderDetailToOrderDetailDTO(OrderDetail orderDetail) {
        if ( orderDetail == null ) {
            return null;
        }

        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();

        orderDetailDTO.setId( orderDetail.getId() );
        orderDetailDTO.setOrderId( orderDetail.getOrderId() );
        orderDetailDTO.setSkuId( orderDetail.getSkuId() );
        orderDetailDTO.setSkuName( orderDetail.getSkuName() );
        orderDetailDTO.setImgUrl( orderDetail.getImgUrl() );
        orderDetailDTO.setOrderPrice( orderDetail.getOrderPrice() );
        orderDetailDTO.setSkuNum( orderDetail.getSkuNum() );
        orderDetailDTO.setCreateTime( orderDetail.getCreateTime() );
        orderDetailDTO.setUpdateTime( orderDetail.getUpdateTime() );

        return orderDetailDTO;
    }

    protected List<OrderDetailDTO> orderDetailListToOrderDetailDTOList(List<OrderDetail> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderDetailDTO> list1 = new ArrayList<OrderDetailDTO>( list.size() );
        for ( OrderDetail orderDetail : list ) {
            list1.add( orderDetailToOrderDetailDTO( orderDetail ) );
        }

        return list1;
    }

    protected List<OrderDetail> orderDetailParamListToOrderDetailList(List<OrderDetailParam> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderDetail> list1 = new ArrayList<OrderDetail>( list.size() );
        for ( OrderDetailParam orderDetailParam : list ) {
            list1.add( convertOrderDetailParam( orderDetailParam ) );
        }

        return list1;
    }

    protected List<WareOrderTaskDetailDTO> orderDetailListToWareOrderTaskDetailDTOList(List<OrderDetail> list) {
        if ( list == null ) {
            return null;
        }

        List<WareOrderTaskDetailDTO> list1 = new ArrayList<WareOrderTaskDetailDTO>( list.size() );
        for ( OrderDetail orderDetail : list ) {
            list1.add( convertDetail( orderDetail ) );
        }

        return list1;
    }

    protected OrderDetail orderDetailDTOToOrderDetail(OrderDetailDTO orderDetailDTO) {
        if ( orderDetailDTO == null ) {
            return null;
        }

        OrderDetail orderDetail = new OrderDetail();

        orderDetail.setId( orderDetailDTO.getId() );
        orderDetail.setCreateTime( orderDetailDTO.getCreateTime() );
        orderDetail.setUpdateTime( orderDetailDTO.getUpdateTime() );
        orderDetail.setOrderId( orderDetailDTO.getOrderId() );
        orderDetail.setSkuId( orderDetailDTO.getSkuId() );
        orderDetail.setSkuName( orderDetailDTO.getSkuName() );
        orderDetail.setImgUrl( orderDetailDTO.getImgUrl() );
        orderDetail.setOrderPrice( orderDetailDTO.getOrderPrice() );
        orderDetail.setSkuNum( orderDetailDTO.getSkuNum() );

        return orderDetail;
    }

    protected List<OrderDetail> orderDetailDTOListToOrderDetailList(List<OrderDetailDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderDetail> list1 = new ArrayList<OrderDetail>( list.size() );
        for ( OrderDetailDTO orderDetailDTO : list ) {
            list1.add( orderDetailDTOToOrderDetail( orderDetailDTO ) );
        }

        return list1;
    }
}
