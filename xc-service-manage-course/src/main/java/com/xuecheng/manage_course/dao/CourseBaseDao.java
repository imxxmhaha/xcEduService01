package com.xuecheng.manage_course.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
public interface CourseBaseDao extends BaseMapper<CourseBase> {

    Page<CourseInfo> findCourseListPage(@Param("courseListRequest") CourseListRequest  courseListRequest);
}
