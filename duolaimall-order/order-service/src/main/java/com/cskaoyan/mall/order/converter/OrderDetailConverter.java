package com.cskaoyan.mall.order.converter;

import com.cskaoyan.mall.order.dto.OrderDetailDTO;
import com.cskaoyan.mall.order.model.OrderDetail;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 创建日期: 2023/03/16 17:37
 *
 * @author ciggar
 */
@Mapper(componentModel = "spring")
public interface OrderDetailConverter {

    OrderDetailDTO convertOrderDetailToDTO(OrderDetail orderDetail);

    OrderDetail convertOrderDetailToDTO(OrderDetailDTO detailDTO);

    List<OrderDetail> convertOrderDetailDTOsToDOs(List<OrderDetailDTO> detailDTO);
}
