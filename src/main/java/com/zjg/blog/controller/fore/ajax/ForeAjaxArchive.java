package com.zjg.blog.controller.fore.ajax;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "ForeAjaxArchive")
@RequestMapping(value = "/fore")
public class ForeAjaxArchive extends BaseController {
    @ApiOperation("获取归档信息")
    @GetMapping("/archive/list/{pageNum}")
    public String getArchiveList(@PathVariable(value = "pageNum")int pageNum){
        JSONObject result=new JSONObject();
        PageInfo<String> pageInfo=articleService.queryMonthList(pageNum,5);
        result.put("code",200);
        result.put("pageInfo",pageInfo);
        return result.toJSONString();
    }
}
