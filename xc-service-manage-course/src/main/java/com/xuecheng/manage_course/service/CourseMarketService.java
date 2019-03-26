package com.xuecheng.manage_course.service;

import com.baomidou.mybatisplus.service.IService;
import com.xuecheng.framework.domain.course.CourseMarket;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
public interface CourseMarketService extends IService<CourseMarket> {

    CourseMarket updateCourseMarket(String id, CourseMarket courseMarket);

    CourseMarket getCourseMarketById(String courseId);
}
