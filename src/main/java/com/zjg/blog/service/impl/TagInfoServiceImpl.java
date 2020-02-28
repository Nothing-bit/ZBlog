package com.zjg.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.dao.ArticleTagMapper;
import com.zjg.blog.dao.TagInfoMapper;
import com.zjg.blog.entity.ArticleTagExample;
import com.zjg.blog.entity.TagInfo;
import com.zjg.blog.entity.TagInfoExample;
import com.zjg.blog.service.TagInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagInfoServiceImpl implements TagInfoService {
    @Autowired
    private TagInfoMapper tagInfoMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Override
    public List<TagInfo> queryAllTagInfo() {
        TagInfoExample tagInfoExample=new TagInfoExample();
        return tagInfoMapper.selectByExample(tagInfoExample);
    }

    @Override
    public List<TagInfo> queryHotTagInfo(int top) {
        TagInfoExample tagInfoExample=new TagInfoExample();
        tagInfoExample.setOrderByClause("number desc");
        PageHelper.startPage(1,top);
        List<TagInfo> daoList=tagInfoMapper.selectByExample(tagInfoExample);
        return daoList;
    }

    @Override
    public int deleteTagById(long id) {
        ArticleTagExample articleTagExample=new ArticleTagExample();
        articleTagExample.createCriteria().andTagIdEqualTo(id);
        articleTagMapper.deleteByExample(articleTagExample);
        tagInfoMapper.deleteByPrimaryKey(id);
        return 1;
    }

    @Override
    public PageInfo queryTagInfos(int pageNum, int pageSize) {
        TagInfoExample tagInfoExample=new TagInfoExample();
        tagInfoExample.setOrderByClause("create_by desc");
        PageHelper.startPage(pageNum,pageSize);
        List<TagInfo> daoList=tagInfoMapper.selectByExample(tagInfoExample);
        PageInfo<TagInfo> daoPageInfo=new PageInfo<>(daoList);
        return daoPageInfo;
    }

    @Override
    public int updateTagInfo(TagInfo tagInfo) {
        tagInfoMapper.updateByPrimaryKey(tagInfo);
        return 1;
    }

    @Override
    public List<TagInfo> queryTagInfosNameLike(String tagName) {
        TagInfoExample tagInfoExample=new TagInfoExample();
        tagInfoExample.setOrderByClause("number desc");
        tagInfoExample.createCriteria().andNameLike("%"+tagName+"%");
        List<TagInfo> tagInfoList=tagInfoMapper.selectByExample(tagInfoExample);
        return tagInfoList;
    }

    @Override
    public TagInfo queryOneByName(String tagName) {
        TagInfoExample tagInfoExample=new TagInfoExample();
        tagInfoExample.createCriteria().andNameEqualTo(tagName);
        TagInfo tagInfo=tagInfoMapper.selectByExample(tagInfoExample).get(0);
        return tagInfo;
    }

    @Override
    public long countTag() {
        TagInfoExample tagInfoExample=new TagInfoExample();
        return tagInfoMapper.countByExample(tagInfoExample);
    }
}
