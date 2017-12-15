package com.zhushou.weichat.auxiliary.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zhushou.weichat.R;

/**
 * Created by Administrator on 2017/3/13.
 */

public class FloatWindowSmallView extends FrameLayout {

    private Context context;
    /**
     * 用于更新小悬浮窗的位置
     */
    private WindowManager windowManager;
    /**
     * 记录小悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录小悬浮窗的高度
     */
    public static int viewHeight;

    /**
     * 小悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;

    public ImageView iv_float_main;

    public FloatWindowSmallView(Context context) {
        super(context);

        init(context);
    }
    public FloatWindowSmallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public FloatWindowSmallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_float_window_big,this,true);
        iv_float_main = (ImageView) findViewById(R.id.iv_float_main);
        viewWidth = iv_float_main.getLayoutParams().width;
        viewHeight = iv_float_main.getLayoutParams().height;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                setIv_float_mainClick();
//                Intent mIntent = new Intent(context, CustomerServiceActivity.class);
//                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                context.startActivity(mIntent);
                Intent mIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context.startActivity(mIntent);
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isFloatSwitch = false;
    public void setIv_float_mainClick(){
        if (iv_float_main==null)
            return;

        ObjectAnimator oaAnimator= ObjectAnimator.ofFloat(iv_float_main, "rotation", isFloatSwitch?0:45,isFloatSwitch?135:180);
        oaAnimator.setDuration(300);
        oaAnimator.start();
        isFloatSwitch = !isFloatSwitch;

    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params
     *            小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

}
