package com.zjg.blog.util;

import com.github.pagehelper.PageInfo;

public class PageInfoUtil {
    /**
     * 分页 pageinfo dao 与 dto 的组装
     * create by zjg 2019年10月10日19:24:06
     * @param daoPageInfo
     * @param dtoPageInfo
     */
    public static void copyPageInfo(PageInfo daoPageInfo,PageInfo dtoPageInfo){
        dtoPageInfo.setPages(daoPageInfo.getPages());
        dtoPageInfo.setPageNum(daoPageInfo.getPageNum());
        dtoPageInfo.setPageSize(daoPageInfo.getPageSize());
        dtoPageInfo.setTotal(daoPageInfo.getTotal());
        dtoPageInfo.setIsFirstPage(daoPageInfo.isIsFirstPage());
        dtoPageInfo.setIsLastPage(daoPageInfo.isIsLastPage());
        dtoPageInfo.setSize(daoPageInfo.getSize());
    }
}
