package com.zjg.blog.service;

import com.zjg.blog.BlogApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TagInfoServiceTest extends BlogApplicationTests {
    @Autowired
    private TagInfoService service;
    @Test
    public void generateTagCloud(){
        Assert.assertEquals("java",service.queryAllTagInfo().get(0));
    }
    @Test
    public void getOneByName(){
        Assert.assertEquals("java",service.queryOneByName("java").getName());
    }
    @Test
    public void queryByName(){
        Assert.assertEquals(2,service.queryTagInfosNameLike("分").size());
    }
}
