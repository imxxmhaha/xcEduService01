package com.xuecheng.manage_course.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.domain.course.ext.CategoryNode;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
public interface CategoryDao extends BaseMapper<Category> {

    CategoryNode findCategoryList();
}
