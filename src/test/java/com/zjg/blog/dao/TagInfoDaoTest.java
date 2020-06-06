package com.zjg.blog.dao;

import com.zjg.blog.BlogApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class TagInfoDaoTest extends BlogApplicationTests {
    @Autowired
    private TagInfoMapper tagInfoMapper;
    @Test
    public void selectNameAndNumber(){
        List<Map<String,Object>> daoList=tagInfoMapper.selectNameAndNumber();
        return;
    }
}
