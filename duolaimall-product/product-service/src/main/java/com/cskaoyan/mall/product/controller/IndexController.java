package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.dto.FirstLevelCategoryNodeDTO;
import com.cskaoyan.mall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 北海 on 2023-05-19 14:47
 */
@RestController
@Slf4j
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @GetMapping({"/","index"})
    public Result<List<FirstLevelCategoryNodeDTO>> index(){
        log.info("enter {} for {}", IndexController.class.getSimpleName(), "index");
        // 获取首页分类数据
        List<FirstLevelCategoryNodeDTO> categoryTreeList = categoryService.getCategoryTreeList();
        return Result.ok(categoryTreeList);
    }
}
