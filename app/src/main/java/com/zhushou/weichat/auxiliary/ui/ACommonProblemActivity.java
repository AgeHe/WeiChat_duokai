package com.zhushou.weichat.auxiliary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.zhushou.weichat.R;
import com.zhushou.weichat.auxiliary.base.BaseActivity;

/**
 * Created by Administrator on 2017/3/21.
 */

public class ACommonProblemActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setContentView(R.layout.activity_auxiliary_commonproblem);
    }

    @Override
    public void initView() {
        findViewById(R.id.tv_intent_open_permissions).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void viewOnClick(View view) {
        super.viewOnClick(view);
        switch (view.getId()){
            case R.id.tv_intent_open_permissions:
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
