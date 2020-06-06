package com.zjg.blog.service;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.entity.TagInfo;

import java.util.List;
import java.util.Map;

public interface TagInfoService {
    /**
     * 标签的相关功能
     */
    //fore
    TagInfo queryOneByName(String tagName);//获取其中一个信息
    long countTag();//统计tag数量
    List<TagInfo> queryAllTagInfo();//标签列表
    List<TagInfo> queryHotTagInfo(int top);//获取热门标签
    List<Map<String,Object>> selectTagCloud();//获取标签云
    //admin
    int deleteTagById(long id);//删除标签
    PageInfo queryTagInfos(int pageNum,int pageSize,String searchValue,String orderProperty,String orderDirection);//获取标签列表
    int updateTagInfo(TagInfo tagInfo);//修改标签信息
    List<TagInfo> queryTagInfosNameLike(String tagName);//根据名称查询标签信息（模糊查询）
    int addTagInfo(TagInfo tagInfo);//添加标签信息
}
