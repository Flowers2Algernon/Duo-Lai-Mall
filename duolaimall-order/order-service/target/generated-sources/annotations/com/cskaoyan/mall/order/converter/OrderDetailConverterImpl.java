package com.cskaoyan.mall.order.converter;

import com.cskaoyan.mall.order.dto.OrderDetailDTO;
import com.cskaoyan.mall.order.model.OrderDetail;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-19T21:43:21+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class OrderDetailConverterImpl implements OrderDetailConverter {

    @Override
    public OrderDetailDTO convertOrderDetailToDTO(OrderDetail orderDetail) {
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

    @Override
    public OrderDetail convertOrderDetailToDTO(OrderDetailDTO detailDTO) {
        if ( detailDTO == null ) {
            return null;
        }

        OrderDetail orderDetail = new OrderDetail();

        orderDetail.setId( detailDTO.getId() );
        orderDetail.setCreateTime( detailDTO.getCreateTime() );
        orderDetail.setUpdateTime( detailDTO.getUpdateTime() );
        orderDetail.setOrderId( detailDTO.getOrderId() );
        orderDetail.setSkuId( detailDTO.getSkuId() );
        orderDetail.setSkuName( detailDTO.getSkuName() );
        orderDetail.setImgUrl( detailDTO.getImgUrl() );
        orderDetail.setOrderPrice( detailDTO.getOrderPrice() );
        orderDetail.setSkuNum( detailDTO.getSkuNum() );

        return orderDetail;
    }

    @Override
    public List<OrderDetail> convertOrderDetailDTOsToDOs(List<OrderDetailDTO> detailDTO) {
        if ( detailDTO == null ) {
            return null;
        }

        List<OrderDetail> list = new ArrayList<OrderDetail>( detailDTO.size() );
        for ( OrderDetailDTO orderDetailDTO : detailDTO ) {
            list.add( convertOrderDetailToDTO( orderDetailDTO ) );
        }

        return list;
    }
}
