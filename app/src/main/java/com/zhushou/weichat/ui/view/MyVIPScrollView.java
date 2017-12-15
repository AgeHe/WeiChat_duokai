package com.zhushou.weichat.ui.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.zhushou.weichat.utils.DisplayUtil;


/**
 * Created by Administrator on 2017/2/27.
 */

public class MyVIPScrollView extends ViewGroup {

    private static final String TAG = "MyScrollView";
    private String bacColor = "";
    private int statuActionHeight = 0;
    private int ScreenHeight = 0;
    private int ScreenWidth = 0;
    private int ViewMarginTop = 0;
    int actionBarHeight = 0;
    private Context mContext;
    private int topY = 0; //view在最顶端的Y轴位置
    private int buttomY = 0;//View在最低部的位置
    private int marginTop = 0;

    public MyVIPScrollView(Context context) {
        super(context);
        init(context);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public MyVIPScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public MyVIPScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        View child = getChildAt(0);
        child.layout(0, 0, ScreenWidth, ScreenHeight-actionBarHeight);
    }

    private void init(Context context){
        this.mContext = context;
        marginTop = DisplayUtil.dip2px(context,30);
        ScreenHeight = DisplayUtil.getScreenHeight(context);
        ScreenWidth = DisplayUtil.getScreenWidth(context);
        statuActionHeight = DisplayUtil.getStatusHeight(context);
        ViewMarginTop = ScreenHeight-actionBarHeight;
        TypedValue tv = new TypedValue();

        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }

        topY = actionBarHeight;
        buttomY = ScreenHeight-actionBarHeight;

        CornerDrawable drawable = new CornerDrawable(context,ScreenWidth,ViewMarginTop,"#efeff0");
        setmBackgroundDrawable(drawable);
    }

    boolean firstCreateView = true;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = ScreenWidth;
        int height = ScreenHeight-actionBarHeight;
        setMeasuredDimension(width, height);
        if (firstCreateView){
//            updateTopMargin(ViewMarginTop-actionBarHeight);
            slideToHide(true);
//            firstViewLocation();
            firstCreateView = false;
        }
        measureChildren(widthMeasureSpec, heightMeasureSpec);
//        childView.measure(width, height);
    }

    private float mLastY = -1; // save event y
    private float OFFSET_RADIO = 1.0f; // support iOS like pull
    private float distanceSliding = 0; //滑动距离

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        boolean islocation = false;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                float y = ev.getRawY()-statuActionHeight;
                if (viewLocationbln){
                    if (y>ScreenHeight-actionBarHeight){
                        islocation =  true;
                    }
                }else{
                    if (y>actionBarHeight+marginTop&&y<actionBarHeight*2+marginTop){
                        islocation =  true;
                    }
                }
                break;
        }
        return islocation;
    }


    private boolean childViewTouchReturn = true;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mLastY == -1) {
            mLastY = event.getRawY();
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                distanceSliding = event.getRawY() - mLastY;
//                if (event.getRawY()<buttomY-actionBarHeight){
//                    if (isViewLocation()){
//                        setY(distanceSliding+ViewMarginTop-actionBarHeight);
//                    }else{
//                        if (distanceSliding>=0){
//                            setY(distanceSliding+actionBarHeight);
//                        }
//                    }
//                }
                break;
            default:
//                if (event.getRawY()==mLastY){
                    if (viewLocationbln){
                        slideToHide(false);
                    }else{
                        slideToHide(true);
                    }
//                }else{
//                    if (Math.abs(distanceSliding)>(ScreenHeight-actionBarHeight)/4){ //滑动距离超出三分之一
//                        if (distanceSliding>0){ //从下往上滑 回到最下面
//                            //                            resetMarginTop();
//                            slideToHide(true);
//                        }else{
//                            slideToHide(false);
//                            //                            setMinMarginTop();
//                        }
//                    }else{  //滑动距离没有超出界限
//                        if (distanceSliding>0){//从下往上滑 回到最上面
//                            //                            setMinMarginTop();
//                            slideToHide(false);
//                        }else{
//                            //                            resetMarginTop(); //回到最下面
//                            slideToHide(true);
//                        }
//                    }
//                }
                mLastY = -1; // reset
                break;
        }
        return true;
    }


//    private boolean isDispatchTouchEvent = true;
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return isDispatchTouchEvent;
//    }

    private void updateTopMargin(float dateFloat){
        setViewMargin(this,false,0,0,(int)dateFloat,0);
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
//        params.height = (int) dateFloat;
//        this.setLayoutParams(params);
    }


    public boolean viewLocationbln = true; // 给出View到达的位置状态

    /**
     *
     */
    public void resetMarginTop(){
        setViewMargin(this,false,0,0,ViewMarginTop,0);
        viewLocationbln = false;
    }

    public void setMinMarginTop(){
        setViewMargin(this,false,0,0,actionBarHeight,0);
        viewLocationbln = true;
    }

    /**
     * ViewGroup 现在所处位置  true 顶端  false 底部
     * @return
     */
    private boolean isViewLocation(){
        return viewLocationbln;
    }


    public void setMarginTop(){
        if (isViewLocation()){
            resetMarginTop();
        }else{
            setMinMarginTop();
        }
    }



    /**
     * 使用ValueAnimator将rl_left以动画的形式弹出去
     */
    private void slideToHide(boolean isViewLocation) {

        if (statusLisener!=null){
            statusLisener.status(isViewLocation);
        }

        viewLocationbln = isViewLocation;
        float startY = getY();
        int endLocation = isViewLocation?buttomY-marginTop:topY;
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) startY,endLocation);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int offset = (Integer) animation.getAnimatedValue();
                setY(offset);
            }
        });
