package com.xuecheng.manage_course.service;

import com.baomidou.mybatisplus.service.IService;
import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.domain.course.ext.CategoryNode;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
public interface CategoryService extends IService<Category> {

    CategoryNode findCategoryList();
}
