package com.zjg.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.dao.ArticleCommentMapper;
import com.zjg.blog.dao.ArticleInfoMapper;
import com.zjg.blog.dao.UserInfoMapper;
import com.zjg.blog.dto.admin.ArticleCommentAdminDto;
import com.zjg.blog.dto.fore.ArticleCommentForeDto;
import com.zjg.blog.dto.fore.LatestArticleCommentDto;
import com.zjg.blog.entity.ArticleComment;
import com.zjg.blog.entity.ArticleCommentExample;
import com.zjg.blog.entity.ArticleInfo;
import com.zjg.blog.entity.UserInfo;
import com.zjg.blog.service.ArticleCommentService;
import com.zjg.blog.util.PageInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class ArticleCommentServiceImpl implements ArticleCommentService {
    @Autowired
    private ArticleCommentMapper articleCommentMapper;
    @Autowired
    private ArticleInfoMapper articleInfoMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public int addArticleComment(ArticleComment articleComment) {
        return articleCommentMapper.insert(articleComment);
    }

    @Override
    public long countNumOfAfterDate(Date date) {
        ArticleCommentExample articleCommentExample=new ArticleCommentExample();
        articleCommentExample.createCriteria()
                .andCreateByGreaterThan(date);
        return articleCommentMapper.countByExample(articleCommentExample);
    }

    @Override
    public long countNumOfAbleByArticleId(long id) {
        ArticleCommentExample articleCommentExample=new ArticleCommentExample();
        articleCommentExample.createCriteria().andEffectiveEqualTo(true)
                .andArticleIdEqualTo(id)
                .andTargetIdEqualTo(0L);
        return articleCommentMapper.countByExample(articleCommentExample);
    }

    @Override
    public int disableArticleComment(long id) {
        ArticleComment articleComment=articleCommentMapper.selectByPrimaryKey(id);
        articleComment.setEffective(false);
        return articleCommentMapper.updateByPrimaryKey(articleComment);
    }

    @Override
    public int enableArticleComment(long id) {
        ArticleComment articleComment=articleCommentMapper.selectByPrimaryKey(id);
        articleComment.setEffective(true);
        articleCommentMapper.updateByPrimaryKey(articleComment);
        return 1;
    }


    @Override
    public int deleteArticleComment(long id) {
        ArticleCommentExample articleCommentExample=new ArticleCommentExample();
        articleCommentExample.createCriteria().andTargetIdEqualTo(id);
        articleCommentMapper.deleteByExample(articleCommentExample);
        return articleCommentMapper.deleteByPrimaryKey(id);
    }

    @Override
    /**
     * 查出所有评论，组装文章名，组装用户名称
     */
    public PageInfo queryArticleComments( int pageNum,int pageSize) {
        ArticleCommentExample articleCommentExample=new ArticleCommentExample();
        articleCommentExample.setOrderByClause("create_by desc");
        PageHelper.startPage(pageNum,pageSize);
        List<ArticleComment> daoList=articleCommentMapper.selectByExample(articleCommentExample);
        PageInfo<ArticleComment> daoPageInfo=new PageInfo<>(daoList);
        List<ArticleCommentAdminDto> dtoList=new ArrayList<>();
        for(ArticleComment daoItem:daoList){
            ArticleCommentAdminDto dtoItem=new ArticleCommentAdminDto();
            BeanUtil.copyProperties(daoItem,dtoItem);
            dtoItem.setArticleTitle(articleInfoMapper.selectByPrimaryKey(daoItem.getArticleId()).getTitle());

            UserInfo userInfo=userInfoMapper.selectByPrimaryKey(daoItem.getUserId());
            dtoItem.setSource(userInfo.getSource());
            dtoItem.setUsername(userInfo.getUsername());
            dtoList.add(dtoItem);
        }
        PageInfo<ArticleCommentAdminDto> dtoPageInfo=new PageInfo<>(dtoList);
        PageInfoUtil.copyPageInfo(daoPageInfo,dtoPageInfo);
        return dtoPageInfo;
}

    @Override
    /**
     * 先找出一级评论，然后封装，再找与之对应的二级评论，填入对应的一级评论
     */
    public PageInfo queryEnableArticleComment(long id, int pageNum,int pageSize) {
        ArticleCommentExample articleCommentExample = new ArticleCommentExample();
        articleCommentExample.setOrderByClause("create_by desc");
        articleCommentExample.createCriteria().andEffectiveEqualTo(true)
                .andArticleIdEqualTo(id)
                .andTargetIdEqualTo(0L);
        PageHelper.startPage(pageNum,pageSize);
        List<ArticleComment> daoList=articleCommentMapper.selectByExample(articleCommentExample);
        PageInfo<ArticleComment> daoPageInfo=new PageInfo<>(daoList);
        List<ArticleCommentForeDto> dtoList=new ArrayList<>();
        for(ArticleComment daoItem:daoList){
            ArticleCommentForeDto dtoItem=new ArticleCommentForeDto();
            assembleDtoInfo(daoItem,dtoItem);

            ArticleCommentExample childExample=new ArticleCommentExample();
            childExample.createCriteria().andTargetIdEqualTo(dtoItem.getId())
                    .andEffectiveEqualTo(true);
            List<ArticleComment> childDaoList=articleCommentMapper.selectByExample(childExample);
            List<ArticleCommentForeDto> childDtoList=new ArrayList<>();
            for(ArticleComment childDaoItem:childDaoList){
                ArticleCommentForeDto childDtoItem=new ArticleCommentForeDto();

                assembleDtoInfo(childDaoItem,childDtoItem);
                childDtoList.add(childDtoItem);
            }
            dtoItem.setReplyList(childDtoList);
            dtoList.add(dtoItem);
        }
        PageInfo<ArticleCommentForeDto> dtoPageInfo=new PageInfo<>(dtoList);
        PageInfoUtil.copyPageInfo(daoPageInfo,dtoPageInfo);
        return dtoPageInfo;
    }

    @Override
    public List<LatestArticleCommentDto> queryLatestArticleComment(int size) {
        ArticleCommentExample articleCommentExample=new ArticleCommentExample();
        articleCommentExample.setOrderByClause("create_by desc");
        PageHelper.startPage(1,size);
        List<ArticleComment> daoList=articleCommentMapper.selectByExample(articleCommentExample);
        List<LatestArticleCommentDto> dtoList=new ArrayList<>();
        for(ArticleComment daoItem:daoList){
            LatestArticleCommentDto dtoItem=new LatestArticleCommentDto();
            BeanUtil.copyProperties(daoItem,dtoItem);
            ArticleInfo articleInfo=articleInfoMapper.selectByPrimaryKey(daoItem.getArticleId());
            BeanUtil.copyProperties(articleInfo,dtoItem,"createBy");
            UserInfo userInfo=userInfoMapper.selectByPrimaryKey(daoItem.getUserId());
            BeanUtil.copyProperties(userInfo,dtoItem,"createBy");
            dtoList.add(dtoItem);
        }
        return dtoList;
    }

    private void assembleDtoInfo(ArticleComment daoItem,Object dtoItem){
        BeanUtil.copyProperties(daoItem,dtoItem);

        UserInfo userInfo=userInfoMapper.selectByPrimaryKey(daoItem.getUserId());
        BeanUtil.copyProperties(userInfo,dtoItem,"createBy","id");
    }
}
