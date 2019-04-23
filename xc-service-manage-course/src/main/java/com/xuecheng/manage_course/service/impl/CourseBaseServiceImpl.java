package com.xuecheng.manage_course.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.netflix.discovery.converters.Auto;
import com.sun.xml.internal.bind.v2.model.core.ID;
import com.xuecheng.api.client.CmsPageClient;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.ext.CourseView;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.*;
import com.xuecheng.manage_course.service.CourseBaseService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    private CoursePubDao coursePubDao;

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


    /**
     * 课程发布
     * @param id
     * @return
     */
    @Transactional
    public CoursePublishResult publish(String id) {
        //查询课程
        CourseBase courseBaseById = this.findCourseBaseById(id);

        //准备页面信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);//站点id
        cmsPage.setDataUrl(publish_dataUrlPre+id);//数据模型url
        cmsPage.setPageName(id+".html");//页面名称
        cmsPage.setPageAliase(courseBaseById.getName());//页面别名，就是课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);//页面物理路径
        cmsPage.setPageWebPath(publish_page_webpath);//页面webpath
        cmsPage.setTemplateId(publish_templateId);//页面模板id
        //调用cms一键发布接口将课程详情页面发布到服务器
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if(!cmsPostPageResult.isSuccess()){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }

        //保存课程的发布状态为“已发布”
        CourseBase courseBase = this.saveCoursePubState(id);
        if(courseBase == null){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }

        //保存课程索引信息
        //先创建一个coursePub对象
        CoursePub coursePub = createCoursePub(id);
        //将coursePub对象保存到数据库
        saveCoursePub(id,coursePub);
        //缓存课程的信息
        //...
        //得到页面的url
        String pageUrl = cmsPostPageResult.getPageUrl();
        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
    }


    //将coursePub对象保存到数据库
    private CoursePub saveCoursePub(String id,CoursePub coursePub){

        CoursePub coursePubNew = null;
        //根据课程id查询coursePub
        coursePubNew = coursePubDao.selectById(id);
        if (coursePubNew == null){
            coursePubNew = new CoursePub();
            //将coursePub对象中的信息保存到coursePubNew中
            BeanUtils.copyProperties(coursePub,coursePubNew);
            coursePubNew.setId(id);
            //时间戳,给logstach使用
            coursePubNew.setTimestamp(new Date());
            //发布时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String date = simpleDateFormat.format(new Date());
            coursePubNew.setPubTime(date);
            coursePubDao.insert(coursePubNew);
        }else {
            //将coursePub对象中的信息保存到coursePubNew中
            BeanUtils.copyProperties(coursePub,coursePubNew);
            coursePubNew.setId(id);
            //时间戳,给logstach使用
            coursePubNew.setTimestamp(new Date());
            //发布时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String date = simpleDateFormat.format(new Date());
            coursePubNew.setPubTime(date);
            coursePubDao.updateById(coursePubNew);
        }
        return coursePubNew;
    }


    //创建coursePub对象
    private CoursePub createCoursePub(String id){
        CoursePub coursePub = new CoursePub();
        //根据课程id查询course_base3
        CourseBase courseBase = courseBaseDao.selectById(id);
        courseBase = null == courseBase? new CourseBase():courseBase;
        //将courseBase属性拷贝到CoursePub中
        BeanUtils.copyProperties(courseBase,coursePub);

        //查询课程图片
        CoursePic coursePic = coursePicDao.selectCoursePicById(id);
        if(null != coursePic){
            BeanUtils.copyProperties(coursePic, coursePub);
        }


        //课程营销信息
        CourseMarket courseMarket = courseMarketDao.selectById(id);
        if(null != courseMarket){
            BeanUtils.copyProperties(courseMarket, coursePub);
        }

        TeachplanNode teachplanNode = teachplanDao.selectTeachplanList(id);
        //课程计划信息
        String jsonString = JSON.toJSONString(teachplanNode);
        //将课程计划信息json串保存到 course_pub中
        coursePub.setTeachplan(jsonString);
        return coursePub;

    }

    //更新课程状态为已发布 202002
    private CourseBase  saveCoursePubState(String courseId){
        CourseBase courseBaseById = this.findCourseBaseById(courseId);
        courseBaseById.setStatus("202002");
        courseBaseDao.updateById(courseBaseById);
        return courseBaseById;
    }

}
