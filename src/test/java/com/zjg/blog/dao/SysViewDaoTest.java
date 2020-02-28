package com.zjg.blog.dao;

import com.zjg.blog.BlogApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SysViewDaoTest extends BlogApplicationTests {
    @Autowired
    private SysViewMapper mapper;
    @Test
    public void queryCountByDays(){
        Assert.assertEquals(5,mapper.queryCountByDays(5).get(1).getCount());
    }
}
