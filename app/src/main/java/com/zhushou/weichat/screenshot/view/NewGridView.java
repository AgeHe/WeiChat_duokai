package com.zhushou.weichat.screenshot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by zhanglinkai on 2017/3/15.
 * 功能:
 */

public class NewGridView extends GridView {

    public NewGridView(Context context) {
        super (context);
    }

    public NewGridView(Context context, AttributeSet attrs) {
        super (context, attrs) ;
    }

    public NewGridView(Context context, AttributeSet attrs , int defStyleAttr) {
        super (context, attrs , defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec , int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST) ;
        super.onMeasure(widthMeasureSpec , expandSpec);

    }
}
