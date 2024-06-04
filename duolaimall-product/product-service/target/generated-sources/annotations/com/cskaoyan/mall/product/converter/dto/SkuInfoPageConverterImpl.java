package com.cskaoyan.mall.product.converter.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.product.dto.SkuInfoDTO;
import com.cskaoyan.mall.product.dto.SkuInfoPageDTO;
import com.cskaoyan.mall.product.model.SkuInfo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-04T16:24:12+0800",
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

        skuInfoPageDTO.setRecords( skuInfoListToSkuInfoDTOList( skuInfoPage.getRecords() ) );
        skuInfoPageDTO.setTotal( (int) skuInfoPage.getTotal() );

        return skuInfoPageDTO;
    }

    protected List<SkuInfoDTO> skuInfoListToSkuInfoDTOList(List<SkuInfo> list) {
        if ( list == null ) {
            return null;
        }

        List<SkuInfoDTO> list1 = new ArrayList<SkuInfoDTO>( list.size() );
        for ( SkuInfo skuInfo : list ) {
            list1.add( skuInfoConverter.skuInfoPO2DTO( skuInfo ) );
        }

        return list1;
    }
}
