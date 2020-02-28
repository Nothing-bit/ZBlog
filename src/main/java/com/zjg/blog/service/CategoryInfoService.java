package com.zjg.blog.service;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.entity.CategoryInfo;

import java.util.List;

public interface CategoryInfoService {
    /**
     * 分类信息
     *
     * junit test passed
     */
    //admin
    int addCategory(CategoryInfo categoryInfo);//添加分类
    int deleteCategoryById(long id);//删除分类
    int updateCategory(CategoryInfo categoryInfo);//修改分类
    PageInfo queryCategories(int pageNum,int pageSize);//获取指定页数的分类信息
    //fore
    CategoryInfo getOneById(long id);//根据id获取指定分类
    List<CategoryInfo> queryAllCategory();//获取全部分类信息
    long countCategory();//统计分类个数
}
