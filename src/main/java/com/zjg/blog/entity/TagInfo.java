package com.zjg.blog.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class TagInfo {
    private Long id;

    private String name;

    private Long number;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Date getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Date createBy) {
        this.createBy = createBy;
    }
}