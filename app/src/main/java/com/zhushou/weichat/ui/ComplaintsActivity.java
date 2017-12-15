package com.zhushou.weichat.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zhushou.weichat.R;
import com.zhushou.weichat.auxiliary.base.BaseActivity;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.utils.MyToast;
import com.zhushou.weichat.utils.OkHttpUtil;
import com.zhushou.weichat.utils.SystemUtil;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/9/25.
 */

public class ComplaintsActivity extends BaseActivity {

    public static final String ComplaintsHost = "http://api.duokai.mxitie.com/cgi/api.ashx";
    private  String[] kfqqArray,kfdhArray,kfwxArray;
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setContentView(R.layout.activity_complaints);
    }

    @Override
    public void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.rl_complaints_type1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ComplaintsActivity.this,ComplaintsOrderNumberActivity.class));
            }
        });

        findViewById(R.id.rl_complaints_type2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ComplaintsActivity.this,ComplaintsFenShenActivity.class));
            }
        });
        findViewById(R.id.rl_complaints_type3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ComplaintsActivity.this,ComplaintsProblemActivity.class));
            }
        });
        findViewById(R.id.tv_to_complaints_historyarray).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,ComplaintsHistoricalRecordActivity.class));
            }
        });

        findViewById(R.id.rl_complaints_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kfqqArray == null)
                    return;
                if (kfqqArray.length > 1) {
                    Random random = new Random();
                    int randomInt = random.nextInt(kfqqArray.length-1);
                    Log.i("hhh", "findView: randomInt"+randomInt);
                    try {
                        String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + kfqqArray[randomInt] + "&version=1";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                    }catch (Exception e){
                        MyToast.mCenterToast("打开QQ异常或未安装QQ",mContext);
                    }
                } else {
                    try {
                        String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + kfqqArray[0] + "&version=1";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                    }catch (Exception e){
                        MyToast.mCenterToast("打开QQ异常或未安装QQ",mContext);
                    }
                }
            }
        });

        findViewById(R.id.rl_complaints_phong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kfdhArray == null)
                    return;
                if (kfdhArray.length > 1) {
                    Random random = new Random();
                    int randomInt = random.nextInt(kfdhArray.length);
                    Intent dialIntent =  new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + kfdhArray[randomInt]));//跳转到拨号界面，同时传递电话号码
                    startActivity(dialIntent);
                }else{
                    Intent dialIntent =  new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + kfdhArray[0]));//跳转到拨号界面，同时传递电话号码
                    startActivity(dialIntent);
                }
            }
        });

        findViewById(R.id.rl_complaints_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kfwxArray == null)
                return;
            if (kfwxArray.length > 1) {
                Random random = new Random();
                int randomInt = random.nextInt(kfwxArray.length-1);
                Log.i("hhh", "findView: randomInt"+randomInt);
                setClipboard(kfwxArray[randomInt]);
            } else {
                setClipboard(kfwxArray[0]);
            }
            }
        });
    }

    private void setClipboard(String content){
        if (android.os.Build.VERSION.SDK_INT > 11) {
            android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) mContext.getSystemService(mContext.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(ClipData.newPlainText("weichatNumber", content));
        } else {
            ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            // 将文本内容放到系统剪贴板里。
            cm.setText(content);
        }
        Toast.makeText(mContext, "复制成功!", Toast.LENGTH_SHORT).show();
    }

    public static PackageInfo packageInfo = null;
    public static String oldWXVersionName = "";
    @Override
    public void initData() {

        UpdatePriceInfo info = APPUsersShareUtil.getUpdateMessage(this);
        kfqqArray = info.getKfqq() == null ? null : info.getKfqq().split(",");
        kfdhArray = info.getKfdh() == null ? null : info.getKfdh().split(",");
        kfwxArray = info.getKfwx() == null ? null : info.getKfwx().split(",");
        try {
            packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        oldWXVersionName = getWXVersionName(mContext);
    }


    public static void sendComplaintsMessage(HashMap<String, String> hashMap, int handlerwhat, Handler mHandler){
        String phoneInfo = new StringBuilder().append("手机厂商:").append(SystemUtil.getDeviceBrand())
                .append("手机型号:").append(SystemUtil.getSystemModel())
                .append("系统版本:").append(SystemUtil.getSystemVersion())
                .append("主微信版本").append(oldWXVersionName)
                .append("多开版本:").append(packageInfo==null?"":packageInfo.versionName).toString();
        hashMap.put("info",phoneInfo);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpResult okHttpResult = OkHttpUtil.getInstance().requestGetBySyn(ComplaintsHost,"DB_user_addcomplain",hashMap);
                Message msg = new Message();
                msg.what = handlerwhat;
                msg.obj = okHttpResult;
                mHandler.sendMessage(msg);
            }
        }).start();
    }
    public static void sendComplaintsMessage(HashMap<String, String> hashMap,String actionUrl, int handlerwhat, Handler mHandler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpResult okHttpResult = OkHttpUtil.getInstance().requestGetBySyn(ComplaintsHost,actionUrl,hashMap);
                Message msg = new Message();
                msg.what = handlerwhat;
                msg.obj = okHttpResult;
                mHandler.sendMessage(msg);
            }
        }).start();
    }


    /**
     * 获取当前主微信版本号 VersionName
     * @param context
     * @return
     */
    public String getWXVersionName(Context context){

        String versionName = "";
        PackageManager pckMan = context.getPackageManager();
        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
        List<PackageInfo> packageInfo = pckMan.getInstalledPackages(0);
        for (PackageInfo pInfo : packageInfo) {
            if (pInfo.packageName.equals("com.tencent.mm")){
                versionName = pInfo.versionName;
                break;
            }
//                HashMap<String, Object> item = new HashMap<String, Object>();
//                item.put("appimage", pInfo.applicationInfo.loadIcon(pckMan));
//                item.put("packageName", pInfo.packageName);
//                item.put("versionCode", pInfo.versionCode);
//                item.put("versionName", pInfo.versionName);
//                item.put("appName", pInfo.applicationInfo.loadLabel(pckMan).toString());
        }
        return versionName;
    }

}
