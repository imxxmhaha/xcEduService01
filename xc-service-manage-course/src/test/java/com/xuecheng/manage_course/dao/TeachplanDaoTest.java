package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author xxm
 * @create 2019-03-18 23:51
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TeachplanDaoTest {

    @Autowired
    private TeachplanDao teachplanDao;
    @Test
    public void selectList() {
        String courseId = "4028e581617f945f01617f9dabc40000";
        TeachplanNode teachplanNode = teachplanDao.selectTeachplanList(courseId);
        System.out.println(teachplanNode);
    }
}