package com.zhushou.weichat.base;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;

import com.zhushou.weichat.ui.pay.PayHelper;


/**
 * Created by zhanglinkai on 2016/11/24.
 * 功能:执行支付activity的父activity
 */

public abstract class PayActivity extends BaseActivity  {
    private PayHelper helper;
    private boolean isActive;
    public ProgressDialog payProgressDialog;
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (helper.isCheck() == true && isActive == true) {
                        if (payProgressDialog.isShowing())
                            payProgressDialog.dismiss();
                        helper.requreHttpAsync(helper.getQureyURL(),mActivity);
                    }
                    break;
            }
        }
    };


    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setPermission();
        this.helper = new PayHelper(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        if (payProgressDialog==null){
            payProgressDialog = new ProgressDialog(mActivity);
            payProgressDialog.setMessage("查询订单...");
            payProgressDialog.setCanceledOnTouchOutside(false);
            payProgressDialog.setCancelable(true);
        }

        if (helper!=null&&helper.isCheck()){
            if (!payProgressDialog.isShowing())
                payProgressDialog.show();
            mHandle.sendEmptyMessageDelayed(0, 1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    public PayHelper getHelper() {
        if (helper==null)
            helper = new PayHelper(mActivity);
        return helper;
    }



    public final int WEITE_STORAGE = 20;
    public void setPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            int weiteStoragePermission = ContextCompat.checkSelfPermission(PayActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readStoragePermission = ContextCompat.checkSelfPermission(PayActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int readPhotoStatePermission = ContextCompat.checkSelfPermission(PayActivity.this, Manifest.permission.READ_PHONE_STATE);
            if(weiteStoragePermission != PackageManager.PERMISSION_GRANTED||readStoragePermission != PackageManager.PERMISSION_GRANTED||readPhotoStatePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(PayActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE}, WEITE_STORAGE);
                return;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case WEITE_STORAGE:  // 申请储存权限成功  用于二维码生成
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//            builder.setTitle("应用提示");
//
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }
}
