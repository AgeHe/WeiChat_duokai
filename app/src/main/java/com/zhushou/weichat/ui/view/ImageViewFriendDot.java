package com.zhushou.weichat.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/6/15.
 */

public class ImageViewFriendDot extends ImageView {


    private Bitmap mBitmap;
    private boolean isdot = false;

    public ImageViewFriendDot(Context context) {
        super(context);
        init(context);
    }

    public ImageViewFriendDot(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){

    }
    //红色圆圈的半径
    private float radius;
    //右边和上边内边距
    private int paddingRight;
    private int paddingTop;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (getDrawable()==null){
//            return;
//        }
//
//        if (getWidth() == 0 || getHeight() == 0) {
//            return;
//        }
//        mBitmap = drawableToBitamp(getDrawable());
//        if (mBitmap == null) {
//            return;
//        }

        if (!isdot)
            return;
        //初始化半径
        radius = getWidth() / 6;
        //初始化边距
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        //初始化画笔
        Paint paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置颜色为红色
        paint.setColor(0xffff4444);
        //设置填充样式为充满
        paint.setStyle(Paint.Style.FILL);
        //画圆
        canvas.drawCircle(getWidth() - radius - paddingRight/2, radius + paddingTop/2, radius, paint);

    }

    private Bitmap drawableToBitamp(Drawable drawable){
        if (drawable==null)
            return null;
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void addDot(){
        isdot = true;
        invalidate();
    }

    public void clearDot(){
        isdot = false;
        invalidate();
    }







}
