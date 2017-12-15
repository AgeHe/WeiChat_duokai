package com.zhushou.weichat.auxiliary;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.zhushou.weichat.auxiliary.info.LuckyMoneyDetailOpratInfo;
import com.zhushou.weichat.auxiliary.interfac.BaseAccessibility;
import com.zhushou.weichat.auxiliary.interfac.NotifictionMessage;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryConstans;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryFunctionSwitch;
import com.zhushou.weichat.auxiliary.utils.RedPacketsVoiceUtil;
import com.zhushou.weichat.utils.TLog;

/**
 * Created by Administrator on 2017/3/8.
 */

public class BaseAccessibilityService extends AccessibilityService implements BaseAccessibility {


    private static final java.lang.String TAG = "BaseAccessibilityService";

    @Override
    public void onInterrupt() {
        TLog.log(TAG,"onInterrupt()--未知方法执行--（在系统想要中断AccessibilityService返给的响应时会调用。在整个生命周期里会被调用多次。）");
    }


    private NotifictionMessage notifictionMessage;

    private ChatAuxiliary weiChatMessage;

    AuxiliaryFunctionSwitch auxiliaryFunctionSwitch;

    private LuckyMoneyDetailOpratInfo luckyMoneyDetailOpratInfo;

    private RedPacketsVoiceUtil redPacketsVoice;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        //做配置/初始化操作
        weiChatMessage = new ChatAuxiliary(this,this);
        //通知栏 微信消息处理
        notifictionMessage = new WeiChatNotification(this,weiChatMessage,this);

        auxiliaryFunctionSwitch = new AuxiliaryFunctionSwitch(this);

        redPacketsVoice = new RedPacketsVoiceUtil(this);

    }

    @Override
    protected boolean onGesture(int gestureId) {
        TLog.log(TAG,"onGesture(int gestureId)--未知方法执行--gestureId= "+gestureId);
        return super.onGesture(gestureId);
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        TLog.log(TAG,"onKeyEvent event= "+event.getAction());
        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int eventType = accessibilityEvent.getEventType();
        String className = accessibilityEvent.getPackageName().toString();
        Log.i(TAG, "onAccessibilityEvent: className="+className);
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:  //收到通知栏消息
                Log.i(TAG, "onAccessibilityEvent: TYPE_NOTIFICATION_STATE_CHANGED//收到通知栏消息");
                if (className.equals("com.tencent.mm")||className.equals("com.zhushou.weichat")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        notifictionMessage.weichatNotificationDispense(accessibilityEvent);
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: // 窗口变化
                Log.i(TAG, "onAccessibilityEvent: xxxclass="+accessibilityEvent.getClassName().toString());
                Log.i(TAG, "onAccessibilityEvent: xxxpackage="+accessibilityEvent.getPackageName().toString());
                Log.i(TAG, "onAccessibilityEvent: TYPE_WINDOW_STATE_CHANGED//窗口变化");
                if (!auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_HB)&&
                        !auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_HF))
                    return;

                if (className.equals("com.tencent.mm")||className.equals("com.zhushou.weichat")){

                    if (accessibilityEvent.getClassName().toString().equals("com.tencent.mm.ui.LauncherUI")&&//微信主界面 包括聊天界面
                            auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_DX)&&
                            isSaveRecordInfo){//答谢开关
                        weiChatMessage.dxluckyMoneyProcessing(getNodeInfo()); // 答谢操作
                        isSaveRecordInfo = false;
                        Log.i(TAG, "onAccessibilityEvent: 答谢操作执行....");
                    }else{
                        isSaveRecordInfo = false;
                    }

                    if (accessibilityEvent.getClassName().toString().equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")&&isBackLuckymoney){
                        weiChatMessage.getHbRecordProcessing();
                        if (luckyMoneyDetailOpratInfo==null)
                            luckyMoneyDetailOpratInfo = new LuckyMoneyDetailOpratInfo();
//                        if (auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_DX)&&!luckyMoneyDetailOpratInfo.isDxOprat()){//自动答谢
//                            luckyMoneyDetailOpratInfo.setDxOprat(true);
//                            weiChatMessage.dxluckyMoneyProcessing(getNodeInfo()); //红包详情界面答谢---留言点击
//                        }else
                        if (auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_DX)){//返回聊天主界面
                            if (auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_DX)){
                                luckyMoneyDetailOpratInfo.setDxOprat(true);
                            }else{
                                luckyMoneyDetailOpratInfo.setDxOprat(false);
                            }
                            weiChatMessage.luckyMoneyDetailBack(getNodeInfo()); //执行操作
                            Log.i(TAG, "onAccessibilityEvent: 返回操作执行....");
                        }
                        isBackLuckymoney = false;
                        return;
                    }

                    if (isHBInterfacemessage){ // 红包界面变动
                        try {
                            Thread.sleep(200); // 默认延时200毫秒 打开红包
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        //判断是否是红包弹窗（加载红包弹窗之前有一个loading弹窗 classname=com.tencent.mm.ui.base.p ）
                        if (!accessibilityEvent.getClassName().toString().equals("com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f"))
                            return;
                        weiChatMessage.CHBMessageProcessing(getNodeInfo()); // 拆红包操作
                        isHBInterfacemessage = false;
                    }else{
                        if (!isDealWeiChatMessage)
                            return;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                            if (isWeiChatMessage){ // 红包消息
                                if (auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_VOICE)){
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            redPacketsVoice.PlaySound(1,0);
                                        }
                                    }).start();
                                }
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e){
                                    e.printStackTrace();
                                }
                                weiChatMessage.HbMessageProcessing(getNodeInfo());
                            }else{ //普通消息
                                weiChatMessage.commonMessageProcessing(getNodeInfo());
                            }
                            isDealWeiChatMessage = false;
                        }
                    }
                }
                String smallTag = accessibilityEvent.getPackageName().toString();
                TLog.log(TAG,"log class_name="+className+"small_class_name="+smallTag);
                break;
            case  AccessibilityEvent.TYPE_VIEW_CLICKED:   //点击事件
                Log.i(TAG, "onAccessibilityEvent: TYPE_VIEW_CLICKED//点击事件");
