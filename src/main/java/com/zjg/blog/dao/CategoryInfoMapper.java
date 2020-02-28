package com.zjg.blog.dao;

import com.zjg.blog.entity.CategoryInfo;
import com.zjg.blog.entity.CategoryInfoExample;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryInfoMapper {
    long countByExample(CategoryInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CategoryInfo record);

    int insertSelective(CategoryInfo record);

    List<CategoryInfo> selectByExample(CategoryInfoExample example);

    CategoryInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CategoryInfo record);

    int updateByPrimaryKey(CategoryInfo record);
}