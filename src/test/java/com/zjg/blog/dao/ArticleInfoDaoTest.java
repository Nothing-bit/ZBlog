package com.zjg.blog.dao;

import com.zjg.blog.BlogApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ArticleInfoDaoTest extends BlogApplicationTests {
    @Autowired
    private ArticleInfoMapper infoMapper;
    @Test
    public void queryMonths(){
        List<String> daoList=infoMapper.queryMonths();
        for(String item:daoList){
            System.out.println(item);
        }
    }
    @Test
    public void countMonths(){
        Assert.assertEquals(5,infoMapper.countMonths());
    }
}
