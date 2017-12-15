package com.zhushou.weichat.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryConstans;
import com.zhushou.weichat.auxiliary.sp.SPAuxiliaryPayUtils;
import com.zhushou.weichat.bean.HistoryPayInfo;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.bean.OrderDBInfo;
import com.zhushou.weichat.bean.UMengStatisticalKey;
import com.zhushou.weichat.bean.WXSDKParamsJsonInfo;
import com.zhushou.weichat.bean.WxSdkQureyOrderInfo;
import com.zhushou.weichat.ui.ListAppContract;
import com.zhushou.weichat.ui.MainActivity;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;
import com.zhushou.weichat.utils.sqlite.SqliteDo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class MyBackCheckService extends Service {

    public static final String TAG = "MyBackCheckService";
    private Context mContext;
    private long totalTime = 2 * 60 * 60 * 1000;//两个小时
    private long interval = 60 * 1000;//1分钟
    private CountDownTimer timer;
    private ArrayList<OrderDBInfo> list;
    private SqliteDo sqliteDo;
    private NotificationManager mNotificationManager;
    private String queryUrl = "http://api.dyj1888.com/cgi/pay.ashx/order";
    private HashMap<String, String> paramMap = new HashMap<>();
    private int notificationId = 2333;
    int i = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        sqliteDo = new SqliteDo(this);
        mContext = this;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        list = sqliteDo.getNeedCheckList();
        Notification noti = new Notification.Builder(mContext)
                .setContentTitle("重要通知")
                .setContentText("您有订单在后台自动确认...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        startForeground(notificationId, noti);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {//当接到startservice后 就拿出要查询的列表
        list = sqliteDo.getNeedCheckList();
        if (list != null && list.size() != 0) {//有需要查的数据
            if (timer == null) {//定时器没有被创建过
                timer = new CountDownTimer(totalTime, interval) {// 第一个参数是总共时间，第二个参数是间隔触发时间
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long minuteTime = Math.round((totalTime - millisUntilFinished) / 1000 / 60);
                        if (minuteTime == 0 || minuteTime == 1 || minuteTime == 2 || minuteTime == 4 || minuteTime == 8 || minuteTime == 15 ||
                                minuteTime == 30 || minuteTime == 60 || minuteTime == 118) {
                            list = sqliteDo.getNeedCheckList();
                            for (OrderDBInfo info : list) {//循环list中的数据 去开线程查
                                if (info != null) {
                                    requestOrder(info, info.getPaytype().equals(VApp.PAY_WXSDK) ? getWXSDKParamsJson(info.getOtherdata()) : null);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        onDestroy();
                    }
                };
                timer.start();
            } else {//如果被创建过就刷新定时器重新开始
                timer.cancel();
                timer.start();
            }
        } else {
            onDestroy();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (timer!=null)
            timer.cancel();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void requestOrder(OrderDBInfo requestParamInfo, WXSDKParamsJsonInfo finalWxSdkOrderInfo) {


        class HttpRequestRun implements Runnable {
            @Override
            public void run() {
                if (requestParamInfo == null)
                    return;
                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("orderno", requestParamInfo.getOrdernum());
                String payType = requestParamInfo.getPaytype().equals("alipay") ? "alipay" : "weixin";
                map.put("paytype", payType);
                OkHttpResult result = okHttpUtil.requestGetBySyn("http://api.duokai.mxitie.com/cgi/pay.ashx/order",
                        "info.json",
                        map);
                Message message = new Message();
                message.what = 0;
                Bundle mBundle = new Bundle();
                String resultStr = "";
                try {
                    JSONObject object = new JSONObject(result.msg);
                    String responseCode = object.getString("responseCode");
                    String transStatus = object.getString("transStatus");
                    if (responseCode.equals("A001") && transStatus.equals("A001")) {
                        resultStr = "SUCCESS";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mBundle.putString("result", resultStr);
                mBundle.putString("ordernum", requestParamInfo.getOrdernum());
                mBundle.putString("functionkey", requestParamInfo.getFunctionkey());
                mBundle.putString("paytype", requestParamInfo.getPaytype());
                mBundle.putFloat("orderprice", requestParamInfo.getOrderprice());
                mBundle.putInt("waretype",requestParamInfo.getWareType());
                message.setData(mBundle);
//                message.obj = result.msg+"<%>"+requestParamInfo.getFunctionkey()+"<%>"+requestParamInfo.getOrdernum();
                httpHandler.sendMessage(message);
            }
        }

        class WXSDKHttpRequestRun implements Runnable {

            @Override
            public void run() {

                if (finalWxSdkOrderInfo != null) {
                    OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                    WxSdkQureyOrderInfo resultInfo = okHttpUtil.wxpayquery(finalWxSdkOrderInfo.getStr_appid(),
                            finalWxSdkOrderInfo.getStr_partnerId(),
                            finalWxSdkOrderInfo.getStr_orderNumber(),
                            finalWxSdkOrderInfo.getStr_nonceStr(),
                            finalWxSdkOrderInfo.getOrderSign(), mContext);

//                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
//                OkHttpResult result = okHttpUtil.requestPostBySyn(url,"orderquery",payMap);
                    Message msg = new Message();
                    msg.what = 0;
                    Bundle mBundle = new Bundle();
                    mBundle.putString("result", resultInfo != null && resultInfo.getTrade_state() != null ? resultInfo.getTrade_state() : "err");
                    mBundle.putString("ordernum", requestParamInfo.getOrdernum());
                    mBundle.putString("functionkey", requestParamInfo.getFunctionkey());
                    mBundle.putString("paytype", requestParamInfo.getPaytype());
                    mBundle.putFloat("orderprice", requestParamInfo.getOrderprice());
                    mBundle.putInt("waretype",requestParamInfo.getWareType());
                    msg.setData(mBundle);
                    httpHandler.sendEmptyMessage(14);

                    httpHandler.sendMessage(msg);
                }
            }
        }
        if (requestParamInfo.getPaytype().equals(VApp.PAY_WXSDK)) {
            if (requestParamInfo.getOtherdata() != null && !requestParamInfo.getOtherdata().equals("") && finalWxSdkOrderInfo != null) {
                new Thread(new WXSDKHttpRequestRun()).start();
            } else {
                sqliteDo.changeOrderstatus(requestParamInfo.getOrdernum(), 4);
            }
        } else {
            new Thread(new HttpRequestRun()).start();
        }

    }


    private Handler httpHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle mBundle = msg.getData();
            OrderDBInfo orderInfo = new OrderDBInfo();
            orderInfo.setOrdernum(mBundle.getString("ordernum"));
            orderInfo.setPaytype(mBundle.getString("paytype"));
            orderInfo.setOrderprice(mBundle.getFloat("orderprice"));
            orderInfo.setFunctionkey(mBundle.getString("functionkey"));
            orderInfo.setWareType(mBundle.getInt("waretype"));
            switch (msg.what){
                case 0:
                    String functionkey = orderInfo.getFunctionkey();
                    if (mBundle.getString("result").equals("SUCCESS")){
                        MobclickAgent.onEvent(mContext, UMengStatisticalKey.pay_service_check_s);
                        if (functionkey == null)
                            return;
                        if (functionkey.equals(AuxiliaryConstans.WX_LifeTime)){
                            APPUsersShareUtil.setLifeTime(mContext);
                            saveHistoryList(mContext, SPAuxiliaryPayUtils.getFunctionKeyName(orderInfo.getFunctionkey()),
                                    String.valueOf(orderInfo.getOrderprice()),
                                    orderInfo.getPaytype());
                            sqliteDo.changeOrderstatus(orderInfo.getOrdernum(), 3);
                            Toast.makeText(mContext, "您的订单后台已处理完成", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (functionkey.equals(AuxiliaryConstans.WX_AllApps)){
                            LauncherUtils.saveLauncherType(mContext, ListAppContract.SET_ALL);
                            saveHistoryList(mContext, SPAuxiliaryPayUtils.getFunctionKeyName(orderInfo.getFunctionkey()),
                                    String.valueOf(orderInfo.getOrderprice()),
                                    orderInfo.getPaytype());
                            sqliteDo.changeOrderstatus(orderInfo.getOrdernum(), 3);
                            Toast.makeText(mContext, "您的订单后台已处理完成,请关闭应用重新打开", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (functionkey.equals(AuxiliaryConstans.WX_Single)) {
                            MainActivity mainActivity = VApp.getInstance().getaActivity();
                            if (mainActivity != null) {
                                mainActivity.addWxMode(getNowTag());
                                //订单已完成
                                sqliteDo.changeOrderstatus(orderInfo.getOrdernum(), 3);
                                saveHistoryList(mContext, SPAuxiliaryPayUtils.getFunctionKeyName(orderInfo.getFunctionkey()),
                                        String.valueOf(orderInfo.getOrderprice()),
                                        orderInfo.getPaytype());
                                Toast.makeText(mContext, "您的订单后台已处理完成！", Toast.LENGTH_SHORT).show();
                            } else {
                                //不在主界面，先保存订单支付成功状态，下次进入主界面时执行并提示用户
                                sqliteDo.changeOrderstatus(orderInfo.getOrdernum(), 2);
                            }
                            return;
                        }

                        if (orderInfo.getWareType()==MainActivity.svipAllType){
                            SPAuxiliaryPayUtils.setWsSVIPFunctionAll(mContext);
                            saveHistoryList(mContext, "SVIP全部功能",
                                    String.valueOf(orderInfo.getOrderprice()),
                                    orderInfo.getPaytype());
                            sqliteDo.changeOrderstatus(orderInfo.getOrdernum(), 3);
                            Toast.makeText(mContext, "您的订单后台已处理完成,请重启应用后查看", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (orderInfo.getWareType()==MainActivity.lifeTimeType){
                            SPAuxiliaryPayUtils.setWsFunctionScreenAll(mContext);
                            saveHistoryList(mContext, "所有生成器功能",
                                    String.valueOf(orderInfo.getOrderprice()),
                                    orderInfo.getPaytype());
                            sqliteDo.changeOrderstatus(orderInfo.getOrdernum(), 3);
                            Toast.makeText(mContext, "您的订单后台已处理完成", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (functionkey.equals(AuxiliaryConstans.C_ALLHF)) {
                            SPAuxiliaryPayUtils.setWsFunctionHFAll(mContext);
                            saveHistoryList(mContext, SPAuxiliaryPayUtils.getFunctionKeyName(orderInfo.getFunctionkey()),
                                    String.valueOf(orderInfo.getOrderprice()),
                                    orderInfo.getPaytype());
                            sqliteDo.changeOrderstatus(orderInfo.getOrdernum(), 3);
                            Toast.makeText(mContext, "您的订单后台已处理完成", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (functionkey.equals(AuxiliaryConstans.C_JHYALL)){
                            SPAuxiliaryPayUtils.setWsAddFriendAll(mContext);
                            saveHistoryList(mContext, SPAuxiliaryPayUtils.getFunctionKeyName(orderInfo.getFunctionkey()),
                                    String.valueOf(orderInfo.getOrderprice()),
                                    orderInfo.getPaytype());
                            sqliteDo.changeOrderstatus(orderInfo.getOrdernum(), 3);
                            Toast.makeText(mContext, "您的订单后台已处理完成", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (functionkey.equals(AuxiliaryConstans.C_SVIP)){
                            SPAuxiliaryPayUtils.setWsSVIPFunctionAll(mContext);
                            saveHistoryList(mContext, SPAuxiliaryPayUtils.getFunctionKeyName(orderInfo.getFunctionkey()),
                                    String.valueOf(orderInfo.getOrderprice()),
                                    orderInfo.getPaytype());
                            sqliteDo.changeOrderstatus(orderInfo.getOrdernum(), 3);
                            Toast.makeText(mContext, "您的订单后台已处理完成,请重启应用后查看", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //微商功能开启，提示用户
                        SPAuxiliaryPayUtils.setAuxiliaryMerchant(mContext, functionkey);
                        sqliteDo.changeOrderstatus(orderInfo.getOrdernum(), 3);
//                        Toast.makeText(mContext, orderInfo.getFunctionkey(), Toast.LENGTH_SHORT).show();
                        saveHistoryList(mContext, SPAuxiliaryPayUtils.getFunctionKeyName(orderInfo.getFunctionkey()),
                                String.valueOf(orderInfo.getOrderprice()),
                                orderInfo.getPaytype());
                        Toast.makeText(mContext, "您的订单后台已处理完成,请重启应用后查看", Toast.LENGTH_SHORT).show();
                    } else {
                        MobclickAgent.onEvent(mContext, UMengStatisticalKey.pay_service_check_f);
                        sqliteDo.changeOrderstatus(orderInfo.getOrdernum(), 1);
                    }

                case 1:
//
                    break;

            }

        }
    };

    public static String getNowTag() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd HH:mm");
        Date date = new Date(System.currentTimeMillis());
        String result = formatter.format(date);
        return result == null ? "" : formatter.format(date);
    }

    public static void saveHistoryList(Context context, String name, String number, String paytype) {
        HistoryPayInfo info = new HistoryPayInfo();
        info.setPayDate(HistoryPayShareUtil.getSystemTime(VApp.DATE_NOSECOND));
        info.setPayName(name);
        info.setPayNumber(number);
        info.setPayType(paytype);
        HistoryPayShareUtil.setHistoryPayList(context, info);
    }

    public static String setWXSDKParamsJson(String str_appid, String str_partnerId, String str_nonceStr, String str_orderNumber) {
        String str_signs = "appid=" + str_appid + "&mch_id=" + str_partnerId + "&nonce_str=" + str_nonceStr + "&out_trade_no=" + str_orderNumber + "&key=865dd0e6577dc1031fd7cf2fb10e2c3e";
        String queryOrderSign = com.zhushou.weichat.utils.MD5.getMessageDigest(str_signs.getBytes()).toUpperCase();
        JSONObject jo = new JSONObject();
        try {
            jo.put("str_appid", str_appid);
            jo.put("str_partnerId", str_partnerId);
            jo.put("str_nonceStr", str_nonceStr);
            jo.put("str_orderNumber", str_orderNumber);
            jo.put("orderSign", queryOrderSign);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
        return jo.toString();
    }

    public static WXSDKParamsJsonInfo getWXSDKParamsJson(String jsonStr) {
        WXSDKParamsJsonInfo info = new WXSDKParamsJsonInfo();
        try {
            JSONObject jo = new JSONObject(jsonStr);
            info.setStr_appid(jo.getString("str_appid"));
            info.setStr_nonceStr(jo.getString("str_nonceStr"));
            info.setStr_partnerId(jo.getString("str_partnerId"));
            info.setStr_orderNumber(jo.getString("str_orderNumber"));
            info.setOrderSign(jo.getString("orderSign"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return info;
    }

}