package com.zjg.blog.dto.fore;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

public class ArticleCommentForeDto {
    /**
     * 文章评论
     * create by zjg 2019年9月13日17:23:53
     * modified by zjg 2019年11月7日15:15:09
     */
    private long id;                //评论id

    private String username;        //昵称
    private String avatar;          //头像

    private String content;         //回复内容
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createBy;

    private List<ArticleCommentForeDto> replyList;  //子评论列表

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

    public List<ArticleCommentForeDto> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<ArticleCommentForeDto> replyList) {
        this.replyList = replyList;
    }
}
