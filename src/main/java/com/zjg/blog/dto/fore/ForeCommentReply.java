package com.zjg.blog.dto.fore;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class ForeCommentReply {
    /**
     * 留言下子回复，统一为@形式
     * create 2020年4月3日11:08:34
     * author zjg
     */
    private String username;
    private long userId;//用户id
    private String targetUsername;//目标用户名 例：Nothing:@zhoujianguo test reply
    private String avatar;
    private String content;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createBy;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public void setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Date createBy) {
        this.createBy = createBy;
    }
}
