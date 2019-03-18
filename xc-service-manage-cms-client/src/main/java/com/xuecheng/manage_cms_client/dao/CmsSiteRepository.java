package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author xxm
 * @create 2019-02-28 22:38
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {

}
