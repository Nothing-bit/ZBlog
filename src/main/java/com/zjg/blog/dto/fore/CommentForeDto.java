package com.zjg.blog.dto.fore;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

public class CommentForeDto {
    /**
     * 前台留言传输层对象
     */
    private long id;
    private String username;
    private String avatar;
    private String content;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createBy;
    private List<CommentForeDto> replyList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public List<CommentForeDto> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<CommentForeDto> replyList) {
        this.replyList = replyList;
    }
}
