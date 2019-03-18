package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author xxm
 * @create 2019-03-18 23:22
 */
@Api(value = "课程管理接口",description = "cms页面管理接口,提供页面的增、删、改、查")
public interface CourseControllerApi {

    @ApiOperation("课程管理计划")
    public TeachplanNode findTeachplanList(String courseId);
}
