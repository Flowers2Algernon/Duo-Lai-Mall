package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.service.TestLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestLockController {

    @Autowired
    TestLockService testLockService;

    @GetMapping("admin/product/lock")
    public Result testLock(){
        testLockService.incrWithLock();
        return Result.ok();
    }
}
