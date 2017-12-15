package com.zhushou.weichat.auxiliary.info;

/**
 * Created by Administrator on 2017/4/17.
 */

public class HbRecordInfo {
    private String money;//抢红包金额
    private String boss; // 发红包人
    private String hbTime; // 抢红包时间

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public String getHbTime() {
        return hbTime;
    }

    public void setHbTime(String hbTime) {
        this.hbTime = hbTime;
    }
}
