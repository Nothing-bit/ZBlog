package com.zjg.blog.service;

import cn.hutool.core.date.DateUtil;
import com.zjg.blog.BlogApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class SysViewServiceTest extends BlogApplicationTests {
    @Autowired
    private SysViewService service;
    @Test
    public void countAll(){
        Assert.assertEquals(20,service.getViewCount());
    }
    @Test
    public void queryAll(){

    }
    @Test
    public void queryCountByDays(){
        Assert.assertEquals(5,service.queryViewCountByDays(5).get(1).getCount());
    }
    @Test
    public void deleteOutOfDays(){
        Assert.assertEquals(1,service.wipeViewOutOfDays(10));
    }
    @Test
    public void countNumOfAfterDate(){
        Date date= DateUtil.parse("2020-1-1 18:58:14","yyyy-MM-dd HH:mm:ss");
        Assert.assertEquals(10,service.countNumOfAfterDate(date));
    }
}
