package com.zhushou.weichat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.zhushou.weichat.R;
import com.zhushou.weichat.auxiliary.base.BaseActivity;
import com.zhushou.weichat.bean.LoginMsgInfo;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.utils.LoginSP;
import com.zhushou.weichat.utils.MyToast;
import com.zhushou.weichat.utils.RandomGUID;
import com.zhushou.weichat.utils.ToastUtil;

import org.json.JSONException;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public class ComplaintsFenShenActivity extends BaseActivity {

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setContentView(R.layout.activity_fenshen);
    }


    ProgressDialog progressDialog;
    EditText et_order_number,et_phone_number,et_checkorder_content;

    @Override
    public void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_complaints_submit).setOnClickListener(this);

        et_order_number = (EditText) findViewById(R.id.et_order_number);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_checkorder_content = (EditText) findViewById(R.id.et_checkorder_content);

    }

    @Override
    public void initData() {
        randomGUID = new RandomGUID(2,mHandler);
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setMessage("请稍等，正在提交...");

    }

    @Override
    protected void viewOnClick(View view) {
        super.viewOnClick(view);
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_complaints_submit:
                LoginMsgInfo loginMsgInfo = null;
                try {
                     loginMsgInfo = LoginSP.getLoginMsg(mContext);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (et_order_number.getText().equals("")){
                    MyToast.mCenterToast("订单号必填",mContext);
                    break;
                }
                if (et_phone_number.getText().equals("")){
                    MyToast.mCenterToast("请输入联系方式，以便客服联系到您",mContext);
                    break;
                }
                if (et_checkorder_content.getText().equals("")){
                    MyToast.mCenterToast("请简单描述使用中所遇到得问题",mContext);
                    break;
                }
                progressDialog.show();
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("type","2");
                hashMap.put("orderno",et_order_number.getText().toString());
                hashMap.put("tell",et_phone_number.getText().toString());
                hashMap.put("description",et_checkorder_content.getText().toString());
//                hashMap.put("userid", randomGUID.getLocalGUID(mContext));
                if (loginMsgInfo==null){
                    ToastUtil.centerToast(mContext,"请先登录");
                    return;
                }
                hashMap.put("userid",loginMsgInfo.getId());
                ComplaintsActivity.sendComplaintsMessage(hashMap,0,mHandler);
                break;
        }
    }
    RandomGUID randomGUID;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if (progressDialog!=null&&progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    OkHttpResult okHttpResult = (OkHttpResult) msg.obj;
                    if (okHttpResult.isSuccess){
                        et_order_number.setText("");
                        et_phone_number.setText("");
                        et_checkorder_content.setText("");
                        MyToast.mCenterToastLong("投诉成功，点击“投诉记录”可查看投诉处理进度",mContext);
                    }else{
                        MyToast.mCenterToast("提交失败！",mContext);
                    }
                    break;
                case 2:

                    break;
            }

        }
    };
}
