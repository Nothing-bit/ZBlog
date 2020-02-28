package com.zjg.blog.service.impl;

import com.zjg.blog.dao.ArticlePictureMapper;
import com.zjg.blog.entity.ArticlePicture;
import com.zjg.blog.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

@Service
public class UploadServiceImpl implements UploadService {
    @Value("${images.upload.path}")
    private String filePath;
    @Autowired
    private ArticlePictureMapper pictureMapper;
    @Override
    public String uploadPicture(MultipartFile file) {
        // 时间戳作为文件名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName=(new Date().getTime())+suffix;
        File file1=new File(filePath+fileName);
        try{
            file.transferTo(file1);
        }catch (Exception e){
            e.printStackTrace();
        }
        ArticlePicture picture=new ArticlePicture();
        picture.setArticleId(-1L);
        picture.setPictureUrl("/images/"+fileName);
        picture.setCreateBy(new Date());
        picture.setModifiedBy(new Date());
        pictureMapper.insert(picture);
        return "/images/"+fileName;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        return null;
    }
}
