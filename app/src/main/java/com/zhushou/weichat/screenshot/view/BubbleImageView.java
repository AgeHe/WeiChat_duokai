package com.zhushou.weichat.screenshot.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2017/4/10.
 * 功能:
 */

public class BubbleImageView extends ImageView {


    private static final int DEFAULT_RADIUS = 10;
    private static final int DEFAULT_TRIANGLE_WIDTH = 20;
    private static final int DEFAULT_TRIANGLE_HEIGHT = 20;
    private static final int DEFAULT_TRIANGLE_MARGIN_TOP = 20;


    private int mViewWidth = 0;//宽度
    private int mViewHeight = 0;//高度
    private int mTriangleWidth = 0;//气泡三角形的宽度
    private int mTriangleHeight = 0;//气泡三角形的高度
    private Path mBubblePath;//用于切割ImageView
    private int mRadius = 0;//ImageView四角的半径
    private int mTriangleMarginTop = 0;//气泡三角形离顶部的距离
    private int mShadowColor;//当点击时ImageView表面覆盖的颜色
    private Paint mShadowPaint;
    private boolean mTouched = false;//是否被点击


    private Rect mTouchRect;//触摸范围
    private boolean mOuter = true;
    private OnClickListener mOnClickListener;
    private OnLongClickListener mOnLongClickListener;



    private Runnable mCheckLongPressRunnable;
    private boolean mHasPerformedLongPress = false;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private int mDirection = LEFT;


    public BubbleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttribute(context, attrs);


    }


    /**
     * 初始化各个参数
     *
     * @param context
     * @param attrs
     */
    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray attr = getTypedArray(context, attrs, R.styleable.BubbleImageView);
        mTriangleMarginTop = attr.getDimensionPixelSize(R.styleable.BubbleImageView_triangle_marginTop, DEFAULT_TRIANGLE_MARGIN_TOP);
        mTriangleWidth = attr.getDimensionPixelSize(R.styleable.BubbleImageView_triangle_width, DEFAULT_TRIANGLE_WIDTH);
        mTriangleHeight = attr.getDimensionPixelSize(R.styleable.BubbleImageView_triangle_height, DEFAULT_TRIANGLE_HEIGHT);
        mRadius = attr.getDimensionPixelSize(R.styleable.BubbleImageView_rect_radius, DEFAULT_RADIUS);
        int mDefaultColor = context.getResources().getColor(R.color.txtash);
        mShadowColor = attr.getColor(R.styleable.BubbleImageView_shadow_color, mDefaultColor);
        mDirection = attr.getInt(R.styleable.BubbleImageView_direction, LEFT);
    }


    protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }


    private void initPath() {
        mBubblePath = new Path();
        switch (mDirection) {
            case LEFT:
                mBubblePath.moveTo(mTriangleWidth, mTriangleMarginTop);
                mBubblePath.lineTo(0, mTriangleMarginTop + mTriangleHeight / 2.0f);
                mBubblePath.lineTo(mTriangleWidth, mTriangleMarginTop + mTriangleHeight);
                mBubblePath.close();
                mBubblePath.addRoundRect(new RectF(mTriangleWidth, 0, mViewWidth, mViewHeight), mRadius, mRadius, Path.Direction.CW);
                break;
            case RIGHT:
                mBubblePath.moveTo(mViewWidth - mTriangleWidth, mTriangleMarginTop);
                mBubblePath.lineTo(mViewWidth, mTriangleMarginTop + mTriangleHeight / 2.0f);
                mBubblePath.lineTo(mViewWidth - mTriangleWidth, mTriangleMarginTop + mTriangleHeight);
                mBubblePath.close();
                mBubblePath.addRoundRect(new RectF(0, 0, mViewWidth - mTriangleWidth, mViewHeight), mRadius, mRadius, Path.Direction.CW);
                break;
        }
    }


    private void init() {
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        initPath();
        //绘制一个气泡状的Path
        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setColor(mShadowColor);
        mTouchRect = new Rect();
        getGlobalVisibleRect(mTouchRect);//获取全局rect
    }


    public void onDraw(Canvas canvas) {
        canvas.clipPath(mBubblePath);//切割画布
        super.onDraw(canvas);
        if (mTouched)
            canvas.drawPath(mBubblePath, mShadowPaint);//当点击时，绘制表面阴影
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {


        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                return onTouchMove(ev);
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                onTouchUp(ev);
                break;
        }


        return true;
    }


    private void onTouchDown(MotionEvent ev) {
        mOuter = false;
        mTouched = true;

        if (mCheckLongPressRunnable != null)
            getHandler().removeCallbacks(mCheckLongPressRunnable);
        invalidate();
        if (mOnLongClickListener != null) {
            postCheckForLongClick();
        }
    }


    private boolean onTouchMove(MotionEvent ev) {
        if (!mTouchRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
            mTouched = false;
            invalidate();
            mOuter = true;
            return false;
        }
        return true;
    }


    private void onTouchUp(MotionEvent ev) {
        if (mHasPerformedLongPress || mOuter)
            return;
        mTouched = false;
        invalidate();
        if (mOnClickListener != null) {
            mOnClickListener.onClick(this);
        }
    }


    private void postCheckForLongClick() {
        mHasPerformedLongPress = false;
        if (mCheckLongPressRunnable == null) {
            mCheckLongPressRunnable = new CheckForLongPress();
        }
        postDelayed(mCheckLongPressRunnable, ViewConfiguration.getLongPressTimeout());


    }




    @Override
    public void setOnClickListener(OnClickListener on) {
        mOnClickListener = on;
    }


    @Override
    public void setOnLongClickListener(OnLongClickListener on) {
        mOnLongClickListener = on;
    }


    private class CheckForLongPress implements Runnable {
        @Override
        public void run() {
            if (mTouched && mOnLongClickListener != null) {
                mOnLongClickListener.onLongClick(BubbleImageView.this);
                mHasPerformedLongPress = true;
                mTouched = false;
                postInvalidate();
            }
        }
    }


}
