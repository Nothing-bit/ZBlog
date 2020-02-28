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
    PageInfo queryLogs(int pageNum, int pageSize);//查看所有日志

    int wipeOutOfDays(int days);//自动清除日志

    void informByMail(String address, String subject,String content);//发送邮件
}
