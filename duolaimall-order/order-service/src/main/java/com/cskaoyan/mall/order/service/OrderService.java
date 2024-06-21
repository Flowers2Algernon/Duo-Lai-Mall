package com.cskaoyan.mall.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.order.model.OrderInfo;
import com.cskaoyan.mall.order.query.OrderInfoParam;
import com.cskaoyan.mall.ware.api.dto.WareOrderTaskDTO;
import com.cskaoyan.mall.ware.api.dto.WareSkuDTO;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {


    /**
     * 提交订单: 校验价格
     */
    Boolean checkPrice(Long skuId, BigDecimal skuPrice);

    /**
     * 提交订单: 更新用户购物车中商品价格
     */
    void refreshPrice(Long skuId, String userId);

    /**
     * 提交订单: 保存订单以及订单详情
     */
    Long saveOrderInfo(OrderInfo orderInfo);

    /**
     * 提交订单: 根据订单id获取订单信息
     */
    OrderInfoDTO getOrderInfo(Long orderId);


    /**
     * 我的订单：获取《我的订单》 列表
     */
    IPage<OrderInfoDTO> getPage(Page<OrderInfoDTO> pageParam, String userId);

    /**
     * 支付回调，支付成功，修改订单状态
     */
    void successPay(Long orderId);

    /**
     * 支付回调：库存扣减完成，修改订单状态, 如果库存工作单的状态是DEDUCTED，则说明扣减库存成功
     * 于是将订单状态修改为待发货状态
     */
    void successLockStock(String orderId, String taskStatus);

    /**
     * 支付回调：拆单
     */
    List<WareOrderTaskDTO> orderSplit(String orderId, List<WareSkuDTO> wareSkuDTOList);

    /**
     * 订单超时取消
     */
    void execExpiredOrder(Long orderId);


    Long saveSeckillOrder(OrderInfoParam orderInfoParam);
}
