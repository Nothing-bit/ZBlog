package com.zjg.blog.controller.fore.ajax;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.controller.BaseController;
import com.zjg.blog.entity.CategoryInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "ForeAjaxCategory")
@RequestMapping(value = "/fore")
public class ForeAjaxCategory extends BaseController {
    /**
     *
     * Category
     */
    @ApiOperation("获取所有分类信息")
    @GetMapping("/category/list/{pageNum}")
    public String getCategoryList(@PathVariable(value = "pageNum") int pageNum){
        JSONObject result=new JSONObject();
        PageInfo<CategoryInfo> pageInfo=categoryInfoService.queryCategories(pageNum,5);
        result.put("code",200);
        result.put("pageInfo",pageInfo);
        return result.toJSONString();
    }
}
