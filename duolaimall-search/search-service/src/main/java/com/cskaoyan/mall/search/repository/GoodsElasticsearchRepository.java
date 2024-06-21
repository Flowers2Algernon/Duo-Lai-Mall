package com.cskaoyan.mall.search.repository;

import com.cskaoyan.mall.search.model.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsElasticsearchRepository extends ElasticsearchRepository<Goods,Long> {
}
