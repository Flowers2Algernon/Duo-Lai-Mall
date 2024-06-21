package com.cskaoyan.mall.promo.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class SeckillGoodsDTO implements Serializable {

    // "秒杀商品Id"
    private Long id;
    // "秒杀商品spu Id"
    private Long spuId;
    //"秒杀商品sku Id"
    private Long skuId;
    // "秒杀商品名称"
    private String skuName;
    // "秒杀商品默认图片url"
    private String skuDefaultImg;
    // "秒杀商品原价"
    private BigDecimal price;
    // "秒杀商品秒杀价格"
    private BigDecimal costPrice;
    // "秒杀商品审核时间"
    private Date checkTime;
    // "秒杀商品审核状态"
    private String status;
    // "秒杀商品所参与的秒杀活动的开始时间"
    private Date startTime;
    // "秒杀商品所参与的秒杀活动的结束时间"
    private Date endTime;
    // "秒杀商品已售卖数量"
    private Integer num;
    // "秒杀商品库存"
    private Integer stockCount;
    // "秒杀商品描述字符串"
    private String skuDesc;
}
