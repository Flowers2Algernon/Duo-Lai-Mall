package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cskaoyan.mall.product.converter.dto.CategoryConverter;
import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.mapper.FirstLevelCategoryMapper;
import com.cskaoyan.mall.product.mapper.SecondLevelCategoryMapper;
import com.cskaoyan.mall.product.mapper.ThirdLevelCategoryMapper;
import com.cskaoyan.mall.product.model.FirstLevelCategory;
import com.cskaoyan.mall.product.model.SecondLevelCategory;
import com.cskaoyan.mall.product.model.ThirdLevelCategory;
import com.cskaoyan.mall.product.query.CategoryTrademarkParam;
import com.cskaoyan.mall.product.service.CategoryService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    FirstLevelCategoryMapper firstLevelCategoryMapper;
    @Autowired
    SecondLevelCategoryMapper secondLevelCategoryMapper;
    @Autowired
    ThirdLevelCategoryMapper thirdLevelCategoryMapper;
    @Autowired
    CategoryConverter categoryConverter;

    @Override
    public List<FirstLevelCategoryDTO> getFirstLevelCategory() {
        List<FirstLevelCategory> firstLevelCategories = firstLevelCategoryMapper.selectList(null);
        List<FirstLevelCategoryDTO> firstLevelCategoryDTOS = categoryConverter.firstLevelCategoryPOs2DTOs(firstLevelCategories);
        return firstLevelCategoryDTOS;
    }

    @Override
    public List<SecondLevelCategoryDTO> getSecondLevelCategory(Long firstLevelCategoryId) {
        QueryWrapper<SecondLevelCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("first_level_category_id",firstLevelCategoryId);
        List<SecondLevelCategory> secondLevelCategories = secondLevelCategoryMapper.selectList(wrapper);
        List<SecondLevelCategoryDTO> secondLevelCategoryDTOS = categoryConverter.secondLevelCategoryPOs2DTOs(secondLevelCategories);
        return secondLevelCategoryDTOS;

    }

    @Override
    public List<ThirdLevelCategoryDTO> getThirdLevelCategory(Long secondLevelCategoryId) {
        QueryWrapper<ThirdLevelCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("second_level_category_id",secondLevelCategoryId);
        List<ThirdLevelCategory> thirdLevelCategories = thirdLevelCategoryMapper.selectList(wrapper);
        List<ThirdLevelCategoryDTO> thirdLevelCategoryDTOS = categoryConverter.thirdLevelCategoryPOs2DTOs(thirdLevelCategories);
        return thirdLevelCategoryDTOS;
    }

    @Override
    public List<TrademarkDTO> findTrademarkList(Long category3Id) {
        return List.of();
    }

    @Override
    public void save(CategoryTrademarkParam categoryTrademarkParam) {

    }

    @Override
    public List<TrademarkDTO> findUnLinkedTrademarkList(Long thirdLevelCategoryId) {
        return List.of();
    }

    @Override
    public void remove(Long thirdLevelCategoryId, Long trademarkId) {

    }

    @Override
    public CategoryHierarchyDTO getCategoryViewByCategoryId(Long thirdLevelCategoryId) {
        return null;
    }

    @Override
    public List<FirstLevelCategoryNodeDTO> getCategoryTreeList() {
        return List.of();
    }
}
