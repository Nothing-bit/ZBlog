package com.zjg.blog.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.BlogApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class ArticleCommentServiceTest extends BlogApplicationTests {
    @Autowired
    private  ArticleCommentService service;
    @Test
    public void add(){
    }
    @Test
    public void queryByArticleIdAndPageNum(){
        PageInfo pageInfo=service.queryEnableArticleComment(38,1,1);
    }
    @Test
    public void delete(){
        service.deleteArticleComment(4L);
    }
    @Test
    public void queryAll(){
        Assert.assertEquals(10,service.queryArticleComments(1,2).getTotal());
    }
    @Test
    public void latest(){
        Assert.assertEquals(service.queryLatestArticleComment(10).get(0).getTitle(),"Thymeleaf");
    }
    @Test
    public void countAfterDate(){
        Date date=DateUtil.parse("2019-10-1","yyyy-MM-dd");
        Assert.assertEquals(service.countNumOfAfterDate(date),20);
    }
}
