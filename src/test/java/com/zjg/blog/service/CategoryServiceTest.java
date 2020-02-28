package com.zjg.blog.service;

import com.alibaba.fastjson.JSONArray;
import com.zjg.blog.BlogApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryServiceTest extends BlogApplicationTests {
    @Autowired
    private CategoryInfoService service;
    @Test
    public void queryAll(){
        String str= JSONArray.toJSONString(service.queryAllCategory());
        System.out.println(str);
    }
    @Test
    public void count(){
        Assert.assertEquals(4,service.countCategory());
    }
}
