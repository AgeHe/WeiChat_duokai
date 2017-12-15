package com.zhushou.weichat.api;

import android.os.Handler;


import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/26.
 */

public class OpenRedRequest {

    /**
     * 获取应用基础信息
     * @param handlerWhat
     * @param mHandler
     */
    public static void requestBaseAppInfo(int handlerWhat, Handler mHandler){
        HttpClient.get(RequestHost.AppBaseHostUrl, RequestHost.AppBaseParam, null, handlerWhat, mHandler);
    }

    /**
     * 版本更新
     * @param handlerWhat
     * @param mHandler
     */
    public static void requestUpdateStatus(int handlerWhat, Handler mHandler){
        HttpClient.get(RequestHost.AppBaseHostUrl, RequestHost.AppUpdateParam, null, handlerWhat, mHandler);
    }

    /**
     * 获取微信支付类型
     * @param handlerWhat
     * @param mHandler
     */
    public static void requestWxPayTypeMsg(int handlerWhat, Handler mHandler){
        HttpClient.get(RequestHost.AppBaseHostUrl, RequestHost.AppPayParam, null, handlerWhat, mHandler);
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

    /**
     * 注册
     * @param logintype
     * @param openid
     * @param nickname
     * @param face
     * @param device
     * @param type
     * @param handlerWhat
     * @param mHandler
     */
    public static void registerGet(String logintype,String openid,String nickname,String face,String device,String type,int handlerWhat,Handler mHandler){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("logintype",logintype);
        map.put("openid", openid);
        map.put("nickname", nickname);
        map.put("face", face);
//        map.put("device",device);
//        map.put("type",type);
        HttpClient.get(RequestHost.AppInfoHost, "member/register", map, handlerWhat, mHandler);

    }

    /**
     * 登录
     * @param openid
     * @param handlerWhat
     * @param mHandler
     */
    public static void loginGet(String openid,int handlerWhat,Handler mHandler){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("openid", openid);
        HttpClient.get(RequestHost.AppInfoHost, "member/login", map, handlerWhat, mHandler);
    }

//    /**
//     * 账单记录  包括消费记录 获奖记录
//     * @param userId
//     * @param handlerWhat
//     * @param mHandler
//     */
//    public static void userBillRecords(String userId,int handlerWhat,Handler mHandler){
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("userid", userId);
//        HttpClient.get(RequestHost.AppInfoHost, "user_consume", map, handlerWhat, mHandler);
//    }

//    /**
//     * 账单记录  包括消费记录 获奖记录
//     * @param userId
//     * @param handlerWhat
//     * @param mHandler
//     */
//    public static void userBillRecords(String userId,int handlerWhat,Handler mHandler){
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("userid", userId);
//        HttpClient.get(RequestHost.AppInfoHost, "user_bill", map, handlerWhat, mHandler);
//    }


//    /**
//     * 提现记录
//     * @param userId
//     * @param handlerWhat
//     * @param mHandler
//     */
//    public static void userBillRecordsTX(String userId,int handlerWhat,Handler mHandler){
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("userid", userId);
//        HttpClient.get(RequestHost.AppInfoHost, "user_payOther", map, handlerWhat, mHandler);
//    }


//    /**
//     * 投诉接口  支付成功功能未开启，申请退款，问题咨询
//     * @param hashMap
//     * @param handlerwhat
//     * @param mHandler
//     */
//    public static void sendProblemRequest(HashMap<String, String> hashMap, int handlerwhat, Handler mHandler){
//        HttpClient.get(RequestHost.AppInfoHost, "user_addcomplain", hashMap, handlerwhat, mHandler);
//    }

    /**
     * 获取用户投诉列表
     * @param hashMap
     * @param handlerwhat
     * @param mHandler
     */
    public static void arrayComplaintsRequest(HashMap<String, String> hashMap,int handlerwhat, Handler mHandler){
        HttpClient.get(RequestHost.AppInfoHost, "user_querycomplain", hashMap, handlerwhat, mHandler);
    }

    public static void txRequest(String userid,String openid,String orderamt,int handlerwhat,Handler mHandler){
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userid", userid);
        hashMap.put("openid", openid);
        hashMap.put("orderamt", orderamt);
        HttpClient.get(RequestHost.AppInfoPayHost, "weixin_another", hashMap, handlerwhat, mHandler);

    }

    /**
     *
     * @param userid 用户id
     * @param orderAmt 订单金额
     * @param payType 支付方式 代码  weixin alipay bd 其中bd 为补单开启 没有订单号 所以自己定义成 bd+20170101091223 固定格式时间
     * @param orderNo 订单号
     * @param handlerWhat
     * @param mHandler
     */
    public static void openPacket_5(String userid,String orderAmt,String payType,String orderNo,int handlerWhat,Handler mHandler){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("orderamt", orderAmt);
        map.put("orderno", orderNo);
        map.put("paytype", payType);
        HttpClient.get(RequestHost.AppInfoHost, "redpacket_5", map, handlerWhat, mHandler);
    }
    public static void openPacket_10(String userid,String orderAmt,String payType,String orderNo,int handlerWhat,Handler mHandler){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("orderamt", orderAmt);
        map.put("orderno", orderNo);
        map.put("paytype", payType);
        HttpClient.get(RequestHost.AppInfoHost, "redpacket_10", map, handlerWhat, mHandler);
    }
    public static void openPacket_20(String userid,String orderAmt,String payType,String orderNo,int handlerWhat,Handler mHandler){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("orderamt", orderAmt);
        map.put("orderno", orderNo);
        map.put("paytype", payType);
        HttpClient.get(RequestHost.AppInfoHost, "redpacket_20", map, handlerWhat, mHandler);
    }
    public static void openPacket_30(String userid,String orderAmt,String payType,String orderNo,int handlerWhat,Handler mHandler){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("orderamt", orderAmt);
        map.put("orderno", orderNo);
        map.put("paytype", payType);
        HttpClient.get(RequestHost.AppInfoHost, "redpacket_30", map, handlerWhat, mHandler);
    }
    public static void openPacket_50(String userid,String orderAmt,String payType,String orderNo,int handlerWhat,Handler mHandler){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("orderamt", orderAmt);
        map.put("orderno", orderNo);
        map.put("paytype", payType);
        HttpClient.get(RequestHost.AppInfoHost, "redpacket_50", map, handlerWhat, mHandler);
    }
    public static void openPacket_100(String userid,String orderAmt,String payType,String orderNo,int handlerWhat,Handler mHandler){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("orderamt", orderAmt);
        map.put("orderno", orderNo);
        map.put("paytype", payType);
        HttpClient.get(RequestHost.AppInfoHost, "redpacket_100", map, handlerWhat, mHandler);
    }
}
