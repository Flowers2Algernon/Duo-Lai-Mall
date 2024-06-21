//package com.cskaoyan.mall.order;
//
//import com.alibaba.fastjson.JSON;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.cskaoyan.mall.order.dto.OrderDetailDTO;
//import com.cskaoyan.mall.order.dto.OrderInfoDTO;
//import com.cskaoyan.mall.order.mapper.OrderInfoMapper;
//import com.cskaoyan.mall.order.service.OrderService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import springfox.documentation.spring.web.json.Json;
//
///**
// * 创建日期: 2023/03/16 23:30
// *
// * @author ciggar
// */
//@SpringBootTest(classes = ServiceOrderApplication.class)
//@RunWith(SpringRunner.class)
//public class OrderTest {
//
//    @Autowired
//    OrderService orderService;
//
//    @Autowired
//    OrderInfoMapper orderInfoMapper;
//
//    @Test
//    public void testSelectOrderPage(){
//        Page<OrderInfoDTO> pageParam = new Page<>(1, 10);
//
//        IPage<OrderInfoDTO> page = orderService.getPage(pageParam, "1");
//
//        System.out.println(JSON.toJSONString(page));
//
//    }
//}
