package com.xuecheng.manage_cms.service;

/**
 * @author xxm
 * @create 2019-03-24 13:32
 */

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.dao.SysDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SysdictionaryService {
    @Autowired
    SysDictionaryRepository sysDictionaryRepository;

    //根据字典分类type查询字典信息
    public SysDictionary findDictionaryByType(String type) {
        return sysDictionaryRepository.findBydType(type);
    }
}
