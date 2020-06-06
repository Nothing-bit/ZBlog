package com.zjg.blog.controller.fore.ajax;

import com.alibaba.fastjson.JSONObject;
import com.zjg.blog.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "ForeAjaxIndex")
@RequestMapping(value = "/fore")
public class ForeAjaxIndex extends BaseController {
    /**
     * IndefAjax
     * @return
     */
    @ApiOperation("获取首页必要数据")
    @GetMapping("/index/map")
    public String getIndexMap(){
        JSONObject result=new JSONObject();
        JSONObject data=new JSONObject();
        data.put("notice",sysSettingService.querySettingByName("notice"));
        data.put("tagInfoList",tagInfoService.selectTagCloud());
        result.put("code",200);
        result.put("data",data);
        return result.toJSONString();
    }
}
