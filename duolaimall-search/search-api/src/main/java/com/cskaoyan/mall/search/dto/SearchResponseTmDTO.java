package com.cskaoyan.mall.search.dto;

import lombok.Data;

import java.io.Serializable;

// 品牌数据
@Data
public class SearchResponseTmDTO implements Serializable {
    //当前属性值的所有值
    private Long tmId;
    //属性名称
    private String tmName;
    //图片名称
    private String tmLogoUrl;
}

