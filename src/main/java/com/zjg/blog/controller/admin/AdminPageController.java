package com.zjg.blog.controller.admin;

import com.zjg.blog.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Api(description = "AdminPage")
@RequestMapping(value = "/admin")
public class AdminPageController extends BaseController {
    @ApiOperation("登录页面")
    @GetMapping("/login/page")
    public String getLoginPage(){
        return "admin/login";
    }
    @ApiOperation(value = "获取首页")
    @GetMapping(value = "/index/page")
    public String getIndexPage(){
        return "admin/index";
    }
    @ApiOperation(value = "获取评论管理页面")
    @GetMapping(value = "/article/comment/page")
    public String getArticleCommentsPage(){
        return "admin/articleComment";
    }
    @ApiOperation(value = "获取文章管理页面")
    @GetMapping(value = "/article/page")
    public String getArticlePage(){
        return "admin/article";
    }
    @ApiOperation(value = "获取分类管理页面")
    @GetMapping(value = "/category/page")
    public String getCategoryPage(){
        return "admin/category";
    }
    @ApiOperation(value = "获取留言管理页面")
    @GetMapping(value = "/comment/page")
    public String getCommentPage(){
        return "admin/comment";
    }
    @ApiOperation(value = "获取访问记录页面")
    @GetMapping(value = "/view/page")
    public String getViewPage(Model model){
        return "admin/view";
    }
    @ApiOperation(value="获取日志查看页面")
    @GetMapping(value = "/log/page")
    public String getLogPage(Model model){
        return "admin/log";
    }
    @ApiOperation(value = "获取标签管理页面")
    @GetMapping(value = "/tag/page")
    public String getTagPage(){
        return "admin/tag";
    }
    @ApiOperation(value = "获取网页设置页面")
    @GetMapping(value = "/setting/page")
    public String getSettingPage(){
        return "admin/setting";
    }
}
