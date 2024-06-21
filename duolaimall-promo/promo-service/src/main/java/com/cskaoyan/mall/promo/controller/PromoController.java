package com.cskaoyan.mall.promo.controller;

import com.cskaoyan.mall.common.constant.RedisConst;
import com.cskaoyan.mall.common.constant.ResultCodeEnum;
import com.cskaoyan.mall.common.execption.BusinessException;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.common.util.AuthContext;
import com.cskaoyan.mall.common.util.DateUtil;
import com.cskaoyan.mall.common.util.MD5;
import com.cskaoyan.mall.order.dto.OrderDetailDTO;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.order.dto.OrderTradeDTO;
import com.cskaoyan.mall.order.query.OrderInfoParam;
import com.cskaoyan.mall.promo.api.dto.SeckillGoodsDTO;
import com.cskaoyan.mall.promo.client.OrderApiClient;
import com.cskaoyan.mall.promo.client.UserApiClient;
import com.cskaoyan.mall.promo.constant.SeckillCodeEnum;
import com.cskaoyan.mall.promo.converter.SeckillGoodsConverter;
import com.cskaoyan.mall.promo.model.OrderRecord;
import com.cskaoyan.mall.promo.model.SeckillGoods;
import com.cskaoyan.mall.promo.model.UserRecord;
import com.cskaoyan.mall.promo.service.PromoService;
import com.cskaoyan.mall.promo.util.LocalCacheHelper;
import com.cskaoyan.mall.user.dto.UserAddressDTO;
import com.mysql.cj.protocol.ResultBuilder;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 北海 on 2023-05-19 15:49
 */
@RestController
public class PromoController {

    @Autowired
    PromoService promoService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    UserApiClient userApiClient;

    @Autowired
    OrderApiClient orderApiClient;


    @Autowired
    SeckillGoodsConverter converter;

    /**
     * 返回全部列表
     *
     * @return
     */
    @GetMapping("seckill")
    public Result< List<SeckillGoodsDTO>> findAll() {
        return Result.ok(promoService.findAll());
    }

    /**
     * 获取实体
     *
     * @param skuId
     * @return
     */
    @GetMapping("seckill/{skuId}")
    public Result<SeckillGoodsDTO> getSeckillGoods(@PathVariable("skuId") Long skuId) {
        return Result.ok(promoService.getSeckillGoodsDTO(skuId));
    }

    /**
     * @param skuId
     * @param request
     * 功能描述: 获取下单码
     */
    @GetMapping("/seckill/auth/getSeckillSkuIdStr/{skuId}")
    public Result<String> getSeckillSkuIdStr(@PathVariable("skuId") Long skuId, HttpServletRequest request) {

        return Result.fail("").message("获取下单码失败");
    }

    /**
     * @param orderInfo
     * @param request
     * 功能描述: 提交订单
     */
    @PostMapping("/seckill/auth/submitOrder")
    public Result submitOrder(@RequestBody OrderInfoParam orderInfo, HttpServletRequest request) {


        return Result.build(null, SeckillCodeEnum.SECKILL_SUCCESS);
    }


    /**
     * @param skuId
     * @param request
     * 功能描述: 前端轮训，秒杀，检查下单状态
     */
    @GetMapping(value = "/seckill/auth/checkOrder/{skuId}")
    public Result checkOrder(@PathVariable("skuId") Long skuId, HttpServletRequest request) {

        return Result.build(null, SeckillCodeEnum.SECKILL_RUN);
    }


    /**
     * 秒杀确认订单
     * @param request
     * @return
     */
    @GetMapping("/seckill/auth/trade/{skuId}")
    public Result<OrderTradeDTO> trade(@PathVariable("skuId") Long skuId,String skuIdStr, HttpServletRequest request) {


        return Result.ok();

    }

    @GetMapping("/seckill/clear")
    public Result clearTest() {
        promoService.clearCache();
        return Result.ok();
    }

}
