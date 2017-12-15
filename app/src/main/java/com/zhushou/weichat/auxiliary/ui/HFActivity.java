package com.zhushou.weichat.auxiliary.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lbsw.stat.LBStat;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.auxiliary.base.BaseAuxiliaryActivity;
import com.zhushou.weichat.auxiliary.interfac.ui.HFActivityImpl;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryConstans;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryFunctionSwitch;
import com.zhushou.weichat.auxiliary.sp.SPAuxiliaryPayUtils;
import com.zhushou.weichat.auxiliary.ui.impl.HFImpl;
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
 * Created by Administrator on 2017/3/15.
 */

public class HFActivity extends BaseAuxiliaryActivity implements CompoundButton.OnCheckedChangeListener,HFActivityImpl.MyHFActivityImpl,PayHelper.RequreClick {

    private CountUtil countUtil;

    private Context mContext;
    private Switch st_hf_switch,st_xiping_switch;
    private EditText et_hf_content;
    private TextView btn_save,tv_hf_layout_alert;
    private SmallMerchantPayDialog smDialog; // 微商支付弹窗
    private PayHelper payHelper; // 支付
    private AuxiliaryFunctionSwitch auxiliaryFunctionSwitch; // 功能开关状态存取

    private HFActivityImpl.MyHFImpl myHFImpl ;
    private UpdatePriceInfo updateMessageInfo;

    @Override
    protected void init(Bundle savedInstanceState){
        super.init(savedInstanceState);
        setContentView(R.layout.activity_auxiliary_hf);
        VApp.getInstance().setHfActivity(this);
        mContext = this;
        auxiliaryFunctionSwitch = new AuxiliaryFunctionSwitch(this);
    }

