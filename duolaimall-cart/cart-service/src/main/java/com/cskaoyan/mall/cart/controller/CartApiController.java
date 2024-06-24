package com.cskaoyan.mall.cart.controller;

import com.cskaoyan.mall.cart.api.dto.CartInfoDTO;
import com.cskaoyan.mall.cart.service.CartService;
import com.cskaoyan.mall.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class CartApiController {
    //用于实现各种服务间内部调用的Controller方法

    @Autowired
    CartService cartService;

    @GetMapping("/api/cart/inner/getCartCheckedList/{userId}")
    public List<CartInfoDTO> getCartCheckedList(@PathVariable(value = "userId") String userId){
        //下单的时候，根据用户Id获取该用户购物车中所有已选中的商品
        List<CartInfoDTO> cartCheckedList = cartService.getCartCheckedList(userId);
        return cartCheckedList;
    }

    @GetMapping("/api/cart/inner/refresh/{userId}/{skuId}")
    public Result refreshCartPrice(@PathVariable(value = "userId") String userId, @PathVariable(value = "skuId") Long skuId){
        //根据用户Id和商品Id从数据库中获取商品价格
        cartService.refreshCartPrice(userId,skuId);
        return Result.ok();
    }
    @PutMapping("/api/cart/inner/delete/order/cart/{userId}")
    public Result removeCartProductsInOrder(@PathVariable("userId") String userId, @RequestBody List<Long> skuIds){
        cartService.delete(userId,skuIds);
        return Result.ok();
    }
}
