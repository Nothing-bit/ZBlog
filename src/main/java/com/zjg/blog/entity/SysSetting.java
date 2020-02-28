package com.zjg.blog.entity;

import java.util.Date;

public class SysSetting {
    private Long id;

    private String name;

    private String value;

    private Date createBy;

    private Date operateBy;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public Date getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Date createBy) {
        this.createBy = createBy;
    }

    public Date getOperateBy() {
        return operateBy;
    }

    public void setOperateBy(Date operateBy) {
        this.operateBy = operateBy;
    }
}