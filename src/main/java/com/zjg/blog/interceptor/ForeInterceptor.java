package com.zjg.blog.interceptor;

import com.zjg.blog.entity.SysLog;
import com.zjg.blog.entity.SysView;
import com.zjg.blog.service.SysLogService;
import com.zjg.blog.service.SysViewService;
import com.zjg.blog.util.BrowserUtil;
import com.zjg.blog.util.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
     * create by zjg 2019年10月5日15:30:46
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private SysViewService sysViewService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getSession().getAttribute("flag")==null||(Boolean) request.getSession().getAttribute("flag")==false){
            request.getSession().setAttribute("flag",true);
            String ip=IPUtil.getOriginIP(request);

            SysView sysView=new SysView();
            sysView.setIp(ip);
            sysView.setCreateBy(new Date());
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
