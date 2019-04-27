package com.xuecheng.search.service;

import com.baomidou.mybatisplus.toolkit.MapUtils;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.utils.MapUtil;
import com.xuecheng.framework.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.applet.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xxm
 * @create 2019-04-24 20:53
 */
@Service
public class EsCourseService {
    @Value("${xuecheng.course.index}")
    private String index;

    @Value("${xuecheng.course.type}")
    private String type;

    @Value("${xuecheng.course.source_field}")
    private String source_field;

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    /**
     * 课程搜索
     * @param page
     * @param size
     * @param courseSearchParam
     * @return
     */
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam) {
        courseSearchParam = null==courseSearchParam?new CourseSearchParam():courseSearchParam;
        // 创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        // 设置搜索类型
        searchRequest.types(type);

        // 设置搜索条件构建器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 过滤源字段
        String[] source_field_array = source_field.split(",");
        searchSourceBuilder.fetchSource(source_field_array, new String[]{});

        // 创建布尔查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 各种过滤条件
        // 根据关键字搜索
        if(!StringUtils.isEmpty(courseSearchParam.getKeyword())){
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(), "name", "description", "teachplan")
                    .minimumShouldMatch("70%").field("name",10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }

        // 根据分类查询
        if(StringUtil.isNotEmpty(courseSearchParam.getMt())){
            // 一级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getMt()));
        }

        if (StringUtil.isNotEmpty(courseSearchParam.getSt())){
            // 一级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("st",courseSearchParam.getSt()));
        }

        // 难度等级
        if (StringUtil.isNotEmpty(courseSearchParam.getGrade())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade",courseSearchParam.getGrade()));
        }

        // 设置boolQueryBuilder到searchSourceBuilder中
        searchSourceBuilder.query(boolQueryBuilder);

        // 设置分页参数
        if(page<=0){
            page = 1;
        }
        if(size<=0){
            size = 12;
        }
        int start = (page -1)*size;
        searchSourceBuilder.from(start);//起始记录的下标
        searchSourceBuilder.size(size);

        // 设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        // 设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);


        searchRequest.source(searchSourceBuilder);

        QueryResult<CoursePub> queryResult = new QueryResult<>();
        List<CoursePub> coursePubList = new ArrayList<>();
        try {
            // 执行搜索
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            // 获取响应结果
            SearchHits hits = searchResponse.getHits();
            // 匹配总记录数
            long totalHits = hits.totalHits;
            queryResult.setTotal(totalHits);
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                CoursePub coursePub = new CoursePub();
                // 源文档
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                // 取出id
                String id = MapUtil.getStrValue(sourceAsMap,"id");
                coursePub.setId(id);
                // 取出name
                String name = MapUtil.getStrValue(sourceAsMap,"name");

                // 取出高亮字段name
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if(null != highlightFields){
                    HighlightField highlightName = highlightFields.get("name");
                    if( null !=highlightName){
                        Text[] fragments = highlightName.fragments();
                        // 定义StringBuilder
                        StringBuilder sb = new StringBuilder();
                        for (Text text : fragments) {
                            sb.append(text);
                        }
                        name = sb.toString();
                    }
                }
                coursePub.setName(name);

                // 图片
                String pic = MapUtil.getStrValue(sourceAsMap,"pic");
                coursePub.setPic(pic);

                // 价格
                String p = MapUtil.getStrValue(sourceAsMap, "price");
                p = StringUtil.isEmpty(p)?"0":p;
                Float price = Float.parseFloat(p);
                coursePub.setPrice(price);

                // 旧价格
                p = MapUtil.getStrValue(sourceAsMap,"price_old");
                p = StringUtil.isEmpty(p)?"0":p;
                Float old_price = Float.parseFloat(p);
                coursePub.setPrice(price);

                coursePubList.add(coursePub);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        queryResult.setList(coursePubList);
        return QueryResponseResult.SUCCESS(queryResult);
    }
}
