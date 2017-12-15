package com.zhushou.weichat.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/15.
 */

public class UserInfo implements Serializable {
    private String id;
    private String phone;  // 已放弃该参数
    private String password; // 已放弃该参数
    private String createTime; //创建时间
    private String goodIdentity; //当前购买商品类型
    private String pastTime; //过期时间 年月日
    private boolean isPast; // 是否过期  用户注册后默认值为false
    private String face;//用户头像地址
    private String nickname;//用户昵称
    private String openid;//第三方登录 用户唯一ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGoodIdentity() {
        return goodIdentity;
    }

    public void setGoodIdentity(String goodIdentity) {
        this.goodIdentity = goodIdentity;
    }

    public String getPastTime() {
        return pastTime;
    }

    public void setPastTime(String pastTime) {
        this.pastTime = pastTime;
    }

    public boolean isPast() {
        return isPast;
    }

    public void setPast(boolean past) {
        isPast = past;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
