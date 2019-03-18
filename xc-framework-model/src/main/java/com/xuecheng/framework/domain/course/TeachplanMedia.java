package com.xuecheng.framework.domain.course;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
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
@TableName("teachplan_media")
public class TeachplanMedia implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程计划id
     */
    @TableId("teachplan_id")
    private String teachplanId;
    /**
     * 媒资文件id
     */
    @TableField("media_id")
    private String mediaId;
    /**
     * 媒资文件的原始名称
     */
    @TableField("media_fileoriginalname")
    private String mediaFileoriginalname;
    /**
     * 媒资文件访问地址
     */
    @TableField("media_url")
    private String mediaUrl;
    /**
     * 课程Id
     */
    private String courseid;


    public String getTeachplanId() {
        return teachplanId;
    }

    public void setTeachplanId(String teachplanId) {
        this.teachplanId = teachplanId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaFileoriginalname() {
        return mediaFileoriginalname;
    }

    public void setMediaFileoriginalname(String mediaFileoriginalname) {
        this.mediaFileoriginalname = mediaFileoriginalname;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    @Override
    public String toString() {
        return "TeachplanMedia{" +
        ", teachplanId=" + teachplanId +
        ", mediaId=" + mediaId +
        ", mediaFileoriginalname=" + mediaFileoriginalname +
        ", mediaUrl=" + mediaUrl +
        ", courseid=" + courseid +
        "}";
    }
}
