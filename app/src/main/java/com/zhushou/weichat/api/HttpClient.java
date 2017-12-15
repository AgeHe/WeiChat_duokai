package com.zhushou.weichat.api;

import android.os.Handler;
import android.os.Message;

import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.utils.OkHttpUtil;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/13.
 * Created by Administrator on 2017/10/26.
 */

public class HttpClient {

    public static void post(final String host, final String url, final HashMap<String, String> map, final int handlerWhat, final Handler mHandler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                OkHttpResult result = okHttpUtil.requestPostBySyn(host, url, map);
                Message msg = new Message();
                msg.obj = result;
                msg.what = handlerWhat;
                if (mHandler!=null)
                    mHandler.sendMessage(msg);
            }
        }).start();
    }

    public static void get(final String host, final String url, final HashMap<String, String> map, final int handlerWhat, final Handler mHandler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                OkHttpResult result = okHttpUtil.requestGetBySyn(host, url, map);
                Message msg = new Message();
                msg.obj = result;
                msg.what = handlerWhat;
                if (mHandler!=null)
                    mHandler.sendMessage(msg);
            }
        }).start();
    }


}
