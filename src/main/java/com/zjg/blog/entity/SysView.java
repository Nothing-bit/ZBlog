package com.zjg.blog.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class SysView {
    private Long id;

    private String ip;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Date getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Date createBy) {
        this.createBy = createBy;
    }
}