package com.zhushou.weichat.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.zhushou.weichat.utils.DisplayUtil;

/**
 * Created by Administrator on 2017/5/9.
 */

public class LauncherButtomLinearlayout extends LinearLayout {

    private int translateSize = 0;

    public LauncherButtomLinearlayout(Context context) {
        super(context);
        initData(context);
    }

    public LauncherButtomLinearlayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    AnimationSet set = new AnimationSet(true);
    private void initData(Context context){
        translateSize = DisplayUtil.dip2px(context,15);
        //透明度
        AlphaAnimation alphAnimation = new AlphaAnimation(0, 1);
        alphAnimation.setDuration(1300);//设置动画持续时间
        set.addAnimation(alphAnimation);

        //平移动画
        TranslateAnimation animation = new TranslateAnimation(0, 0,translateSize, 0);
        animation.setDuration(1000);//设置动画持续时间
        set.addAnimation(animation);
        //设置动画执行前延时时间
        set.setStartOffset(100);
    }

    public void startVisibility(){
        setVisibility(VISIBLE);
        startAnimation(set);
    }
}
