package com.zjg.blog.dto.admin;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class CommentAdminDto {
    /**
     * 留言传输层对象
     * create by zjg 2019年10月6日11:30:42
     */
    private long id;
    private long targetId;
    private String username;

    private String source;
    private boolean effective;
    private String content;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createBy;
    private String ip;

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

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public boolean getEffective() {
        return effective;
    }

    public void setEffective(boolean effective) {
        this.effective = effective;
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
}
