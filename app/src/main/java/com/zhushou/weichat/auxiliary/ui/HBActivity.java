package com.zhushou.weichat.auxiliary.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lbsw.stat.LBStat;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.auxiliary.base.BaseAuxiliaryActivity;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryConstans;
import com.zhushou.weichat.auxiliary.sp.SPAuxiliaryPayUtils;
import com.zhushou.weichat.auxiliary.ui.mode.HbContacts;
import com.zhushou.weichat.auxiliary.ui.mode.HbInterfaceGroup;
import com.zhushou.weichat.auxiliary.ui.view.RiseNumberTextView;
import com.zhushou.weichat.bean.HistoryPayInfo;
import com.zhushou.weichat.bean.StatisticsConstans;
import com.zhushou.weichat.bean.UMengStatisticalKey;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.ui.MainActivity;
import com.zhushou.weichat.ui.pay.PayHelper;
import com.zhushou.weichat.ui.pay.SmallMerchantPayDialog;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;
import com.zhushou.weichat.utils.CountUtil;
import com.zhushou.weichat.utils.HistoryPayShareUtil;
import com.zhushou.weichat.utils.SharedPreferencesUtils;
import com.zhushou.weichat.utils.UMengTJUtils;

import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */

public class HBActivity extends BaseAuxiliaryActivity implements PayHelper.RequreClick,CompoundButton.OnCheckedChangeListener,HbInterfaceGroup.ViewContacts{

