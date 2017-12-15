package com.zhushou.weichat.addfriends.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lbsw.stat.LBStat;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.addfriends.base.AddFriendsBaseActivity;
import com.zhushou.weichat.addfriends.ui.mode.InsertContactsSource;
import com.zhushou.weichat.addfriends.ui.mode.QYContacts;
import com.zhushou.weichat.addfriends.widget.ALoadingDialog;
import com.zhushou.weichat.addfriends.widget.AlertDialog;
import com.zhushou.weichat.auxiliary.sp.SPAuxiliaryPayUtils;
import com.zhushou.weichat.bean.HistoryPayInfo;
import com.zhushou.weichat.bean.StatisticsConstans;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.ui.MainActivity;
import com.zhushou.weichat.ui.pay.PayHelper;
import com.zhushou.weichat.ui.pay.SmallMerchantPayDialog;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;
import com.zhushou.weichat.utils.CountUtil;
import com.zhushou.weichat.utils.HistoryPayShareUtil;
import com.zhushou.weichat.utils.SharedPreferencesUtils;

import java.util.Map;

/**
 * Created by Administrator on 2017/3/28.
 */

public class AddFriendsFunctionA extends AddFriendsBaseActivity implements InsertContactsSource.ViewInterface,
        AlertDialog.ItemClickLisener,PayHelper.RequreClick{

    private CountUtil countUtil;
    private SmallMerchantPayDialog smDialog;
    private PayHelper payHelper; // 支付

    private ALoadingDialog aLoadingDialog;
    private InsertContactsSource.ViewController viewController;
    private UpdatePriceInfo updateMessageInfo;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        VApp.getInstance().setaddFriendsFunctionA(this);
        setContentView(R.layout.activity_add_friends_function);
    }

    @Override
    public void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.rl_qy_add_friend).setOnClickListener(this);
        findViewById(R.id.rl_hd_add_friend).setOnClickListener(this);
        findViewById(R.id.rl_clear_friend).setOnClickListener(this);
        findViewById(R.id.rl_add_friends_help).setOnClickListener(this);
    }

    @Override
    public void initData() {
        viewController = new QYContacts(this,this);
        aLoadingDialog = new ALoadingDialog(mContext);

        countUtil = new CountUtil(mContext);
        updateMessageInfo = APPUsersShareUtil.getUpdateMessage(this);
        payHelper = getHelper();
        smDialog = new SmallMerchantPayDialog(this, this, payHelper);
        smDialog.setDialogTitleName(mResources.getString(R.string.wx_addf_function_jf));
        smDialog.setAllPrice(Float.valueOf(updateMessageInfo.getWeichat_jf_all_price()));
        smDialog.setaPrice(Float.valueOf(updateMessageInfo.getWeichat_jf_dg_price()));
        smDialog.setLifeTimeselectorStr(mResources.getString(R.string.wx_addf_allpay));
        anginQueryDialog();
    }

    @Override
    protected void viewOnClick(View view) {
        super.viewOnClick(view);
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_qy_add_friend:
                //2017/4/12 修改批量加好友支付逻辑 支付改至一级界面
//                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_QUJHY)){
//                    smDialog.setApkName(mResources.getString(R.string.wx_addf_function_qu));
//                    smDialog.setCommodityType(AuxiliaryConstans.C_QUJHY);
//                    smDialog.setContentName(mResources.getString(R.string.wx_addf_function_qu));
//                    smDialog.setDialogTitleName(mResources.getString(R.string.wx_addf_jf_paycontent));
//                    smDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.wx_addf_qy_pay),String.valueOf(smDialog.getaPrice())));
//                    smDialog.setSaveCommondityKey(AuxiliaryConstans.C_QUJHY);
//                    smDialog.showPayDialog(mResources.getString(R.string.wx_addf_function_qu),String.valueOf(smDialog.getaPrice()));
//                }else{
                    startActivity(new Intent(AddFriendsFunctionA.this,QYAddFriendsActivity.class));
//                }
                break;
            case R.id.rl_hd_add_friend:
                //2017/4/12 修改批量加好友支付逻辑 支付改至一级界面
//                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_HDJHY)){
//                    smDialog.setApkName(mResources.getString(R.string.wx_addf_function_hd));
//                    smDialog.setCommodityType(AuxiliaryConstans.C_HDJHY);
//                    smDialog.setContentName(mResources.getString(R.string.wx_addf_function_hd));
//                    smDialog.setDialogTitleName(mResources.getString(R.string.wx_addf_jf_paycontent));
//                    smDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.wx_addf_hd_pay),String.valueOf(smDialog.getaPrice())));
//                    smDialog.setSaveCommondityKey(AuxiliaryConstans.C_HDJHY);
//                    smDialog.showPayDialog(mResources.getString(R.string.wx_addf_function_hd),String.valueOf(smDialog.getaPrice()));
//                }else{
                    startActivity(new Intent(AddFriendsFunctionA.this,HDAddFriendsActivity.class));
//                }
                break;
            case R.id.rl_clear_friend:
                AlertDialog alertDialog = new AlertDialog(mContext);
                alertDialog.setClickLisener(this);
                alertDialog.show();
                break;
            case R.id.rl_add_friends_help:
                startActivity(new Intent(AddFriendsFunctionA.this,HelpServiceActivity.class));
                break;
        }
    }

    @Override
    public void startTackleContacts() {
        aLoadingDialog.setMessage(mResources.getString(R.string.wx_addf_friends_toastC));
        aLoadingDialog.loadingShow();
    }

    @Override
    public void endTackleContactsLoading() {
        aLoadingDialog.dismiss();
        Toast.makeText(AddFriendsFunctionA.this,mResources.getString(R.string.wx_addf_friends_toastS),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void endExceptionStatu() {
        aLoadingDialog.dismiss();
        Toast.makeText(AddFriendsFunctionA.this,mContext.getResources().getString(R.string.wx_addf_e_toast),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemclick() {
        viewController.clearContacts();
    }

    @Override
    public void successfulclick(String apkName, int type, int waretype) {

        if (payHelper.getPayType().equals(VApp.PAYTYPE_ZFB)){
            SharedPreferencesUtils.saveOrderInfo(mContext,payHelper.getorderNumber(),true,payHelper.getWaretype(),payHelper.getPayType(),payHelper.getNum());
        }
        Toast.makeText(mContext,mResources.getString(R.string.zhifu_chenggong),Toast.LENGTH_SHORT).show();
        HistoryPayInfo info = new HistoryPayInfo();
        if (waretype== MainActivity.singleType){
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(mContext,smDialog.getSaveCommondityKey()); //保存支付功能KEY
            LBStat.click(StatisticsConstans.getTjValue(smDialog.getSaveCommondityKey())); //统计支付成功量
            info.setPayName(smDialog.getContentName());
        }else{
            SPAuxiliaryPayUtils.setWsAddFriendAll(mContext);//保存支付功能KEY
            LBStat.click(StatisticsConstans.TJ_PAY_JHYALL);//统计支付成功量
            info.setPayName(mResources.getString(R.string.wx_addf_jf_allpay));
        }

        info.setPayDate(HistoryPayShareUtil.getSystemTime(VApp.DATE_NOSECOND));
        info.setPayNumber(payHelper.getNum());
        info.setPayType(payHelper.getPayType());
        HistoryPayShareUtil.setHistoryPayList(mContext, info);

        //统计支付成功
        LBStat.pay(payHelper.getPayType(),
                payHelper.getorderNumber(),
                true,
                waretype==MainActivity.singleType? StatisticsConstans.getPayTagValue(smDialog.getSaveCommondityKey()):StatisticsConstans.TJ_PAYTYPE_JHYALL,
                Float.valueOf(payHelper.getNum()),
                "");

        countUtil.doPayNumTj(true, payHelper.getorderNumber(), payHelper.getNum(), payHelper.getPayType(), "");
        if (smDialog.isShowDialog())
            smDialog.dismiss();
    }

    @Override
    public void failClick() {
        Toast.makeText(mContext,mResources.getString(R.string.zhifu_shibai),Toast.LENGTH_SHORT).show();
        LBStat.pay(payHelper.getPayType(),
                payHelper.getorderNumber(),
                false,
                payHelper.getWaretype()==MainActivity.singleType? StatisticsConstans.getPayTagValue(smDialog.getSaveCommondityKey()):StatisticsConstans.TJ_PAYTYPE_JHYALL,
                Float.valueOf(payHelper.getNum()),
                "");
        countUtil.doPayNumTj(false, payHelper.getorderNumber(), payHelper.getNum(), payHelper.getPayType(),"");
//        onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(MotionEvent.ACTION_DOWN,KeyEvent.KEYCODE_BACK));
    }

    @Override
    public void exceprtion(){

    }

    @Override
    public void anginQuery(){

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        VApp.getInstance().removeAddFriendsFunctionA();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (!smDialog.isShowDialog()){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(500);
//                        myHandler.sendEmptyMessage(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(500);
//                    myHandler.sendEmptyMessage(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Map<String,Object> map=SharedPreferencesUtils.getOrderInfo(AddFriendsFunctionA.this);
            boolean status= (boolean) map.get("orderstatus");
            String orderid=map.get("orderid").toString();
            if (status==false&&!orderid.isEmpty()){
                angin_query_dialog.show();
            }
        }
    };

    public static android.app.AlertDialog angin_query_dialog;
    public void anginQueryDialog(){
        angin_query_dialog=new android.app.AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(R.layout.layout_anginquery,null);
        angin_query_dialog.setView(view);
        angin_query_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        angin_query_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        angin_query_dialog.setCanceledOnTouchOutside(false);
        angin_query_dialog.setCancelable(true);
        Button anginquery= (Button) view.findViewById(R.id.anginquery);
        TextView cancle= (TextView) view.findViewById(R.id.cancle);
        //再次查询监听事件
        anginquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map1=SharedPreferencesUtils.getOrderInfo(AddFriendsFunctionA.this);
                boolean status= (boolean) map1.get("orderstatus");
                String orderid1=map1.get("orderid").toString();
                if (status==true&&orderid1.isEmpty())
                    return;

                Map<String,Object> map= SharedPreferencesUtils.getOrderInfo(AddFriendsFunctionA.this);
                String orderid=map.get("orderid").toString();
                String queryType=map.get("paytype").toString();
                int waretype= (int) map.get("waretype");
                String number = (String) map.get("number");
                SharedPreferencesUtils.saveOrderInfo(mContext,orderid,true,waretype,queryType,number);
                if (!orderid.isEmpty()){
                    String requreUrl="";
                    if (queryType.equals("weixin")){
                        requreUrl = "http://api.duokai.mxitie.com/cgi/pay.ashx/order/info.json?" +
                                "orderno="+orderid+"&paytype=weixin";
                    }else if(queryType.equals("alipay")){
                        requreUrl = "http://api.duokai.mxitie.com/cgi/pay.ashx/order/info.json?" +
                                "orderno="+orderid+"&paytype=alipay";
                    }
                    if (!requreUrl.isEmpty()){
                        getHelper().setClick(AddFriendsFunctionA.this);
                        getHelper().setWaretype(waretype);
                        getHelper().setPayType(queryType);
                        getHelper().setNum(number);
                        getHelper().setorderNumber(orderid);
                        getHelper().requreHttpAsync(requreUrl, AddFriendsFunctionA.this);
                    }
                }
                angin_query_dialog.dismiss();
            }
        });

        //再次查询dialog取消监听事件
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Map<String,Object> map=SharedPreferencesUtils.getOrderInfo(AddFriendsFunctionA.this);
                String orderid=map.get("orderid").toString();
                String queryType=map.get("paytype").toString();
                int waretype= (int) map.get("waretype");
                String number = (String) map.get("number");
                SharedPreferencesUtils.saveOrderInfo(mContext,orderid,true,waretype,queryType,number);
                //SharedPreferencesUtils.cancleSaveOrderId(MainActivity.this);
                angin_query_dialog.dismiss();
            }
        });

        angin_query_dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK&& keyEvent.getRepeatCount() == 0) {
                    if (angin_query_dialog.isShowing()){
                        Map<String,Object> map=SharedPreferencesUtils.getOrderInfo(AddFriendsFunctionA.this);
                        String orderid=map.get("orderid").toString();
                        String queryType=map.get("paytype").toString();
                        int waretype= (int) map.get("waretype");
                        String number = (String) map.get("number");
                        SharedPreferencesUtils.saveOrderInfo(mContext,orderid,true,waretype,queryType,number);
                    }
                }
                angin_query_dialog.dismiss();
                return false;
            }
        });
    }
}
