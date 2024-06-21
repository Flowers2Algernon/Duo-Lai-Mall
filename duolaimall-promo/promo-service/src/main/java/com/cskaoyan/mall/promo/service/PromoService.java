package com.cskaoyan.mall.promo.service;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.order.dto.OrderTradeDTO;
import com.cskaoyan.mall.order.query.OrderInfoParam;
import com.cskaoyan.mall.promo.api.dto.SeckillGoodsDTO;
import com.cskaoyan.mall.promo.model.OrderRecord;
import com.cskaoyan.mall.promo.model.SeckillGoods;

import java.util.List;

public interface PromoService {

    /**
     * 把秒杀商品列表信息导入Redis
     */
    void importIntoRedis();

    /**
     * 返回全部列表
     * @return
     */
    List<SeckillGoodsDTO> findAll();

    /**
     * 根据ID获取实体
     * @param skuId
     * @return
     */
    SeckillGoodsDTO getSeckillGoodsDTO(Long skuId);


    /***
     * 根据商品id与用户ID查看订单信息
     */
    boolean checkOrder(Long skuId, String userId);

    /**
     * 清理Redis缓存
     */
    void clearCache();

    /*
         组装订单确认页数据
     */
    OrderTradeDTO getTradeData(String userId, Long skuId);

    /*
         提交秒杀订单
     */
     void submitOrder(OrderInfoParam orderInfo);

    void submitOrderInTransaction(OrderInfoParam orderInfo);

}
