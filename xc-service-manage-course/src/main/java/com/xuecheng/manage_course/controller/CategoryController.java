package com.xuecheng.manage_course.controller;


import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/findOne/{id}")
    public Category findOne(@PathVariable("id") String id){
        Category category = categoryService.selectById("1");
        return category;
    }
}

