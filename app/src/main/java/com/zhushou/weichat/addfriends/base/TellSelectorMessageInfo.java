package com.zhushou.weichat.addfriends.base;

/**
 * Created by Administrator on 2017/3/29.
 */

public class TellSelectorMessageInfo {

    public String cityName; // 城市
    public int contactsSum; //联系人数量
    public String shortCode; //地级市 号码段

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public int getContactsSum() {
        return contactsSum;
    }

    public void setContactsSum(int contactsSum) {
        this.contactsSum = contactsSum;
    }
}
