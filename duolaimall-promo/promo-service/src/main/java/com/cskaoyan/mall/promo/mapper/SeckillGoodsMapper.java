package com.cskaoyan.mall.promo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cskaoyan.mall.promo.model.SeckillGoods;
import org.apache.ibatis.annotations.Param;

public interface SeckillGoodsMapper extends BaseMapper<SeckillGoods> {

    Integer decreaseStock(@Param("skuId") Long skuId, @Param("num") Integer num);
}
