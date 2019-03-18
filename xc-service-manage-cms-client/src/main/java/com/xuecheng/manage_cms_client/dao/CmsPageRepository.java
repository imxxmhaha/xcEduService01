package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author xxm
 * @create 2019-02-28 22:38
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

    //根据页面名称查询
    CmsPage findByPageName(String pageName);

    //根据页面名称,站点ID,页面webpath查询
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName, String siteId, String pageWebPath);
}
