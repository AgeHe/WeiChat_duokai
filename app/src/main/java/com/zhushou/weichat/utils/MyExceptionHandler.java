package com.zhushou.weichat.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/12.
 */

public class MyExceptionHandler implements UncaughtExceptionHandler {
    // 上下文
    private Context mContext;
    // 是否打开上传
    public boolean openUpload = true;
    // Log文件路径
    private static final String LOG_FILE_DIR = "log";
    // log文件的后缀名
    private static final String FILE_NAME = ".log";
    private static MyExceptionHandler instance = null;
    // 系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    public MyExceptionHandler(Context cxt) {
        // 获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        // 获取Context，方便内部使用
        this.mContext = cxt.getApplicationContext();
    }

    public synchronized static MyExceptionHandler create(Context cxt) {
        if (instance == null) {
            instance = new MyExceptionHandler(cxt);
        }
        return instance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        try {
            // 保存导出异常日志信息到SD卡中
            saveToSDCard(throwable);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
            Toast.makeText(mContext,
                    "很抱歉:\r\n" + throwable.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
            if (mDefaultCrashHandler != null) {
                mDefaultCrashHandler.uncaughtException(thread, throwable);
            } else {
                throwable.printStackTrace();
            }
        }
    }

    /**
     * 保存文件到SD卡
     *
     * @description：
     * @author ldm
     * @date 2016-4-18 上午11:37:17
     */
    private void saveToSDCard(Throwable ex) throws Exception {
//        File file = new File(FileUtil.getRootPath()
//                +mContext.getPackageName()
//                + File.separator + LOG_FILE_DIR
//                +getDataTime("yyyy-MM-dd-HH-mm-ss")
//                + FILE_NAME);
//        PrintWriter pw = new PrintWriter(new BufferedWriter(
//                new FileWriter(file)));
//        // 导出发生异常的时间
//        pw.println(getDataTime("yyyy-MM-dd-HH-mm-ss"));
//        // 导出手机信息
//        savePhoneInfo(pw);
//        pw.println();
//        // 导出异常的调用栈信息
//        ex.printStackTrace(pw);
//        pw.close();
        saveCrashInfoFile(ex);
    }

    /**
     * 保存手机硬件信息
     *
     * @description：
     * @author ldm
     * @date 2016-4-18 上午11:38:01
     */
    private void savePhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        // 应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
                PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
        pw.println();

        // android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        pw.println();

        // 手机制造商
        pw.print("Manufacturer: ");
        pw.println(Build.MANUFACTURER);
        pw.println();

        // 手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        pw.println();

        infos.put("Manufacturer:",Build.MODEL);
        infos.put("App Version:",pi.versionName);
    }

    /**
     * 根据时间格式返回时间
     *
     * @description：
     * @author ldm
     * @date 2016-4-18 上午11:39:30
     */
    private String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();
    /**
     * 保存错误信息到文件中
     * @param ex
     * @return 返回文件名称,便于将文件传送到服务器
     * @throws Exception
     */
    private String saveCrashInfoFile(Throwable ex) throws Exception {
        StringBuffer sb = new StringBuffer();
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            sb.append("\r\n" + date + "\n");
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

            printWriter.flush();
            printWriter.close();
            String result = writer.toString();
            sb.append(result);

            String fileName = writeFile(sb.toString());
            return fileName;
        } catch (Exception e) {
            Log.e("hh", "an error occured while writing file...", e);
            sb.append("an error occured while writing file...\r\n");
//            writeFile(sb.toString());
        }
        return null;
    }

    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private String writeFile(String sb) throws Exception {
        String time = formatter.format(new Date());
        String fileName = "crash-" + time + ".log";
//        if (FileUtil.hasSdcard()) {
            String path = getGlobalpath();
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            FileOutputStream fos = new FileOutputStream(path + fileName, true);
            fos.write(sb.getBytes());
            fos.flush();
            fos.close();
//        }
        return fileName;
    }

    public static String getGlobalpath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "crash" + File.separator;
    }

}
