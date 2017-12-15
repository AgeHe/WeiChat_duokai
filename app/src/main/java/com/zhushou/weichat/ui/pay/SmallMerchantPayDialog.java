package com.zhushou.weichat.ui.pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.lanzs.zypay.activity.PaymentActivity;
import com.lbsw.stat.LBStat;
import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;
import com.switfpass.pay.utils.SignUtils;
import com.switfpass.pay.utils.Util;
import com.switfpass.pay.utils.XmlUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.bean.NewPayDialogInfo;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.bean.StatisticsConstans;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.bean.WxSdkQureyOrderInfo;
import com.zhushou.weichat.ui.MainActivity;
import com.zhushou.weichat.ui.view.NewPayDialog;
import com.zhushou.weichat.utils.CountUtil;
import com.zhushou.weichat.utils.HistoryPayShareUtil;
import com.zhushou.weichat.utils.OkHttpUtil;
import com.zhushou.weichat.utils.SharedPreferencesUtils;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jshy.pay.bfb.Pay;
import jshy.pay.bfb.http.HttpUtils;

import static com.switfpass.pay.utils.Util.genNonceStr;

/**
 * Created by Administrator on 2017/2/21.
 */

public class SmallMerchantPayDialog {

    private static final String TAG = "SmallMerchantPayDialog";
    //支付按钮点击标识
    boolean isPayStatu = true;
    private CountUtil countUtil;
    private Activity activity;
    private static AlertDialog dialog;
    private PayHelper helper;
    private PayHelper.RequreClick requreClick;
    private Resources mResources;
    public static String apkName;
    public String commodityType;
    public float aPrice, allPrice, price;
    private int waretype; // 选择的商品类型
    private UpdatePriceInfo updatePriceInfo;
    private NewPayDialog newPayDialog;

    private String singleSelectorStr = "";
    private String lifeTimeselectorStr = "";

    private String dialogTitleName = "";

    private String saveCommondityKey = "";

    private String contentName;
    private float mDialogPrice;

    private PayLodingDialog payLodingDialog;

    private long paymentProcesTime;

    public SmallMerchantPayDialog(Activity activity, PayHelper.RequreClick requreClick, PayHelper helper) {
        this.activity = activity;
        mResources = activity.getResources();
        this.requreClick = requreClick;
        this.helper = helper;
        countUtil = new CountUtil(activity);
        this.updatePriceInfo = APPUsersShareUtil.getUpdateMessage(activity);
        payLodingDialog = new PayLodingDialog(activity);
    }

    public static String getApkName() {
        return apkName;
    }

