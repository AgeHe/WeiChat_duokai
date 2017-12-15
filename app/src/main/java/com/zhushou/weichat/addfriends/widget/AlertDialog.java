package com.zhushou.weichat.addfriends.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zhushou.weichat.R;

/**
 * Created by Administrator on 2017/3/30.
 */

public class AlertDialog extends Dialog implements View.OnClickListener {

    public AlertDialog(@NonNull Context context) {
        super(context, R.style.DialogAlpha);
        initView();
    }

    private void initView(){
        setLayoutView();
    }

    private void setLayoutView(){
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout_clear_friends,null);
        dialogView.findViewById(R.id.tv_cancel).setOnClickListener(this);
        dialogView.findViewById(R.id.tv_clear).setOnClickListener(this);
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
    public void show() {
        super.show();
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        /////////获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels/5*4;
        /////////设置高宽
        lp.width = screenWidth; // 宽度
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_clear:
                if (itemClickLisener!=null)
                    itemClickLisener.itemclick();
                dismiss();
                break;
        }

    }
    private ItemClickLisener itemClickLisener;
    public void setClickLisener(ItemClickLisener itemClickLisener){
        this.itemClickLisener = itemClickLisener;
    }

    public interface ItemClickLisener{
        void itemclick();
    }
}
