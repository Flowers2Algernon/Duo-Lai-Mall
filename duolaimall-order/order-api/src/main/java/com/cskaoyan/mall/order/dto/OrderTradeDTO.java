package com.cskaoyan.mall.order.dto;

import com.cskaoyan.mall.user.dto.UserAddressDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderTradeDTO {

    // TODO 修改 userAddressList
    // "用户地址列表"
    List<UserAddressDTO> userAddressList;
    // "订单明细列表"
    List<OrderDetailDTO> detailArrayList;
   // "订单条目数量"
    Integer totalNum;
    // "总金额"
    BigDecimal totalAmount;
}
