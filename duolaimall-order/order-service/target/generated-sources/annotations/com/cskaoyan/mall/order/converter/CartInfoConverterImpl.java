package com.cskaoyan.mall.order.converter;

import com.cskaoyan.mall.cart.api.dto.CartInfoDTO;
import com.cskaoyan.mall.order.dto.OrderDetailDTO;
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
public class CartInfoConverterImpl implements CartInfoConverter {

    @Override
    public OrderDetailDTO convertCartInfoDTOToOrderDetailDTO(CartInfoDTO cartInfoDTO) {
        if ( cartInfoDTO == null ) {
            return null;
        }

        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();

        orderDetailDTO.setOrderPrice( cartInfoDTO.getSkuPrice() );
        orderDetailDTO.setSkuId( cartInfoDTO.getSkuId() );
        orderDetailDTO.setSkuName( cartInfoDTO.getSkuName() );
        orderDetailDTO.setImgUrl( cartInfoDTO.getImgUrl() );
        orderDetailDTO.setSkuNum( cartInfoDTO.getSkuNum() );
        orderDetailDTO.setCreateTime( cartInfoDTO.getCreateTime() );
        orderDetailDTO.setUpdateTime( cartInfoDTO.getUpdateTime() );

        return orderDetailDTO;
    }

    @Override
    public List<OrderDetailDTO> convertCartInfoDTOToOrderDetailDTOList(List<CartInfoDTO> cartInfoDTOs) {
        if ( cartInfoDTOs == null ) {
            return null;
        }

        List<OrderDetailDTO> list = new ArrayList<OrderDetailDTO>( cartInfoDTOs.size() );
        for ( CartInfoDTO cartInfoDTO : cartInfoDTOs ) {
            list.add( convertCartInfoDTOToOrderDetailDTO( cartInfoDTO ) );
        }

        return list;
    }
}
