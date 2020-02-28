package com.zjg.blog.service;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.dto.fore.LatestArticleCommentDto;
import com.zjg.blog.entity.ArticleComment;

import java.util.Date;
import java.util.List;

public interface ArticleCommentService {
    /**
     * 文章评论
     * 先发留言
     * 再插入文章
     *
     * junit test passed
     */
    //admin
    int enableArticleComment(long id);//审核通过一条评论
    long countNumOfAfterDate(Date date);//获取指定时间后的新增评论条数
    long countNumOfAbleByArticleId(long id);//获取对应文章id的可展示评论条数
    int disableArticleComment(long id);//使某条评论失效
    int deleteArticleComment(long id);//删除一条评论
    PageInfo queryArticleComments(int pageNum, int pageSize);//
    //fore
    PageInfo queryEnableArticleComment(long id,int pageNum,int pageSize);//展示文章指定页评论
    List<LatestArticleCommentDto> queryLatestArticleComment(int size);//展示最近的文章评论
    //public
    int addArticleComment(ArticleComment articleComment);//添加一条评论
}
