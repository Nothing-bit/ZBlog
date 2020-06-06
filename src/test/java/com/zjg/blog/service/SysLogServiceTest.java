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
    }
    @Test
    public  void wipeOutOfDays(){
        Assert.assertEquals(10,service.wipeOutOfDays(10));
    }
}
