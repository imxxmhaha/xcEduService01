package com.xuecheng.manage_course.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
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
 *  服务实现类
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
        if(null == teachplan || StringUtils.isEmpty(teachplan.getCourseid())|| StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }


        String courseid = teachplan.getCourseid();
        // 处理parentId
        String parentid = teachplan.getParentid();

        if(StringUtils.isEmpty(parentid)){
            parentid = this.getTeachplanRoot(courseid);
        }
        Teachplan parentTeachplan = teachplanDao.selectById(parentid);
        String parentGrade =StringUtils.defaultString(parentTeachplan.getGrade(),"1") ;
        Teachplan teachplanNew = new Teachplan();
        BeanUtils.copyProperties(teachplan,teachplanNew);
        teachplanNew.setId(idWorker.getId()+"");
        teachplanNew.setParentid(parentid);
        teachplanNew.setCourseid(courseid);
        teachplanNew.setGrade(String.valueOf(Integer.parseInt(parentGrade) +1));
        teachplanNew.setStatus(StringUtils.defaultString(teachplan.getStatus(),"0"));
        teachplanDao.insert(teachplanNew);

        return ResponseResult.SUCCESS();
    }

    private String getTeachplanRoot(String courseId){
        CourseBase courseBase = courseBaseDao.selectById(courseId);
        if(null == courseBase){
            return null;
        }
        Wrapper<Teachplan> ew = new EntityWrapper();
        ew = ew.eq("courseid", courseId).eq("parentid",0);
        List<Teachplan> list = teachplanDao.selectList(ew);
        if(CollectionUtils.isEmpty(list)){
            // 查不到根节点,那么自动添加一个根节点
            Teachplan teachplan = new Teachplan();
            teachplan.setId(idWorker.getId()+"");
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
