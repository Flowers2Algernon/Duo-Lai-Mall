package com.cskaoyan.mall.order.query;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailParam {

    Long id;

    Long orderId;

    Long skuId;

    String skuName;

    String imgUrl;

    BigDecimal orderPrice;

    private Integer skuNum;

}
