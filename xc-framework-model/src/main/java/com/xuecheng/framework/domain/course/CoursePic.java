package com.xuecheng.framework.domain.course;

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
@TableName("course_pic")
public class CoursePic implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程id
     */
    private String courseid;
    /**
     * 图片id
     */
    private String pic;


    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "CoursePic{" +
        ", courseid=" + courseid +
        ", pic=" + pic +
        "}";
    }
}
