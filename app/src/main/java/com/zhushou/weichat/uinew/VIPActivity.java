package com.zhushou.weichat.uinew;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.lbsw.stat.LBStat;
import com.zhushou.weichat.R;
import com.zhushou.weichat.addfriends.ui.AddFriendsFunctionA;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryConstans;
import com.zhushou.weichat.auxiliary.sp.SPAuxiliaryPayUtils;
import com.zhushou.weichat.auxiliary.ui.HFActivity;
import com.zhushou.weichat.base.PayActivity;
import com.zhushou.weichat.bean.NewPayDialogInfo;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.constant.PayConstant;
import com.zhushou.weichat.ui.pay.MyDialog;
import com.zhushou.weichat.ui.pay.PayHelper;
import com.zhushou.weichat.uinew.dialog.PayFailureDialog;
import com.zhushou.weichat.uinew.info.ComsuptionInfo;
import com.zhushou.weichat.utils.DebugLog;
import com.zhushou.weichat.utils.database.DBLibs;
import com.zhushou.weichat.utils.database.OperationDB;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

/**
 * Created by Administrator on 2017/11/30.
 */

public class VIPActivity extends PayActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener,PayHelper.RequreClick{

    private MyDialog myDialog; //支付弹窗
    private UpdatePriceInfo updatePriceInfo;//应用基础信息
    OperationDB operationDB;//支付信息数据库  用于对支付订单操作
    PayFailureDialog payFailureDialog; //订单失败处理弹窗

    @Override
    protected void init(Bundle savedInstanceState){
        super.init(savedInstanceState);
        setContentView(R.layout.activity_vip);
    }