    @Override
    public void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.rl_hf_switch).setOnClickListener(this);
        findViewById(R.id.rl_xiping_switch).setOnClickListener(this);
        findViewById(R.id.tv_to_help).setOnClickListener(this);

        st_hf_switch = (Switch) findViewById(R.id.st_hf_switch); // 自动回复开关
        st_xiping_switch = (Switch) findViewById(R.id.st_xiping_switch);  // 息屏自动回复开关
        et_hf_content = (EditText) findViewById(R.id.et_hf_content); // 自动回复内容输入框
        btn_save = (TextView) findViewById(R.id.btn_save); // 自动回复内容保存按钮

        tv_hf_layout_alert = (TextView) findViewById(R.id.tv_hf_layout_alert);//自动回复主界面友情提示

        btn_save.setOnClickListener(this);
        st_hf_switch.setOnCheckedChangeListener(this);
        st_xiping_switch.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData(){
        countUtil = new CountUtil(mContext);
        updateMessageInfo = APPUsersShareUtil.getUpdateMessage(this);
        payHelper = getHelper();
        smDialog = new SmallMerchantPayDialog(this, this, payHelper);
        smDialog.setDialogTitleName(mResources.getString(R.string.weichat_ws_hf_function));
        smDialog.setAllPrice(Float.valueOf(updateMessageInfo.getWeichat_hf_all_price()));
        smDialog.setaPrice(Float.valueOf(updateMessageInfo.getWeichat_hf_dg_price()));
        smDialog.setLifeTimeselectorStr(mResources.getString(R.string.pay_ws_huifu_all));
        myHFImpl = new HFImpl(this,this);
        anginQueryDialog();

        et_hf_content.setHint(auxiliaryFunctionSwitch.getLeavingMessage());

        st_hf_switch.setChecked(auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_HF));
        st_xiping_switch.setChecked(auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_XPHF));

        tv_hf_layout_alert.setText(String.format(mResources.getString(R.string.weichat_ws_fuzhutishi),updateMessageInfo.getWeichat_version_code()));
    }

    @Override
    protected void viewOnClick(View view) {
        super.viewOnClick(view);
        switch(view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_save:
                //2017/4/12 修改自动回复支付逻辑 支付改至一级界面
//                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext,AuxiliaryConstans.C_LYHF)){
//                    smDialog.setApkName(getPayName(AuxiliaryConstans.C_LYHF));
//                    smDialog.setCommodityType(AuxiliaryConstans.C_LYHF);
//                    smDialog.setContentName(mResources.getString(R.string.weichat_name_hf_nr));
//                    smDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.pay_ws_huifu_nr),String.valueOf(smDialog.getaPrice())));
//                    smDialog.setSaveCommondityKey(AuxiliaryConstans.C_LYHF);
//                    smDialog.showPayDialog(getPayName(AuxiliaryConstans.C_LYHF),String.valueOf(smDialog.getaPrice()));
//                }else{
                    //保存回复内容
                    String leavingmessage = et_hf_content.getText().toString();
                    if (!leavingmessage.equals("")){
                        auxiliaryFunctionSwitch.saveLeavingMessage(leavingmessage);
                        Toast.makeText(mContext,mResources.getString(R.string.weichat_hf_lybc),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext,mResources.getString(R.string.input_not),Toast.LENGTH_SHORT).show();
//                    }
                }
                break;
            case R.id.rl_hf_switch:
                st_hf_switch.setChecked(!st_hf_switch.isChecked());
                break;
            case R.id.rl_xiping_switch:
                st_xiping_switch.setChecked(!st_xiping_switch.isChecked());
                break;
            case R.id.tv_to_help:
                startActivity(new Intent(HFActivity.this,ACommonProblemActivity.class));
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b){
        switch (compoundButton.getId()){
            case R.id.st_hf_switch: //自动回复开关
                auxiliaryFunctionSwitch.setFuncitonSwitchInfo(AuxiliaryConstans.C_HF,b);
                break;
            case R.id.st_xiping_switch: //息屏自动回复开关
                //2017/4/12 修改自动回复支付逻辑 支付改至一级界面
//                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext,AuxiliaryConstans.C_XPHF)){
//                    smDialog.setApkName(getPayName(AuxiliaryConstans.C_XPHF));
//                    smDialog.setCommodityType(AuxiliaryConstans.C_XPHF);
//                    smDialog.setContentName(mResources.getString(R.string.weichat_name_hf_xp));
//                    smDialog.setDialogTitleName(mResources.getString(R.string.weichat_ws_hf_function));
//                    smDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.pay_ws_huifu_xp),String.valueOf(smDialog.getaPrice())));
//                    smDialog.setSaveCommondityKey(AuxiliaryConstans.C_XPHF);
//                    smDialog.showPayDialog(getPayName(AuxiliaryConstans.C_XPHF),String.valueOf(smDialog.getaPrice()));
//                    st_xiping_switch.setChecked(!b);
//                }else{
                        auxiliaryFunctionSwitch.setFuncitonSwitchInfo(AuxiliaryConstans.C_XPHF,b);
//                }
                break;
        }
    }

    @Override
    public void successfulclick(String apkName, int type, int waretype){

        SharedPreferencesUtils.saveOrderInfo(mContext,payHelper.getorderNumber(),true,payHelper.getWaretype(),payHelper.getPayType(),payHelper.getNum());

        Toast.makeText(mContext,mResources.getString(R.string.zhifu_chenggong),Toast.LENGTH_SHORT).show();
        HistoryPayInfo info = new HistoryPayInfo();
        if (waretype==MainActivity.singleType){
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(mContext,smDialog.getSaveCommondityKey()); //保存支付功能KEY
            LBStat.click(StatisticsConstans.getTjValue(smDialog.getSaveCommondityKey())); //统计支付成功量
            info.setPayName(smDialog.getContentName());
        }else{
            SPAuxiliaryPayUtils.setWsFunctionHFAll(mContext);//保存支付功能KEY
            LBStat.click(StatisticsConstans.TJ_PAY_HF_ALL);//统计支付成功量
            info.setPayName(mResources.getString(R.string.weichat_name_hf_all));
        }

        info.setPayDate(HistoryPayShareUtil.getSystemTime(VApp.DATE_NOSECOND));
        info.setPayNumber(payHelper.getNum());
        info.setPayType(payHelper.getPayType());
        HistoryPayShareUtil.setHistoryPayList(mContext, info);

        //统计支付成功
        LBStat.pay(payHelper.getPayType(),
                payHelper.getorderNumber(),
                true,
                waretype==MainActivity.singleType? StatisticsConstans.getPayTagValue(smDialog.getSaveCommondityKey()):StatisticsConstans.TJ_PAYTYPE_HFALL,
                Float.valueOf(payHelper.getNum()),
                "");

        countUtil.doPayNumTj(true, payHelper.getorderNumber(), payHelper.getNum(), payHelper.getPayType(), "");
        if (smDialog.isShowDialog())
            smDialog.dismiss();
    }

    @Override
    public void failClick(){

        Toast.makeText(mContext,mResources.getString(R.string.zhifu_shibai),Toast.LENGTH_SHORT).show();
        LBStat.pay(payHelper.getPayType(),
                payHelper.getorderNumber(),
                false,
                payHelper.getWaretype()==MainActivity.singleType? StatisticsConstans.getPayTagValue(smDialog.getSaveCommondityKey()):StatisticsConstans.TJ_PAYTYPE_HFALL,
                Float.valueOf(payHelper.getNum()),
                "");
        countUtil.doPayNumTj(false, payHelper.getorderNumber(), payHelper.getNum(), payHelper.getPayType(),"");
    }

    @Override
    public void exceprtion(){

    }

    @Override
    public void anginQuery(){

    }

    public static AlertDialog angin_query_dialog;
    public void anginQueryDialog(){
        angin_query_dialog=new AlertDialog.Builder(this).create();
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

                Map<String,Object> map1=SharedPreferencesUtils.getOrderInfo(HFActivity.this);
                boolean status= (boolean) map1.get("orderstatus");
                String orderid1=map1.get("orderid").toString();
                if (status==true&&orderid1.isEmpty())
                    return;

                Map<String,Object> map= SharedPreferencesUtils.getOrderInfo(HFActivity.this);
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
                        getHelper().setClick(HFActivity.this);
                        getHelper().setWaretype(waretype);
                        getHelper().setPayType(queryType);
                        getHelper().setNum(number);
                        getHelper().setorderNumber(orderid);
                        getHelper().requreHttpAsync(requreUrl, HFActivity.this);
                    }
                }
                angin_query_dialog.dismiss();
            }
        });
        //再次查询dialog取消监听事件
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map=SharedPreferencesUtils.getOrderInfo(HFActivity.this);
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
                        Map<String,Object> map=SharedPreferencesUtils.getOrderInfo(HFActivity.this);
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


    private String payFunctionkey = "";
    @Override
    public void functionPay(String payName, String payPrice, String payFunctionKey) {
        this.payFunctionkey = payFunctionKey;

    }

    @Override
    public void functionClickBack(String functionKey, boolean payStatus) {

    }


    private String getPayName(String functionKey){
        String payname = "";
        if (functionKey.equals("C_XPHF")){
            payname = mResources.getString(R.string.weichat_hf_xphf);
        }else if(functionKey.equals("C_LYHF")){
            payname = mResources.getString(R.string.weichat_hf_hfnr);
        }else if(functionKey.equals("C_HF")){
            payname = mResources.getString(R.string.weichat_hf_hf);
        }
        return payname;
    }

    private String getPayPrice(String functionKey){
        String payname = "";
        if (functionKey.equals("C_XPHF")){
            payname = updateMessageInfo.getWeichat_xphf_price();
        }else if(functionKey.equals("C_LYHF")){
            payname = updateMessageInfo.getWeichat_hf_dg_price();
        }else if(functionKey.equals("C_HF")){
//            payname = mResources.getString(R.string.weichat_hf_hf);
            payname = "0";
        }
        return payname;
    }

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
            Map<String,Object> map=SharedPreferencesUtils.getOrderInfo(HFActivity.this);
            boolean status= (boolean) map.get("orderstatus");
            String orderid=map.get("orderid").toString();
            if (status==false&&!orderid.isEmpty()){
                angin_query_dialog.show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: HFActivity");
        VApp.getInstance().removeHFActivity();
    }
}
