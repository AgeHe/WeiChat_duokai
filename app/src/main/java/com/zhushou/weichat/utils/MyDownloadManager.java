package com.zhushou.weichat.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by Administrator on 2016-11-03.
 */

public class MyDownloadManager {
    private DownloadManager dm;
    private Context mContext;
    private long downloadId;
    private String updateApkName="";
    public MyDownloadManager(Context context) {
        mContext = context;
        dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }
    //开始下载
    private void start(String uri, String title, String descriprion) {
        updateApkName = title+".apk";
        if (canDownloadState()) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));
            request.setTitle(title);//设置通知栏标题
            request.setDescription(descriprion);//设置通知栏的提示介绍
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);//设置下载完毕后显示在通知栏
            //目录: Android -> data -> com.app -> files -> Download -> xxx.apk
            //这个文件是你的应用所专用的,软件卸载后，下载的文件将随着卸载全部被删除
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
            request.setDestinationInExternalPublicDir(  Environment.DIRECTORY_DOWNLOADS  ,updateApkName ) ;
            downloadId = dm.enqueue(request);
            mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } else {
            String packageName = "com.android.providers.downloads";
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            mContext.startActivity(intent);
        }
    }
    public void download(String url, String title, String description) {
//        UserHelper helper = new UserHelper(mContext);
//       Long saveId = helper.getDownloadId();
//        if (saveId == 0) {//没有 去下载
            start(url, title, description);
//        } else {//有下载过的id了
//            int status = getDownloadStatus(saveId);
//            if (status == DownloadManager.STATUS_SUCCESSFUL) {//下载成功了
//                Uri uri = getDownloadUri(saveId);
//                if (uri != null) {//如果文件还保存着
//                    if (compare(getApkInfo(mContext, uri.getPath()), mContext)) {//如果版本小
//                        installApk(mContext, downloadId);//安装
//                    } else {//版本不小就不用安装
//                    }
//                } else {//如果文件没保存
//                    start(url, title, description);
//                }
//            } else if (status == DownloadManager.STATUS_FAILED) {
//                start(url, title, description);
//            }
//        }
    }
    //获取下载的状态
    public int getDownloadStatus(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = dm.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                }
            } finally {
                c.close();
            }
        }
        return -1;
    }
    /**
     * 获取apk程序信息[packageName,versionName...]
     *
     * @param context Context
     * @param path    apk path
     */
    private static PackageInfo getApkInfo(Context context, String path) {
//        if (Config.DEV_MODE) {
//            Log.d(TAG, "apk download path: " + path);
//        }
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            //String packageName = info.packageName;
            //String version = info.versionName;
            //Log.d(TAG, "packageName:" + packageName + ";version:" + version);
            //String appName = pm.getApplicationLabel(appInfo).toString();
            //Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息
            return info;
        }
        return null;
    }
    /**
     * 下载的apk和当前程序版本比较
     *
     * @param apkInfo apk file's packageInfo
     * @param context Context
     * @return 如果当前应用版本小于apk的版本则返回true
     */
    private boolean compare(PackageInfo apkInfo, Context context) {
        if (apkInfo == null) {
            return false;
        }
        String localPackage = context.getPackageName();
        if (apkInfo.packageName.equals(localPackage)) {
            try {
                PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(localPackage, 0);
                if (apkInfo.versionCode > packageInfo.versionCode) {
                    return true;
                } else {
//                    if (Config.DEV_MODE) {
//                        Log.d(TAG, "apk's versionCode <= app's versionCode");
//                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
//        if (Config.DEV_MODE) {
//            Log.d(TAG, "apk's package not match app's package");
//        }
        return false;
    }
    //判断是否能下载
    private boolean canDownloadState() {
        try {
            int state = mContext.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //安装apk
    private void installApk(Context context, long downloadApkId) {
//        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri apkUri = downloadManager.getUriForDownloadedFile(downloadApkId);
        String urlStr = Environment.getExternalStorageDirectory().getPath()+"/"+ Environment.DIRECTORY_DOWNLOADS+"/"+updateApkName;
        if (urlStr != null) {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.parse("file://"+urlStr), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }
    }


    /**
     * 通过隐式意图调用系统安装程序安装APK
     * 兼容7.0
     */
    public static void installApk70(Context context,String filePath) {
        File file = new File(filePath);
//        File file = new File(
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                , "myApp.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>=24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }else{
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
    //获取保存的文件apk路径
    public Uri getDownloadUri(long downloadId) {
        return dm.getUriForDownloadedFile(downloadId);
    }

    //下载完成的广播
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if (id ==downloadId){
//                    installApk(mContext, downloadId);
                    String urlStr = Environment.getExternalStorageDirectory().getPath()+"/"+ Environment.DIRECTORY_DOWNLOADS+"/"+updateApkName;
                    installApk70(mContext,urlStr);
                    mContext.unregisterReceiver(this);
                }
            }
        }
    };

}
