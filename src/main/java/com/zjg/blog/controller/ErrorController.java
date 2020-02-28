//package com.zjg.blog.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Controller
//public class ErrorController extends BaseController implements org.springframework.boot.web.servlet.error.ErrorController {
//    @RequestMapping("/error")
//    public String handleError(HttpServletRequest request){
//        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//        switch (statusCode){
//            case 400:return "/error/400";
//            case 500:return "/error/500";
//            default: return "/error/404";
//        }
//    }
//    @Override
//    public String getErrorPath() {
//        return null;
//    }
//}
