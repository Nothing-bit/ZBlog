package com.zjg.blog.dao;

import com.zjg.blog.entity.ArticleTag;
import com.zjg.blog.entity.ArticleTagExample;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ArticleTagMapper {
    long countByExample(ArticleTagExample example);

    int deleteByExample(ArticleTagExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ArticleTag record);

    int insertSelective(ArticleTag record);

    List<ArticleTag> selectByExample(ArticleTagExample example);

    ArticleTag selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArticleTag record);

    int updateByPrimaryKey(ArticleTag record);
}