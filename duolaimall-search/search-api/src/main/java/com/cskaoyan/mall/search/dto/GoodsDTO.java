package com.cskaoyan.mall.search.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/*
     该类用来ES根据条件查询到的每一个SKU商品信息
 */
@Data
public class GoodsDTO {

    // 商品Id skuId
    //"商品skuId"
    private Long id;

    //"商品默认图片url"
    private String defaultImg;
    // "商品名称"
    private String title;
    // "价格"
    private Double price;
    // "商品品牌Id"
    private Long tmId;
    // "商品品牌名称"
    private String tmName;
    // "商品品牌logourl"
    private String tmLogoUrl;
    // "商品一级类目Id"
    private Long firstLevelCategoryId;
    // "商品一级类目名称"
    private String firstLevelCategoryName;
    // "商品二级类目Id"
    private Long secondLevelCategoryId;

    // "商品二级类目名称"
    private String secondLevelCategoryName;
    // "商品三级级类目Id"
    private Long thirdLevelCategoryId;

    // "商品三级类目名称"
    private String thirdLevelCategoryName;

    //  商品的热度！ 我们将商品被用户点查看的次数越多，则说明热度就越高！
    // "商品热度"
    private Long hotScore = 0L;

    // 平台属性集合对象
    // Nested 支持嵌套查询
    // "商品平台属性值集合"
    private List<SearchAttrDTO> attrs;
}
