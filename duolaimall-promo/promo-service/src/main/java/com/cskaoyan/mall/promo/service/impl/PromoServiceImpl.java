package com.cskaoyan.mall.promo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cskaoyan.mall.common.constant.RedisConst;
import com.cskaoyan.mall.common.util.DateUtil;
import com.cskaoyan.mall.order.dto.OrderTradeDTO;
import com.cskaoyan.mall.order.query.OrderInfoParam;
import com.cskaoyan.mall.promo.api.dto.SeckillGoodsDTO;
import com.cskaoyan.mall.promo.client.OrderApiClient;
import com.cskaoyan.mall.promo.client.UserApiClient;
import com.cskaoyan.mall.promo.constant.SeckillGoodsStatus;
import com.cskaoyan.mall.promo.converter.SeckillGoodsConverter;
import com.cskaoyan.mall.promo.mapper.SeckillGoodsMapper;
import com.cskaoyan.mall.promo.model.SeckillGoods;
import com.cskaoyan.mall.promo.service.PromoService;
import com.cskaoyan.mall.promo.util.LocalCacheHelper;

import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@SuppressWarnings("all")
public class PromoServiceImpl implements PromoService {

    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    SeckillGoodsConverter seckillGoodsConverter;

    @Autowired
    UserApiClient userApiClient;

    /**
     * 把秒杀商品列表信息导入Redis
     */
    @Override
    public void importIntoRedis() {
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @Override
    public List<SeckillGoodsDTO> findAll() {


        return null;
    }


    /**
     * 根据ID获取实体
     *
     * @param skuId
     * @return
     */
    @Override
    public SeckillGoodsDTO getSeckillGoodsDTO(Long skuId) {

        return null;
    }


    /**
     * 功能描述: 前端轮训，检查用户下单信息
     */
    @Override
    public boolean checkOrder(Long skuId, String userId) {

        return true;
    }



    /**
     * 清理Redis缓存
     */
    @Override
    public void clearCache() {

    }




    @Override
    public OrderTradeDTO getTradeData(String userId, Long skuId) {

        return null;
    }

    @Autowired
    OrderApiClient orderApiClient;

    // 仅仅实现业务功能
    @Override
    @Transactional
    public void submitOrder(OrderInfoParam orderInfo) {


    }


    @Override
    public void submitOrderInTransaction(OrderInfoParam orderInfo) {

    }


}
