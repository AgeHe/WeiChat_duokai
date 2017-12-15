package com.zhushou.weichat.bean;

/**
 * Created by Administrator on 2016-11-03.
 */

public class OkHttpResult {
    public boolean isSuccess;
    public String msg;

    public OkHttpResult(boolean isSuccess, String msg) {
        this.isSuccess = isSuccess;
        this.msg = msg;
    }
}
