package com.xuecheng.manage_course.service.impl;

import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_course.service.TeachplanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author xxm
 * @create 2019-03-23 18:11
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class TeachplanServiceImplTest {
@Autowired
private TeachplanService teachplanService;

    @Test
    public void findCourseList() {
        CourseListRequest courseListRequest = new CourseListRequest();
        courseListRequest.setCompanyId("1");
        QueryResponseResult<CourseInfo> courseList = teachplanService.findCourseList(1, 10, courseListRequest);
        System.out.println(courseList);
    }
}