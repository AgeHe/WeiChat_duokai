package com.zhushou.weichat.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.api.OpenRedRequest;
import com.zhushou.weichat.bean.LoginMsgInfo;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.bean.UpdateInfo;
import com.zhushou.weichat.bean.WxUserInfo;
import com.zhushou.weichat.interfac.UpdateInterface;
import com.zhushou.weichat.ui.dialog.UpdateDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/10/30.
 */

public class UpdateControllerImpl  {

    Activity mActivity;
    UpdateInterface updateImpl;
    private CountUtil countUtil;
    private UpdateDialog updateDialog;
    private ProgressDialog progressDialog;
    WxUserInfo wxUserInfo;
    private UpdateInfo updateVersionInfo;

    public UpdateControllerImpl(Activity mActivity, UpdateInterface viewImpl){
        this.mActivity = mActivity;
        this.updateImpl = viewImpl;
        this.countUtil = new CountUtil(mActivity);
    }


    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 4:
                    OkHttpResult result = (OkHttpResult) msg.obj;
                    JSONObject updateJo = null;
                    if (progressDialog!=null&&progressDialog.isShowing())
                        progressDialog.dismiss();
                    try {
                        if (!result.isSuccess){
                            return;
                        }
                        updateJo = new JSONObject(result.msg);
                        updateVersionInfo = UpdateUtil.DecodeUpdateJson(updateJo,countUtil.getFrom(),countUtil.getApp(),countUtil.getVersion());
                        if (updateVersionInfo != null) {
                            if (updateVersionInfo.isUpdate()) {
                                String address = updateVersionInfo.getUpdateAddress();
                                if (address != null && !address.isEmpty()) {
                                     updateImpl.checkNeedUpdate(true);
                                }
                            }else {
                                updateImpl.checkNeedUpdate(false);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


    public void checkVersionUpdate(){
        if (progressDialog==null){
            progressDialog = new ProgressDialog(mActivity);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(true);
            progressDialog.getWindow().setWindowAnimations(R.style.dialog_JB_SF);
        }
        progressDialog.setMessage("请稍后...");
        progressDialog.show();
        OpenRedRequest.requestUpdateStatus(4,mHandler);
    }


}
