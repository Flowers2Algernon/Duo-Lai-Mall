package com.cskaoyan.mall.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.model.FirstLevelCategory;
import com.cskaoyan.mall.product.model.Trademark;
import com.cskaoyan.mall.product.query.PlatformAttributeParam;
import com.cskaoyan.mall.product.query.TrademarkParam;
import com.cskaoyan.mall.product.service.CategoryService;
import com.cskaoyan.mall.product.service.PlatformAttributeService;
import com.cskaoyan.mall.product.service.TrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminProductController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    PlatformAttributeService platformAttributeService;
    @Autowired
    TrademarkService trademarkService;

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
        List<PlatformAttributeValueDTO> platformAttrInfo = platformAttributeService.getPlatformAttrInfo(attrId);
        return Result.ok(platformAttrInfo);
    }

    // http://localhost/admin/product/baseTrademark/1/10
    // 查看品牌列表
    @GetMapping("/admin/product/baseTrademark/{pageNo}/{pageSize}")
    public Result<TrademarkPageDTO> getTradeMarkDTOList(@PathVariable Long pageNo, @PathVariable Long pageSize) {
        Page<Trademark> page = new Page<>(pageNo,pageSize);
        TrademarkPageDTO pageDTO = trademarkService.getPage(page);
        return Result.ok(pageDTO);
    }


    // 保存品牌
//http://localhost/admin/product/baseTrademark/save
    @PostMapping("/admin/product/baseTrademark/save")
    public Result save(@RequestBody TrademarkParam trademarkParam){
        trademarkService.save(trademarkParam);
        return Result.ok();
    }

    // http://localhost/admin/product/baseTrademark/remove/10
// 删除品牌
    @DeleteMapping("/admin/product/baseTrademark/remove/{tradeMarkId}")
    public Result deleteById(@PathVariable Long tradeMarkId){
        trademarkService.removeById(tradeMarkId);
        return Result.ok();
    }


    // http://localhost/admin/product/baseTrademark/get/17
// 查询品牌
    @GetMapping("/admin/product/baseTrademark/get/{tradeMarkId}")
    public Result<TrademarkDTO> getTradeMarkDTO(@PathVariable Long tradeMarkId) {
        TrademarkDTO trademarkDTO = trademarkService.getTrademarkByTmId(tradeMarkId);
        return Result.ok(trademarkDTO);
    }

    // 修改品牌
// http://localhost/admin/product/baseTrademark/update
    @PutMapping("/admin/product/baseTrademark/update")
    public Result updateTradeMark(@RequestBody TrademarkParam trademarkParam){
        trademarkService.updateById(trademarkParam);
        return Result.ok();
    }

}
