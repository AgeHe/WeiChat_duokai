package com.zhushou.weichat.addfriends.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.UMengStatisticalKey;
import com.zhushou.weichat.ui.MainActivity;
import com.zhushou.weichat.ui.dialog.ApkNameDialog;
import com.zhushou.weichat.utils.MyDownloadManager;

/**
 * Created by Administrator on 2017/3/31.
 */

public class WebActivity extends Activity {

    WebView web_view;
    TextView tv_title;
    private String intentUrl = "";
    Bundle mBundle;

    ApkNameDialog apkNameDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_web);
        web_view = (WebView) findViewById(R.id.web_view);
        tv_title = (TextView) findViewById(R.id.tv_title);
        apkNameDialog = new ApkNameDialog(this);

        // ~~~ 设置数据
        web_view.getSettings().setJavaScriptEnabled(true);
        Intent mIntent = getIntent();
        mBundle = mIntent.getExtras();
        if (mBundle!=null){
//            if (mBundle==null)
//                return;
            intentUrl = mBundle.getString("url");
            tv_title.setText(mBundle.getString("title_name"));
        }
        web_view.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        if (intentUrl!=null&&!intentUrl.equals("")){
            apkNameDialog.myAlert("下载\t\t" + tv_title.getText().toString() + "\t\t?", new ApkNameDialog.ApkNameCallback() {
                @Override
                public void confirmCallback(String apkName) {
                    MobclickAgent.onEvent(WebActivity.this, UMengStatisticalKey.adv_download);
                    MyDownloadManager manager = new MyDownloadManager(WebActivity.this);
                    manager.download(intentUrl,tv_title.getText().toString(),tv_title.getText()+"下载中...");
                    Toast.makeText(WebActivity.this,"下载中...",Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            web_view.loadUrl(intentUrl==null&&intentUrl.equals("")?"http://www.10086ku.com/all.html":intentUrl);
        }


        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(web_view.canGoBack())
                {
                    web_view.goBack();//返回上一页面
                }
                else
                {
                    if (!intentUrl.equals("")){
                        startActivity(new Intent(WebActivity.this, MainActivity.class));
                        finish();//退出界面
                    }else{
                        finish();//退出界面
                    }

                }
            }
        });
    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(web_view.canGoBack())
            {
                web_view.goBack();//返回上一页面
                return true;
            }
            else
            {
                if (!intentUrl.equals("")){
                    startActivity(new Intent(WebActivity.this, MainActivity.class));
                    finish();//退出界面
                }else{
                    finish();//退出界面
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
