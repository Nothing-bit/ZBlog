package com.zjg.blog.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    /**
     * 上传服务
     */
    String uploadPicture(MultipartFile file);//上传图片
    String uploadFile(MultipartFile file);//上传文件
}
