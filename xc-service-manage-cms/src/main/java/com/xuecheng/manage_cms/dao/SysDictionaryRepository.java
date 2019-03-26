package com.xuecheng.manage_cms.dao;

/**
 * @author xxm
 * @create 2019-03-24 13:31
 */

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysDictionaryRepository extends MongoRepository<SysDictionary, String> {
    //根据字典分类查询字典信息
    SysDictionary findBydType(String dType);
}
