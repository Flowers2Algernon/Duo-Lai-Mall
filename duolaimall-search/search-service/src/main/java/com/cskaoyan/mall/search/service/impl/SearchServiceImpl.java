package com.cskaoyan.mall.search.service.impl;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.gson.Gson;
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
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.json.Json;
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
        //此方法的本质是通过skiId获取对应的goods，并将该goods存储到ES中--通过maven依赖elastic-search来实现
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
        // 设置一级类目信息
        goods.setFirstLevelCategoryId(categoryView.getFirstLevelCategoryId());
        goods.setFirstLevelCategoryName(categoryView.getFirstLevelCategoryName());
        // 设置二级类目信息
        goods.setSecondLevelCategoryId(categoryView.getSecondLevelCategoryId());
        goods.setSecondLevelCategoryName(categoryView.getSecondLevelCategoryName());
        //设置三级类名信息
        goods.setThirdLevelCategoryId(categoryView.getThirdLevelCategoryId());
        goods.setThirdLevelCategoryName(categoryView.getThirdLevelCategoryName());

        // 5. 获取平台属性值信息
        List<PlatformAttributeInfoDTO> attrList = productFeignClient.getAttrList(skuId);
        List<SearchAttr> searchAttrList = attrList.stream().map(platformAttributeInfoDTO -> {
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
        goods.setAttrs(searchAttrList);


        // 保存文档对象，将其存储到ES中，方便搜索
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

        //解析查询结果 ，在其中处理有关goodsList的信息
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
        //此处的查询参数如下:
        //1.类目查询--提供几级类名的Id来进行查询
        //2.品牌查询--提供一个表示品牌的字符串 trademark.比如trademark=2:华为
        //3.顶部搜索框关键字查询-- 对应SearchParam中的keyword
        //4.商品分类页面中点击各种平台属性来进行分类查询--此处提供的是一个string数组--props

        // 构建boolQueryBuilder
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 构造关键字查询参数->3 solve
        buildKeyQuery(searchParam, boolQueryBuilder);
        // 构建品牌查询 trademark=2:华为 ->2 solve
        buildTrademarkQuery(searchParam, boolQueryBuilder);

        // 构造类目查询 -> 1 solve
        buildCategoryQuery(searchParam, boolQueryBuilder);

        // 构建平台属性查询 23:4G:运行内存(可能有多个查询，这个例子只是一个平台属性或者叫规格参数)-> 4 solve
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
        // 1.1 构造品牌聚合最外层的 tmId聚合
        TermsAggregationBuilder tmIdAgg = AggregationBuilders.terms("tmIdAgg").field("tmId");

        // 构造nested中的嵌套聚合的内层聚合
        //tmId的第一个子聚合
        TermsAggregationBuilder tmNameAgg = AggregationBuilders.terms("tmNameAgg").field("tmName");
        //将其设置为tmId的子聚合
        tmIdAgg.subAggregation(tmNameAgg);

        //tmId的第二个子聚合
        TermsAggregationBuilder tmLogoUrlAgg = AggregationBuilders.terms("tmLogoUrlAgg").field("tmLogoUrl");
        tmIdAgg.subAggregation(tmLogoUrlAgg);

        // 将品牌聚合添加到最终的聚合参数中
        nativeSearchQueryBuilder.withAggregations(tmIdAgg);


        //  构造平台属性聚合
        // 2.1 构造最外层的nested类型的聚合
        NestedAggregationBuilder attrAgg
                = AggregationBuilders.nested("attrAgg", "attrs");
        // 2.2 构造nested中的嵌套聚合中的外层聚合  attrIdAgg
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId");
        // 设置nested的子聚合
        attrAgg.subAggregation(attrIdAgg);

        // 2.3 attrId聚合的第一个子聚合 attrNameAgg
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attrNameAgg").field("attrs.attrName");
        // 设置attrIdAgg的第一个子聚合
        attrIdAgg.subAggregation(attrNameAgg);

        // 2.4 attrId聚合的第二个子聚合  attrValueAgg
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue");
        // 设置attrIdAgg的第二个子聚合
        attrIdAgg.subAggregation(attrValueAgg);
        //将平台属性聚合添加到最终查询结果集中
        nativeSearchQueryBuilder.withAggregations(attrAgg);

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
        //解析结果
        //1. 获取查询到的目标页文档的数据
        parseDocument(hits,searchResponseDTO);
        //2. 获取品牌列表
        AggregationsContainer<?> aggregationsContainer = hits.getAggregations();
        Aggregations aggregations = (Aggregations) aggregationsContainer.aggregations();
        //根据聚合名称获取聚合结果
        Terms tmIdAgg = aggregations.get("tmIdAgg");
        List<? extends Terms.Bucket> tmIdAggBuckets = tmIdAgg.getBuckets();
        //设置结果集
        ArrayList<SearchResponseTmDTO> searchResponseTmDTOS = new ArrayList<>();
        for (Terms.Bucket tmIdAggBucket : tmIdAggBuckets) {
            SearchResponseTmDTO searchResponseTmDTO = new SearchResponseTmDTO();
            long id = tmIdAggBucket.getKeyAsNumber().longValue();
            searchResponseTmDTO.setTmId(id);

            //拿第一个内层分桶tmName的值
            Terms tmName = tmIdAggBucket.getAggregations().get("tmName");
            if (tmName!=null){
                List<? extends Terms.Bucket> tmNameBuckets = tmName.getBuckets();
                ArrayList<String> strings = new ArrayList<>();
                for (Terms.Bucket tmNameBucket : tmNameBuckets) {
                    //获取内层tmName的值
                    String tmNameString = tmNameBucket.getKeyAsString();
                    strings.add(tmNameString);
                }
                searchResponseTmDTO.setTmName(strings.get(0));
            }

            //拿tmIdAgg第二个分桶tmLogoUrl的值
            Terms tmLogoUrl = tmIdAggBucket.getAggregations().get("tmLogoUrl");
            if (tmLogoUrl!=null){
                searchResponseTmDTO.setTmLogoUrl(tmLogoUrl.getBuckets().get(0).getKeyAsString());
            }

            //放入结果集
            searchResponseTmDTOS.add(searchResponseTmDTO);
        }
        searchResponseDTO.setTrademarkList(searchResponseTmDTOS);

        // 3. 获取平台属性及平台属性值列表

        // 3.1 获取所有的聚合结果
        AggregationsContainer<?> aggregationsContainer1 = hits.getAggregations();
        Aggregations aggregations1 = (Aggregations) aggregationsContainer1.aggregations();

        // 3.2 根据聚合名称获取聚合结果, nested聚合的结果
        Nested nested = aggregations1.get("attrAgg");

        // 3.3 从nested聚合中获取attrIdAgg
        Terms attrIdAgg = nested.getAggregations().get("attrIdAgg");

        // 3.4 获取外层聚合attrIdAgg的分桶
        List<? extends Terms.Bucket> attrIdAggBuckets = attrIdAgg.getBuckets();

        List<SearchResponseAttrDTO> searchResponseAttrDTOS = new ArrayList<>();

        // 遍历attrIdAgg的分桶
        for (Terms.Bucket attrIdBucket :attrIdAggBuckets) {
            SearchResponseAttrDTO searchResponseAttrDTO = new SearchResponseAttrDTO();

            // 获取attrId分桶的key(平台属性的id)
            long attrId = attrIdBucket.getKeyAsNumber().longValue();
            searchResponseAttrDTO.setAttrId(attrId);

            // 从attrId分桶中，获取第一个内层分桶聚合 attrNameAgg的结果
            Terms attrNameAgg = attrIdBucket.getAggregations().get("attrNameAgg");
            // 获取内层分桶的key值(平台属性的名称)
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            searchResponseAttrDTO.setAttrName(attrName);

            // 从attrId分桶中，获取第一个内层分桶聚合 attrValueAgg的结果
            Terms attrValueAgg = attrIdBucket.getAggregations().get("attrValueAgg");
            List<? extends Terms.Bucket> attrValueAggBuckets = attrValueAgg.getBuckets();

            List<String> values = new ArrayList<>();
            for (Terms.Bucket attrValueBucket :attrValueAggBuckets) {
                // 获取内层attrValueAgg分桶的key值(一个平台属性值)
                String attrValue = attrValueBucket.getKeyAsString();
                values.add(attrValue);
            }
            searchResponseAttrDTO.setAttrValueList(values);

            // 将表示一个平台属性信息的searchResponseAttrDTO 添加到结果集
            searchResponseAttrDTOS.add(searchResponseAttrDTO);
        }
        // 平台属性信息设置
        searchResponseDTO.setAttrsList(searchResponseAttrDTOS);

        return searchResponseDTO;
    }

    private void parseDocument(SearchHits<Goods> hits, SearchResponseDTO searchResponseDTO) {
        List<SearchHit<Goods>> searchHits = hits.getSearchHits();
        //高亮处理
        List<GoodsDTO> goodsDTOS = searchHits.stream().map(searchHit -> {
            //获取文档对象
            Goods content = searchHit.getContent();
            //获取高亮字符串
            List<String> title = searchHit.getHighlightField("title");
            //替换高亮字符串
            if (title != null && title.size() != 0) {
                content.setTitle(title.get(0));
            }
            return goodsConverter.goodsPO2DTO(content);
        }).collect(Collectors.toList());
        searchResponseDTO.setGoodsList(goodsDTOS);
    }
}
