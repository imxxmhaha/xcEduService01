package com.xuecheng.manage_course.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseDao;
import com.xuecheng.manage_course.dao.TeachplanDao;
import com.xuecheng.manage_course.service.TeachplanService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanDao, Teachplan> implements TeachplanService {

    @Autowired
    private TeachplanDao teachplanDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private CourseBaseDao courseBaseDao;

    @Override
    public TeachplanNode findTeachplanList(String courseId) {
        TeachplanNode teachplanNode = teachplanDao.selectTeachplanList(courseId);
        return teachplanNode;
    }

    @Override
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        if (null == teachplan || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }


        String courseid = teachplan.getCourseid();
        // 处理parentId
        String parentid = teachplan.getParentid();

        if (StringUtils.isEmpty(parentid)) {
            parentid = this.getTeachplanRoot(courseid);
        }
        Teachplan parentTeachplan = teachplanDao.selectById(parentid);
        String parentGrade = StringUtils.defaultString(parentTeachplan.getGrade(), "1");
        Teachplan teachplanNew = new Teachplan();
        BeanUtils.copyProperties(teachplan, teachplanNew);
        teachplanNew.setId(idWorker.getId() + "");
        teachplanNew.setParentid(parentid);
        teachplanNew.setCourseid(courseid);
        teachplanNew.setGrade(String.valueOf(Integer.parseInt(parentGrade) + 1));
        teachplanNew.setStatus(StringUtils.defaultString(teachplan.getStatus(), "0"));
        teachplanDao.insert(teachplanNew);

        return ResponseResult.SUCCESS();
    }

    @Override
    public QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest) {
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        if (page <= 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 20;
        }
        //设置分页参数
        PageHelper.startPage(page, size);
        //分页查询
        Page<CourseInfo> courseListPage = courseBaseDao.findCourseListPage(courseListRequest);
        //查询列表
        List<CourseInfo> list = courseListPage.getResult();
        //总记录数
        long total = courseListPage.getTotal();
        //查询结果集
        QueryResult<CourseInfo> courseIncfoQueryResult = new QueryResult<CourseInfo>();
        courseIncfoQueryResult.setList(list);
        courseIncfoQueryResult.setTotal(total);
        return QueryResponseResult.SUCCESS(courseIncfoQueryResult);
        //return new QueryResponseResult<CourseInfo>(CommonCode.SUCCESS, courseIncfoQueryResult);
    }

    private String getTeachplanRoot(String courseId) {
        CourseBase courseBase = courseBaseDao.selectById(courseId);
        if (null == courseBase) {
            return null;
        }
        Wrapper<Teachplan> ew = new EntityWrapper();
        ew = ew.eq("courseid", courseId).eq("parentid", 0);
        List<Teachplan> list = teachplanDao.selectList(ew);
        if (CollectionUtils.isEmpty(list)) {
            // 查不到根节点,那么自动添加一个根节点
            Teachplan teachplan = new Teachplan();
            teachplan.setId(idWorker.getId() + "");
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setPname(courseBase.getName());
            teachplan.setCourseid(courseId);
            teachplan.setStatus("0");
            teachplanDao.insert(teachplan);
            return teachplan.getId();
        }
        return list.get(0).getId();
    }
}
