package com.zjg.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.dao.CommentMapper;
import com.zjg.blog.dao.UserInfoMapper;
import com.zjg.blog.dto.admin.AdminComment;
import com.zjg.blog.dto.fore.ForeComment;
import com.zjg.blog.dto.fore.ForeCommentReply;
import com.zjg.blog.dto.fore.LatestComment;
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
        commentExample.createCriteria().andParentIdEqualTo(id);
        commentMapper.deleteByExample(commentExample);
        return commentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int disableCommentById(long id) {
        Comment comment=commentMapper.selectByPrimaryKey(id);
        comment.setEffective(false);
        return commentMapper.updateByPrimaryKey(comment);
    }

    /**
     * 二级留言逻辑update
     * update 2020年4月3日19:48:40
     * author zjg
     */
    @Override
    public PageInfo<AdminComment> queryComments(int pageNum,int pageSize,String searchValue,String orderProperty,String orderDirection) {
        CommentExample commentExample=new CommentExample();
        commentExample.setOrderByClause(orderProperty+" "+orderDirection);
        commentExample.createCriteria()
                .andContentLike("%"+searchValue+"%");
        PageHelper.startPage(pageNum,pageSize);
        List<Comment> daoList=commentMapper.selectByExample(commentExample);
        PageInfo<Comment> daoPageInfo=new PageInfo<>(daoList);
        List<AdminComment> dtoList=new ArrayList<>();
        for(Comment daoItem:daoList){
            AdminComment dtoItem=new AdminComment();
            if(daoItem.getParentId()!=0){
                dtoItem.setTargetUsername(userInfoMapper.selectByPrimaryKey(daoItem.getTargetUserId()).getUsername());
            }else{
                dtoItem.setTargetUsername("");
            }
            assembleUserInfo(daoItem,dtoItem);
            dtoList.add(dtoItem);
        }
        PageInfo<AdminComment> dtoPageInfo=new PageInfo<>(dtoList);
        PageInfoUtil.copyPageInfo(daoPageInfo,dtoPageInfo);
        return dtoPageInfo;
    }

    /**
     *根据parentid通知
     * create 2020年4月4日10:50:53
     * author zjg
     */
    @Override
    public List<String> queryInformList(long parentId) {
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(parentId);
        List<Comment> commentList=commentMapper.selectByExample(commentExample);
        //parent记录也需要加入
        if(parentId!=0L){
            commentList.add(commentMapper.selectByPrimaryKey(parentId));
        }
        List<String> emailList=new ArrayList<>();
        for(Comment item:commentList){
            if(item.getInform()){
                emailList.add(item.getEmail());
            }
        }
        return emailList;
    }

    @Override
    public int addComment(Comment comment) {
        return commentMapper.insert(comment);
    }

    @Override
    public long countNumOfEnableAndParent() {
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria()
                .andEffectiveEqualTo(true)
                .andParentIdEqualTo(0L);
        return commentMapper.countByExample(commentExample);
    }

    /**
     * 更新前台展示逻辑
     * update 2020年4月3日12:05:31
     * author zjg
     */
    @Override
    public PageInfo<ForeComment> queryEnableComment(int pageNum, int pageSize) {
        CommentExample commentExample=new CommentExample();
        commentExample.setOrderByClause("create_by desc");
        commentExample.createCriteria()
                .andParentIdEqualTo(0L)
                .andEffectiveEqualTo(true);
        PageHelper.startPage(pageNum,pageSize);
        List<Comment> commentDaoList=commentMapper.selectByExample(commentExample);
        PageInfo<Comment> daoPageInfo=new PageInfo<>(commentDaoList);
        List<ForeComment> dtoList=new ArrayList<>();
        //获取一级留言
        for(Comment commentDaoItem:commentDaoList){
            ForeComment foreCommentDtoItem=new ForeComment();
            assembleUserInfo(commentDaoItem,foreCommentDtoItem);
            //获取二级回复
            CommentExample replyExample=new CommentExample();
            replyExample.createCriteria().andParentIdEqualTo(foreCommentDtoItem.getId())
                    .andEffectiveEqualTo(true);
            List<Comment> replyDaoList=commentMapper.selectByExample(replyExample);
            List<ForeCommentReply> foreCommentReplyDtoList=new ArrayList<>();
            for(Comment replyDaoItem:replyDaoList){
                ForeCommentReply foreCommentReplyDtoItem=new ForeCommentReply();
                assembleUserInfo(replyDaoItem,foreCommentReplyDtoItem);
                UserInfo targetUserInfo=userInfoMapper.selectByPrimaryKey(replyDaoItem.getTargetUserId());
                foreCommentReplyDtoItem.setTargetUsername(targetUserInfo.getUsername());
                foreCommentReplyDtoItem.setUserId(replyDaoItem.getUserId());
                foreCommentReplyDtoList.add(foreCommentReplyDtoItem);
            }
            foreCommentDtoItem.setReplyList(foreCommentReplyDtoList);
            dtoList.add(foreCommentDtoItem);
        }
        PageInfo<ForeComment> dtoPageInfo=new PageInfo<>(dtoList);
        PageInfoUtil.copyPageInfo(daoPageInfo,dtoPageInfo);
        return dtoPageInfo;
    }

    @Override
    public List<LatestComment> queryLatestCommentList(int size) {
        CommentExample commentExample=new CommentExample();
        PageHelper.startPage(1,size);
        commentExample.setOrderByClause("create_by desc");
        commentExample.createCriteria()
                .andEffectiveEqualTo(true);
        commentExample.createCriteria().andEffectiveEqualTo(true);
        List<Comment> daoList=commentMapper.selectByExample(commentExample);
        List<LatestComment> dtoList=new ArrayList<>();
        for(Comment daoItem:daoList){
            LatestComment dtoItem=new LatestComment();
            BeanUtil.copyProperties(daoItem,dtoItem);
            UserInfo userInfo=userInfoMapper.selectByPrimaryKey(daoItem.getUserId());
            BeanUtil.copyProperties(userInfo,dtoItem,"createBy");
            dtoList.add(dtoItem);
        }
        return dtoList;
    }

    /**
     * 为dto传输对象装填user_info
     * update 2020年4月3日13:37:30
     * author zjg
     */
    private void assembleUserInfo(Comment daoItem, Object dtoItem){
        BeanUtil.copyProperties(daoItem,dtoItem);

        UserInfo userInfo=userInfoMapper.selectByPrimaryKey(daoItem.getUserId());
        BeanUtil.copyProperties(userInfo,dtoItem,"createBy","id");
    }
}
