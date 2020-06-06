package com.zjg.blog.controller.admin.ajax;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.controller.BaseController;
import com.zjg.blog.entity.Comment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(description = "AdminAjaxComment")
@RestController
@RequestMapping(value = "/admin")
public class AdminAjaxComment extends BaseController {
    /**
     * Comment
     */
    @Value("${admin.userInfo.id}")
    long adminUserInfoId;
    @ApiOperation("删除留言")
    @DeleteMapping(value = "/comment/{id}")
    public String deleteComment(@PathVariable(value = "id")long id){
        JSONObject result=new JSONObject();
        if(commentService.deleteCommentById(id)==1){
            result.put("code",200);
        }
        return result.toJSONString();
    }
    @ApiOperation("审核通过某一条留言")
    @PutMapping(value = "/comment/{id}")
    public String enableComment(@PathVariable(value = "id") long id){
        JSONObject result=new JSONObject();
        if(commentService.enableCommentById(id)==1){
            result.put("code",200);
        }
        return result.toJSONString();
    }
    @ApiOperation("使某一条留言失效（后台）")
    @PutMapping(value = "/comment/disable/{id}")
    public String disableComment(@PathVariable(value = "id") long id){
        JSONObject result=new JSONObject();
        if(commentService.disableCommentById(id)==1){
            result.put("code",200);
        }
        return result.toJSONString();
    }
    @ApiOperation("获取留言（后台 dataTable结合）")
    @GetMapping(value = "/comment/list")
    public String queryComment(@RequestParam(value = "draw")int draw,@RequestParam(value = "start")long start,
                               @RequestParam(value = "length")int pageSize,@RequestParam(value = "search[value]")String searchValue,
                               @RequestParam(value = "order[0][column]")int column,@RequestParam(value = "order[0][dir]")String orderDirection){
        String orderProperty=null;//排序属性
        switch (column){//dataTable列号与表字段映射，详情请查看插件官网 http://datatables.club/manual/server-side.html
            case 1:orderProperty="id";break;
            case 2:orderProperty="parent_id";break;
            case 3:orderProperty="user_id";break;
            case 4:orderProperty="target_user_id";break;
            case 5:orderProperty="content";break;
            case 7:orderProperty="is_effective";break;
            default:orderProperty="create_by";break;
        }
        JSONObject result=new JSONObject();
        int pageNum=(int)start/pageSize+1;
        PageInfo pageInfo=commentService.queryComments(pageNum,pageSize,searchValue,orderProperty,orderDirection);
        result.put("draw",draw);
        result.put("recordsTotal",pageInfo.getTotal());
        result.put("recordsFiltered",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        return result.toJSONString();
    }

    /**
     * 回复后，查看所有一级留言以及二级回复，如果有需要回复通知的，则通知
     * comment需要前台填充的参数：targetUserId,parentId,content
     * update 2020年4月4日10:42:50
     * author zjg
     */
    @ApiOperation("回复某一条留言")
    @PostMapping(value = "/comment/reply")
    public String replyComment(@RequestBody Comment comment){
        JSONObject result=new JSONObject();
        comment.setInform(false);
        comment.setEffective(true);
        comment.setCreateBy(new Date());
        comment.setUserId(adminUserInfoId);
        if(commentService.addComment(comment)==1){
            result.put("code",200);
        }
        List<String> emailList=commentService.queryInformList(comment.getParentId());
        for(String item:emailList){
            informService.mail(item,"ZhouJianGuo| 个人Blog 【留言】新回复通知", "回复内容："+comment.getContent()
                    +"\n你也可以点击 https://www.zhoujianguo.ltd 进行查看"
                    +"\n如非本人，请忽视此条邮件。"
                    +"\n回复时间：" + DateUtil.format(new Date(),"yyyy年MM月dd日 HH:mm:ss"));
        }
        return result.toJSONString();
    }
}
