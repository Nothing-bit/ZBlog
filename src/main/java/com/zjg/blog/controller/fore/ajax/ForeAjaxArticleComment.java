package com.zjg.blog.controller.fore.ajax;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.zjg.blog.controller.BaseController;
import com.zjg.blog.entity.ArticleComment;
import com.zjg.blog.entity.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@Api(description = "ForeAjaxArticleComment")
@RequestMapping(value = "/fore")
public class ForeAjaxArticleComment extends BaseController {
    /**
     * ArticleComment
     */
    /**
     * 优化评论
     * 前台articleComment所需填充：articleId,content,inform,email
     * update 2020年4月4日13:45:01
     * author zjg
     */
    @ApiOperation("给某一篇文章添加评论")
    @PostMapping("/article/comment")
    public String postCommentArticle(@RequestBody ArticleComment articleComment, HttpServletRequest request){
        JSONObject result=new JSONObject();
        UserInfo userInfo=(UserInfo)request.getSession().getAttribute("userInfo");
        if(userInfo!=null){
            articleComment.setParentId(0L);//一级评论
            articleComment.setTargetUserId(0L);
            articleComment.setEffective(false);
            articleComment.setUserId(userInfo.getId());
            articleComment.setCreateBy(new Date());
            articleCommentService.addArticleComment(articleComment);
            String mailAddress=sysSettingService.querySettingByName("mailAddress");
            informService.mail(mailAddress,"Blog 新【评论】通知","用户名："+userInfo.getUsername()
                    +"\n留言内容："+articleComment.getContent()
                    +"\n平台："+userInfo.getSource()
                    +"\n回复时间："+ DateUtil.format(new Date(),"yyyy年MM月dd日 HH:mm:ss")
            );
            result.put("code",200);
        }else{
            result.put("code",201);
        }

        return result.toJSONString();
    }

    /**
     * 评论回复
     * articleComment前台需填充：articleId,targetUserId,parentId,content,inform,email
     * create 2020年4月4日13:50:55
     * author zjg
     */
    @ApiOperation("给某一篇文章的评论回复")
    @PostMapping("/article/comment/reply")
    public String postReplyArticleComment(@RequestBody ArticleComment articleComment, HttpSession session){
        JSONObject result=new JSONObject();
        UserInfo userInfo=(UserInfo)session.getAttribute("userInfo");
        if(userInfo!=null){
            articleComment.setEffective(false);
            articleComment.setUserId(userInfo.getId());
            articleComment.setCreateBy(new Date());
            articleCommentService.addArticleComment(articleComment);
            String mailAddress=sysSettingService.querySettingByName("mailAddress");
            informService.mail(mailAddress,"Blog 新【评论】通知","用户名："+userInfo.getUsername()
                    +"\n留言内容："+articleComment.getContent()
                    +"\n平台："+userInfo.getSource()
                    +"\n回复时间："+ DateUtil.format(new Date(),"yyyy年MM月dd日 HH:mm:ss")
            );
            result.put("code",200);
        }else{
            result.put("code",201);
        }
        return result.toJSONString();
    }
    @ApiOperation("获取指定页数的文章评论")
    @GetMapping("/article/comment/list/{id}/{pageNum}")
    public String getCommentArticle(@PathVariable(name = "id") long id, @PathVariable(name="pageNum") int pageNum){
        JSONObject result=new JSONObject();
        result.put("code",200);
        PageHelper.orderBy("create_by desc");
        result.put("pageInfo",articleCommentService.queryEnableArticleComment(id,pageNum,5));
        return result.toJSONString();
    }
}
