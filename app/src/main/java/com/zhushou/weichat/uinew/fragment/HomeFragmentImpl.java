package com.zhushou.weichat.uinew.fragment;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.api.OpenRedRequest;
import com.zhushou.weichat.api.ZhuShouApi;
import com.zhushou.weichat.auxiliary.service.ScreenListener;
import com.zhushou.weichat.bean.ApkItemInfo;
import com.zhushou.weichat.bean.AppUsersInfo;
import com.zhushou.weichat.bean.LoginMsgInfo;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.bean.ScreenShotPreview;
import com.zhushou.weichat.bean.UpdateInfo;
import com.zhushou.weichat.ui.ListAppContract;
import com.zhushou.weichat.ui.models.AppDataSource;
import com.zhushou.weichat.ui.models.AppModel;
import com.zhushou.weichat.ui.models.AppRepository;
import com.zhushou.weichat.uinew.info.HomeItemInfo;
import com.zhushou.weichat.uinew.interfac.HomeAppContract;
import com.zhushou.weichat.utils.CountUtil;
import com.zhushou.weichat.utils.DebugLog;
import com.zhushou.weichat.utils.HistoryPayShareUtil;
import com.zhushou.weichat.utils.LoginSP;
import com.zhushou.weichat.utils.ToastUtil;
import com.zhushou.weichat.utils.UpdateUtil;
import com.zhushou.weichat.utils.WeiChatVersionCodeSp;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;
import com.zhushou.weichat.utils.sp.AppAdvShareUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */
@Deprecated
public class HomeFragmentImpl implements HomeAppContract.AppPresenter, ListAppContract.StartCheck {

    private Activity mActivity;
    private HomeAppContract.AppView appView;
    public AppDataSource mRepository;
    private int implementType = 0;
    private Resources mResources;
    private CountUtil countUtil;

    //声明键盘管理器
    private KeyguardManager mKeyguardManager = null;
    //声明键盘锁
    private KeyguardManager.KeyguardLock mKeyguardLock = null;
    private ScreenListener screenListener; // 屏幕监听

    public HomeFragmentImpl(Activity mActivity, HomeAppContract.AppView AppView) {
        this.mActivity = mActivity;
        this.appView = AppView;
        mRepository = new AppRepository(mActivity);
        mResources = mActivity.getResources();
        countUtil = new CountUtil(mActivity);
    }

    @Override
    public void start(int type) {
        appView.startLoading();
        implementType = type;
        checkWxVersionCode();
    }

    @Override
    public void addWeiChatApp(String appName, boolean isFreeTrial) {
        DebugLog.e("创建微信分身 //"+appName+"//"+isFreeTrial);
        try {
            createUser(appName,isFreeTrial);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.centerToast(mActivity,"创建分身时出现异常");
        }
    }

    @Override
    public void deleteItemApp(ApkItemInfo apkItemInfo) {

    }

    @Override
    public void deleteVirtualApp(AppModel appModel) {

    }

    @Override
    public void addItemToDeskTop(ApkItemInfo apkItemInfo, int position) {

    }

    @Override
    public void resetModel() {
        //todo 修改12.4
//        mRepository.getVirtualApps(HomeFragmentImpl.this,HomeFragmentImpl.this).done(mRepository::chackApps);
    }

    @Override
    public void resetVirtualApps() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void showPreviewDialog(List<ScreenShotPreview> listData, String functionKey) {

    }

    @Override
    public void checkWxVersionCode() {
        mRepository.wxCodeUpdateToAppModel(mActivity.getPackageManager().getInstalledPackages(0)).done(this::nowWxVersionCode);
    }

    @Override
    public void isOk(List<AppModel> models) {
        appView.loadFinish(models);
        appView.mHideLoadin();
    }

    @Override
    public void isNot() {
        mRepository.getInstalledApps(mActivity).done(mRepository::installedWXAppList);
    }

