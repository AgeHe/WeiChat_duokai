package com.zhushou.weichat.ui.pay;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2016/11/25.
 * 功能:loding
 */

public class PayLodingDialog extends Dialog {
    private ImageView mLoadingImageView;
    private Animation mLoadingAnimation;
    public PayLodingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public PayLodingDialog(Context context, int theme) {
        super(context, theme);
    }

    public PayLodingDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View loadingView= LayoutInflater.from(getContext()).inflate(R.layout.layout_paydialog, null);
        mLoadingImageView=(ImageView) loadingView.findViewById(R.id.iv_paydialog);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        setContentView(loadingView);
    }

    @Override
    public void show() {
        super.show();
        mLoadingAnimation= AnimationUtils.loadAnimation(getContext(), R.anim.loading);
        mLoadingImageView.startAnimation(mLoadingAnimation);
    }
    @Override
    public void dismiss() {
        super.dismiss();
        mLoadingAnimation.cancel();
    }

}