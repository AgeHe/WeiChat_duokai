package com.zhushou.weichat.screenshot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by zhanglinkai on 2017/3/16.
 * 功能:
 */

public class Newlistview extends ListView {
    public Newlistview(Context context) {
        super (context);
    }

    public Newlistview(Context context, AttributeSet attrs) {
        super (context, attrs) ;
    }

    public Newlistview(Context context, AttributeSet attrs , int defStyleAttr) {
        super (context, attrs , defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec , int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST) ;
        super.onMeasure(widthMeasureSpec , expandSpec);

    }
}

