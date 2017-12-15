package com.zhushou.weichat.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.zhushou.weichat.R;
import com.zhushou.weichat.abs.MyTmActivity;
import com.zhushou.weichat.abs.TuiB;
import com.zhushou.weichat.auxiliary.base.BaseActivity;
import com.zhushou.weichat.uinew.widget.BrowserLayout;


/**
 * Created by Administrator on 2017/8/14.
 */

public class TuiABrowserActivity extends BaseActivity {

    private String browserurl = null;
    private String title  = "";
    private boolean isb = true;
    private static Intent c;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Intent var2 = this.getIntent();
        this.browserurl = var2.getExtras().getString("BUNDLE_KEY_URL", "");
        try {
            this.title = var2.getExtras().getString("title");
        }catch (Exception e){
            e.printStackTrace();
        }
        setContentView(R.layout.activity_tuia_browser);
    }

    private BrowserLayout adv_browser_layout;

    @Override
    public void initView() {
        adv_browser_layout = (BrowserLayout) findViewById(R.id.adv_browser_layout);

        if (browserurl.endsWith(".apk")){

        }else{
            if (!TextUtils.isEmpty(this.browserurl)) {
                this.adv_browser_layout.a(this.browserurl);
            }
            if (!this.isb) {
                this.adv_browser_layout.c();
            } else {
                this.adv_browser_layout.d();
            }
        }
        if (title!=null&&!title.isEmpty()){
            adv_browser_layout.setmTextView(title);
        }

        if (title.isEmpty()){
            adv_browser_layout.setBack(true);
        }
    }

    @Override
    public void initData() {

    }

    public static void intentTuiABrowserA(Context context, String url) {
        if (!TextUtils.isEmpty(url)) {
            Intent var2 = new Intent(context, MyTmActivity.class);
            var2.putExtra("BUNDLE_KEY_URL", url);
            var2.putExtra("BUNDLE_KEY_SHOW_BOTTOM_BAR", true);
            context.startActivity(var2);
        }
    }
    public static void intentTuiABrowserA(Context context, String url,String title) {
        if (!TextUtils.isEmpty(url)) {
            Intent var2 = new Intent(context, MyTmActivity.class);
            var2.putExtra("BUNDLE_KEY_URL", url);
            var2.putExtra("BUNDLE_KEY_SHOW_BOTTOM_BAR", true);
            var2.putExtra("title",title);
            context.startActivity(var2);
        }
    }

    private void a() {
        if (this.adv_browser_layout.a()) {
            this.adv_browser_layout.b();
        } else {
            this.adv_browser_layout.a("about:blank");
            this.finish();
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
                return super.onKeyDown(var1, var2);
        }
    }
}