//                if (luckyMoneyDetailOpratInfo!=null&&luckyMoneyDetailOpratInfo.isDxOprat()){
//                    Log.i(TAG, "onAccessibilityEvent: 输入答谢内容");
//                    weiChatMessage.dxluckymoneyInputProcessing(getNodeInfo());
//                }
                break;
            case AccessibilityEvent.CONTENT_CHANGE_TYPE_TEXT: //文本??改变
                Log.i(TAG, "onAccessibilityEvent: CONTENT_CHANGE_TYPE_TEXT//文本状态变化");
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                Log.i(TAG, "onAccessibilityEvent: TYPE_VIEW_FOCUSED");
                break;
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                Log.i(TAG, "onAccessibilityEvent: TYPE_VIEW_SELECTED");
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                Log.i(TAG, "onAccessibilityEvent: TYPE_VIEW_TEXT_CHANGED");
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                Log.i(TAG, "onAccessibilityEvent: TYPE_TOUCH_EXPLORATION_GESTURE_END");
                break;
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                Log.i(TAG, "onAccessibilityEvent: TYPE_ANNOUNCEMENT");
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                Log.i(TAG, "onAccessibilityEvent: TYPE_TOUCH_EXPLORATION_GESTURE_START");
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                Log.i(TAG, "onAccessibilityEvent: TYPE_VIEW_HOVER_ENTER");
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                Log.i(TAG, "onAccessibilityEvent: TYPE_VIEW_HOVER_EXIT");
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                Log.i(TAG, "TYPE_VIEW_SCROLLED: TYPE_VIEW_SCROLLED");
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                Log.i(TAG, "onAccessibilityEvent: TYPE_VIEW_TEXT_SELECTION_CHANGED");
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
//                ChangeAccessibility(getNodeInfo());
                Log.i(TAG, "onAccessibilityEvent: TYPE_WINDOW_CONTENT_CHANGED");
                Log.i(TAG, "onAccessibilityEvent: xxxclass="+accessibilityEvent.getClassName().toString());
                if (className.equals("com.tencent.mm")||className.equals("com.zhushou.weichat")){
                    if (!isDealWeiChatMessage){
//                        if (accessibilityEvent.getClassName().toString().equals("android.widget.TextView")){
//                            weiChatMessage.chatViewRedMessageProcessing(getNodeInfo());
//                            Log.i(TAG, "onAccessibilityEvent: 实时聊天界面监听....");
//                        }
                        return;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
//                        try {
//                            Thread.sleep(200); // 默认延时200毫秒 打开红包
//                        } catch (InterruptedException e){
//                            e.printStackTrace();
//                        }
                        if (isWeiChatMessage){ // 红包消息
                            weiChatMessage.HbMessageProcessing(getNodeInfo());
                        }else{ //普通消息
                            weiChatMessage.commonMessageProcessing(getNodeInfo());
                        }
                        isDealWeiChatMessage = false;
                    }
                }
                TLog.log(TAG,"TYPE_WINDOW_CONTENT_CHANGED "+className);
                break;
        }

    }

    @Override
    public AccessibilityNodeInfo getNodeInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            return getRootInActiveWindow();
        }else{
            return null;
        }
    }

    private boolean isWeiChatMessage = false; // 区别 是红包还是普通消息
    private boolean isDealWeiChatMessage = false; // 是否有消息需要被处理
    private boolean isSaveRecordInfo = false;//是否执行答谢回复
    private boolean isBackLuckymoney = false;//是否在红包详情页面执行返回按钮
    @Override
    public void notifictionMessageIntent(boolean isHB){
        isWeiChatMessage = isHB;
        isDealWeiChatMessage = true;
    }


    private boolean isHBInterfacemessage = false;
    @Override
    public void getHBInterfaceMessage() {
        isHBInterfacemessage = true;
    }

    @Override
    public void setIsDxluckyMoney(boolean bon) {
        isSaveRecordInfo = bon;
    }

    @Override
    public void setIsBackluckyMoney(boolean bon) {
        this.isBackLuckymoney = bon;
    }


    private static void ChangeAccessibility(AccessibilityNodeInfo info){
        if (info != null) {
            if (info.getChildCount() == 0) {
                TLog.log(TAG, "log_classname:" + info.getClassName());
                TLog.log(TAG, "log_content:" + info.getText());
                TLog.log(TAG, "log_PackageName:"+info.getPackageName());
            } else {
                for (int i = 0; i < info.getChildCount(); i++) {
                    if (info.getChild(i) != null) {
                        ChangeAccessibility(info.getChild(i));
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        auxiliaryFunctionSwitch.setFuncitonSwitchInfo(AuxiliaryConstans.C_HB,false);//服务被关闭 保存状态 关闭抢红包开关
    }

}
