package com.zhushou.weichat.uinew.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhushou.weichat.R;
import com.zhushou.weichat.abs.TuiB;
import com.zhushou.weichat.base.BaseFragment;
import com.zhushou.weichat.uinew.widget.BrowserLayout;

/**
 * Created by Administrator on 2017/11/27.
 */

public class StoryFragment extends BaseFragment {

    private String browserurl = "https://s.iyd.cn/mobile/book/index/heimama/091300029";
    private boolean isb = true;
    private static Intent c;

    @Override
    protected View init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story,container,false);
    }

    private BrowserLayout bl_browser_layout;

    @Override
    public void initView(View view) {
        bl_browser_layout = (BrowserLayout) view.findViewById(R.id.bl_browser_layout);
        if (!TextUtils.isEmpty(this.browserurl)) {
            this.bl_browser_layout.a(this.browserurl);
        }
        if (!this.isb) {
            this.bl_browser_layout.c();
        } else {
            this.bl_browser_layout.d();
        }

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
            Class var1 = TuiB.a(mActivity).a();
            if (var1 != null) {
                if (c == null) {
                    Intent var2 = new Intent(mActivity, var1);
                    this.startActivity(var2);
                    TuiB.a(mActivity).TuiB();
                } else {
                    this.startActivity(c);
                    TuiB.a(mActivity).TuiB();
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
                return super.mActivity.onKeyDown(var1, var2);
        }
    }

}
