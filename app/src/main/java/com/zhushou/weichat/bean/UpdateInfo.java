package com.zhushou.weichat.bean;

/**
 * Created by Administrator on 2017/4/7.
 */

public class UpdateInfo {
    private String versionCode;//获取版本代码
    private boolean isForceUpdate;//是否强制更新
    private String updateAddress;//更新地址
    private boolean isUpdate;//是否更新

    public UpdateInfo(String versionCode, boolean isForceUpdate, String updateAddress) {
        this.versionCode = versionCode;
        this.isForceUpdate = isForceUpdate;
        this.updateAddress = updateAddress;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public boolean isForceUpdate() {
        return isForceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        isForceUpdate = forceUpdate;
    }

    public String getUpdateAddress() {
        return updateAddress;
    }

    public void setUpdateAddress(String updateAddress) {
        this.updateAddress = updateAddress;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }
}
