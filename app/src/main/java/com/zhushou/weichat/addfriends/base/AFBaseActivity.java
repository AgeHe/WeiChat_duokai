package com.zhushou.weichat.addfriends.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.zhushou.weichat.auxiliary.interfac.BaseViewInterface;

/**
 * Created by Administrator on 2017/3/28.
 */

public abstract class AFBaseActivity extends Activity implements View.OnClickListener,BaseViewInterface {
    public Context mContext;
    public Resources mResources;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        this.mResources = this.getResources();
        init(savedInstanceState);
        initView();
        initData();
    }
    protected void viewOnClick(View view){}
    protected void init(Bundle savedInstanceState) {}
    @Override
    protected void onStart() {
        super.onStart();
        setPermission();
    }
    @Override
    public void onClick(View view) {
        viewOnClick(view);
    }
    public final int CONTACTS_PERMISSION = 20;
    public void setPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            int readContactsPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS);
            int writeContactsPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CONTACTS);
            if(readContactsPermission != PackageManager.PERMISSION_GRANTED||writeContactsPermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(AFBaseActivity.this, new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS}, CONTACTS_PERMISSION);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CONTACTS_PERMISSION:
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);//友盟统计
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);//友盟统计
    }
}
