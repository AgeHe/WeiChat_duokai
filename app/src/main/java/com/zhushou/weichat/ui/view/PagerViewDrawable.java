package com.zhushou.weichat.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zhushou.weichat.abs.ui.VUiKit;

/**
 * Created by Administrator on 2017/7/4.
 */

public class PagerViewDrawable extends Drawable {

    private int itemWidth = 0;
    private int itemHeight = 0;
    private int linesWidth = 0;
    private Context context;

    public PagerViewDrawable(Context context,int itemWidth,int itemHeight){
        this.context = context;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        this.linesWidth = VUiKit.dpToPx(context, 1);
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#d9d9d9"));
        float[] pts={0,itemHeight,itemWidth*3+linesWidth*2,itemHeight,
                0,itemHeight*2+linesWidth,itemWidth*3+linesWidth*2,itemHeight*2+linesWidth,
                0,itemHeight*3+linesWidth*2,itemWidth*3+linesWidth*2,itemHeight*3+linesWidth*2,
                0,itemHeight*4+linesWidth*3,itemWidth*3+linesWidth*2,itemHeight*4+linesWidth*3,
                itemWidth,0,itemWidth,itemHeight*5+linesWidth*4,
                itemWidth*2,0,itemWidth*2+linesWidth,itemHeight*5+linesWidth*4,
        };
        canvas.drawLines(pts,paint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
