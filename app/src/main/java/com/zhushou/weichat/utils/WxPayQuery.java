package com.zhushou.weichat.utils;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/5/20.
 */

public class WxPayQuery {
    public HashMap<String,String> payMap = new HashMap<>();
    public String queryUrl = "https://api.mch.weixin.qq.com/pay/orderquery";

    private String str_appid;
    private String str_partnerId;
    private String str_orderNumber;
    private String str_prepayId;
    private String queryOrderSign;

    public WxPayQuery(String str_appid,String str_partnerId,String str_orderNumber,String str_prepayId,String queryOrderSign){
//        payMap.put("appid",str_appid);//应用APPID
//        payMap.put("mch_id",str_partnerId);//商户号
//        payMap.put("out_trade_no",str_orderNumber);//商户订单号
//        payMap.put("nonce_str",str_prepayId);//随机字符串
//        payMap.put("sign",queryOrderSign);//签名
        this.str_appid = str_appid;
        this.str_partnerId = str_partnerId;
        this.str_orderNumber = str_orderNumber;
        this.str_prepayId = str_prepayId;
        this.queryOrderSign = queryOrderSign;
    }
      class httpThread implements Runnable{

        @Override
        public void run() {

        }
    }
}
