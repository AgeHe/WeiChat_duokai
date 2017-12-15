package com.zhushou.weichat.utils.brodcastr;

import android.content.Context;
import android.content.Intent;

import com.zhushou.weichat.bean.UserInfo;
import com.zhushou.weichat.constant.BrodcastConstant;
import com.zhushou.weichat.utils.DebugLog;


/**
 * Created by Administrator on 2017/5/17.
 */

public class UIHelper {


    /**
     * 发送更新用户数据广播
     * @param context
     * @param balanceNumber
     * @param luckbagNumber
     */
    public static void sendBrodcastUserMsg(Context context, String balanceNumber, String luckbagNumber){
        //创建意图对象
        Intent intent = new Intent();
        //指定发送广播的频道
        intent.setAction(BrodcastConstant.BROADCAST_ACTION_UPDATE);
        //限制接收广播的包名
        intent.setPackage(BrodcastConstant.INTENT_BROADCAST_PACKAGE);
        if (balanceNumber!=null){
//            intent.putExtra(BrodcastConstant.BROADCAST_TYPE_KEY, BrodcastConstant.UPDATE_BALANCE);
            intent.putExtra(BrodcastConstant.BROADCAST_VALUE_KEY,balanceNumber);
            //发送
            context.sendBroadcast(intent);
        }
        if (luckbagNumber!=null){
//            intent.putExtra(BrodcastConstant.BROADCAST_TYPE_KEY, BrodcastConstant.UPDATE_LUCKBAG);
            intent.putExtra(BrodcastConstant.BROADCAST_VALUE_KEY,luckbagNumber);
            //发送
            context.sendBroadcast(intent);
        }
    }


    public static void sendBrodCastPaySuccess(Context context,UserInfo userInfo){
        //创建意图对象
        Intent intent = new Intent();
        //指定发送广播的频道
        intent.setAction(BrodcastConstant.BROADCAST_ACTION_PAYSTATU);
        //限制接收广播的包名
        intent.setPackage(BrodcastConstant.INTENT_BROADCAST_PACKAGE);
        intent.putExtra(BrodcastConstant.BROADCAST_TYPE_KEY, BrodcastConstant.PAY_SUCCESS_STATUS);
        intent.putExtra(BrodcastConstant.BROADCAST_VALUE_KEY, userInfo);
        //发送
        context.sendBroadcast(intent);
    }

    /**
     * 发送用户登录状态
     * @param context
     * @param loginStatus
     */
    public static void sendBrodCastLoginStatus(Context context,boolean loginStatus){
        //创建意图对象
        Intent intent = new Intent();
        //指定发送广播的频道
        intent.setAction(BrodcastConstant.BROADCAST_ACTION_LOGIN);
        //限制接收广播的包名
        intent.setPackage(BrodcastConstant.INTENT_BROADCAST_PACKAGE);
        intent.putExtra(BrodcastConstant.BROADCAST_TYPE_KEY, BrodcastConstant.USER_LOGIN_STATUS);
        intent.putExtra(BrodcastConstant.BROADCAST_VALUE_KEY,loginStatus);
        //发送
        context.sendBroadcast(intent);
    }
    /**
     * 发送VPN开启关闭状态
     * @param context
     * @param vpnStatus
     */
    public static void sendBrodCastVpnStatus(Context context,boolean vpnStatus){
        DebugLog.e("sendBrodCastVpnStatus",vpnStatus);
        //创建意图对象
        Intent intent = new Intent();
        //指定发送广播的频道
        intent.setAction(BrodcastConstant.BROADCAST_ACTION_VPNCHANGE);
        //限制接收广播的包名
        intent.setPackage(BrodcastConstant.INTENT_BROADCAST_PACKAGE);
        intent.putExtra(BrodcastConstant.BROADCAST_TYPE_KEY, BrodcastConstant.VPN_CHANGE_STATUS);
        intent.putExtra(BrodcastConstant.BROADCAST_VALUE_KEY,vpnStatus);
        //发送
        context.sendBroadcast(intent);
    }

}
