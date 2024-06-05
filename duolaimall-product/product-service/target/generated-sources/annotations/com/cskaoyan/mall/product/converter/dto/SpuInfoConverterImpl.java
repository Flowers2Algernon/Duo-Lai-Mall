package com.cskaoyan.mall.product.converter.dto;

import com.cskaoyan.mall.product.dto.SpuImageDTO;
import com.cskaoyan.mall.product.dto.SpuInfoDTO;
import com.cskaoyan.mall.product.dto.SpuPosterDTO;
import com.cskaoyan.mall.product.dto.SpuSaleAttributeInfoDTO;
import com.cskaoyan.mall.product.dto.SpuSaleAttributeValueDTO;
import com.cskaoyan.mall.product.model.SpuImage;
import com.cskaoyan.mall.product.model.SpuInfo;
import com.cskaoyan.mall.product.model.SpuPoster;
import com.cskaoyan.mall.product.model.SpuSaleAttributeInfo;
import com.cskaoyan.mall.product.model.SpuSaleAttributeValue;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-05T15:36:04+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class SpuInfoConverterImpl implements SpuInfoConverter {

    @Override
    public SpuInfoDTO spuInfoPO2DTO(SpuInfo spuInfo) {
        if ( spuInfo == null ) {
            return null;
        }

        SpuInfoDTO spuInfoDTO = new SpuInfoDTO();

        spuInfoDTO.setId( spuInfo.getId() );
        spuInfoDTO.setSpuName( spuInfo.getSpuName() );
        spuInfoDTO.setDescription( spuInfo.getDescription() );
        spuInfoDTO.setThirdLevelCategoryId( spuInfo.getThirdLevelCategoryId() );
        spuInfoDTO.setTmId( spuInfo.getTmId() );
        spuInfoDTO.setSpuSaleAttributeInfoList( spuSaleAttributeInfoPOs2DTOs( spuInfo.getSpuSaleAttributeInfoList() ) );
        spuInfoDTO.setSpuImageList( spuImagePOs2DTOs( spuInfo.getSpuImageList() ) );
        spuInfoDTO.setSpuPosterList( spuPosterPOs2DTOs( spuInfo.getSpuPosterList() ) );

        return spuInfoDTO;
    }

    @Override
    public List<SpuInfoDTO> spuInfoPO2DTOs(List<SpuInfo> spuInfos) {
        if ( spuInfos == null ) {
            return null;
        }

        List<SpuInfoDTO> list = new ArrayList<SpuInfoDTO>( spuInfos.size() );
        for ( SpuInfo spuInfo : spuInfos ) {
            list.add( spuInfoPO2DTO( spuInfo ) );
        }

        return list;
    }

    @Override
    public SpuSaleAttributeInfoDTO spuSaleAttributeInfoPO2DTO(SpuSaleAttributeInfo spuSaleAttributeInfo) {
        if ( spuSaleAttributeInfo == null ) {
            return null;
        }

        SpuSaleAttributeInfoDTO spuSaleAttributeInfoDTO = new SpuSaleAttributeInfoDTO();

        spuSaleAttributeInfoDTO.setId( spuSaleAttributeInfo.getId() );
        spuSaleAttributeInfoDTO.setSpuId( spuSaleAttributeInfo.getSpuId() );
        spuSaleAttributeInfoDTO.setSaleAttrId( spuSaleAttributeInfo.getSaleAttrId() );
        spuSaleAttributeInfoDTO.setSaleAttrName( spuSaleAttributeInfo.getSaleAttrName() );
        spuSaleAttributeInfoDTO.setSpuSaleAttrValueList( spuSaleAttributeValuePOs2DTOs( spuSaleAttributeInfo.getSpuSaleAttrValueList() ) );

        return spuSaleAttributeInfoDTO;
    }

    @Override
    public List<SpuSaleAttributeInfoDTO> spuSaleAttributeInfoPOs2DTOs(List<SpuSaleAttributeInfo> spuSaleAttributeInfos) {
        if ( spuSaleAttributeInfos == null ) {
            return null;
        }

        List<SpuSaleAttributeInfoDTO> list = new ArrayList<SpuSaleAttributeInfoDTO>( spuSaleAttributeInfos.size() );
        for ( SpuSaleAttributeInfo spuSaleAttributeInfo : spuSaleAttributeInfos ) {
            list.add( spuSaleAttributeInfoPO2DTO( spuSaleAttributeInfo ) );
        }

        return list;
    }

    @Override
    public SpuSaleAttributeValueDTO spuSaleAttributeValuePO2DTO(SpuSaleAttributeValue spuSaleAttributeValue) {
        if ( spuSaleAttributeValue == null ) {
            return null;
        }

        SpuSaleAttributeValueDTO spuSaleAttributeValueDTO = new SpuSaleAttributeValueDTO();

        spuSaleAttributeValueDTO.setId( spuSaleAttributeValue.getId() );
        spuSaleAttributeValueDTO.setSpuId( spuSaleAttributeValue.getSpuId() );
        spuSaleAttributeValueDTO.setSpuSaleAttrId( spuSaleAttributeValue.getSpuSaleAttrId() );
        spuSaleAttributeValueDTO.setSpuSaleAttrValueName( spuSaleAttributeValue.getSpuSaleAttrValueName() );
        spuSaleAttributeValueDTO.setIsChecked( spuSaleAttributeValue.getIsChecked() );

        return spuSaleAttributeValueDTO;
    }

    @Override
    public List<SpuSaleAttributeValueDTO> spuSaleAttributeValuePOs2DTOs(List<SpuSaleAttributeValue> spuSaleAttributeValues) {
        if ( spuSaleAttributeValues == null ) {
            return null;
        }

        List<SpuSaleAttributeValueDTO> list = new ArrayList<SpuSaleAttributeValueDTO>( spuSaleAttributeValues.size() );
        for ( SpuSaleAttributeValue spuSaleAttributeValue : spuSaleAttributeValues ) {
            list.add( spuSaleAttributeValuePO2DTO( spuSaleAttributeValue ) );
        }

        return list;
    }

    @Override
    public SpuImageDTO spuImagePO2spuImageDTO(SpuImage spuImage) {
        if ( spuImage == null ) {
            return null;
        }

        SpuImageDTO spuImageDTO = new SpuImageDTO();

        spuImageDTO.setId( spuImage.getId() );
        spuImageDTO.setImgName( spuImage.getImgName() );
        spuImageDTO.setImgUrl( spuImage.getImgUrl() );

        return spuImageDTO;
    }

    @Override
    public List<SpuImageDTO> spuImagePOs2DTOs(List<SpuImage> spuImages) {
        if ( spuImages == null ) {
            return null;
        }

        List<SpuImageDTO> list = new ArrayList<SpuImageDTO>( spuImages.size() );
        for ( SpuImage spuImage : spuImages ) {
            list.add( spuImagePO2spuImageDTO( spuImage ) );
        }

        return list;
    }

    @Override
    public SpuPosterDTO spuPosterPO2DTO(SpuPoster spuPoster) {
        if ( spuPoster == null ) {
            return null;
        }

        SpuPosterDTO spuPosterDTO = new SpuPosterDTO();

        spuPosterDTO.setId( spuPoster.getId() );
        spuPosterDTO.setSpuId( spuPoster.getSpuId() );
        spuPosterDTO.setImgName( spuPoster.getImgName() );
        spuPosterDTO.setImgUrl( spuPoster.getImgUrl() );

        return spuPosterDTO;
    }

    @Override
    public List<SpuPosterDTO> spuPosterPOs2DTOs(List<SpuPoster> spuPosters) {
        if ( spuPosters == null ) {
            return null;
        }

        List<SpuPosterDTO> list = new ArrayList<SpuPosterDTO>( spuPosters.size() );
        for ( SpuPoster spuPoster : spuPosters ) {
            list.add( spuPosterPO2DTO( spuPoster ) );
        }

        return list;
    }
}