//        valueAnimator.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator anim2 = ObjectAnimator.ofObject(this, "bacColor", new ColorEvaluator(),
                isViewLocation?"#ffffff":"#efeff0", isViewLocation?"#efeff0":"#ffffff");
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(valueAnimator).with(anim2);
        float fraction = Math.abs((endLocation + startY) / endLocation);
//        Log.i(TAG, "slideToHide: setDuration="+(long) (30 * fraction));
//        animSet.setDuration((long) (40 * fraction));
        animSet.setDuration(300);
        animSet.start();

//        float fraction = Math.abs((endLocation + startY) / endLocation);
//        Log.i(TAG, "slideToHide: setDuration="+(long) (30 * fraction));
//        valueAnimator.setDuration((long) (30 * fraction));
//        valueAnimator.start();
    }


    public void firstViewLocation(){
        float startY = getY();
        int endLocation = ViewMarginTop/3*2;
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) startY,endLocation);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int offset = (Integer) animation.getAnimatedValue();
                setY(offset);
            }
        });
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(valueAnimator);
        animSet.setDuration(300);
        animSet.start();
    }

    /**
     * 设置某个View的margin
     *
     * @param view   需要设置的view
     * @param isDp   需要设置的数值是否为DP
     * @param left   左边距
     * @param right  右边距
     * @param top    上边距
     * @param bottom 下边距
     * @return
     */
    public LayoutParams setViewMargin(View view, boolean isDp, int left, int right, int top, int bottom) {
        if (view == null) {
            return null;
        }
        int leftPx = left;
        int rightPx = right;
        int topPx = top;
        int bottomPx = bottom;
        LayoutParams params = this.getLayoutParams();
        MarginLayoutParams marginParams = null;
        //获取view的margin设置参数
        if (params instanceof MarginLayoutParams) {
            marginParams = (MarginLayoutParams) params;
        } else {
            //不存在时创建一个新的参数
            marginParams = new MarginLayoutParams(params);
        }

        //根据DP与PX转换计算值
        if (isDp) {
            leftPx = DisplayUtil.dip2px(mContext,left);
            rightPx = DisplayUtil.dip2px(mContext,right);
            topPx = DisplayUtil.dip2px(mContext,top);
            bottomPx = DisplayUtil.dip2px(mContext,bottom);
        }
        //设置margin
        marginParams.setMargins(leftPx, topPx, rightPx, bottomPx);
        view.setLayoutParams(marginParams);
        return marginParams;
    }

    public String getBacColor() {
        return bacColor;
    }

    public void setBacColor(String bacColor) {
        this.bacColor = bacColor;
        bacColorSet(bacColor);
    }

    private void bacColorSet(String color){
//        setBackgroundColor(color);
        CornerDrawable drawable = new CornerDrawable(mContext,ScreenWidth,ViewMarginTop,bacColor);
        setmBackgroundDrawable(drawable);
    }

    private void setmBackgroundDrawable(Drawable drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        }else{
            setBackgroundDrawable(drawable);
        }
    }

    private OpenStatusLisener statusLisener;
    public void setViewOpenStatuLisener(OpenStatusLisener statusLisener){
        this.statusLisener = statusLisener;
    }

    public interface OpenStatusLisener{
        void status(boolean statuBon);
    }


    public void setViewLocationbln(boolean isBon){
        slideToHide(isBon);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

//        Path path = new Path();
//        RectF rectOvalF = new RectF(0,0,100,ScreenWidth);
//        path.addOval(rectOvalF, Path.Direction.CW);
//        canvas.clipPath(path,Region.Op.REPLACE);
//        canvas.clipRect(0,50,ScreenWidth,ScreenHeight-actionBarHeight, Region.Op.DIFFERENCE);
//        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

//    //手指在屏幕上滑动时调用
//    private void touchMove(MotionEvent event)
//    {
//        final float x = event.getX();
//        final float y = event.getY();
//        final float previousX = mX;
//        final float previousY = mY;
//        final float dx = Math.abs(x - previousX);
//        final float dy = Math.abs(y - previousY);
//        //两点之间的距离大于等于3时，生成贝塞尔绘制曲线
//        if (dx >= 3 || dy >= 3)
//        {
//            //设置贝塞尔曲线的操作点为起点和终点的一半
//            float cX = (x + previousX) / 2;
//            float cY = (y + previousY) / 2;
//
//            //二次贝塞尔，实现平滑曲线；previousX, previousY为操作点，cX, cY为终点
//            mPath.quadTo(previousX, previousY, cX, cY);
//
//            //第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值
//            mX = x;
//            mY = y;
//        }
//    }
}
