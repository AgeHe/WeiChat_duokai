package com.lody.virtual;

import android.media.AudioRecord;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.VLog;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/1/18.
 */

public class audioFixer {
    private static String TAG = "audioFixer";
    private static boolean sNativeFixed = false;

    private static native void nativeMark();

    private static native void hookNativeMethod(Object method, String pkg, boolean isArt);
    static {
        try {
            System.loadLibrary("audiohook");
        } catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
        }
    }
    public static void fixAudio() {
        VLog.w(TAG,"jyh fixAudio");
        if (!sNativeFixed) {
            try {
                Method native_check_permission;//
                native_check_permission = Reflect.on(AudioRecord.class).exactMethod("native_check_permission",
                                new Class[]{ String.class});
                if (native_check_permission != null) {
                    native_check_permission.setAccessible(true);
                }
                hookNativeMethod(native_check_permission, VirtualCore.get().getHostPkg(),VirtualRuntime.isArt());
            } catch (NoSuchMethodException e) {
//                e.printStackTrace(); 没有的话 也就不做处理
            }
            sNativeFixed = true;
        }
    }



}
