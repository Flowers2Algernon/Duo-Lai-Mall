package com.cskaoyan.mall.product.converter.dto;

import com.cskaoyan.mall.product.dto.TestProductDTO;
import com.cskaoyan.mall.product.model.SkuInfo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-17T14:49:29+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class TestProductConverterImpl implements TestProductConverter {

    @Override
    public TestProductDTO skuInfoDO2DTO(SkuInfo skuInfo) {
        if ( skuInfo == null ) {
            return null;
        }

        TestProductDTO testProductDTO = new TestProductDTO();

        testProductDTO.setProductName( skuInfo.getSkuName() );
        testProductDTO.setImgUrl( skuInfo.getSkuDefaultImg() );
        testProductDTO.setPrice( skuInfo.getPrice() );

        return testProductDTO;
    }
}
