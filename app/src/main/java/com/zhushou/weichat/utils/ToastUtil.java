package com.zhushou.weichat.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/9/20.
 */

public class ToastUtil {

    public static void centerToast(Context context,String msg){
        Toast toast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void centerLongToast(Context context,String msg){
        Toast toast = Toast.makeText(context,msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
