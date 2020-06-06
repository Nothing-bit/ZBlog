package com.zjg.blog.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.dao.SysLogMapper;
import com.zjg.blog.entity.SysLog;
import com.zjg.blog.entity.SysLogExample;
import com.zjg.blog.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;
    @Override
    public int addLog(SysLog sysLog) {
        return sysLogMapper.insert(sysLog);
    }

    @Override
    public int deleteLog(long id) {
        sysLogMapper.deleteByPrimaryKey(id);
        return 1;
    }

    @Override
    public PageInfo queryLogs(int pageNum, int pageSize,String searchValue,String orderProperty,String orderDirection) {
        SysLogExample sysLogExample=new SysLogExample();
        sysLogExample.setOrderByClause(orderProperty+" "+orderDirection);
        sysLogExample.createCriteria()
                .andIpLike("%"+searchValue+"%");
        PageHelper.startPage(pageNum,pageSize);
        List<SysLog> daoList=sysLogMapper.selectByExample(sysLogExample);
        PageInfo<SysLog> daoPageInfo=new PageInfo<>(daoList);
        return daoPageInfo;
    }

    @Override
    public int wipeOutOfDays(int days) {
        SysLogExample sysLogExample=new SysLogExample();
        sysLogExample.createCriteria()
                .andCreateByLessThan(DateUtil.offsetDay(new Date(),-days));
        return sysLogMapper.deleteByExample(sysLogExample);
    }

}
