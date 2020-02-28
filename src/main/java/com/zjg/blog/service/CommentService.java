package com.zjg.blog.service;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.dto.fore.CommentForeDto;
import com.zjg.blog.dto.fore.LatestCommentDto;
import com.zjg.blog.entity.Comment;

import java.util.Date;
import java.util.List;

/**
 * CreateBy 周建国 2019年9月16日19:07:12
 * 留言服务
 */
public interface CommentService  {
    /**
     * admin
     */
    int enableCommentById(long id);//审核通过留言
    long countNumOfAfterDate(Date date);//获取指定日期后新增的留言条数
    int deleteCommentById(long id);//删除一条留言
    int disableCommentById(long id);//使某一条留言失效
    PageInfo queryComments(int pageNum,int pageSize);//获取留言

    /**
     * fore
     */
    int addComment(Comment comment);//添加一条留言
    long countNumOfEnable();
    PageInfo<CommentForeDto> queryEnableComment(int pageNum, int pageSize);//根据页码获取评论

    List<LatestCommentDto> queryLatestCommentList(int size);//获取最新的留言记录
}
