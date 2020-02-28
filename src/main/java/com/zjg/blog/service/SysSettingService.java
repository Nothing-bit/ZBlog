package com.zjg.blog.service;

import java.util.Map;

public interface SysSettingService {
    /**
     * 管理员用修改后台设置选项
     * create by zjg 2020年1月31日13:13:30
     */
    Map<String,String> queryAllSettings();//获取全部设置信息
    int updateSettingByName(String name,String value);//修改指定设置选项的值
    int addSetting(String name,String value);//添加新设置选项

    /**
     * 前端展示后台设置
     */
    String querySettingByName(String name);//获取指定属性的值
}
