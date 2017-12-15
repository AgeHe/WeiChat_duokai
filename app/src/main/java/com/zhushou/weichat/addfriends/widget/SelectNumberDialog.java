package com.zhushou.weichat.addfriends.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.addfriends.utils.DateUtils;

/**
 * Created by Administrator on 2017/3/29.
 */

public class SelectNumberDialog extends Dialog implements View.OnClickListener {
    private SelectNumberLisener selectNumberLisener;
    private int screenWidth ;
    private Context mContext;
    private int selectedQuantity=10;
    public SelectNumberDialog(@NonNull Context context) {
        super(context,R.style.DialogAlpha);
        initData(context);
        setLayoutView();
    }

    private void initData(Context context){
        screenWidth = DateUtils.getScreenWidth(context);
        this.mContext = context;
    }
    TextView tv_select_10;
    TextView tv_select_50;
    TextView tv_select_100;
    TextView tv_select_200;
    private void setLayoutView(){
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_number,null);
        tv_select_10 = (TextView) dialogView.findViewById(R.id.tv_select_10);
        tv_select_50 = (TextView) dialogView.findViewById(R.id.tv_select_50);
        tv_select_100 = (TextView) dialogView.findViewById(R.id.tv_select_100);
        tv_select_200 = (TextView) dialogView.findViewById(R.id.tv_select_200);
        tv_select_10.setOnClickListener(this);
        tv_select_50.setOnClickListener(this);
        tv_select_100.setOnClickListener(this);
        tv_select_200.setOnClickListener(this);

        this.setCanceledOnTouchOutside(true);

        Window win = this.getWindow();
        win.setGravity(Gravity.CENTER);                       //从中间弹出
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;   //宽度自适应
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;  //高度自适应
        win.setAttributes(lp);

        super.setContentView(dialogView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_select_10:
                selectNumberLisener.number(10);
                dismiss();
                break;
            case R.id.tv_select_50:
                selectNumberLisener.number(50);
                dismiss();
                break;
            case R.id.tv_select_100:
                selectNumberLisener.number(100);
                dismiss();
                break;
            case R.id.tv_select_200:
                selectNumberLisener.number(200);
                dismiss();
                break;
        }

    }

    @Override
    public void show() {
        super.show();

        setItemColor(selectedQuantity);

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        /////////获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels/4*3;
        /////////设置高宽
        lp.width = screenWidth; // 宽度
        dialogWindow.setAttributes(lp);
    }

    public void setSelectedIndex(int quantity){
        this.selectedQuantity = quantity;
    }

    private void setItemColor(int quantity){
        setTextViewAllColor();
        switch (quantity){
            case 10:
            if (tv_select_10!=null)
                tv_select_10.setTextColor(Color.parseColor("#4d3d3d"));
                break;
            case 50:
                if (tv_select_50!=null)
                    tv_select_50.setTextColor(Color.parseColor("#4d3d3d"));
                break;
            case 100:
                if (tv_select_100!=null)
                    tv_select_100.setTextColor(Color.parseColor("#4d3d3d"));
                break;
            case 200:
                if (tv_select_200!=null)
                    tv_select_200.setTextColor(Color.parseColor("#4d3d3d"));
                break;
        }
    }

    private void setTextViewAllColor(){
        if (tv_select_10!=null)
            tv_select_10.setTextColor(Color.parseColor("#acacac"));
        if (tv_select_50!=null)
            tv_select_50.setTextColor(Color.parseColor("#acacac"));
        if (tv_select_100!=null)
            tv_select_100.setTextColor(Color.parseColor("#acacac"));
        if (tv_select_200!=null)
            tv_select_200.setTextColor(Color.parseColor("#acacac"));
    }

    public void setonClickNumberLisener(SelectNumberLisener selectNumberLisener){
        this.selectNumberLisener = selectNumberLisener;
    }

    public interface SelectNumberLisener{
        void number(int value);
    }
}
