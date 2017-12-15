package com.zhushou.weichat.auxiliary.ui.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.utils.DisplayUtil;

/**
 * Created by Administrator on 2017/3/16.
 */

public class AuxiliarySystemSettingDialog{

    public interface AuxiliaryLisener{
        void onAuxiliaryClick();
    }

    private AuxiliaryLisener lisener;

    private Resources mResources;

    public AuxiliarySystemSettingDialog(AuxiliaryLisener lisener){
        this.lisener = lisener;

    }

    public void readioDialog(Context context){
        mResources = context.getResources();
        final AlertDialog myDialog;
        myDialog = new AlertDialog.Builder(context).create();
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_open_auxiliary_dialog, null);
        TextView tv_open_auxiliary = (TextView) v.findViewById(R.id.tv_open_auxiliary);
        TextView tv_alert = (TextView) v.findViewById(R.id.tv_alert);
        ImageView iv_exit = (ImageView) v.findViewById(R.id.iv_exit);
        int screenWidth = DisplayUtil.getScreenWidth(context);
        tv_alert.setText(mResources.getString(R.string.open)+mResources.getString(R.string.auxiliary_name));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        myDialog.getWindow().setContentView(v, params);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        tv_open_auxiliary.setOnClickListener(v1 -> {
            lisener.onAuxiliaryClick();
            myDialog.dismiss();
        });
        iv_exit.setOnClickListener(v12 -> myDialog.dismiss());
//
    }

}
