package com.xuecheng.manage_course.service.impl;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.netflix.discovery.converters.Auto;
import com.sun.xml.internal.bind.v2.model.core.ID;
import com.xuecheng.api.client.CmsPageClient;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.ext.CourseView;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseDao;
import com.xuecheng.manage_course.dao.CourseMarketDao;
import com.xuecheng.manage_course.dao.CoursePicDao;
import com.xuecheng.manage_course.dao.TeachplanDao;
import com.xuecheng.manage_course.service.CourseBaseService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseDao, CourseBase> implements CourseBaseService {



    @Value("${course.publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course.publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course.publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course.publish.siteId}")
    private String publish_siteId;
    @Value("${course.publish.templateId}")
    private String publish_templateId;
    @Value("${course.publish.previewUrl}")
    private String previewUrl;

    @Autowired
    private CourseBaseDao courseBaseDao;
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CoursePicDao coursePicDao;
    @Autowired
    private CourseMarketDao courseMarketDao;

    @Autowired
    private TeachplanDao teachplanDao;

    @Autowired
    private CmsPageClient cmsPageClient;

    //添加课程提交
    @Transactional
    public AddCourseResult addCourseBase(CourseBase courseBase) {
        //课程状态默认为未发布 
        courseBase.setStatus("202001");
        courseBase.setId(idWorker.getId() + "");
        courseBaseDao.insert(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS, courseBase.getId());
    }

    public CourseBase getCoursebaseById(String courseid) {
        CourseBase courseBase = courseBaseDao.selectById(courseid);
        return courseBase;
    }

    @Transactional
    public ResponseResult updateCoursebase(String id, CourseBase courseBase) {
        CourseBase one = this.getCoursebaseById(id);
        if (one == null) {
            //抛出异常..
        }
        //修改课程信息
        one.setName(courseBase.getName());
        one.setMt(courseBase.getMt());
        one.setSt(courseBase.getSt());
        one.setGrade(courseBase.getGrade());
        one.setStudymodel(courseBase.getStudymodel());
        one.setUsers(courseBase.getUsers());
        one.setDescription(courseBase.getDescription());
        courseBaseDao.updateById(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 向课程管理数据库添加课程与图片的关联信息
     *
     * @param courseId
     * @param pic
     * @return
     */
    @Override
    @Transactional
    public ResponseResult addCoursePic(String courseId, String pic) {
        CoursePic dbcourse = new CoursePic();
        dbcourse.setCourseid(courseId);
        dbcourse = coursePicDao.selectOne(dbcourse);
        if (null == dbcourse) {
            CoursePic coursePic = new CoursePic();
            coursePic.setPic(pic);
            coursePic.setCourseid(courseId);
            coursePicDao.insert(coursePic);
        } else {
            coursePicDao.updateByCId(courseId, pic);
        }

        return ResponseResult.SUCCESS();
    }

    @Override
    public CoursePic findCoursePic(String courseId) {
        CoursePic coursePic = coursePicDao.selectCoursePicById(courseId);
        return coursePic;
    }

    @Override
    @Transactional
    public ResponseResult deleteCoursePicById(String courseId) {
        Integer row = coursePicDao.deleteCoursePicById(courseId);
        if (row > 0) {
            return ResponseResult.SUCCESS();
        } else {
            return ResponseResult.FAIL();
        }
    }

    /**
     * 查询课程视图,包括基本信息,图片,营销,课程计划
     *
     * @param id
     * @return
     */
    @Override
    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();
        // 课程基本信息
        CourseBase courseBase = courseBaseDao.selectById(id);
        if (null != courseBase) {
            courseView.setCourseBase(courseBase);
        }

        //查询课程图片
        CoursePic coursePic = coursePicDao.selectCoursePicById(id);
        if (null != coursePic) {
            courseView.setCoursePic(coursePic);
        }
        CourseMarket courseMarket = courseMarketDao.selectById(id);
        if (null != courseMarket) {
            courseView.setCourseMarket(courseMarket);
        }

        //课程计划信息
        TeachplanNode teachplanNode = teachplanDao.selectTeachplanList(id);
        courseView.setTeachplanNode(teachplanNode);

        return courseView;
    }

    /**
     * 课程预览
     *
     * @param id
     * @return
     */
    @Override
    public CoursePublishResult preview(String id) {
        CourseBase courseBase = findCourseBaseById(id);
        // 请求cms 添加页面 (远程调用)
        // 准备cmsPage信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);// 站点id
        cmsPage.setDataUrl(publish_dataUrlPre + id);//数据模型url
        cmsPage.setPageName(id+".html");
        cmsPage.setPageAliase(courseBase.getName());//页面别名就是课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);//页面物理路径
        cmsPage.setPageWebPath(publish_page_webpath);//页面webpath
        cmsPage.setTemplateId(publish_templateId);//页面模板id

        // 远程调用cms
        CmsPageResult cmsPageResult = cmsPageClient.saveCmsPage(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            // 返回失败信息
            return new CoursePublishResult(CommonCode.FAIL,null);
        }
        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        // 拼装页面预览的url
        String url = previewUrl+ pageId;
        // 返回CoursePublishResult对象(当中包含了页面预览的url)
        return CoursePublishResult.success(url);
    }

    //根据id查询课程基本信息
    public CourseBase findCourseBaseById(String courseId) {
        CourseBase courseBase = courseBaseDao.selectById(courseId);
        if(null == courseBase){
            ExceptionCast.cast(CourseCode.COURSE_GET_NOTEXISTS);
        }
        return courseBase;
    }
}
