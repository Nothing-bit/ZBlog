package com.zjg.blog.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class TimesStatistic {
    /**
     * 统计使用的pojo类
     */
    private long count;
    @JSONField(format = "yyyy年MM月dd日")
    private Date date;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
