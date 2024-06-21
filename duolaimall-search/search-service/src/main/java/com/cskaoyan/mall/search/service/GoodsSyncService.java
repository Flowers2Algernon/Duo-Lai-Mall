package com.cskaoyan.mall.search.service;

import com.cskaoyan.mall.search.model.Goods;
import com.cskaoyan.mall.search.repository.GoodsElasticsearchRepository;
import com.cskaoyan.mall.search.repository.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class GoodsSyncService {
    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    GoodsElasticsearchRepository goodsElasticsearchRepository;

    @Scheduled(fixedRate = 300000)//每5分钟执行一次
    public void syncToElasticSearch(){
        log.info("Starting synchronization to Elasticsearch");
        Iterable<Goods> goodsRepositoryAll = goodsRepository.findAll();
        //打印输出每个商品的信息
        goodsRepositoryAll.forEach(goods ->
                log.info("Found goods: id={}, name={}", goods.getId(), goods.getTmName())
        );
        long count = StreamSupport.stream(goodsRepositoryAll.spliterator(), false).count();
        log.info("Found {} goods in database", count);
        //观察ElasticSearch中有多少个数据
        goodsElasticsearchRepository.saveAll(goodsRepositoryAll);
        log.info("Finished synchronization to Elasticsearch");
        // 验证 Elasticsearch 中的数据
        Iterable<Goods> savedGoods = goodsElasticsearchRepository.findAll();
        long savedCount = StreamSupport.stream(savedGoods.spliterator(), false).count();
        log.info("Found {} goods in Elasticsearch after sync", savedCount);
    }
}
