package com.zjg.blog.service;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.dto.ArticlePreview;
import com.zjg.blog.dto.ArticleView;

import java.util.Date;

public interface ArticleService {
    /** 文章管理
     *
     */
    // admin
    int insertArticle(ArticleView article);
    int deleteArticle(long id);
    int updateArticle(ArticleView article);
    PageInfo queryPreviews(int pageNum,int pageSize,String searchValue,String orderProperty,String orderDirection);

    //fore
    PageInfo<ArticlePreview> queryPublicPreviews(int pageNum, int pageSize);//分页获取公开展示
    PageInfo<ArticlePreview> queryPreviewsByCategory(long categoryId, int pageNum, int pageSize);//根据分类分页查询缩略信息
    PageInfo<ArticlePreview> queryPreviewsByTagName(String tagName, int pageNum, int pageSize);//通过标签名进行查询
    PageInfo<String>queryMonthList(int pageNum,int pageSize);//获取月份归档列表
    long countMonths();//获取月份归档长度
    long countArticleByBeginAndEndDate(Date beginDate, Date endDate);//统计begin end内文章总数
    PageInfo<ArticlePreview> queryPreviewsByBeginAndEndDate(Date beginDate, Date endDate, int pageNum, int pageSize);//根据所属月份
    long countPublicArticle();//统计文章总数
    //privated
    PageInfo<ArticlePreview> queryPrivatedPreviews(int pageNum, int pageSize);

    //public
    ArticleView getOneById(long id);//获取一篇文章的详细信息

 }
