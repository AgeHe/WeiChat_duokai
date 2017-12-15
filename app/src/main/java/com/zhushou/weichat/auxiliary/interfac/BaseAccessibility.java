package com.zhushou.weichat.auxiliary.interfac;

import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by Administrator on 2017/3/14.
 */

public interface BaseAccessibility {
    AccessibilityNodeInfo getNodeInfo(); //获取当前界面信息
    void notifictionMessageIntent(boolean isHB); // 微信消息点击 跳转 是否红包消息
    void getHBInterfaceMessage(); // 接收红包界面信息
    void setIsDxluckyMoney(boolean bon); //执行答谢状态
    void setIsBackluckyMoney(boolean bon);//是否执行红包详情界面返回
}
