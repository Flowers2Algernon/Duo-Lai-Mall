package com.cskaoyan.mall.search.service.impl;

import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.search.client.ProductApiClient;
import com.cskaoyan.mall.search.converter.GoodsConverter;
import com.cskaoyan.mall.search.dto.GoodsDTO;
import com.cskaoyan.mall.search.dto.SearchResponseAttrDTO;
import com.cskaoyan.mall.search.dto.SearchResponseDTO;
import com.cskaoyan.mall.search.dto.SearchResponseTmDTO;
import com.cskaoyan.mall.search.model.Goods;
import com.cskaoyan.mall.search.model.SearchAttr;
import com.cskaoyan.mall.search.param.SearchParam;
import com.cskaoyan.mall.search.repository.GoodsRepository;
import com.cskaoyan.mall.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

        // 调用商品服务，获取所需的具体上架商品数据

        // 1. 获取sku商品基本信息
        SkuInfoDTO skuInfo = productFeignClient.getSkuInfo(skuId);
        // 设置 sku基本信息
        goods.setId(skuInfo.getId());
        goods.setTitle(skuInfo.getSkuName());
        goods.setDefaultImg(skuInfo.getSkuDefaultImg());

        // 2. 获取商品的最新价格
        BigDecimal price = productFeignClient.getPrice(skuId);
        goods.setPrice(price.doubleValue());

        // 3. 获取品牌信息
        TrademarkDTO trademark = productFeignClient.getTrademark(skuInfo.getTmId());
        // 设置品牌信息
        goods.setTmId(trademark.getId());
        goods.setTmName(trademark.getTmName());
        goods.setTmLogoUrl(trademark.getLogoUrl());

        // 4. 类目信息
        CategoryHierarchyDTO categoryView
                = productFeignClient.getCategoryView(skuInfo.getThirdLevelCategoryId());
        // 设置类目信息
        goods.setFirstLevelCategoryId(categoryView.getFirstLevelCategoryId());
        goods.setFirstLevelCategoryName(categoryView.getFirstLevelCategoryName());
        // ....

        // 5. 获取平台属性值信息
        List<PlatformAttributeInfoDTO> attrList = productFeignClient.getAttrList(skuId);
        attrList.stream().map(platformAttributeInfoDTO -> {
            SearchAttr searchAttr = new SearchAttr();

            //1. 平台属性id
            searchAttr.setAttrId(platformAttributeInfoDTO.getId());
            //2. 平台属性名
            searchAttr.setAttrName(platformAttributeInfoDTO.getAttrName());
            //3. 平台属性值
            List<PlatformAttributeValueDTO> attrValueList = platformAttributeInfoDTO.getAttrValueList();
            searchAttr.setAttrValue(attrValueList.get(0).getValueName());
            return searchAttr;
        }).collect(Collectors.toList());


        // 保存文档对象
        goodsRepository.save(goods);
    }

    /**
     * 下架商品列表
     *
     * @param skuId
     */
    @Override
    public void lowerGoods(Long skuId) {

        // 1.  删除es中的文档数据
        goodsRepository.deleteById(skuId);

        // 2.  将商品的热度删除掉
        String hotScore = "hotScoreKey";
        redissonClient.getScoredSortedSet(hotScore).remove("skuId:" + skuId);

    }

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void incrHotScore(Long skuId) {

        String hotScore = "hotScoreKey";

        // 1. 访问redis
        Double newScore = redissonClient.getScoredSortedSet(hotScore).addScore("skuId:" + skuId, 1);

        // 2. 向文档中保存最新的热度值
        Optional<Goods> goodsOptional = goodsRepository.findById(skuId);
        if (newScore.longValue() % 10 == 0) {
            // 减少对es的访问
            if (goodsOptional.isPresent()) {
                // 获取文档数据
                Goods goods = goodsOptional.get();
                // 设置最新热度值
                goods.setHotScore(newScore.longValue());
                // 保存更新热度之后的文档
                goodsRepository.save(goods);
            }

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

        //解析查询结果 (自己完成)
        SearchResponseDTO responseDTO = parseSearchResult(searchResults);

        //设置满足条件的总记录数
        responseDTO.setTotal(searchResults.getTotalHits());
        // 响应中设置一页的文档数量
        responseDTO.setPageSize(searchParam.getPageSize());
        // 响应中设置当前页数
        responseDTO.setPageNo(searchParam.getPageNo());
        // 计算总页数
        //  一页 3
        //  共4条
        //    responseDTO.getTotal() / searchParam.getPageSize()
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
        buildSpecificQuery(searchParam, boolQueryBuilder);
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


        //  构造平台属性聚合


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
            String[] split = order.split(":");
            String orderField = "";
            switch (split[0]) {
                case "1":
                    orderField = "hotScore";
                    break;
                case "2":
                    orderField = "price";
                    break;

            }

            if (!orderField.isEmpty()) {
                // 按照指定的字段
                Sort sort = Sort.by("asc".equals(split[1]) ? "asc" : "desc", orderField);
                nativeSearchQueryBuilder.withSort(sort);
                return;
            }
        }

        // 没有排序参数, 默认按照热度降序排序
        Sort sort = Sort.by(Sort.Direction.DESC, "hotScore");
        nativeSearchQueryBuilder.withSort(sort);
    }

    private void buildSpecificQuery(SearchParam searchParam, BoolQueryBuilder boolQueryBuilder) {
        // 23:4G:运行内存
        String[] props = searchParam.getProps();
        if (props != null && props.length > 0) {
            // 循环遍历
            for (String prop : props) {
                // 23:4G:运行内存
                String[] split = StringUtils.split(prop, ":");
                if (split != null && split.length == 3) {

                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();

                    // 查询平台属性id
                    TermQueryBuilder attrIdQueryBuilder = QueryBuilders.termQuery("attrs.attrId", split[0]);
                    nestedBoolQueryBuilder.filter(attrIdQueryBuilder);

                    // 查询平台属性值
                    TermQueryBuilder attrValueQueryBuilder = QueryBuilders.termQuery("attrs.attrValue", split[1]);
                    nestedBoolQueryBuilder.filter(attrValueQueryBuilder);

                    // 构造nested查询
                    NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQueryBuilder, ScoreMode.None);

                    // 添加到最终的查询
                    boolQueryBuilder.filter(nestedQuery);

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
            String[] trademarkParam = trademark.split(":");
            if (trademarkParam != null && trademarkParam.length == 2) {
                // 构造品牌查询
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("tmId", trademarkParam[0]);

                // 添加到最终的查询中
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }
    }

    private void buildKeyQuery(SearchParam searchParam, BoolQueryBuilder boolQueryBuilder) {
        // 判断查询条件是否为空 关键字
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            // 构造关键字查询
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", searchParam.getKeyword());
            // 添加到最终的查询
            boolQueryBuilder.must(matchQueryBuilder);

        }
    }

    // 制作返回结果集
    private SearchResponseDTO parseSearchResult(SearchHits<Goods> hits) {
        // 1. 文档对象集合(title赋值高亮字符串)
        // 2. 品牌数据（聚合结果）
        // 3. 平台属性(聚合结果)

        //声明对象
        SearchResponseDTO searchResponseDTO = new SearchResponseDTO();


        return searchResponseDTO;
    }
}
