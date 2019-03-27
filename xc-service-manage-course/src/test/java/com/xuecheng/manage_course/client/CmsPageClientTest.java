package com.xuecheng.manage_course.client;

import com.xuecheng.api.client.CmsPageClient;
import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author xxm
 * @create 2019-03-27 23:26
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageClientTest {

    @Autowired
    private CmsPageClient cmsPageClient;

    @Test
    public void findCmsPageById() {
        CmsPage cmsPage = cmsPageClient.findCmsPageById("5a795ac7dd573c04508f3a56");
        System.out.println(cmsPage);
    }
}