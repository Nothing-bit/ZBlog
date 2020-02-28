package com.zjg.blog.service;

import com.zjg.blog.BlogApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SysSettingServiceTest extends BlogApplicationTests {
    @Autowired
    SysSettingService sysSettingService;
    @Test
    public void add(){
        sysSettingService.addSetting("notice","测试公告");
    }
    @Test
    public void queryAll(){
        Assert.assertEquals(sysSettingService.queryAllSettings().get("notice"),"ce");
    }
    @Test
    public void update(){
        Assert.assertEquals(sysSettingService.updateSettingByName("notice","测试修改0"),1);
    }
    @Test
    public void queryNotice(){
        Assert.assertEquals(sysSettingService.querySettingByName("latestLoginTime"),"");
    }
}
