package com.zhushou.weichat.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/31.
 */

public class AdvTypeInfo {

    private String app_index; // 应用别名
    private boolean adv_status; //广告显示状态
    private String adv_type; // 主要展示广告的商家
    private boolean adv_pay; // 是否去掉去广告计费点
    private String appid;//广告商appid
    private String posid;//广告商posid
    private List<AdverInfo> my_self_adver;//自家广告配置

    public String getApp_index() {
        return app_index;
    }

    public void setApp_index(String app_index) {
        this.app_index = app_index;
    }

    public boolean isAdv_status() {
        return adv_status;
    }

    public void setAdv_status(boolean adv_status) {
        this.adv_status = adv_status;
    }

    public String getAdv_type() {
        return adv_type;
    }

    public void setAdv_type(String adv_type) {
        this.adv_type = adv_type;
    }

    public boolean isAdv_pay() {
        return adv_pay;
    }

    public void setAdv_pay(boolean adv_pay) {
        this.adv_pay = adv_pay;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPosid() {
        return posid;
    }

    public void setPosid(String posid) {
        this.posid = posid;
    }

    public List<AdverInfo> getMy_self_adver() {
        return my_self_adver;
    }

    public void setMy_self_adver(List<AdverInfo> my_self_adver) {
        this.my_self_adver = my_self_adver;
    }
}
