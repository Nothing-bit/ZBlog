package com.zjg.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.dao.CommentMapper;
import com.zjg.blog.dao.UserInfoMapper;
import com.zjg.blog.dto.admin.CommentAdminDto;
import com.zjg.blog.dto.fore.CommentForeDto;
import com.zjg.blog.dto.fore.LatestCommentDto;
import com.zjg.blog.entity.Comment;
import com.zjg.blog.entity.CommentExample;
import com.zjg.blog.entity.UserInfo;
import com.zjg.blog.service.CommentService;
import com.zjg.blog.util.PageInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    public int enableCommentById(long id) {
        Comment comment=commentMapper.selectByPrimaryKey(id);
        comment.setEffective(true);
        commentMapper.updateByPrimaryKey(comment);
        return 1;
    }

    @Override
    public long countNumOfAfterDate(Date date) {
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria()
                .andCreateByGreaterThan(date);
        return commentMapper.countByExample(commentExample);
    }


    @Override
    public int deleteCommentById(long id) {
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria().andTargetIdEqualTo(id);
        commentMapper.deleteByExample(commentExample);
        return commentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int disableCommentById(long id) {
        Comment comment=commentMapper.selectByPrimaryKey(id);
        comment.setEffective(false);
        return commentMapper.updateByPrimaryKey(comment);
    }

    @Override
    public PageInfo queryComments(int pageNum,int pageSize) {
        CommentExample commentExample=new CommentExample();
        commentExample.setOrderByClause("create_by desc");
        PageHelper.startPage(pageNum,pageSize);
        List<Comment> daoList=commentMapper.selectByExample(commentExample);
        PageInfo<Comment> daoPageInfo=new PageInfo<>(daoList);
        List<CommentAdminDto> dtoList=new ArrayList<>();
        for(Comment daoItem:daoList){
            CommentAdminDto dtoItem=new CommentAdminDto();
            assembleDtoInfo(daoItem,dtoItem);
            dtoList.add(dtoItem);
        }
        PageInfo<CommentAdminDto> dtoPageInfo=new PageInfo<>(dtoList);
        PageInfoUtil.copyPageInfo(daoPageInfo,dtoPageInfo);
        return dtoPageInfo;
    }

    @Override
    public int addComment(Comment comment) {
        return commentMapper.insert(comment);
    }

    @Override
    public long countNumOfEnable() {
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria().andEffectiveEqualTo(true);
        return commentMapper.countByExample(commentExample);
    }

    @Override
    /**
     * 获取一级留言列表，获取用户信息，查询子回复
     */
    public PageInfo<CommentForeDto> queryEnableComment(int pageNum, int pageSize) {
        CommentExample commentExample=new CommentExample();
        commentExample.setOrderByClause("create_by desc");
        commentExample.createCriteria().andTargetIdEqualTo(0L)
                .andEffectiveEqualTo(true);
        PageHelper.startPage(pageNum,pageSize);
        List<Comment> commentList=commentMapper.selectByExample(commentExample);
        PageInfo<Comment> daoPageInfo=new PageInfo<>(commentList);
        List<CommentForeDto> dtoList=new ArrayList<>();
        for(Comment daoItem:commentList){
            CommentForeDto dtoItem=new CommentForeDto();
            assembleDtoInfo(daoItem,dtoItem);
            CommentExample childExample=new CommentExample();
            childExample.createCriteria().andTargetIdEqualTo(dtoItem.getId())
                    .andEffectiveEqualTo(true);
            List<Comment> childDaoList=commentMapper.selectByExample(childExample);
            List<CommentForeDto> childDtoList=new ArrayList<>();
            for(Comment childDaoItem:childDaoList){
                CommentForeDto childDtoItem=new CommentForeDto();

                assembleDtoInfo(childDaoItem,childDtoItem);
                childDtoList.add(childDtoItem);
            }
            dtoItem.setReplyList(childDtoList);
            dtoList.add(dtoItem);
        }
        PageInfo<CommentForeDto> dtoPageInfo=new PageInfo<>(dtoList);
        PageInfoUtil.copyPageInfo(daoPageInfo,dtoPageInfo);
        return dtoPageInfo;
    }

    @Override
    public List<LatestCommentDto> queryLatestCommentList(int size) {
        CommentExample commentExample=new CommentExample();
        PageHelper.startPage(1,size);
        commentExample.setOrderByClause("create_by desc");
        commentExample.createCriteria().andEffectiveEqualTo(true);
        List<Comment> daoList=commentMapper.selectByExample(commentExample);
        List<LatestCommentDto> dtoList=new ArrayList<>();
        for(Comment daoItem:daoList){
            LatestCommentDto dtoItem=new LatestCommentDto();
            BeanUtil.copyProperties(daoItem,dtoItem);
            UserInfo userInfo=userInfoMapper.selectByPrimaryKey(daoItem.getUserId());
            BeanUtil.copyProperties(userInfo,dtoItem,"createBy");
            dtoList.add(dtoItem);
        }
        return dtoList;
    }

    private void assembleDtoInfo(Comment daoItem, Object dtoItem){
        BeanUtil.copyProperties(daoItem,dtoItem);

        UserInfo userInfo=userInfoMapper.selectByPrimaryKey(daoItem.getUserId());
        BeanUtil.copyProperties(userInfo,dtoItem,"createBy","id");
    }
}
