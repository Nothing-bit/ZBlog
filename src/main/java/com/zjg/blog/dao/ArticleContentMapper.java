package com.zjg.blog.dao;

import com.zjg.blog.entity.ArticleContent;
import com.zjg.blog.entity.ArticleContentExample;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ArticleContentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ArticleContent record);

    int insertSelective(ArticleContent record);

    List<ArticleContent> selectByExample(ArticleContentExample example);

    ArticleContent selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArticleContent record);

    int updateByPrimaryKey(ArticleContent record);
}