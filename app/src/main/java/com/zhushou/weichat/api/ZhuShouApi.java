package com.zhushou.weichat.api;

import android.os.Handler;
import android.os.Message;

import com.zhushou.weichat.constant.RequestHost;
import com.zhushou.weichat.utils.DebugLog;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/13.
 */

public class ZhuShouApi {

    /**
     * 用户登陆
     *
     * @param openid
     * @param nickname
     * @param handlerWhat
     * @param mHandler
     */
    public static void loginGet(String openid, String nickname,String face, int handlerWhat, Handler mHandler) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("openid", openid);
        map.put("nickname", nickname);
        map.put("face", face);
        HttpClient.get(RequestHost.UserHostUrl, "client_user_login", map, handlerWhat, mHandler);
    }

    /**
     * 用户注册
     *
     * @param openid
     * @param nickname
     * @param handlerWhat
     * @param mHandler
     */
    public static void registerGet(String openid, String nickname,String face, int handlerWhat, Handler mHandler) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("openid", openid);
        map.put("nickname", nickname);
        map.put("face", face);
        HttpClient.get(RequestHost.UserHostUrl, "client_user_register", map, handlerWhat, mHandler);
    }



    /**
     * 应用基础信息拉取
     *
     * @param handlerWhat
     * @param mHandler
     */
    public static void appBaseInfoGet(int handlerWhat, Handler mHandler) {
        HttpClient.get(RequestHost.AppHostUrl, RequestHost.AppBaseParam, null, handlerWhat, mHandler);
    }


    /**
     * 应用广告信息拉取
     *
     * @param handlerWhat
     * @param mHandler
     */
    public static void appAdvInfoGet(int handlerWhat, Handler mHandler){
        HttpClient.get(RequestHost.AppHostUrl, RequestHost.AppAdvParam, null, handlerWhat, mHandler);
    }


    /**
     * 东方头条  头条新闻
     * @param handlerWhat
     * @param mHandler
     */
    public static void getArrayDFTTAdv(int handlerWhat, Handler mHandler){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("qid","qid02516");
        HttpClient.get(RequestHost.DFTT_NEWS_HOST, RequestHost.DFTT_NEWS, hashMap, handlerWhat, mHandler);
    }


    /**
     * 获取网络时间
     * @param handlerWhat
     * @param mHandler
     */
    public static void getInternetTime(final int handlerWhat, final Handler mHandler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long ld = 0;
                URL url = null;//取得资源对象
                try {
                    url = new URL("http://www.baidu.com");
                    URLConnection uc = url.openConnection();//生成连接对象
                    uc.connect(); //发出连接
                    ld = uc.getDate(); //取得网站日期时间
                    DebugLog.i("getInternetTime", "ld---->>>>" + ld);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = handlerWhat;
                msg.obj = ld;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 微信登录
     * 第二步：通过code获取access_token
     * @param appid
     * @param sectet
     * @param code
     * @param handlerWhat
     * @param mHandler
     */
    public static void getWxAccess_token(String appid,String sectet,String code,int handlerWhat,Handler mHandler){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("appid",appid);
        hashMap.put("secret",sectet);
        hashMap.put("code",code);
        hashMap.put("grant_type","authorization_code");
        HttpClient.post("https://api.weixin.qq.com/sns/oauth2","access_token",hashMap,handlerWhat,mHandler);
    }

    /**
     * 微信登录
     * 刷新用户登录的 access_token有效期
     * @param appid
     * @param refresh_token
     * @param handlerWhat
     * @param mHandler
     */
    public static void refreshAccess_token(String appid,String refresh_token,int handlerWhat,Handler mHandler){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("appid",appid);
        hashMap.put("refresh_token",refresh_token);
        hashMap.put("grant_type","refresh_token");
        HttpClient.post("https://api.weixin.qq.com/sns/oauth2","refresh_token",hashMap,handlerWhat,mHandler);
    }

    /**
     * 微信登录
     * 通过 用户openid  access_token  获取用户信息
     * @param openId
     * @param access_token
     * @param handlerWhat
     * @param mHandler
     */
    public static void getWxUserInfo(String openId,String access_token,int handlerWhat,Handler mHandler){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("access_token",access_token);
        hashMap.put("openid",openId);
        HttpClient.post("https://api.weixin.qq.com/sns","userinfo",hashMap,handlerWhat,mHandler);
    }

}
