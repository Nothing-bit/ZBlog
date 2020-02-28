package com.zjg.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.zjg.blog.dao.UserInfoMapper;
import com.zjg.blog.entity.UserInfo;
import com.zjg.blog.entity.UserInfoExample;
import com.zjg.blog.service.UserInfoService;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class UserInfoServiceImpl   implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    /**
     * 先检查用户是否存在，存在，则创建新用户，否则更新用户，然后put in session
     */
    public int login(AuthResponse response,HttpServletRequest request) {
        AuthUser authUser=(AuthUser)response.getData();
        UserInfo newUserInfo=new UserInfo();
        UserInfoExample userInfoExample=new UserInfoExample();
        BeanUtil.copyProperties(authUser,newUserInfo);
        userInfoExample.createCriteria().andUuidEqualTo(newUserInfo.getUuid());
        List<UserInfo> userInfoList=userInfoMapper.selectByExample(userInfoExample);
        if(userInfoList.size()>0){
            UserInfo oldUserInfo=userInfoList.get(0);
            newUserInfo.setId(oldUserInfo.getId());
            BeanUtil.copyProperties(oldUserInfo,newUserInfo);
            newUserInfo.setModifiedBy(new Date());
            userInfoMapper.updateByPrimaryKey(newUserInfo);
        }else{
            Date currTime=new Date();
            newUserInfo.setCreateBy(currTime);
            newUserInfo.setModifiedBy(currTime);
            userInfoMapper.insert(newUserInfo);
        }
        request.getSession().setAttribute("userInfo",newUserInfo);
        return 1;
    }

    @Override
    public int logout() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().removeAttribute("userInfo");
        return 1;
    }

    @Override
    public int check() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        UserInfo userInfo=(UserInfo)request.getSession().getAttribute("userInfo");
        if(userInfo!=null){
            return 1;
        }else{
            return 0;
        }
    }


}