    private Switch switch_svip,switch_svip_frend,switch_svip_zdhf,switch_svip_ffh;
    private TextView tv_ffh_status;
    @Override
    public void initView() {
        switch_svip = (Switch) findViewById(R.id.switch_svip);
        switch_svip_frend = (Switch) findViewById(R.id.switch_svip_frend);
        switch_svip_zdhf = (Switch) findViewById(R.id.switch_svip_zdhf);
        switch_svip_ffh = (Switch) findViewById(R.id.switch_svip_ffh);
        tv_ffh_status = (TextView) findViewById(R.id.tv_ffh_status);
        switch_svip.setOnCheckedChangeListener(this);
        switch_svip_frend.setOnCheckedChangeListener(this);
        switch_svip_zdhf.setOnCheckedChangeListener(this);
        switch_svip_ffh.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData(){
        if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mActivity, AuxiliaryConstans.C_QUJHY)||SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mActivity, AuxiliaryConstans.C_HDJHY)){
            switch_svip_frend.setChecked(true);
            switch_svip_frend.setClickable(false);
        }

        if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mActivity, AuxiliaryConstans.C_XPHF)||SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mActivity, AuxiliaryConstans.C_LYHF)){
            switch_svip_zdhf.setChecked(true);
            switch_svip_frend.setClickable(false);
        }

        if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mActivity, AuxiliaryConstans.C_FFH)){
            switch_svip_ffh.setChecked(true);
            switch_svip_frend.setClickable(false);
        }

        if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mActivity, AuxiliaryConstans.C_SVIP)){
            switch_svip.setChecked(true);
            switch_svip_frend.setClickable(false);
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rl_svip_jhy:
                if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mActivity, AuxiliaryConstans.C_QUJHY)||SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mActivity, AuxiliaryConstans.C_HDJHY)){
                    startActivity(new Intent(mActivity, AddFriendsFunctionA.class));
                }
                break;
            case R.id.rl_svip_hf:
                if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mActivity, AuxiliaryConstans.C_XPHF)||SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mActivity, AuxiliaryConstans.C_LYHF)){
                    startActivity(new Intent(mActivity, HFActivity.class));
                }
                break;
            case R.id.rl_svip_ffh:
                if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mActivity, AuxiliaryConstans.C_FFH)){

                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.switch_svip:
                handlingPayDialog(switch_svip,isChecked,PayConstant.Pay_S_SVIP,AuxiliaryConstans.C_SVIP);
                break;
            case R.id.switch_svip_frend:
                handlingPayDialog(switch_svip_frend,isChecked,PayConstant.Pay_S_SVIP_JHY,AuxiliaryConstans.C_QUJHY);
                break;
            case R.id.switch_svip_zdhf:
                handlingPayDialog(switch_svip_zdhf,isChecked,PayConstant.Pay_S_SVIP_HF,AuxiliaryConstans.C_XPHF);
                break;
            case R.id.switch_svip_ffh:
                handlingPayDialog(switch_svip_ffh,isChecked,PayConstant.Pay_S_SVIP_FFH,AuxiliaryConstans.C_FFH);
                break;
        }
    }

    private void handlingPayDialog(Switch svipSwitch,boolean isChecked,String functionKey,String spKey){
        if (isChecked){
            if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mActivity, spKey)){
                if (functionKey.equals(PayConstant.Pay_S_SVIP_FFH)){
                    tv_ffh_status.setText("已开启");
                }
                svipSwitch.setClickable(false);
            }else{
                svipSwitch.setChecked(false);
                if (myDialog==null)
                    myDialog = new MyDialog(mActivity,this,getHelper());
                if (!functionKey.equals(PayConstant.Pay_S_SVIP)){
                    if (updatePriceInfo==null)
                        updatePriceInfo = APPUsersShareUtil.getUpdateMessage(mActivity);
                    NewPayDialogInfo info3 = new NewPayDialogInfo();
                    info3.setFunctionName(getVipFunctionName(functionKey));
                    info3.setPrice(getVipFunctionprice(functionKey));
                    info3.setFunctionKey(functionKey);
                    try {
                        myDialog.showDialog(myDialog.getSVIPFunctionDialogData(info3));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        myDialog.showDialog(myDialog.getSVIPFunctionDialogData(null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mActivity, spKey)){
                svipSwitch.setChecked(true);
            }
        }
    }

    public String getVipFunctionName(String functionKey){
        String name = "";
        if (functionKey.equals(PayConstant.Pay_S_SVIP)){
            name = "SVIP所有功能";
        }else if (functionKey.equals(PayConstant.Pay_S_SVIP_JHY)){
            name = "微信批量加好友";
        }else if (functionKey.equals(PayConstant.Pay_S_SVIP_HF)){
            name = "微信自动回复";
        }else if (functionKey.equals(PayConstant.Pay_S_SVIP_FFH)){
            name = "分身仿封号";
        }
        return name;
    }
    public String getVipFunctionprice(String functionKey){
        String name = "99";

        if (functionKey.equals(PayConstant.Pay_S_SVIP)){
            if (updatePriceInfo!=null)
            name = updatePriceInfo.getWeichat_svip_price();
        }else if (functionKey.equals(PayConstant.Pay_S_SVIP_JHY)){
            if (updatePriceInfo!=null)
            name = updatePriceInfo.getWeichat_jf_all_price();
        }else if (functionKey.equals(PayConstant.Pay_S_SVIP_HF)){
            if (updatePriceInfo!=null)
            name = updatePriceInfo.getWeichat_hf_all_price();
        }else if (functionKey.equals(PayConstant.Pay_S_SVIP_FFH)){
            if (updatePriceInfo!=null)
            name = updatePriceInfo.getWeichat_ffh_price();
        }
        return name;
    }

    @Override
    public void successfulclick(String apkName, int type, int waretype) {
        DebugLog.e("支付成功");
        if (operationDB==null)
            operationDB = new OperationDB(mActivity);
        //修改订单数据库 设置该笔订单的状态为支付完成 ,顺带获取该笔成功订单数据 做统计和开启功能操作
        ComsuptionInfo comsuptionInfo = operationDB.changePayComplateStatus(getHelper().getorderNumber(), DBLibs.Pay_status_Successful);
        LBStat.pay(comsuptionInfo.getPayType(),
                comsuptionInfo.getOrderNumber(),
                true,
                PayConstant.getPayDescribeStr(comsuptionInfo.getCommodityFunctionType()),
                Float.valueOf(comsuptionInfo.getPrice()),
                comsuptionInfo.getPayType());
        //开启对应功能
        handlingSeccessOrder(comsuptionInfo.getCommodityFunctionType(),false,comsuptionInfo);

    }

    @Override
    public void failClick() {
        DebugLog.e("支付失败");
        if (operationDB==null)
            operationDB = new OperationDB(mActivity);
        handlingFailedOrders(operationDB.changePayComplateStatus(getHelper().getorderNumber(), DBLibs.Pay_status_Failure));
    }

    @Override
    public void exceprtion() {
        DebugLog.e("支付失败");
        if (operationDB==null)
            operationDB = new OperationDB(mActivity);
        handlingFailedOrders(operationDB.changePayComplateStatus(getHelper().getorderNumber(), DBLibs.Pay_status_Failure));
    }
    /**
     * 处理失败订单
     * @param comsuptionInfo
     */
    public void handlingFailedOrders(ComsuptionInfo comsuptionInfo){
        if (comsuptionInfo != null) {
            if (payFailureDialog==null)
                payFailureDialog = new PayFailureDialog(mActivity);
            //填充订单数据
            payFailureDialog.setComsuptionInfo(comsuptionInfo);
            //开始处理失败订单
            payFailureDialog.failureOrderDialog();
            //设置处理回调监听
            payFailureDialog.setCallBack(new PayFailureDialog.PayFailureDialogCallback() {
                @Override
                public void seccess(ComsuptionInfo comsuptionInfo) {
                    handlingSeccessOrder(comsuptionInfo.getCommodityFunctionType(),false,comsuptionInfo);
                }
            });
        }
    }
    @Override
    public void anginQuery() {

    }


    /**
     * 处理支付成功订单
     * @param functionType
     * @param isBd 是否属于补单操作
     */
    public void handlingSeccessOrder(String functionType,boolean isBd,ComsuptionInfo comsuptionInfo){

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(isBd?"补单成功":"支付成功");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
        });
        builder.setNegativeButton("问题咨询", (dialogInterface, i) -> {
            // TODO: 2017/11/30 跳转客服投诉界面

        });

        if (functionType.equals(PayConstant.Pay_S_SVIP)){
            SPAuxiliaryPayUtils.setSVIPPermission(mActivity);
            builder.setMessage("支付成功！SVIP权限已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            switch_svip.setChecked(true);
            switch_svip_frend.setChecked(true);
            switch_svip_zdhf.setChecked(true);
            switch_svip_ffh.setChecked(true);
            if (comsuptionInfo!=null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }
        if (functionType.equals(PayConstant.Pay_S_SVIP_JHY)){
            SPAuxiliaryPayUtils.setWsAddFriendAll(mActivity);
            builder.setMessage("支付成功！微信加好友功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            switch_svip_frend.setChecked(true);
            if (comsuptionInfo!=null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }
        if (functionType.equals(PayConstant.Pay_S_SVIP_HF)){
            SPAuxiliaryPayUtils.setWsFunctionHFAll(mActivity);
            builder.setMessage("支付成功！微信自动回复功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            switch_svip_zdhf.setChecked(true);
            if (comsuptionInfo!=null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }
        if (functionType.equals(PayConstant.Pay_S_SVIP_FFH)){
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(mActivity, AuxiliaryConstans.C_FFH);
            builder.setMessage("支付成功！微信防封号功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            switch_svip_ffh.setChecked(true);
            if (comsuptionInfo!=null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }



    }

}
