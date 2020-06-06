package com.zjg.blog.dto.fore;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

public class ForeComment {
    /**
     * 修改原先的逻辑
     * 前台留言展示bean
     * update 2020年4月3日11:03:34
     * author zjg
     */
    private long id;//留言id
    private long userId;//用户id
    private String username;
    private String avatar;
    private String content;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createBy;
    private List<ForeCommentReply> replyList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public List<ForeCommentReply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<ForeCommentReply> replyList) {
        this.replyList = replyList;
    }
}
