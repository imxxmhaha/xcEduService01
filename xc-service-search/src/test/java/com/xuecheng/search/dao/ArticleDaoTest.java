package com.xuecheng.search.dao;


import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.xuecheng.search.pojo.Article;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

/**
 * @author xxm
 * @create 2019-04-18 22:55
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ArticleDaoTest {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    RestHighLevelClient client;

    @Autowired
    RestClient restClient;

    /**
     * 查询全部带分页
     */
    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(1 - 1, 5);
        Page<Article> articles = articleDao.findAll(pageable);
        System.out.println(articles);
    }

    @Test
    public void testBoolFindAll() {
        Pageable pageable = PageRequest.of(1 - 1, 5);
        Page<Article> articles = articleDao.testBoolQuery("开发","1",pageable);
        System.out.println(articles);
    }


    /**
     * 原生API
     * 查询全部
     */
    @Test
    public void testSearchAll() throws IOException {
        // 搜索请求对象,并指定index
        SearchRequest searchRequest = new SearchRequest("xxm_index");
        // 设置type
        searchRequest.types("xxm");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 搜索全部
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // source源字段过滤
        searchSourceBuilder.fetchSource(new String[]{"id", "title", "content"}, new String[]{});
        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);

        // 处理结果集
        dealResponse(searchResponse);

    }

    /**
     * 原生API
     * 分页查询
     */
    @Test
    public void testSearchPage() throws IOException {
        // 搜索请求对象,并指定index
        SearchRequest searchRequest = new SearchRequest("xxm_index");
        // 设置type
        searchRequest.types("xxm");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 搜索全部
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // source源字段过滤
        searchSourceBuilder.fetchSource(new String[]{"id", "title", "content"}, new String[]{});

        // 分页查询,设置起始下标,从0开始
        searchSourceBuilder.from(0);
        // 每页显示个数
        searchSourceBuilder.size(2);


        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);

        // 处理结果集
        dealResponse(searchResponse);

    }

    /**
     * 原生API
     * Term Query  关键词不分词完全匹配
     * match Query 关键词先分词在匹配
     */
    @Test
    public void testSearchTermQuery() throws IOException {
        // 搜索请求对象,并指定index
        SearchRequest searchRequest = new SearchRequest("xc_course");
        // 设置type
        searchRequest.types("doc");
        // 搜索源构建者对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 搜索,有什么查询过滤条件,就往searchSourceBuilder.query这个里面塞
        searchSourceBuilder.query(QueryBuilders.termQuery("name", "spring"));
        //searchSourceBuilder.query(QueryBuilders.termQuery("id", "1118116038718918656"));
        // source源字段过滤 ,第一个参数结果集包括哪些字段，第二个参数表示结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"id", "title", "content","name","description"}, new String[]{});


        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);

        // 处理结果集
        dealResponse(searchResponse);

    }

    /**
     * 原生API
     * 根据id查询
     */
    @Test
    public void testSearchTermQueryByIds() throws IOException {
        // 搜索请求对象,并指定index
        SearchRequest searchRequest = new SearchRequest("xxm_index");
        // 设置type
        searchRequest.types("xxm");
        // 搜索源构建者对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 搜索,有什么查询过滤条件,就往searchSourceBuilder.query这个里面塞
        String[] ids = new String[]{"1118116038718918656"};
        //String[] ids = new String[]{"1118116038718918656","1118116312703438848"};
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id", ids));
        // source源字段过滤 ,第一个参数结果集包括哪些字段，第二个参数表示结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"id", "title", "content"}, new String[]{});

        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);

        // 处理结果集
        dealResponse(searchResponse);

    }

    /**
     * Match Query
     * 1、将"spring开发"分词，分为spring、开发两个词
     * 2、再使用spring和开发两个词去匹配索引中搜索
     * 3、由于设置了operator为or，只要有一个词匹配成功则就返回该文档。
     * 4、但是Match Query 不能同时搜索多个域
     */
    @Test
    public void testSearchMatchQuery() throws IOException {
        // 搜索请求对象,并指定index
        SearchRequest searchRequest = new SearchRequest("xxm_index");
        // 设置type
        searchRequest.types("xxm");
        // 搜索源构建者对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 搜索,有什么查询过滤条件,就往searchSourceBuilder.query这个里面塞
        searchSourceBuilder.query(QueryBuilders.matchQuery("title", "添加测试").minimumShouldMatch("70%"));
        // source源字段过滤 ,第一个参数结果集包括哪些字段，第二个参数表示结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"id", "title", "content"}, new String[]{});

        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);

        // 处理结果集
        dealResponse(searchResponse);

    }


    /**
     * Multi Match Query
     * 1、将"spring开发"分词，分为spring、开发两个词
     * 2、再使用spring和开发两个词去匹配索引中搜索
     * 3、由于设置了operator为or，只要有一个词匹配成功则就返回该文档。
     * 4、但是Multi Match Query 可以同时搜索多个域
     * 5、可以设置权重  提升boost
     */
    @Test
    public void testSearchMultiMatchQuery() throws IOException {
        // 搜索请求对象,并指定index
        SearchRequest searchRequest = new SearchRequest("xxm_index");
        // 设置type
        searchRequest.types("xxm");
        // 搜索源构建者对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
       // source源字段过滤 ,第一个参数结果集包括哪些字段，第二个参数表示结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"id", "title", "content"}, new String[]{});

        // 搜索,有什么查询过滤条件,就往searchSourceBuilder.query这个里面塞
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("开发", "title","content").minimumShouldMatch("50%").field("title",10));

        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);

        // 处理结果集
        dealResponse(searchResponse);

    }





    /**
     * boolQueryBuilder
     *   可以对多个queryBuilder进行交集,并集,非集处理
     */
    @Test
    public void testBoolQuery() throws IOException {
        // 搜索请求对象,并指定index
        SearchRequest searchRequest = new SearchRequest("xxm_index");
        // 设置type
        searchRequest.types("xxm");
        // 搜索源构建者对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // source源字段过滤 ,第一个参数结果集包括哪些字段，第二个参数表示结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"id", "title", "content"}, new String[]{});

        // 搜索,有什么查询过滤条件,就往searchSourceBuilder.query这个里面塞
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        MultiMatchQueryBuilder matchQueryBuilder = QueryBuilders.multiMatchQuery("开发", "title", "content").minimumShouldMatch("50%").field("title", 10);
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("id", "1");
        boolQueryBuilder.must(matchQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);

        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);

        // 处理结果集
        dealResponse(searchResponse);

    }




    public void dealResponse(SearchResponse searchResponse) {
        // 匹配搜索结果
        SearchHits hits = searchResponse.getHits();
        // 取出总记录数
        long totalHits = hits.totalHits;
        // 匹配度最高的前N个文档
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String index = searchHit.getIndex();
            String type = searchHit.getType();
            String id = searchHit.getId();
            float score = searchHit.getScore(); // 分数
            System.out.println("score = " + score);
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String id1 = (String) sourceAsMap.get("id");
            System.out.println("id1 = " + id1);
            String title = (String) sourceAsMap.get("title");
            System.out.println("title = " + title);
            String content = (String) sourceAsMap.get("content");
            System.out.println("content = " + content);
            String name = (String) sourceAsMap.get("name");
            System.out.println("name = " + name);
            String description = (String) sourceAsMap.get("description");
            System.out.println("description = " + description);
        }
    }

}