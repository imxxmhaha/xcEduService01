package com.xuecheng.manage_course.service.impl;

import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.manage_course.dao.CategoryDao;
import com.xuecheng.manage_course.service.CategoryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {

}
