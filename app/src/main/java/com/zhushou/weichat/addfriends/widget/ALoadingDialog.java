package com.zhushou.weichat.addfriends.widget;

import android.app.ProgressDialog;
import android.content.Context;

import com.zhushou.weichat.R;

/**
 * Created by Administrator on 2017/3/29.
 */

public class ALoadingDialog{
    private Context mContext;
    private ProgressDialog progressDialog;
    public ALoadingDialog(Context mContext){
        this.mContext = mContext;
        progressDialog = new ProgressDialog(mContext);
        init();
    }

    private void init(){
        progressDialog.setMessage(mContext.getResources().getString(R.string.wx_addf_loading_message));
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void setMessage(String message){
        progressDialog.setMessage(message);
    }

    public void loadingShow(){
        if (progressDialog!=null)
            progressDialog.show();
    }

    public void dismiss(){
        if (progressDialog!=null)
            progressDialog.dismiss();
    }

}
