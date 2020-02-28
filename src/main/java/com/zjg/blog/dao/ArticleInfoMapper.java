package com.zjg.blog.dao;

import com.zjg.blog.entity.ArticleInfo;
import com.zjg.blog.entity.ArticleInfoExample;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ArticleInfoMapper {
    long countByExample(ArticleInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ArticleInfo record);

    int insertSelective(ArticleInfo record);

    List<ArticleInfo> selectByExample(ArticleInfoExample example);

    ArticleInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArticleInfo record);

    int updateByPrimaryKey(ArticleInfo record);

    List<String> queryMonths();
}