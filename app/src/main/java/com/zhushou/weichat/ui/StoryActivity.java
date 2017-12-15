package com.zhushou.weichat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.zhushou.weichat.R;
import com.zhushou.weichat.abs.TuiB;
import com.zhushou.weichat.auxiliary.base.BaseActivity;
import com.zhushou.weichat.uinew.widget.BrowserLayout;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

/**
 * Created by Administrator on 2017/11/27.
 */

public class StoryActivity extends BaseActivity {

    private String browserurl = "";
    private boolean isb = true;
    private static Intent c;
    private BrowserLayout bl_browser_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.fragment_story);
        super.onCreate(savedInstanceState);
        initView();
    }


    public void initView() {
        this.browserurl = APPUsersShareUtil.getUpdateMessage(mContext).getLeft_button_url();
        bl_browser_layout = (BrowserLayout) findViewById(R.id.bl_browser_layout);
        if (!TextUtils.isEmpty(this.browserurl)) {
            this.bl_browser_layout.a(this.browserurl);
        }
        if (!this.isb) {
            this.bl_browser_layout.c();
        } else {
            this.bl_browser_layout.d();
        }
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        bl_browser_layout.setmTextView("小说");
    }

    private void a() {
        if (this.bl_browser_layout.a()) {
            this.bl_browser_layout.b();
        } else {
            this.bl_browser_layout.a("about:blank");
//            this.finish();
            Class var1 = TuiB.a(this).a();
            if (var1 != null) {
                if (c == null) {
                    Intent var2 = new Intent(this, var1);
                    this.startActivity(var2);
                    TuiB.a(this).TuiB();
                } else {
                    this.startActivity(c);
                    TuiB.a(this).TuiB();
                }
            }
        }
    }

    public boolean onKeyDown(int var1, KeyEvent var2) {
        switch (var2.getKeyCode()) {
            case 4:
                this.a();
                return true;
            default:
                return this.onKeyDown(var1, var2);
        }
    }

}
