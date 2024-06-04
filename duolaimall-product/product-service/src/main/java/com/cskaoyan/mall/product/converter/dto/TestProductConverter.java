package com.cskaoyan.mall.product.converter.dto;


import com.cskaoyan.mall.product.dto.TestProductDTO;
import com.cskaoyan.mall.product.model.SkuInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TestProductConverter {


    // SkuInfo -> TestProductDTO
    @Mapping(source = "skuName", target = "productName")
    @Mapping(source = "skuDefaultImg", target = "imgUrl")
    TestProductDTO  skuInfoDO2DTO(SkuInfo skuInfo);
}
