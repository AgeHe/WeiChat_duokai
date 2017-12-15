package com.zhushou.weichat.ui;

import android.app.Activity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2017/4/18.
 */

public abstract class BaseMainActivity extends Activity{


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
