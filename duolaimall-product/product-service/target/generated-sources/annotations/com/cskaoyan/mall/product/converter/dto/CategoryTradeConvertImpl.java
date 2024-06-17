package com.cskaoyan.mall.product.converter.dto;

import com.cskaoyan.mall.product.model.CategoryTrademark;
import com.cskaoyan.mall.product.query.CategoryTrademarkParam;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-17T14:49:29+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class CategoryTradeConvertImpl implements CategoryTradeConvert {

    @Override
    public CategoryTrademark categoryTrademarkParam2CategoryTrademark(CategoryTrademarkParam categoryTrademarkParam) {
        if ( categoryTrademarkParam == null ) {
            return null;
        }

        CategoryTrademark categoryTrademark = new CategoryTrademark();

        return categoryTrademark;
    }
}
