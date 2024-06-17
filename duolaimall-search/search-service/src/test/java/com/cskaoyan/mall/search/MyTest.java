package com.cskaoyan.mall.search;

import com.cskaoyan.mall.search.repository.GoodsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class MyTest {


    @Autowired
    GoodsRepository goodsRepository;

    @Test
    public void test() {
        goodsRepository.deleteById(405L);
        goodsRepository.deleteById(396L);
    }
}
