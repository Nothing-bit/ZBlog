package com.zjg.blog.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class CategoryInfo {
    private Long id;

    private String name;

    private Integer number;
    @JSONField(format = "yyyy年MM月dd日 HH:mm:ss")
    private Date createBy;
    @JSONField(format = "yyyy年MM月dd日 HH:mm:ss")
    private Date modifiedBy;

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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Date getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Date createBy) {
        this.createBy = createBy;
    }

    public Date getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Date modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}