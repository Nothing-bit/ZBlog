package com.zjg.blog.dao;

import com.zjg.blog.entity.ArticlePicture;
import com.zjg.blog.entity.ArticlePictureExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticlePictureMapper {
    int deleteByExample(ArticlePictureExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ArticlePicture record);

    int insertSelective(ArticlePicture record);

    List<ArticlePicture> selectByExample(ArticlePictureExample example);

    ArticlePicture selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ArticlePicture record, @Param("example") ArticlePictureExample example);

    int updateByExample(@Param("record") ArticlePicture record, @Param("example") ArticlePictureExample example);

    int updateByPrimaryKeySelective(ArticlePicture record);

    int updateByPrimaryKey(ArticlePicture record);
}