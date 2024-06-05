package com.cskaoyan.mall.product.converter.dto;

import com.cskaoyan.mall.product.model.CategoryTrademark;
import com.cskaoyan.mall.product.query.CategoryTrademarkParam;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryTradeConvert {
    CategoryTrademark categoryTrademarkParam2CategoryTrademark(CategoryTrademarkParam categoryTrademarkParam );
}
