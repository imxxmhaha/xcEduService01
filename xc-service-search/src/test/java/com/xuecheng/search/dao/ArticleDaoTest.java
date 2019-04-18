package com.xuecheng.search.dao;


import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.xuecheng.search.pojo.Article;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

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


    /**
     * 查询全部带分页
     */
    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(1 - 1, 5);
        Page<Article> articles = articleDao.findAll(pageable);
        System.out.println(articles);
    }
}