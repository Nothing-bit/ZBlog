package com.zjg.blog.service;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.dto.ArticlePreviewDto;
import com.zjg.blog.dto.ArticleViewDto;

import java.util.Date;
import java.util.List;

public interface ArticleService {
    /** 文章管理
     *
     */
    // admin
    int insertArticle(ArticleViewDto article);
    int deleteArticle(long id);
    int updateArticle(ArticleViewDto article);
    PageInfo queryPreviews(int pageNum,int pageSize);

    //fore
    PageInfo<ArticlePreviewDto> queryPublicPreviews(int pageNum, int pageSize);//分页获取公开展示
    PageInfo<ArticlePreviewDto> queryPreviewsByCategory(long categoryId, int pageNum, int pageSize);//根据分类分页查询缩略信息
    PageInfo<ArticlePreviewDto> queryPreviewsByTagName(String tagName,int pageNum,int pageSize);//通过标签名进行查询
    List<String>queryMonthList();//获取月份归档列表
    PageInfo<ArticlePreviewDto> queryPreviewsByBeginAndEndDate(Date beginDate,Date endDate,int pageNum,int pageSize);//根据所属月份
    long countPublicArticle();//统计日志总数
    //privated
    PageInfo<ArticlePreviewDto> queryPrivatedPreviews(int pageNum, int pageSize);

    //public
    ArticleViewDto getOneById(long id);//获取一篇文章的详细信息
 }
