package com.zhushou.weichat.constant;

/**
 * Created by Administrator on 2017/9/15.
 */

public class BrodcastConstant {

    public static final String  INTENT_BROADCAST_PACKAGE = "com.zhushou.weichat";

    /**
     *广播类型
     */
    public static final String BROADCAST_ACTION_UPDATE = "zhushou.userinfo";
    public static final String BROADCAST_ACTION_LOGIN = "zhushou.login";
    public static final String BROADCAST_ACTION_VPNCHANGE = "zhushou.vpn.change";
    public static final String BROADCAST_ACTION_PAYSTATU = "zhushou.pay.change";

    /**
     * 广播键
     */
    public static final String BROADCAST_TYPE_KEY = "typeKey";
    public static final String BROADCAST_VALUE_KEY = "keyValue";

    /**
     * 广播值类型
     */
//    public static final int UPDATE_BALANCE = 0x01;
//    public static final int UPDATE_LUCKBAG = 0x02;
    public static final int USER_LOGIN_STATUS = 0x03;
    public static final int VPN_CHANGE_STATUS = 0x04;
    public static final int PAY_SUCCESS_STATUS = 0x05;
}
