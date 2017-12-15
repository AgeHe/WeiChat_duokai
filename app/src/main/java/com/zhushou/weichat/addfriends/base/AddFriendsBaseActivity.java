package com.zhushou.weichat.addfriends.base;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zhushou.weichat.auxiliary.interfac.BaseViewInterface;
import com.zhushou.weichat.ui.pay.PayActivity;

/**
 * Created by Administrator on 2017/3/28.
 */

public abstract class AddFriendsBaseActivity extends PayActivity implements BaseViewInterface,View.OnClickListener {

    public Context mContext;
    public Resources mResources;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mResources = this.getResources();
        init(savedInstanceState);
        initView();
        initData();
    }
    protected void viewOnClick(View view){}
    protected void init(Bundle savedInstanceState) {}
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    public void onClick(View view) {
        viewOnClick(view);


    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return super.onKeyDown(keyCode, event);
//    }
}
