package com.zjg.blog.service;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.dto.admin.AdminComment;
import com.zjg.blog.dto.fore.ForeComment;
import com.zjg.blog.dto.fore.LatestComment;
import com.zjg.blog.entity.Comment;

import java.util.Date;
import java.util.List;

/**
 * create 2019年9月16日19:07:12
 * author zjg
 */
public interface CommentService  {
    /**
     * admin
     */
    int enableCommentById(long id);//审核通过留言
    long countNumOfAfterDate(Date date);//获取指定日期后新增的留言条数
    int deleteCommentById(long id);//删除一条留言
    int disableCommentById(long id);//使某一条留言失效
    PageInfo<AdminComment> queryComments(int pageNum, int pageSize,String searchValue,String orderProperty,String orderDirection);//获取留言
    List<String> queryInformList(long parentId);//查询需要联系的邮箱list

    /**
     * fore
     */
    int addComment(Comment comment);//添加一条留言
    long countNumOfEnableAndParent();//
    PageInfo<ForeComment> queryEnableComment(int pageNum, int pageSize);//根据页码获取评论

    List<LatestComment> queryLatestCommentList(int size);//获取最新的留言记录
}
