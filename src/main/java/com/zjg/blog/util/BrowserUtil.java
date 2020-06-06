package com.zjg.blog.util;

import javax.servlet.http.HttpServletRequest;

public class BrowserUtil {
    /**
     * 获取操作系统,浏览器及浏览器版本信息
     *
     * @param request
     * @return
     */
    public static String getOsAndBrowserInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if(userAgent==null){
            return "unknown-unknown";
        }
        String user = userAgent.toLowerCase();

        String os = "";
        String browser = "";

        //=================OS Info=======================
        if (userAgent.toLowerCase().indexOf("windows") >= 0) {
            os = "Windows";
        } else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
            os = "Mac";
        } else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
            os = "Unix";
        } else if (userAgent.toLowerCase().indexOf("android") >= 0) {
            os = "Android";
        } else if (userAgent.toLowerCase().indexOf("iphone") >= 0) {
            os = "IPhone";
        } else {
            os = "UnKnown";
        }
        //===============Browser===========================
        if (user.contains("edge")) {
            browser ="Edge";
        } else if (user.contains("safari")) {
            browser ="Safari";
        } else if (user.contains("opr") || user.contains("opera")) {
            browser="Opera";
        } else if (user.contains("chrome")) {
            browser = "Chrome";
        }  else if (user.contains("firefox")||user.contains("mozilla")) {
            browser = "Firefox";
        } else if (user.contains("rv")) {
            browser = "IE";
        } else {
            browser = "UnKnown";
        }

        return os + "-" + browser;
    }
}