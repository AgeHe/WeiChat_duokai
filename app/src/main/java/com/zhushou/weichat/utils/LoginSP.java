package com.zhushou.weichat.utils;

import android.content.Context;
import android.text.TextUtils;


import com.zhushou.weichat.bean.LoginMsgInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/10/30.
 */

public class LoginSP {

    public static final String LOGIN_SP = "red.login.sp";

    /**
     * 保存登录信息
     * @param context
     */
    public static void saveLoginMsg(Context context,String loginResultJo){
        SharedPreferencesUtils.setParam(context,LOGIN_SP,loginResultJo);
    }

    /**
     * 获取用户登录信息
     */
    public static LoginMsgInfo getLoginMsg(Context context) throws JSONException {
        LoginMsgInfo loginMsgInfo ;
        String loginJo = (String) SharedPreferencesUtils.getParam(context,LOGIN_SP,"");
        if (!loginJo.equals("")&&!TextUtils.isEmpty(loginJo)){
            loginMsgInfo = new LoginMsgInfo();
            JSONObject jsonObject = new JSONObject(loginJo);
            loginMsgInfo.setId(jsonObject.getString("uid"));
            loginMsgInfo.setOpenid(jsonObject.getString("openid"));
            loginMsgInfo.setLogintype(jsonObject.getString("logintype"));
            loginMsgInfo.setFace(jsonObject.getString("face"));
            loginMsgInfo.setNickname(jsonObject.getString("nickname"));
            loginMsgInfo.setBdCode(jsonObject.getString("dk_function"));
        }else{
            throw new JSONException("loginjson is null");
        }
        return loginMsgInfo;
    }

    /**
     * 清空登录信息
     * @param context
     */
    public static void clearLoginMsg(Context context){
        SharedPreferencesUtils.setParam(context,LOGIN_SP,"");
    }

}
