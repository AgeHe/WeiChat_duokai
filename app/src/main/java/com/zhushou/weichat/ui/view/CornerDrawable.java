package com.zhushou.weichat.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;

import com.zhushou.weichat.utils.DisplayUtil;

/**
 * Created by Administrator on 2017/3/3.
 */

public class CornerDrawable extends Drawable {

    private Paint mPaint;
    private Bitmap bmp;
    private RectF rectF;
    private Context context;
    private int ScreenWidth = 0;
    private int ScreenHeight = 0;
    private int ovalHeight = 0;
    public String bacColor ;
    private int dp1 = 0;
    private int textsize = 0;
    public CornerDrawable(Context context,int width,int height,String color){
        this.context = context;
        this.mPaint = new Paint();
        this.ScreenHeight = height;
        this.ScreenWidth = width;
        this.bacColor = color;
        ovalHeight = DisplayUtil.dip2px(context,10);
        dp1 = DisplayUtil.dip2px(context,1);
        textsize = DisplayUtil.dip2px(context,60);
    }

    @Override
    public void draw(Canvas canvas) {
        Path path = new Path();
        //left top right bottom
        RectF rectOvalF = new RectF(0,ovalHeight*3,ScreenWidth,ovalHeight*5);
        path.addOval(rectOvalF, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.REPLACE);
        canvas.drawColor(Color.parseColor(bacColor));

        canvas.save();
        //画椭圆线
        mPaint.setColor(Color.parseColor("#d9d9d9"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(dp1);
//        // 实例化路径
//        mPath = new Path();
        // 移动起点至[100,100]
        path.moveTo(0, 0);
        // 添加一条弧线到Path中
        RectF oval = new RectF(0,ovalHeight*3-dp1,ScreenWidth,ovalHeight*5-dp1);
        path.addOval(oval, Path.Direction.CW);

        canvas.restore();
        canvas.clipPath(path, Region.Op.REPLACE);
        canvas.drawColor(Color.parseColor(bacColor));
        canvas.drawPath(path, mPaint);

//        canvas.restore();

//        canvas.save();
        canvas.clipRect(0,ovalHeight*4,ScreenWidth,ScreenHeight-ovalHeight*4, Region.Op.REPLACE);

        canvas.drawColor(Color.parseColor(bacColor));
//        canvas.restore();
    }

    @Override
    public void setAlpha(int i) {
        mPaint.setAlpha(i);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
