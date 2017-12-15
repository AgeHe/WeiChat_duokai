package com.zhushou.weichat.screenshot.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * Created by zhanglinkai on 2017/4/1.
 * 功能:
 */

public class CustomNumberPicker extends NumberPicker
{

    public CustomNumberPicker(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public void addView(View child)
    {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params)
    {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params)
    {
        super.addView(child, params);
        updateView(child);
    }

    public void updateView(View view)
    {
        if (view instanceof EditText)
        {
            //这里修改字体的属性
            ((EditText) view).setTextSize(12);
            ((EditText) view).setTextColor(Color.GREEN);
        }
    }

}
