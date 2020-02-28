package com.zjg.blog.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.zjg.blog.entity.ArticleComment;
import com.zjg.blog.entity.CategoryInfo;
import com.zjg.blog.entity.Comment;
import com.zjg.blog.entity.UserInfo;
import com.zjg.blog.util.IPUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/fore")
@Api(description = "前台展示接口")
public class ForeAjaxController extends BaseController{
    /**
     * 展示controller
     *
     * 文章
     */
    @ApiOperation(value="获取预览分页")
    @GetMapping(value = "/article/list/{pageNum}")
    public String getArticleListByPageNum(@PathVariable(name="pageNum")int pageNum){
        JSONObject result=new JSONObject();
        result.put("code",200);
        result.put("pageInfo",articleService.queryPublicPreviews(pageNum,5));
        return result.toJSONString();
    }

    @ApiOperation(value = "获取预览分页（分类）")
    @GetMapping(value = "/category/article/list/{categoryId}/{pageNum}")
    public String getArticleCategoryListByPageNum(@RequestParam(value = "isAll")boolean isAll,@PathVariable(name="categoryId")long categoryId,@PathVariable(name="pageNum")int pageNum){
        JSONObject result=new JSONObject();
        if(isAll==true){
            result.put("pageInfo",articleService.queryPublicPreviews(pageNum,5));
        }else{
            result.put("pageInfo",articleService.queryPreviewsByCategory(categoryId,pageNum,5));
        }
        result.put("code",200);
        return result.toJSONString();
    }
    @ApiOperation(value="获取预览分页(归档 format:yyyy年MM月)")
    @GetMapping(value = "/archive/article/list/{pageNum}")
    public String getArchiveArticleListByPageNum(@RequestParam(value = "month") String month,@RequestParam(value = "isAll") boolean isAll,@PathVariable(value = "pageNum")int pageNum){
        JSONObject result=new JSONObject();
        if(isAll==true){
            result.put("pageInfo",articleService.queryPublicPreviews(pageNum,5));
        }else{
            Date date= DateUtil.parse(month,"yyyy年MM月");
            Date beginDate=DateUtil.beginOfMonth(date);
            Date endDate=DateUtil.endOfMonth(date);
            result.put("pageInfo",articleService.queryPreviewsByBeginAndEndDate(beginDate,endDate,pageNum,5));
        }
        result.put("code",200);
        return result.toJSONString();
    }
    /**
     *
     * 分类信息
     */
    @ApiOperation("获取所有分类信息")
    @GetMapping("/category/list")
    public String getCategoryList(){
        JSONObject result=new JSONObject();
        List<CategoryInfo> infoList=categoryInfoService.queryAllCategory();
        result.put("code",200);
        result.put("rows",infoList);
        result.put("total",infoList.size());
        return result.toJSONString();
    }
    /**
     * 标签查找
     */
    @ApiOperation("获取预览分页（标签）")
    @GetMapping("/tag/article/list/{tagName}/{pageNum}")
    public String getTagArticleListByPageNum(@PathVariable(value = "tagName") String tagName,@PathVariable(value = "pageNum") int pageNum){
        JSONObject result=new JSONObject();
        result.put("code",200);
        result.put("pageInfo",articleService.queryPreviewsByTagName(tagName,pageNum,5));
        return result.toJSONString();
    }
    /**
     * 文章评论
     */
    @ApiOperation("给某一篇文章添加评论")
    @PostMapping("/article/comment")
    public String postCommentArticle(@RequestBody ArticleComment articleComment, HttpServletRequest request){
        JSONObject result=new JSONObject();
        String ip = IPUtil.getOriginIP(request);
        articleComment.setIp(ip);
        articleComment.setEffective(false);
        UserInfo userInfo=(UserInfo)request.getSession().getAttribute("userInfo");
        if(userInfo!=null){
            articleComment.setUserId(userInfo.getId());
            articleComment.setCreateBy(new Date());
            articleCommentService.addArticleComment(articleComment);
            String mailAddress=sysSettingService.querySettingByName("mailAddress");
            sysLogService.informByMail(mailAddress,"Blog 新【评论】通知","用户名："+userInfo.getUsername()
                    +"\n留言内容："+articleComment.getContent()
                    +"\n平台："+userInfo.getSource()
                    +"\n回复时间："+DateUtil.format(new Date(),"yyyy年MM月dd日 HH:mm:ss")
            );
            result.put("code",200);
        }else{
            result.put("code",201);
        }

        return result.toJSONString();
    }

    @ApiOperation("获取指定页数的文章评论")
    @GetMapping("/article/comment/list/{id}/{pageNum}")
    public String getCommentArticle(@PathVariable(name = "id") long id,@PathVariable(name="pageNum") int pageNum){
        JSONObject result=new JSONObject();
        result.put("code",200);
        PageHelper.orderBy("create_by desc");
        result.put("pageInfo",articleCommentService.queryEnableArticleComment(id,pageNum,5));
        return result.toJSONString();
    }

    /**
     * 留言
     * @return
     */
    @ApiOperation("增加一条留言")
    @PostMapping("/comment")
    public String postComment(@RequestBody Comment comment , HttpServletRequest request){
        JSONObject result=new JSONObject();
        String ip = IPUtil.getOriginIP(request);
        comment.setIp(ip);
        comment.setEffective(false);
        UserInfo userInfo=(UserInfo)request.getSession().getAttribute("userInfo");
        if(userInfo!=null){
            comment.setUserId(userInfo.getId());
            comment.setCreateBy(new Date());
            commentService.addComment(comment);
            String mailAddress=sysSettingService.querySettingByName("mailAddress");
            sysLogService.informByMail(mailAddress,"Blog 新【留言】通知","用户名："+userInfo.getUsername()
                            +"\n留言内容："+comment.getContent()
                            +"\n平台："+userInfo.getSource()
                            +"\n回复时间："+DateUtil.format(new Date(),"yyyy年MM月dd日 HH:mm:ss")
                    );
            result.put("code",200);
        }else{
            result.put("code",201);
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

    /**
     * 系统设置项获取
     * @return
     */
    @ApiOperation("获取公告")
    @GetMapping("/notice")
    public String getNotice(){
        JSONObject result=new JSONObject();
        result.put("code",200);
        result.put("data",sysSettingService.querySettingByName("notice"));
        return result.toJSONString();
    }
}
