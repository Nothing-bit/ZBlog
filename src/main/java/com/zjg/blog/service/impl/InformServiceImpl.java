package com.zjg.blog.service.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.zjg.blog.service.InformService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InformServiceImpl implements InformService {
    @Value("${blog.mail.host}")
    String mailHost;
    @Value("${blog.mail.pass}")
    String mailPass;
    @Value("${blog.mail.from}")
    String mailFrom;
    @Override
    public void mail(String address, String subject, String content) {
        MailAccount account=new MailAccount();
        account.setHost(mailHost);
        account.setFrom(mailFrom);
        account.setPass(mailPass);
        account.setAuth(true);
        account.setSslEnable(true);
        MailUtil.send(account, address, subject, content, false);
    }
}
