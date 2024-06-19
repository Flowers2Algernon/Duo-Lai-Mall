package com.cskaoyan.mall.cart.controller;

import com.cskaoyan.mall.cart.api.dto.CartInfoDTO;
import com.cskaoyan.mall.cart.service.CartService;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.common.util.AuthContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 北海 on 2023-05-19 15:46
 */
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * @param skuId
     * @param skuNum
     * 功能描述: 添加商品到购物车
     */
    @GetMapping("/cart/add/{skuId}/{skuNum}")
    public Result addToCart(@PathVariable Long skuId, @PathVariable Integer skuNum, HttpServletRequest request) {

        // 1. 获取userId
        String userId = AuthContext.getUserId(request);

        // 2. 当userId为空，获取用户临时id
        if (StringUtils.isBlank(userId)) {
            userId = AuthContext.getUserTempId(request);
        }

        cartService.addToCart(skuId, userId, skuNum);

        return Result.ok();
    }

    /**
     * @param skuId      商品Id
     * @param isChecked  选中状态，1:选中  0:未选中
     * @param request
     * @return: com.cskaoyan.mall.common.result.Result
     * 功能描述:
     */
    @PutMapping("/cart/check/{skuId}/{isChecked}")
    public Result checkCart(@PathVariable Long skuId,
                            @PathVariable Integer isChecked,
                            HttpServletRequest request){

        return Result.ok();
    }

    /**
     * 删除购物车指定的商品
     * @param skuId
     * @param request
     * @return
     */
    @DeleteMapping("/cart/{skuId}")
    public Result deleteCart(@PathVariable("skuId") Long skuId, HttpServletRequest request) {


        return Result.ok();
    }

    /**
     * @param request
     * @return
     */
    @GetMapping("cart")
    public Result<List<CartInfoDTO>> cartList(HttpServletRequest request) {

        // 用户登录的userId
        String userId = AuthContext.getUserId(request);

        // 用户临时id
        String userTempId = AuthContext.getUserTempId(request);

        List<CartInfoDTO> cartList = cartService.getCartList(userId, userTempId);

        return Result.ok(cartList);
    }

    @DeleteMapping("/cart/checked")
    public Result deleteChecked(HttpServletRequest request) {

        return Result.ok();
    }

}
