package com.zjg.blog.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.dao.SysViewMapper;
import com.zjg.blog.entity.SysView;
import com.zjg.blog.entity.SysViewExample;
import com.zjg.blog.entity.TimesStatistic;
import com.zjg.blog.service.SysViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
@Service
public class SysViewServiceImpl implements SysViewService {
    @Autowired
    private SysViewMapper sysViewMapper;

    @Override
    public int addView(SysView sysView) {
        return sysViewMapper.insert(sysView);
    }

    @Override
    public long getViewCount() {
        SysViewExample sysViewExample=new SysViewExample();
        return sysViewMapper.countByExample(sysViewExample);
    }

    @Override
    public PageInfo queryViews(int pageNum, int pageSize) {
        SysViewExample sysViewExample=new SysViewExample();
        sysViewExample.setOrderByClause("create_by desc");
        PageHelper.startPage(pageNum,pageSize);
        List<SysView> daoList=sysViewMapper.selectByExample(sysViewExample);
        PageInfo<SysView> daoPageInfo=new PageInfo<>(daoList);
        return daoPageInfo;
    }

    @Override
    public long countNumOfAfterDate(Date date) {
        SysViewExample sysViewExample=new SysViewExample();
        sysViewExample.createCriteria()
                .andCreateByGreaterThan(date);
        return sysViewMapper.countByExample(sysViewExample);
    }

    @Override
    public List<TimesStatistic> queryViewCountByDays(int days) {
        List<TimesStatistic> statisticList=sysViewMapper.queryCountByDays(days);
        Collections.reverse(statisticList);
        return statisticList;
    }

    @Override
    public int wipeViewOutOfDays(int days) {
        SysViewExample sysViewExample=new SysViewExample();
        sysViewExample.createCriteria()
                .andCreateByLessThan(DateUtil.offsetDay(new Date(),-days));
        return sysViewMapper.deleteByExample(sysViewExample);
    }
}
