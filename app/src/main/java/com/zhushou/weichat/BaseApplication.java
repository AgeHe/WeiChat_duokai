package com.zhushou.weichat;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Administrator on 2017/3/9.
 */

public class BaseApplication extends Application {
    static Context _context;
    static Resources _resource;

    @Override
    public void onCreate() {
        super.onCreate();
        _context = getApplicationContext();
        _resource = _context.getResources();
    }

    public static synchronized BaseApplication context() {
        return (BaseApplication) _context;
    }
    public static Resources resource(){
        return _resource;
    }
}
