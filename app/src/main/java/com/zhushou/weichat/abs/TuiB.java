package com.zhushou.weichat.abs;

import android.content.Context;


/**
 * Created by Administrator on 2017/11/17.
 */

public class TuiB {
    private Context a;
    private static TuiB b;
    private Class c;

    public static synchronized TuiB a(Context var0) {
        if(b == null) {
            b = new TuiB(var0);
        }

        return b;
    }

    private TuiB(Context var1) {
        this.a = var1.getApplicationContext();
    }

    public Class a() {
        return this.c;
    }

    public void a(Class var1) {
        this.c = var1;
    }

    public void TuiB() {
        this.c = null;
    }
}
