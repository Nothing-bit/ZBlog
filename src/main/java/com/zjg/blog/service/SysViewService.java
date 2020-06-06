package com.zjg.blog.service;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.entity.SysView;
import com.zjg.blog.entity.TimesStatistic;

import java.util.Date;
import java.util.List;

public interface SysViewService {
    /**
     * 访问记录
     *
     *junit test pass
     */
    int addView(SysView sysView);//添加访问记录
    long getViewCount();//获取访问记录条数
    PageInfo queryViews(int pageNum,int pageSize,String searchValue,String orderProperty,String orderDirection);//查看所有访问历史
    int deleteView(long id);//删除指定访问记录

    long countNumOfAfterDate(Date date);//获取指定时间后新增的浏览次数
    List<TimesStatistic> queryViewCountByDays(int days);//根据日实现统计访问次数
    int wipeViewOutOfDays(int days);//清除指定日之前的访问数据
}
