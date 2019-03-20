package com.xuecheng.manage_course.service;

import com.baomidou.mybatisplus.service.IService;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
public interface TeachplanService extends IService<Teachplan> {
    TeachplanNode findTeachplanList(String courseId);

    ResponseResult addTeachplan(Teachplan teachplan);
}
