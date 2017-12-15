package com.zhushou.weichat.bean;

/**
 * Created by Administrator on 2017/5/31.
 */

public class AdvGgsResultInfo {

    private String img_src; // 图片地址
    private String title; //标题
    private String desc; //
    private String curl; //点击下载地址
    private String clickStatu; // dowload 点击下载  h5 点击跳转网页
    private String pkg_name;

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCurl() {
        return curl;
    }

    public void setCurl(String curl) {
        this.curl = curl;
    }

    public String getClickStatu() {
        return clickStatu;
    }

    public void setClickStatu(String clickStatu) {
        this.clickStatu = clickStatu;
    }

    public String getPkg_name() {
        return pkg_name;
    }

    public void setPkg_name(String pkg_name) {
        this.pkg_name = pkg_name;
    }
}
