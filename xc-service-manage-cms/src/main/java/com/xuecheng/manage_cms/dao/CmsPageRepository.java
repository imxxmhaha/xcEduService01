package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author xxm
 * @create 2019-02-28 22:38
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

}
