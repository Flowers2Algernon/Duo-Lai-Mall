package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.product.dto.SkuInfoDTO;
import com.cskaoyan.mall.product.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/product")
public class ProductApiController {

    @Autowired
    SkuService skuService;

    @GetMapping("inner/getSkuInfo/{skuId}")
    public SkuInfoDTO getSkuInfo(@PathVariable("skuId") long skuId) {
        SkuInfoDTO skuInfo = skuService.getSkuInfo(skuId);
        return skuInfo;
    }

    @GetMapping("inner/getSkuPrice/{skuId}")
    public BigDecimal getSkuImage(@PathVariable long skuId){
        BigDecimal skuPrice = skuService.getSkuPrice(skuId);
        if (skuPrice!=null){
            return skuPrice;
        }else return BigDecimal.ZERO;
    }

    @GetMapping("/api/product/inner/getSkuPrice/{skuId}")
    public BigDecimal getSkuPrice(@PathVariable(value = "skuId") Long skuId){
        //获取商品id并返回
        BigDecimal skuPrice = skuService.getSkuPrice(skuId);
        return skuPrice;
    }
}
