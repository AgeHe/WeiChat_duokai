package com.zhushou.weichat.utils;

import android.content.Context;

/**
 * Created by Administrator on 2017/9/25.
 */

public class WeiChatVersionCodeSp {

    public static final String WX_VERSIONCODE_SP = "wx.version.code.sp";

    public static void saveVersionCode(Context context,String versionCode){
        SharedPreferencesUtils.setParam(context,WX_VERSIONCODE_SP,versionCode);
    }


    public static String getVersionCode(Context context){
        return (String) SharedPreferencesUtils.getParam(context,WX_VERSIONCODE_SP,"");
    }

}
