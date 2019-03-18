package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

/**
 * @author xxm
 * @create 2019-03-13 20:47
 */
@Service
@Slf4j
public class PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;
    /**
     * 保存html页面到服务器物理路径
     * @param pageId
     */
    public void savePageToServerPath(  String pageId){
        log.info("===保存html页面到服务器===");
        // 根据pageId查询cmsPage
        CmsPage cmsPage = findCmsPageById(pageId);
        // 从cmsPage中获取htmlFileId内容
        String htmlFileId = cmsPage.getHtmlFileId();

        // 从GridFs中查询html文件
        InputStream inputStream = getFileById(htmlFileId);
        if(null == inputStream){
            log.error("====PageService.getFileById({})==,inputStream is null",htmlFileId);
            return ;
        }

        // 得到站点的物理路径
        String siteId = cmsPage.getSiteId();
        CmsSite cmsSite = findCmsSiteById(siteId);
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();
        //得到页面的物理路径
        String pagePath = sitePhysicalPath + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();

        // 将html文件保存到服务器物理路径上
        FileOutputStream fileOutputStream = null;
        try {
             fileOutputStream = new FileOutputStream(new File(pagePath));
            IOUtils.copy(inputStream,fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 根据文件id从GridFS中查询文件内容
     * @param fileId
     * @return
     */
    public InputStream getFileById(String fileId){
        // 文件对象
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        // 打开下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        // 定义
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        InputStream inputStream = null;
        try {
            inputStream =   gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 根据页面id查询页面信息
     * @param pageId
     * @return
     */
    public CmsPage findCmsPageById(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        CmsPage cmsPage = null;
        if(optional.isPresent()){
             cmsPage = optional.get();
        }
        return cmsPage;
    };
    /**
     * 根据页面id查询页面信息
     * @param siteId
     * @return
     */
    public CmsSite findCmsSiteById(String siteId) {
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        CmsSite cmsSite = null;
        if(optional.isPresent()){
            cmsSite = optional.get();
        }
        return cmsSite;
    };


}
