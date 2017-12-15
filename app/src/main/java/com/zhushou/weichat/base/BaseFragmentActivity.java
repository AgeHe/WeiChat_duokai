package com.zhushou.weichat.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.zhushou.weichat.brodcastr.UserInfoBroadcast;
import com.zhushou.weichat.constant.BrodcastConstant;
import com.zhushou.weichat.interfac.BaseViewInterface;
import com.zhushou.weichat.utils.brodcastr.UIHelper;


/**
 * Created by Administrator on 2017/9/11.
 */

public abstract class BaseFragmentActivity extends FragmentActivity implements BaseViewInterface {
    public Activity mActivity;
    public Resources mResources;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        init(savedInstanceState);
        IntentFilter filter = new IntentFilter(BrodcastConstant.INTENT_BROADCAST_PACKAGE);
        filter.addAction(BrodcastConstant.BROADCAST_ACTION_LOGIN);
        filter.addAction(BrodcastConstant.BROADCAST_ACTION_UPDATE);
        filter.addAction(BrodcastConstant.BROADCAST_ACTION_VPNCHANGE);
        filter.addAction(BrodcastConstant.BROADCAST_ACTION_PAYSTATU);
        mActivity.registerReceiver(userInfoBroadcast, filter);
        initView();
        initData();
    }

    protected void init(Bundle savedInstanceState){};

    protected void loginStatusBC(boolean isLogin){}

    protected void vpnServiceChanged(boolean ischeck){}
    /**
     * 接收广播
     */
    private UserInfoBroadcast userInfoBroadcast = new UserInfoBroadcast(){
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            String action = intent.getAction();
            if (action.equals(BrodcastConstant.BROADCAST_ACTION_LOGIN)){
                loginStatusBC(intent.getBooleanExtra(BrodcastConstant.BROADCAST_VALUE_KEY,false));
            }else if(action.equals(BrodcastConstant.BROADCAST_ACTION_LOGIN)){
                vpnServiceChanged(intent.getBooleanExtra(BrodcastConstant.BROADCAST_VALUE_KEY,false));
            }
        }
    };

    /**
     * 发送用户登录状态
     * @param state
     */
    public void sendLoginStateBroadcast(boolean state){
        UIHelper.sendBrodCastLoginStatus(mActivity,state);
    }
    /**
     * 发送VPN状态
     * @param state
     */
    public void sendVpnStateBroadcast(boolean state){
        UIHelper.sendBrodCastVpnStatus(mActivity,state);
    }


    public final int WEITE_STORAGE = 20;
    public void setPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            int weiteStoragePermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readStoragePermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int readPhotoStatePermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_PHONE_STATE);
            if(weiteStoragePermission != PackageManager.PERMISSION_GRANTED||readStoragePermission != PackageManager.PERMISSION_GRANTED||readPhotoStatePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE}, WEITE_STORAGE);
                return;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case WEITE_STORAGE:  // 申请储存权限成功  用于二维码生成
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onDestroy() {
        try{
            mActivity.unregisterReceiver(userInfoBroadcast);
        }catch (Exception e){
        }
        super.onDestroy();
    }


}
