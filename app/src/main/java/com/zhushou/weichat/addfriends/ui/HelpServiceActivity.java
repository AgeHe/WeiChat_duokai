package com.zhushou.weichat.addfriends.ui;

import android.os.Bundle;
import android.view.View;

import com.zhushou.weichat.R;
import com.zhushou.weichat.addfriends.base.AFBaseActivity;

/**
 * Created by Administrator on 2017/3/31.
 */

public class HelpServiceActivity extends AFBaseActivity {

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setContentView(R.layout.activity_add_friends_help);

    }

    @Override
    public void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }
}
