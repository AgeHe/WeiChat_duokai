package com.zhushou.weichat.auxiliary.interfac;

import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by Administrator on 2017/3/14.
 */

public interface WeiChatMessage {

    //聊天界面红包点击处理
    void HbMessageProcessing(AccessibilityNodeInfo info);

    //拆红包处理
    void CHBMessageProcessing(AccessibilityNodeInfo info);

    //普通消息自动回复处理
    void commonMessageProcessing(AccessibilityNodeInfo info);

    //红包详情界面返回操作
    void luckyMoneyDetailBack(AccessibilityNodeInfo info);

    //红包详情界面答谢处理--留言点击
    void dxluckyMoneyProcessing(AccessibilityNodeInfo info);

    void getHbRecordProcessing();

    //聊天界面实时实时对红包消息进行操作
    void chatViewRedMessageProcessing(AccessibilityNodeInfo info);

    //红包详情界面答谢处理-- 输入答谢内容-->>发送点击
    void dxluckymoneyInputProcessing(AccessibilityNodeInfo info);

    void screenStatus(boolean status);

}
