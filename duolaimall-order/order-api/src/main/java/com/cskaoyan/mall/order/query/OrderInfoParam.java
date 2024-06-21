package com.cskaoyan.mall.order.query;

import lombok.Data;

import java.util.List;


@Data
public class OrderInfoParam {

    //"用户id")
    Long userId;

    // "收货人"
    String consignee;

    // "联系方式"
    String consigneeTel;
    // "收货地址"
    String deliveryAddress;
    // "订单评论，暂时不需要"
    String orderComment;

    String paymentWay = "ONLINE";
    // "订单明细列表"
    List<OrderDetailParam> orderDetailList;
}
