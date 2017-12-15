package com.zhushou.weichat.bean;

import android.graphics.drawable.Drawable;

import com.lody.virtual.os.VUserInfo;
import com.zhushou.weichat.ui.models.AppModel;

/**
 * Created by Administrator on 2017/1/7.
 */

public class ApkItemInfo {

    public static final int WEICHAT_TYPE = 0xa2;
    public static final int QQ_TYPE  = 0xa1;

    public Drawable icon; // 图标
    public CharSequence title; // 标题
    public String createDate; //创建时间
    public int apkType; // 应用类别  QQ/微信
    public VUserInfo userInfo; //启动应用实体
    public AppModel appModel; //启动应用实体
    private int openId;//启动ID
    private String price;//应用创建时价格
    private boolean freeTrialType = false; // 试用标识
    private long createTiem; // 试用应用创建时间;
    private String createTimeIdCard; // 应用创建时间  唯一标识毫秒
    private String packageName;//分身应用包名

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
    public String getCreateDate() {
        return createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public int getApkType() {
        return apkType;
    }
    public void setApkType(int apkType) {
        this.apkType = apkType;
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
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getCreateTimeIdCard() {
        return createTimeIdCard;
    }

    public void setCreateTimeIdCard(String createTimeIdCard) {
        this.createTimeIdCard = createTimeIdCard;
    }
}
