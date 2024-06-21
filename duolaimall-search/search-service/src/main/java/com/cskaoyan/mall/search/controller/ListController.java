package com.cskaoyan.mall.search.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.search.dto.SearchResponseDTO;
import com.cskaoyan.mall.search.param.SearchParam;
import com.cskaoyan.mall.search.service.GoodsSyncService;
import com.cskaoyan.mall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by 北海 on 2023-05-19 16:08
 */
@RestController
public class ListController {


    @Autowired
    private SearchService searchService;
    @Autowired
    GoodsSyncService goodsSyncService;
    /**
     * 搜索商品
     * @param searchParam
     * @return
     * @throws IOException
     */
    @GetMapping("list")
    public Result<SearchResponseDTO> list(SearchParam searchParam) throws IOException {
        goodsSyncService.syncToElasticSearch();
        SearchResponseDTO response = searchService.search(searchParam);
        return Result.ok(response);
    }
}
