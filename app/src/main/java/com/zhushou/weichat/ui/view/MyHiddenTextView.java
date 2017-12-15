package com.zhushou.weichat.ui.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/3.
 */

public class MyHiddenTextView extends TextView {

    private String bacColor = "";
    private TextViewChangeColorLisener changeColorLisener = null;
    private Context context;
    private boolean changeTextView = false;

    public MyHiddenTextView(Context context) {
        super(context);
        init(context);
    }

    public MyHiddenTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyHiddenTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        this.changeColorLisener = new TextViewChangeColorLisener();
    }


    private void ChangeColorAnimator(boolean changeBon){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(changeBon ? 100 : 0, changeBon ? 0 : 100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float offset = (Integer) animation.getAnimatedValue();
//                getBackground().setAlpha(offset);
                setAlpha(offset/100);
            }
        });
        ObjectAnimator anim2 = ObjectAnimator.ofObject(this, "bacColor", new ColorEvaluator(),
                changeBon?"#efeff0":"#ffffff", changeBon?"#ffffff":"#efeff0");
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim2).with(valueAnimator);
        animSet.setDuration(300);
        anim2.addListener(changeColorLisener);
        animSet.start();
        setVisibility(VISIBLE);

    }

    /**
     * true 显示 false 隐藏
     * @param changeBon
     */
    public void setChangeStatu(boolean changeBon){
        this.changeTextView = changeBon;
        ChangeColorAnimator(changeBon);
    }

    public String getBacColor() {
        return bacColor;
    }

    public void setBacColor(String bacColor) {
        this.bacColor = bacColor;
        bacColorSet(Color.parseColor(bacColor));
    }

    private void bacColorSet(int color){
        setBackgroundColor(color);
    }

    class TextViewChangeColorLisener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {

        }
        @Override
        public void onAnimationEnd(Animator animation) {
            if (!changeTextView){ //显示
                setVisibility(VISIBLE);
            }else{// 隐藏
                setVisibility(GONE);
            }
        }
        @Override
        public void onAnimationCancel(Animator animation) {
        }
        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
