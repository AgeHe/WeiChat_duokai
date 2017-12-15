package com.zhushou.weichat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lbsw.stat.LBStat;
import com.qq.e.ads.cfg.MultiProcessFlag;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.umeng.analytics.MobclickAgent;
import com.zhushou.weichat.abs.AdvImpl;
import com.zhushou.weichat.abs.BaseAdvActivity;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryConstans;
import com.zhushou.weichat.auxiliary.sp.SPAuxiliaryPayUtils;
import com.zhushou.weichat.bean.AdvGgsResultInfo;
import com.zhushou.weichat.bean.AdvTypeInfo;
import com.zhushou.weichat.bean.AdverInfo;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.bean.UMengStatisticalKey;
import com.zhushou.weichat.ui.MainActivity;
import com.zhushou.weichat.ui.TuiABrowserActivity;
import com.zhushou.weichat.ui.dialog.ApkNameDialog;
import com.zhushou.weichat.ui.view.LauncherButtomLinearlayout;
import com.zhushou.weichat.utils.DisplayUtil;
import com.zhushou.weichat.utils.MD5;
import com.zhushou.weichat.utils.MyDownloadManager;
import com.zhushou.weichat.utils.OkHttpUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

//import com.oppo.mobad.ad.SplashAd;
//import com.oppo.mobad.listener.ISplashAdListener;

/**
 * Created by Administrator on 2017/5/9.
 */

public class GDTLauncherActivity extends BaseAdvActivity implements BaseAdvActivity.getPhoneStatus
        , SplashADListener {
    private static final String TAG = "GDTLauncherActivity";
    private boolean isAdvClicked = false; // 广告是否被点击
    private SplashAD splashAD;
    private ViewGroup container;
    private TextView skipView;
    private ImageView splashHolder;


    private AdvImpl advimpl;
    private AdvGgsResultInfo advGgsResultInfo; //至尊广告 返回广告信息

    public boolean canJump = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        /**
//         *打开sdk日志开关，便于排查问题；在应用发布正式版本的时候请屏蔽掉这行代码。
//         */
//        MobAdManager.getInstance(getApplicationContext()).enableDebugLog();
        LBStat.start();
        LBStat.active();
        setContentView(R.layout.activity_launcher_gdt);
        if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(this, AuxiliaryConstans.C_QGG)) {
            startActivity(new Intent(GDTLauncherActivity.this, MainActivity.class));
            finish();
            return;
        } else {
            MultiProcessFlag.setMultiProcess(true);
            MobclickAgent.onEvent(GDTLauncherActivity.this, UMengStatisticalKey.adv_send_request);
            initView();
            initData();
        }
    }

    LauncherButtomLinearlayout app_logo;
    TextView tv_versionname;
    ApkNameDialog apkNameDialog;

    private void initView() {

        app_logo = (LauncherButtomLinearlayout) findViewById(R.id.app_logo);
        tv_versionname = (TextView) findViewById(R.id.tv_versionname);

        container = (ViewGroup) this.findViewById(R.id.splash_container);
        skipView = (TextView) findViewById(R.id.skip_view);
        skipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advimpl.cancelCdt();
                jumMainActivity();
            }
        });
        splashHolder = (ImageView) findViewById(R.id.splash_holder);
        splashHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (advGgsResultInfo == null) {
                    selectedAdv(0, null);
                }
                try {
                    if (advGgsResultInfo.getCurl() == null)
                        return;
                    isAdvClicked = true;
                    advimpl.cancelCdt();
                    MobclickAgent.onEvent(GDTLauncherActivity.this, UMengStatisticalKey.adv_click);
                    LBStat.click("adv_launcher_other_click");
                    if (advGgsResultInfo.getClickStatu().equals("dowload")) {

                        if (scanLocalInstallAppList(getPackageManager(), advGgsResultInfo.getPkg_name())) {
                            Intent intent = GDTLauncherActivity.this.getPackageManager().getLaunchIntentForPackage(advGgsResultInfo.getPkg_name());
                            GDTLauncherActivity.this.startActivity(intent);
                        } else {
                            apkNameDialog.alertDialogToast("下载\t\t" + advGgsResultInfo.getTitle().toString() + "\t\t?", new ApkNameDialog.ApkNameCallback() {
                                @Override
                                public void confirmCallback(String apkName) {
                                    MobclickAgent.onEvent(GDTLauncherActivity.this, UMengStatisticalKey.adv_download);
                                    MyDownloadManager manager = new MyDownloadManager(GDTLauncherActivity.this);
                                    manager.download(advGgsResultInfo.getCurl(), advGgsResultInfo.getTitle(), advGgsResultInfo.getTitle() + "下载中...");
                                    Toast.makeText(GDTLauncherActivity.this, "下载中...", Toast.LENGTH_SHORT).show();
                                }
                            }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    jumMainActivity();
                                }
                            }).show();
                        }
                    } else {

                        TuiABrowserActivity.intentTuiABrowserA(GDTLauncherActivity.this, advGgsResultInfo.getCurl());
                    }

