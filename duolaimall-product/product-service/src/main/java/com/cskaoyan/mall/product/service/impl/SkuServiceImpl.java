package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.product.converter.dto.SkuInfoConverter;
import com.cskaoyan.mall.product.dto.PlatformAttributeInfoDTO;
import com.cskaoyan.mall.product.dto.SkuInfoDTO;
import com.cskaoyan.mall.product.dto.SkuInfoPageDTO;
import com.cskaoyan.mall.product.dto.SpuSaleAttributeInfoDTO;
import com.cskaoyan.mall.product.mapper.SkuImageMapper;
import com.cskaoyan.mall.product.mapper.SkuInfoMapper;
import com.cskaoyan.mall.product.mapper.SkuPlatformAttrValueMapper;
import com.cskaoyan.mall.product.mapper.SkuSaleAttrValueMapper;
import com.cskaoyan.mall.product.model.SkuImage;
import com.cskaoyan.mall.product.model.SkuInfo;
import com.cskaoyan.mall.product.model.SkuPlatformAttributeValue;
import com.cskaoyan.mall.product.model.SkuSaleAttributeValue;
import com.cskaoyan.mall.product.query.SkuImageParam;
import com.cskaoyan.mall.product.query.SkuInfoParam;
import com.cskaoyan.mall.product.query.SkuPlatformAttributeValueParam;
import com.cskaoyan.mall.product.query.SkuSaleAttributeValueParam;
import com.cskaoyan.mall.product.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    SkuInfoMapper skuInfoMapper;
    @Autowired
    SkuImageMapper skuImageMapper;
    @Autowired
    SkuInfoConverter skuInfoConverter;
    @Autowired
    SkuPlatformAttrValueMapper skuPlatformAttrValueMapper;
    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Override
    public void saveSkuInfo(SkuInfoParam skuInfo) {
        //需要将传递过来的数据插入到四张表中
        //skuInfo
        //skuImage
        //skuAttrValue
        //skuSaleAttrValue
        SkuInfo skuInfoSaveToDatabases = new SkuInfo();
        skuInfoSaveToDatabases.setThirdLevelCategoryId(skuInfo.getCategory3Id());
        skuInfoSaveToDatabases.setSkuDesc(skuInfo.getSkuDesc());
        skuInfoSaveToDatabases.setSkuName(skuInfo.getSkuName());
        skuInfoSaveToDatabases.setSkuDefaultImg(skuInfo.getSkuDefaultImg());
        skuInfoSaveToDatabases.setPrice(skuInfo.getPrice());
        skuInfoSaveToDatabases.setIsSale(skuInfo.getIsSale());
        skuInfoSaveToDatabases.setTmId(skuInfo.getTmId());
        skuInfoSaveToDatabases.setWeight(skuInfo.getWeight());
        skuInfoSaveToDatabases.setSpuId(skuInfo.getSpuId());
        //插入skuInfo表中
        skuInfoMapper.insert(skuInfoSaveToDatabases);
        //此时能得到skuInfo表中刚刚插入的数据的id
        Long skuInfoId = skuInfoSaveToDatabases.getId();

        //接下来处理images
        List<SkuImageParam> skuImageList = skuInfo.getSkuImageList();
        for (SkuImageParam skuImageParam : skuImageList) {
            //一张张插入
//            SkuImage skuImage = new SkuImage();
//            skuImage.setSkuId(skuInfoId);
//            skuImage.setImgUrl(skuImageParam.getImgUrl());
//            skuImage.setIsDefault(skuImageParam.getIsDefault());
            SkuImage skuImage = skuInfoConverter.skuImageP20P(skuImageParam);
            skuImage.setImgUrl(skuImageParam.getImgUrl());
            skuImage.setImgName(skuImageParam.getImgName());
            skuImage.setSkuId(skuInfoId);
            skuImageMapper.insert(skuImage);
        }

        //接下来处理sku_attr_value
        List<SkuPlatformAttributeValueParam> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (SkuPlatformAttributeValueParam skuPlatformAttributeValueParam : skuAttrValueList) {
            //转换一下即可插入
            SkuPlatformAttributeValue skuPlatformAttributeValue = skuInfoConverter.skuPlatformAttributeValueP2O(skuPlatformAttributeValueParam);
            skuPlatformAttributeValue.setSkuId(skuInfoId);
            skuPlatformAttrValueMapper.insert(skuPlatformAttributeValue);
        }

        //接下来处理sku_sale_attr_value
        List<SkuSaleAttributeValueParam> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttributeValueParam skuSaleAttributeValueParam : skuSaleAttrValueList) {
            SkuSaleAttributeValue skuSaleAttributeValue = skuInfoConverter.skuSaleAttributeValueP2O(skuSaleAttributeValueParam);
            skuSaleAttributeValue.setSkuId(skuInfoId);
            skuSaleAttributeValue.setSpuId(skuInfo.getSpuId());
            skuSaleAttributeValue.setSpuSaleAttrValueId(skuSaleAttributeValueParam.getSaleAttrValueId());
            skuSaleAttrValueMapper.insert(skuSaleAttributeValue);
        }


    }

    @Override
    public SkuInfoPageDTO getPage(Page<SkuInfo> pageParam) {
        QueryWrapper<SkuInfo> wrapper = new QueryWrapper<>();
        Page<SkuInfo> skuInfoPage = skuInfoMapper.selectPage(pageParam, wrapper);
        SkuInfoPageDTO skuInfoPageDTO = new SkuInfoPageDTO();
        List<SkuInfo> records = skuInfoPage.getRecords();
        List<SkuInfoDTO> skuInfoDTOS = skuInfoConverter.skuInfoPO2DTOs(records);
        skuInfoPageDTO.setRecords(skuInfoDTOS);
        skuInfoPageDTO.setTotal((int) skuInfoPage.getTotal());
        return skuInfoPageDTO;
    }

    @Override
    public void onSale(Long skuId) {
        QueryWrapper<SkuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("id",skuId);
        SkuInfo skuInfo = skuInfoMapper.selectOne(wrapper);
        skuInfo.setIsSale(1);
        skuInfoMapper.updateById(skuInfo);
    }

    @Override
    public void offSale(Long skuId) {
        QueryWrapper<SkuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("id",skuId);
        SkuInfo skuInfo = skuInfoMapper.selectOne(wrapper);
        skuInfo.setIsSale(0);
        skuInfoMapper.updateById(skuInfo);
    }

    @Override
    public SkuInfoDTO getSkuInfo(Long skuId) {
        QueryWrapper<SkuInfo> skuInfoQueryWrapper = new QueryWrapper<>();
        skuInfoQueryWrapper.eq("id",skuId);
        SkuInfo skuInfo = skuInfoMapper.selectOne(skuInfoQueryWrapper);
        return skuInfoConverter.skuInfoPO2DTO(skuInfo);
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        QueryWrapper<SkuInfo> skuInfoQueryWrapper = new QueryWrapper<>();
        skuInfoQueryWrapper.eq("id",skuId);
        SkuInfo skuInfo = skuInfoMapper.selectOne(skuInfoQueryWrapper);
        return skuInfo.getPrice();
    }

    @Override
    public List<SpuSaleAttributeInfoDTO> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return List.of();
    }

    @Override
    public List<PlatformAttributeInfoDTO> getPlatformAttrInfoBySku(Long skuId) {
        return List.of();
    }
}
