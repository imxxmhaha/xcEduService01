package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.exception.CustomException;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author xxm
 * @create 2019-02-28 23:13
 */
@Service
public class PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

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
            dbCmsPage.setDataUrl(cmsPage.getDataUrl());
            CmsPage save = cmsPageRepository.save(dbCmsPage);
            if (null != save) {
                //返回成功
                CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, save);
                return cmsPageResult;
            }

        }
        //修改失败
        return new CmsPageResult(CommonCode.FAIL, null);

    }

    // 根据页面id删除页面
    public ResponseResult deleteById(String id) {
        //先查询一下
        Optional<CmsPage> optionalPage = cmsPageRepository.findById(id);
        if (optionalPage.isPresent()) {
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    //根据id查询cmsConfig
    public CmsConfig getConfigById(String id) {
        CmsConfig cmsConfig = null;
        Optional<CmsConfig> cmsConfigOptional = cmsConfigRepository.findById(id);
        if (cmsConfigOptional.isPresent()) {
            cmsConfig = cmsConfigOptional.get();
        }
        return cmsConfig;
    }

    /**
     * 页面静态化方法
     * 1.获取页面的dataUrl
     * 2.远程请求dataUrl获取数据模型
     * 3.获取页面的模板信息
     * 4.执行页面静态化
     *
     * @param pageId
     * @return
     */
    public String getPageHtml(String pageId) {

        // 远程请求dataUrl获取数据模型
        Map model = getModelByPageId(pageId);
        if (null == model) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        // 获取页面模板信息
        String template = getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(template)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        // 执行静态化
        String html = generateHtml(template, model);
        return html;
    }

    private String generateHtml(String template, Map model) {
        try {
            //生成配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            //模板加载器
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template", template);
            //配置模板加载器
            configuration.setTemplateLoader(stringTemplateLoader);
            //获取模板
            Template template1 = configuration.getTemplate("template");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getTemplateByPageId(String pageId) {
        // 取出页面的信息
        CmsPage cmsPage = this.getById(pageId);
        if (null == cmsPage) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        // 查询模板信息
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        CmsTemplate cmsTemplate = null;
        if (optional.isPresent()) {
            cmsTemplate = optional.get();
            // 获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            // 从GridFS中获取模板文件内容
            //根据id查询文件
            GridFSFile gridFSFile =
                    gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开下载流对象
            GridFSDownloadStream gridFSDownloadStream =
                    gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建gridFsResource，用于获取流对象
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            //获取流中的数据
            try {
                String template = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
                return template;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    //获取数据模型
    private Map getModelByPageId(String pageId) {
        // 取出页面的信息
        CmsPage cmsPage = this.getById(pageId);
        if (null == cmsPage) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)) {
            // 页面dataUrl为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //通过resTemplate请求dataUrl获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }


    /**
     * 页面发布
     * @param pageId
     * @return
     */
    public ResponseResult post(String pageId){
        // 执行页面静态化
        String pageHtml = this.getPageHtml(pageId);

        // 将页面静态化文件存储到GridFs中
        CmsPage cmsPage = saveHtml(pageId, pageHtml);

        // 向MQ发送消息
        sendPostPage(pageId,cmsPage.getSiteId());
        return  ResponseResult.SUCCESS();
    }


    /**
     * 向MQ发送消息
     * @param pageId
     */
    private void sendPostPage(String pageId,String siteId){
        // 创建消息对象
        Map<String,String> msg = new HashMap<>();
        msg.put("pageId",pageId);
        //转成json串
        String jsonString = JSON.toJSONString(msg);
        // 发送给mq
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,siteId,jsonString);
    }

    /**
     * 保存html到GridFS中
     * @param pageId
     * @param htmlContent
     * @return
     */
    private CmsPage saveHtml(String pageId,String htmlContent){
        //得到页面信息
        CmsPage cmsPage = this.getById(pageId);
        if(null == cmsPage){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        ObjectId objectId = null;
        try {
            //将htmlContent内容转化成输入流
            InputStream inputStream = IOUtils.toInputStream(htmlContent, "UTF-8");
            //将html文件类容保存到GridFS
             objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将html文件id更新到cmsPage中
        cmsPage.setHtmlFileId(objectId.toHexString());
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }

    /**
     * 保存页面,有则更新,没有则添加
     * @param cmsPage
     * @return
     */
    public CmsPageResult save( CmsPage cmsPage) {
        // 判断页面是否存在
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(null != cmsPage1){
            // 进行更新
           return this.update(cmsPage1.getPageId(),cmsPage);
        }
        return this.add(cmsPage);
    }


    //一键发布页面
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {

        //将页面信息存储到cms_page 集合中
        CmsPageResult save = this.save(cmsPage);
        if(!save.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //得到页面的id
        CmsPage cmsPageSave = save.getCmsPage();
        String pageId = cmsPageSave.getPageId();

        //执行页面发布（先静态化、保存GridFS，向MQ发送消息）
        ResponseResult post = this.post(pageId);
        if(!post.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //拼接页面Url= cmsSite.siteDomain+cmsSite.siteWebPath+ cmsPage.pageWebPath + cmsPage.pageName
        //取出站点id
        String siteId = cmsPageSave.getSiteId();
        CmsSite cmsSite = this.findCmsSiteById(siteId);
        //页面url
        String pageUrl =cmsSite.getSiteDomain() + cmsSite.getSiteWebPath() + cmsPageSave.getPageWebPath() + cmsPageSave.getPageName();
        return new CmsPostPageResult(CommonCode.SUCCESS,pageUrl);
    }

    //根据站点id查询站点信息
    public CmsSite findCmsSiteById(String siteId){
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }
}
