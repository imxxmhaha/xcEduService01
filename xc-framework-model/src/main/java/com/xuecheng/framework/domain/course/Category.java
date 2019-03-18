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
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类标签默认和名称一样
     */
    private String label;
    /**
     * 父结点id
     */
    private String parentid;
    /**
     * 是否显示
     */
    private String isshow;
    /**
     * 排序字段
     */
    private Integer orderby;
    /**
     * 是否叶子
     */
    private String isleaf;


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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getIsshow() {
        return isshow;
    }

    public void setIsshow(String isshow) {
        this.isshow = isshow;
    }

    public Integer getOrderby() {
        return orderby;
    }

    public void setOrderby(Integer orderby) {
        this.orderby = orderby;
    }

    public String getIsleaf() {
        return isleaf;
    }

    public void setIsleaf(String isleaf) {
        this.isleaf = isleaf;
    }

    @Override
    public String toString() {
        return "Category{" +
        ", id=" + id +
        ", name=" + name +
        ", label=" + label +
        ", parentid=" + parentid +
        ", isshow=" + isshow +
        ", orderby=" + orderby +
        ", isleaf=" + isleaf +
        "}";
    }
}
