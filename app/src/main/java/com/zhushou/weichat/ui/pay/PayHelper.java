package com.zhushou.weichat.ui.pay;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.lbsw.stat.LBStat;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.bean.NewPayDialogInfo;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.constant.PayConstant;
import com.zhushou.weichat.ui.MainActivity;
import com.zhushou.weichat.ui.NbzleaActivity;
import com.zhushou.weichat.uinew.info.ComsuptionInfo;
import com.zhushou.weichat.utils.CountUtil;
import com.zhushou.weichat.utils.DebugLog;
import com.zhushou.weichat.utils.HistoryPayShareUtil;
import com.zhushou.weichat.utils.SharedPreferencesUtils;
import com.zhushou.weichat.utils.database.DBLibs;
import com.zhushou.weichat.utils.database.OperationDB;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

import static android.content.ContentValues.TAG;

/**
 * Created by zhanglinkai on 2016/11/24.
 */

public class PayHelper {
    private static Activity activity;
    private WebView webView;
    private String qureyURL;
    private boolean isCheck;
    private boolean isstart;//?ж?webview????м??????
    private static String orderNumber;
    private int type=-1, waretype;
    private ProgressDialog lodingDialog;
    private String num;//支付的钱
    private String payType;
    private String payTunnel;
    private UpdatePriceInfo updatePriceInfo;
    private boolean isPayStatu =true ;
    private CountUtil countUtil;
    private OperationDB operationDB;
    public ProgressDialog selectorOrderProgressDialog; // 订单查询Dialog

