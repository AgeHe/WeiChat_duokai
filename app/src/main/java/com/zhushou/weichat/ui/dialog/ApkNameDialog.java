package com.zhushou.weichat.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.ApkItemInfo;
import com.zhushou.weichat.bean.AppUsersInfo;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.ui.pay.MyDialog;
import com.zhushou.weichat.uinew.info.HomeItemInfo;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/1/7.
 */

public class ApkNameDialog {

    private static final String TAG = "ApkNameDialog";

    public interface ApkNameCallback{
        void confirmCallback(String apkName);
    }

    private Context mContext;
    private MyDialog mDialog;
    private Resources mResources;
    public ApkNameDialog(Context context){
        this.mContext = context;
        mResources = mContext.getResources();
        //mDialog=new MyDialog((Activity) context,this);

    }

    public void dialog(ApkItemInfo itemInfo, final ApkNameCallback callback){
        final AlertDialog myDialog;
        myDialog = new AlertDialog.Builder(mContext).create();
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_layout_change_apkname, null);
        final EditText et_apk_name = (EditText) view.findViewById(R.id.et_apk_name);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_pay_btn = (TextView) view.findViewById(R.id.tv_pay_btn);
        if (itemInfo!=null){
            tv_title.setText(mResources.getString(R.string.mingcheng_genggai));
            et_apk_name.setHint(itemInfo.getTitle());
            tv_pay_btn.setText(mResources.getString(R.string.que_ding));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        myDialog.getWindow().setContentView(view, params);
        myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //??????
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        //??? --> ??????
        view.findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        //??? --> ????
        tv_pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_apk_name.getText()!=null&&!et_apk_name.getText().toString().equals("")){
                    String apkName = et_apk_name.getText().toString();
                    boolean isCreate = true;
                    List<AppUsersInfo> userList = APPUsersShareUtil.getUserList(mContext);
                    for (AppUsersInfo info:userList){
                        if (apkName.equals(info.getAppName())){
                            isCreate = false;
                            break;
                        }
                    }
                    if (isCreate){
                        if (callback!=null)
                            callback.confirmCallback(apkName);
                        myDialog.dismiss();
                    }else{
                        Toast.makeText(mContext,mResources.getString(R.string.appName_chongfu),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext,mResources.getString(R.string.input_not),Toast.LENGTH_SHORT).show();
                }

            }
        });
        et_apk_name.setClickable(true);
    }

    public void changenameOrCreateNameDialog(HomeItemInfo itemInfo, final ApkNameCallback callback){
        final AlertDialog myDialog;
        myDialog = new AlertDialog.Builder(mContext).create();
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_layout_change_apkname, null);
        final EditText et_apk_name = (EditText) view.findViewById(R.id.et_apk_name);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_pay_btn = (TextView) view.findViewById(R.id.tv_pay_btn);
        if (itemInfo!=null){
            tv_title.setText(mResources.getString(R.string.mingcheng_genggai));
            et_apk_name.setHint(itemInfo.getTitle());
            tv_pay_btn.setText(mResources.getString(R.string.que_ding));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        myDialog.getWindow().setContentView(view, params);
        myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //??????
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        //??? --> ??????
        view.findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        //??? --> ????
        tv_pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_apk_name.getText()!=null&&!et_apk_name.getText().toString().equals("")){
                    String apkName = et_apk_name.getText().toString();
                    boolean isCreate = true;
                    List<AppUsersInfo> userList = APPUsersShareUtil.getUserList(mContext);
                    for (AppUsersInfo info:userList){
                        if (apkName.equals(info.getAppName())){
                            isCreate = false;
                            break;
                        }
                    }
                    if (isCreate){
                        if (callback!=null)
                            callback.confirmCallback(apkName);
                        myDialog.dismiss();
                    }else{
                        Toast.makeText(mContext,mResources.getString(R.string.appName_chongfu),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext,mResources.getString(R.string.input_not),Toast.LENGTH_SHORT).show();
                }

            }
        });
        et_apk_name.setClickable(true);
    }

    public void myAlertDialog(String content,final ApkNameCallback callback){
        final AlertDialog myDialog;
        myDialog = new AlertDialog.Builder(mContext).create();
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_layout_alert, null);
        TextView tv_pay_btn = (TextView) view.findViewById(R.id.tv_pay_btn);
        TextView tv_alert_content = (TextView) view.findViewById(R.id.tv_alert_content);
        if (content!=null){
            tv_alert_content.setText(Html.fromHtml(String.format(mResources.getString(R.string.qd_shanchu_fenshen),content)));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        myDialog.getWindow().setContentView(view, params);
        myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //??????
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        //??? --> ??????
        view.findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        //??? --> ????
        tv_pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (callback!=null)
                        callback.confirmCallback("");
                    myDialog.dismiss();

            }
        });
    }


    public android.support.v7.app.AlertDialog.Builder myAlert(String message, ApkNameCallback callback){
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(mContext,R.style.AlertDialogCustom)
                .setTitle(mResources.getString(R.string.ti_shi))
                .setMessage(Html.fromHtml(message))
                .setPositiveButton(mResources.getString(R.string.que_ding), (dialogInterface, i) -> {
                    if (callback!=null){
                        callback.confirmCallback("");
                    }
                })
                .setCancelable(true);
        alertDialog.create();
        alertDialog.show();
        return alertDialog;
    }
    public android.support.v7.app.AlertDialog.Builder alertDialogToast(String message, ApkNameCallback callback){
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(mContext,R.style.AlertDialogCustom)
                .setTitle(mResources.getString(R.string.ti_shi))
                .setMessage(Html.fromHtml(message))
                .setPositiveButton(mResources.getString(R.string.que_ding), (dialogInterface, i) -> {
                    if (callback!=null){
                        callback.confirmCallback("");
                    }
                })
                .setCancelable(true);
        alertDialog.create();
        return alertDialog;
    }


    public interface baoyueCallback{
        void baoyueGo(String price);
    }

    public void vipbaoyue(boolean isFirstPay,baoyueCallback callback){
        UpdatePriceInfo updateMessageInfo = APPUsersShareUtil.getUpdateMessage(mContext);
        if (updateMessageInfo.getScfy()==null)
            return;
        String firstpayStr = String.format(mContext.getResources().getString(R.string.first_pay_baoyue),updateMessageInfo.getScfy(),updateMessageInfo.getZcfy());
        String paystr = String.format(mContext.getResources().getString(R.string.pay_baoyue),updateMessageInfo.getZcfy());
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage(Html.fromHtml(isFirstPay?paystr:firstpayStr))
                .setPositiveButton("去包月", (dialogInterface, i) -> {
                    if (callback!=null){
                        callback.baoyueGo("");
                    }
                })
                .setNegativeButton("取消", (dialogInterface, i) -> {

                })
                .setCancelable(false);
        alertDialog.create();
        alertDialog.show();
    }

    public interface selectPayTypeCallback{
        void aSingle();
        void lifeTime();
    }
    public void selectPayType(selectPayTypeCallback callback){
        UpdatePriceInfo updateMessageInfo = APPUsersShareUtil.getUpdateMessage(mContext);
        final AlertDialog myDialog;
        myDialog = new AlertDialog.Builder(mContext).create();
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_select_pay_type, null);
        TextView tv_single = (TextView) view.findViewById(R.id.tv_single);
        TextView tv_life_time = (TextView) view.findViewById(R.id.tv_life_time);
        tv_single.setText(String.format(mContext.getResources().getString(R.string.pay_single),updateMessageInfo.getScfy()));
        tv_life_time.setText(String.format(mContext.getResources().getString(R.string.pay_lifeTime),updateMessageInfo.getZcfy()));
        view.findViewById(R.id.iv_close).setOnClickListener(view1 -> {
            myDialog.dismiss();
        });
        tv_single.setOnClickListener(view13 -> {
            callback.aSingle();
            myDialog.dismiss();
        });
        tv_life_time.setOnClickListener(view12 -> {
            callback.lifeTime();
            myDialog.dismiss();
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        myDialog.getWindow().setContentView(view, params);
        myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }


    public void myAddDeskAlertDialog(String content,final ApkNameCallback callback){
        final AlertDialog myDialog;
        myDialog = new AlertDialog.Builder(mContext).create();
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_layout_adddesktop, null);
        TextView tv_pay_btn = (TextView) view.findViewById(R.id.tv_pay_btn);
        TextView tv_alert_content = (TextView) view.findViewById(R.id.tv_alert_content);
        if (content!=null){
            tv_alert_content.setText(Html.fromHtml(String.format(mResources.getString(R.string.qd_adddesktop),content)));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        myDialog.getWindow().setContentView(view, params);
        myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //??????
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        //??? --> ??????
        view.findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        //??? --> ????
        tv_pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                if (callback!=null)
                    callback.confirmCallback("");
            }
        });
    }
}