    private CountUtil countUtil;
    private HbContacts hbContacts;// 界面功能代码执行
    public SmallMerchantPayDialog smDialog ;
    private UpdatePriceInfo updateMessageInfo;
    private PayHelper payHelper; // 支付
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setContentView(R.layout.activity_auxiliary_hb);
        VApp.getInstance().setHbActivity(this);
        hbContacts = new HbContacts(this,this);
    }

    private ScrollView sl_red_packets;
    private ImageView iv_back;//返回
    private LinearLayout ll_loading;//loading
    private TextView tv_scroll;//滚动View
    private TextView tv_hb_userid;//用户id
    private TextView tv_red_packets_stauts; // 红包开启状态
    private RiseNumberTextView tv_red_number;//已抢红包数量
    private RiseNumberTextView tv_red_money;//已抢金额
    private TextView tv_intent_help;//帮助界面跳转
    private TextView tv_red_packets_record;//红包记录界面跳转
    private ImageView iv_animation;//红包界面动画

    private RelativeLayout rl_red_packets_xp;//息屏抢红包
    private Switch weitch_xp;//
    private RelativeLayout rl_red_packets_dx;//自动答谢
    private Switch switch_dx;//
    private RelativeLayout rl_red_packets_hb;//开启自动抢红包
    private Switch switch_hb;//
    private RelativeLayout rl_red_packets_back;//自动返回聊天页
    private Switch switch_back;//
    private RelativeLayout rl_red_packets_voice;//开启声音提示
    private Switch switch_voice;//

    private Switch switch_svip_hb;// svip红包全部功能
    private RelativeLayout rl_shouqi;
    private Switch switch_shouqi;// 提高手气最佳概率
    private RelativeLayout rl_dabao;
    private Switch switch_dabao;// 提高抢大包概率
    private RelativeLayout rl_jiasudu;
    private Switch switch_jiasudu;// 抢红包加速度
    private RelativeLayout rl_ganrao;
    private Switch switch_ganrao;// 抢红包干扰竞争者
    private RelativeLayout rl_xiaobo;
    private Switch switch_xiaobao;//最高概率躲避小包



    @Override
    public void initView(){
        boolean lll= true;
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        sl_red_packets = (ScrollView) findViewById(R.id.sl_red_packets);

        hbContacts.initViewStatus();

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_scroll = (TextView) findViewById(R.id.tv_scroll);
        tv_hb_userid = (TextView) findViewById(R.id.tv_hb_userid);
        tv_red_packets_stauts = (TextView) findViewById(R.id.tv_red_packets_stauts);
        tv_red_number = (RiseNumberTextView) findViewById(R.id.tv_red_number);
        tv_red_money = (RiseNumberTextView) findViewById(R.id.tv_red_money);
        tv_intent_help = (TextView) findViewById(R.id.tv_intent_help);
        tv_intent_help = (TextView) findViewById(R.id.tv_intent_help);
        tv_red_packets_record = (TextView) findViewById(R.id.tv_red_packets_record);
        iv_animation = (ImageView) findViewById(R.id.iv_animation);

        rl_red_packets_xp = (RelativeLayout) findViewById(R.id.rl_red_packets_xp);
        weitch_xp = (Switch) findViewById(R.id.weitch_xp);
        rl_red_packets_dx = (RelativeLayout) findViewById(R.id.rl_red_packets_dx);
        switch_dx = (Switch) findViewById(R.id.switch_dx);
        rl_red_packets_hb = (RelativeLayout) findViewById(R.id.rl_red_packets_hb);
        switch_hb = (Switch) findViewById(R.id.switch_hb);
        rl_red_packets_back = (RelativeLayout) findViewById(R.id.rl_red_packets_back);
        switch_back = (Switch) findViewById(R.id.switch_back);
        rl_red_packets_voice = (RelativeLayout) findViewById(R.id.rl_red_packets_voice);
        switch_voice = (Switch) findViewById(R.id.switch_voice);

        findViewById(R.id.rl_svip_hb).setOnClickListener(this);
        switch_svip_hb = (Switch) findViewById(R.id.switch_svip_hb);

        rl_shouqi = (RelativeLayout) findViewById(R.id.rl_shouqi);
        rl_shouqi.setOnClickListener(this);
        switch_shouqi = (Switch) findViewById(R.id.switch_shouqi);

        rl_dabao = (RelativeLayout) findViewById(R.id.rl_dabao);
        rl_dabao.setOnClickListener(this);
        switch_dabao = (Switch) findViewById(R.id.switch_dabao);

        rl_xiaobo = (RelativeLayout) findViewById(R.id.rl_xiaobo);
        rl_xiaobo.setOnClickListener(this);
        switch_xiaobao = (Switch) findViewById(R.id.switch_xiaobao);

        rl_jiasudu = (RelativeLayout) findViewById(R.id.rl_jiasudu);
        rl_jiasudu.setOnClickListener(this);
        switch_jiasudu = (Switch) findViewById(R.id.switch_jiasudu);

        rl_ganrao = (RelativeLayout) findViewById(R.id.rl_ganrao);
        rl_ganrao.setOnClickListener(this);
        switch_ganrao = (Switch) findViewById(R.id.switch_ganrao);


        iv_back.setOnClickListener(this);
        tv_intent_help.setOnClickListener(this);
        tv_red_packets_record.setOnClickListener(this);
        iv_animation.setOnClickListener(this);

        rl_red_packets_xp.setOnClickListener(this);
        rl_red_packets_dx.setOnClickListener(this);
        rl_red_packets_hb.setOnClickListener(this);
        rl_red_packets_back.setOnClickListener(this);
        rl_red_packets_voice.setOnClickListener(this);


        switch_svip_hb.setOnCheckedChangeListener(this);
        switch_shouqi.setOnCheckedChangeListener(this);
        switch_dabao.setOnCheckedChangeListener(this);
        switch_jiasudu.setOnCheckedChangeListener(this);
        switch_ganrao.setOnCheckedChangeListener(this);
        switch_xiaobao.setOnCheckedChangeListener(this);

        weitch_xp.setOnCheckedChangeListener(this);
        switch_dx.setOnCheckedChangeListener(this);
        switch_hb.setOnCheckedChangeListener(this);
        switch_back.setOnCheckedChangeListener(this);
        switch_voice.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData(){
//        if (hbContacts==null)
//            hbContacts = new HbContacts(this,this);
        uMengTJUtils = new UMengTJUtils(this);
        countUtil = new CountUtil(mContext);
        updateMessageInfo = APPUsersShareUtil.getUpdateMessage(this);
        payHelper = getHelper();
        smDialog = new SmallMerchantPayDialog(this, this, payHelper);
        smDialog.setDialogTitleName(mResources.getString(R.string.pzy_wx_qhb));
        smDialog.setAllPrice(Float.valueOf(updateMessageInfo.getPc_svip()));
        smDialog.setLifeTimeselectorStr(mResources.getString(R.string.pay_wx_hb_svip));
        anginQueryDialog();

        // 设置数字动画播放时间
        tv_red_number.setDuration(1500);
        tv_red_money.setDuration(1500);

        iv_animation.setBackgroundResource(R.drawable.red_packets_animation);
        AnimationDrawable anim = (AnimationDrawable) iv_animation.getBackground();
        anim.start();

        hbContacts.setSwitchSetStatus(true);

        switch_svip_hb.setChecked(hbContacts.auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_HB_SVIP));

        switch_shouqi.setChecked(hbContacts.auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_HB_SQ));
        switch_dabao.setChecked(hbContacts.auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_HB_DB));
        switch_xiaobao.setChecked(hbContacts.auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_HB_XB));
        switch_jiasudu.setChecked(hbContacts.auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_HB_JSD));
        switch_ganrao.setChecked(hbContacts.auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_HB_GR));

        rl_shouqi.setVisibility(updateMessageInfo.isShouqi()||SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext,AuxiliaryConstans.C_HB_SQ)?View.VISIBLE:View.GONE);
        rl_dabao.setVisibility(updateMessageInfo.isDabao()||SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext,AuxiliaryConstans.C_HB_DB)?View.VISIBLE:View.GONE);
        rl_jiasudu.setVisibility(updateMessageInfo.isJiasudu()||SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext,AuxiliaryConstans.C_HB_JSD)?View.VISIBLE:View.GONE);
        rl_ganrao.setVisibility(updateMessageInfo.isGuanrao()||SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext,AuxiliaryConstans.C_HB_GR)?View.VISIBLE:View.GONE);
        rl_xiaobo.setVisibility(updateMessageInfo.isXiaobao()||SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext,AuxiliaryConstans.C_HB_XB)?View.VISIBLE:View.GONE);

        weitch_xp.setChecked(hbContacts.auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_XPHB));
        switch_dx.setChecked(hbContacts.auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_DX));
        switch_hb.setChecked(hbContacts.auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_HB));
        switch_back.setChecked(hbContacts.auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_BACK));
        switch_voice.setChecked(hbContacts.auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_VOICE));
        hbContacts.setSwitchSetStatus(false);

        hbContacts.startViewData();
        hbContacts.updateOpeningFunctionData("",""); // 加载用户随机数据
    }


    @Override
    protected void viewOnClick(View view){
        super.viewOnClick(view);
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_intent_help: // 红包帮助界面跳转
                startActivity(new Intent(HBActivity.this,ACommonProblemActivity.class));
                break;
            case R.id.tv_red_packets_record: //红包记录界面跳转
                startActivity(new Intent(HBActivity.this,HBRecordActivity.class));
                break;
            case R.id.iv_animation:
                hbContacts.hbAnimationClick(switch_hb.isChecked());
                break;
            case R.id.rl_svip_hb://抢红包svip全部功能
                switch_svip_hb.setChecked(!switch_svip_hb.isChecked());
                break;
            case R.id.rl_shouqi:// 提高手气最佳概率
                switch_shouqi.setChecked(!switch_shouqi.isChecked());
                break;
            case R.id.rl_dabao://提高抢大包概率
                switch_dabao.setChecked(!switch_dabao.isChecked());
                break;
            case R.id.rl_xiaobo: //最高概率躲避最小包
                switch_xiaobao.setChecked(!switch_xiaobao.isChecked());
                break;
            case R.id.rl_jiasudu://抢红包加速度
                switch_jiasudu.setChecked(!switch_jiasudu.isChecked());
                break;
            case R.id.rl_ganrao://抢红包干扰竞争者
                switch_ganrao.setChecked(!switch_ganrao.isChecked());
                break;
            case R.id.rl_red_packets_xp: //息屏抢红包
                weitch_xp.setChecked(!weitch_xp.isChecked());
                break;
            case R.id.rl_red_packets_dx: // 自动答谢
                switch_dx.setChecked(!switch_dx.isChecked());
                break;
            case R.id.rl_red_packets_hb: // 抢红包开关
                switch_hb.setChecked(!switch_hb.isChecked());
                break;
            case R.id.rl_red_packets_back: // 抢红包自动返回聊天页面
                switch_back.setChecked(!switch_back.isChecked());
                break;
            case R.id.rl_red_packets_voice: // 抢红包提示音
                switch_voice.setChecked(!switch_voice.isChecked());
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (hbContacts.isSwitchSetStatus())
            return;
        switch (compoundButton.getId()){
            case R.id.switch_svip_hb:
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext,AuxiliaryConstans.C_HB_SVIP)){
                    smDialog.setaPrice(Float.valueOf(updateMessageInfo.getPc_svip()));
                    smDialog.setApkName(mResources.getString(R.string.pay_wx_title_svip));
                    smDialog.setCommodityType(AuxiliaryConstans.C_HB_SVIP);
                    smDialog.setContentName(mResources.getString(R.string.pzy_wx_qhb));
                    smDialog.setDialogTitleName(mResources.getString(R.string.pay_wx_title_svip));
                    smDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.pay_wx_hb_svip),String.valueOf(smDialog.getaPrice())));
                    smDialog.setSaveCommondityKey(AuxiliaryConstans.C_HB_SVIP);
                    smDialog.showPayDialog(mResources.getString(R.string.pay_wx_title_svip),String.valueOf(smDialog.getaPrice()));
                    switch_svip_hb.setChecked(!b);
                }else{
                    hbContacts.auxiliaryFunctionSwitch.setFuncitonSwitchInfo(AuxiliaryConstans.C_HB_SVIP,true);
                }
                break;
            case R.id.switch_shouqi:
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext,AuxiliaryConstans.C_HB_SQ)){
                    smDialog.setaPrice(Float.valueOf(updateMessageInfo.getPc_shouqi()));
                    smDialog.setApkName(mResources.getString(R.string.pay_ws_title_shouqi));
                    smDialog.setCommodityType(AuxiliaryConstans.C_HB_SQ);
                    smDialog.setContentName(mResources.getString(R.string.pay_ws_title_shouqi));
                    smDialog.setDialogTitleName(mResources.getString(R.string.pay_ws_title_shouqi));
                    smDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.pay_ws_hb_shouqi),String.valueOf(smDialog.getaPrice())));
                    smDialog.setSaveCommondityKey(AuxiliaryConstans.C_HB_SQ);
                    smDialog.showPayDialog(mResources.getString(R.string.pay_ws_title_shouqi),String.valueOf(smDialog.getaPrice()));
                    switch_shouqi.setChecked(!b);
                }else{
                    hbContacts.auxiliaryFunctionSwitch.setFuncitonSwitchInfo(AuxiliaryConstans.C_HB_SQ,b);
                }
                break;
            case R.id.switch_dabao:
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext,AuxiliaryConstans.C_HB_DB)){
                    smDialog.setaPrice(Float.valueOf(updateMessageInfo.getPc_dabao()));
                    smDialog.setApkName(mResources.getString(R.string.pay_ws_title_dabao));
                    smDialog.setCommodityType(AuxiliaryConstans.C_HB_DB);
                    smDialog.setContentName(mResources.getString(R.string.pay_ws_title_dabao));
                    smDialog.setDialogTitleName(mResources.getString(R.string.pay_ws_title_dabao));
                    smDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.pay_ws_hb_dabao),String.valueOf(smDialog.getaPrice())));
                    smDialog.setSaveCommondityKey(AuxiliaryConstans.C_HB_DB);
                    smDialog.showPayDialog(mResources.getString(R.string.pay_ws_title_dabao),String.valueOf(smDialog.getaPrice()));
                    switch_dabao.setChecked(!b);
                }else{
                    hbContacts.auxiliaryFunctionSwitch.setFuncitonSwitchInfo(AuxiliaryConstans.C_HB_DB,b);
                }
                break;
            case R.id.switch_xiaobao:
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext,AuxiliaryConstans.C_HB_XB)){
                    smDialog.setaPrice(Float.valueOf(updateMessageInfo.getPc_xiaobao()));
                    smDialog.setApkName(mResources.getString(R.string.pay_wx_hb_xiaobo_title));
                    smDialog.setCommodityType(AuxiliaryConstans.C_HB_XB);
                    smDialog.setContentName(mResources.getString(R.string.pay_wx_hb_xiaobo_title));
                    smDialog.setDialogTitleName(mResources.getString(R.string.pay_wx_hb_xiaobo_title));
                    smDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.pay_wx_hb_xiaobo),String.valueOf(smDialog.getaPrice())));
                    smDialog.setSaveCommondityKey(AuxiliaryConstans.C_HB_XB);
                    smDialog.showPayDialog(mResources.getString(R.string.pay_wx_hb_xiaobo_title),String.valueOf(smDialog.getaPrice()));
                    switch_xiaobao.setChecked(!b);
                }else{
                    hbContacts.auxiliaryFunctionSwitch.setFuncitonSwitchInfo(AuxiliaryConstans.C_HB_XB,b);
                }
                break;
            case R.id.switch_jiasudu:
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext,AuxiliaryConstans.C_HB_JSD)){
                    smDialog.setaPrice(Float.valueOf(updateMessageInfo.getPc_jiasudu()));
                    smDialog.setApkName(mResources.getString(R.string.pay_ws_title_jiasudu));
                    smDialog.setCommodityType(AuxiliaryConstans.C_HB_JSD);
                    smDialog.setContentName(mResources.getString(R.string.pay_ws_title_jiasudu));
                    smDialog.setDialogTitleName(mResources.getString(R.string.pay_ws_title_jiasudu));
                    smDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.pay_ws_hb_jiasudu),String.valueOf(smDialog.getaPrice())));
                    smDialog.setSaveCommondityKey(AuxiliaryConstans.C_HB_JSD);
                    smDialog.showPayDialog(mResources.getString(R.string.pay_ws_title_jiasudu),String.valueOf(smDialog.getaPrice()));
                    switch_jiasudu.setChecked(!b);
                }else{
                    hbContacts.auxiliaryFunctionSwitch.setFuncitonSwitchInfo(AuxiliaryConstans.C_HB_JSD,b);
                }
                break;
            case R.id.switch_ganrao:
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext,AuxiliaryConstans.C_HB_GR)){
                    smDialog.setaPrice(Float.valueOf(updateMessageInfo.getPc_ganrao()));
                    smDialog.setApkName(mResources.getString(R.string.pay_ws_title_ganrao));
                    smDialog.setCommodityType(AuxiliaryConstans.C_HB_GR);
                    smDialog.setContentName(mResources.getString(R.string.pay_ws_title_ganrao));
                    smDialog.setDialogTitleName(mResources.getString(R.string.pay_ws_title_ganrao));
                    smDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.pay_ws_hb_ganrao),String.valueOf(smDialog.getaPrice())));
                    smDialog.setSaveCommondityKey(AuxiliaryConstans.C_HB_GR);
                    smDialog.showPayDialog(mResources.getString(R.string.pay_ws_title_ganrao),String.valueOf(smDialog.getaPrice()));
                    switch_ganrao.setChecked(!b);
                }else{
                    hbContacts.auxiliaryFunctionSwitch.setFuncitonSwitchInfo(AuxiliaryConstans.C_HB_GR,b);
                }
                break;
            case R.id.weitch_xp: // 息屏抢红包开关
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext,AuxiliaryConstans.C_XPHB)){
                    smDialog.setaPrice(Float.valueOf(updateMessageInfo.getPc_xpqhb()));
                    smDialog.setApkName(mResources.getString(R.string.pay_ws_title_xpqhb));
                    smDialog.setCommodityType(AuxiliaryConstans.C_XPHB);
                    smDialog.setContentName(mResources.getString(R.string.pay_ws_title_xpqhb));
                    smDialog.setDialogTitleName(mResources.getString(R.string.pay_ws_title_xpqhb));
                    smDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.pay_ws_hb_xpqhb),String.valueOf(smDialog.getaPrice())));
                    smDialog.setSaveCommondityKey(AuxiliaryConstans.C_XPHB);
                    smDialog.showPayDialog(mResources.getString(R.string.pay_ws_title_xpqhb),String.valueOf(smDialog.getaPrice()));
                    weitch_xp.setChecked(!b);
                }else{
                    hbContacts.auxiliaryFunctionSwitch.setFuncitonSwitchInfo(AuxiliaryConstans.C_XPHB,b);
                }
                break;
            case R.id.switch_dx: // 自动答谢开关
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext,AuxiliaryConstans.C_DX)){
                    smDialog.setaPrice(Float.valueOf(updateMessageInfo.getPc_zddx()));
                    smDialog.setApkName(mResources.getString(R.string.pay_ws_title_zddx));
                    smDialog.setCommodityType(AuxiliaryConstans.C_DX);
                    smDialog.setContentName(mResources.getString(R.string.pay_ws_title_zddx));
                    smDialog.setDialogTitleName(mResources.getString(R.string.pay_ws_title_zddx));
                    smDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.pay_ws_hb_zddx),String.valueOf(smDialog.getaPrice())));
                    smDialog.setSaveCommondityKey(AuxiliaryConstans.C_DX);
                    smDialog.showPayDialog(mResources.getString(R.string.pay_ws_title_zddx),String.valueOf(smDialog.getaPrice()));
                    switch_dx.setChecked(!b);
                }else{
                    hbContacts.auxiliaryFunctionSwitch.setFuncitonSwitchInfo(AuxiliaryConstans.C_DX,b);//保存状态
                }
                break;
            case R.id.switch_hb: // 抢红包状态开关
                hbContacts.auxiliaryFunctionSwitch.setFuncitonSwitchInfo(AuxiliaryConstans.C_HB,b);//保存状态
                hbContacts.settingGrabRedPacketsStatus(); // 更新抢红包状态
                break;
            case R.id.switch_voice: // 抢红包提示音开关
                hbContacts.auxiliaryFunctionSwitch.setFuncitonSwitchInfo(AuxiliaryConstans.C_VOICE,b);//保存状态
                break;
        }
    }


    @Override
    protected void onDestroy(){
        VApp.getInstance().removeHbActivity();
        super.onDestroy();
    }

    @Override
    public void setRedPacketsNumberAndMoney(float number, float money) {
        int oldNmber = Integer.valueOf(tv_red_number.getText().toString());
        float oldMoney = Float.valueOf(tv_red_money.getText().toString());
        if (oldNmber!=(int)number){
            tv_red_number.withNumber((int)number);
            tv_red_number.start();
        }
        if (oldMoney!=money){
            tv_red_money.withNumber(money);
            tv_red_money.start();
        }
    }

    @Override
    public void setGrabRedPacketsStatus(boolean status) {
        tv_red_packets_stauts.setText(status?mResources.getString(R.string.wx_hb_open_true):mResources.getString(R.string.wx_hb_open_false));
    }

    @Override
    public void loadingStatus(boolean status) {
        sl_red_packets.setVisibility(status?View.GONE:View.VISIBLE);
        ll_loading.setVisibility(status?View.VISIBLE:View.GONE);
    }

    @Override
    public void updateOpeningFunctionData(String UsersData) {
        tv_scroll.setText(UsersData);
    }

    @Override
    public void setUserNumber(String UserNumber) {
        tv_hb_userid.setText(String.format(mResources.getString(R.string.hb_user_id),UserNumber));
    }

    @Override
    public void setGrabRedPacketsSwichStatus(boolean status) {
        if (switch_hb==null)
            return;
        switch_hb.setChecked(status);
        Toast.makeText(mContext,status?mResources.getString(R.string.open_hb_status):mResources.getString(R.string.close_hb_status),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        hbContacts.updateRedPacktsNumberAndMoney(); // 更新已抢红包数量和金额
        hbContacts.settingGrabRedPacketsStatus(); // 更新抢红包状态
//        hbContacts.updateOpeningFunctionData("","");

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
//    Handler myHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
////            installedNumber+=1;
//            switch (msg.what) {
//                case 1:
////                    exitAnginQuery();
//                    break;
//            }
//
//
//        }
//    };

    //二次查询
    private void exitAnginQuery(){
        Map<String,Object> map=SharedPreferencesUtils.getOrderInfo(HBActivity.this);
        boolean status= (boolean) map.get("orderstatus");
        String orderid=map.get("orderid").toString();
        if (status==false&&!orderid.isEmpty()){
            angin_query_dialog.show();
        }
    }

    private UMengTJUtils uMengTJUtils;
    @Override
    public void successfulclick(String apkName, int type, int waretype) {

        //获取统计map信息
        Map<String, String> umengMap = uMengTJUtils.getEventValuePayMap(payHelper.getPayType(),
                payHelper.getorderNumber(),
                payHelper.getNum());
        //获取支付过程时长
        int payTime = (int) (System.currentTimeMillis() - smDialog.getPaymentProcesTime());


        SharedPreferencesUtils.saveOrderInfo(mContext,payHelper.getorderNumber(),true,payHelper.getWaretype(),payHelper.getPayType(),payHelper.getNum());
        Toast.makeText(mContext,mResources.getString(R.string.zhifu_chenggong),Toast.LENGTH_SHORT).show();
        HistoryPayInfo info = new HistoryPayInfo();
        if (waretype== MainActivity.singleType){
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(mContext,smDialog.getSaveCommondityKey()); //保存支付功能KEY
//            LBStat.click(StatisticsConstans.getTjValue(smDialog.getSaveCommondityKey())); //统计支付成功量
            info.setPayName(smDialog.getContentName());
            hbContacts.updateOpeningFunctionData(hbContacts.getUserId(),smDialog.getContentName());
        }else{
            SPAuxiliaryPayUtils.setHBFunctionALL(mContext);//保存支付功能KEY
//            LBStat.click(StatisticsConstans.getTjValue(StatisticsConstans.TJ_PAY_HF_ALL));//统计支付成功量
            info.setPayName(mResources.getString(R.string.pay_wx_title_svip));
            hbContacts.updateOpeningFunctionData(hbContacts.getUserId(),mResources.getString(R.string.pay_wx_title_svip));
        }

        info.setPayDate(HistoryPayShareUtil.getSystemTime(VApp.DATE_NOSECOND));
        info.setPayNumber(payHelper.getNum());
        info.setPayType(payHelper.getPayType());
        HistoryPayShareUtil.setHistoryPayList(mContext, info);

        uMengTJUtils.onEventValuePay(UMengStatisticalKey.selectUmenfunctionPayHBKey(smDialog.getSaveCommondityKey()), umengMap, payTime);
        //统计支付成功
        LBStat.pay(payHelper.getPayType(),
                payHelper.getorderNumber(),
                true,
                StatisticsConstans.getPayTagValue(smDialog.getSaveCommondityKey()),
                Float.valueOf(payHelper.getNum()),
                payHelper.getPayTunnel());

        countUtil.doPayNumTj(true, payHelper.getorderNumber(), payHelper.getNum(), payHelper.getPayType(), "");
        if (smDialog.isShowDialog())
            smDialog.dismiss();
        smDialog.dismiss();
    }

    @Override
    public void failClick() {
        Toast.makeText(mContext,mResources.getString(R.string.zhifu_shibai),Toast.LENGTH_SHORT).show();
//        LBStat.pay(payHelper.getPayType(),
//                payHelper.getorderNumber(),
//                false,
//                StatisticsConstans.getPayTagValue(smDialog.getSaveCommondityKey()),
//                Float.valueOf(payHelper.getNum()),
//                "");
        countUtil.doPayNumTj(false, payHelper.getorderNumber(), payHelper.getNum(), payHelper.getPayType(),"");
    }

    @Override
    public void exceprtion() {

    }

    @Override
    public void anginQuery() {

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

                Map<String,Object> map1= SharedPreferencesUtils.getOrderInfo(HBActivity.this);
                boolean status= (boolean) map1.get("orderstatus");
                String orderid1=map1.get("orderid").toString();
                if (status==true&&orderid1.isEmpty())
                    return;

                Map<String,Object> map= SharedPreferencesUtils.getOrderInfo(HBActivity.this);
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
                        getHelper().setClick(HBActivity.this);
                        getHelper().setWaretype(waretype);
                        getHelper().setPayType(queryType);
                        getHelper().setNum(number);
                        getHelper().setorderNumber(orderid);
                        getHelper().requreHttpAsync(requreUrl, HBActivity.this);
                    }
                }
                angin_query_dialog.dismiss();
            }
        });
        //再次查询dialog取消监听事件
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map=SharedPreferencesUtils.getOrderInfo(HBActivity.this);
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
                        Map<String,Object> map=SharedPreferencesUtils.getOrderInfo(HBActivity.this);
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
