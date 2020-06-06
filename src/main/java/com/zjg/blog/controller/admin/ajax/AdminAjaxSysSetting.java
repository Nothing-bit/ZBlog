package com.zjg.blog.controller.admin.ajax;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjg.blog.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
@Api(description = "AdminAjaxSysSetting")
@RestController
@RequestMapping(value = "/admin")
public class AdminAjaxSysSetting extends BaseController {
    /**
     * SysSetting
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
        data.put("latestLoginTime", DateUtil.format(latestLoginTime,"yyyy年MM月dd日 HH:mm:ss"));
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
