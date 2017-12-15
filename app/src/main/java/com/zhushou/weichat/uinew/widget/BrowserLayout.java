package com.zhushou.weichat.uinew.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.abs.TuiTaskA;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/14.
 */

public class BrowserLayout extends LinearLayout {

    private Context a = null;
    private WebView b = null;
    private TextView c = null;
    private View d = null;
    private ImageButton e = null;
    private int f = 5;
    private ProgressBar g = null;
    private String h;
    private OnClickListener i;

    public BrowserLayout(Context var1) {
        super(var1);
        this.a(var1);
    }
    public void setBack(boolean isVisibility){
        this.e.setVisibility(isVisibility?View.VISIBLE:View.GONE);
    }

    public BrowserLayout(Context var1, AttributeSet var2) {
        super(var1, var2);
        this.a(var1);
    }

    public void setOnBackClickListener(OnClickListener var1) {
        this.i = var1;
    }

    private void a(Context var1) {
        this.a = var1;
        this.setOrientation(LinearLayout.VERTICAL);
        this.d = LayoutInflater.from(var1).inflate(R.layout.layout_browser, (ViewGroup) null);
        this.e = (ImageButton) this.d.findViewById(R.id.browser_controller_back);
        this.c = (TextView) this.d.findViewById(R.id.browser_controller_title);
        this.e.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
                if (BrowserLayout.this.a()) {
                    BrowserLayout.this.b();
                } else if (BrowserLayout.this.i != null) {
                    BrowserLayout.this.i.onClick(var1);
                }

            }
        });
        this.addView(this.d, -1, -2);
        this.g = (ProgressBar) LayoutInflater.from(var1).inflate(R.layout.tm_progress_horizontal, (ViewGroup) null);
        this.g.setMax(100);
        this.g.setProgress(0);
        this.addView(this.g, -1, (int) TypedValue.applyDimension(0, (float) this.f, this.getResources().getDisplayMetrics()));
        this.b = new WebView(var1);
        this.b.getSettings().setJavaScriptEnabled(true);
        this.b.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
        this.b.getSettings().setDefaultTextEncodingName("UTF-8");
        this.b.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.b.getSettings().setBuiltInZoomControls(false);
        this.b.getSettings().setUseWideViewPort(true);
        this.b.getSettings().setLoadWithOverviewMode(true);
        this.b.getSettings().setSupportZoom(false);
        this.b.getSettings().setPluginState(WebSettings.PluginState.ON);
        this.b.getSettings().setDomStorageEnabled(true);
        this.b.getSettings().setLoadsImagesAutomatically(true);
        this.b.setDownloadListener(new BrowserLayout.a());
        LayoutParams var2 = new LayoutParams(-1, 0, 1.0F);
        this.addView(this.b, var2);
        this.b.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView var1, int var2) {
                super.onProgressChanged(var1, var2);
                if (var2 == 100) {
                    BrowserLayout.this.g.setVisibility(View.GONE);
                } else {
                    BrowserLayout.this.g.setVisibility(View.VISIBLE);
                    BrowserLayout.this.g.setProgress(var2);
                }
            }
        });
        this.b.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if (url.startsWith("weixin://wap/pay?")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        getContext().startActivity(intent);
                    }else if(url.equals(VApp.PAYTYPE_ZFB)){//贝付宝
                        url = url.replaceFirst("intent://", "alipays://");
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                        getContext().startActivity(intent);
                    }else{
                        Map extraHeaders = new HashMap();
                        extraHeaders.put("Referer", view.getUrl());
                        view.loadUrl(url,extraHeaders);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }

            public void onPageFinished(WebView var1, String var2) {
                super.onPageFinished(var1, var2);
                BrowserLayout.this.h = var2;

                try {
                    if (BrowserLayout.this.b != null && BrowserLayout.this.b.getTitle() != null) {
                        // TODO: 2017/11/29  隐藏标题名称
                        if (BrowserLayout.this.b.getTitle().length() >= 9) {
//                            BrowserLayout.this.c.setText(BrowserLayout.this.b.getTitle().substring(0, 7) + "...");
                        } else {
//                            BrowserLayout.this.c.setText(BrowserLayout.this.b.getTitle());
                        }
                    }
                } catch (Exception var4) {
                    var4.printStackTrace();
                }

            }
        });
    }

    public void a(String var1) {
        this.b.loadUrl(var1);
    }

    public boolean a() {
        return null != this.b ? this.b.canGoBack() : false;
    }

    public void b() {
        if (null != this.b) {
            this.b.goBack();
        }

    }

    public WebView getWebView() {
        return this.b != null ? this.b : null;
    }

    public void c() {
        this.d.setVisibility(View.GONE);
    }

    public void d() {
        this.d.setVisibility(View.VISIBLE);
    }

    public void setmTextView(String var1) {
        this.c.setText(var1);
    }

    class a implements DownloadListener {
        a() {
        }

        public void onDownloadStart(String var1, String var2, String var3, String var4, long var5) {
            if (var1.endsWith(".apk")) {
                ProgressDialog var7 = new ProgressDialog(BrowserLayout.this.a);
                var7.setProgressStyle(1);
                var7.setTitle("正在下载");
                var7.setMessage("完成进度...");
                var7.setProgressStyle(1);
                var7.setCancelable(true);
                final TuiTaskA var8 = new TuiTaskA(BrowserLayout.this.a, var7);
                var8.execute(new String[]{var1});
                var7.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface var1) {
                        var8.cancel(true);
                    }
                });
            }

        }
    }

}