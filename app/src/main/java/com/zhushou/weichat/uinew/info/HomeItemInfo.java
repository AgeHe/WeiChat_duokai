package com.zhushou.weichat.uinew.info;

import android.graphics.drawable.Drawable;

import com.lody.virtual.os.VUserInfo;
import com.zhushou.weichat.ui.models.AppModel;

/**
 * Created by Administrator on 2017/11/27.
 */

public class HomeItemInfo {

    public static final int Launcher_WX = 0x01;
    public static final int Launcher_ALL_APP = 0x02;


    public Drawable icon; // 图标
    public CharSequence title = ""; // 标题
    public int launcherType = Launcher_WX; // 点击启动类别
    public AdInfo adInfo; //广告实体
    public VUserInfo userInfo; //启动应用实体
    public AppModel appModel; //启动应用实体
    private int openId;//启动ID
    private boolean freeTrialType = false; // 试用标识
    private long createTiem; // 应用创建时间;
    private String createTimeIdCard; // 应用创建时间  唯一标识毫秒
    private String packageName;//分身应用包名
    private boolean isVip;


    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public int getLauncherType() {
        return launcherType;
    }

    public void setLauncherType(int launcherType) {
        this.launcherType = launcherType;
    }

    public AdInfo getAdInfo() {
        return adInfo;
    }

    public void setAdInfo(AdInfo adInfo) {
        this.adInfo = adInfo;
    }

    public VUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(VUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public AppModel getAppModel() {
        return appModel;
    }

    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
    }

    public int getOpenId() {
        return openId;
    }

    public void setOpenId(int openId) {
        this.openId = openId;
    }

    public boolean isFreeTrialType() {
        return freeTrialType;
    }

    public void setFreeTrialType(boolean freeTrialType) {
        this.freeTrialType = freeTrialType;
    }

    public long getCreateTiem() {
        return createTiem;
    }

    public void setCreateTiem(long createTiem) {
        this.createTiem = createTiem;
    }

    public String getCreateTimeIdCard() {
        return createTimeIdCard;
    }

    public void setCreateTimeIdCard(String createTimeIdCard) {
        this.createTimeIdCard = createTimeIdCard;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

}
