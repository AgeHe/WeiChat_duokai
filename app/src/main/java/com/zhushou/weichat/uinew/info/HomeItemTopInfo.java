package com.zhushou.weichat.uinew.info;

/**
 * Created by Administrator on 2017/11/27.
 */

public class HomeItemTopInfo {

    private String imageUrl;
    private int jumpType;
    private String jumpAddress;
    private String adName;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getJumpType() {
        return jumpType;
    }

    public void setJumpType(int jumpType) {
        this.jumpType = jumpType;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getJumpAddress() {
        return jumpAddress;
    }

    public void setJumpAddress(String jumpAddress) {
        this.jumpAddress = jumpAddress;
    }
}
