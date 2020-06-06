package com.zjg.blog.service;

import com.zjg.blog.BlogApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryServiceTest extends BlogApplicationTests {
    @Autowired
    private CategoryInfoService service;
    @Test
    public void queryAll(){
    }
    @Test
    public void count(){
        Assert.assertEquals(4,service.countCategory());
    }
}
