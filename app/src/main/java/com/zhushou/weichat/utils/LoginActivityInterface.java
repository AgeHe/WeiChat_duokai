package com.zhushou.weichat.utils;

/**
 * Created by Administrator on 2017/9/20.
 */

public class LoginActivityInterface {

    public interface ViewImpl{
        void onResultUserInfo(boolean successful);
    }

    public interface ViewControllerImpl {
        void getWxLoginInfo(String code);
        void start();
        void rafreshLoginmsg(String openId);
        void checkVersionUpdate();
    }
}
