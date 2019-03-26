package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author xxm
 * @create 2019-03-23 16:41
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class CourseBaseDaoTest {

    @Autowired
    private CourseBaseDao courseBaseDao;

    @Test
    public void testPageHelper() {
        PageHelper.startPage(1, 10);
        CourseListRequest courseListRequest = new CourseListRequest();
        courseListRequest.setCompanyId("1");
        Page<CourseInfo> courseListPage = courseBaseDao.findCourseListPage(courseListRequest);
        List<CourseInfo> result = courseListPage.getResult();
        System.out.println(courseListPage);
    }
}