package com.zjg.blog.dao;

import com.zjg.blog.entity.TagInfo;
import com.zjg.blog.entity.TagInfoExample;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TagInfoMapper {
    long countByExample(TagInfoExample example);

    int deleteByExample(TagInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TagInfo record);

    int insertSelective(TagInfo record);

    List<TagInfo> selectByExample(TagInfoExample example);

    TagInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TagInfo record);

    int updateByPrimaryKey(TagInfo record);

    List<Map<String,Object>> selectNameAndNumber();
}