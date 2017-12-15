package com.zhushou.weichat.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhushou.weichat.bean.UserInfo;
import com.zhushou.weichat.brodcastr.UserInfoBroadcast;
import com.zhushou.weichat.constant.BrodcastConstant;
import com.zhushou.weichat.interfac.BaseFragmentInterface;
import com.zhushou.weichat.utils.brodcastr.UIHelper;


/**
 * Created by Administrator on 2017/9/12.
 */

public abstract class BaseFragment extends Fragment implements BaseFragmentInterface {

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
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
}
