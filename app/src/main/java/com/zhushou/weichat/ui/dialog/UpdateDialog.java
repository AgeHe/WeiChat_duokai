package com.zhushou.weichat.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.utils.CountUtil;
import com.zhushou.weichat.utils.OkHttpDownloadUtil;

import java.io.File;


/**
 * Created by Administrator on 2016-11-01.
 */

public class UpdateDialog extends Dialog implements View.OnClickListener{
    private static final  String TAG = "UpdateDialog";
    private TextView content, later, download,tv_update_title;
    private RelativeLayout rl_download;
    private ProgressBar pb_progressbar;
    private TextView tv_progressbar_number;
    private LinearLayout ll_update_button;
    private Context mContext;
    private String version;
    private String downloadAddress;
    private boolean isForceUpdate;

    public UpdateDialog(Context context, String version, String downloadAddress,boolean isForceUpdate) {
        super(context);
        mContext = context;
        this.version = version;
        this.downloadAddress = downloadAddress;
        this.isForceUpdate = isForceUpdate;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_updateapk);
        init();
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void init() {
        rl_download = (RelativeLayout) findViewById(R.id.rl_download);
        pb_progressbar = (ProgressBar) findViewById(R.id.pb_progressbar);
        tv_progressbar_number = (TextView) findViewById(R.id.tv_progressbar_number);
        ll_update_button = (LinearLayout) findViewById(R.id.ll_update_button);
        tv_update_title = (TextView) findViewById(R.id.tv_update_title);
        later = (TextView) findViewById(R.id.dialog_later);
        download = (TextView) findViewById(R.id.dialog_download);
        content = (TextView) findViewById(R.id.dialog_content);
        later.setOnClickListener(this);
        download.setOnClickListener(this);
        later.setVisibility(isForceUpdate?View.GONE:View.VISIBLE);
        content.setText(String.format(mContext.getString(R.string.update_app_content),getVersionName(version)));
    }

    private String getVersionName(String versionNumber){
        String versionName = "";
        if (versionNumber==null||versionNumber.equals(""))
            return versionName;
        for (int i=0;i<versionNumber.length();i++){
            char vchar = versionNumber.charAt(i);
            versionName += i==versionNumber.length()-1?String.valueOf(vchar):String.valueOf(vchar+".");
        }
        return versionName;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
    }

    CountUtil countUtil;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_later:
                UpdateDialog.this.dismiss();
                callback.later();
                break;
            case R.id.dialog_download:
                rl_download.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);
                ll_update_button.setVisibility(View.GONE);
                tv_update_title.setText("正在下载");
                countUtil = new CountUtil(mContext);
                File saveFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), countUtil.getApp()+"_"+countUtil.getFrom());
//                UpdateDialog.this.dismiss();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpDownloadUtil.get().download(downloadAddress, countUtil.getApp()+"_"+countUtil.getFrom()+".apk", new OkHttpDownloadUtil.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess(String filePath) {
                                Message mMsg = new Message();
                                mMsg.what = 2;
                                mMsg.obj = filePath.toString();
                                mHandler.sendMessage(mMsg);
                            }

                            @Override
                            public void onDownloading(int progress) {
                                Message mMsg = new Message();
                                mMsg.what = 0;
                                mMsg.obj = progress;
                                mHandler.sendMessage(mMsg);
                            }

                            @Override
                            public void onDownloadFailed() {
                                Message mMsg = new Message();
                                mMsg.what = 1;
                                mHandler.sendMessage(mMsg);

                            }
                        });
                    }
                }).start();

//                MyDownloadManager manager = new MyDownloadManager(mContext);
//                manager.download(downloadAddress, countUtil.getApp()+"_"+countUtil.getFrom(),mContext.getResources().getString(R.string.download_description));
                callback.update();
                break;
        }
    }

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0:
                    int progress = (int) msg.obj;
                    pb_progressbar.setProgress(progress);
                    tv_progressbar_number.setText(new StringBuilder().append(progress).append(" %"));
                    break;
                case 1:
                    tv_progressbar_number.setTextColor(0xffff0018);
                    tv_progressbar_number.setText("下载更新失败！");
                    break;
                case 2:
                    if (countUtil!=null)
                        installApk70(mContext, (String) msg.obj);
                    UpdateDialog.this.dismiss();
                    break;

            }

        }
    };

    private UpdateCallback callback;
    public interface UpdateCallback{
        void update();
        void later();
    }
    public void setCallback(UpdateCallback callback) {
        this.callback = callback;
    }

    /**
     * 是否强制更新
     * @param bon  true 强制 false 不强制
     */
    public void setmCanceledOnTouchOutside(boolean bon){
        this.setCancelable(bon);
    }


    /**
     *
     * @param context
     * @param file
     */
    public void installApk(Context context, File file){

        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setAction(Intent.ACTION_VIEW);
        mIntent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        context.startActivity(mIntent);

    }

    /**
     * 通过隐式意图调用系统安装程序安装APK
     */
    public static void install(Context context, String filePath) {
        File file = new File(filePath);
//        File saveFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), countUtil.getApp()+"_"+countUtil.getFrom());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>=24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, "com.zhushou.weichat", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }else{
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
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
            Uri apkUri =FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }else{
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}
