package com.zjg.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.dao.CategoryInfoMapper;
import com.zjg.blog.entity.CategoryInfo;
import com.zjg.blog.entity.CategoryInfoExample;
import com.zjg.blog.service.CategoryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class CategoryInfoServiceImpl implements CategoryInfoService {
    @Autowired
    private CategoryInfoMapper categoryInfoMapper;
    @Override
    public int addCategory(CategoryInfo categoryInfo) {
        categoryInfo.setNumber(0);
        categoryInfo.setCreateBy(new Date());
        categoryInfo.setModifiedBy(new Date());
        categoryInfoMapper.insert(categoryInfo);
        return 1;
    }

    @Override
    public int deleteCategoryById(long id) {
        categoryInfoMapper.deleteByPrimaryKey(id);
        return 1;
    }

    @Override
    public int updateCategory(CategoryInfo categoryInfo) {
        categoryInfo.setModifiedBy(new Date());
        categoryInfo.setCreateBy(null);
        categoryInfo.setNumber(null);
        categoryInfoMapper.updateByPrimaryKeySelective(categoryInfo);
        return 1;
    }

    @Override
    public List<CategoryInfo> queryAll() {
        CategoryInfoExample categoryInfoExample=new CategoryInfoExample();
        categoryInfoExample.setOrderByClause("number desc");
        return categoryInfoMapper.selectByExample(categoryInfoExample);
    }

    @Override
    public PageInfo queryCategories(int pageNum, int pageSize,String searchValue,String orderProperty,String orderDirection) {
        CategoryInfoExample categoryInfoExample=new CategoryInfoExample();
        categoryInfoExample.setOrderByClause(orderProperty+" "+orderDirection);
        categoryInfoExample.createCriteria()
                .andNameLike("%"+searchValue+"%");
        PageHelper.startPage(pageNum,pageSize);
        List<CategoryInfo> daoList=categoryInfoMapper.selectByExample(categoryInfoExample);
        PageInfo<CategoryInfo> daoPageInfo=new PageInfo<>(daoList);
        return daoPageInfo;
    }

    @Override
    public CategoryInfo getOneById(long id) {
        return categoryInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<CategoryInfo> queryCategories(int pageNum,int pageSize) {
        List<CategoryInfo> infoList;
        CategoryInfoExample example=new CategoryInfoExample();
        PageHelper.startPage(pageNum,pageSize);
        infoList=categoryInfoMapper.selectByExample(example);
        PageInfo<CategoryInfo> pageInfo=new PageInfo<>(infoList);
        return pageInfo;
    }

    @Override
    public long countCategory() {
        CategoryInfoExample example=new CategoryInfoExample();
        return categoryInfoMapper.countByExample(example);
    }

    @Override
    public long countArticleByCategoryId(long categoryId) {
        return categoryInfoMapper.selectByPrimaryKey(categoryId).getNumber();
    }
}
