package com.zhushou.weichat.utils;

import android.util.Log;

import com.zhushou.weichat.VApp;


/**
 * Created by hehongwen on 15/12/28.
 */
public class DebugLog {

	private static final String TAG = "EasyFirewall";

	public static void v(String format, Object... objs) {
		vWithTag(TAG, format, objs);
	}

	public static void vWithTag(String tag, String format, Object... objs) {
		if (VApp.IS_DEBUG) {
			Log.v(tag, format(format, objs));
		}
	}

	public static void i(String format, Object... objs) {
		iWithTag(TAG, format, objs);
	}

	public static void iWithTag(String tag, String format, Object... objs) {
		if (VApp.IS_DEBUG) {
			Log.i(tag, format(format, objs));
		}
	}

	public static void d(String format, Object... objs) {
		dWithTag(TAG, format, objs);
	}

	public static void dWithTag(String tag, String format, Object... objs) {
		if (VApp.IS_DEBUG) {
			Log.d(tag, format(format, objs));
		}
	}

	public static void w(String format, Object... objs) {
		wWithLog(TAG, format, objs);
	}

	public static void wWithLog(String tag, String format, Object... objs) {
		if (VApp.IS_DEBUG) {
			Log.w(tag, format(format, objs));
		}
	}

	public static void e(String format, Object... objs) {
		eWithTag(TAG, format, objs);
	}

	public static void eWithTag(String tag, String format, Object... objs) {
		if (VApp.IS_DEBUG) {
			Log.e(tag, format(format, objs));
		}
	}

	private static String format(String format, Object... objs) {
		if (objs == null || objs.length == 0) {
			return format;
		} else {
			return String.format(format, objs);
		}
	}

}
