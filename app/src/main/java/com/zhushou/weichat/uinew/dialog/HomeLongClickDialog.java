package com.zhushou.weichat.uinew.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zhushou.weichat.R;
import com.zhushou.weichat.utils.DisplayUtil;

/**
 * Created by Administrator on 2017/11/29.
 */

public class HomeLongClickDialog extends Dialog implements View.OnClickListener{


    public HomeLongClickDialog(@NonNull Context context) {
        super(context);
        initView();
    }

    public HomeLongClickDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_home_longclick,null);
        view.findViewById(R.id.tv_change_name).setOnClickListener(this);
        view.findViewById(R.id.tv_create_Shortcut).setOnClickListener(this);
        view.findViewById(R.id.tv_delete_app).setOnClickListener(this);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        Window dialogWindow = this.getWindow();
        dialogWindow.setWindowAnimations(R.style.ScaleAnimBottom);
        super.setContentView(view);
    }


    @Override
    public void show() {
        super.show();
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        int screenWidth = (int) (DisplayUtil.getScreenWidth(getContext())*0.8f);
        //设置高宽
        lp.width = screenWidth; // 宽度
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.ScaleAnimBottom);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_change_name:
                if (homeLongClickLisener!=null)
                    homeLongClickLisener.changename();
                dismiss();
                break;
            case R.id.tv_create_Shortcut:
                if (homeLongClickLisener!=null)
                    homeLongClickLisener.createShortcut();
                dismiss();
                break;
            case R.id.tv_delete_app:
                if (homeLongClickLisener!=null)
                    homeLongClickLisener.deleteApp();
                dismiss();
                break;
            case R.id.tv_cancel:
               dismiss();
                break;
        }
    }

    public HomeLongClickLisener homeLongClickLisener;
    public HomeLongClickDialog setHomeLongClickLisener(HomeLongClickLisener homeLongClickLisener){
        this.homeLongClickLisener = homeLongClickLisener;
        return this;
    }

    public interface HomeLongClickLisener{
        void changename();
        void createShortcut();
        void deleteApp();
    }

}
