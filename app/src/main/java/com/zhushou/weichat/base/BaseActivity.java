package com.zhushou.weichat.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.zhushou.weichat.bean.UserInfo;
import com.zhushou.weichat.brodcastr.UserInfoBroadcast;
import com.zhushou.weichat.constant.BrodcastConstant;
import com.zhushou.weichat.interfac.BaseViewInterface;
import com.zhushou.weichat.utils.brodcastr.UIHelper;

/**
 * Created by Administrator on 2017/9/11.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseViewInterface {

    public Activity mActivity;
    public Resources mResources;
    public ProgressDialog loadingProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mResources = this.getResources();
        init(savedInstanceState);
        initView();
        initData();
    }

    protected void init(Bundle savedInstanceState){
        IntentFilter filter = new IntentFilter(BrodcastConstant.INTENT_BROADCAST_PACKAGE);
        filter.addAction(BrodcastConstant.BROADCAST_ACTION_LOGIN);
        filter.addAction(BrodcastConstant.BROADCAST_ACTION_UPDATE);
        filter.addAction(BrodcastConstant.BROADCAST_ACTION_VPNCHANGE);
        filter.addAction(BrodcastConstant.BROADCAST_ACTION_PAYSTATU);
        registerReceiver(userInfoBroadcast, filter);
    }
    protected void loginStatusBC(boolean isLogin){

    }


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
            }else{

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

    public void sendPayStatuBroadcast(UserInfo userInfo){
        UIHelper.sendBrodCastPaySuccess(mActivity,userInfo);
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(userInfoBroadcast);
        super.onDestroy();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mActivity!=null)
        MobclickAgent.onResume(mActivity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mActivity!=null)
            MobclickAgent.onPause(mActivity);
    }
}
