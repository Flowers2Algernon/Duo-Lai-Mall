package com.cskaoyan.mall.cart.converter;

import com.cskaoyan.mall.cart.api.dto.CartInfoDTO;
import com.cskaoyan.mall.product.dto.SkuInfoDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-19T17:38:27+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class SkuInfoConverterImpl implements SkuInfoConverter {

    @Override
    public CartInfoDTO skuInfoToCartInfo(SkuInfoDTO skuInfo, Integer skuNum, Long skuId, String userId) {
        if ( skuInfo == null && skuNum == null && skuId == null && userId == null ) {
            return null;
        }

        CartInfoDTO cartInfoDTO = new CartInfoDTO();

        if ( skuInfo != null ) {
            cartInfoDTO.setImgUrl( skuInfo.getSkuDefaultImg() );
            cartInfoDTO.setSkuName( skuInfo.getSkuName() );
            cartInfoDTO.setSkuPrice( skuInfo.getPrice() );
        }
        cartInfoDTO.setSkuNum( skuNum );
        cartInfoDTO.setSkuId( skuId );
        cartInfoDTO.setUserId( userId );
        cartInfoDTO.setIsChecked( 1 );
        cartInfoDTO.setCreateTime( new java.util.Date() );
        cartInfoDTO.setUpdateTime( new java.util.Date() );

        return cartInfoDTO;
    }
}
