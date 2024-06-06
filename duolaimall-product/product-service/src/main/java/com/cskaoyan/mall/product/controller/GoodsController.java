package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.dto.ProductDetailDTO;
import com.cskaoyan.mall.product.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {
    @Autowired
    ProductDetailService productDetailService;
    @GetMapping("goods/{skuId}")
    public Result<ProductDetailDTO> getItem(@PathVariable Long skuId){
        ProductDetailDTO itemBySkuId = productDetailService.getItemBySkuId(skuId);
        return Result.ok(itemBySkuId);
    }
}
