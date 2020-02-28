package com.zjg.blog.entity;

public class ForeUser {
    /**
     * 前台用户信息缓存用
     */
    private String nickName;//昵称
    private String openId;//开放Id
    private String platform;//所属平台
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
