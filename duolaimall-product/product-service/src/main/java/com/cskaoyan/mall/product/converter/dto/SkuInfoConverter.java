package com.cskaoyan.mall.product.converter.dto;

import com.cskaoyan.mall.product.dto.SkuImageDTO;
import com.cskaoyan.mall.product.dto.SkuInfoDTO;
import com.cskaoyan.mall.product.dto.SkuPlatformAttributeValueDTO;
import com.cskaoyan.mall.product.dto.SkuSaleAttributeValueDTO;
import com.cskaoyan.mall.product.model.SkuImage;
import com.cskaoyan.mall.product.model.SkuInfo;
import com.cskaoyan.mall.product.model.SkuPlatformAttributeValue;
import com.cskaoyan.mall.product.model.SkuSaleAttributeValue;
import com.cskaoyan.mall.product.query.SkuImageParam;
import com.cskaoyan.mall.product.query.SkuPlatformAttributeValueParam;
import com.cskaoyan.mall.product.query.SkuSaleAttributeValueParam;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SkuInfoConverter {

    SkuInfoDTO skuInfoPO2DTO(SkuInfo skuInfo);
    List<SkuInfoDTO> skuInfoPO2DTOs(List<SkuInfo> skuInfos);

    SkuImageDTO skuImagePO2DTO(SkuImage skuImage);
    SkuImage skuImageP20P(SkuImageParam skuImageParam);

    SkuPlatformAttributeValueDTO skuPlatformAttributeValuePO2DTO(
            SkuPlatformAttributeValue skuPlatformAttributeValue);
    SkuPlatformAttributeValue skuPlatformAttributeValueP2O(
            SkuPlatformAttributeValueParam skuPlatformAttributeValueParam);

    SkuSaleAttributeValueDTO skuSaleAttributeValuePOs2DTOs(
            SkuSaleAttributeValue skuSaleAttributeValue);
    SkuSaleAttributeValue skuSaleAttributeValueP2O(
            SkuSaleAttributeValueParam skuSaleAttributeValueParam);
}
