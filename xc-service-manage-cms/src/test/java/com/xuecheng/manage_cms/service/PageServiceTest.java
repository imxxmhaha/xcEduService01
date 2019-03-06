package com.xuecheng.manage_cms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author xxm
 * @create 2019-03-06 23:33
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {

    @Autowired
    private PageService pageService;
    @Test
    public void getPageHtml() {
        String pageId = "5c7fea56dbbbea073426d52e";
        String pageHtml = pageService.getPageHtml(pageId);
        System.out.println(pageHtml);
    }
}