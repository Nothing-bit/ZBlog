package com.zjg.blog.controller.fore.ajax;

import com.alibaba.fastjson.JSONObject;
import com.zjg.blog.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "ForeAjaxTagInfo")
@RequestMapping(value = "/fore")
public class ForeAjaxTagInfo extends BaseController {
    /**
     * TagInfo
     */
    @ApiOperation("获取预览分页（标签）")
    @GetMapping("/tag/article/list/{tagName}/{pageNum}")
    public String getTagArticleListByPageNum(@PathVariable(value = "tagName") String tagName, @PathVariable(value = "pageNum") int pageNum){
        JSONObject result=new JSONObject();
        result.put("code",200);
        result.put("pageInfo",articleService.queryPreviewsByTagName(tagName,pageNum,5));
        return result.toJSONString();
    }
}
