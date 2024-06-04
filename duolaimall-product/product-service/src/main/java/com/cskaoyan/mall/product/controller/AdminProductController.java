package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.model.FirstLevelCategory;
import com.cskaoyan.mall.product.query.PlatformAttributeParam;
import com.cskaoyan.mall.product.service.CategoryService;
import com.cskaoyan.mall.product.service.PlatformAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminProductController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    PlatformAttributeService platformAttributeService;

    @GetMapping("admin/product/getCategory1")
    public Result<List<FirstLevelCategoryDTO>> getCategory1(){
        List<FirstLevelCategoryDTO> firstLevelCategory = categoryService.getFirstLevelCategory();
        return Result.ok(firstLevelCategory);
    }

    // 根据一级分类查询二级分类
    @GetMapping("/admin/product/getCategory2/{firstLevelCategoryId}")
    public Result<List<SecondLevelCategoryDTO>> getCategory2(@PathVariable Long firstLevelCategoryId){
        List<SecondLevelCategoryDTO> secondLevelCategory = categoryService.getSecondLevelCategory(firstLevelCategoryId);
        return Result.ok(secondLevelCategory);
    }

    // 根据二级分类，查询三级分类
    @GetMapping("/admin/product/getCategory3/{category2Id}")
    public Result<List<ThirdLevelCategoryDTO>> getCategory3(@PathVariable Long category2Id){
        List<ThirdLevelCategoryDTO> thirdLevelCategory = categoryService.getThirdLevelCategory(category2Id);
        return Result.ok(thirdLevelCategory);
    }
    // 根据分类Id查询平台属性以及平台属性值
    //返回的是连表查询的结果--attr_info和attr_value表，其中要求attr_value表中attr_id要求和attr_info表中的id一致
// http://localhost/admin/product/attrInfoList/3/20/149
    @GetMapping("/admin/product/attrInfoList/{firstLevelCategoryId}/{secondLevelCategoryId}/{thirdLevelCategoryId}")
    public Result getAttrInfoList(@PathVariable Long firstLevelCategoryId,
                                  @PathVariable Long secondLevelCategoryId,
                                  @PathVariable Long thirdLevelCategoryId){
        List<PlatformAttributeInfoDTO> platformAttrInfoList = platformAttributeService.getPlatformAttrInfoList(firstLevelCategoryId, secondLevelCategoryId, thirdLevelCategoryId);
        return Result.ok(platformAttrInfoList);
    }

    // 保存平台属性
//  http://localhost/admin/product/saveAttrInfo
    @PostMapping("/admin/product/saveAttrInfo")
    public Result saveAttrInfo(@RequestBody PlatformAttributeParam platformAttributeParam) {
        platformAttributeService.savePlatformAttrInfo(platformAttributeParam);
        return Result.ok();
    }
    // http://localhost/admin/product/getAttrValueList/106
// 平台属性值回显
    @GetMapping("/admin/product/getAttrValueList/{attrId}")
    public Result<List<PlatformAttributeValueDTO>> getAttrInfoDTO(@PathVariable Long attrId) {
        return null;
    }

}
