package com.zjg.blog.dto.fore;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class LatestCommentDto {
    /**
     * 首页最新留言展示
     */
    private String username;
    private String avatar;
    private String content;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createBy;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
