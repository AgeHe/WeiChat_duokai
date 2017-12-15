package com.zhushou.weichat.auxiliary.interfac;


import com.zhushou.weichat.auxiliary.ui.view.RiseNumberTextView;

/**
 * Created by Administrator on 2016/11/3.
 */
public interface IRiseNumber {


    /**
     * 开始播放动画的方法
     */
    public void start();

    /**
     * 设置小数
     *
     * @param number
     * @return
     */
    public void withNumber(float number);

    /**
     * 设置整数
     *
     * @param number
     * @return
     */
    public void withNumber(int number);

    /**
     * 设置动画播放时长
     *
     * @param duration
     * @return
     */
    public void setDuration(long duration);

    /**
     * 设置动画结束监听器
     *
     * @param callback
     */
    public void setOnEndListener(RiseNumberTextView.EndListener callback);

}