    public PayHelper(final Activity activity) {
        this.activity = activity;
        updatePriceInfo = APPUsersShareUtil.getUpdateMessage(activity);
        setIsstart(false);
        operationDB = new OperationDB(activity);
        countUtil = new CountUtil(activity);
        webView = new WebView(activity);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                setIsstart(true);
                Intent intent = null;
                try {
                    if(payTunnel!=null&&payTunnel.equals("wiipay")){
                        String finalUrl = "weixin://dl/businessWebview/link/?appid="+updatePriceInfo.getAppId()+"&url="+ URLEncoder.encode(url,"UTF-8");
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setComponent(new ComponentName("com.tencent.mm",
                                "com.tencent.mm.plugin.base.stub.WXCustomSchemeEntryActivity"));
                        intent.setData(Uri.parse(finalUrl + "&scene=WXSceneSession"));
                        intent.putExtra("translate_link_scene", 1);
                        clear();
                        activity.startActivity(intent);
                    }else{
                        if (url.startsWith("alipays://")||url.startsWith("alipayqr://")){
                            Uri uri = Uri.parse(url);
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            clear();
                            activity.startActivity(intent);
                        } else if (url.startsWith("weixin://wap/pay?")){
                            intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            clear();
                            activity.startActivity(intent);
                        } else if (url.startsWith("intent://")){
                            if(payType.equals(VApp.PAYTYPE_WX)){
                                url = url.replaceFirst("intent://", "weixin://");
                                intent = new Intent(Intent.ACTION_VIEW);
                                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                intent.setComponent(new ComponentName("com.tencent.mm",
                                        "com.tencent.mm.plugin.base.stub.WXCustomSchemeEntryActivity"));
                                intent.setData(Uri.parse(url + "&scene=WXSceneSession"));
                                intent.putExtra("translate_link_scene", 1);
                                clear();
                                activity.startActivity(intent);
                            }else if(payType.equals(VApp.PAYTYPE_ZFB)){//贝付宝
                                url = url.replaceFirst("intent://", "alipays://");
                                intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                                clear();
                                activity.startActivity(intent);
                            }
                        }else if (url.startsWith("weixin://dl/business/")||url.startsWith("https://zhongxin.junka.com/MSite/Cashier/WeixinQRCodePay.aspx")) {
                            intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(url);
                            intent.setData(content_url);
                            clear();
                            activity.startActivity(intent);
                        }else if (url.startsWith("mqqapi://forward/url")){
                            intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            clear();
                            activity.startActivity(intent);
                        }else{
//                            view.loadUrl(url);
                            if(payTunnel.equals(VApp.PAY_TLWAP)){//同乐需要对请求头进行处理
                                String  referer = "";
                                Log.e(TAG, "shouldOverrideUrlLoading: "+url);
                                if(url.startsWith("https://wx.tenpay.com")){
                                    referer ="https://payh5.bbnpay.com";
                                }else{
                                    referer ="http://api.duokai.mxitie.com";
                                }
                                Map extraHeaders = new HashMap();
                                extraHeaders.put("Referer", referer);
                                view.loadUrl(url,extraHeaders);
                            }else{
                                Log.e(TAG, "shouldOverrideUrlLoading: "+url);
                                Map extraHeaders = new HashMap();
                                extraHeaders.put("Referer", view.getUrl());
                                view.loadUrl(url,extraHeaders);
                            }
                        }
                    }
//                    activity.startActivity(intent);
                } catch (Exception e) {
                    //Toast.makeText(activity, activity.getString(R.string.not_xiangguan_app), Toast.LENGTH_SHORT).show();
                    lodingDialog.dismiss();
                }
                return true;
            }
        });
        lodingDialog = new ProgressDialog(activity);
        lodingDialog.setMessage("调起支付...");
        lodingDialog.setCanceledOnTouchOutside(false);
        lodingDialog.setCancelable(true);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
    }
    private void clear(){
        lodingDialog.dismiss();
        webView.clearHistory();
//        MyDialog.dismiss();
    }
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1 && lodingDialog.isShowing() && msg.obj != null && msg.obj == getorderNumber()) {
                if (isstart == false) {
                    lodingDialog.dismiss();
                    restoreDefault();//恢复默认值
                    Toast.makeText(activity, activity.getString(R.string.tijiao_dingdan_shibai), Toast.LENGTH_SHORT).show();
                }/*else{
                    lodingDialog.dismiss();
                    restoreDefault();//???????
                    Toast.makeText(activity, "??????", Toast.LENGTH_SHORT).show();
                }*/
            }
        }
    };

    //???????
    public PayHelper lodingWeb(String url, Activity activity) {
//        if (isNetworkConnected(activity) == true) {
        this.activity = activity;
        lodingDialog.show();
        Message message = new Message();
        message.obj = getorderNumber();
        message.what = 1;
        mHandle.sendMessageDelayed(message, 10000);
        webView.loadUrl(url);
//        } else {
//            Toast.makeText(activity, "????????", Toast.LENGTH_SHORT).show();
//        }
        return this;
    }

    public void requreHttpAsync(String url, Activity payActivity) {
        if (selectorOrderProgressDialog==null){
            selectorOrderProgressDialog = new ProgressDialog(payActivity);
            selectorOrderProgressDialog.setMessage("查询订单...");
            selectorOrderProgressDialog.setCanceledOnTouchOutside(false);
            selectorOrderProgressDialog.setCancelable(true);
        }
        selectorOrderProgressDialog.show();
        Resources mResources = payActivity.getResources();
        DebugLog.e("支付查询="+url);
        OkHttpUtil.getDefault(activity).doGetAsync(HttpInfo.Builder().setUrl(url).build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(final HttpInfo info) throws IOException {
                        if (info.isSuccessful()){
                            try {
                                JSONObject object = new JSONObject(info.getRetDetail());
                                String responseCode = object.getString("responseCode");
                                String transStatus = object.getString("transStatus");
                                if (responseCode.equals("A001") && transStatus.equals("A001")) {
                                    Toast.makeText(activity, mResources.getString(R.string.zhifu_chenggong), Toast.LENGTH_SHORT).show();
                                    if (selectorOrderProgressDialog!=null&&selectorOrderProgressDialog.isShowing())
                                        selectorOrderProgressDialog.dismiss();
                                    r.successfulclick(MyDialog.getApkName() == null ? SmallMerchantPayDialog.apkName : MyDialog.getApkName(), type, waretype);
                                } else {
                                    r.failClick();
                                    if (selectorOrderProgressDialog!=null&&selectorOrderProgressDialog.isShowing())
                                        selectorOrderProgressDialog.dismiss();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                r.exceprtion();
                                if (selectorOrderProgressDialog!=null&&selectorOrderProgressDialog.isShowing())
                                    selectorOrderProgressDialog.dismiss();
                            }
                            restoreDefault();

                        } else {
                            r.exceprtion();
                            Toast.makeText(activity, mResources.getString(R.string.wangluo_yichang), Toast.LENGTH_SHORT).show();
                            if (selectorOrderProgressDialog!=null&&selectorOrderProgressDialog.isShowing())
                                selectorOrderProgressDialog.dismiss();
                        }
                    }
                });
    }

    //微信支付
    public void doWxPay(NewPayDialogInfo dateInfo, String wareName, float price, Dialog dialog){
        try {
            if (!check())
                return;
            dialog.dismiss();//原来的dialog消失
            Flowable flowable = Flowable.create(new FlowableOnSubscribe<OkHttpResult>() {
                @Override
                public void subscribe(FlowableEmitter<OkHttpResult> e) throws Exception {
                    com.zhushou.weichat.utils.OkHttpUtil okHttpUtil = com.zhushou.weichat.utils.OkHttpUtil.getInstance();
                    OkHttpResult result = okHttpUtil.requestGetBySyn(VApp.URLCX_PAY, null, null);
                    e.onNext(result);
                    e.onComplete();
                }
            }, BackpressureStrategy.DROP).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            ResourceSubscriber resultSubscriber = new ResourceSubscriber<OkHttpResult>() {
                @Override
                public void onNext(OkHttpResult result) {
                    if (result != null && result.isSuccess) {
                        try {
                            JSONObject jo = new JSONObject(result.msg);
                            if (jo != null) {
                                String type = jo.getString(countUtil.getApp());
                                if (type.equals(VApp.PAY_WAP)) {//普通wap
                                    StringBuilder requseUrl =new StringBuilder(VApp.PAY_URL)
                                            .append("orderno=").append(setOrderId()).append( "&")
                                            .append("ordername=").append(URLEncoder.encode(wareName, "UTF-8")).append( "&")
                                            .append(  "orderamt=").append((int) Math.ceil(price * 100)).append( "&").append("paytype=weixin");
                                    StringBuilder requreUrl =new StringBuilder(VApp.PAY_CHECK_URL)
                                            .append("orderno=").append( getorderNumber()).append( "&paytype=weixin");
                                    lodingWeb(requseUrl.toString(), activity)
                                            .setQureyURL(requreUrl.toString())
                                            .setCheck(true)
                                            .setPayType(VApp.PAYTYPE_WX)
                                            .setNum(String.valueOf(price))
//                                            .setWaretype(wareType)
                                            .setPayTunnel("web");
                                    SharedPreferencesUtils.saveOrderInfo(activity, getorderNumber(), false, waretype, "weixin", String.valueOf(num));
                                    LBStat.pay(getPayType(),
                                            getorderNumber(),
                                            false,
                                            PayConstant.getPayDescribeStr(dateInfo.getFunctionKey()),
                                            Float.valueOf(getNum()),
                                            type);
                                }  else if (type.equals(VApp.PAY_WAPLLQ)) {// 外部浏览器
                                    StringBuilder requseUrl =new StringBuilder(VApp.PAY_URL)
                                            .append("orderno=").append(setOrderId()).append( "&")
                                            .append("ordername=").append(URLEncoder.encode(wareName, "UTF-8")).append( "&")
                                            .append(  "orderamt=").append((int) Math.ceil(price * 100)).append( "&").append("paytype=weixin");
                                    StringBuilder requreUrl =new StringBuilder(VApp.PAY_CHECK_URL)
                                            .append("orderno=").append( getorderNumber()).append( "&paytype=weixin");
                                    setQureyURL(requreUrl.toString())
                                            .setCheck(true)
                                            .setPayType(VApp.PAYTYPE_WX)
                                            .setNum(String.valueOf(num))
                                            .setWaretype(waretype)
                                            .setPayTunnel("web");
                                    SharedPreferencesUtils.saveOrderInfo(activity, getorderNumber(), false, waretype, "weixin", String.valueOf(num));
                                    LBStat.pay(getPayType(),
                                            getorderNumber(),
                                            false,
                                            PayConstant.getPayDescribeStr(dateInfo.getFunctionKey()),
                                            Float.valueOf(getNum()),
                                            type);
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    Uri content_url = Uri.parse(requseUrl.toString());
                                    intent.setData(content_url);
                                    activity.startActivity(intent);
                                } else if (type.equals(VApp.PAY_WXSC)) {//收藏支付
                                    wxShouCang(price,wareName);
                                } else {
                                    StringBuilder requseUrl =new StringBuilder(VApp.PAY_URL)
                                            .append("orderno=").append(setOrderId()).append( "&")
                                            .append("ordername=").append(URLEncoder.encode(wareName, "UTF-8")).append( "&")
                                            .append("orderamt=").append((int) Math.ceil(price * 100)).append( "&").append("paytype=weixin");
//                                    StringBuilder requseUrl = new StringBuilder("http://api.duokai.mxitie.com/cgi/pay.ashx/bfb_pay_do_WX?orderamt=100&ordername=fdsfsd");
                                    DebugLog.e(requseUrl.toString());
                                    StringBuilder requreUrl =new StringBuilder(VApp.PAY_CHECK_URL)
                                            .append("orderno=").append( getorderNumber()).append( "&paytype=weixin");
                                    DebugLog.e("requseUrl="+requseUrl+"//requreUrl="+requreUrl);
                                    lodingWeb(requseUrl.toString(), activity)
                                            .setQureyURL(requreUrl.toString())
                                            .setCheck(true)
                                            .setPayType(VApp.PAYTYPE_WX)
                                            .setNum(String.valueOf(price))
                                            .setPayTunnel("web");
                                    SharedPreferencesUtils.saveOrderInfo(activity, getorderNumber(), false, waretype, "weixin", String.valueOf(num));
                                    LBStat.pay(getPayType(),
                                            getorderNumber(),
                                            false,
                                            PayConstant.getPayDescribeStr(dateInfo.getFunctionKey()),
                                            Float.valueOf(getNum()),
                                            type);
                                }
                                ComsuptionInfo comsuptionInfo = new ComsuptionInfo();
                                comsuptionInfo.setPrice(String.valueOf(price));
                                comsuptionInfo.setPayType(PayConstant.PAY_TYPE_WX);
                                comsuptionInfo.setPayChannelType("weixin");
                                comsuptionInfo.setCommodityFunctionType(dateInfo.getFunctionKey());
                                comsuptionInfo.setCommodityName(wareName);
                                comsuptionInfo.setPayComplateStatus(DBLibs.Pay_status_Unknown);
                                comsuptionInfo.setInsertDate("" + System.currentTimeMillis());
                                comsuptionInfo.setOrderNumber(getorderNumber());
                                comsuptionInfo.setOther("");
                                operationDB.insertComsuptionInfo(comsuptionInfo);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            lodingDialog.dismiss();
                            Toast.makeText(activity, activity.getString(R.string.getorder_fail), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        lodingDialog.dismiss();
                        Toast.makeText(activity, activity.getString(R.string.getorder_fail), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    this.dispose();
                    lodingDialog.dismiss();
                }

                @Override
                public void onComplete() {
                    this.dispose();
                }
            };
            flowable.subscribe(resultSubscriber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //支付宝支付
    public void doAliPay(NewPayDialogInfo dateInfo, String wareName, float price, Dialog dialog){
        if (isPayStatu) {
            isPayStatu = false;
            new Thread(new PayStatusRunable()).start();
        } else {
            return;
        }
        dialog.dismiss();
        try {
            StringBuilder url =new StringBuilder(VApp.PAY_URL)
                    .append("orderno=").append(setOrderId()).append( "&")
                    .append("ordername=").append(URLEncoder.encode(wareName, "UTF-8")).append( "&")
                    .append(  "orderamt=").append((int) Math.ceil(price * 100)).append( "&").append("paytype=zhifubao");
//                            Log.e(TAG, "paySelectCallback: url=" + url);
            StringBuilder url2 =new StringBuilder(VApp.PAY_CHECK_URL)
                    .append("orderno=").append(getorderNumber()).append( "&paytype=alipay");
//                            Log.e(TAG, "paySelectCallback: url2=" + url2);
            lodingWeb(url.toString(), activity)
                    .setQureyURL(url2.toString())
                    .setCheck(true)
                    .setPayType(VApp.PAYTYPE_ZFB)
                    .setNum(String.valueOf(price))
                    .setPayTunnel("");
            SharedPreferencesUtils.saveOrderInfo(activity, getorderNumber(), false, waretype, VApp.PAYTYPE_ZFB, String.valueOf(price));
            LBStat.pay(getPayType(),
                    getorderNumber(),
                    false,
                    PayConstant.getPayDescribeStr(dateInfo.getFunctionKey()),
                    Float.valueOf(num),
                    "alipay");
            ComsuptionInfo comsuptionInfo = new ComsuptionInfo();
            comsuptionInfo.setPrice(String.valueOf(price));
            comsuptionInfo.setPayType(PayConstant.PAY_TYPE_ZFB);
            comsuptionInfo.setPayChannelType("alipay");
            comsuptionInfo.setCommodityFunctionType(dateInfo.getFunctionKey());
            comsuptionInfo.setCommodityName(wareName);
            comsuptionInfo.setPayComplateStatus(DBLibs.Pay_status_Unknown);
            comsuptionInfo.setInsertDate("" + System.currentTimeMillis());
            comsuptionInfo.setOrderNumber(getorderNumber());
            comsuptionInfo.setOther("");
            operationDB.insertComsuptionInfo(comsuptionInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //QQ支付
    public void doQQPay(NewPayDialogInfo dateInfo, String wareName, float price, Dialog dialog){
        if (isPayStatu) {
            isPayStatu = false;
            new Thread(new PayStatusRunable()).start();
        } else {
            return;
        }
        dialog.dismiss();
        try {
            StringBuilder requseUrl =new StringBuilder(VApp.PAY_URL)
                    .append("orderno=").append(setOrderId()).append( "&")
                    .append("ordername=").append(URLEncoder.encode(wareName, "UTF-8")).append( "&")
                    .append(  "orderamt=").append((int) Math.ceil(price * 100)).append( "&").append("paytype=QQpay");
//                            Log.e(TAG, "paySelectCallback: url=" + url);
            StringBuilder requreUrl =new StringBuilder(VApp.PAY_CHECK_URL)
                    .append("orderno=").append( getorderNumber()).append( "&paytype=QQpay");
            lodingWeb(requseUrl.toString(), activity)
                    .setQureyURL(requreUrl.toString())
                    .setCheck(true)
                    .setPayType(VApp.PAYTYPE_QQ)
                    .setNum(String.valueOf(price))
//                    .setWaretype(wareType)
                    .setPayTunnel("web");
            SharedPreferencesUtils.saveOrderInfo(activity, getorderNumber(), false, waretype, VApp.PAYTYPE_QQ, String.valueOf(num));
            LBStat.pay(getPayType(),
                    getorderNumber(),
                    false,
                    PayConstant.getPayDescribeStr(dateInfo.getFunctionKey()),
                    Float.valueOf(getNum()),
                    "qqPay");
            ComsuptionInfo comsuptionInfo = new ComsuptionInfo();
            comsuptionInfo.setPrice(String.valueOf(price));
            comsuptionInfo.setPayType(PayConstant.PAY_TYPE_QQ);
            comsuptionInfo.setPayChannelType("QQpay");
            comsuptionInfo.setCommodityFunctionType(dateInfo.getFunctionKey());
            comsuptionInfo.setCommodityName(wareName);
            comsuptionInfo.setPayComplateStatus(DBLibs.Pay_status_Unknown);
            comsuptionInfo.setInsertDate("" + System.currentTimeMillis());
            comsuptionInfo.setOrderNumber(getorderNumber());
            comsuptionInfo.setOther("");
            operationDB.insertComsuptionInfo(comsuptionInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void wxShouCang(float num_get, String wareName_get) {
        //数据源
        Flowable flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                String codeUrl = "";
                StringBuilder requseUrl =new StringBuilder(VApp.PAY_URL)
                        .append("orderno=").append(setOrderId()).append( "&")
                        .append("ordername=").append(URLEncoder.encode(wareName_get, "UTF-8")).append( "&")
                        .append(  "orderamt=").append((int) Math.ceil(num_get * 100)).append( "&").append("paytype=weixin");
                com.zhushou.weichat.utils.OkHttpUtil okHttpUtil = com.zhushou.weichat.utils.OkHttpUtil.getInstance();
                OkHttpResult result = okHttpUtil.requestGetBySyn(requseUrl.toString(), null, null);
                if (result.isSuccess) {
                    if (result.msg != null && !result.msg.isEmpty()) {
                        codeUrl = result.msg;
                    } else {
                        Toast.makeText(activity, activity.getString(R.string.getorder_fail), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, activity.getString(R.string.getorder_fail), Toast.LENGTH_SHORT).show();
                }
                e.onNext(codeUrl);
                e.onComplete();
            }
        }, BackpressureStrategy.DROP).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()); //指定背压处理策略
        //观察者
        ResourceSubscriber resultSubscriber = new ResourceSubscriber<String>() {
            @Override
            public void onNext(String codeUrl) {
                if (codeUrl != null && !codeUrl.isEmpty()) {//&&(codeUrl.startsWith("weixin")||codeUrl.startsWith("http://mobile.qq.com")
                    StringBuilder queryUrl =new StringBuilder(VApp.PAY_CHECK_URL)
                            .append("orderno=").append( getorderNumber()).append( "&paytype=weixin");
                    setQureyURL(queryUrl.toString())
                            .setCheck(true)
                            .setPayType(VApp.PAYTYPE_WX)
                            .setNum(String.valueOf(num_get))
//                            .setWaretype(wareType)
                            .setPayTunnel("web");
                    SharedPreferencesUtils.saveOrderInfo(activity, getorderNumber(), false, waretype, "weixin", String.valueOf(num));
                    LBStat.pay(getPayType(),
                            getorderNumber(),
                            false,
                            waretype == MainActivity.singleType ? VApp.SP_DG : waretype == MainActivity.allAppsType ? VApp.SP_syyydk : VApp.SP_ZS,
                            Float.valueOf(getNum()),
                            "wxllq");
                    Intent intent = new Intent(activity, NbzleaActivity.class);
                    intent.putExtra("codeUrl", codeUrl);
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, activity.getString(R.string.getorder_fail), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable t) {
                this.dispose();
            }

            @Override
            public void onComplete() {
                this.dispose();
            }
        };
        //订阅注册
        flowable.subscribe(resultSubscriber);
    }

    private boolean check() {
        if (!HistoryPayShareUtil.isWeixinAvilible(activity)) {
            Toast.makeText(activity, activity.getString(R.string.anzhuang_weixin_app), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    public class PayStatusRunable implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
                isPayStatu = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    public void closeSqlite() {
//        sqliteDo.closeDb();
    }

    private RequreClick r;

    public interface RequreClick {
        void successfulclick(String apkName, int type, int waretype);

        void failClick();

        void exceprtion();

        void anginQuery();

    }

    public PayHelper setClick(RequreClick r) {
        this.r = r;
        return this;
    }

    public RequreClick getR() {
        return r;
    }

    public void setR(RequreClick r) {
        this.r = r;
    }

    public void setIsstart(boolean isstart) {
        this.isstart = isstart;
    }

    //?????????
    public String getorderNumber() {
        return orderNumber;
    }

    //?????????
    public void setorderNumber(String orderId) {
        orderNumber = orderId;
    }

    //?ж?????????
    public boolean isCheck() {
        return isCheck;
    }

    //????????????
    public PayHelper setCheck(boolean check) {
        isCheck = check;
        return this;
    }

    //??????URL
    public String getQureyURL() {
        return qureyURL;
    }

    //???ò????URL
    public PayHelper setQureyURL(String qureyURL) {
        this.qureyURL = qureyURL;
        return this;
    }

    public String getNum() {
        if (num==null)
            num = "0";
        return num;
    }

    public PayHelper setNum(String num) {
        this.num = num;
        return this;
    }

    public String getPayType() {
        return payType;
    }

    public PayHelper setPayType(String payType) {
        this.payType = payType;
        if (lodingDialog != null)
            lodingDialog.setMessage(payType.equals("alipay") ? "正在调起支付宝..." :payType.equals(PayConstant.PAY_TYPE_QQ)?"正在调起QQ支付...":"正在调起微信支付...");
        return this;
    }

    public void restoreDefault() {
        //???????????????????ν????????????true??д????
        setCheck(false);
        isstart = false;
        setIsstart(false);
    }

    //是否有网络
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //生成订单号（时间+DEVICE_ID+MD5）取32位
    public String setOrderId() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = sDateFormat.format(new java.util.Date());
        String s = date + getBs();
        String md = MD5.getDigest(s);
        String orderId = date + md;
        setorderNumber(orderId.substring(0, 26));
        return orderId.substring(0, 26);
    }

    public int getType() {
        return type;
    }

    public PayHelper setType(int type) {
        this.type = type;
        return this;
    }

    public int getWaretype() {
        return waretype;
    }

    public PayHelper setWaretype(int waretype) {
        this.waretype = waretype;
        return this;
    }

    private static String getBs() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI

                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        return m_szDevIDShort;
    }

    public String getPayTunnel() {
        return payTunnel;
    }

    public PayHelper setPayTunnel(String payTunnel) {
        this.payTunnel = payTunnel;
        return this;
    }
}
