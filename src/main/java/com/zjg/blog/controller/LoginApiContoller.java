package com.zjg.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.xkcoding.justauth.AuthRequestFactory;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/oauth")
public class LoginApiContoller extends BaseController{
    @Autowired
    private AuthRequestFactory factory;

    @GetMapping("/login/{platform}")
    @ResponseBody
    public void login(@RequestParam(name="lastUrl")String lastUrl, @PathVariable(value = "platform") String platform, HttpSession session, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(platform);
        session.setAttribute("lastUrl",lastUrl);
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
        //System.out.println(session.getId());
    }

    @GetMapping("/{platform}/callback")
    public void loginCallback(@PathVariable String platform, HttpServletRequest request,AuthCallback callback, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(platform);
        AuthResponse authResponse = authRequest.login(callback);
        userInfoService.login(authResponse,request);
       // System.out.println(request.getSession().getId());
        String lastUrl = (String)request.getSession().getAttribute("lastUrl");
        if(lastUrl != null){
            response.sendRedirect(lastUrl);
        }else{
            response.sendRedirect("/fore/index/page");
        }
    }
    @PostMapping("/logout")
    @ResponseBody
    public String logout(HttpServletRequest request){
        JSONObject result=new JSONObject();
        request.getSession().removeAttribute("userInfo");
        result.put("code",200);
        return result.toJSONString();
    }
    @PostMapping("/check")
    @ResponseBody
    public String check(){
        JSONObject result=new JSONObject();
        if(userInfoService.check()==1){
            result.put("code",200);
        }else{
            result.put("code",202);
        }
        return  result.toJSONString();
    }
}
