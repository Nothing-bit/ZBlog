package com.zjg.blog.service;

import cn.hutool.core.date.DateUtil;
import com.zjg.blog.BlogApplicationTests;
import com.zjg.blog.entity.Comment;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class CommentServiceTest extends BlogApplicationTests {
    @Autowired
    private CommentService service;
    @Test
    public void add(){
        Comment comment=new Comment();
        comment.setCreateBy(new Date());
        comment.setEffective(false);
        comment.setContent("测试service添加留言");
        comment.setUserId(2L);
        service.addComment(comment);
    }
    @Test
    public void enable(){
        service.enableCommentById(1L);
    }
    @Test
    public void queryAll() {

    }
    @Test
    public void delete(){
    }
    @Test
    public void countNumOfAfterDate(){
        Date date=DateUtil.parse("2019-10-1","yyyy-MM-dd");
        Assert.assertEquals(25,service.countNumOfAfterDate(date));
    }
    @Test
    public void queryEnable(){
        Assert.assertEquals(1,service.queryEnableComment(1,5).getTotal());
    }
    @Test
    public void disable(){
        Assert.assertEquals(1,service.disableCommentById(19));
    }
    @Test
    public void queryLatest(){
        Assert.assertEquals("Nothing",service.queryLatestCommentList(10).get(0).getUsername());
    }
}
