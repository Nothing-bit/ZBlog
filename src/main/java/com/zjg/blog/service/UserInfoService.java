package com.zjg.blog.service;

import me.zhyd.oauth.model.AuthResponse;

import javax.servlet.http.HttpServletRequest;

public interface UserInfoService {
    /**
     * 前端用户登录模块
     * create by zjg 2019年11月6日10:57:36
     */
    int login(AuthResponse response, HttpServletRequest request);//登录
    int logout();//退出
    int check();//检查状态
}
