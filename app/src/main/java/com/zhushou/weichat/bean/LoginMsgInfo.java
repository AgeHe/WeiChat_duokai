package com.zhushou.weichat.bean;

/**
 * Created by Administrator on 2017/10/30.
 */

public class LoginMsgInfo {

    private String id; //用户id
    private String nickname; //用户昵称
    private String face; //用户头像
    private String logintype; //用户登录方式
    private String openid; // 用户登录openid
    private String bdCode;//补单code


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getLogintype() {
        return logintype;
    }

    public void setLogintype(String logintype) {
        this.logintype = logintype;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getBdCode() {
        return bdCode;
    }

    public void setBdCode(String bdCode) {
        this.bdCode = bdCode;
    }
}
