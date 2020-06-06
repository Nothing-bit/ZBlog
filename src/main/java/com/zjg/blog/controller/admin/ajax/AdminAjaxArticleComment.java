package com.zjg.blog.controller.admin.ajax;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.controller.BaseController;
import com.zjg.blog.entity.ArticleComment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Api(description = "AdminAjaxArticleComment")
@RestController
@RequestMapping(value = "/admin")
public class AdminAjaxArticleComment extends BaseController {
    /**
     * 文章评论信息
     */
    @Value("${admin.userInfo.id}")
    long adminUserInfoId;
    /**
     * 优化评论逻辑
     * 前台填充 targetUserId,parentId,content,articleId
     * update 2020年4月4日13:56:54
     * author zjg
     */
    @ApiOperation("回复评论（后台）")
    @PostMapping(value = "/article/comment/reply")
    public String postArticleComment(@RequestBody ArticleComment articleComment, HttpServletRequest request){
        JSONObject result=new JSONObject();
        articleComment.setEffective(true);
        articleComment.setUserId(adminUserInfoId);
        articleComment.setInform(false);
        articleComment.setCreateBy(new Date());
        if(articleCommentService.addArticleComment(articleComment)==1){
            //逐个通知
            List<String> emailList=articleCommentService.queryInformList(articleComment.getParentId());
            for(String item:emailList){
                informService.mail(item,"ZhouJianGuo| 个人Blog 【评论】新回复通知", "回复内容："+articleComment.getContent()
                        +"\n你也可以点击 https://www.zhoujianguo.ltd 进行查看"
                        +"\n如非本人，请忽视此条邮件。"
                        +"\n回复时间：" + DateUtil.format(new Date(),"yyyy年MM月dd日 HH:mm:ss"));
            }
        }
        result.put("code",200);

        return result.toJSONString();
    }
    @ApiOperation("删除文章评论（后台）")
    @DeleteMapping(value = "/article/comment/{id}")
    public String deleteCommentArticle(@PathVariable long  id){
        articleCommentService.deleteArticleComment(id);
        JSONObject result=new JSONObject();
        result.put("code",200);
        return result.toJSONString();
    }
    @ApiOperation("审核通过文章评论(后台)")
    @PutMapping(value = "/article/comment/{id}")
    public String ableCommentArticle(@PathVariable long  id){
        articleCommentService.enableArticleComment(id);
        JSONObject result=new JSONObject();
        result.put("code",200);
        return result.toJSONString();
    }
    @ApiOperation("使某条评论失效（后台）")
    @PutMapping(value="/article/comment/disable/{id}")
    public String disableCommentArticle(@PathVariable long  id){
        articleCommentService.disableArticleComment(id);
        JSONObject result=new JSONObject();
        result.put("code",200);
        return result.toJSONString();
    }
    @ApiOperation("获取文章评论列表（后台 dataTable结合）")
    @GetMapping(value = "/article/comment/list")
    public String queryArticleComment(@RequestParam(value = "draw")int draw,@RequestParam(value = "start")long start,
                                      @RequestParam(value = "length")int pageSize,@RequestParam(value = "search[value]")String searchValue,
                                      @RequestParam(value = "order[0][column]")int column,@RequestParam(value = "order[0][dir]")String orderDirection){
        String orderProperty=null;//排序属性
        switch (column){//dataTable列号与表字段映射，详情请查看插件官网 http://datatables.club/manual/server-side.html
            case 1:orderProperty="id";break;
            case 2:orderProperty="parent_id";break;
            case 3:orderProperty="article_id";break;
            case 4:orderProperty="user_id";break;
            case 5:orderProperty="target_user_id";break;
            case 6:orderProperty="content";break;
            case 7:orderProperty="is_effective";break;
            default:orderProperty="create_by";break;
        }
        JSONObject result=new JSONObject();
        int pageNum=(int)start/pageSize+1;
        PageInfo pageInfo=articleCommentService.queryArticleComments(pageNum,pageSize,searchValue,orderProperty,orderDirection);
        result.put("draw",draw);
        result.put("recordsTotal",pageInfo.getTotal());
        result.put("recordsFiltered",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        return result.toJSONString();
    }
}
