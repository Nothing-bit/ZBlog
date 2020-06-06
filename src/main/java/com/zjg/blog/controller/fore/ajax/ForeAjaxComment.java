package com.zjg.blog.controller.fore.ajax;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjg.blog.controller.BaseController;
import com.zjg.blog.entity.Comment;
import com.zjg.blog.entity.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@Api(description = "ForeAjaxComment")
@RequestMapping(value = "/fore")
public class ForeAjaxComment extends BaseController {
    /**
     * Comment
     */
    @ApiOperation("增加一条留言")
    @PostMapping("/comment")
    public String postComment(@RequestBody Comment comment , HttpServletRequest request){
        JSONObject result=new JSONObject();
        UserInfo userInfo=(UserInfo)request.getSession().getAttribute("userInfo");
        if(userInfo!=null){
            comment.setEffective(false);
            comment.setUserId(userInfo.getId());
            comment.setCreateBy(new Date());
            comment.setParentId(0L);
            comment.setTargetUserId(0L);
            commentService.addComment(comment);
            String mailAddress=sysSettingService.querySettingByName("mailAddress");
            informService.mail(mailAddress,"Blog 新【留言】通知","用户名："+userInfo.getUsername()
                    +"\n留言内容："+comment.getContent()
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
     * 二级留言功能
     * @param comment 前台需填充 parentId,content,inform,targetUserId,email
     * create 2020年4月3日17:38:30
     * author zjg
     */
    @ApiOperation("回复某条留言(二级留言)")
    @PostMapping("/comment/reply")
    public String postCommentReply(@RequestBody Comment comment, HttpServletRequest request){
        JSONObject result=new JSONObject();
        UserInfo userInfo=(UserInfo)request.getSession().getAttribute("userInfo");
        if(userInfo!=null){//判断是否已登录
            comment.setEffective(false);
            comment.setCreateBy(new Date());
            comment.setUserId(userInfo.getId());
            commentService.addComment(comment);
            String mailAddress=sysSettingService.querySettingByName("mailAddress");
            informService.mail(mailAddress,"Blog 新【留言二级回复】通知","用户名："+userInfo.getUsername()
                    +"\n留言内容："+comment.getContent()
                    +"\n平台："+userInfo.getSource()
                    +"\n回复时间："+ DateUtil.format(new Date(),"yyyy年MM月dd日 HH:mm:ss")
            );
            result.put("code",200);
        }else{
            result.put("code",201);//未登录状态
        }
        return result.toJSONString();
    }
    @ApiOperation("获取留言列表")
    @GetMapping("/comment/list/{pageNum}")
    public String getCommentList(@PathVariable(name="pageNum") int pageNum){
        JSONObject result=new JSONObject();
        result.put("code",200);
        result.put("pageInfo",commentService.queryEnableComment(pageNum,5));
        return result.toJSONString();
    }
}
