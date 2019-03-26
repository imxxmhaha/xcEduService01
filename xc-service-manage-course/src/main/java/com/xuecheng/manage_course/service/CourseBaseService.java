package com.xuecheng.manage_course.service;

import com.baomidou.mybatisplus.service.IService;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.model.response.ResponseResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
public interface CourseBaseService extends IService<CourseBase> {

    AddCourseResult addCourseBase(CourseBase courseBase);

    CourseBase getCoursebaseById(String courseId);

    ResponseResult updateCoursebase(String id, CourseBase courseBase);

    ResponseResult addCoursePic(String courseId, String pic);

    CoursePic findCoursePic(String courseId);

    ResponseResult deleteCoursePicById(String courseId);
}
