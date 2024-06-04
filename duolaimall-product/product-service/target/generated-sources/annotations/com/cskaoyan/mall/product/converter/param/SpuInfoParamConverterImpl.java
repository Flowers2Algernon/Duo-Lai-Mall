package com.cskaoyan.mall.product.converter.param;

import com.cskaoyan.mall.product.model.SpuImage;
import com.cskaoyan.mall.product.model.SpuInfo;
import com.cskaoyan.mall.product.model.SpuPoster;
import com.cskaoyan.mall.product.model.SpuSaleAttributeInfo;
import com.cskaoyan.mall.product.model.SpuSaleAttributeValue;
import com.cskaoyan.mall.product.query.SpuImageParam;
import com.cskaoyan.mall.product.query.SpuInfoParam;
import com.cskaoyan.mall.product.query.SpuPosterParam;
import com.cskaoyan.mall.product.query.SpuSaleAttributeInfoParam;
import com.cskaoyan.mall.product.query.SpuSaleAttributeValueParam;
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
public class SpuInfoParamConverterImpl implements SpuInfoParamConverter {

    @Override
    public SpuInfo spuInfoParam2Info(SpuInfoParam spuInfo) {
        if ( spuInfo == null ) {
            return null;
        }

        SpuInfo spuInfo1 = new SpuInfo();

        spuInfo1.setThirdLevelCategoryId( spuInfo.getCategory3Id() );
        spuInfo1.setSpuSaleAttributeInfoList( spuSaleAttributeParams2Infos( spuInfo.getSpuSaleAttrList() ) );
        spuInfo1.setId( spuInfo.getId() );
        spuInfo1.setSpuName( spuInfo.getSpuName() );
        spuInfo1.setDescription( spuInfo.getDescription() );
        spuInfo1.setTmId( spuInfo.getTmId() );
        spuInfo1.setSpuImageList( spuImageParams2Images( spuInfo.getSpuImageList() ) );
        spuInfo1.setSpuPosterList( spuPosterParamListToSpuPosterList( spuInfo.getSpuPosterList() ) );

        return spuInfo1;
    }

    @Override
    public SpuImage spuImageParam2Image(SpuImageParam spuImage) {
        if ( spuImage == null ) {
            return null;
        }

        SpuImage spuImage1 = new SpuImage();

        spuImage1.setId( spuImage.getId() );
        spuImage1.setImgName( spuImage.getImgName() );
        spuImage1.setImgUrl( spuImage.getImgUrl() );

        return spuImage1;
    }

    @Override
    public List<SpuImage> spuImageParams2Images(List<SpuImageParam> spuImages) {
        if ( spuImages == null ) {
            return null;
        }

        List<SpuImage> list = new ArrayList<SpuImage>( spuImages.size() );
        for ( SpuImageParam spuImageParam : spuImages ) {
            list.add( spuImageParam2Image( spuImageParam ) );
        }

        return list;
    }

    @Override
    public SpuSaleAttributeInfo spuSaleAttributeParam2Info(SpuSaleAttributeInfoParam spuSaleAttributeInfo) {
        if ( spuSaleAttributeInfo == null ) {
            return null;
        }

        SpuSaleAttributeInfo spuSaleAttributeInfo1 = new SpuSaleAttributeInfo();

        spuSaleAttributeInfo1.setSaleAttrId( spuSaleAttributeInfo.getBaseSaleAttrId() );
        spuSaleAttributeInfo1.setId( spuSaleAttributeInfo.getId() );
        spuSaleAttributeInfo1.setSpuId( spuSaleAttributeInfo.getSpuId() );
        spuSaleAttributeInfo1.setSaleAttrName( spuSaleAttributeInfo.getSaleAttrName() );
        spuSaleAttributeInfo1.setSpuSaleAttrValueList( spuSaleAttributeValueParams2Values( spuSaleAttributeInfo.getSpuSaleAttrValueList() ) );

        return spuSaleAttributeInfo1;
    }

    @Override
    public List<SpuSaleAttributeInfo> spuSaleAttributeParams2Infos(List<SpuSaleAttributeInfoParam> spuSaleAttributeInfos) {
        if ( spuSaleAttributeInfos == null ) {
            return null;
        }

        List<SpuSaleAttributeInfo> list = new ArrayList<SpuSaleAttributeInfo>( spuSaleAttributeInfos.size() );
        for ( SpuSaleAttributeInfoParam spuSaleAttributeInfoParam : spuSaleAttributeInfos ) {
            list.add( spuSaleAttributeParam2Info( spuSaleAttributeInfoParam ) );
        }

        return list;
    }

    @Override
    public SpuSaleAttributeValue spuSaleAttributeValueParam2Value(SpuSaleAttributeValueParam spuSaleAttributeValue) {
        if ( spuSaleAttributeValue == null ) {
            return null;
        }

        SpuSaleAttributeValue spuSaleAttributeValue1 = new SpuSaleAttributeValue();

        spuSaleAttributeValue1.setSpuSaleAttrValueName( spuSaleAttributeValue.getSaleAttrValueName() );
        spuSaleAttributeValue1.setSpuSaleAttrId( spuSaleAttributeValue.getBaseSaleAttrId() );
        spuSaleAttributeValue1.setId( spuSaleAttributeValue.getId() );
        spuSaleAttributeValue1.setSpuId( spuSaleAttributeValue.getSpuId() );

        return spuSaleAttributeValue1;
    }

    @Override
    public List<SpuSaleAttributeValue> spuSaleAttributeValueParams2Values(List<SpuSaleAttributeValueParam> spuSaleAttributeValues) {
        if ( spuSaleAttributeValues == null ) {
            return null;
        }

        List<SpuSaleAttributeValue> list = new ArrayList<SpuSaleAttributeValue>( spuSaleAttributeValues.size() );
        for ( SpuSaleAttributeValueParam spuSaleAttributeValueParam : spuSaleAttributeValues ) {
            list.add( spuSaleAttributeValueParam2Value( spuSaleAttributeValueParam ) );
        }

        return list;
    }

    protected SpuPoster spuPosterParamToSpuPoster(SpuPosterParam spuPosterParam) {
        if ( spuPosterParam == null ) {
            return null;
        }

        SpuPoster spuPoster = new SpuPoster();

        spuPoster.setId( spuPosterParam.getId() );
        spuPoster.setSpuId( spuPosterParam.getSpuId() );
        spuPoster.setImgName( spuPosterParam.getImgName() );
        spuPoster.setImgUrl( spuPosterParam.getImgUrl() );

        return spuPoster;
    }

    protected List<SpuPoster> spuPosterParamListToSpuPosterList(List<SpuPosterParam> list) {
        if ( list == null ) {
            return null;
        }

        List<SpuPoster> list1 = new ArrayList<SpuPoster>( list.size() );
        for ( SpuPosterParam spuPosterParam : list ) {
            list1.add( spuPosterParamToSpuPoster( spuPosterParam ) );
        }

        return list1;
    }
}
