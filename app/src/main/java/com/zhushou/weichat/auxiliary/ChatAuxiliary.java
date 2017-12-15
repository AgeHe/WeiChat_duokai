package com.zhushou.weichat.auxiliary;

import android.accessibilityservice.AccessibilityService;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.auxiliary.info.HbRecordInfo;
import com.zhushou.weichat.auxiliary.interfac.WeiChatMessage;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryConstans;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryFunctionSwitch;
import com.zhushou.weichat.auxiliary.sp.HbSaveDataUtils;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;
import com.zhushou.weichat.utils.HistoryPayShareUtil;
import com.zhushou.weichat.utils.TLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */
public class ChatAuxiliary implements WeiChatMessage {
    private static final String TAG = "ChatAuxiliary";

    BaseAccessibilityService baseAccessibility;
    Context mContext;
    AuxiliaryFunctionSwitch auxiliaryFunctionSwitch;
    UpdatePriceInfo updatePriceInfo;

    HbSaveDataUtils hbSavedataUtils;

    private PowerManager.WakeLock wl;
    private KeyguardManager.KeyguardLock kl;

    public ChatAuxiliary(BaseAccessibilityService baseAccessibilityService, Context context){
        this.baseAccessibility = baseAccessibilityService;
        this.mContext = context;
        auxiliaryFunctionSwitch = new AuxiliaryFunctionSwitch(mContext);
        updatePriceInfo = APPUsersShareUtil.getUpdateMessage(context);
        hbSavedataUtils = new HbSaveDataUtils(mContext);
    }

    public static void ChangeAccessibility(AccessibilityNodeInfo info){
        if (info != null) {
            TLog.log(TAG, "classname:" + info.getClassName());
            if (info.getChildCount() == 0) {
                TLog.log(TAG, "classname:" + info.getClassName());
                TLog.log(TAG, "content:" + info.getText());
            } else {
                for (int i = 0; i < info.getChildCount(); i++) {
                    if (info.getChild(i) != null) {
                        ChangeAccessibility(info.getChild(i));
                    }
                }
            }
        }
    }

    @Override
    public void HbMessageProcessing(AccessibilityNodeInfo info) {
        List<AccessibilityNodeInfo> listInfo = new ArrayList<>();
        try {
            listInfo = info.findAccessibilityNodeInfosByViewId(updatePriceInfo.getId_hb());
//            if (listInfo.size()<=0)
//                listInfo = info.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a5c");
        }catch (Exception e){
            TLog.log(e.getMessage());
        }
        if (listInfo.size()<=0){
            listInfo = getNodeInfo(updatePriceInfo.getId_hb());
            if (listInfo.size()<=0)
                return;
        }
        AccessibilityNodeInfo thisInfo = listInfo.get(listInfo.size()-1);
        baseAccessibility.getHBInterfaceMessage();
        thisInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }




