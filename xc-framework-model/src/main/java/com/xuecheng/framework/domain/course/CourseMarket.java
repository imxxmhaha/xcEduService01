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
@TableName("course_market")
public class CourseMarket implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程id
     */
    private String id;
    /**
     * 收费规则，对应数据字典
     */
    private String charge;
    /**
     * 有效性，对应数据字典
     */
    private String valid;
    /**
     * 过期时间
     */
    private Date expires;
    /**
     * 咨询qq
     */
    private String qq;
    /**
     * 价格
     */
    private Float price;
    /**
     * 原价
     */
    @TableField("price_old")
    private Float priceOld;
    /**
     * 课程有效期-开始时间
     */
    @TableField("start_time")
    private Date startTime;
    /**
     * 课程有效期-结束时间
     */
    @TableField("end_time")
    private Date endTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "CourseMarket{" +
        ", id=" + id +
        ", charge=" + charge +
        ", valid=" + valid +
        ", expires=" + expires +
        ", qq=" + qq +
        ", price=" + price +
        ", priceOld=" + priceOld +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        "}";
    }
}
