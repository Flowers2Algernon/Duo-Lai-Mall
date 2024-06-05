package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.product.converter.dto.SpuInfoConverter;
import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.mapper.*;
import com.cskaoyan.mall.product.model.*;
import com.cskaoyan.mall.product.query.*;
import com.cskaoyan.mall.product.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    SpuInfoMapper spuInfoMapper;
    @Autowired
    SpuInfoConverter spuInfoConverter;
    @Autowired
    SpuImageMapper spuImageMapper;
    @Autowired
    SpuPosterMapper spuPosterMapper;
    @Autowired
    SpuSaleAttrInfoMapper spuSaleAttrInfoMapper;
    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;
    @Override
    public SpuInfoPageDTO getSpuInfoPage(Page<SpuInfo> pageParam, Long category3Id) {
        //获取某一个三级分类下的spuInfo
        QueryWrapper<SpuInfo> spuInfoQueryWrapper = new QueryWrapper<>();
        spuInfoQueryWrapper.eq("third_level_category_id",category3Id);
        Page<SpuInfo> spuInfoPage = spuInfoMapper.selectPage(pageParam, spuInfoQueryWrapper);
        long total = spuInfoPage.getTotal();
        List<SpuInfo> records = spuInfoPage.getRecords();
        SpuInfoPageDTO spuInfoPageDTO = new SpuInfoPageDTO();
        spuInfoPageDTO.setTotal((int) total);
        //转换成所需要的DTO类型
        List<SpuInfoDTO> spuInfoDTOS = spuInfoConverter.spuInfoPO2DTOs(records);
        spuInfoPageDTO.setRecords(spuInfoDTOS);
        return spuInfoPageDTO;
    }

    @Override
    public void saveSpuInfo(SpuInfoParam spuInfo) {
        SpuInfo spuInfoSaveToDatabase = new SpuInfo();
        spuInfoSaveToDatabase.setSpuName(spuInfo.getSpuName());
        spuInfoSaveToDatabase.setDescription(spuInfo.getDescription());
        spuInfoSaveToDatabase.setThirdLevelCategoryId(spuInfo.getCategory3Id());
        spuInfoSaveToDatabase.setTmId(spuInfo.getTmId());
        //spuInfo表能插入的到此为止
        spuInfoMapper.insert(spuInfoSaveToDatabase);
        //此时insert进去之后就有了id--mybatis封装了处理细节，直接拿来用就行
        Long spuInfoId = spuInfoSaveToDatabase.getId();//todo 观察是否有值
        //接下来处理spuImageList
        List<SpuImageParam> spuImageList = spuInfo.getSpuImageList();
        for (SpuImageParam spuImageParam : spuImageList) {
            SpuImage spuImage = new SpuImage();
            spuImage.setSpuId(spuInfoId);
            spuImage.setImgName(spuImageParam.getImgName());
            spuImage.setImgUrl(spuImageParam.getImgUrl());
            spuImageMapper.insert(spuImage);
        }
        //接下来处理spuPostList
        List<SpuPosterParam> spuPosterList = spuInfo.getSpuPosterList();
        for (SpuPosterParam spuPosterParam : spuPosterList) {
            SpuPoster spuPoster = new SpuPoster();
            spuPoster.setSpuId(spuInfoId);
            spuPoster.setImgUrl(spuPosterParam.getImgUrl());
            spuPoster.setImgName(spuPosterParam.getImgName());
            spuPosterMapper.insert(spuPoster);
        }
        //接下来处理spuSaleAttrList
        List<SpuSaleAttributeInfoParam> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        for (SpuSaleAttributeInfoParam spuSaleAttributeInfoParam : spuSaleAttrList) {
            SpuSaleAttributeInfo spuSaleAttributeInfo = new SpuSaleAttributeInfo();
            spuSaleAttributeInfo.setSpuId(spuInfoId);
            spuSaleAttributeInfo.setSaleAttrId(spuSaleAttributeInfoParam.getBaseSaleAttrId());
            spuSaleAttributeInfo.setSaleAttrName(spuSaleAttributeInfoParam.getSaleAttrName());
            //至此spu_sale_attr_info能插入的数据结束
            //插入上述对象到数据库中
            spuSaleAttrInfoMapper.insert(spuSaleAttributeInfo);
            Long saleAttributeInfoId = spuSaleAttributeInfo.getId();
            //接下来处理spu-sale-attr-value
            List<SpuSaleAttributeValueParam> spuSaleAttrValueList = spuSaleAttributeInfoParam.getSpuSaleAttrValueList();
            for (SpuSaleAttributeValueParam spuSaleAttributeValueParam : spuSaleAttrValueList) {
                SpuSaleAttributeValue spuSaleAttributeValue = new SpuSaleAttributeValue();
                spuSaleAttributeValue.setSpuId(spuInfoId);
                spuSaleAttributeValue.setSpuSaleAttrId(saleAttributeInfoId);
                spuSaleAttributeValue.setSpuSaleAttrValueName(spuSaleAttributeValueParam.getSaleAttrValueName());
                //插入到数据库中
                spuSaleAttrValueMapper.insert(spuSaleAttributeValue);
            }
        }
    }

    @Override
    public List<SpuImageDTO> getSpuImageList(Long spuId) {
        return List.of();
    }

    @Override
    public List<SpuSaleAttributeInfoDTO> getSpuSaleAttrList(Long spuId) {
        return List.of();
    }

    @Override
    public List<SpuPosterDTO> findSpuPosterBySpuId(Long spuId) {
        return List.of();
    }

    @Override
    public Map<String, Long> getSkuValueIdsMap(Long spuId) {
        return Map.of();
    }
}
