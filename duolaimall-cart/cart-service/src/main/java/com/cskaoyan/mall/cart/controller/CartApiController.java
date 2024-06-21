package com.cskaoyan.mall.cart.controller;

import com.cskaoyan.mall.cart.api.dto.CartInfoDTO;
import com.cskaoyan.mall.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
