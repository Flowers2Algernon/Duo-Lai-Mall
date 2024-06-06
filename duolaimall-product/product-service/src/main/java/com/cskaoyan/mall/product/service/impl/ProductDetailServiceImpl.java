package com.cskaoyan.mall.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cskaoyan.mall.product.converter.dto.CategoryConverter;
import com.cskaoyan.mall.product.converter.dto.SkuInfoConverter;
import com.cskaoyan.mall.product.converter.dto.SpuInfoConverter;
import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.mapper.*;
import com.cskaoyan.mall.product.model.*;
import com.cskaoyan.mall.product.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {
    @Autowired
    SkuInfoMapper skuInfoMapper;
    @Autowired
    SkuImageMapper skuImageMapper;
    @Autowired
    SkuInfoConverter skuInfoConverter;
    @Autowired
    SpuPosterMapper spuPosterMapper;
    @Autowired
    SpuInfoConverter spuInfoConverter;
    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;
    @Autowired
    SpuSaleAttrInfoMapper spuSaleAttrInfoMapper;
    @Autowired
    ThirdLevelCategoryMapper thirdLevelCategoryMapper;
    @Autowired
    SecondLevelCategoryMapper secondLevelCategoryMapper;
    @Autowired
    FirstLevelCategoryMapper firstLevelCategoryMapper;
    @Autowired
    PlatformAttrInfoMapper platformAttrInfoMapper;
    @Autowired
    SkuPlatformAttrValueMapper skuPlatformAttrValueMapper;
    @Autowired
    PlatformAttrValueMapper platformAttrValueMapper;
    @Autowired
    CategoryConverter categoryConverter;
    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Override
    public ProductDetailDTO getItemBySkuId(Long skuId) {
        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        QueryWrapper<SkuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("id", skuId);
        SkuInfo skuInfo = skuInfoMapper.selectOne(wrapper);
        //接下来查询需要插入到skuInfo里面的skuImageList
        QueryWrapper<SkuImage> skuImageQueryWrapper = new QueryWrapper<>();
        skuImageQueryWrapper.eq("sku_id", skuId);
        List<SkuImage> skuImages = skuImageMapper.selectList(skuImageQueryWrapper);
        skuInfo.setSkuImageList(skuImages);

        //设置price
        BigDecimal price = skuInfo.getPrice();
        productDetailDTO.setPrice(price);

        //查询skuInfo里面的skuPlatFormAttrValueList和skuSaleAttributeValueList

        //放入skuInfo
        SkuInfoDTO skuInfoDTO = skuInfoConverter.skuInfoPO2DTO(skuInfo);
        productDetailDTO.setSkuInfo(skuInfoDTO);

        //处理海报信息spuPosterList
        {
            QueryWrapper<SpuPoster> spuPosterQueryWrapper = new QueryWrapper<>();
            spuPosterQueryWrapper.eq("spu_id", skuInfo.getSpuId());
            List<SpuPoster> spuPosters = spuPosterMapper.selectList(spuPosterQueryWrapper);
            List<SpuPosterDTO> spuPosterDTOS = spuInfoConverter.spuPosterPOs2DTOs(spuPosters);
            productDetailDTO.setSpuPosterList(spuPosterDTOS);
        }

        //处理spuSaleAttrList信息
        {
            QueryWrapper<SpuSaleAttributeInfo> infoQueryWrapper = new QueryWrapper<>();
            infoQueryWrapper.eq("spu_id", skuInfo.getSpuId());
            //处理isChecked问题
            QueryWrapper<SkuSaleAttributeValue> skuSaleAttributeValueQueryWrapper = new QueryWrapper<>();
            skuSaleAttributeValueQueryWrapper.eq("sku_id",skuId);
            List<SkuSaleAttributeValue> skuSaleAttributeValues = skuSaleAttrValueMapper.selectList(skuSaleAttributeValueQueryWrapper);
            ArrayList<Long> integers = new ArrayList<>();
            for (SkuSaleAttributeValue skuSaleAttributeValue : skuSaleAttributeValues) {
                Long spuSaleAttrValueId = skuSaleAttributeValue.getSpuSaleAttrValueId();
                integers.add(spuSaleAttrValueId);
            }
            //上述代码可以得到该商品的sku_sale_attr_value值
            List<SpuSaleAttributeInfo> spuSaleAttributeInfos = spuSaleAttrInfoMapper.selectList(infoQueryWrapper);
            for (SpuSaleAttributeInfo spuSaleAttributeInfo : spuSaleAttributeInfos) {
                QueryWrapper<SpuSaleAttributeValue> valueQueryWrapper = new QueryWrapper<>();
                valueQueryWrapper.eq("spu_sale_attr_id", spuSaleAttributeInfo.getId());
                List<SpuSaleAttributeValue> spuSaleAttributeValues = spuSaleAttrValueMapper.selectList(valueQueryWrapper);
                for (SpuSaleAttributeValue spuSaleAttributeValue : spuSaleAttributeValues) {
                    //此处解决isChecked问题,当表中getId与根据skuId在sku_sale_attr_vale中选择出的spu_sale_attr_value的id相同时，标记为checked
                    if (integers.contains(spuSaleAttributeValue.getId())){
                        //是商品的属性之一，标记为checked
                        spuSaleAttributeValue.setIsChecked(String.valueOf(1));
                    }
                }
                spuSaleAttributeInfo.setSpuSaleAttrValueList(spuSaleAttributeValues);
            }
            List<SpuSaleAttributeInfoDTO> spuSaleAttributeInfoDTOS = spuInfoConverter.spuSaleAttributeInfoPOs2DTOs(spuSaleAttributeInfos);
            productDetailDTO.setSpuSaleAttrList(spuSaleAttributeInfoDTOS);
        }

        //处理categoryHierarchy
        //从sku_id中的thirdLevelCategoryId倒推一级和二级ID
        {
            CategoryHierarchy categoryHierarchy = new CategoryHierarchy();
            //获取三级目录名称及编号
            Long thirdLevelCategoryId = skuInfo.getThirdLevelCategoryId();
            categoryHierarchy.setThirdLevelCategoryId(thirdLevelCategoryId);
            QueryWrapper<ThirdLevelCategory> thirdLevelCategoryQueryWrapper = new QueryWrapper<>();
            thirdLevelCategoryQueryWrapper.eq("id", thirdLevelCategoryId);
            ThirdLevelCategory thirdLevelCategory = thirdLevelCategoryMapper.selectOne(thirdLevelCategoryQueryWrapper);
            //获取二级目录名称及编号
            categoryHierarchy.setThirdLevelCategoryName(thirdLevelCategory.getName());
            Long secondLevelCategoryId = thirdLevelCategory.getSecondLevelCategoryId();
            QueryWrapper<SecondLevelCategory> secondLevelCategoryQueryWrapper = new QueryWrapper<>();
            secondLevelCategoryQueryWrapper.eq("id", secondLevelCategoryId);
            SecondLevelCategory secondLevelCategory = secondLevelCategoryMapper.selectOne(secondLevelCategoryQueryWrapper);
            categoryHierarchy.setSecondLevelCategoryId(secondLevelCategoryId);
            categoryHierarchy.setSecondLevelCategoryName(secondLevelCategory.getName());
            //获取一级目录名称及编号
            Long firstLevelCategoryId = secondLevelCategory.getFirstLevelCategoryId();
            QueryWrapper<FirstLevelCategory> firstLevelCategoryQueryWrapper = new QueryWrapper<>();
            firstLevelCategoryQueryWrapper.eq("id", firstLevelCategoryId);
            FirstLevelCategory firstLevelCategory = firstLevelCategoryMapper.selectOne(firstLevelCategoryQueryWrapper);
            categoryHierarchy.setFirstLevelCategoryId(firstLevelCategoryId);
            categoryHierarchy.setFirstLevelCategoryName(firstLevelCategory.getName());
            CategoryHierarchyDTO categoryHierarchyDTO = categoryConverter.categoryViewPO2DTO(categoryHierarchy);
            productDetailDTO.setCategoryHierarchy(categoryHierarchyDTO);
        }

        //处理skuAttrList
        {
            //可以直接使用下述写好的sql语句查询
//        List<PlatformAttributeInfo> platformAttributeInfos = platformAttrInfoMapper.selectPlatformAttrInfoListBySkuId(skuId);
//        productDetailDTO.setSkuAttrList(platformAttributeInfos);
            //也可以使用多mapper多步骤查询
            QueryWrapper<SkuPlatformAttributeValue> skuPlatformAttributeValueQueryWrapper = new QueryWrapper<>();
            skuPlatformAttributeValueQueryWrapper.eq("sku_id", skuId);
            List<SkuPlatformAttributeValue> skuPlatformAttributeValues = skuPlatformAttrValueMapper.selectList(skuPlatformAttributeValueQueryWrapper);
            List<SkuSpecification> skuSpecifications = new ArrayList<>();
            for (SkuPlatformAttributeValue skuPlatformAttributeValue : skuPlatformAttributeValues) {
                //对每一个sku_plat_attr_value而言，都有不同的attrId和valueId对应
                SkuSpecification skuSpecification = new SkuSpecification();
                QueryWrapper<PlatformAttributeInfo> platformAttributeInfoQueryWrapper = new QueryWrapper<>();
                platformAttributeInfoQueryWrapper.eq("id", skuPlatformAttributeValue.getAttrId());
                PlatformAttributeInfo platformAttributeInfo = platformAttrInfoMapper.selectOne(platformAttributeInfoQueryWrapper);
                if (platformAttributeInfo != null) {//todo 空指针
                    skuSpecification.setAttrName(platformAttributeInfo.getAttrName());
                }
                //查询valueName
                QueryWrapper<PlatformAttributeValue> platformAttributeValueQueryWrapper = new QueryWrapper<>();
                platformAttributeValueQueryWrapper.eq("id", skuPlatformAttributeValue.getValueId());
                PlatformAttributeValue platformAttributeValue = platformAttrValueMapper.selectOne(platformAttributeValueQueryWrapper);
                if (platformAttributeValue != null) {//todo 出现空指针
                    skuSpecification.setAttrValue(platformAttributeValue.getValueName());
                }
                skuSpecifications.add(skuSpecification);
            }
            productDetailDTO.setSkuAttrList(skuSpecifications);
        }

        //处理valuesSkuJson
        {
            List<SkuSaleAttributeValuePermutation> skuSaleAttributeValuePermutations = skuSaleAttrValueMapper.selectSaleAttrValuesBySpu(skuInfo.getSpuId());
            Map<String, Long> collect = skuSaleAttributeValuePermutations.stream().collect(Collectors.toMap(SkuSaleAttributeValuePermutation::getSkuSaleAttrValuePermutation, SkuSaleAttributeValuePermutation::getSkuId));
            String json = JSONArray.toJSONString(collect);
            productDetailDTO.setValuesSkuJson(json);
        }
        return productDetailDTO;
    }
}
