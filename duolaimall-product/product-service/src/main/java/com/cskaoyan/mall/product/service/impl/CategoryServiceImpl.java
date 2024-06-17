package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cskaoyan.mall.product.converter.dto.CategoryConverter;
import com.cskaoyan.mall.product.converter.dto.CategoryTradeConvert;
import com.cskaoyan.mall.product.converter.dto.TrademarkConverter;
import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.mapper.*;
import com.cskaoyan.mall.product.model.*;
import com.cskaoyan.mall.product.query.CategoryTrademarkParam;
import com.cskaoyan.mall.product.service.CategoryService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    @Autowired
    CategoryTrademarkMapper categoryTrademarkMapper;
    @Autowired
    CategoryTradeConvert categoryTradeConvert;
    @Autowired
    TrademarkMapper trademarkMapper;
    @Autowired
    TrademarkConverter trademarkConverter;
    @Qualifier("categoryHierarchyMapper")
    @Autowired
    private CategoryHierarchyMapper categoryHierarchyMapper;

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
        QueryWrapper<CategoryTrademark> wrapper = new QueryWrapper<>();
        wrapper.eq("third_level_category_id",category3Id);
        List<CategoryTrademark> categoryTrademarks = categoryTrademarkMapper.selectList(wrapper);
        ArrayList<Trademark> trademarks = new ArrayList<>();
        for (CategoryTrademark categoryTrademark : categoryTrademarks) {
            QueryWrapper<Trademark> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("id",categoryTrademark.getTrademarkId());
            Trademark trademark = trademarkMapper.selectOne(wrapper1);
            if (trademark!=null){
                trademarks.add(trademark);
            }
        }
        List<TrademarkDTO> trademarkDTOS = trademarkConverter.trademarkPOs2DTOs(trademarks);
        return trademarkDTOS;
    }

    @Override
    public void save(CategoryTrademarkParam categoryTrademarkParam) {
        //有两张表需要插入数据
        //先是插入categoryTrademark表--表示映射关系
        //接下来插入trademark表，表示具体的映射？--无需插入具体的表，因为具体的trade表中已在品牌页进行了插入，做好映射管理即可
        List<Long> trademarkIdList = categoryTrademarkParam.getTrademarkIdList();
        for (Long l : trademarkIdList) {
            CategoryTrademark categoryTrademark = new CategoryTrademark();
            categoryTrademark.setThirdLevelCategoryId(categoryTrademarkParam.getCategory3Id());
            categoryTrademark.setTrademarkId(l);
            categoryTrademarkMapper.insert(categoryTrademark);
        }
    }

    @Override
    public List<TrademarkDTO> findUnLinkedTrademarkList(Long thirdLevelCategoryId) {
        //首先查找所有该categoryID下的品牌
        QueryWrapper<CategoryTrademark> wrapper = new QueryWrapper<>();
        wrapper.eq("third_level_category_id",thirdLevelCategoryId);
        List<CategoryTrademark> allCategoryTrademarkList = categoryTrademarkMapper.selectList(wrapper);
        //此处得到已有的trademarkId
        ArrayList<Long> alreadyExistsTrade = new ArrayList<>();
        for (CategoryTrademark categoryTrademark : allCategoryTrademarkList) {
            alreadyExistsTrade.add(categoryTrademark.getTrademarkId());
        }
        //接下来查找所有品牌，从中剔除已关联的品牌
        QueryWrapper<Trademark> queryWrapper = new QueryWrapper<>();
        List<Trademark> trademarks = trademarkMapper.selectList(queryWrapper);
        List<Trademark> list = new ArrayList<>();
        for (Trademark trademark : trademarks) {
            if (alreadyExistsTrade.contains(trademark.getId())){
                continue;
            }else {
                list.add(trademark);
            }
        }
        //转换
        List<TrademarkDTO> trademarkDTOS = trademarkConverter.trademarkPOs2DTOs(list);
        return trademarkDTOS;
    }

    @Override
    public void remove(Long thirdLevelCategoryId, Long trademarkId) {
        QueryWrapper<CategoryTrademark> wrapper = new QueryWrapper<>();
        wrapper.eq("third_level_category_id",thirdLevelCategoryId).eq("trademark_id",trademarkId);
        CategoryTrademark categoryTrademark = categoryTrademarkMapper.selectOne(wrapper);
        categoryTrademark.setIsDeleted(1);
        categoryTrademarkMapper.deleteById(categoryTrademark);
    }

    @Override
    public CategoryHierarchyDTO getCategoryViewByCategoryId(Long thirdLevelCategoryId) {
        return null;
    }

    @Override
    public List<FirstLevelCategoryNodeDTO> getCategoryTreeList() {
        ArrayList<FirstLevelCategoryNodeDTO> firstLevelCategoryNodeDTOS = new ArrayList<>();
        List<CategoryHierarchy> categoryHierarchies = categoryHierarchyMapper.selectCategoryHierarchy(null);
        Map<Long, List<CategoryHierarchy>> firstLevelCategoryMap = categoryHierarchies.stream().collect(Collectors.groupingBy(CategoryHierarchy::getFirstLevelCategoryId));
        //获取一级分类下的所有数据
        for (Map.Entry<Long, List<CategoryHierarchy>> firstLevelEntry : firstLevelCategoryMap.entrySet()) {
            //获取一级分类id
            Long firstLevelEntryKey = firstLevelEntry.getKey();
            //获取一级分类下方的所有分类
            List<CategoryHierarchy> firstLevelEntryValue = firstLevelEntry.getValue();
            FirstLevelCategoryNodeDTO firstLevelCategoryNodeDTO = new FirstLevelCategoryNodeDTO();
            firstLevelCategoryNodeDTO.setCategoryId(firstLevelEntryKey);
            firstLevelCategoryNodeDTO.setCategoryName(firstLevelEntryValue.get(0).getFirstLevelCategoryName());
            List<SecondLevelCategoryNodeDTO> secondLevelCategoryNodes = buildSecondLevelCategoryNodeDTO(firstLevelEntryValue);
            firstLevelCategoryNodeDTO.setCategoryChild(secondLevelCategoryNodes);
            firstLevelCategoryNodeDTOS.add(firstLevelCategoryNodeDTO);
        }
        return firstLevelCategoryNodeDTOS;
    }

    private List<SecondLevelCategoryNodeDTO> buildSecondLevelCategoryNodeDTO(List<CategoryHierarchy> firstLevelEntryValue) {
        ArrayList<SecondLevelCategoryNodeDTO> secondLevelCategoryNodeDTOS = new ArrayList<>();
        //循环获取二级分类数据
        Map<Long, List<CategoryHierarchy>> firstLevelCategoryChildMap = firstLevelEntryValue.stream().collect(Collectors.groupingBy(CategoryHierarchy::getSecondLevelCategoryId));
        for (Map.Entry<Long, List<CategoryHierarchy>> secondLevelEntry : firstLevelCategoryChildMap.entrySet()) {
            Long secondLevelCategoryId = secondLevelEntry.getKey();
            List<CategoryHierarchy> secondLevelCategories = secondLevelEntry.getValue();
            SecondLevelCategoryNodeDTO secondLevelCategoryNodeDTO = new SecondLevelCategoryNodeDTO();
            secondLevelCategoryNodeDTO.setCategoryId(secondLevelCategoryId);
            secondLevelCategoryNodeDTO.setCategoryName(secondLevelCategories.get(0).getSecondLevelCategoryName());
            List<ThirdLevelCategoryNodeDTO> thirdLevelCategoryNodes =buildThirdLevelCategoryNodes(secondLevelCategories);
            secondLevelCategoryNodeDTO.setCategoryChild(thirdLevelCategoryNodes);
            secondLevelCategoryNodeDTOS.add(secondLevelCategoryNodeDTO);
        }
        return secondLevelCategoryNodeDTOS;
    }

    private List<ThirdLevelCategoryNodeDTO> buildThirdLevelCategoryNodes(List<CategoryHierarchy> secondLevelCategories) {
        List<ThirdLevelCategoryNodeDTO> collect = secondLevelCategories.stream().map(categoryHierarchy -> {
            ThirdLevelCategoryNodeDTO thirdLevelCategoryNodeDTO = new ThirdLevelCategoryNodeDTO();
            thirdLevelCategoryNodeDTO.setCategoryId(categoryHierarchy.getThirdLevelCategoryId());
            thirdLevelCategoryNodeDTO.setCategoryName(categoryHierarchy.getThirdLevelCategoryName());
            return thirdLevelCategoryNodeDTO;
        }).collect(Collectors.toList());
        return collect;
    }
}
