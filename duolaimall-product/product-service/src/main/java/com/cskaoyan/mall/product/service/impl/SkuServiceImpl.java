package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.common.cache.RedisCache;
import com.cskaoyan.mall.common.constant.RedisConst;
import com.cskaoyan.mall.mq.constant.MqTopicConst;
import com.cskaoyan.mall.mq.producer.BaseProducer;
import com.cskaoyan.mall.product.converter.dto.PlatformAttributeInfoConverter;
import com.cskaoyan.mall.product.converter.dto.SkuInfoConverter;
import com.cskaoyan.mall.product.converter.dto.SpuInfoConverter;
import com.cskaoyan.mall.product.dto.PlatformAttributeInfoDTO;
import com.cskaoyan.mall.product.dto.SkuInfoDTO;
import com.cskaoyan.mall.product.dto.SkuInfoPageDTO;
import com.cskaoyan.mall.product.dto.SpuSaleAttributeInfoDTO;
import com.cskaoyan.mall.product.mapper.*;
import com.cskaoyan.mall.product.model.*;
import com.cskaoyan.mall.product.query.SkuImageParam;
import com.cskaoyan.mall.product.query.SkuInfoParam;
import com.cskaoyan.mall.product.query.SkuPlatformAttributeValueParam;
import com.cskaoyan.mall.product.query.SkuSaleAttributeValueParam;
import com.cskaoyan.mall.product.service.SkuService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    BaseProducer producer;
    @Autowired
    SpuSaleAttrInfoMapper spuSaleAttrInfoMapper;
    @Autowired
    SpuInfoConverter spuInfoConverter;
    @Autowired
    PlatformAttrInfoMapper platformAttrInfoMapper;
    @Autowired
    PlatformAttributeInfoConverter platformAttributeInfoConverter;

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
        //更改销售状态
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(1);
        skuInfoMapper.updateById(skuInfo);

        String getSkuInfoKey = getRedisKey(SkuServiceImpl.class, "getSkuInfo", skuId);
        redissonClient.getBucket(getSkuInfoKey).delete();

        //添加布隆过滤
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER);
        bloomFilter.add(skuId);

        //发送消息，在es中添加该商品信息
        producer.sendMessage(MqTopicConst.PRODUCT_ONSALE_TOPIC, skuId);
    }

    private String getRedisKey(Class clz, String methodName, Object... args) {
        Stream<Object> stream = Arrays.asList(args).stream();
        Stream<? extends Class<?>> classStream = stream.map(obj -> obj.getClass());
        List<? extends Class<?>> argTypes = classStream.collect(Collectors.toList());
        Class[] classes = argTypes.toArray(new Class[0]);
        Method declaredMethod = null;
        try {
            declaredMethod = clz.getDeclaredMethod(methodName, classes);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        declaredMethod.setAccessible(true);
        RedisCache annotation = declaredMethod.getAnnotation(RedisCache.class);
        return annotation.prefix() + Arrays.asList(args);
    }

    @Override
    public void offSale(Long skuId) {
        //更改销售状态
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        skuInfo.setIsSale(0);
        skuInfoMapper.updateById(skuInfo);

        //删除redis缓存中的数据
        String skuInfoKey = getRedisKey(SkuServiceImpl.class, "getSkuInfo", skuId);
        redissonClient.getBucket(skuInfoKey).delete();

        String spuSaleAttrListKey = getRedisKey(SkuServiceImpl.class, "getSpuSaleAttrListCheckBySku",
                skuId, skuInfo.getSpuId());
        redissonClient.getBucket(spuSaleAttrListKey).delete();

        String platformAttrListKey = getRedisKey(SkuServiceImpl.class, "getPlatformAttrInfoBySku", skuId);
        redissonClient.getBucket(platformAttrListKey).delete();

        String skuPosterListKey = getRedisKey(SpuServiceImpl.class, "findSpuPosterBySpuId", skuInfo.getSpuId());
        redissonClient.getBucket(skuPosterListKey).delete();

        String skuValueIdsMapKey = getRedisKey(SpuServiceImpl.class, "getSkuValueIdsMap", skuInfo.getSpuId());
        redissonClient.getBucket(skuValueIdsMapKey).delete();


        String categoryViewKey = getRedisKey(CategoryServiceImpl.class, "getCategoryViewByCategoryId", skuInfo.getThirdLevelCategoryId());
        redissonClient.getBucket(categoryViewKey).delete();

        //发送消息，在es中删除该条商品消息
        producer.sendMessage(MqTopicConst.PRODUCT_OFFSALE_TOPIC, skuId);
    }
    @RedisCache(prefix = RedisConst.SKUKEY_PREFIX)
    @Override
    public SkuInfoDTO getSkuInfo(Long skuId) {
        LambdaQueryWrapper<SkuInfo> skuInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        skuInfoLambdaQueryWrapper.eq(SkuInfo::getIsSale,1)
                .eq(SkuInfo::getId,skuId);
        List<SkuInfo> skuInfos = skuInfoMapper.selectList(skuInfoLambdaQueryWrapper);
        if (skuInfos==null||skuInfos.isEmpty()){
            return new SkuInfoDTO();
        }
        SkuInfo skuInfo = skuInfos.get(0);
        //根据skuId，查询图片列表集合
        List<SkuImage> skuImages = skuImageMapper.getSkuImages(skuId);
        skuInfo.setSkuImageList(skuImages);
        return skuInfoConverter.skuInfoPO2DTO(skuInfo);
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        QueryWrapper<SkuInfo> skuInfoQueryWrapper = new QueryWrapper<>();
        skuInfoQueryWrapper.eq("id", skuId);
        SkuInfo skuInfo = skuInfoMapper.selectOne(skuInfoQueryWrapper);
        if (null != skuInfo.getPrice()) {
            return skuInfo.getPrice();
        }
        return new BigDecimal("0");
    }

    @RedisCache(prefix = "spuSaleAttrListCheckBySku:")
    @Override
    public List<SpuSaleAttributeInfoDTO> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        List<SpuSaleAttributeInfo> spuSaleAttributeInfos = spuSaleAttrInfoMapper.selectSpuSaleAttrListCheckedBySku(skuId, spuId);
        return spuInfoConverter.spuSaleAttributeInfoPOs2DTOs(spuSaleAttributeInfos);
    }
    @RedisCache(prefix = "platformAttributeInfoList:")
    @Override
    public List<PlatformAttributeInfoDTO> getPlatformAttrInfoBySku(Long skuId) {
        List<PlatformAttributeInfo> platformAttributeInfos = platformAttrInfoMapper.selectPlatformAttrInfoListBySkuId(skuId);
        List<PlatformAttributeInfoDTO> platformAttributeInfoDTOS = platformAttributeInfoConverter.platformAttributeInfoPOs2DTOs(platformAttributeInfos);
        return platformAttributeInfoDTOS;
    }
}
