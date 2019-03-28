package com.xuecheng.api.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author xxm
 * @create 2019-03-27 23:21
 */
@FeignClient(value = "XC-SERVICE-MANAGE-CMS") //指定需要远程调用的服务名
public interface CmsPageClient {
    //根据页面id查询页面信息,远程调用cms请求数据
    @GetMapping("/cms/page/get/{id}")
    public CmsPage findCmsPageById(@PathVariable("id") String id);

    // 添加页面,用于课程预览
    @PostMapping("/cms/page/save")
    public CmsPageResult saveCmsPage(@RequestBody CmsPage cmsPage);

}
