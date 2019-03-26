package com.xuecheng.manage_cms.controller;

/**
 * @author xxm
 * @create 2019-03-24 13:33
 */

import com.xuecheng.api.cms.SysDictionaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.service.SysdictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/dictionary")
public class SysDictionaryController implements SysDictionaryControllerApi {

    @Autowired
    private SysdictionaryService sysdictionaryService;

    //根据字典分类id查询字典信息
    @Override
    @GetMapping("/get/{type}")
    public SysDictionary getByType(@PathVariable("type") String type) {
        return sysdictionaryService.findDictionaryByType(type);
    }
}
