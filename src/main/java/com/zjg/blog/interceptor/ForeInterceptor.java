package com.zjg.blog.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjg.blog.entity.SysLog;
import com.zjg.blog.entity.SysView;
import com.zjg.blog.service.SysLogService;
import com.zjg.blog.service.SysViewService;
import com.zjg.blog.util.BrowserUtil;
import com.zjg.blog.util.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
@Configuration
public class ForeInterceptor implements HandlerInterceptor {
    /**
     * 前台拦截器
     * 新增ip地址解析
     * update 2020年4月2日17:01:23
     * author zjg
     */
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private SysViewService sysViewService;
    @Value("${api.ip.reslution.address}")
    String ipReslutionAddress;

    /**
     * 新增ip地址解析，如果更改解析接口，则需要作出对应的更改
     * update 2020年4月2日20:05:36
     * author zjg
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getSession().getAttribute("firstView")==null||(Boolean) request.getSession().getAttribute("firstView")==false){
            request.getSession().setAttribute("firstView",true);
            String ip=IPUtil.getOriginIP(request);
            String reslutionUrl= StrUtil.replace(ipReslutionAddress,"xxx.xxx.xxx.xxx",ip);
            JSONObject result= JSON.parseObject(HttpUtil.get(reslutionUrl));
            String address="";
            String isp="";
            //System.out.println(ip+" "+reslutionUrl+" "+result.toJSONString()+" ");
            if(StrUtil.equals(result.getString("status"),"success")){
                address=result.getString("country")+" "+result.getString("regionName")+" "+result.getString("city");
                isp=result.getString("isp");
            }else{//解析失败
                address="unknown";
                isp="unknown";
            }

            SysView sysView=new SysView();
            sysView.setIp(ip);
            sysView.setCreateBy(new Date());
            sysView.setAddress(address);
            sysView.setIsp(isp);
            sysView.setOperateBy(BrowserUtil.getOsAndBrowserInfo(request));
            sysViewService.addView(sysView);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView){
        String ip = IPUtil.getOriginIP(request);
        String browserName= BrowserUtil.getOsAndBrowserInfo(request);
        String url=request.getRequestURL().toString();
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method=handlerMethod.getMethod();
        SysLog sysLog=new SysLog();

        sysLog.setIp(ip);
        sysLog.setCreateBy(new Date());
        sysLog.setOperateBy(browserName);
        sysLog.setRemark(method.getName());
        sysLog.setOperateUrl(url);
        sysLogService.addLog(sysLog);
    }
}
