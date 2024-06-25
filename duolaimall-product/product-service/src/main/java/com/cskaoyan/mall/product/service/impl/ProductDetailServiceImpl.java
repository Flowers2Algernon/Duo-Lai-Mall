package com.cskaoyan.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.cskaoyan.mall.common.constant.RedisConst;
import com.cskaoyan.mall.product.client.SearchApiClient;
import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.service.CategoryService;
import com.cskaoyan.mall.product.service.ProductDetailService;
import com.cskaoyan.mall.product.service.SkuService;
import com.cskaoyan.mall.product.service.SpuService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
@Service
@Slf4j
public class ProductDetailServiceImpl implements ProductDetailService {

    @Autowired
    SpuService spuService;

    @Autowired
    SkuService skuService;

    @Autowired
    CategoryService categoryService;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    SearchApiClient searchApiClient;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public ProductDetailDTO getItemBySkuId(Long skuId) {

        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
//        final RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER);
//        if (!bloomFilter.contains(skuId)) {
//            return productDetailDTO;
//        }


        CompletableFuture<SkuInfoDTO> skuCompletableFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfoDTO skuInfo = skuService.getSkuInfo(skuId);
            // 保存skuInfo
            productDetailDTO.setSkuInfo(skuInfo);
            return skuInfo;
        });


// 销售属性-销售属性值回显并锁定 thenAcceptAsync 串行化
        CompletableFuture<Void> spuSaleAttrCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuInfo -> {
            List<SpuSaleAttributeInfoDTO> spuSaleAttrListCheckBySku
                    = skuService.getSpuSaleAttrListCheckBySku(skuInfo.getId(), skuInfo.getSpuId());

            // 保存数据
            productDetailDTO.setSpuSaleAttrList(spuSaleAttrListCheckBySku);
        });

//根据spuId 查询map 集合属性
// 销售属性-销售属性值回显并锁定 thenAcceptAsync 串行化
        CompletableFuture<Void> skuValueIdsMapCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuInfo -> {
            Map<String, Long> skuValueIdsMap = spuService.getSkuValueIdsMap(skuInfo.getSpuId());

            String valuesSkuJson = JSON.toJSONString(skuValueIdsMap);
            // 保存valuesSkuJson
            productDetailDTO.setValuesSkuJson(valuesSkuJson);

        });


//获取商品最新价格
        CompletableFuture<Void> skuPriceCompletableFuture = CompletableFuture.runAsync(() -> {
            BigDecimal skuPrice = skuService.getSkuPrice(skuId);
            productDetailDTO.setPrice(skuPrice);

        });


//获取分类信息 thenAcceptAsync 串行化
        CompletableFuture<Void> categoryViewCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuInfo -> {
            CategoryHierarchyDTO categoryViewByCategory = categoryService.getCategoryViewByCategoryId(skuInfo.getThirdLevelCategoryId());
            //分类信息
            productDetailDTO.setCategoryHierarchy(categoryViewByCategory);
        });

//  获取海报数据 thenAcceptAsync 串行化
        CompletableFuture<Void> spuPosterListCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuInfo -> {
            //  spu海报数据
            List<SpuPosterDTO> spuPosterBySpuId = spuService.findSpuPosterBySpuId(skuInfo.getSpuId());
            productDetailDTO.setSpuPosterList(spuPosterBySpuId);

        });

//  获取sku平台属性，即规格数据
        CompletableFuture<Void> skuAttrListCompletableFuture = CompletableFuture.runAsync(() -> {
            List<PlatformAttributeInfoDTO> platformAttrInfoBySku = skuService.getPlatformAttrInfoBySku(skuId);
            List<SkuSpecification> skuAttrList = platformAttrInfoBySku.stream().map((baseAttrInfo) -> {
                //
                SkuSpecification skuSpecification = new SkuSpecification();
                skuSpecification.setAttrName(baseAttrInfo.getAttrName());
                skuSpecification.setAttrValue(baseAttrInfo.getAttrValueList().get(0).getValueName());
                return skuSpecification;
            }).collect(Collectors.toList());
            productDetailDTO.setSkuAttrList(skuAttrList);
        });

        CompletableFuture.runAsync(() -> {
            searchApiClient.incrHotScore(skuId);
        });

// 等待所有异步任务执行完毕
        CompletableFuture.allOf(skuCompletableFuture, spuSaleAttrCompletableFuture,
                skuValueIdsMapCompletableFuture, skuPriceCompletableFuture,
                categoryViewCompletableFuture, spuPosterListCompletableFuture, skuAttrListCompletableFuture).join();
        return productDetailDTO;
    }

}
