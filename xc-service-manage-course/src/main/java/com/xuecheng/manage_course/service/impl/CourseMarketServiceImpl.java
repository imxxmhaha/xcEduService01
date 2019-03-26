package com.xuecheng.manage_course.service.impl;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.manage_course.dao.CourseMarketDao;
import com.xuecheng.manage_course.service.CourseMarketService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
@Service
public class CourseMarketServiceImpl extends ServiceImpl<CourseMarketDao, CourseMarket> implements CourseMarketService {


    @Autowired
    private CourseMarketDao courseMarketDao;

    public CourseMarket getCourseMarketById(String courseid) {

        return courseMarketDao.selectById(courseid);
    }

    @Transactional
    public CourseMarket updateCourseMarket(String id, CourseMarket courseMarket) {
        CourseMarket one = this.getCourseMarketById(id);
        if (one != null) {
            one.setCharge(courseMarket.getCharge());
            one.setStartTime(courseMarket.getStartTime());//课程有效期，开始时间
            one.setEndTime(courseMarket.getEndTime());//课程有效期，结束时间
            one.setPrice(courseMarket.getPrice());
            one.setQq(courseMarket.getQq());
            one.setValid(courseMarket.getValid());
            courseMarketDao.updateById(one);
        } else {
            //添加课程营销信息
            one = new CourseMarket();
            BeanUtils.copyProperties(courseMarket, one);
            //设置课程id
            one.setId(id);
            courseMarketDao.insert(one);
        }
        return one;
    }
}
