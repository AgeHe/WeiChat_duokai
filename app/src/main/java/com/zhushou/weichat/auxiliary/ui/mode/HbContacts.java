package com.zhushou.weichat.auxiliary.ui.mode;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;

import com.zhushou.weichat.R;
import com.zhushou.weichat.auxiliary.info.HbMainMessageInfo;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryConstans;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryFunctionSwitch;
import com.zhushou.weichat.auxiliary.sp.HbSaveDataUtils;
import com.zhushou.weichat.auxiliary.ui.dialog.AuxiliarySystemSettingDialog;
import com.zhushou.weichat.auxiliary.utils.CheckAuxiliarySwitch;

import java.util.Random;


/**
 * Created by Administrator on 2017/4/11.
 */

public class HbContacts implements HbInterfaceGroup.ImplContacts,AuxiliarySystemSettingDialog.AuxiliaryLisener{

    HbInterfaceGroup.ViewContacts viewContacts;

    Activity mActivity;

    Resources mResources;

    private String userId = "";

    private String[] hbFunctionArray;

    public AuxiliaryFunctionSwitch auxiliaryFunctionSwitch; // 功能开关状态存取
    public HbSaveDataUtils hbSaveDataUtils; // 红包数据存取

    public AuxiliarySystemSettingDialog auxiliarySystemSettingDialog;

    private boolean switchSetStatus = true;

    public HbContacts(Activity mActivity,HbInterfaceGroup.ViewContacts viewContacts){
        this.viewContacts = viewContacts;
        this.mActivity = mActivity;
        this.mResources = mActivity.getResources();
        auxiliarySystemSettingDialog = new AuxiliarySystemSettingDialog(this);
        hbFunctionArray = new String[]{mResources.getString(R.string.pay_wx_title_svip),
                mResources.getString(R.string.pay_ws_title_shouqi),
                mResources.getString(R.string.pay_ws_title_dabao),
                mResources.getString(R.string.pay_ws_title_jiasudu),
                mResources.getString(R.string.pay_ws_title_ganrao),
                mResources.getString(R.string.pay_ws_title_xpqhb),
                mResources.getString(R.string.pay_ws_title_zddx)};
    }

    @Override
    public void initViewStatus() {
        viewContacts.loadingStatus(true);
        auxiliaryFunctionSwitch = new AuxiliaryFunctionSwitch(mActivity);
        hbSaveDataUtils = new HbSaveDataUtils(mActivity);
    }

    @Override
    public void startViewData() {
        settingGrabRedPacketsStatus();
        viewContacts.loadingStatus(false);
    }

    @Override
    public void updateRedPacktsNumberAndMoney(){
        settingRedpacktsNumberAndMoney();
    }

    @Override
    public void updateOpeningFunctionData(String userid,String function) {
        String hbUserId = hbSaveDataUtils.getHbUserId();
        if (hbUserId!=null&&!hbUserId.equals("")){
            viewContacts.setUserNumber(hbUserId);
            userId = hbUserId;
        }else{
            Random random=new Random();
            int max = 102035465;
            int min = 102015385;
            int randomUserId=random.nextInt(max)%(max-min+1) + min;
            hbSaveDataUtils.saveHbUserId(String.valueOf(randomUserId));
            viewContacts.setUserNumber(String.valueOf(randomUserId));
            userId = String.valueOf(randomUserId);
        }
        viewContacts.updateOpeningFunctionData(radomUserData("",""));
    }

    @Override
    public String getUserId() {
        if (!userId.equals("")){
            return userId;
        }else{
            return hbSaveDataUtils.getHbUserId();
        }
    }

    @Override
    public void hbAnimationClick(boolean isHbSwich) {
        if (!CheckAuxiliarySwitch.isAccessibilitySettingsOn(mActivity)&&auxiliarySystemSettingDialog!=null){
            auxiliarySystemSettingDialog.readioDialog(mActivity);
            viewContacts.setGrabRedPacketsSwichStatus(false);
        }else{
            viewContacts.setGrabRedPacketsSwichStatus(!isHbSwich);
        }
    }

    public String radomUserData(String userid,String function){
        String scrollUsersData = "";
        Random random=new Random();
        int max = 102035465;
        int min = 102015385;
        for (int i=0;i<=20;i++){
            if (i==1&&!userid.equals("")){
                scrollUsersData += String.format(mResources.getString(R.string.hb_scroll_user_id),
                        "3",
                        userid,
                        function);
                continue;
            }
            int randomUserId=random.nextInt(max)%(max-min+1) + min;
            int randomS = random.nextInt(10)%(10-1+1) + 1;
            int randomfucntion = random.nextInt(6);
            scrollUsersData += String.format(mResources.getString(R.string.hb_scroll_user_id),
                    String.valueOf(randomS),
                    String.valueOf(randomUserId),
                    hbFunctionArray!=null?hbFunctionArray[randomfucntion]:mResources.getString(R.string.pay_wx_title_svip));
        }
        return scrollUsersData;
    }

    /**
     * 设置抢红包状态
     */
    public void settingGrabRedPacketsStatus(){
        if (CheckAuxiliarySwitch.isAccessibilitySettingsOn(mActivity)&&
                auxiliaryFunctionSwitch.checkFunctionSwitchStatus(AuxiliaryConstans.C_HB)){
            viewContacts.setGrabRedPacketsStatus(true);
        }else{
            viewContacts.setGrabRedPacketsStatus(false);
        }
    }

    /**
     * 更新 已抢红包数量和金额
     */
    public void settingRedpacktsNumberAndMoney(){
//        float[] floatsData = hbSaveDataUtils.getRedPacketsNumberAndMoney();
        HbMainMessageInfo info = hbSaveDataUtils.getRedPacketsNumberAndMoney();
        float[] floatsData = new float[]{Float.valueOf(info.getHbSum()),Float.valueOf(info.getHbSumMoney())};
        viewContacts.setRedPacketsNumberAndMoney(floatsData[0],floatsData[1]);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle mBundle = msg.getData();
            if (mBundle==null)
                return;
            switch (mBundle.getInt("what")){
                case 0:
                    switchSetStatus = mBundle.getBoolean("switchSetStatus");
                    break;
            }
        }
    };

    public boolean isSwitchSetStatus(){
        return switchSetStatus;
    }

    public void setSwitchSetStatus(boolean switchSetStatus){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                if (mHandler!=null){
                    Message message = new Message();
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("what",0);
                    mBundle.putBoolean("switchSetStatus",switchSetStatus);
                    message.setData(mBundle);
                    mHandler.sendMessage(message);
                }
            }
        }).start();

    }


    @Override
    public void onAuxiliaryClick() {
        mActivity.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }
}
