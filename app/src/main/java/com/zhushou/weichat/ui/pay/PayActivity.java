package com.zhushou.weichat.ui.pay;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;

import com.lanzs.zypay.activity.PaymentActivity;
import com.umeng.analytics.MobclickAgent;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.abs.ui.VMyActivity;
import com.zhushou.weichat.ui.ListAppContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhanglinkai on 2016/11/24.
 * 功能:执行支付activity的父activity
 */

public class PayActivity extends VMyActivity<ListAppContract.ListAppPresenter> {
    private PayHelper helper;
    private boolean isActive;
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);



            switch (msg.what) {
//                case 0:
//                    if (helper.isCheck() == true && isActive == true){
//                        helper.requreHttpAsync(helper.getQureyURL(),PayActivity.this);
//                    }
//                break;
                case 0:
                    if (helper.isCheck() == true && isActive == true) {
                        if(helper.getPayTunnel().equals(VApp.PAY_XGSDK)){
                            PaymentActivity.onQueryPayStatus(PayActivity.this,"dkzs01",helper.getorderNumber(), new com.lanzs.zypay.a.a.a() {
                                @Override
                                public void onHttpResult(String s, int i) {
                                    try {
                                        JSONObject object=new JSONObject(s);
                                        if(!object.isNull("message")){
                                            String m=object.getString("message");
                                            if(m.equals("支付成功")){
                                                sendEmptyMessage(1);
                                            }else{
                                                sendEmptyMessage(2);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else{
                            helper.requreHttpAsync(helper.getQureyURL(),PayActivity.this);
                        }
                    }
                    break;
                case 1://成功
                    helper.getR().successfulclick("",helper.getType(),helper.getWaretype());//进入payhelp的肯定是webpay
                    helper.restoreDefault();
                    break;
                case 2://失败
                    helper.getR().failClick();;//进入payhelp的肯定是webpay
                    helper.restoreDefault();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPermission();
        this.helper = new PayHelper(this);
//        setHelper(helper);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        mHandle.sendEmptyMessageDelayed(0, 500);
        MobclickAgent.onResume(this);//友盟统计
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
        MobclickAgent.onPause(this);//友盟统计
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.closeSqlite();
    }

    public PayHelper getHelper() {
        return helper;
    }

//    public void setHelper(PayHelper helper) {
//        this.helper = helper;
//    }


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
        return super.onKeyDown(keyCode, event);
    }
}
