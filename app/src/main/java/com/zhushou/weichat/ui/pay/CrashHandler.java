package com.zhushou.weichat.ui.pay;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.zhushou.weichat.ui.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";
	private static CrashHandler INSTANCE = new CrashHandler();
	private UncaughtExceptionHandler mDefaultHandler;

	private Map<String, String> infos = new HashMap<String, String>();

	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	private Context mContext;

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {  
            // 如果用户没有处理则让系统默认的异常处理器来处理  
            mDefaultHandler.uncaughtException(thread, ex);  
        } else {  
            try {  
                Thread.sleep(3000);  
            } catch (InterruptedException e) {  
                Log.e(TAG, "error : ", e);  
            }  
  
            // 退出程序,注释下面的重启启动程序代码
            android.os.Process.killProcess(android.os.Process.myPid());  
            System.exit(1);
            Intent intent = new Intent();
            intent.setClass(mContext,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
	}

	private void CrashHandler() {

	}

	public static CrashHandler getInstance() {
		if(INSTANCE==null)
		{
			INSTANCE = new CrashHandler();
		}
		return INSTANCE;
	}

	public void init(Context context) {
		this.mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}

		// 使用 Toast 来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出。", Toast.LENGTH_LONG)
						.show();
				Looper.loop();
			}
		}.start();

		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		saveCrashInfo2File(ex);
		return true;
	}

	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);

			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}

		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	private String saveCrashInfo2File(Throwable ex) {
		ex.printStackTrace();
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();

		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".log";

			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String path = "/sdcard/crash/";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
			}

			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}

		return null;
	}

}
