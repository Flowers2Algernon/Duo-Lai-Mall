package com.cskaoyan.mall.search;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ImportTest {



    @Test
    public void importTestFormDatabases(){



    }
}
