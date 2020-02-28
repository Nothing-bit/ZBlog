package com.zjg.blog.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.dto.ArticleViewDto;
import com.zjg.blog.entity.*;
import com.zjg.blog.util.IPUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Api(description = "后台管理ajax接口")
@RestController
@RequestMapping(value = "/admin")
public class AdminAjaxController extends BaseController {
    @Value("${admin.username}")
    String username;
    @Value("${admin.password}")
    String password;
    /**
     * admin ajax controller
     */
    /**
     * 后台登录模块
     */
    @ApiOperation(value = "登录后台系统")
    @PostMapping(value = "/login")
    public String login(@RequestBody AdminUser adminUser, HttpSession session){
        JSONObject result=new JSONObject();
        int failCount=0;
        if(session.getAttribute("failCount")==null){
            session.setAttribute("failCount",failCount);
        }else{
            failCount=(int)session.getAttribute("failCount");
        }
        if(adminUser!=null&&failCount<5){
            if(adminUser.getUsername().equals(username)&&adminUser.getPassword().equals(password)){
                session.setAttribute("adminUser",adminUser);
                Date latestLoginTime=DateUtil.parse(sysSettingService.querySettingByName("latestLoginTime"),"yyyy-MM-dd HH:mm:ss");
                session.setAttribute("latestLoginTime",latestLoginTime);
                sysSettingService.updateSettingByName("latestLoginTime",DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
                result.put("code",200);
            }else{
                failCount++;
                session.setAttribute("failCount",failCount);
                result.put("code",203);
                result.put("chance",5-failCount+1);
            }
        }else if(failCount>=5){
            result.put("code",204);
        }else{
            result.put("code",205);
        }
        return result.toJSONString();
    }
    @ApiOperation(value = "退出后台系统")
    @DeleteMapping(value = "/logout")
    public String logout(HttpSession session){
        JSONObject result=new JSONObject();
        if(session.getAttribute("adminUser")!=null){
            session.removeAttribute("adminUser");
        }
        return result.toJSONString();
    }
    /**
     * 文章
     */
    @ApiOperation(value = "获取单个全部文章信息")
    @GetMapping(value = "/article/{id}")
    @ApiImplicitParam(paramType = "path",name="id",value = "article Id",required = true,dataType = "Long")
    public String getArticle(@PathVariable Long id){

        JSONObject result=new JSONObject();
        ArticleViewDto view=articleService.getOneById(id);
        result.put("code",200);
        result.put("article",view);
        return result.toJSONString();
    }

    @ApiOperation(value = "修改一篇文章")
    @ApiImplicitParam(paramType = "path",name="id",value = "文章id",required = true,dataType = "Long")
    @PutMapping(value = "/article/{id}")
    public String putArticle(@PathVariable long id,@RequestBody ArticleViewDto dto){
        JSONObject result=new JSONObject();
        dto.setId(id);
        if(articleService.updateArticle(dto)==1){
            result.put("code",200);
        }else {
            result.put("code",100);
        }
        return result.toJSONString();
    }

    @ApiOperation("删除一篇文章")
    @ApiImplicitParam(name="id",value = "文章id",required = true,dataType = "long",paramType = "path")
    @DeleteMapping(value = "/article/{id}")
    public String deleteArticle(@PathVariable long id){
        JSONObject result=new JSONObject();
        if(articleService.deleteArticle(id)==1){
            result.put("code",200);
        }else{
            result.put("code",100);
        }
        return result.toJSONString();
    }

    @ApiOperation("新增一篇文章")
    @PostMapping(value = "/article")
    public String postArticle(@RequestBody ArticleViewDto dto){
        JSONObject result=new JSONObject();
        if(articleService.insertArticle(dto)==1){
            result.put("code",200);
        }else{
            result.put("code",100);
        }
        return result.toJSONString();
    }
    @ApiOperation("获取文章（后台 dataTable结合）")
    @GetMapping(value = "/article/list")
    public String getArticleList(@RequestParam(value = "draw")int draw,@RequestParam(value = "start")long start,@RequestParam(value = "length")int pageSize){
        JSONObject result=new JSONObject();
        int pageNum=(int)start/pageSize+1;
        PageInfo pageInfo=articleService.queryPreviews(pageNum,pageSize);
        result.put("draw",draw);
        result.put("recordsTotal",pageInfo.getTotal());
        result.put("recordsFiltered",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        return result.toJSONString();
    }
    /**
     * 图片上传
     */
    @ApiOperation("上传图片")
    @PostMapping(value = "/upload/images")
    public String postPicture(@RequestParam("upload")MultipartFile file){
        JSONObject result=new JSONObject();
        result.put("code",200);
        result.put("uploaded",1);
        result.put("filename","test");
        result.put("url",pictureService.uploadPicture(file));
        return result.toJSONString();
    }
    /**
     * 文章分类管理
     */
    @ApiOperation("增加文章分类")
    @PostMapping(value = "/category")
    public String postCategory(@RequestBody CategoryInfo categoryInfo){
        JSONObject result=new JSONObject();
        if(categoryInfoService.addCategory(categoryInfo)==1){
            result.put("code",200);
        }
        return result.toJSONString();
    }
    @ApiOperation("更新分类信息")
    @PutMapping(value = "/category/{id}")
    public String putCategory(@RequestBody CategoryInfo categoryInfo,@PathVariable long id){
        JSONObject result=new JSONObject();
        categoryInfo.setId(id);
        if(categoryInfoService.updateCategory(categoryInfo)==1){
            result.put("code",200);
        }
        return result.toJSONString();
    }
    @ApiOperation("删除分类信息")
    @DeleteMapping(value="/category/{id}")
    public String deleteCategory(@PathVariable long id){
        JSONObject result=new JSONObject();
        if(categoryInfoService.deleteCategoryById(id)==1){
            result.put("code",200);
        }
        return result.toJSONString();
    }
    @ApiOperation("获取分类信息 (后台 dataTable结合）")
    @GetMapping(value = "/category/list")
    public String getCategoryList(@RequestParam(value = "draw")int draw,@RequestParam(value = "start")long start,@RequestParam(value = "length")int pageSize){
        JSONObject result=new JSONObject();
        int pageNum=(int)start/pageSize+1;
        PageInfo pageInfo=categoryInfoService.queryCategories(pageNum,pageSize);
        result.put("draw",draw);
        result.put("recordsTotal",pageInfo.getTotal());
        result.put("recordsFiltered",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        return result.toJSONString();
    }
    /**
     * 文章评论信息
     */
    @ApiOperation("回复评论（后台）")
    @PostMapping(value = "/article/comment")
    public String postArticleComment(@RequestBody ArticleComment articleComment,HttpServletRequest request){
        JSONObject result=new JSONObject();
        String ip = IPUtil.getOriginIP(request);
        articleComment.setIp(ip);
        articleComment.setEffective(true);
        articleComment.setUserId(2L);
        articleComment.setCreateBy(new Date());
        articleCommentService.addArticleComment(articleComment);
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
    public String queryArticleComment(@RequestParam(value = "draw")int draw,@RequestParam(value = "start")long start,@RequestParam(value = "length")int pageSize){
        JSONObject result=new JSONObject();
        int pageNum=(int)start/pageSize+1;
        PageInfo pageInfo=articleCommentService.queryArticleComments(pageNum,pageSize);
        result.put("draw",draw);
        result.put("recordsTotal",pageInfo.getTotal());
        result.put("recordsFiltered",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        return result.toJSONString();
    }

    /**
     * 留言
     */
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
    public String queryComment(@RequestParam(value = "draw")int draw,@RequestParam(value = "start")long start,@RequestParam(value = "length")int pageSize){
        JSONObject result=new JSONObject();
        int pageNum=(int)start/pageSize+1;
        PageInfo pageInfo=commentService.queryComments(pageNum,pageSize);
        result.put("draw",draw);
        result.put("recordsTotal",pageInfo.getTotal());
        result.put("recordsFiltered",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        return result.toJSONString();
    }
    @ApiOperation("回复某一条留言")
    @PostMapping(value = "/comment")
    public String replyComment(@RequestBody Comment comment, HttpServletRequest request){
        JSONObject result=new JSONObject();
        comment.setEffective(true);
        comment.setIp(IPUtil.getOriginIP(request));
        comment.setCreateBy(new Date());
        if(commentService.addComment(comment)==1){
            result.put("code",200);
        }
        return result.toJSONString();
    }
    /**
     * 访问记录
     */
    @ApiOperation("获取访问记录统计")
    @GetMapping(value = "/view/count/{days}")
    public  String getViewCount(@PathVariable(name = "days") int days){
        JSONObject result=new JSONObject();
        result.put("code",200);
        List<TimesStatistic> timesStatisticList=sysViewService.queryViewCountByDays(days);
        result.put("total",timesStatisticList.size());
        result.put("rows",timesStatisticList);
        return result.toJSONString();
    }
    @ApiOperation("查看访问记录 （后台 dataTable结合）")
    @GetMapping(value = "/view/list")
    public String getViewList(@RequestParam(value = "draw")int draw,@RequestParam(value = "start")long start,@RequestParam(value = "length")int pageSize){
        JSONObject result=new JSONObject();
        int pageNum=(int)start/pageSize+1;
        PageInfo pageInfo=sysViewService.queryViews(pageNum,pageSize);
        result.put("draw",draw);
        result.put("recordsTotal",pageInfo.getTotal());
        result.put("recordsFiltered",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        return result.toJSONString();
    }
    @ApiOperation("清除浏览记录")
    @DeleteMapping(value = "/view/out/{days}")
    public String deleteViewOutOfDays(@PathVariable(name="days")int days){
        JSONObject result=new JSONObject();
        result.put("code",200);
        result.put("data",sysViewService.wipeViewOutOfDays(days));
        return result.toJSONString();
    }
    /**
     * 日志
     */
    @ApiOperation("查看访问日志 （后台 dataTable结合）")
    @GetMapping(value = "/log/list")
    public String getLogList(@RequestParam(value = "draw")int draw,@RequestParam(value = "start")long start,@RequestParam(value = "length")int pageSize){
        JSONObject result=new JSONObject();
        int pageNum=(int)start/pageSize+1;
        PageInfo pageInfo=sysLogService.queryLogs(pageNum,pageSize);
        result.put("draw",draw);
        result.put("recordsTotal",pageInfo.getTotal());
        result.put("recordsFiltered",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        return result.toJSONString();
    }
    @ApiOperation("清除日志")
    @DeleteMapping(value = "/log/out/{days}")
    public String deleteLogOutOfDays(@PathVariable(name="days")int days){
        JSONObject result=new JSONObject();
        result.put("code",200);
        result.put("data",sysLogService.wipeOutOfDays(days));
        return result.toJSONString();
    }
    /**
     * 标签
     */
    @ApiOperation("查看标签列表 （后台 dataTable结合）")
    @GetMapping(value = "/tag/list")
    public String getTagList(@RequestParam(value = "draw")int draw,@RequestParam(value = "start")long start,@RequestParam(value = "length")int pageSize){
        JSONObject result=new JSONObject();
        int pageNum=(int)start/pageSize+1;
        PageInfo pageInfo=tagInfoService.queryTagInfos(pageNum,pageSize);
        result.put("draw",draw);
        result.put("recordsTotal",pageInfo.getTotal());
        result.put("recordsFiltered",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        return result.toJSONString();
    }
    @ApiOperation("删除一个标签")
    @DeleteMapping(value = "/tag/{id}")
    public String deleteTagById(@PathVariable(value ="id")long id){
        JSONObject result=new JSONObject();
        tagInfoService.deleteTagById(id);
        result.put("code",200);
        return result.toJSONString();
    }
    @ApiOperation("更新标签信息")
    @PutMapping(value = "/tag/{id}")
    public String updateTagInfo(@RequestBody TagInfo tagInfo,@PathVariable(value = "id") long id){
        JSONObject result=new JSONObject();
        tagInfo.setId(id);
        tagInfoService.updateTagInfo(tagInfo);
        result.put("code",200);
        return result.toJSONString();
    }
    @ApiOperation("模糊查询标签信息")
    @GetMapping(value = "/tag/name/like")
    public String getTagNameLike(@RequestParam(value = "tagName") String tagName){
        JSONObject result=new JSONObject();
        result.put("code",200);
        result.put("data",tagInfoService.queryTagInfosNameLike(tagName));
        return result.toJSONString();
    }

    /**
     * 后台设置
     * @return
     */
    @ApiOperation("统计上次登录后新数据")
    @GetMapping("/new/count")
    public String getNewCommentsCount(HttpSession session){
        Date latestLoginTime=(Date)session.getAttribute("latestLoginTime");
        long newCommentCount=commentService.countNumOfAfterDate(latestLoginTime);
        long newArticleCommentCount=articleCommentService.countNumOfAfterDate(latestLoginTime);
        long newViewCount=sysViewService.countNumOfAfterDate(latestLoginTime);
        JSONObject data=new JSONObject();
        data.put("newCommentCount",newCommentCount);
        data.put("newArticleCommentCount",newArticleCommentCount);
        data.put("newViewCount",newViewCount);
        data.put("latestLoginTime",DateUtil.format(latestLoginTime,"yyyy年MM月dd日 HH:mm:ss"));
        JSONObject result=new JSONObject();
        result.put("code",200);
        result.put("data",data);
        return result.toJSONString();
    }
    @ApiOperation("获取后台全部设置选项")
    @GetMapping(value = "/setting/map")
    public String getSettingList(){
        JSONObject result=new JSONObject();
        result.put("code",200);
        result.put("data",sysSettingService.queryAllSettings());
        return result.toJSONString();
    }
    @ApiOperation("更新指定后台设置选项")
    @PutMapping(value = "/setting")
    public String putSetting(@RequestParam(name="name")String name, @RequestParam(name="value")String value){
        JSONObject result=new JSONObject();
        sysSettingService.updateSettingByName(name,value);
        result.put("code",200);
        return result.toJSONString();
    }
}
