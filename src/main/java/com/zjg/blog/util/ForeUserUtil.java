package com.zjg.blog.util;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjg.blog.entity.ForeUser;
import me.zhyd.oauth.model.AuthResponse;

import javax.servlet.http.HttpServletRequest;

public class ForeUserUtil {
    /**
     * 登录存放信息
     * @param platform
     * @param authResponse
     * @param request
     * @return
     */
    public static int loginFactory(String platform, AuthResponse authResponse, HttpServletRequest request) {
        int code;
        switch (platform){
            case "qq":code=loginQQ(authResponse,request);break;
            default:code=0;break;
        }
        return code;
    }
    private static int loginQQ(AuthResponse authResponse,HttpServletRequest request){
        JSONObject result= JSON.parseObject( JSONUtil.toJsonStr(authResponse));
        if(result.getInteger("code")==2000){
            JSONObject data=result.getJSONObject("data");
            ForeUser userInfo=new ForeUser();
            userInfo.setNickName(data.getString("nickname"));
            userInfo.setOpenId(data.getString("uuid"));
            userInfo.setPlatform("qq");
            request.getSession().setAttribute("userInfo",userInfo);
            return 1;
        }else{
            return 0;
        }
    }

    /**
     * 检查用户是否登录
     * @param request
     * @return
     */
    public static int checkLogin(HttpServletRequest request){
        if(request.getSession().getAttribute("userInfo")!=null){
            return 1;
        }else{
            return 0;
        }
    }


}
