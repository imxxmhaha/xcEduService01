package com.xuecheng.manage_course.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xuecheng.framework.domain.course.CoursePic;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
public interface CoursePicDao extends BaseMapper<CoursePic> {

    @Update("update course_pic set pic = #{pic} where courseid = #{courseId}")
    void updateByCId(@Param("courseId") String courseId,@Param("pic") String pic);

    @Select("select * from course_pic where courseid = #{courseId}")
    CoursePic selectCoursePicById(@Param("courseId") String courseId);

    @Delete("delete from course_pic where courseid = #{courseId}")
    Integer deleteCoursePicById(@Param("courseId") String courseId);
}
