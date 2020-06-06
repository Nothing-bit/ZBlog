package com.zjg.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.dao.ArticleCommentMapper;
import com.zjg.blog.dao.ArticleInfoMapper;
import com.zjg.blog.dao.UserInfoMapper;
import com.zjg.blog.dto.admin.AdminArticleComment;
import com.zjg.blog.dto.fore.ForeArticleComment;
import com.zjg.blog.dto.fore.ForeArticleCommentReply;
import com.zjg.blog.dto.fore.LatestArticleComment;
import com.zjg.blog.entity.*;
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

    /**
     * 只统计一级评论数量
     * @param id articleId
     * update 2020年4月4日14:25:44
     * author zjg
     */

    @Override
    public long countNumOfAbleByArticleId(long id) {
        ArticleCommentExample articleCommentExample=new ArticleCommentExample();
        articleCommentExample.createCriteria().andEffectiveEqualTo(true)
                .andArticleIdEqualTo(id)
                .andParentIdEqualTo(0L);
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
        articleCommentExample.createCriteria().andParentIdEqualTo(id);
        articleCommentMapper.deleteByExample(articleCommentExample);
        return articleCommentMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询通知所有邮件地址
     * create 2020年4月4日15:27:12
     * author zjg
     */
    @Override
    public List<String> queryInformList(long parentId) {
        ArticleCommentExample articleCommentExample=new ArticleCommentExample();
        articleCommentExample.createCriteria()
                .andParentIdEqualTo(parentId);
        List<ArticleComment> articleCommentList=articleCommentMapper.selectByExample(articleCommentExample);
        //parent记录也需要加入
        if(parentId!=0L){
            articleCommentList.add(articleCommentMapper.selectByPrimaryKey(parentId));
        }
        List<String> emailList=new ArrayList<>();
        for(ArticleComment item:articleCommentList){
            if(item.getInform()){
                emailList.add(item.getEmail());
            }
        }
        return emailList;
    }

    @Override
    /**
     * 查出所有评论，组装文章名，组装用户名称
     */
    public PageInfo<AdminArticleComment> queryArticleComments( int pageNum,int pageSize,String searchValue,String orderProperty,String orderDirection) {
        ArticleCommentExample articleCommentExample=new ArticleCommentExample();
        articleCommentExample.setOrderByClause(orderProperty+" "+orderDirection);
        articleCommentExample.createCriteria()
                .andContentLike("%"+searchValue+"%");
        PageHelper.startPage(pageNum,pageSize);
        List<ArticleComment> daoList=articleCommentMapper.selectByExample(articleCommentExample);
        PageInfo<ArticleComment> daoPageInfo=new PageInfo<>(daoList);
        List<AdminArticleComment> dtoList=new ArrayList<>();
        for(ArticleComment daoItem:daoList){
            AdminArticleComment dtoItem=new AdminArticleComment();
            BeanUtil.copyProperties(daoItem,dtoItem);
            dtoItem.setArticleTitle(articleInfoMapper.selectByPrimaryKey(daoItem.getArticleId()).getTitle());

            UserInfo userInfo=userInfoMapper.selectByPrimaryKey(daoItem.getUserId());
            if(daoItem.getTargetUserId()!=0L){
                UserInfo targetUserInfo=userInfoMapper.selectByPrimaryKey(daoItem.getTargetUserId());
                dtoItem.setTargetUsername(targetUserInfo.getUsername());
            }else{
                dtoItem.setTargetUsername("");
            }
            dtoItem.setSource(userInfo.getSource());
            dtoItem.setUsername(userInfo.getUsername());
            dtoList.add(dtoItem);
        }
        PageInfo<AdminArticleComment> dtoPageInfo=new PageInfo<>(dtoList);
        PageInfoUtil.copyPageInfo(daoPageInfo,dtoPageInfo);
        return dtoPageInfo;
}

    @Override
    /**
     * 先找出一级评论，然后封装，再找与之对应的二级评论，填入对应的一级评论
     * update 2020年4月4日13:33:03
     * author zjg
     */
    public PageInfo<ForeArticleComment> queryEnableArticleComment(long id, int pageNum,int pageSize) {
        //获取一级评论
        ArticleCommentExample articleCommentExample = new ArticleCommentExample();
        articleCommentExample.setOrderByClause("create_by desc");
        articleCommentExample.createCriteria().andEffectiveEqualTo(true)
                .andArticleIdEqualTo(id)
                .andParentIdEqualTo(0L);
        PageHelper.startPage(pageNum,pageSize);
        List<ArticleComment> daoList=articleCommentMapper.selectByExample(articleCommentExample);
        PageInfo<ArticleComment> daoPageInfo=new PageInfo<>(daoList);
        List<ForeArticleComment> dtoList=new ArrayList<>();
        for(ArticleComment daoItem:daoList){
            //获取二级回复
            ForeArticleComment dtoItem=new ForeArticleComment();
            assembleDtoInfo(daoItem,dtoItem);

            ArticleCommentExample childExample=new ArticleCommentExample();
            childExample.createCriteria().andParentIdEqualTo(dtoItem.getId())
                    .andEffectiveEqualTo(true);
            List<ArticleComment> childDaoList=articleCommentMapper.selectByExample(childExample);
            List<ForeArticleCommentReply> childDtoList=new ArrayList<>();
            for(ArticleComment childDaoItem:childDaoList){
                ForeArticleCommentReply childDtoItem=new ForeArticleCommentReply();
                UserInfo replyedUserInfo=userInfoMapper.selectByPrimaryKey(childDaoItem.getTargetUserId());
                if(replyedUserInfo!=null){
                    childDtoItem.setTargetUsername(replyedUserInfo.getUsername());
                }else{
                    childDtoItem.setTargetUsername("unknwon");
                }
                assembleDtoInfo(childDaoItem,childDtoItem);
                childDtoList.add(childDtoItem);
            }
            dtoItem.setReplyList(childDtoList);
            dtoList.add(dtoItem);
        }
        PageInfo<ForeArticleComment> dtoPageInfo=new PageInfo<>(dtoList);
        PageInfoUtil.copyPageInfo(daoPageInfo,dtoPageInfo);
        return dtoPageInfo;
    }

    @Override
    public List<LatestArticleComment> queryLatestArticleComment(int size) {
        ArticleCommentExample articleCommentExample=new ArticleCommentExample();
        articleCommentExample.setOrderByClause("create_by desc");
        articleCommentExample.createCriteria()
                .andEffectiveEqualTo(true);
        PageHelper.startPage(1,size);
        List<ArticleComment> daoList=articleCommentMapper.selectByExample(articleCommentExample);
        List<LatestArticleComment> dtoList=new ArrayList<>();
        for(ArticleComment daoItem:daoList){
            LatestArticleComment dtoItem=new LatestArticleComment();
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
