package com.cskaoyan.mall.search.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

//@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.host}")
    String host;

    @Value("${elasticsearch.port}")
    String port;

    @Bean
    public RestHighLevelClient restClient() {
        int intPort = Integer.parseInt(port);

        RestClientBuilder builder = RestClient
                .builder(new HttpHost(host, intPort, "http"));

        return new RestHighLevelClient(builder);
    }

    @Bean
    public ElasticsearchRestTemplate elasticsearchTemplate(RestHighLevelClient restHighLevelClient) {

        return new ElasticsearchRestTemplate(restHighLevelClient);
    }
}