    /**
     * 当前主微信 model
     *
     * @param appModel
     */
    public void nowWxVersionCode(AppModel appModel) {
        String oldVersionCode = WeiChatVersionCodeSp.getVersionCode(mActivity);
        if (oldVersionCode == null || oldVersionCode.equals("")) {
            WeiChatVersionCodeSp.saveVersionCode(mActivity, String.valueOf(appModel.versionCode));
            appStart();
            return;
        }
        if (!oldVersionCode.equals(String.valueOf(appModel.versionCode))) { // 不相等 需要升级

            new AlertDialog.Builder(mActivity).setTitle("警告")
                    .setMessage("经检测：手机主微信版本已更新！本应用需要更新微信分身插件，否则分身可能将无法使用！\n更新不消耗流量")
                    .setCancelable(true)
                    .setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            appStart();
                        }
                    })
                    .setPositiveButton("更新插件", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            WeiChatVersionCodeSp.saveVersionCode(mActivity, String.valueOf(appModel.versionCode));
                            mRepository.clearWxAppModel();
                            appStart();
                        }
                    })
                    .show();
        } else { // 相等 无需操作
            appStart();
        }
    }

    public void clearWxAppModel(){
        mRepository.clearWxAppModel();
        appStart();
    }

    public void appStart() {
        if (HistoryPayShareUtil.isWeixinAvilible(mActivity)) {
            // TODO: 2017/12/14  1.4.5 新版多开广告的盒子数据 现盒子广告数据已改变，如重新启用请已盒子数据结构为准
            ZhuShouApi.appAdvInfoGet(1,startHandler);
        } else {
            appView.mHideLoadin();
            Toast.makeText(mActivity, mResources.getString(R.string.anzhuang_weixin_app), Toast.LENGTH_SHORT).show();
        }

        screenListener = new ScreenListener(mActivity); // 实例化屏幕监听器
        screenListener.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {

            }

            @Override
            public void onScreenOff() {
                VApp.getInstance().setLockScreenStatus(false);
                //获取系统服务
                if (mKeyguardManager == null)
                    mKeyguardManager = (KeyguardManager) mActivity.getSystemService(Context.KEYGUARD_SERVICE);
                //初始化键盘锁，可以锁定或解开键盘锁
                if (mKeyguardLock == null)
                    mKeyguardLock = mKeyguardManager.newKeyguardLock("");
                //恢复系统锁屏服务
                mKeyguardLock.reenableKeyguard();
            }

            @Override
            public void onUserPresent() {
                VApp.getInstance().setLockScreenStatus(true);
            }
        });

    }
    private void createUser(String userName,boolean isFreeTrial) throws Exception{
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(mActivity, mResources.getString(R.string.input_value_null), Toast.LENGTH_SHORT).show();
            return;
        }
        VUserInfo vUserInfo =  VUserManager.get().createUser(userName, VUserInfo.FLAG_ADMIN);
        if (vUserInfo==null){
            Toast.makeText(mActivity,mResources.getString(R.string.chuang_jian_shi_bai),Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mActivity,mResources.getString(R.string.chuang_jian_cheng_gong),Toast.LENGTH_SHORT).show();
        }
        if (vUserInfo==null){
            return;
        }
        boolean iss = VirtualCore.get().installPackageAsUser(vUserInfo.id, "com.tencent.mm");
        if (!iss){
            ToastUtil.centerToast(mActivity,"创建失败");
            return;
        }

        AppUsersInfo weichat_userInfo = new AppUsersInfo();
        weichat_userInfo.setId(String.valueOf(vUserInfo.id));
        weichat_userInfo.setAppType(ApkItemInfo.WEICHAT_TYPE);
        weichat_userInfo.setAppName(userName);
        weichat_userInfo.setPrice("");
        weichat_userInfo.setDate(HistoryPayShareUtil.getSystemTime(VApp.DATE_NOSECOND));
        weichat_userInfo.setPackageName("com.tencent.mm");
        if (isFreeTrial){
            weichat_userInfo.setCreateTiem(System.currentTimeMillis());
            weichat_userInfo.setFreeTrialType(true);
        }

        APPUsersShareUtil.setUsersList(mActivity,weichat_userInfo);
        HomeItemInfo apkItemInfo = new HomeItemInfo();
        apkItemInfo.setTitle(userName);
        apkItemInfo.setUserInfo(vUserInfo);
        apkItemInfo.setAppModel(VApp.getApp().getWeiChatModel());
        apkItemInfo.setCreateTimeIdCard(HistoryPayShareUtil.getFormatedDateTime("yy-MM-dd",vUserInfo.creationTime));
        apkItemInfo.setCreateTiem(System.currentTimeMillis());
        apkItemInfo.setPackageName("com.tencent.mm");
        apkItemInfo.setFreeTrialType(isFreeTrial);
        appView.addItem(apkItemInfo);
    }
    private Handler startHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    UpdateInfo updateVersionInfo = null;
                    OkHttpResult result = (OkHttpResult) msg.obj;
                    if (result != null && result.isSuccess) {
                        try {
                            APPUsersShareUtil.analysisAppBaseJson(result.msg,mActivity);
                            JSONObject jo = new JSONObject(result.msg);
                            updateVersionInfo = UpdateUtil.DecodeUpdateJson(jo, countUtil.getFrom(), countUtil.getApp(), countUtil.getVersion());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mActivity, mActivity.getString(R.string.getdata_fail), Toast.LENGTH_SHORT).show();
                            appView.netWorkRequestFailure();
                            return;
                        }
                    } else {
                        appView.netWorkRequestFailure();
                        Toast.makeText(mActivity, mActivity.getString(R.string.lianjie_shibai), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    appView.startUpdateApp(updateVersionInfo);
                    if (implementType != 0 && implementType == ListAppContract.SET_WEICHAT) {
                        Log.i("MainActivity", "handleMessage: ");
//                        mRepository.getVirtualApps(HomeFragmentImpl.this,HomeFragmentImpl.this).done(mRepository::chackApps);
                    } else if (implementType != 0 && implementType == ListAppContract.SET_ALL) {
//                        mRepository.getVirtualApps(HomeFragmentImpl.this,HomeFragmentImpl.this).done(appView::loadInstallAllApps);
                    }
                    try {
                        LoginMsgInfo loginMsgInfo = LoginSP.getLoginMsg(mActivity);

                        if (loginMsgInfo.getOpenid()!=null)
                            OpenRedRequest.loginGet(loginMsgInfo.getOpenid(),2,startHandler);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    OkHttpResult okHttpResult = (OkHttpResult) msg.obj;
                    if (okHttpResult.isSuccess){
                        try {
                            AppAdvShareUtil.saveAppAdv(mActivity,okHttpResult.msg);
                            ZhuShouApi.appBaseInfoGet(0, startHandler);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            appView.netWorkRequestFailure();
                        }
                    }else{
                        appView.netWorkRequestFailure();
                    }
                    break;
                case 2:
                    JSONObject jo = null;
                    try {
                        jo = new JSONObject(((OkHttpResult) msg.obj).msg);
                        int resultcode = jo.getInt("code");
                        if (resultcode==1){
                            JSONObject resultjo = jo.getJSONObject("content");
                            LoginSP.saveLoginMsg(mActivity, resultjo.toString());
                            LoginMsgInfo loginMsgInfo = LoginSP.getLoginMsg(mActivity);
                            if (loginMsgInfo.getBdCode()!=null&&!loginMsgInfo.getBdCode().isEmpty()&&!loginMsgInfo.getBdCode().equals("")){
                                appView.bdHandling(loginMsgInfo.getBdCode());
                            }
                        }else{
                            ToastUtil.centerToast(mActivity, "登录失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }
    };
}
