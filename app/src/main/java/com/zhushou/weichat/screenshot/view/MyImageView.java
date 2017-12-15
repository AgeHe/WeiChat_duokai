package com.zhushou.weichat.screenshot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by zhanglinkai on 2017/4/11.
 * 功能:
 */

public class MyImageView extends ImageView{
    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Snap to width
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
