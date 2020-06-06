package com.zjg.blog.dao;

import com.zjg.blog.entity.SysView;
import com.zjg.blog.entity.SysViewExample;
import com.zjg.blog.entity.TimesStatistic;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SysViewMapper {
    long countByExample(SysViewExample example);

    int deleteByExample(SysViewExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysView record);

    int insertSelective(SysView record);

    List<SysView> selectByExample(SysViewExample example);

    SysView selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SysView record, @Param("example") SysViewExample example);

    int updateByExample(@Param("record") SysView record, @Param("example") SysViewExample example);

    int updateByPrimaryKeySelective(SysView record);

    int updateByPrimaryKey(SysView record);

    List<TimesStatistic> queryCountByDays(int days);
}