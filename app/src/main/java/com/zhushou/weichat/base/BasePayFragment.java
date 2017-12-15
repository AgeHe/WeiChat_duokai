package com.zhushou.weichat.base;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhushou.weichat.bean.UserInfo;
import com.zhushou.weichat.brodcastr.UserInfoBroadcast;
import com.zhushou.weichat.constant.BrodcastConstant;
import com.zhushou.weichat.interfac.BaseFragmentInterface;
import com.zhushou.weichat.ui.pay.PayHelper;
import com.zhushou.weichat.utils.DebugLog;
import com.zhushou.weichat.utils.brodcastr.UIHelper;

/**
 * Created by Administrator on 2017/11/30.
 */

public abstract class BasePayFragment extends Fragment implements BaseFragmentInterface {

    private PayHelper helper;
    private boolean isActive;
    public ProgressDialog payProgressDialog;
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    if (helper.isCheck() == true && isActive == true) {
                        if (payProgressDialog.isShowing())
                            payProgressDialog.dismiss();
                        helper.requreHttpAsync(helper.getQureyURL(),mActivity);
                    }
                    break;
            }
        }
    };


    private String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    public Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = init(inflater,container,savedInstanceState);
        this.mActivity = getActivity();
        IntentFilter filter = new IntentFilter(BrodcastConstant.INTENT_BROADCAST_PACKAGE);
        filter.addAction(BrodcastConstant.BROADCAST_ACTION_LOGIN);
        filter.addAction(BrodcastConstant.BROADCAST_ACTION_UPDATE);
        filter.addAction(BrodcastConstant.BROADCAST_ACTION_VPNCHANGE);
        filter.addAction(BrodcastConstant.BROADCAST_ACTION_PAYSTATU);
        mActivity.registerReceiver(userInfoBroadcast, filter);
        this.helper = new PayHelper(mActivity);
        setPermission();
        DebugLog.e("初始化PayHelper");
        initView(mView);
        initData();
        return mView;
    }


    protected abstract View init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected void loginStatusBC(boolean isLogin){}

    protected void vpnServiceChanged(boolean ischeck){}

    protected void payStatuMsg(UserInfo userInfo){}
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
            }else if(action.equals(BrodcastConstant.BROADCAST_ACTION_VPNCHANGE)){
                vpnServiceChanged(intent.getBooleanExtra(BrodcastConstant.BROADCAST_VALUE_KEY,false));
            }else if (action.equals(BrodcastConstant.BROADCAST_ACTION_PAYSTATU)){
                payStatuMsg((UserInfo) intent.getSerializableExtra(BrodcastConstant.BROADCAST_VALUE_KEY));
            }
        }
    };

    /**
     * 发送用户登录状态
     * @param state
     */
    public void sendLoginStateBroadcast(boolean state){
        UIHelper.sendBrodCastLoginStatus(getActivity(),state);
    }

    /**
     * 发送VPN状态
     * @param state
     */
    public void sendVpnStateBroadcast(boolean state){
        UIHelper.sendBrodCastVpnStatus(mActivity,state);
    }


    @Override
    public void onDestroy() {

        try{
            mActivity.unregisterReceiver(userInfoBroadcast);
        }catch (Exception e){
            e.printStackTrace();
        }

        super.onDestroy();
    }



    @Override
    public void onResume(){
        super.onResume();
        isActive = true;

        if (payProgressDialog==null){
            payProgressDialog = new ProgressDialog(mActivity);
            payProgressDialog.setMessage("查询订单...");
            payProgressDialog.setCanceledOnTouchOutside(false);
            payProgressDialog.setCancelable(true);
        }

        if (helper!=null&&helper.isCheck()){
            if (!payProgressDialog.isShowing())
                payProgressDialog.show();
            mHandle.sendEmptyMessageDelayed(0, 1000);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        isActive = false;
    }

    public PayHelper getHelper(){
        if (helper==null)
            helper = new PayHelper(mActivity);
        return helper;
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


}
