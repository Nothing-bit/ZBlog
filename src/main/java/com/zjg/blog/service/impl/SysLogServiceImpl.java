package com.zjg.blog.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.dao.SysLogMapper;
import com.zjg.blog.entity.SysLog;
import com.zjg.blog.entity.SysLogExample;
import com.zjg.blog.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;
    @Value("${blog.mail.host}")
    String mailHost;
    @Value("${blog.mail.pass}")
    String mailPass;
    @Value("${blog.mail.from}")
    String mailFrom;
    @Override
    public int addLog(SysLog sysLog) {
        return sysLogMapper.insert(sysLog);
    }

    @Override
    public PageInfo queryLogs(int pageNum, int pageSize) {
        SysLogExample sysLogExample=new SysLogExample();
        sysLogExample.setOrderByClause("create_by desc");
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

    @Override
    public void informByMail(String address, String subject,String content) {
        MailAccount account=new MailAccount();
        account.setHost(mailHost);
        account.setFrom(mailFrom);
        account.setPass(mailPass);
        account.setAuth(true);
        account.setSslEnable(true);
        MailUtil.send(account, address, subject, content, false);
    }

}