//                    Bundle mBundle = new Bundle();
//                    mBundle.putString("url", advGgsResultInfo.getCurl());
//                    mBundle.putString("title_name", advGgsResultInfo.getTitle());
//                    startActivity(new Intent(GDTLauncherActivity.this, WebActivity.class).putExtras(mBundle));
//                    finish();
                } catch (Exception e) {
                    Log.i(TAG, "onClick: " + e.getMessage());
                    return;
                }

            }
        });
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: 点击 点击 点击");
            }
        });
    }

    private void initData() {
        apkNameDialog = new ApkNameDialog(GDTLauncherActivity.this);
        advimpl = new AdvImpl(this);
        // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
        if (Build.VERSION.SDK_INT >= 23) {
            Log.i(TAG, "onCreate: 高版本需要检查权限" + "");
            checkAndRequestPermission();
        } else {
            new Thread(new RequestBoxAdv()).start();
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        PackageInfo packageInfo = null;
        try {
            packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tv_versionname.setText(String.format(getResources().getString(R.string.launcher_versionname), packageInfo != null ? packageInfo.versionName : "1.2.8"));

        if (isAdvClicked) {
            advimpl.cancelCdt();
            jumMainActivity();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = isOppo;
        if (isAdvClicked)
            advimpl.cancelCdt();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            next();
        }
        canJump = true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
//            fetchSplashAD(this, container, skipView, Constants.APPID, Constants.SplashPosID, this, 0);
            new Thread(new RequestBoxAdv()).start();
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, getResources().getString(R.string.lack_of_permissions), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onADDismissed() {
        next();
    }

    @Override
    public void onNoAD(AdError adError) {
        Log.i("AD_DEMO", "LoadSplashADFail, eCode=" + adError.getErrorCode());
        /** 如果加载广告失败，则直接跳转
         * 广点通偶尔会返回501 文档解释：无广告返回，建议重新请求

         * */
//        this.startActivity(new Intent(this, MainActivity.class));
//        this.finish();
        if (adError.getErrorCode() == 501 && advLenght < 10) {
            advLenght += 1;
            Log.i(TAG, "onNoAD: ++++++++++++++" + (advLenght += 1));
            selectedAdv(2, advTypeInfo);
        } else {
            selectedAdv(0, advTypeInfo);
        }
    }

    int advLenght = 0;


    @Override
    public void onADPresent() {
        Log.i("AD_DEMO", "SplashADPresent");
        splashHolder.setVisibility(View.INVISIBLE); // 广告展示后一定要把预设的开屏图片隐藏起来
    }

    @Override
    public void onADClicked() {
        Log.e("AD_DEMO", "SplashADClicked");
        isAdvClicked = true;
        MobclickAgent.onEvent(GDTLauncherActivity.this, UMengStatisticalKey.adv_click);
        LBStat.click("adv_launcher_click");

    }

    /**
     * 倒计时回调，返回广告还将被展示的剩余时间。
     * 通过这个接口，开发者可以自行决定是否显示倒计时提示，或者还剩几秒的时候显示倒计时
     *
     * @param l 剩余毫秒数
     */
    @Override
    public void onADTick(long l) {
        Log.i("AD_DEMO", "SplashADTick " + l + "ms");
        skipView.setText(String.format(getResources().getString(R.string.click_to_skip_time), Math.round(l / 1000f)));
    }

    /**
     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
     * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
     */
    private void next() {
        Log.i(TAG, "next: next广告跳转");
        if (canJump) {
            this.startActivity(new Intent(this, MainActivity.class));
            this.finish();
        } else {
            canJump = true;
        }
    }

    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            Log.i(TAG, "onCreate: 所有权限已授权" + "");
            new Thread(new RequestBoxAdv()).start();
//            fetchSplashAD(this, container, skipView, Constants.APPID, Constants.SplashPosID, this, 0);
//            getAdvparamter(this);
        } else {
            Log.i(TAG, "onCreate: 缺少权限需要申请" + "");
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity      展示广告的activity
     * @param adContainer   展示广告的大容器
     * @param skipContainer 自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
     * @param appId         应用ID
     * @param posId         广告位ID
     * @param adListener    广告状态监听器
     * @param fetchDelay    拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     */
    private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                               String appId, String posId, SplashADListener adListener, int fetchDelay) {
        splashAD = new SplashAD(activity, adContainer, skipContainer, appId, posId, adListener, fetchDelay);
    }


    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    String parameterKeys64 = "";

    @Override
    public void httpReturn() {
        String toMD5Str = MD5.getMessageDigest(kpKey.getBytes()) + systemTimes + kpSaid;
        encryption = MD5.getMessageDigest(toMD5Str.getBytes());
        JSONObject advJo = new JSONObject();
        try {
            advJo.put("key", kpKey);
            advJo.put("said", kpSaid);
            advJo.put("time", systemTimes);
            advJo.put("encryption", encryption);
            advJo.put("imei", system_imei);
            advJo.put("scale", scale);
            advJo.put("device_type", device_type);
            advJo.put("os", system_os);
            advJo.put("screen_size", screen_size);
            advJo.put("version", system_version);
            advJo.put("phone_model", phone_model);
            advJo.put("network", netWork);
            advJo.put("operator", operator);
            advJo.put("ip", ip);
        } catch (Exception e) {

        }
        parameterKeys64 = Base64.encodeToString(advJo.toString().getBytes(), Base64.DEFAULT);
        new Thread(new RequestGgsAdv()).start();
    }


    /**
     * 至尊传媒广告返回
     *
     * @param ggsResultInfo
     */
    @Override
    public void ggsresultInfo(AdvGgsResultInfo ggsResultInfo) {
        advSuccess = true;
//        splashHolder;
        this.advGgsResultInfo = ggsResultInfo;
        int screenWidth = DisplayUtil.getScreenWidth(this);
        int advHeight = (int) (screenWidth * 1.375f);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, advHeight);
        splashHolder.setLayoutParams(params);
        Picasso.with(this).load(ggsResultInfo.getImg_src()).into(mTarget);
//        advimpl.StartLodingAdvOvertime();
    }

    private Target mTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            isLodingAdvS = true;
            splashHolder.setVisibility(View.GONE);
            splashHolder.setImageBitmap(bitmap);
            AlphaAnimation scaleAnimation = new AlphaAnimation(0, 1);
            scaleAnimation.setDuration(300);
            splashHolder.setVisibility(View.VISIBLE);
            splashHolder.startAnimation(scaleAnimation);
            advimpl.startDownTimer();
            skipView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private boolean isLodingAdvS = false;

    public void sleepIntent() {
        if (!isLodingAdvS) {
            advimpl.cancelCdt();
            jumMainActivity();
        }

    }

//    /**
//     * OPPO 广告SDK初始化完成回调
//     * @param b
//     */
//    @Override
//    public void onInit(boolean b) {
//
//    }


//    /**
//     * OPPO 广告加载回调
//     */
//    @Override
//    public void onAdShow() {
//        Log.i(TAG, "onAdShow: oppo 广告曝光回调//");
//    }
//
//    @Override
//    public void onAdFailed(String s) {
//        Log.i(TAG, "onAdFailed: oppo 广告加载失败//" + s);
//        advimpl.cancelCdt();
//        selectedAdv(0, advTypeInfo);
//    }
//
//    @Override
//    public void onAdReady() {
//        Log.i(TAG, "onAdReady: oppo 广告加载成功");
//    }
//
//    @Override
//    public void onAdClick() {
//        Log.e(TAG, "onAdClick: oppo 广告点击");
////        advimpl.cancelCdt();
//        canJump = true;
//        isAdvClicked = true;
//        MobclickAgent.onEvent(GDTLauncherActivity.this, UMengStatisticalKey.adv_click);
//        LBStat.click("adv_launcher_click");
//    }
//
//    @Override
//    public void onVerify(int i, String s) {
//        Log.i(TAG, "onVerify: oppo 广告加载状态回调 i=" + i + "&&s=" + s);
//    }
//
//    @Override
//    public void onAdDismissed() {
//        Log.i(TAG, "onAdDismissed: oppo 广告被关闭");
////        next();
//    }

    /**
     * OPPO 广告加载回调 end
     */

    private class RequestGgsAdv implements Runnable {
        @Override
        public void run() {
            //处理网络请求
            OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("keys", parameterKeys64);
            OkHttpResult result = okHttpUtil.requestPostBySyn(getADVUrl, "api", hashMap);
            Message msg = new Message();
            msg.obj = result.msg;
            msg.what = 1;
            myHttpHandler.sendMessage(msg);
        }
    }


    //// TODO: 2017/11/8 屏蔽启动页广告
    private class RequestBoxAdv implements Runnable {
        @Override
        public void run() {
            //处理网络请求
            OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
            OkHttpResult result = okHttpUtil.requestGetBySyn(VApp.ADV_TYPE, null, null);
            Message msg = new Message();
            msg.obj = result.msg;
            msg.what = 0;
            advHandler.sendMessage(msg);
        }
    }

    private boolean isOppo = false;
    AdvTypeInfo advTypeInfo = new AdvTypeInfo();
    private boolean advSuccess = false;
    private Handler advHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    advTypeInfo = boxAdvConfigure(msg.obj.toString());
                    //TODO 应用市场广告屏蔽
//                    if (advTypeInfo!=null)
//                        advTypeInfo.setAdv_type("xxx");
                    if (advTypeInfo == null || !advTypeInfo.isAdv_status()) {
                        splashHolder.setVisibility(View.GONE);
                        skipView.setVisibility(View.GONE);
                        selectedAdv(0, advTypeInfo);
                        return;
                    }
                    if (advTypeInfo != null && advTypeInfo.getAdv_type() != null && !advTypeInfo.getAdv_type().equals("")) {
                        if (advTypeInfo.getAdv_type().equals("adv_gdt")) {
                            selectedAdv(2, advTypeInfo);
                        } else if (advTypeInfo.getAdv_type().equals("adv_ggs")) {
                            selectedAdv(1, advTypeInfo);
                        } else if (advTypeInfo.getAdv_type().equals("adv_oppo")) {
                            isOppo = true;
                            selectedAdv(3, advTypeInfo);
                        } else if (advTypeInfo.getAdv_type().equals("adv_self")) {
                            selectedAdv(4, advTypeInfo);
                        } else if (advTypeInfo.getAdv_type().equals("adv_random")) {
                            int randomInt = new Random().nextInt(2);
                            if (randomInt == 0) {
                                selectedAdv(2, advTypeInfo);
                            } else {
                                selectedAdv(4, advTypeInfo);
                            }
                        } else {
                            selectedAdv(0, advTypeInfo);
                        }
                    } else {
                        selectedAdv(0, advTypeInfo);
                    }
                    break;
                case 1:
                    if (!advSuccess)
                        jumMainActivity();
                    break;
            }
        }
    };


    public class SleepRunnable implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            advHandler.sendEmptyMessage(1);
        }
    }

    public void selectedAdv(int type_int, AdvTypeInfo advTypeInfo) {

        // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
//        if (Build.VERSION.SDK_INT >= 23) {
//            Log.i(TAG, "onCreate: 高版本需要检查权限" + "");
//            checkAndRequestPermission();
//        }


        switch (type_int) {
            case 0:
                MobclickAgent.onEvent(GDTLauncherActivity.this, UMengStatisticalKey.adv_open_failed);
                new Thread(new SleepRunnable()).start();
                break;
            case 1:
                //广告商
                getAdvparamter(GDTLauncherActivity.this);
                container.setVisibility(View.GONE);
                break;
            case 2:

//                advSuccess = true;
                // 如果是Android6.0以下的机器，默认在安装时获得了所有权限，可以直接调用SDK
                int screenWidth = DisplayUtil.getScreenWidth(this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenWidth / 8 * 11);
                container.setLayoutParams(params);
                //广点通
                fetchSplashAD(GDTLauncherActivity.this,
                        container,
                        skipView,
                        advTypeInfo.getAppid(),
                        advTypeInfo.getPosid(),
                        GDTLauncherActivity.this,
                        0);
                splashHolder.setVisibility(View.GONE);
                skipView.setVisibility(View.VISIBLE);
                break;
            case 3:
                advSuccess = true;
//                int screenWidth2 = DisplayUtil.getScreenWidth(this);
//                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenWidth2 / 8 * 11);
//                container.setLayoutParams(params2);
//                //oppo广告
//                oppoSplashAd(container,
//                        "816",
//                        GDTLauncherActivity.this);
////                advimpl.startDownTimer();
//                splashHolder.setVisibility(View.GONE);
//                skipView.setVisibility(View.VISIBLE);
                MobclickAgent.onEvent(GDTLauncherActivity.this, UMengStatisticalKey.adv_open_failed);
                new Thread(new SleepRunnable()).start();
                break;
            case 4:
                List<AdverInfo> adverInfos = advTypeInfo.getMy_self_adver();
                int randomInt = 0;
                if (adverInfos.size() > 1) {
                    Random random = new Random();
                    randomInt = random.nextInt(adverInfos.size() - 1);
                }
                AdverInfo adverInfo = adverInfos.get(randomInt);
                AdvGgsResultInfo ggsResultInfo = new AdvGgsResultInfo();
                ggsResultInfo.setTitle(adverInfo.getTitlename());
                ggsResultInfo.setCurl(adverInfo.getAppAddress());
                ggsResultInfo.setImg_src(adverInfo.getIconAddress());
                ggsResultInfo.setClickStatu(adverInfo.getClickStatu());
                ggsResultInfo.setPkg_name(adverInfo.getPkg_name());
                ggsresultInfo(ggsResultInfo);
                container.setVisibility(View.GONE);
                break;
        }
        app_logo.startVisibility();
    }

    /**
     * 跳转主界面
     */
    public void jumMainActivity() {
        startActivity(new Intent(GDTLauncherActivity.this, MainActivity.class));
        finish();
    }


    public boolean scanLocalInstallAppList(PackageManager packageManager, String packagename) {
        boolean isIntall = false;
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
//                过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    continue;
                }
                if (packageInfo.packageName.equals(packagename)) {
                    isIntall = true;
                    break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isIntall;
    }

}