    public static void setApkName(String apkName) {
        SmallMerchantPayDialog.apkName = apkName;
    }

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;

    }


    private String selectTitleName(int waretype) {
        String titleName = "";
        switch (waretype) {
            case MainActivity.lifeTimeType:
                titleName = mResources.getString(R.string.pay_wx_title_svip);
                break;
            default:
                titleName = dialogTitleName;
                break;
        }
        return titleName;
    }

    public void showPayDialog(String payContentName, String dialogPrice) {
        this.price = allPrice;//默认购买全部类型商品
        this.mDialogPrice = Float.valueOf(dialogPrice);
        this.contentName = payContentName;
        this.waretype = MainActivity.lifeTimeType;

        paymentProcesTime = System.currentTimeMillis();

//        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_small_merchant_pay, null);
////        TextView dialog_pay_content = (TextView) dialogView.findViewById(R.id.dialog_pay_content);
////        dialog_pay_content.setText(payContentName+"   "+(mDialogPrice)+mResources.getString(R.string.rmb_yuan));
//        dialogView.findViewById(R.id.dialog_layout_close).setOnClickListener(view -> {
//            dialog.dismiss();
//        });
//        TextView dialog_apkname = (TextView) dialogView.findViewById(R.id.dialog_apkname);
//        dialog_apkname.setText(dialogTitleName);
//
//        LinearLayout dialog_zfb_layout = (LinearLayout) dialogView.findViewById(R.id.dialog_zfb_layout);
//        LinearLayout dialog_wx_layout = (LinearLayout) dialogView.findViewById(R.id.dialog_wx_layout);
//
//        dialog_zfb_layout.setVisibility(MD5.isVisiblePayWindow(VApp.PAYTYPE_ZFB, activity) ? View.VISIBLE : View.GONE);
//        dialog_wx_layout.setVisibility(MD5.isVisiblePayWindow(VApp.PAYTYPE_WX, activity) ? View.VISIBLE : View.GONE);
//
//        RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.radiogroup);
//        RadioButton dcBtn = (RadioButton) dialogView.findViewById(R.id.dc);
//        dcBtn.setText(String.format(singleSelectorStr, Float.toString(aPrice)));
//        RadioButton zsBtn = (RadioButton) dialogView.findViewById(R.id.zs);
//        zsBtn.setText(String.format(lifeTimeselectorStr, Float.toString(allPrice)));
//
//        dcBtn.setVisibility(saveCommondityKey.equals(AuxiliaryConstans.C_HB_SVIP) ? View.GONE : View.VISIBLE);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int radioId) {
//                switch (radioId) {
//                    case R.id.dc:
//                        price = aPrice;
//                        waretype = MainActivity.singleType; // single commodity
//                        break;
//                    case R.id.zs:
//                        price = allPrice;
//                        waretype = MainActivity.lifeTimeType; // All commodity /goods
//                        break;
//                }
//            }
//        });
//
//        //支付宝支付按钮
//        dialog_zfb_layout.setOnClickListener(view -> {
//            if (isPayStatu) {
//                isPayStatu = false;
//                new Thread(new PayStatusRunable()).start();
//            } else {
//                return;
//            }
//            try {
//                String url = "http://api.duokai.mxitie.com/cgi/pay.ashx/pay.do?" +
//                        "orderno=" + helper.setOrderId() + "&" +
//                        "ordername=" + URLEncoder.encode(selectTitleName(waretype), "UTF-8") + "&" +
//                        "orderamt=" + Integer.toString((int) (price * 100))
//                        + "&" +
//                        "paytype=zhifubao";
//                String url2 = "http://api.duokai.mxitie.com/cgi/pay.ashx/order/info.json?" +
//                        "orderno=" + helper.getorderNumber() + "&paytype=alipay";
//                helper.lodingWeb(url, activity)
//                        .setQureyURL(url2)
//                        .setCheck(true)
//                        .setClick(requreClick)
//                        .setType(-1)
//                        .setPayType(VApp.PAYTYPE_ZFB)
//                        .setNum(String.valueOf(price))
//                        .setWaretype(waretype)
//                        .setPayTunnel("");
//
//                LBStat.pay(helper.getPayType(),
//                        helper.getorderNumber(),
//                        false,
//                        StatisticsConstans.getPayTagValue(getSaveCommondityKey()),
//                        Float.valueOf(helper.getNum()),
//                        "");
////                SharedPreferencesUtils.saveOrderInfo(activity,helper.getorderNumber(),false,-1,VApp.PAYTYPE_ZFB,apkName);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        //微信支付按钮
//        dialog_wx_layout.setOnClickListener(view -> {
//            if (isPayStatu) {
//                isPayStatu = false;
//                new Thread(new PayStatusRunable()).start();
//            } else {
//                return;
//            }
//
//            try {
//                if (check()) {//
//                    new Thread(new StartRunnable()).start();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        dialog = new AlertDialog.Builder(activity).create();
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setView(dialogView);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        List<NewPayDialogInfo> listdata = new ArrayList<NewPayDialogInfo>();
//        if (getSaveCommondityKey().equals(AuxiliaryConstans.C_HB_SVIP)){
//            NewPayDialogInfo info = new NewPayDialogInfo();
//            info.setFunctionName("开启抢红包全部功能");
//            info.setNewPrice(String.valueOf(allPrice));
//            info.setOldPrice(discount(
//                    Double.valueOf(allPrice),
//                    Double.valueOf(updatePriceInfo.getDiscount())
//            ));
//            listdata.add(0, info);
//        }else{
//            NewPayDialogInfo info1 = new NewPayDialogInfo();
//            info1.setFunctionName(contentName);
//            info1.setNewPrice(String.valueOf(aPrice));
//            info1.setOldPrice(discount(
//                    Double.valueOf(aPrice),
//                    Double.valueOf(updatePriceInfo.getDiscount())
//            ));
//            info1.setFunctionKey(saveCommondityKey);
//            listdata.add(0, info1);
//            NewPayDialogInfo info2 = new NewPayDialogInfo();
//            info2.setFunctionName("开启抢红包全部功能");
//            info2.setNewPrice(String.valueOf(allPrice));
//            info2.setOldPrice(discount(
//                    Double.valueOf(allPrice),
//                    Double.valueOf(updatePriceInfo.getDiscount())
//            ));
//            listdata.add(1, info2);
//        }
//        newPayDialog = new NewPayDialog(activity,
//                listdata,
//                Float.valueOf(updatePriceInfo.getDiscount()) * 10);
        newPayDialog.setPayTypeIsVisibility(updatePriceInfo.isPay_status_ali(),
                updatePriceInfo.isPay_status_wx(),false);

        newPayDialog.setpayDialogInterface(new NewPayDialog.PayDialogInterface() {
            @Override
            public void paySelectCallback(NewPayDialogInfo dateInfo, String functionKey, int payType, String selectPrice) {
                Log.i(TAG, "newpaydialog_paySelectCallback: functionKey=" + functionKey + "&payType=" + payType + "&price=" + price);
//                switch (functionKey) {
//                    case 0:
//                        waretype = MainActivity.singleType;
//                        break;
//                    case 1:
//                        waretype = MainActivity.lifeTimeType;
//                        break;
//                }
                price = Float.valueOf(selectPrice);
                helper.setNum(String.valueOf(price));
                switch (payType) {
                    case 1:
                        if (isPayStatu) {
                            isPayStatu = false;
                            new Thread(new PayStatusRunable()).start();
                        } else {
                            return;
                        }
                        try {
                            String url = "http://api.duokai.mxitie.com/cgi/pay.ashx/pay.do?" +
                                    "orderno=" + helper.setOrderId() + "&" +
                                    "ordername=" + URLEncoder.encode(selectTitleName(waretype), "UTF-8") + "&" +
                                    "orderamt=" + Integer.toString((int) (price * 100))
                                    + "&" +
                                    "paytype=zhifubao";
                            String url2 = "http://api.duokai.mxitie.com/cgi/pay.ashx/order/info.json?" +
                                    "orderno=" + helper.getorderNumber() + "&paytype=alipay";
                            helper.lodingWeb(url, activity)
                                    .setQureyURL(url2)
                                    .setCheck(true)
                                    .setClick(requreClick)
                                    .setType(-1)
                                    .setPayType(VApp.PAYTYPE_ZFB)
                                    .setNum(String.valueOf(price))
                                    .setWaretype(waretype)
                                    .setPayTunnel("");

                            LBStat.pay(helper.getPayType(),
                                    helper.getorderNumber(),
                                    false,
                                    StatisticsConstans.getPayTagValue(getSaveCommondityKey()),
                                    Float.valueOf(helper.getNum()),
                                    "");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        if (isPayStatu) {
                            isPayStatu = false;
                            new Thread(new PayStatusRunable()).start();
                        } else {
                            break;
                        }
                        try {
                            if (!check())
                                break;
                            new Thread(new StartRunnable()).start();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

        newPayDialog.show();


    }

    private String discount(double price, double discount) {
        DecimalFormat df = new DecimalFormat("####0.00");
        double resultPrice = price / discount;
        return String.valueOf(df.format(resultPrice));
    }


    public static void dismiss() {
        if (dialog!=null)
        dialog.dismiss();
    }

    public static boolean isShowDialog() {
        if (dialog == null)
            return false;
        return dialog.isShowing();
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

    /**
     * 功能:微信支付
     * 参数:
     */
    JSONObject jsonObject = null;
    Order mOrder;

    public void startPay() {
        wxPay();
        if (mOrder != null && jsonObject != null) {
            if ("0".equals(jsonObject.optString("status"))) {
                jshy.pay.bfb.ben.Order.getInstance().setApp_id(mOrder.getApp_id());
                Pay.startPay(activity, jsonObject.optString("token_id"), jsonObject.optInt("type"));//mOrder.getType()
            }
        }
    }

    private void wxPay() {
        mOrder = Order.getInstance();

        // APP`id
        mOrder.setApp_id("10406");
        // 计费说明
        mOrder.setBody(encodeUrl(contentName != null ? contentName : ""));
        //设备信息
        mOrder.setDevice_info(getDeviceId());
        //异步回调地址
        mOrder.setNofity_url("http://156h25l868.51mypc.cn:27613/ybcore/get");
        //订单号
        mOrder.setPara_tradeNo(helper.setOrderId());
        //商户 id
        mOrder.setPara_id("10515");
        // 金额
        mOrder.setTotal_fee(String.valueOf((int) Math.ceil(price * 100)));
        //自定义参数
        mOrder.setAttach("123");
        mOrder.setType(0);
        //	md5(para_id+app_id+order_no+total_fee+key).tolowercase()
        String str = mOrder.getPara_id() + "" + mOrder.getApp_id() + "" + mOrder.getPara_tradeNo() + "" + mOrder.getTotal_fee() + "" + "U3e22OLF3NSYbYMCQ0VcIaHgL43DWLUJ";
        mOrder.setSign(MD5sign(str));
        String stri = null;
        try {
            stri = "type=" + mOrder.getType() + "&device_id=" + "1" + "&body=" + URLEncoder.encode(mOrder.getBody(), "utf-8") + "&total_fee=" + this.mOrder.getTotal_fee() + "&para_id=" + this.mOrder.getPara_id() + "&app_id=" + this.mOrder.getApp_id() + "&order_no=" + this.mOrder.getPara_tradeNo() + "&notify_url=" + this.mOrder.getNofity_url() + "&attach=" + this.mOrder.getAttach() + "&sign=" + this.mOrder.getSign() + "&child_para_id=" + this.mOrder.getChild_para_id() + "&userIdentity" + "124424231231";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = HttpUtils.startGet("http://lftpay.jieshenkj.com/sdk_transform/wxapp_ali_choosepay", stri);

        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String encodeUrl(String s) {
        String i = "计费";
        try {
            i = URLEncoder.encode(s, "UTF-8");
            return i;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return i;
        }
    }

    public String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        return DEVICE_ID;
    }

    public static String MD5sign(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes("UTF-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getSaveCommondityKey() {
        return saveCommondityKey;
    }

    public void setSaveCommondityKey(String saveCommondityKey) {
        this.saveCommondityKey = saveCommondityKey;
    }

    public String getDialogTitleName() {
        return dialogTitleName;
    }

    public void setDialogTitleName(String dialogTitleName) {
        this.dialogTitleName = dialogTitleName;
    }

    public String getSingleSelectorStr() {
        return singleSelectorStr;
    }

    public void setSingleSelectorStr(String singleSelectorStr) {
        this.singleSelectorStr = singleSelectorStr;
    }

    public float getaPrice() {
        return aPrice;
    }

    public void setaPrice(float aPrice) {
        this.aPrice = aPrice;
    }

    public float getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(float allPrice) {
        this.allPrice = allPrice;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getLifeTimeselectorStr() {
        return lifeTimeselectorStr;
    }

    public void setLifeTimeselectorStr(String lifeTimeselectorStr) {
        this.lifeTimeselectorStr = lifeTimeselectorStr;
    }

    public long getPaymentProcesTime() {
        long returnTime = paymentProcesTime;
        paymentProcesTime = 0;
        return returnTime;
    }

    class StartRunnable implements Runnable {
        @Override
        public void run() {
            OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
            OkHttpResult result = okHttpUtil.requestGetBySyn(VApp.URLCX_PAY, null, null);
            Message msg = new Message();
            msg.obj = result;
            msg.what = 0;
            startHandler.sendMessage(msg);
        }
    }

    private Handler startHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    OkHttpResult result = (OkHttpResult) msg.obj;
                    if (result != null && result.isSuccess) {
                        try {
                            JSONObject jo = new JSONObject(result.msg);
                            if (jo != null) {
                                String type = jo.getString(countUtil.getApp());
//                                type = VApp.PAY_TLWAP;
                                if (requreClick == null)
                                    return;
                                if (type.equals("bfb")){
                                    new Thread(new Runnable(){
                                        @Override
                                        public void run(){
                                            helper.setClick(requreClick).setType(-1).setPayType(VApp.PAYTYPE_WX).setNum(String.valueOf(price)).setWaretype(waretype).setPayTunnel(VApp.PAY_BFB);
                                            startPay();
                                        }
                                    }).start();
                                } else if (type.equals("wft")){
                                    new GetPrepayIdTask().execute();
                                    helper.setClick(requreClick).setType(-1).setPayType(VApp.PAYTYPE_WX).setNum(String.valueOf(price)).setWaretype(waretype).setPayTunnel(VApp.PAY_WFT);
                                } else if (type.equals("wiipay")) {
                                    String requseurl = "http://api.duokai.mxitie.com/cgi/pay.ashx/pay.do?" +
                                            "orderno=" + helper.setOrderId() + "&" +
                                            "ordername=" + URLEncoder.encode(selectTitleName(waretype), "UTF-8") + "&" +
                                            "orderamt=" + (int) Math.ceil(price * 100)//
                                            + "&" +
                                            "paytype=weixin";
                                    String requreUrl = "http://api.duokai.mxitie.com/cgi/pay.ashx/order/info.json?" +
                                            "orderno=" + helper.getorderNumber() + "&paytype=weixin";
                                    helper.lodingWeb(requseurl, activity).setQureyURL(requreUrl).setCheck(true).setClick(requreClick).setType(-1).setPayType(VApp.PAYTYPE_WX).setNum(String.valueOf(price)).setWaretype(waretype).setPayTunnel(VApp.PAY_WIIPAY);
                                    SharedPreferencesUtils.saveOrderInfo(activity, helper.getorderNumber(), false, waretype, "weixin", String.valueOf(price));
                                    LBStat.pay(helper.getPayType(),
                                            helper.getorderNumber(),
                                            false,
                                            StatisticsConstans.getPayTagValue(getSaveCommondityKey()),
                                            Float.valueOf(helper.getNum()),
                                            type);
                                } else if (type.equals(VApp.PAY_BFBSCAN)){
                                    new Thread(new StartQRRunnable()).start();
                                }else if (type.equals(VApp.PAY_WXSDK)) {
                                    payLodingDialog.show();
                                    new Thread(new wxSdkRunnable()).start();
                                }else if(type.equals(VApp.PAY_XGSDK)){
                                    //鑫广支付
                                    helper.setOrderId();
                                    helper.setQureyURL("").setCheck(true).setClick(requreClick).setType(-1).setPayType(VApp.PAYTYPE_WX).setNum(String.valueOf(price)).setWaretype(waretype).setPayTunnel(VApp.PAY_XGSDK);
                                    xgPay(activity);
                                }else if (type.equals(VApp.PAY_TLWAP)) {
                                    String requseurl = "http://api.duokai.mxitie.com/cgi/Default2.aspx?" +
                                            "orderno=" + helper.setOrderId() + "&" +
                                            "ordername=" + URLEncoder.encode(selectTitleName(waretype), "UTF-8") + "&" +
                                            "orderamt=" + (int) Math.ceil(price * 100)//
                                            + "&" +
                                            "paytype=weixin";
                                    String requreUrl = "http://api.duokai.mxitie.com/cgi/pay.ashx/order/info.json?" +
                                            "orderno=" + helper.getorderNumber() + "&paytype=weixin";
                                    helper.lodingWeb(requseurl, activity).setQureyURL(requreUrl).setCheck(true).setClick(requreClick).setType(-1).setPayType(VApp.PAYTYPE_WX).setNum(String.valueOf(price)).setWaretype(waretype).setPayTunnel(VApp.PAY_TLWAP);
                                    SharedPreferencesUtils.saveOrderInfo(activity, helper.getorderNumber(), false, waretype, "weixin", String.valueOf(price));
                                    LBStat.pay(helper.getPayType(),
                                            helper.getorderNumber(),
                                            false,
                                            StatisticsConstans.getPayTagValue(getSaveCommondityKey()),
                                            Float.valueOf(helper.getNum()),
                                            type);
                                } else{
                                    String requseurl = "http://api.duokai.mxitie.com/cgi/pay.ashx/pay.do?" +
                                            "orderno=" + helper.setOrderId() + "&" +
                                            "ordername=" + URLEncoder.encode(selectTitleName(waretype), "UTF-8") + "&" +
                                            "orderamt=" + (int) Math.ceil(price * 100)//
                                            + "&" +
                                            "paytype=weixin";
                                    String requreUrl = "http://api.duokai.mxitie.com/cgi/pay.ashx/order/info.json?" +
                                            "orderno=" + helper.getorderNumber() + "&paytype=weixin";
                                    helper.lodingWeb(requseurl, activity).setQureyURL(requreUrl).setCheck(true).setClick(requreClick).setType(-1).setPayType(VApp.PAYTYPE_WX).setNum(String.valueOf(price)).setWaretype(waretype).setPayTunnel("");
                                    SharedPreferencesUtils.saveOrderInfo(activity, helper.getorderNumber(), false, waretype, "weixin", String.valueOf(price));
                                    LBStat.pay(helper.getPayType(),
                                            helper.getorderNumber(),
                                            false,
                                            StatisticsConstans.getPayTagValue(getSaveCommondityKey()),
                                            Float.valueOf(helper.getNum()),
                                            type);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(activity, "获取订单失败,请稍后重试" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "获取订单失败,请稍后重试1", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
//                    if ((boolean) msg.obj) {
//                        Intent intentFile = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                        Uri uri = Uri.fromFile(new File(FileUtil.getRootPath() + "/" + VApp.SCAN_PNG));
//                        intentFile.setData(uri);
//                        activity.sendBroadcast(intentFile);
//
//                        String requreUrl = "http://api.duokai.mxitie.com/cgi/pay.ashx/order/info.json?" +
//                                "orderno=" + helper.getorderNumber() + "&paytype=weixin";
//                        helper.setQureyURL(requreUrl).setCheck(true).setClick(requreClick).setType(MainActivity.wsPayResultType).setPayType(VApp.PAYTYPE_WX).setNum(String.valueOf(price)).setWaretype(waretype).setPayTunnel(VApp.PAY_BFBSCAN);
//                        SharedPreferencesUtils.saveOrderInfo(activity, helper.getorderNumber(), false, waretype, "weixin", String.valueOf(price));
//                        Intent intent = new Intent(activity, ScanHintActivity.class);
//                        activity.startActivity(intent);
//                    } else {
//                        Toast.makeText(activity, "获取订单失败,请稍后重试", Toast.LENGTH_SHORT).show();
//                    }
                    if (((String)msg.obj).startsWith("weixin:")){
                        String requreUrl = "http://api.duokai.mxitie.com/cgi/pay.ashx/order/info.json?" +
                                "orderno=" + helper.getorderNumber() + "&paytype=weixin";
                        helper.setQureyURL(requreUrl).setCheck(true).setClick(requreClick).setType(-1).setPayType(VApp.PAYTYPE_WX).setNum(String.valueOf(price)).setWaretype(waretype).setPayTunnel(VApp.PAY_BFBSCAN);
                        SharedPreferencesUtils.saveOrderInfo(activity, helper.getorderNumber(), false, waretype, "weixin", String.valueOf(price));
                        Bundle mBundle = new Bundle();
                        mBundle.putString("codeUrl", (String) msg.obj);
                        Intent intent = new Intent(activity, ScanHintActivity.class).putExtras(mBundle);
                        activity.startActivity(intent);
                    }else{
                        Toast.makeText(activity, "获取订单失败,请稍后重试", Toast.LENGTH_SHORT).show();
                    }

//                    try {
//                        OkHttpResult result1 = (OkHttpResult) msg.obj;
//                        JSONObject jo = new JSONObject(result1.msg);
//                        if (jo != null) {
//                            String type = null;
//                            type = jo.getString(countUtil.getApp());

                            LBStat.pay(helper.getPayType(),
                                    helper.getorderNumber(),
                                    false,
                                    StatisticsConstans.getPayTagValue(getSaveCommondityKey()),
                                    Float.valueOf(helper.getNum()),
                                    VApp.PAY_BFBSCAN);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    break;

                case 2:


                    IWXAPI msgApi = WXAPIFactory.createWXAPI(activity, VApp.APP_ID, false);
//            IWXAPI api = WXAPIFactory.createWXAPI(MainActivity.this, "wx18248f724c4c8889");
                    msgApi.registerApp(VApp.APP_ID);
                    PayReq request = new PayReq();
                    try {
                        JSONObject jo = new JSONObject(msg.obj.toString());
                        str_appid = jo.getString("appId");
                        str_partnerId = jo.getString("partnerId");
                        str_prepayId = jo.getString("prepayId");
                        str_packageValue = jo.getString("package");
                        str_nonceStr = jo.getString("nonceStr");
                        str_timeStamp = jo.getString("timeStamp");
                        helper.setNum(String.valueOf(price));
                        helper.setPayType(VApp.PAYTYPE_WX);
                        helper.setWaretype(waretype);
                        request.appId = str_appid;
                        request.partnerId = str_partnerId;
                        request.prepayId = str_prepayId;
                        request.packageValue = str_packageValue;
                        request.nonceStr = str_nonceStr;
                        request.timeStamp = str_timeStamp;//System.currentTimeMillis() / 1000 + "";
                        String str_signs = "appid=" + str_appid + "&noncestr=" + str_nonceStr + "&package=" + str_packageValue + "&partnerid=" + str_partnerId + "&prepayid=" + str_prepayId + "&timestamp=" + str_timeStamp + "&key=865dd0e6577dc1031fd7cf2fb10e2c3e";
                        request.sign = com.zhushou.weichat.utils.MD5.getMessageDigest(str_signs.getBytes()).toUpperCase();
                        msgApi.sendReq(request);
                        payLodingDialog.dismiss();

                        LBStat.pay(helper.getPayType(),
                                helper.getorderNumber(),
                                false,
                                StatisticsConstans.getPayTagValue(getSaveCommondityKey()),
                                Float.valueOf(helper.getNum()),
                                VApp.PAY_WXSDK);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(activity, "调起微信失败", Toast.LENGTH_SHORT).show();
                        payLodingDialog.dismiss();
                    }
                    Log.i(TAG, "handleMessage:" + msg.obj);
                    break;
                case 4:
                    payLodingDialog.dismiss();
                    if (msg.obj.equals("SUCCESS")){
                        if (VApp.getInstance().getHbActivity()!=null){
                            VApp.getInstance().getHbActivity().successfulclick(
                                    SmallMerchantPayDialog.getApkName()!=null?SmallMerchantPayDialog.apkName:""
                                    ,VApp.getInstance().getHbActivity().getHelper().getType()
                                    ,VApp.getInstance().getHbActivity().getHelper().getWaretype());
                        }
                    }else if (msg.obj.equals("err")){
                        dialog.dismiss();
                    }else{
                        dialog.dismiss();
                    }
                    Log.i(TAG, "handleMessage: " + msg.obj);
                    break;
            }

        }
    };
    private void xgPay(Activity activity){
        Intent intent =new Intent(activity,PaymentActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("partnerId",  "dkzs01");// 去平台申请
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        bundle.putString("outTradeNo",  helper.getorderNumber()); //订单号商户生成，保证全局唯一
        bundle.putString("subject",  selectTitleName(waretype)); //订单描述 商户填
        bundle.putString("amt",(int) Math.ceil(price * 100)+""); //订单描述 金额 1表示  0.01元 (int) Math.ceil(num * 100)+""
        bundle.putString("notifyUrl","notifyUrl");//回调地址 //选填
        bundle.putString("returnUrl","returnUrl");//返回地址 //选填
        bundle.putString("reference",  "test");// 选填 防钓鱼参数 商户填
        bundle.putString("bundleId",  "dkzs01001");// 必填 应用程序的包名
        bundle.putString("appName",  "test");// 选填 应用名称
        bundle.putString("clientType",  "2");// 必填 类型
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, 0);
    }

    private String str_appid = "";
    private String str_partnerId = "";
    private String str_prepayId = "";
    private String str_packageValue = "";
    private String str_nonceStr = "";
    private String str_timeStamp = "";
    private String str_orderNumber = "";

    /**
     * 微信订单查询
     */
    public void queryWxSdkOrder() {
        payLodingDialog.show();
        class RequestWxPayStatusRunnableex implements Runnable {
            @Override
            public void run() {

                String queryOrderSign = "";
                String str_signs = "appid=" + str_appid + "&mch_id=" + str_partnerId + "&nonce_str=" + str_nonceStr + "&out_trade_no=" + str_orderNumber + "&key=865dd0e6577dc1031fd7cf2fb10e2c3e";
                queryOrderSign = com.zhushou.weichat.utils.MD5.getMessageDigest(str_signs.getBytes()).toUpperCase();

//                String  requseturl = "https://api.mch.weixin.qq.com/pay/orderquery?appid="+helper.setOrderId()
//                        +"&appid="+str_appid
//                        +"&mch_id="+str_partnerId
//                        +"&out_trade_no="+str_orderNumber
//                        +"&nonce_str="+str_prepayId
//                        +"&sign="+queryOrderSign;

//                HashMap<String, String> payMap = new HashMap<>();
//                payMap.put("appid", str_appid);//应用APPID
//                payMap.put("mch_id", str_partnerId);//商户号
//                payMap.put("out_trade_no", str_orderNumber);//商户订单号
//                payMap.put("nonce_str", str_nonceStr);//随机字符串
//                payMap.put("sign", queryOrderSign);//签名
//                String url = "https://api.mch.weixin.qq.com/pay/orderquery";

//                WxPayQuery wxpayQuery = new WxPayQuery(str_appid,str_partnerId,str_orderNumber,str_prepayId,queryOrderSign);
                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                WxSdkQureyOrderInfo resultInfo = okHttpUtil.wxpayquery(str_appid, str_partnerId, str_orderNumber, str_nonceStr,
                        com.zhushou.weichat.utils.MD5.getMessageDigest(str_signs.getBytes()).toUpperCase(),activity);

//                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
//                OkHttpResult result = okHttpUtil.requestPostBySyn(url,"orderquery",payMap);
                Message msg = new Message();
                msg.obj = resultInfo!=null&&resultInfo.getTrade_state()!=null?resultInfo.getTrade_state():"err";
                msg.what = 4;
                startHandler.sendMessage(msg);
            }
        }
        new Thread(new RequestWxPayStatusRunnableex()).start();
    }

    /**
     * 微信SDK支付请求
     */
    class wxSdkRunnable implements Runnable {
        @Override
        public void run() {
//            String requseturl = null;
//            try {
            str_orderNumber = helper.setOrderId();
            String requseturl = "http://api.duokai.mxitie.com/cgi/pay.ashx/wxSDK/pay.do?orderno=" + helper.setOrderId() + "&orderamt=" + (int) Math.ceil(price * 100);
//                        "ordername=" + URLEncoder.encode(name, "UTF-8") + "&" +
//                        "orderamt=" + (int) Math.ceil(num * 100)//
//                        + "&" +
//                        "paytype=weixin";
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
            OkHttpResult result = okHttpUtil.requestGetBySyn(requseturl, null, null);
            Message msg = new Message();
            msg.obj = result.msg;
            msg.what = 2;
            startHandler.sendMessage(msg);
        }
    }


    class StartQRRunnable implements Runnable {
        @Override
        public void run() {
            String requseturl = null;
            try {
                requseturl = "http://api.duokai.mxitie.com/cgi/pay.ashx/pay.do?" +
                        "orderno=" + helper.setOrderId() + "&" +
                        "ordername=" + URLEncoder.encode(selectTitleName(waretype), "UTF-8") + "&" +
                        "orderamt=" + (int) Math.ceil(price * 100)//
                        + "&" +
                        "paytype=weixin";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
            OkHttpResult result = okHttpUtil.requestGetBySyn(requseturl, null, null);
            Message msg = new Message();
//            msg.obj = FileUtil.download(result.msg, VApp.SCAN_PNG);
            msg.obj = result.msg;
            msg.what = 1;
            startHandler.sendMessage(msg);
        }
    }

    /**
     * 组装参数
     * <功能详细描述>
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("body", selectTitleName(waretype)); // 商品名称
        params.put("service", "unified.trade.pay"); // 支付类型
        params.put("version", "2.0"); // 版本
        params.put("mch_id", "101590041370"); // 威富通商户号
        //        params.put("mch_id", mchId.getText().toString()); // 威富通商户号
        params.put("notify_url", " "); // 后台通知url
        params.put("nonce_str", genNonceStr()); // 随机数money, body, mchId, notifyUrl, orderNo, signKey, appId, seller_id, credit_pay;
//        String out_trade_no = genOutTradNo();
        String out_trade_no = helper.setOrderId();
        //String out_trade_no =dateFormat.format(new Date()).toString();
        params.put("out_trade_no", out_trade_no); //订单号
        params.put("mch_create_ip", "127.0.0.1"); // 机器ip地址
        params.put("sub_appid", VApp.APP_ID); //
        params.put("total_fee", String.valueOf((int) Math.ceil(price * 100))); // 总金额
        params.put("device_info", "android_sdk"); // 手Q反扫这个设备号必须要传1ff9fe53f66189a6a3f91796beae39fe
        params.put("limit_credit_pay", "0"); // 是否禁用信用卡支付， 0：不禁用（默认），1：禁用
        String sign = createSign("e6f32b7375b9d4fc246aa4ed0672a29b", params); // 9d101c97133837e13dde2d32a5054abb 威富通密钥
        params.put("sign", sign); // sign签名

        return XmlUtils.toXml(params);
    }

    //签名
    public String createSign(String signKey, Map<String, String> params) {
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        buf.append("&key=").append(signKey);
        String preStr = buf.toString();
        String sign = "";
        // 获得签名验证结果
        try {
            sign = com.switfpass.pay.utils.MD5.md5s(preStr).toUpperCase();
        } catch (Exception e) {
            sign = com.switfpass.pay.utils.MD5.md5s(preStr).toUpperCase();
        }
        return sign;
    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {


        public GetPrepayIdTask() {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            if (result == null) {
                Toast.makeText(activity, "1111", Toast.LENGTH_LONG).show();
            } else {
                if (result.get("status").equalsIgnoreCase("0")) // 成功
                {
                    RequestMsg msg = new RequestMsg();
                    msg.setTokenId(result.get("token_id"));
                    //微信
                    msg.setTradeType(MainApplication.WX_APP_TYPE);
                    msg.setAppId(VApp.APP_ID);//wxd3a1cdf74d0c41b3
                    PayPlugin.unifiedAppPay(activity, msg);
                } else {
                    Toast.makeText(activity, "1212", Toast.LENGTH_LONG)
                            .show();
                }

            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {
            // 统一预下单接口
            //            String url = String.format("https://api.weixin.qq.com/pay/genprepay?access_token=%s", accessToken);
            String url = "https://pay.swiftpass.cn/pay/gateway";
            //            String entity = getParams();

            String entity = getParams();


//            GetPrepayIdResult result = new GetPrepayIdResult();

            byte[] buf = Util.httpPost(url, entity);
            if (buf == null || buf.length == 0) {
                return null;
            }
            String content = new String(buf);
//            result.parseFrom(content);
            try {
                return XmlUtils.parse(content);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }
    }

}
