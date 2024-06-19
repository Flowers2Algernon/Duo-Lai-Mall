package com.cskaoyan.mall.cart.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 创建日期: 2023/03/16 09:48
 *
 * @author ciggar
 */
@Data
//购物车商品
public class CartInfoDTO {
    private static final long serialVersionUID = 1L;

    //"用户id"
    private String userId;

    //"skuid"
    private Long skuId;

    // "数量"
    private Integer skuNum;

    //"图片文件"
    private String imgUrl;

    //"sku名称 "
    private String skuName;

    //"是否选中"
    private Integer isChecked = 1;

    // 实时价格 skuInfo.price
    // "价格"
    private BigDecimal skuPrice;

    //"创建时间"
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //"更新时间"
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}

