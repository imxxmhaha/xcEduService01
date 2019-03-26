package com.xuecheng.manage_course.service.impl;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseDao;
import com.xuecheng.manage_course.dao.CoursePicDao;
import com.xuecheng.manage_course.service.CourseBaseService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private CourseBaseDao courseBaseDao;
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CoursePicDao coursePicDao;

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
        if(null == dbcourse){
            CoursePic coursePic  = new CoursePic();
            coursePic.setPic(pic);
            coursePic.setCourseid(courseId);
            coursePicDao.insert(coursePic);
        }else{
            coursePicDao.updateByCId(courseId,pic);
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
        if(row>0){
            return ResponseResult.SUCCESS();
        }else{
            return ResponseResult.FAIL();
        }
    }
}
