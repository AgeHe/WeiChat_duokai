package com.zhushou.weichat.bean;

/**
 * Created by Administrator on 2017/1/15.
 */

public class AppUsersInfo {

    private int appType;
    private String appName = "";
    private String id;
    private String price;
    private String date;
    private String packageName;
    private String createTimeIdCard;//以创建时间作为分身的唯一编号

    private boolean freeTrialType = false; // 试用标识
    private long createTiem = 0; // 试用应用创建时间;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
