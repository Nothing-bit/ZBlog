package com.zjg.blog.controller.fore;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.zjg.blog.controller.BaseController;
import com.zjg.blog.dto.ArticleView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@RequestMapping("/fore")
@Api(description = "ForePage")
public class ForePageController extends BaseController {
    @ApiOperation(value = "获取文章细节页面")
    @GetMapping("/article/page/{id}")

    public String getArticleDetailsPage(@PathVariable long id, Model model){
        ArticleView dto=articleService.getOneById(id);
        model.addAttribute("articleDetail",dto);
        model.addAttribute("pageSize",5);
        model.addAttribute("total",articleCommentService.countNumOfAbleByArticleId(id));
        return "fore/article";
    }
    @ApiOperation(value = "获取博客首页")
    @GetMapping("/index/page")
    public String getIndexPage(Model model){
        int pageSize=5;//分页用pageSize(初始化）
        model.addAttribute("total",articleService.countPublicArticle());//文章总数
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("countArticle",articleService.countPublicArticle());
        model.addAttribute("countCategory",categoryInfoService.countCategory());
        model.addAttribute("countTag",tagInfoService.countTag());
        PageHelper.orderBy("traffic desc");
        model.addAttribute("topPageInfo",articleService.queryPublicPreviews(1,pageSize));
        model.addAttribute("tagList",tagInfoService.queryHotTagInfo(20));
        model.addAttribute("latestComment",commentService.queryLatestCommentList(10));
        model.addAttribute("latestArticleComment",articleCommentService.queryLatestArticleComment(10));
        return "fore/index";
    }
    @ApiOperation(value="归档页面")
    @GetMapping("/archive/page")
    public String getTimeLinePage(Model model,@RequestParam(value = "month") String month){
        if(month.equals("all")){//是否是全部
            model.addAttribute("total",articleService.countPublicArticle());
        }else{
            Date date= DateUtil.parse(month,"yyyy年MM月");
            Date beginDate=DateUtil.beginOfMonth(date);
            Date endDate=DateUtil.endOfMonth(date);
            model.addAttribute("total",articleService.countArticleByBeginAndEndDate(beginDate,endDate));
        }
        PageHelper.orderBy("create_by desc");
        model.addAttribute("pageSize",5);
        model.addAttribute("month",month);
        //menu分页
        model.addAttribute("menuTotal",articleService.countMonths());
        model.addAttribute("menuPageSize",5);
        return "fore/archive";
    }
    @ApiOperation(value="分类页面")
    @GetMapping("/category/page/{categoryId}")
    public String getCategoryPage(Model model,@PathVariable(value = "categoryId") long categoryId){
        if(categoryId==0L){//判断是否显示全部
            model.addAttribute("total",articleService.countPublicArticle());
            model.addAttribute("categoryName","all");
        }else{
            model.addAttribute("total",categoryInfoService.countArticleByCategoryId(categoryId));
            model.addAttribute("categoryName",categoryInfoService.getOneById(categoryId).getName());
        }
        model.addAttribute("categoryId",categoryId);
        model.addAttribute("pageSize",5);
        //分类菜单信息
        model.addAttribute("menuTotal",categoryInfoService.countCategory());
        model.addAttribute("menuPageSize",5);
        return "fore/category";
    }
    @ApiOperation(value = "标签页面")
    @GetMapping("/tag/page")
    public String getTagPage(Model model){
        model.addAttribute("tagList",tagInfoService.queryAllTagInfo());
        return "fore/tag";
    }
    @ApiOperation(value = "标签查询结果页面")
    @GetMapping("/article/tag/page")
    public String queryArticleByTagName(@RequestParam(value = "tagName") String tagName,Model model){
        model.addAttribute("currentTag",tagInfoService.queryOneByName(tagName));
        model.addAttribute("pageSize",5);
        return "fore/articleTag";
    }
    @ApiOperation(value="关于页面")
    @GetMapping("/about/page")
    public String getAboutPage(Model model){
        model.addAttribute("pageSize",5);
        model.addAttribute("total",commentService.countNumOfEnableAndParent());
        return "fore/about";
    }
}
