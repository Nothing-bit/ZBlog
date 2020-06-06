package com.zjg.blog.service;

public interface InformService {
    /**
     * 通知服务
     * create 2020年4月3日11:21:25
     * author zjg
     */
    void mail(String address, String subject,String content);//发送邮件
}
