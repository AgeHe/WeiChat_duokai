package com.zhushou.weichat.utils;

/**
 * Created by Administrator on 2017/9/20.
 */

public interface AuthLoginListener {

    int AUTH_WX = 0x01;

    void onResult(String resultUrl);


    void onCancel(int authType);

}
