package com.zjg.blog.service;

import cn.hutool.core.date.DateUtil;
import com.zjg.blog.BlogApplicationTests;
import com.zjg.blog.dto.ArticleViewDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleServiceTest extends BlogApplicationTests {
    @Autowired
    private ArticleService service;
    @Test
    public void queryAll(){

    }

    @Test
    public void getOneById(){
        Assert.assertEquals("spring,java,thymeleaf",service.getOneById(54).getTagList());
    }
    @Test
    public void assertPreAndNext(){
        Assert.assertEquals(41,service.getOneById(41).getNextId());
        Assert.assertEquals(33,service.getOneById(33).getPreId());
    }
    @Test
    public void deleteById(){
        service.deleteArticle(55);
    }
    @Test
    public void insert(){
        ArticleViewDto viewDto=new ArticleViewDto();
        viewDto.setCategoryId(2);
        viewDto.setContent("这是测试content");
        viewDto.setTitle("测试title");
        viewDto.setPictureUrl("测试url");
        viewDto.setSummary("测试Summary");
        viewDto.setTop(true);
        viewDto.setTraffic(0);
        viewDto.setPrivated(false);
        List<String> tagList=new ArrayList<>();
        tagList.add("java");
        viewDto.setTagList(tagList);
        service.insertArticle(viewDto);
    }
    @Test
    public void update(){
        ArticleViewDto viewDto=new ArticleViewDto();
        viewDto.setId(54);
        viewDto.setCategoryId(2);
        viewDto.setContent("这是测试content update");
        viewDto.setTitle("测试title update");
        viewDto.setPictureUrl("测试url update");
        viewDto.setSummary("测试Summary update");
        viewDto.setTop(true);
        viewDto.setTraffic(999);
        List<String> tagList=new ArrayList<>();
        tagList.add("c++");
        tagList.add("c");
        viewDto.setTagList(tagList);
        service.updateArticle(viewDto);
    }
    @Test
    public void count(){
        Assert.assertEquals(4,service.countPublicArticle());
    }
    @Test
    public void queryByTag(){
        Assert.assertEquals(1,service.queryPreviewsByTagName("Java",1,2).getTotal());
        Assert.assertEquals(2,service.queryPreviewsByTagName("Thymeleaf",1,2).getTotal());
    }
    @Test
    public void queryByDate(){
        Date date= DateUtil.parse("2019-11","yyyy-MM");
        Date beginDate=DateUtil.beginOfMonth(date);
        Date endDate=DateUtil.endOfMonth(date);
        Assert.assertEquals(service.queryPreviewsByBeginAndEndDate(beginDate,endDate,1,10).getList().size(),3);
    }
}
