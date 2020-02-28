package com.zjg.blog.service.impl;

import com.zjg.blog.dao.SysSettingMapper;
import com.zjg.blog.entity.SysSetting;
import com.zjg.blog.entity.SysSettingExample;
import com.zjg.blog.service.SysSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysSettingServiceImpl implements SysSettingService {
    @Autowired
    SysSettingMapper sysSettingMapper;
    @Override
    public Map<String,String> queryAllSettings() {
        SysSettingExample sysSettingExample=new SysSettingExample();
        List<SysSetting> settingList=sysSettingMapper.selectByExample(sysSettingExample);
        Map<String,String> settingsMap=new HashMap<>();
        for(SysSetting item:settingList){
            settingsMap.put(item.getName(),item.getValue());
        }
        return settingsMap;
    }

    @Override
    public int updateSettingByName(String name,String value) {
        SysSetting sysSetting=new SysSetting();
        sysSetting.setName(name);
        sysSetting.setValue(value);
        sysSetting.setOperateBy(new Date());
        SysSettingExample sysSettingExample=new SysSettingExample();
        sysSettingExample.createCriteria()
                .andNameEqualTo(sysSetting.getName());
        return sysSettingMapper.updateByExampleSelective(sysSetting,sysSettingExample);
    }

    @Override
    public int addSetting(String name, String value) {
        SysSetting sysSetting=new SysSetting();
        sysSetting.setName(name);
        sysSetting.setValue(value);
        sysSetting.setOperateBy(new Date());
        sysSetting.setCreateBy(new Date());
        return sysSettingMapper.insert(sysSetting);
    }

    @Override
    public String querySettingByName(String name) {
        SysSettingExample sysSettingExample=new SysSettingExample();
        sysSettingExample.createCriteria()
                .andNameEqualTo(name);
        String value=sysSettingMapper.selectByExample(sysSettingExample).get(0).getValue();
        return value;
    }
}
