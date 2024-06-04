package com.cskaoyan.mall.product.converter.param;

import com.cskaoyan.mall.product.model.SkuImage;
import com.cskaoyan.mall.product.model.SkuInfo;
import com.cskaoyan.mall.product.model.SkuPlatformAttributeValue;
import com.cskaoyan.mall.product.model.SkuSaleAttributeValue;
import com.cskaoyan.mall.product.query.SkuImageParam;
import com.cskaoyan.mall.product.query.SkuInfoParam;
import com.cskaoyan.mall.product.query.SkuPlatformAttributeValueParam;
import com.cskaoyan.mall.product.query.SkuSaleAttributeValueParam;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-04T16:24:12+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class SkuInfoParamConverterImpl implements SkuInfoParamConverter {

    @Override
    public SkuInfo SkuInfoParam2Info(SkuInfoParam skuInfoParam) {
        if ( skuInfoParam == null ) {
            return null;
        }

        SkuInfo skuInfo = new SkuInfo();

        skuInfo.setThirdLevelCategoryId( skuInfoParam.getCategory3Id() );
        skuInfo.setSkuPlatformAttributeValueList( skuPlatformAttributeValueParamListToSkuPlatformAttributeValueList( skuInfoParam.getSkuAttrValueList() ) );
        skuInfo.setSkuSaleAttributeValueList( skuSaleAttributeValueParamListToSkuSaleAttributeValueList( skuInfoParam.getSkuSaleAttrValueList() ) );
        skuInfo.setId( skuInfoParam.getId() );
        skuInfo.setSpuId( skuInfoParam.getSpuId() );
        skuInfo.setPrice( skuInfoParam.getPrice() );
        skuInfo.setSkuName( skuInfoParam.getSkuName() );
        skuInfo.setSkuDesc( skuInfoParam.getSkuDesc() );
        skuInfo.setWeight( skuInfoParam.getWeight() );
        skuInfo.setTmId( skuInfoParam.getTmId() );
        skuInfo.setSkuDefaultImg( skuInfoParam.getSkuDefaultImg() );
        skuInfo.setIsSale( skuInfoParam.getIsSale() );
        skuInfo.setSkuImageList( skuImageParamListToSkuImageList( skuInfoParam.getSkuImageList() ) );

        return skuInfo;
    }

    @Override
    public SkuImage skuImageParam2Image(SkuImageParam skuImageParam) {
        if ( skuImageParam == null ) {
            return null;
        }

        SkuImage skuImage = new SkuImage();

        skuImage.setId( skuImageParam.getId() );
        skuImage.setSkuId( skuImageParam.getSkuId() );
        skuImage.setImgName( skuImageParam.getImgName() );
        skuImage.setImgUrl( skuImageParam.getImgUrl() );
        skuImage.setSpuImgId( skuImageParam.getSpuImgId() );
        skuImage.setIsDefault( skuImageParam.getIsDefault() );

        return skuImage;
    }

    @Override
    public SkuPlatformAttributeValue skuPlatformAttributeValueParam2Value(SkuPlatformAttributeValueParam param) {
        if ( param == null ) {
            return null;
        }

        SkuPlatformAttributeValue skuPlatformAttributeValue = new SkuPlatformAttributeValue();

        skuPlatformAttributeValue.setId( param.getId() );
        skuPlatformAttributeValue.setAttrId( param.getAttrId() );
        skuPlatformAttributeValue.setValueId( param.getValueId() );
        skuPlatformAttributeValue.setSkuId( param.getSkuId() );

        return skuPlatformAttributeValue;
    }

    @Override
    public SkuSaleAttributeValue skuSaleAttributeValueParam2Value(SkuSaleAttributeValueParam param) {
        if ( param == null ) {
            return null;
        }

        SkuSaleAttributeValue skuSaleAttributeValue = new SkuSaleAttributeValue();

        skuSaleAttributeValue.setSpuSaleAttrValueId( param.getSaleAttrValueId() );
        skuSaleAttributeValue.setId( param.getId() );
        skuSaleAttributeValue.setSkuId( param.getSkuId() );
        skuSaleAttributeValue.setSpuId( param.getSpuId() );

        return skuSaleAttributeValue;
    }

    protected List<SkuPlatformAttributeValue> skuPlatformAttributeValueParamListToSkuPlatformAttributeValueList(List<SkuPlatformAttributeValueParam> list) {
        if ( list == null ) {
            return null;
        }

        List<SkuPlatformAttributeValue> list1 = new ArrayList<SkuPlatformAttributeValue>( list.size() );
        for ( SkuPlatformAttributeValueParam skuPlatformAttributeValueParam : list ) {
            list1.add( skuPlatformAttributeValueParam2Value( skuPlatformAttributeValueParam ) );
        }

        return list1;
    }

    protected List<SkuSaleAttributeValue> skuSaleAttributeValueParamListToSkuSaleAttributeValueList(List<SkuSaleAttributeValueParam> list) {
        if ( list == null ) {
            return null;
        }

        List<SkuSaleAttributeValue> list1 = new ArrayList<SkuSaleAttributeValue>( list.size() );
        for ( SkuSaleAttributeValueParam skuSaleAttributeValueParam : list ) {
            list1.add( skuSaleAttributeValueParam2Value( skuSaleAttributeValueParam ) );
        }

        return list1;
    }

    protected List<SkuImage> skuImageParamListToSkuImageList(List<SkuImageParam> list) {
        if ( list == null ) {
            return null;
        }

        List<SkuImage> list1 = new ArrayList<SkuImage>( list.size() );
        for ( SkuImageParam skuImageParam : list ) {
            list1.add( skuImageParam2Image( skuImageParam ) );
        }

        return list1;
    }
}