    @Override
    public void CHBMessageProcessing(AccessibilityNodeInfo info) {
        List<AccessibilityNodeInfo> listInfo = new ArrayList<>();
        try {
            listInfo = info.findAccessibilityNodeInfosByViewId(updatePriceInfo.getId_chb());
        }catch (Exception e){
            TLog.log(e.getMessage());
        }
        if (listInfo.size()<=0){
            listInfo = getNodeInfo(updatePriceInfo.getId_chb());
            if (listInfo.size()<=0){
                Log.i(TAG, "CHBMessageProcessing: listInfo.size <= 0 ");
                baseAccessibility.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                return;
            }
        }
        baseAccessibility.setIsBackluckyMoney(true);
        AccessibilityNodeInfo thisInfo = listInfo.get(listInfo.size()-1);
        thisInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);


//        auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_DX);
//        auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_BACK);
//        if (auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_DX)){
//
//        }else if (auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_BACK)){
//
//        }
//        goHome();
    }


    /**
     * 红包详情界面 返回聊天界面
     * @param info
     */
    @Override
    public void luckyMoneyDetailBack(AccessibilityNodeInfo info) {




//        List<AccessibilityNodeInfo> listInfo = new ArrayList<>();
//        try {
//            listInfo = info.findAccessibilityNodeInfosByViewId(updatePriceInfo.getId_hb_detail_back());
//        }catch (Exception e){
//            TLog.log(e.getMessage());
//        }
//        if (listInfo.size()<=0){
//            listInfo = getNodeInfo(updatePriceInfo.getId_hb_detail_back());
//            if (listInfo.size()<=0){
//                return;
//            }
//        }
//        AccessibilityNodeInfo thisInfo = listInfo.get(listInfo.size()-1);
//        thisInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK); //点击返回聊天界面
        baseAccessibility.setIsDxluckyMoney(true);
        baseAccessibility.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);

        if (!auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_DX)){
            goHome();
        }
    }

    private String  inputContent = "";
    @Override
    public void dxluckyMoneyProcessing(AccessibilityNodeInfo info) {
        List<AccessibilityNodeInfo> listInfo = new ArrayList<>();

        try {
            listInfo = info.findAccessibilityNodeInfosByViewId(updatePriceInfo.getId_hf_input());
        }catch (Exception e){
            TLog.log(e.getMessage());
        }
        if (listInfo.size()<=0){
            listInfo = getNodeInfo(updatePriceInfo.getId_hf_input());
            if (listInfo.size()<=0)
                return;
        }
        AccessibilityNodeInfo thisInfo = listInfo.get(listInfo.size()-1);
        //给输入框赋值
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE
                ,inputContent);
        thisInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        listInfo.clear();

        AccessibilityNodeInfo sendInfo = baseAccessibility.getNodeInfo();
        try {
            listInfo = sendInfo.findAccessibilityNodeInfosByViewId(updatePriceInfo.getId_hf_sendbtn());
        }catch (Exception e){
            TLog.log(e.getMessage());
        }
        if (listInfo.size()<=0){
            listInfo = getNodeInfo(updatePriceInfo.getId_hf_sendbtn());
            if (listInfo.size()<=0)
                return;
        }
        AccessibilityNodeInfo sendThisInfo = listInfo.get(listInfo.size()-1);
        sendThisInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        goHome();
    }

    @Override
    public void getHbRecordProcessing() {
        String grabMoney =  getGrabMoney(baseAccessibility.getNodeInfo());
        String bossname = getBossName(baseAccessibility.getNodeInfo());
        inputContent = String.format(mContext.getResources().getString(R.string.wx_hb_ly_input),
                grabMoney,
                bossname);
        hbSavedataUtils.saveRedPacketsNumber();
        hbSavedataUtils.saveRedPacketsMoney(Float.valueOf(grabMoney));

        HbRecordInfo recordInfo = new HbRecordInfo();
        recordInfo.setMoney(grabMoney);
        recordInfo.setBoss(bossname);
        recordInfo.setHbTime(HistoryPayShareUtil.getSystemTime(VApp.DATE_NOSECOND));
        hbSavedataUtils.saveHbRecord(recordInfo);
    }

    /**
     * 聊天界面对红包消息进行操作
     * @param info
     */
    @Override
    public void chatViewRedMessageProcessing(AccessibilityNodeInfo info) {
        List<AccessibilityNodeInfo> listInfo = new ArrayList<>();
        try {
            listInfo = info.findAccessibilityNodeInfosByViewId(updatePriceInfo.getId_hb());
//            if (listInfo.size()<=0)
//                listInfo = info.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a5c");
        }catch (Exception e){
            TLog.log(e.getMessage());
        }
        if (listInfo.size()<=0){
//            listInfo = getNodeInfo(updatePriceInfo.getId_chat_item_id());
//            if (listInfo.size()<=0)
                return;
        }
        AccessibilityNodeInfo thisInfo = listInfo.get(listInfo.size()-1);
        baseAccessibility.getHBInterfaceMessage();
        thisInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    @Override
    public void dxluckymoneyInputProcessing(AccessibilityNodeInfo info) {
        List<AccessibilityNodeInfo> listInfo = new ArrayList<>();
        String inputContent = String.format(mContext.getResources().getString(R.string.wx_hb_ly_input),getGrabMoney(baseAccessibility.getNodeInfo()),
                getBossName(baseAccessibility.getNodeInfo()));
        try {
            listInfo = info.findAccessibilityNodeInfosByViewId(updatePriceInfo.getId_hb_input_ly());
        }catch (Exception e){
            TLog.log(e.getMessage());
        }
        if (listInfo.size()<=0){
            listInfo = getNodeInfo(updatePriceInfo.getId_hf_input());
            if (listInfo.size()<=0)
                return;
        }
        AccessibilityNodeInfo thisInfo = listInfo.get(listInfo.size()-1);
        //给红包答谢输入框赋值
        Bundle arguments = new Bundle();
        Log.i(TAG, "dxluckymoneyInputProcessing: xxxx inputContent="+inputContent);
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,inputContent);
        thisInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        listInfo.clear();

        //点击发送
        AccessibilityNodeInfo sendInfo = baseAccessibility.getNodeInfo();
        try {
            listInfo = sendInfo.findAccessibilityNodeInfosByViewId(updatePriceInfo.getId_hb_send_ly());
        }catch (Exception e){
            TLog.log(e.getMessage());
        }
        if (listInfo.size()<=0){
            listInfo = getNodeInfo(updatePriceInfo.getId_hf_sendbtn());
            if (listInfo.size()<=0)
                return;
        }
        AccessibilityNodeInfo sendThisInfo = listInfo.get(listInfo.size()-1);
        sendThisInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        goHome();
    }


    /**
     * 获取界面中抢到的金额
     * @return
     */
    private String getGrabMoney(AccessibilityNodeInfo info){
        String grabmoney = "0.00";
        List<AccessibilityNodeInfo> listInfo = new ArrayList<>();
        try {
            listInfo = info.findAccessibilityNodeInfosByViewId(updatePriceInfo.getId_hb_grab_money());
        }catch (Exception e){
            TLog.log(e.getMessage());
        }
        if (listInfo.size()<=0){
            listInfo = getNodeInfo(updatePriceInfo.getId_hb_grab_money());
            if (listInfo.size()<=0){
                return grabmoney;
            }
        }
        AccessibilityNodeInfo thisInfo = listInfo.get(listInfo.size()-1);
        grabmoney = thisInfo.getText().toString();
        return grabmoney;
    }

    /**
     * 获取发红包用户名称
     * @param info
     * @return
     */
    private String getBossName(AccessibilityNodeInfo info){
        String bossName = "";
        List<AccessibilityNodeInfo> listInfo = new ArrayList<>();
        try {
            listInfo = info.findAccessibilityNodeInfosByViewId(updatePriceInfo.getId_hb_boss_name());
        }catch (Exception e){
            TLog.log(e.getMessage());
        }
        if (listInfo.size()<=0){
            listInfo = getNodeInfo(updatePriceInfo.getId_hb_boss_name());
            if (listInfo.size()<=0){
                return bossName;
            }
        }
        AccessibilityNodeInfo thisInfo = listInfo.get(listInfo.size()-1);
        bossName = thisInfo.getText().toString();
        return bossName;
    }

    @Override
    public void commonMessageProcessing(AccessibilityNodeInfo info) {//com.tencent.mm:id/a2v
        List<AccessibilityNodeInfo> listInfo = new ArrayList<>();
        try {
            listInfo = info.findAccessibilityNodeInfosByViewId(updatePriceInfo.getId_hf_input());
        }catch (Exception e){
            TLog.log(e.getMessage());
        }
        if (listInfo.size()<=0){
            listInfo = getNodeInfo(updatePriceInfo.getId_hf_input());
            if (listInfo.size()<=0)
                return;
        }
        AccessibilityNodeInfo thisInfo = listInfo.get(listInfo.size()-1);
        //给输入框赋值
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE
                ,auxiliaryFunctionSwitch.getLeavingMessage());
        thisInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        listInfo.clear();


        AccessibilityNodeInfo sendInfo = baseAccessibility.getNodeInfo();
        try {
            listInfo = sendInfo.findAccessibilityNodeInfosByViewId(updatePriceInfo.getId_hf_sendbtn());
        }catch (Exception e){
            TLog.log(e.getMessage());
        }
        if (listInfo.size()<=0){
            listInfo = getNodeInfo(updatePriceInfo.getId_hf_sendbtn());
            if (listInfo.size()<=0)
                return;
        }
        AccessibilityNodeInfo sendThisInfo = listInfo.get(listInfo.size()-1);
        sendThisInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);

        goHome();
    }




    private boolean screenStatu = true;  // false 锁屏状态下操作
    @Override
    public void screenStatus(boolean status) {
        this.screenStatu = status;
    }


    /**
     * 虚拟点击home键
     */
    public void goHome(){
        Intent mintent = new Intent(Intent.ACTION_MAIN);
        mintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mintent.addCategory(Intent.CATEGORY_HOME);
        VApp.context().startActivity(mintent);
    }


    /**
     * 获取微信界面ViewId  10次循环每次延时200ms
     * @param viewId
     * @return
     */
    private List<AccessibilityNodeInfo> getNodeInfo(String viewId){
        List<AccessibilityNodeInfo> listInfo = new ArrayList<>();
        for (int i=0;i<10;i++){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listInfo = baseAccessibility.getNodeInfo().findAccessibilityNodeInfosByViewId(viewId);
//            if (listInfo.size()<=0){
//                listInfo =  baseAccessibility.getNodeInfo().findAccessibilityNodeInfosByViewId(viewId2);
//            }
            if (listInfo.size()>0){
                break;
            }
        }
        return listInfo;
    }


}
