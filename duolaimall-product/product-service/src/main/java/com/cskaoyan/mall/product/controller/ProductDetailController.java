package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.dto.ProductDetailDTO;
import com.cskaoyan.mall.product.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 北海 on 2023-05-19 15:37
 */
@RestController
public class ProductDetailController {

    @Autowired
    private ProductDetailService productDetailService;

    /**
     * 获取sku详情信息
     */
    @GetMapping("goods/{skuId}")
    public Result<ProductDetailDTO> getItem(@PathVariable Long skuId){
        ProductDetailDTO itemBySkuId = productDetailService.getItemBySkuId(skuId);
        return Result.ok(itemBySkuId);
    }
}
