package com.zjg.blog.interceptor;

import com.zjg.blog.entity.AdminUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Value("${admin.username}")
    String username;
    @Value("${admin.password}")
    String password;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = true;
        AdminUser user = (AdminUser) request.getSession().getAttribute("adminUser");
        if (user==null) {
            flag = false;
        } else {
            // 对用户账号进行验证,是否正确
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                flag = true;
            } else {
                flag = false;
            }
        }
        if(flag==false){
            response.sendRedirect("/admin/login/page");
        }
        return flag;
    }
}
