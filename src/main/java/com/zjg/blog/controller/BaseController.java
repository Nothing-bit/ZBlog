package com.zjg.blog.controller;

import com.zjg.blog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {
    @Autowired
    protected ArticleService articleService;
    @Autowired
    protected CategoryInfoService categoryInfoService;
    @Autowired
    protected UploadService pictureService;
    @Autowired
    protected ArticleCommentService articleCommentService;
    @Autowired
    protected CommentService commentService;
    @Autowired
    protected TagInfoService tagInfoService;
    @Autowired
    protected SysLogService sysLogService;
    @Autowired
    protected SysViewService sysViewService;
    @Autowired
    protected UserInfoService userInfoService;
    @Autowired
    protected SysSettingService sysSettingService;
}
