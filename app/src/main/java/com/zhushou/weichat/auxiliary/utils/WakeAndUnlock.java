package com.zhushou.weichat.auxiliary.utils;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import com.zhushou.weichat.VApp;

/**
 * Created by Administrator on 2016/11/7.
 */
public class WakeAndUnlock {

    private static final String TAG = "WakeAndUnlock";

    //锁屏、唤醒相关
    private KeyguardManager km;

    private PowerManager pm;

//    private KeyguardManager.KeyguardLock kl;
//    private PowerManager.WakeLock wl;

    private Context context;

    public WakeAndUnlock(Context context) {
        this.context = context;
    }

    public void wakeAndUnlock1(boolean b) {
//        PowerManager.WakeLock wl = MyApplication.getInstance().getWl();
//        KeyguardManager.KeyguardLock kl = MyApplication.getInstance().getKl();
        PowerManager.WakeLock wl;
        KeyguardManager.KeyguardLock kl;
//        if (kl==null||wl==null||MyApplication.getInstance().isRefreshScreenController()){
        //获取电源管理器对象
//            pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
//            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
//            wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
//            MyApplication.getInstance().setWl(wl);
//            //得到键盘锁管理器对象
//            km= (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
//            kl = km.newKeyguardLock("bright");
//            MyApplication.getInstance().setKl(kl);
//            MyApplication.getInstance().setIsRefreshScreenController(false); // 刷新屏幕控制器刷新完毕  恢复false状态

//        }

        if (b) {
            pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
            wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            VApp.getInstance().setWl(wl);
            //得到键盘锁管理器对象
            km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            kl = km.newKeyguardLock("bright");
            VApp.getInstance().setKl(kl);

//            MyApplication.getInstance().setIsRefreshScreenController(false); // 刷新屏幕控制器刷新完毕  恢复false状态
//            KeyguardManager.KeyguardLock kl = MyApplication.getInstance().getKl();
//            PowerManager.WakeLock wl = MyApplication.getInstance().getWl();
            //解锁
            kl.disableKeyguard();
            //点亮屏幕
            wl.acquire();
//            wl.release();
        } else {
            Log.i(TAG, "wakeAndUnlock: 锁屏");
            wl = VApp.getInstance().getWl();
            kl = VApp.getInstance().getKl();
            //锁屏
            kl.reenableKeyguard();

//            //点亮屏幕
//            wl.acquire();
            //释放屏幕常亮锁
            if (null != wl) {
                wl.release();
            }

        }
    }

    public void wakeAndUnlock(boolean b, PowerManager.WakeLock wl, KeyguardManager.KeyguardLock kl) {
        DevicePolicyManager policyManager = VApp.getInstance().getPolicyManager();
        if (b) {

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

            pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
            wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "");
            //点亮屏幕
            wl.acquire();
            //得到键盘锁管理器对象
            km= (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
            kl = km.newKeyguardLock("");
            //解锁
            kl.disableKeyguard();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                wl.release(PowerManager.ON_AFTER_RELEASE);
            }else{
                    wl.release();
            }


//            //获取电源管理器对象
//            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//            //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
//            PowerManager.WakeLock wl1 = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
//            //点亮屏幕
//            wl1.acquire();
//            if (wl.isHeld())
//                wl.release();
//            //得到键盘锁管理器对象
//            KeyguardManager km1 = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
//            //参数是LogCat里用的Tag
//            KeyguardManager.KeyguardLock kl1 = km1.newKeyguardLock("bright");
////            //锁屏
////            kl.reenableKeyguard();
//            //解锁</pre><br><br>
//            kl1.disableKeyguard();




        } else {
            if (policyManager != null) {
                policyManager.resetPassword("", 0);
                policyManager.lockNow();//直接锁屏
            } else {
                Log.i(TAG, "wakeAndUnlock: 设备管理器为空");
            }
            if (kl != null) {
                //锁屏
                kl.reenableKeyguard();
            }
            if (wl != null) {
                if (wl.isHeld())
                    wl.release();
            }
        }
    }
}
