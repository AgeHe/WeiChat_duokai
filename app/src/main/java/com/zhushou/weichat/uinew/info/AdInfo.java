package com.zhushou.weichat.uinew.info;

/**
 * 广告Item实体
 * Created by Administrator on 2017/11/27.
 */

public class AdInfo {

    private String cornerUrl;
    private String name;
    private String iconUrl;
    private int jumpType;
    private String jumpUrl;

    public String getCornerUrl() {
        return cornerUrl;
    }

    public void setCornerUrl(String cornerUrl) {
        this.cornerUrl = cornerUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getJumpType() {
        return jumpType;
    }

    public void setJumpType(int jumpType) {
        this.jumpType = jumpType;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }
}
