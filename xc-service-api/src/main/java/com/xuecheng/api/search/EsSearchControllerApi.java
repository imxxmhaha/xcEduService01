package com.xuecheng.api.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author xxm
 * @create 2019-04-24 20:46
 */
@Api(value = "课程搜索接口",description = "课程搜索",tags = {"课程搜索"})
public interface EsSearchControllerApi {
    // 搜索课程信息
    @ApiOperation("课程综合搜索")
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam);
}
