package com.cskaoyan.mall.search.service.impl;

import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.search.client.ProductApiClient;
import com.cskaoyan.mall.search.converter.GoodsConverter;
import com.cskaoyan.mall.search.dto.GoodsDTO;
import com.cskaoyan.mall.search.dto.SearchResponseAttrDTO;
import com.cskaoyan.mall.search.dto.SearchResponseTmDTO;
import com.cskaoyan.mall.search.dto.SearchResponseDTO;
import com.cskaoyan.mall.search.model.Goods;
import com.cskaoyan.mall.search.model.SearchAttr;
import com.cskaoyan.mall.search.param.SearchParam;
import com.cskaoyan.mall.search.repository.GoodsRepository;
import com.cskaoyan.mall.search.service.SearchService;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ProductApiClient productFeignClient;


    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    GoodsConverter goodsConverter;

    /**
     * 上架商品列表
     *
     * @param skuId
     */
    @Override
    public void upperGoods(Long skuId) {
        Goods goods = new Goods();
        //查询sku对应的平台属性
        List<PlatformAttributeInfoDTO> platformAttrInfoList = productFeignClient.getAttrList(skuId);
        if (null != platformAttrInfoList) {
            List<SearchAttr> searchAttrList = platformAttrInfoList.stream().map(baseAttrInfo -> {
                SearchAttr searchAttr = new SearchAttr();
                searchAttr.setAttrId(baseAttrInfo.getId());
                searchAttr.setAttrName(baseAttrInfo.getAttrName());
                //一个sku的一个销售属性，只对应一个属性值
                List<PlatformAttributeValueDTO> attrValueList = baseAttrInfo.getAttrValueList();
                searchAttr.setAttrValue(attrValueList.get(0).getValueName());
                return searchAttr;
            }).collect(Collectors.toList());

            goods.setAttrs(searchAttrList);
        }

        //查询sku信息
        SkuInfoDTO skuInfoDTO = productFeignClient.getSkuInfo(skuId);
        // 查询品牌
        TrademarkDTO baseTrademark = productFeignClient.getTrademark(skuInfoDTO.getTmId());
        if (baseTrademark != null) {
            goods.setTmId(skuInfoDTO.getTmId());
            goods.setTmName(baseTrademark.getTmName());
            goods.setTmLogoUrl(baseTrademark.getLogoUrl());

        }

        // 查询分类
        CategoryHierarchyDTO categoryView = productFeignClient.getCategoryView(skuInfoDTO.getThirdLevelCategoryId());
        if (categoryView != null) {
            goods.setFirstLevelCategoryId(categoryView.getFirstLevelCategoryId());
            goods.setFirstLevelCategoryName(categoryView.getFirstLevelCategoryName());
            goods.setSecondLevelCategoryId(categoryView.getSecondLevelCategoryId());
            goods.setSecondLevelCategoryId(categoryView.getSecondLevelCategoryId());
            goods.setThirdLevelCategoryId(categoryView.getThirdLevelCategoryId());
            goods.setThirdLevelCategoryName(categoryView.getThirdLevelCategoryName());
        }
        goods.setDefaultImg(skuInfoDTO.getSkuDefaultImg());
        goods.setPrice(skuInfoDTO.getPrice().doubleValue());
        goods.setId(skuInfoDTO.getId());
        goods.setTitle(skuInfoDTO.getSkuName());
        this.goodsRepository.save(goods);
    }

    /**
     * 下架商品列表
     *
     * @param skuId
     */
    @Override
    public void lowerGoods(Long skuId) {
        // 删除文档
        this.goodsRepository.deleteById(skuId);

        // 删除热度
        redissonClient.getScoredSortedSet("hotScore").remove("skuId:" + skuId);

    }

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void incrHotScore(Long skuId) {
        // 定义key
        String hotKey = "hotScore";
        // 保存数据
        Double hotScore = redissonClient.getScoredSortedSet(hotKey).addScore("skuId:" + skuId, 1);
        if (hotScore.longValue() % 10 == 0) {
            // 更新es
            Optional<Goods> optional = goodsRepository.findById(skuId);
            Goods goods = optional.get();
            goods.setHotScore(hotScore.longValue());
            goodsRepository.save(goods);
        }
    }

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Override
    public SearchResponseDTO search(SearchParam searchParam) throws IOException {
        // 构建dsl语句
        NativeSearchQueryBuilder nativeSearchQueryBuilder = buildQueryDsl(searchParam);
        NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();
        SearchHits<Goods> searchResults = restTemplate.search(searchQuery, Goods.class);

        //解析查询结果
        SearchResponseDTO responseDTO = parseSearchResult(searchResults);


        //设置满足条件的总记录数
        responseDTO.setTotal(searchResults.getTotalHits());
        // 响应中设置一页的文档数量
        responseDTO.setPageSize(searchParam.getPageSize());
        // 响应中设置当前页数
        responseDTO.setPageNo(searchParam.getPageNo());
        // 计算总页数
        long totalPages = (responseDTO.getTotal() + searchParam.getPageSize() - 1) / searchParam.getPageSize();
        if (totalPages == 0) {
            // 前端的页数是从1开始的
            totalPages = 1;
        }
        responseDTO.setTotalPages(totalPages);
        return responseDTO;
    }

    // 制作dsl 语句
    private NativeSearchQueryBuilder buildQueryDsl(SearchParam searchParam) {
        // 构建查询器
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();


        // 构建boolQueryBuilder
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 构造关键字查询参数
        buildKeyQuery(searchParam, boolQueryBuilder);
        // 构建品牌查询 trademark=2:华为
        buildTrademarkQuery(searchParam, boolQueryBuilder);

        // 构造类目查询
        buildCategoryQuery(searchParam, boolQueryBuilder);

        // 构建平台属性查询 23:4G:运行内存(可能有多个查询，这个例子只是一个平台属性或者叫规格参数)
        BuildSpecificQuery(searchParam, boolQueryBuilder);
        // 设置整个复合查询
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        // 构建分页
        PageRequest pageRequest = PageRequest.of(searchParam.getPageNo() - 1, searchParam.getPageSize());
        nativeSearchQueryBuilder.withPageable(pageRequest);

        // 构造排序参数 order=1:desc  1为按热度排序，2为按照价格排序
        buildSort(searchParam, nativeSearchQueryBuilder);

        // 构建高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title").postTags("</span>").preTags("<span style=color:red>");
        nativeSearchQueryBuilder.withHighlightBuilder(highlightBuilder);

        //  构造品牌聚合
        TermsAggregationBuilder tmIdAgg = AggregationBuilders.terms("tmIdAgg").field("tmId");
        tmIdAgg.subAggregation(AggregationBuilders.terms("tmNameAgg").field("tmName"));
        tmIdAgg.subAggregation(AggregationBuilders.terms("tmLogoUrlAgg").field("tmLogoUrl"));
        // 设置品牌聚合
        nativeSearchQueryBuilder.addAggregation(tmIdAgg);

        //  构造平台属性聚合
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attrAgg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId");
        attrAgg.subAggregation(attrIdAgg);

        attrIdAgg.subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName"));
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue"));
        //  设置平台属性聚合
        nativeSearchQueryBuilder.addAggregation(attrAgg);


        // 设置结果集过滤
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(new String[]{"id", "defaultImg", "title", "price"}, null);
        nativeSearchQueryBuilder.withSourceFilter(fetchSourceFilter);

        return nativeSearchQueryBuilder;
    }

    private void buildSort(SearchParam searchParam, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        // 排序参数：order=1:desc
        String order = searchParam.getOrder();
        if (!StringUtils.isEmpty(order)) {
            // 判断排序规则
            String[] split = StringUtils.split(order, ":");
            if (split != null && split.length == 2) {
                // 排序的字段
                String field = null;
                // 数组中的第一个参数
                switch (split[0]) {
                    case "1":
                        field = "hotScore";
                        break;
                    case "2":
                        field = "price";
                        break;
                }
                Sort.Direction direction = "asc".equals(split[1])? Sort.Direction.ASC : Sort.Direction.DESC;
                nativeSearchQueryBuilder.withSort(Sort.by(direction, field));
            } else {
                // 没有传值的时候给默认值, 按照
                nativeSearchQueryBuilder.withSort(Sort.by(Sort.Direction.DESC, "hotScore"));
            }
        }
    }

    private void BuildSpecificQuery(SearchParam searchParam, BoolQueryBuilder boolQueryBuilder) {
        // 23:4G:运行内存
        String[] props = searchParam.getProps();
        if (props != null && props.length > 0) {
            // 循环遍历
            for (String prop : props) {
                // 23:4G:运行内存
                String[] split = StringUtils.split(prop, ":");
                if (split != null && split.length == 3) {
                    // 嵌套查询子查询
                    BoolQueryBuilder subBoolQuery = QueryBuilders.boolQuery();
                    // 构建子查==询中的过滤条件
                    subBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", split[0]));
                    subBoolQuery.must(QueryBuilders.termQuery("attrs.attrValue", split[1]));
                    // ScoreMode.None ？
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", subBoolQuery, ScoreMode.None);
                    // 添加到整个过滤对象中
                    boolQueryBuilder.filter(nestedQueryBuilder);
                }
            }
        }
    }

    private void buildCategoryQuery(SearchParam searchParam, BoolQueryBuilder boolQueryBuilder) {
        // 构建分类过滤 用户在点击的时候，只能点击一个值，所以此处使用term
        if (null != searchParam.getFirstLevelCategoryId()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("firstLevelCategoryId", searchParam.getFirstLevelCategoryId()));
        }
        // 构建分类过滤
        if (null != searchParam.getSecondLevelCategoryId()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("secondLevelCategoryId", searchParam.getSecondLevelCategoryId()));
        }
        // 构建分类过滤
        if (null != searchParam.getThirdLevelCategoryId()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("thirdLevelCategoryId", searchParam.getThirdLevelCategoryId()));
        }
    }

    private void buildTrademarkQuery(SearchParam searchParam, BoolQueryBuilder boolQueryBuilder) {
        String trademark = searchParam.getTrademark();
        if (!StringUtils.isEmpty(trademark)) {
            // trademark=2:华为
            String[] split = StringUtils.split(trademark, ":");
            if (split != null && split.length == 2) {
                // 根据品牌Id过滤
                boolQueryBuilder.filter(QueryBuilders.termQuery("tmId", split[0]));
            }
        }
    }

    private void buildKeyQuery(SearchParam searchParam, BoolQueryBuilder boolQueryBuilder) {
        // 判断查询条件是否为空 关键字
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            // 小米手机  小米and手机
            // MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("title",searchParam.getKeyword()).operator(Operator.AND);
            MatchQueryBuilder title = QueryBuilders.matchQuery("title", searchParam.getKeyword()).operator(Operator.AND);
            boolQueryBuilder.must(title);
        }
    }

    // 制作返回结果集
    private SearchResponseDTO parseSearchResult(SearchHits<Goods> hits) {

        //声明对象
        SearchResponseDTO searchResponseDTO = new SearchResponseDTO();

        //
        Aggregations aggregations = (Aggregations) hits.getAggregations().aggregations();
        Terms tmIdAgg = aggregations.get("tmIdAgg");
        List<SearchResponseTmDTO> trademarkList = tmIdAgg.getBuckets().stream().map(bucket -> {
            // 封装品牌信息
            SearchResponseTmDTO trademark = new SearchResponseTmDTO();
            //获取品牌Id
            trademark.setTmId((Long.parseLong(bucket.getKeyAsString())));

            //获取品牌名称, 子bucket
            Terms tmNameAgg = bucket.getAggregations().get("tmNameAgg");
            String tmName = tmNameAgg.getBuckets().get(0).getKeyAsString();
            trademark.setTmName(tmName);

            // 获取品牌logo url
            Terms tmLogoUrlAgg = bucket.getAggregations().get("tmLogoUrlAgg");
            String tmLogoUrl = tmLogoUrlAgg.getBuckets().get(0).getKeyAsString();
            trademark.setTmLogoUrl(tmLogoUrl);

            return trademark;
        }).collect(Collectors.toList());
        searchResponseDTO.setTrademarkList(trademarkList);


        List<Goods> goodsList = new ArrayList<>();
        //赋值商品列表
        List<SearchHit<Goods>> searchHits = hits.getSearchHits();
        if (searchHits != null && searchHits.size() > 0) {
            //循环遍历
            for (SearchHit<Goods> subHit : searchHits) {
                Goods goods = subHit.getContent();

                //获取高亮
                if (subHit.getHighlightFields().get("title") != null) {
                    String title = subHit.getHighlightField("title").get(0);
                    goods.setTitle(title);
                }
                goodsList.add(goods);
            }
        }
        List<GoodsDTO> goodsDTOs = goodsConverter.goodsPOs2DTOs(goodsList);
        // 设置查询到的目标页中的SKU商品数据
        searchResponseDTO.setGoodsList(goodsDTOs);

        //获取平台属性数据
        Nested attrAgg = aggregations.get("attrAgg");
        Terms attrIdAgg = attrAgg.getAggregations().get("attrIdAgg");
        List<? extends Terms.Bucket> attrIdAggBuckets = attrIdAgg.getBuckets();
        if (!CollectionUtils.isEmpty(attrIdAggBuckets)) {
            List<SearchResponseAttrDTO> searchResponseAttrDTOS = attrIdAggBuckets.stream().map(subBucket -> {
                //声明平台属性对象
                SearchResponseAttrDTO responseAttrVO = new SearchResponseAttrDTO();
                //设置平台属性值Id
                responseAttrVO.setAttrId(subBucket.getKeyAsNumber().longValue());

                // 设置平台属性名称
                Terms attrNameAgg = subBucket.getAggregations().get("attrNameAgg");
                List<? extends Terms.Bucket> nameBuckets = attrNameAgg.getBuckets();
                responseAttrVO.setAttrName(nameBuckets.get(0).getKeyAsString());
                //设置规格参数列表
                ParsedStringTerms attrValueAgg = subBucket.getAggregations().get("attrValueAgg");
                List<? extends Terms.Bucket> valueBuckets = attrValueAgg.getBuckets();
                // 平台属性值
                List<String> values = valueBuckets.stream().map(Terms.Bucket::getKeyAsString).collect(Collectors.toList());
                responseAttrVO.setAttrValueList(values);

                return responseAttrVO;

            }).collect(Collectors.toList());
            searchResponseDTO.setAttrsList(searchResponseAttrDTOS);
        }

        return searchResponseDTO;
    }
}
