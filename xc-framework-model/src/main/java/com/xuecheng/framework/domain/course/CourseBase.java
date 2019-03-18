package com.xuecheng.framework.domain.course;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
@TableName("course_base")
public class CourseBase implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 课程名称
     */
    private String name;
    /**
     * 适用人群
     */
    private String users;
    /**
     * 课程大分类
     */
    private String mt;
    /**
     * 课程等级
     */
    private String grade;
    /**
     * 学习模式
     */
    private String studymodel;
    /**
     * 授课模式
     */
    private String teachmode;
    /**
     * 课程介绍
     */
    private String description;
    /**
     * 课程小分类
     */
    private String st;
    /**
     * 课程状态
     */
    private String status;
    /**
     * 教育机构
     */
    @TableField("company_id")
    private String companyId;
    /**
     * 创建用户
     */
    @TableField("user_id")
    private String userId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getMt() {
        return mt;
    }

    public void setMt(String mt) {
        this.mt = mt;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStudymodel() {
        return studymodel;
    }

    public void setStudymodel(String studymodel) {
        this.studymodel = studymodel;
    }

    public String getTeachmode() {
        return teachmode;
    }

    public void setTeachmode(String teachmode) {
        this.teachmode = teachmode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CourseBase{" +
        ", id=" + id +
        ", name=" + name +
        ", users=" + users +
        ", mt=" + mt +
        ", grade=" + grade +
        ", studymodel=" + studymodel +
        ", teachmode=" + teachmode +
        ", description=" + description +
        ", st=" + st +
        ", status=" + status +
        ", companyId=" + companyId +
        ", userId=" + userId +
        "}";
    }
}
