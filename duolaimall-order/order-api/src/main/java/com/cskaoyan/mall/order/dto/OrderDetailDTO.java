package com.cskaoyan.mall.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDetailDTO {
    //"订单条目Id"
    Long id;
    //"订单Id"
    Long orderId;
    //"订单条目商品skuId"
    Long skuId;
    //"订单条目商品名称"
    String skuName;
    //"订单条目商品图片url"
    String imgUrl;
    //"订单条目商品价格"
    BigDecimal orderPrice;
    //"订单条目商品价格"
    Integer skuNum;

    //"订单条目创建时间"
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    Date createTime;
    //"订单条目更新时间"
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    Date updateTime;
}
