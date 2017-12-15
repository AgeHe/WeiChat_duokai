package com.zhushou.weichat.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/2/22.
 */

public class SmallMerchantInfo {

    private int functionType;  //功能类型
    private String functionName;  // 功能名称
    private Drawable drawable;   //动能对应图标
    private String downloadUrl; // 下载地址
    private String youmengClick; //友盟统计点击key
    private String youmengPay;  // 友盟支付成功key

    public int getFunctionType() {
        return functionType;
    }

    public void setFunctionType(int functionType) {
        this.functionType = functionType;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getYoumengClick() {
        return youmengClick;
    }

    public void setYoumengClick(String youmengClick) {
        this.youmengClick = youmengClick;
    }

    public String getYoumengPay() {
        return youmengPay;
    }

    public void setYoumengPay(String youmengPay) {
        this.youmengPay = youmengPay;
    }
}
