package com.xuecheng.manage_course.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
public interface TeachplanDao extends BaseMapper<Teachplan> {

    //课程计划查询
    public TeachplanNode selectTeachplanList(String courseId);
}
