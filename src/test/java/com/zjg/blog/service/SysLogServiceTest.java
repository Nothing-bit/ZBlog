package com.zjg.blog.service;

import com.zjg.blog.BlogApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SysLogServiceTest extends BlogApplicationTests {
    @Autowired
    private SysLogService service;
    @Test
    public void queryAll(){
        Assert.assertEquals(418,service.queryLogs(1,2));
    }
    @Test
    public  void wipeOutOfDays(){
        Assert.assertEquals(10,service.wipeOutOfDays(10));
    }
    @Test
    public void informByMail(){
        service.informByMail("zhoujianguo@njfu.edu.cn","测试标题","测试内容");
    }
}
