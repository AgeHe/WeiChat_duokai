package com.zhushou.weichat.auxiliary.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.zhushou.weichat.auxiliary.interfac.BaseViewInterface;
import com.zhushou.weichat.utils.DisplayUtil;

/**
 * Created by Administrator on 2017/4/11.
 */

public abstract class BaseActivity extends Activity implements View.OnClickListener,BaseViewInterface {
    public Context mContext;
    public Resources mResources;
    public int screenHeight = 0;
    public long startActivityTime = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        this.mResources = this.getResources();
        screenHeight = DisplayUtil.getScreenHeight(mContext);
        init(savedInstanceState);
        startActivityTime = System.currentTimeMillis();
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);//友盟统计
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);//友盟统计
    }
}
