package com.zhushou.weichat.bean;

/**
 * Created by Administrator on 2017/4/18.
 * 广告实体类
 */

public class AdverInfo {
    private String name;//对应名称
    private String titlename;
    private String iconAddress;//图片对应下载地址
    private String appAddress;//app对应下载地址
    private int ad_length ;//广告时长
    private  String clickStatu; //下载链接orH5
    private String pkg_name;//下载包包名

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconAddress() {
        return iconAddress;
    }

    public void setIconAddress(String iconAddress) {
        this.iconAddress = iconAddress;
    }

    public String getAppAddress() {
        return appAddress;
    }

    public void setAppAddress(String appAddress) {
        this.appAddress = appAddress;
    }

    public String getTitlename() {
        return titlename;
    }

    public void setTitlename(String titlename) {
        this.titlename = titlename;
    }

    public int getAd_lenght() {
        return ad_length;
    }

    public void setAd_lenght(int ad_lenght) {
        this.ad_length = ad_lenght;
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
