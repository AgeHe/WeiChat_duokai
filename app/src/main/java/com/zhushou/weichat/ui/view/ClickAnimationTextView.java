package com.zhushou.weichat.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.zhushou.weichat.R;
import com.zhushou.weichat.utils.WsFriendJsonDataUtil;


/**
 * Created by Administrator on 2017/5/5.
 */

public class ClickAnimationTextView extends android.support.v7.widget.AppCompatTextView {

    private static final String TAG = "ClickAnimationTextView";
    public static final int cancelSelect = 0x01;
    public static final int selected = 0x02;

    private int currentSelect = 0x01;

    public Context context;
    public ClickAnimationTextView(Context context) {
        super(context);
        this.context = context;
        actionDownAnimation();
    }

    public ClickAnimationTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        actionDownAnimation();
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(event);
    }

    AnimationSet set = new AnimationSet(true);
    private void actionDownAnimation(){
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f,1.1f,1f,1.1f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(200);
        set.addAnimation(scaleAnimation);

        ScaleAnimation scaleAnimation2 = new ScaleAnimation(1.1f,1f,1.1f,1f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation2.setDuration(150);
        scaleAnimation2.setStartOffset(150);
        set.addAnimation(scaleAnimation2);

//        ScaleAnimation scaleAnimation3 = new ScaleAnimation(0.9f,1f,0.9f,1f,
//                Animation.RELATIVE_TO_SELF,0.5f,
//                Animation.RELATIVE_TO_SELF,0.5f);
//        scaleAnimation3.setDuration(150);
//        scaleAnimation3.setStartOffset(390);
//        set.addAnimation(scaleAnimation3);
    }


    public int setSelect(int selected){
//        this.currentSelect = selected;
        currentSelect = WsFriendJsonDataUtil.isDianzan(context,selected);
        setTextViewValue(currentSelect==cancelSelect);
//        setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
//        setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(currentSelect==cancelSelect?R.mipmap.ic_friend_dianzan:R.mipmap.ic_friend_dianzan_t),null,null,null);
//        setTextColor(getResources().getColor(currentSelect==cancelSelect?R.color.ws_dianzan:R.color.colorAccent));
        int num = Integer.valueOf(getText().toString());
        int nowSum = currentSelect==cancelSelect?num-1:num+1;
        setText(String.valueOf(nowSum));
        WsFriendJsonDataUtil.msgDZNum(context,nowSum,selected);
        startAnimation(set);
        return nowSum;
    }

    public void setTextViewValue(boolean is){
        setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
        setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(is?R.mipmap.ic_friend_dianzan:R.mipmap.ic_friend_dianzan_t),null,null,null);
        setTextColor(getResources().getColor(is?R.color.ws_dianzan:R.color.colorAccent));
    }


    private void requestDZ(){
        class DZRunable implements Runnable {
            @Override
            public void run() {
//                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
//                OkHttpResult result = okHttpUtil.requestGetBySyn(httpUrl,"user/tweet/list.json", map);
//                Message msg = new Message();
//                msg.obj = result.msg;
            }
        }
        new Thread(new DZRunable()).start();
    }

}
