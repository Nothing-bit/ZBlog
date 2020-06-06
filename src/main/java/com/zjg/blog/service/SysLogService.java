package com.zjg.blog.service;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.entity.SysLog;

public interface SysLogService {
    /**
     * 添加日志
     * 查看所有日志
     * 获得日志条数
     *
     * junit test pass
     */
    //admin
    int addLog(SysLog sysLog);//添加日志
    PageInfo queryLogs(int pageNum, int pageSize,String searchValue,String orderProperty,String orderDirection);//查看所有日志
    int deleteLog(long id);//删除指定日志记录

    int wipeOutOfDays(int days);//清除日志

}
