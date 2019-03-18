package com.xuecheng.framework.domain.course;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Xxm123
 * @since 2019-03-18
 */
@TableName("course_pub")
public class CoursePub implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
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
     * 大分类
     */
    private String mt;
    /**
     * 小分类
     */
    private String st;
    /**
     * 课程等级
     */
    private String grade;
    /**
     * 学习模式
     */
    private String studymodel;
    /**
     * 教育模式
     */
    private String teachmode;
    /**
     * 课程介绍
     */
    private String description;
    /**
     * 时间戳logstash使用
     */
    private Date timestamp;
    /**
     * 收费规则，对应数据字典
     */
    private String charge;
    /**
     * 有效性，对应数据字典
     */
    private String valid;
    /**
     * 咨询qq
     */
    private String qq;
    /**
     * 价格
     */
    private Float price;
    /**
     * 原价格
     */
    @TableField("price_old")
    private Float priceOld;
    /**
     * 过期时间
     */
    private String expires;
    /**
     * 课程有效期-开始时间
     */
    @TableField("start_time")
    private String startTime;
    /**
     * 课程有效期-结束时间
     */
    @TableField("end_time")
    private String endTime;
    /**
     * 课程图片
     */
    private String pic;
    /**
     * 课程计划
     */
    private String teachplan;
    /**
     * 发布时间
     */
    @TableField("pub_time")
    private String pubTime;


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

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getPriceOld() {
        return priceOld;
    }

    public void setPriceOld(Float priceOld) {
        this.priceOld = priceOld;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTeachplan() {
        return teachplan;
    }

    public void setTeachplan(String teachplan) {
        this.teachplan = teachplan;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    @Override
    public String toString() {
        return "CoursePub{" +
        ", id=" + id +
        ", name=" + name +
        ", users=" + users +
        ", mt=" + mt +
        ", st=" + st +
        ", grade=" + grade +
        ", studymodel=" + studymodel +
        ", teachmode=" + teachmode +
        ", description=" + description +
        ", timestamp=" + timestamp +
        ", charge=" + charge +
        ", valid=" + valid +
        ", qq=" + qq +
        ", price=" + price +
        ", priceOld=" + priceOld +
        ", expires=" + expires +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", pic=" + pic +
        ", teachplan=" + teachplan +
        ", pubTime=" + pubTime +
        "}";
    }
}
