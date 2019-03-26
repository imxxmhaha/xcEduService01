package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author xxm
 * @create 2019-03-24 12:29
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryDaoTest {

    @Autowired
    private CategoryDao categoryDao;
    @Test
    public void findCategoryList() {
        CategoryNode categoryList = categoryDao.findCategoryList();
        System.out.println(categoryList);
    }
}