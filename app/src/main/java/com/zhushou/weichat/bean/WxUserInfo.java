package com.zhushou.weichat.bean;

/**
 * Created by Administrator on 2017/9/20.
 */

public class WxUserInfo {

    private String openId;//普通用户的标识，对当前开发者帐号唯一
    private String nickname;//普通用户昵称
    private int sex;//普通用户性别，1为男性，2为女性
    private String city;//普通用户个人资料填写的城市
    private String province;//普通用户个人资料填写的省份
    private String country;//国家，如中国为CN
    private String headimageurl;//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
    private String unionid;//用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimageurl() {
        return headimageurl;
    }

    public void setHeadimageurl(String headimageurl) {
        this.headimageurl = headimageurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
