package com.zjg.blog.dto.admin;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class ArticleCommentAdminDto {
    /**
     *  管理评论bean
     *  create by zjg 2019年11月7日16:42:38
     */
    private long id;
    private long articleId;
    private long targetId;
    private String articleTitle;
    private String username;
    private String source;
    private String ip;
    private String content;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createBy;
    private boolean effective;

    public long getId() {
        return id;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public boolean getEffective() {
        return effective;
    }

    public void setEffective(boolean effective) {
        this.effective = effective;
    }
}
