package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.CustomException;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * @author xxm
 * @create 2019-02-28 23:13
 */
@Service
public class PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    /**
     * 页面查询方法
     *
     * @param page             页码,从1开始计数,dao查询从0开始
     * @param size             每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {

        //自定义条件查询
        //条件值对象
        CmsPage cmsPage = new CmsPage();
        if (null != queryPageRequest) {
            BeanUtils.copyProperties(queryPageRequest, cmsPage);
        }
        // 条件匹配器  可以用ExampleMatcher.GenericPropertyMatchers.contains() 设置模糊搜索
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = exampleMatcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        // 定义Example
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);


        // 分页参数
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CmsPage> pages = cmsPageRepository.findAll(example, pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(pages.getContent());  // 数据列表
        queryResult.setTotal(pages.getTotalElements()); //数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }


    //新增页面
    public CmsPageResult add(CmsPage cmsPage) {
        // 校验页面名称,站点ID,页面webpath的唯一性
        // 根据页面名称,站点ID,页面webpath区cms_page集合查询,有记录说明此页面已经存在,查询不到则可以添加

        CmsPage dbCmspage = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        //调用Dao新增页面
        if (null != dbCmspage) {
            //页面已经存在
            //跑出异常
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        // 调用dao新增页面
        cmsPage.setPageId(null);
        cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        //添加失败
    }

    // 根据页面id查询页面
    public CmsPage getById(String id) {
        Optional<CmsPage> optionalPage = cmsPageRepository.findById(id);
        if (optionalPage.isPresent()) {
            CmsPage cmsPage = optionalPage.get();
            return cmsPage;
        }
        return null;
    }

    // 修改页面
    public CmsPageResult update(String id, CmsPage cmsPage) {
        //根据id从数据库中查询页面信息
        CmsPage dbCmsPage = this.getById(id);
        if (null != dbCmsPage) {
            //准备跟新数据
            //设置要修改的数据
            dbCmsPage.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            dbCmsPage.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            dbCmsPage.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            dbCmsPage.setPageName(cmsPage.getPageName());
            //更新访问路径
            dbCmsPage.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            dbCmsPage.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            CmsPage save = cmsPageRepository.save(dbCmsPage);
            if (null != save) {
                //返回成功
                CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, save);
                return cmsPageResult;
            }

        }
        //修改失败
        return  new CmsPageResult(CommonCode.FAIL,null);

    }

    // 根据页面id删除页面
    public ResponseResult deleteById(String id) {
        Optional<CmsPage> optionalPage = cmsPageRepository.findById(id);
        if(optionalPage.isPresent()){
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
}
