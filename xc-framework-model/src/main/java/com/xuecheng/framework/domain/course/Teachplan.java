package com.xuecheng.framework.domain.course;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
public class Teachplan implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String pname;
    private String parentid;
    /**
     * 层级，分为1、2、3级
     */
    private String grade;
    /**
     * 课程类型:1视频、2文档
     */
    private String ptype;
    /**
     * 章节及课程时介绍
     */
    private String description;
    /**
     * 时长，单位分钟
     */
    private Double timelength;
    /**
     * 课程id
     */
    private String courseid;
    /**
     * 排序字段
     */
    private String orderby;
    /**
     * 状态：未发布、已发布
     */
    private String status;
    /**
     * 是否试学
     */
    private String trylearn;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTimelength() {
        return timelength;
    }

    public void setTimelength(Double timelength) {
        this.timelength = timelength;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrylearn() {
        return trylearn;
    }

    public void setTrylearn(String trylearn) {
        this.trylearn = trylearn;
    }

    @Override
    public String toString() {
        return "Teachplan{" +
        ", id=" + id +
        ", pname=" + pname +
        ", parentid=" + parentid +
        ", grade=" + grade +
        ", ptype=" + ptype +
        ", description=" + description +
        ", timelength=" + timelength +
        ", courseid=" + courseid +
        ", orderby=" + orderby +
        ", status=" + status +
        ", trylearn=" + trylearn +
        "}";
    }
}
