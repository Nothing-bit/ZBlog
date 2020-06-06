package com.zjg.blog.controller.admin.ajax;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjg.blog.controller.BaseController;
import com.zjg.blog.entity.AdminUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Api(description = "AdminAjaxUser")
@RestController
@RequestMapping(value = "/admin")
public class AdminAjaxUser extends BaseController {
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
                Date latestLoginTime= DateUtil.parse(sysSettingService.querySettingByName("latestLoginTime"),"yyyy-MM-dd HH:mm:ss");
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


}