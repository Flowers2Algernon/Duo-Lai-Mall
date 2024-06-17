package com.cskaoyan.mall.product.converter.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.product.dto.SkuInfoPageDTO;
import com.cskaoyan.mall.product.model.SkuInfo;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-17T14:49:29+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class SkuInfoPageConverterImpl implements SkuInfoPageConverter {

    @Autowired
    private SkuInfoConverter skuInfoConverter;

    @Override
    public SkuInfoPageDTO skuInfoPagePO2PageDTO(Page<SkuInfo> skuInfoPage) {
        if ( skuInfoPage == null ) {
            return null;
        }

        SkuInfoPageDTO skuInfoPageDTO = new SkuInfoPageDTO();

        skuInfoPageDTO.setRecords( skuInfoConverter.skuInfoPO2DTOs( skuInfoPage.getRecords() ) );
        skuInfoPageDTO.setTotal( (int) skuInfoPage.getTotal() );

        return skuInfoPageDTO;
    }
}
