package com.zjg.blog.controller.fore.ajax;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjg.blog.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Api(description = "ForeAjaxArticle")
@RequestMapping(value = "/fore")
public class ForeAjaxArticle extends BaseController {
    /**
     *  article
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
    public String getArticleCategoryListByPageNum(@PathVariable(name="categoryId")long categoryId,
                                                  @PathVariable(name="pageNum")int pageNum){
        JSONObject result=new JSONObject();
        if(categoryId==0L){//判断是否无分类筛选条件
            result.put("pageInfo",articleService.queryPublicPreviews(pageNum,5));
        }else{
            result.put("pageInfo",articleService.queryPreviewsByCategory(categoryId,pageNum,5));
        }
        result.put("code",200);
        return result.toJSONString();
    }
    @ApiOperation(value="获取预览分页(归档 format:yyyy年MM月)")
    @GetMapping(value = "/archive/article/list/{pageNum}")
    public String getArchiveArticleListByPageNum(@RequestParam(value = "month") String month,
                                                 @PathVariable(value = "pageNum")int pageNum){
        JSONObject result=new JSONObject();
        if(month.equals("all")){//判断是否是全部
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
}
