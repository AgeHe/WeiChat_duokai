package com.zhushou.weichat.bean;

/**
 * Created by Administrator on 2017/6/8.
 */

public class WXSDKParamsJsonInfo {

    public String str_appid;
    public String str_partnerId;
    public String str_nonceStr;
    public String str_orderNumber;
    public String orderSign;

    public String getStr_appid() {
        return str_appid;
    }

    public void setStr_appid(String str_appid) {
        this.str_appid = str_appid;
    }

    public String getStr_partnerId() {
        return str_partnerId;
    }

    public void setStr_partnerId(String str_partnerId) {
        this.str_partnerId = str_partnerId;
    }

    public String getStr_nonceStr() {
        return str_nonceStr;
    }

    public void setStr_nonceStr(String str_nonceStr) {
        this.str_nonceStr = str_nonceStr;
    }

    public String getStr_orderNumber() {
        return str_orderNumber;
    }

    public void setStr_orderNumber(String str_orderNumber) {
        this.str_orderNumber = str_orderNumber;
    }

    public String getOrderSign() {
        return orderSign;
    }

    public void setOrderSign(String orderSign) {
        this.orderSign = orderSign;
    }
}
