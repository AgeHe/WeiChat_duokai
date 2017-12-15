package com.zhushou.weichat.auxiliary;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.PowerManager;
import android.view.accessibility.AccessibilityEvent;

import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.auxiliary.interfac.BaseAccessibility;
import com.zhushou.weichat.auxiliary.interfac.NotifictionMessage;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryConstans;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryFunctionSwitch;
import com.zhushou.weichat.auxiliary.utils.WakeAndUnlock;
import com.zhushou.weichat.utils.TLog;

import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 */

public class WeiChatNotification implements NotifictionMessage {

    BaseAccessibility baseAccessibility;
    ChatAuxiliary chatAuxiliary;
    private Context context;
    private AuxiliaryFunctionSwitch auxiliaryFunctionSwitch;

    private WakeAndUnlock wakeAndUnlock;
    private PowerManager.WakeLock wl;
    private KeyguardManager.KeyguardLock kl;


    //声明键盘管理器
    KeyguardManager mKeyguardManager = null;
    //声明键盘锁
    private KeyguardManager.KeyguardLock mKeyguardLock = null;
    //声明电源管理器
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;

    public WeiChatNotification(BaseAccessibilityService baseAccessibilityService,ChatAuxiliary chatAuxiliary,Context context){
        this.baseAccessibility = baseAccessibilityService;
        this.chatAuxiliary = chatAuxiliary;
        this.context = context;
        auxiliaryFunctionSwitch = new AuxiliaryFunctionSwitch(context);
        wakeAndUnlock = new WakeAndUnlock(context);
    }

    @Override
    public void weichatNotificationDispense(AccessibilityEvent event) {
        List<CharSequence> texts = event.getText();
        //模拟打开通知栏消息
        if (event.getParcelableData() == null&&!(event.getParcelableData() instanceof Notification))
            return;
        if (!texts.isEmpty()) {
            notificationClick(texts,event);
        }
    }

    public void notificationClick(List<CharSequence> texts,AccessibilityEvent event){

        //需要判断的 状态  1，锁屏状态下 抢红包和自动回复的开关  2没有锁屏状态下 抢红包和自动回复的开关

        for (CharSequence text : texts) {
            String content = text.toString();
            TLog.log("demo", "text:" + content);
            if (content.contains(VApp.resource().getString(R.string.weichat_hb_zfpz)))
                return;
            if (content.contains(VApp.resource().getString(R.string.weichat_hb_notification))){

                //抢红包开关是否开启
                if (!auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_HB))
                    return;

                //锁屏状态下 锁屏自动回复开关没有打开
                if (!VApp.getInstance().isLockScreenStatus()&&!auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_XPHB))
                    return;

                //判断是否锁屏状态
                if (!VApp.getInstance().isLockScreenStatus()){
                    openScreen();//打开屏幕
//                    chatAuxiliary.screenStatus(false);
                }
//                else{
//                    chatAuxiliary.screenStatus(true);
//                }

                //红包消息
                try {
                    Notification notification = (Notification) event.getParcelableData();
                    baseAccessibility.notifictionMessageIntent(true);
                    notification.contentIntent.send();
                } catch (PendingIntent.CanceledException e){
                    e.printStackTrace();
                }
            } else {
                //自动回复开关是否开启
                if (!auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_HF))
                    return;

                //锁屏状态下 息屏自动回复 是否打开
                if (!VApp.getInstance().isLockScreenStatus()&&!auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_XPHF))
                    return;

                //是否是锁屏状态
                if (!VApp.getInstance().isLockScreenStatus()){
                    openScreen();//打开屏幕
//                    chatAuxiliary.screenStatus(false);
                }
//                else{
//                    chatAuxiliary.screenStatus(true);
//                }


                    //普通消息
                try {
                    Notification notification = (Notification) event.getParcelableData();
                    baseAccessibility.notifictionMessageIntent(false);
                    notification.contentIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void openScreen(){
//        if (!VApp.getInstance().isLockScreenStatus()){  //是否锁屏状态
        wakeAndUnlock.wakeAndUnlock(true,wl,kl); // 解锁

//            //获取电源的服务
//            pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//            //获取系统服务
//            mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
//            //点亮亮屏
//            wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "MyTag");
//            wakeLock.acquire();
//            TLog.log("Log : ", "------>mKeyguardLock");
//            //初始化键盘锁，可以锁定或解开键盘锁
//            mKeyguardLock = mKeyguardManager.newKeyguardLock("MyTag");
//            //禁用显示键盘锁定
//            mKeyguardLock.disableKeyguard();
//            wakeLock.release();

    }

}
