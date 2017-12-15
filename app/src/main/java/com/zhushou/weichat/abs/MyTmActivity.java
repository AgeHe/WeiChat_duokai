package com.zhushou.weichat.abs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.zhushou.weichat.R;
import com.zhushou.weichat.uinew.widget.BrowserLayout;


/**
 * Created by Administrator on 2017/11/17.
 */

public class MyTmActivity extends Activity {

    private String a = null;
    private boolean b = true;
    private static Intent c;
    private BrowserLayout d;

    public static void startMyTmActivity(Activity mActivity,String title){
        mActivity.startActivity(new Intent(mActivity,MyTmActivity.class).putExtra("title",title));
    }

    protected void onCreate(Bundle var1) {
        super.onCreate(var1);
        this.setContentView(R.layout.tm_activity_webview_in_sdk);
        Intent var2 = this.getIntent();
        this.a = var2.getExtras().getString("BUNDLE_KEY_URL");
        this.b = var2.getExtras().getBoolean("BUNDLE_KEY_SHOW_BOTTOM_BAR");
        this.d = (BrowserLayout)this.findViewById(R.id.tm_common_web_browser_layout);
        if(!TextUtils.isEmpty(this.a)) {
            this.d.a(this.a);
        }

        if(!this.b) {
            this.d.c();
        } else {
            this.d.d();
        }

        this.d.setOnBackClickListener(new View.OnClickListener() {
            public void onClick(View var1) {
                MyTmActivity.this.a();
            }
        });
        try {
            String title = getIntent().getStringExtra("title");
            if (title!=null){
                this.d.setmTextView(title);
                this.d.setBack(true);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static void a(Context var0, String var1) {
        if(!TextUtils.isEmpty(var1)) {
            Intent var2 = new Intent(var0, MyTmActivity.class);
            var2.addFlags(268435456);
            var2.putExtra("BUNDLE_KEY_URL", var1);
            var2.putExtra("BUNDLE_KEY_SHOW_BOTTOM_BAR", true);
            var0.startActivity(var2);
        }

    }

    public static void a(Context var0, String var1, Intent var2) {
        if(!TextUtils.isEmpty(var1)) {
            c = var2;
            Intent var3 = new Intent(var0, MyTmActivity.class);
            var3.addFlags(268435456);
            var3.putExtra("BUNDLE_KEY_URL", var1);
            var3.putExtra("BUNDLE_KEY_SHOW_BOTTOM_BAR", true);
            var0.startActivity(var3);
        }

    }

    private void a() {
        if(this.d.a()) {
            this.d.b();
        } else {
            this.d.a("about:blank");
            this.finish();
            Class var1 = TuiB.a(this).a();
            if(var1 != null) {
                if(c == null) {
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
        switch(var2.getKeyCode()) {
            case 4:
                this.a();
                return true;
            default:
                return super.onKeyDown(var1, var2);
        }
    }
}
