package com.zhushou.weichat.ui;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.VLog;
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
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.screenshot.Utils.ListDataSave;
import com.zhushou.weichat.screenshot.roleIndex.StringHelper;
import com.zhushou.weichat.ui.dialog.ApkNameDialog;
import com.zhushou.weichat.ui.dialog.PreviewDialog;
import com.zhushou.weichat.ui.models.AppDataSource;
import com.zhushou.weichat.ui.models.AppModel;
import com.zhushou.weichat.ui.models.AppRepository;
import com.zhushou.weichat.utils.AddShortcutToDesktop;
import com.zhushou.weichat.utils.CountUtil;
import com.zhushou.weichat.utils.DebugLog;
import com.zhushou.weichat.utils.HistoryPayShareUtil;
import com.zhushou.weichat.utils.LoginSP;
import com.zhushou.weichat.utils.TLog;
import com.zhushou.weichat.utils.ToastUtil;
import com.zhushou.weichat.utils.UpdateUtil;
import com.zhushou.weichat.utils.UserHelper;
import com.zhushou.weichat.utils.WeiChatVersionCodeSp;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lody
 */
public class ListAppPresenterImpl implements ListAppContract.ListAppPresenter, ListAppContract.StartCheck {

    private static final String TAG = "ListAppPresenterImpl";
    private Activity mActivity;
    private ListAppContract.ListAppView mView;
    private AppDataSource mRepository;
    private Resources mResources;
    private ScreenListener screenListener; // 屏幕监听
    private AddShortcutToDesktop desktop;
    private CountUtil countUtil;
    private PreviewDialog previewDialog;
    private int implementType = 0;

    private ListDataSave save;

    //声明键盘管理器
    private KeyguardManager mKeyguardManager = null;
    //声明键盘锁
    private KeyguardManager.KeyguardLock mKeyguardLock = null;

    public ListAppPresenterImpl(Activity activity, ListAppContract.ListAppView view) {
        mActivity = activity;
        mResources = mActivity.getResources();
        mView = view;
        mRepository = new AppRepository(activity);
        mView.setPresenter(this);
        desktop = new AddShortcutToDesktop(mActivity);
        countUtil = new CountUtil(activity);
    }

    @Override
    public void start(int type) {
//		Toast.makeText(mActivity,VApp.APP_ID,Toast.LENGTH_SHORT).show();
        mView.startLoading();
        previewDialog = new PreviewDialog(mActivity);
        implementType = type;
        checkWxVersionCode();
    }

    public void appStart() {
        Log.i(TAG, "appStart: ");
        if (HistoryPayShareUtil.isWeixinAvilible(mActivity)) {
//            new Thread(new StartRunnable()).start();
            ZhuShouApi.appBaseInfoGet(0,startHandler);
        } else {
            mView.mHideLoadin();
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
                TLog.log(TAG, "suo ping");
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
//				TLog.log(TAG,"ping mu da kai");
            }
        });

        save = new ListDataSave(mActivity, "tt");

        List<Map<String, String>> list = save.getDataList("tt");
        if (list.size() == 0) {
            iniRole();
        }
    }

    @Override
    public void selectApp(AppModel model) {
//		Intent data = new Intent();
//		data.putExtra(VCommends.EXTRA_APP_MODEL, model);
//		mActivity.setResult(Activity.RESULT_OK, data);
//		mActivity.finish();
    }

    @Override
    public void addWeiChatApp(String appName, String price, String date, boolean isFreeTrial) {
        try {
            createUser(appName, ApkItemInfo.WEICHAT_TYPE, price, date, isFreeTrial);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mActivity, "1" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void addQQApp(String appName, String price, String date, boolean isFreeTrial) {
        try {
            createUser(appName, ApkItemInfo.QQ_TYPE, price, date, isFreeTrial);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void changeItemAppName(AppModel model) {
    }

    /**
     * 当前主微信 model
     *
     * @param appModel
     */
    public void nowWxVersionCode(AppModel appModel) {
        Log.i(TAG, "nowWxVersionCode: ");
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


    @Override
    public void deleteItemApp(ApkItemInfo apkItemInfo) {
//
//		new ApkNameDialog(mActivity).myAlertDialog(apkItemInfo.getTitle().toString(), new ApkNameDialog.ApkNameCallback() {
//			@Override
//			public void confirmCallback(String apkName) {
//				mView.startLoading();
//				Log.i(TAG, "confirmCallback: ");
//				boolean deleted = VUserManager.get().removeUser(apkItemInfo.getUserInfo().id);
//				APPUsersShareUtil.deleteUser(mActivity,apkItemInfo.getUserInfo().id);
//				mView.deleteItem(apkItemInfo);
//				Toast.makeText(mActivity, deleted?mResources.getString(R.string.shanchu_chenggong):mResources.getString(R.string.shanchu_shibai), Toast.LENGTH_SHORT).show();
//			}
//		});
    }

    @Override
    public void deleteVirtualApp(AppModel appModel) {
        if (appModel != null) {
            try {
                mRepository.removeVirtualApp(appModel);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            resetVirtualApps();
        }
    }

    @Override
    public void addItemToDeskTop(ApkItemInfo apkItemInfo, int position) {
        new ApkNameDialog(mActivity).myAddDeskAlertDialog(apkItemInfo.getTitle().toString(), new ApkNameDialog.ApkNameCallback() {
            @Override
            public void confirmCallback(String apkName) {
                if (!desktop.isShortcutInstalled(apkItemInfo.getTitle().toString())) {
                    VirtualCore.OnEmitShortcutListener listener = new VirtualCore.OnEmitShortcutListener() {
                        @Override
                        public Bitmap getIcon(Bitmap originIcon) {
                            return originIcon;
                        }

                        @Override
                        public String getName(String originName) {
                            return apkItemInfo.getTitle().toString();
                        }
                    };
                    boolean isbon = VirtualCore.get().createShortcut(apkItemInfo.getUserInfo().id, apkItemInfo.getAppModel().packageName, listener);
                    Toast.makeText(mActivity, isbon ? mResources.getString(R.string.adddesktop_chenggong) : mResources.getString(R.string.adddesktop_shibai_qx), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, mResources.getString(R.string.adddesktop_shibai), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void resetModel() {
        Log.i("MainActivity", "resetModel: ");
        //// TODO: 2017/11/28
        mRepository.getVirtualApps(ListAppPresenterImpl.this).done(mRepository::chackApps);
    }

    @Override
    public void resetVirtualApps() {
        mView.startLoading();
        //// TODO: 2017/11/28
        mRepository.getVirtualApps(ListAppPresenterImpl.this).done(mView::loadVirtualApps);
    }

    @Override
    public void onDestroy() {
        if (screenListener != null)
            screenListener.unregisterListener();
    }

    @Override
    public void showPreviewDialog(List<ScreenShotPreview> listData, String functionKey) {
        if (functionKey == null)
            return;
        if (previewDialog == null)
            return;
        if (listData == null || listData.size() <= 0)
            return;
        String imageUrl = "";
        for (int i = 0; i < listData.size(); i++) {
            ScreenShotPreview info = listData.get(i);
            if (info.getFunction_name().equals(functionKey)) {
                imageUrl = info.getImage_url();
                i = listData.size();
                break;
            }
        }
        previewDialog.previewShow(imageUrl);
    }


    /**
     * 检查微信版本
     */
    @Override
    public void checkWxVersionCode() {
        Log.i(TAG, "checkWxVersionCode: ");
        mRepository.wxCodeUpdateToAppModel(mActivity.getPackageManager().getInstalledPackages(0)).done(this::nowWxVersionCode);
    }

    @Override
    public void clearWxModel() {
        mRepository.clearWxAppModel();
        start(ListAppContract.SET_WEICHAT);
    }

    @Override
    public void isOk(List<AppModel> models) {
        Log.i("MainActivity", "isOk: ");
        mView.loadFinish(models);
//		Toast.makeText(mActivity,"isOk!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isNot() {
//		Toast.makeText(mActivity,"isNot!",Toast.LENGTH_SHORT).show();
        // 微信 QQ models不存在！一般第一次进入应用时会出现
        // 创建应用快捷键
//		AddShortcutToDesktop addShortcutToDesktopb = new AddShortcutToDesktop(mActivity);
//		if (!addShortcutToDesktopb.isShortcutInstalled()){
//			addShortcutToDesktopb.addShortcutToDesktop();
//		}
        //添加微信  models，
        mRepository.getInstalledApps(mActivity).done(mView::installedAppList);
        DebugLog.e("isNot");
    }

    private void createUser(String userName, int type, String price, String date, boolean isFreeTrial) throws Exception {
        mView.startLoading();
        Log.i(TAG, "createUser: ");
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(mActivity, mResources.getString(R.string.input_value_null), Toast.LENGTH_SHORT).show();
            return;
        }

        VUserInfo vUserInfo = VUserManager.get().createUser(userName, VUserInfo.FLAG_ADMIN);
        if (vUserInfo == null) {
            Toast.makeText(mActivity, mResources.getString(R.string.chuang_jian_shi_bai), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mActivity, mResources.getString(R.string.chuang_jian_cheng_gong), Toast.LENGTH_SHORT).show();
        }
        if (vUserInfo == null) {
            return;
        }
        boolean iss = VirtualCore.get().installPackageAsUser(vUserInfo.id, "com.tencent.mm");
        if (!iss) {
            return;
        }

        switch (type) {
            case ApkItemInfo.QQ_TYPE:
//				String shareQQ = (String) SharedPreferencesUtils.getParam(mActivity,VApp.Share_QQ_User,"");
//				if (shareQQ!=null&&!shareQQ.equals("")){
//					SharedPreferencesUtils.setParam(mActivity,VApp.Share_QQ_User,shareQQ+vUserInfo.id+",");
//					Log.i(TAG, "createUser: insert QQ str="+shareQQ+userName+",");
//				}else{
//					SharedPreferencesUtils.setParam(mActivity,VApp.Share_QQ_User,vUserInfo.id+",");
//					Log.i(TAG, "createUser: insert QQ str="+userName+",");
//				}
                AppUsersInfo qq_userInfo = new AppUsersInfo();
                qq_userInfo.setId(String.valueOf(vUserInfo.id));
                qq_userInfo.setAppType(ApkItemInfo.QQ_TYPE);
                qq_userInfo.setAppName(userName);
                qq_userInfo.setPrice(price);
                qq_userInfo.setDate(date);
                APPUsersShareUtil.setUsersList(mActivity, qq_userInfo);
                break;
            case ApkItemInfo.WEICHAT_TYPE:
//				String shareWeiChat = (String) SharedPreferencesUtils.getParam(mActivity,VApp.Share_WeiChat_User,"");
//				if (shareWeiChat!=null&&!shareWeiChat.equals("")){
//					SharedPreferencesUtils.setParam(mActivity,VApp.Share_WeiChat_User,shareWeiChat+vUserInfo.id+",");
//					Log.i(TAG, "createUser: insert WeiChat str="+shareWeiChat+userName+",");
//				}else{
//					SharedPreferencesUtils.setParam(mActivity,VApp.Share_WeiChat_User,vUserInfo.id+",");
//					Log.i(TAG, "createUser: insert WeiChat str="+userName+",");
//				}
                AppUsersInfo weichat_userInfo = new AppUsersInfo();
                weichat_userInfo.setId(String.valueOf(vUserInfo.id));
                weichat_userInfo.setAppType(ApkItemInfo.WEICHAT_TYPE);
                weichat_userInfo.setAppName(userName);
                weichat_userInfo.setPrice(price);
                weichat_userInfo.setDate(date);
                weichat_userInfo.setPackageName("com.tencent.mm");
                if (isFreeTrial) {
                    weichat_userInfo.setCreateTiem(System.currentTimeMillis());
                    weichat_userInfo.setFreeTrialType(true);
                }
                APPUsersShareUtil.setUsersList(mActivity, weichat_userInfo);
                break;
        }

        ApkItemInfo apkItemInfo = new ApkItemInfo();
        apkItemInfo.setTitle(userName);
        apkItemInfo.setUserInfo(vUserInfo);
        apkItemInfo.setAppModel(type == ApkItemInfo.QQ_TYPE ? VApp.getApp().getQQModel() : VApp.getApp().getWeiChatModel());
        apkItemInfo.setCreateDate(HistoryPayShareUtil.getFormatedDateTime("yy-MM-dd", vUserInfo.creationTime));
        if (isFreeTrial) {
            apkItemInfo.setCreateTiem(System.currentTimeMillis());
            apkItemInfo.setFreeTrialType(isFreeTrial);
        }
        mView.addItem(apkItemInfo, type);
    }


//    class StartRunnable implements Runnable {
//        @Override
//        public void run() {
//            long startTime = System.currentTimeMillis();
//            //?????????????????????
//            OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
//            OkHttpResult result = okHttpUtil.requestGetBySyn(VApp.URL, null, null);
//            long spend = System.currentTimeMillis() - startTime;
//            if (spend < 1000) {
//                try {
//                    Thread.sleep(1000 - spend);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            Message msg = new Message();
//            msg.obj = result;
//            msg.what = 0;
//            startHandler.sendMessage(msg);
//        }
//    }

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
                            JSONObject jo = new JSONObject(result.msg);
                            if (jo != null) {
                                UpdatePriceInfo oldUpdateInfo = APPUsersShareUtil.getUpdateMessage(mActivity);
                                String addTime = oldUpdateInfo.getSysj() != null ? oldUpdateInfo.getSysj() : "0";
                                UpdatePriceInfo updateInfo = new UpdatePriceInfo();
                                updateInfo.setVersionCode(jo.getString("versionCode"));
                                updateInfo.setAppId(jo.getString("appId"));
                                updateInfo.setAd_length(jo.getInt("ad_length"));
                                updateInfo.setDiscount(jo.getString("discount"));
                                updateInfo.setTuia_adv_isbrowser(jo.getBoolean("tuia_adv_isbrowser"));
                                updateInfo.setKfqq(jo.getString("kfqq"));
                                updateInfo.setKfwx(jo.getString("kfwx"));
                                updateInfo.setKfdh(jo.getString("kfdh"));
                                updateInfo.setTop_tip(jo.getString("top_tip"));
                                updateInfo.setShareTime(jo.getString("fxsj"));
                                updateInfo.setShareaddress(jo.getString("shareaddress"));
                                updateInfo.setFxcs(jo.getString("fxcs"));
                                updateInfo.setLeft_button_url(jo.getString("left_button_url"));


                                //保存截图功能预览图片信息
                                JSONArray previewJa = jo.getJSONArray("screenshot_preview");
                                APPUsersShareUtil.setScreenShotPreviewArray(mActivity, previewJa.toString());


                                JSONObject fzJson = jo.getJSONObject("weichat_click_id");
                                updateInfo.setWeichat_version_code(fzJson.getString("weichat_version_code"));
                                updateInfo.setId_hb(fzJson.getString("id_hb"));
                                updateInfo.setId_chb(fzJson.getString("id_chb"));
                                updateInfo.setId_hf_input(fzJson.getString("id_hf_input"));
                                updateInfo.setId_hf_sendbtn(fzJson.getString("id_hf_sendbtn"));
                                updateInfo.setId_hb_btn_ly(fzJson.getString("id_hb_btn_ly"));//废除
                                updateInfo.setId_hb_input_ly(fzJson.getString("id_hb_input_ly"));//废除
                                updateInfo.setId_hb_send_ly(fzJson.getString("id_hb_send_ly"));//废除
                                updateInfo.setId_hb_detail_back(fzJson.getString("id_hb_detail_back"));
                                updateInfo.setId_hb_grab_money(fzJson.getString("id_hb_grab_money"));
                                updateInfo.setId_hb_boss_name(fzJson.getString("id_hb_boss_name"));
                                updateInfo.setId_chat_item_id(fzJson.getString("id_chat_item_id"));//微信聊天界面Item ID

                                //ui隐藏控制
                                JSONObject ycJson = jo.getJSONObject("wxhb_visiblility");
                                updateInfo.setFfh(ycJson.getBoolean("ffh"));
                                updateInfo.setWxqhb(ycJson.getBoolean("wxqhb"));
                                updateInfo.setShouqi(ycJson.getBoolean("shouqi"));
                                updateInfo.setDabao(ycJson.getBoolean("dabao"));
                                updateInfo.setJiasudu(ycJson.getBoolean("jiasudu"));
                                updateInfo.setGuanrao(ycJson.getBoolean("guanrao"));
                                updateInfo.setXiaobao(ycJson.getBoolean("xiaobao"));
                                updateInfo.setQgg(ycJson.getBoolean("qgg"));
                                updateInfo.setJhy(ycJson.getBoolean("jhy"));
                                updateInfo.setZdhf(ycJson.getBoolean("zdhf"));

                                JSONObject wxPayStatusJo = jo.getJSONObject("pay_status_wx");
                                updateInfo.setPay_status_wx(wxPayStatusJo.getBoolean(countUtil.getApp()));
                                JSONObject aliPayStatusJo = jo.getJSONObject("pay_status_ali");
                                updateInfo.setPay_status_ali(aliPayStatusJo.getBoolean(countUtil.getApp()));
                                JSONObject qqPayStatusJo = jo.getJSONObject("pay_status_qq");
                                updateInfo.setPay_status_qq(qqPayStatusJo.getBoolean(countUtil.getApp()));

                                //红包功能点价格
                                JSONObject pcJson = jo.getJSONObject("wxqhb_function_price");
                                updateInfo.setPc_svip(pcJson.getString("pc_svip"));
                                updateInfo.setPc_shouqi(pcJson.getString("pc_shouqi"));
                                updateInfo.setPc_dabao(pcJson.getString("pc_dabao"));
                                updateInfo.setPc_xiaobao(pcJson.getString("pc_xiaobao"));
                                updateInfo.setPc_jiasudu(pcJson.getString("pc_jiasudu"));
                                updateInfo.setPc_ganrao(pcJson.getString("pc_ganrao"));
                                updateInfo.setPc_xpqhb(pcJson.getString("pc_xpqhb"));
                                updateInfo.setPc_zddx(pcJson.getString("pc_zddx"));

                                updateInfo.setSysj(jo.getString("sysj"));
                                updateInfo.setScfy(jo.getString("scfy"));
                                updateInfo.setZcfy(jo.getString("zcfy"));
                                updateInfo.setAllAppsPrice(jo.getString("all_apps_price"));
                                updateInfo.setWeichat_hf_all_price(jo.getString("weichat_hf_all_price"));
                                updateInfo.setWeichat_hf_dg_price(jo.getString("weichat_hf_dg_price"));
                                updateInfo.setWeichat_jt_all_price(jo.getString("weichat_jt_all_price"));
                                updateInfo.setWeichat_jt_dg_price(jo.getString("weichat_jt_dg_price"));
                                updateInfo.setWeichat_jf_dg_price(jo.getString("weichat_jf_dg_price"));
                                updateInfo.setWeichat_jf_all_price(jo.getString("weichat_jf_all_price"));
                                updateInfo.setWeichat_qgg_price(jo.getString("weichat_qgg_price"));
                                updateInfo.setWeichat_ffh_price(jo.getString("weichat_ffh_price"));
                                updateInfo.setWeichat_svip_price(jo.getString("weichat_svip"));

//                                updateInfo.setPc_svip("0.03");
//                                updateInfo.setPc_shouqi("0.01");
//                                updateInfo.setPc_dabao("0.01");
//                                updateInfo.setPc_xiaobao("0.01");
//                                updateInfo.setPc_jiasudu("0.01");
//                                updateInfo.setPc_ganrao("0.01");
//                                updateInfo.setPc_xpqhb("0.01");
//                                updateInfo.setPc_zddx("0.01");
//
//                                updateInfo.setSysj("2");
//                                updateInfo.setScfy("1");
//                                updateInfo.setZcfy("0.01");
//                                updateInfo.setAllAppsPrice("0.02");
//                                updateInfo.setWeichat_hf_all_price("0.02");
//                                updateInfo.setWeichat_hf_dg_price("0.01");
//                                updateInfo.setWeichat_jt_all_price("0.02");
//                                updateInfo.setWeichat_jt_dg_price("0.01");
//                                updateInfo.setWeichat_jf_dg_price("0.01");
//                                updateInfo.setWeichat_jf_all_price("0.02");
//                                updateInfo.setWeichat_qgg_price("0.01");
//                                updateInfo.setWeichat_ffh_price("0.01");
//                                updateInfo.setWeichat_svip_price("0.03");

                                if (oldUpdateInfo.getMfSj() != null) {
                                    if (Integer.valueOf(oldUpdateInfo.getMfSj()) > 0) {
                                        updateInfo.setMfSj(oldUpdateInfo.getMfSj());
                                    } else {
                                        updateInfo.setMfSj(jo.getString("sysj"));
//								updateInfo.setMfSj("1");
                                    }
                                } else {
                                    updateInfo.setMfSj(jo.getString("sysj"));
//							updateInfo.setMfSj("1");
                                }
                                APPUsersShareUtil.setUpdateMessage(mActivity, updateInfo);
                            }
//					int versionCode = jo.getInt("versionCode");
//					if(compareVersion(versionCode)){//???????
                            VLog.d("Startacitivty", "jinru");
//						Toast.makeText(mActivity,countUtil.getFrom()+"//"+countUtil.getApp()+"//"+countUtil.getVersion(),Toast.LENGTH_SHORT).show();
                            updateVersionInfo = UpdateUtil.DecodeUpdateJson(jo, countUtil.getFrom(), countUtil.getApp(), countUtil.getVersion());

//					}
                            String kfqqStr = jo.getString("kfqq");
                            if (kfqqStr != null) {
                                UserHelper uih = new UserHelper(mActivity);
                                uih.setKfqq(kfqqStr);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mActivity, mActivity.getString(R.string.getdata_fail), Toast.LENGTH_SHORT).show();
                            mView.netWorkRequestFailure();
                            return;
                        }
                    } else {
                        mView.netWorkRequestFailure();
                        Toast.makeText(mActivity, mActivity.getString(R.string.lianjie_shibai), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mView.startUpdateApp(updateVersionInfo);


                    //用户登录信息处理
                    LoginMsgInfo loginMsgInfo = null;
                    try {
                        loginMsgInfo = LoginSP.getLoginMsg(mActivity);
                        if (loginMsgInfo.getOpenid() != null)
                            OpenRedRequest.loginGet(loginMsgInfo.getOpenid(), 2, startHandler);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //应用广告信息更新保存
                    ZhuShouApi.appAdvInfoGet(3,startHandler);

                    if (implementType != 0 && implementType == ListAppContract.SET_WEICHAT) {
                        Log.i("MainActivity", "handleMessage: ");
                        //tos
                        mRepository.getVirtualApps(ListAppPresenterImpl.this).done(mRepository::chackApps);
                    } else if (implementType != 0 && implementType == ListAppContract.SET_ALL) {
                        mRepository.getVirtualApps(ListAppPresenterImpl.this).done(mView::loadInstallAllApps);
                    }
                    break;
                case 2:
                    JSONObject jo = null;
                    try {
                        jo = new JSONObject(((OkHttpResult) msg.obj).msg);
                        int resultcode = jo.getInt("code");
                        if (resultcode == 1) {
                            JSONObject resultjo = jo.getJSONObject("content");
                            LoginSP.saveLoginMsg(mActivity, resultjo.toString());
                            LoginMsgInfo loginMsgInfoNow = LoginSP.getLoginMsg(mActivity);
                            if (loginMsgInfoNow.getBdCode() != null && !loginMsgInfoNow.getBdCode().isEmpty() && !loginMsgInfoNow.getBdCode().equals("")) {
                                mView.bdHandling(loginMsgInfoNow.getBdCode());
                            }
                        } else {
                            ToastUtil.centerToast(mActivity, "登录失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case 3:
                    if (countUtil==null)
                        break;
                    OkHttpResult okHttpResult = (OkHttpResult) msg.obj;
                    if (okHttpResult.isSuccess){
                        try {
                            JSONArray ja = new JSONArray(okHttpResult.msg);
                            for (int i=0;i<ja.length();i++){
                                JSONObject advjo = ja.getJSONObject(i);
                                if (advjo.getString("app_index").equals(countUtil.getApp())){
                                    advjo.getJSONObject("app_adv");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }

        }
    };



    //获取androidmanifest中配置的版本号
    public int getVersion() {
        try {
            PackageManager manager = mActivity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mActivity.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private List<Map<String, String>> iniRole() {
        List<Map<String, String>> persons = new ArrayList<>();
        InputStream is = null;
        String path = "";
        try {
            is = mActivity.getAssets().open("img10001.png");
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            saveImage(bitmap, "test.png");
            path = Environment.getExternalStorageDirectory() + "/myImage/test.png";
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < 5; i++) {
            if (i <= 2) {
                Map<String, String> map = new HashMap<>();
                map.put("name", "迪士尼分");
                map.put("image", path);
                persons.add(map);
            } else if (i > 2 && i < 4) {
                Map<String, String> map = new HashMap<>();
                map.put("name", "啥的");
                map.put("image", path);
                persons.add(map);
            } else {
                Map<String, String> map = new HashMap<>();
                map.put("name", "查询");
                map.put("image", path);
                persons.add(map);
            }
        }
        save.setDataList("tt", sortIndex(persons));
        return persons;
    }

    public List<Map<String, String>> sortIndex(List<Map<String, String>> persons) {
        List<Map<String, String>> list = new ArrayList<>();

        for (int i = 0; i < persons.size(); i++) {
            list.add(persons.get(i));
        }

        //获取拼音名字
        for (int i = 0; i < list.size(); i++) {
            String pinyin = StringHelper.getPingYin(list.get(i).get("name")).toLowerCase();
            list.get(i).put("pinyin", pinyin);
        }
        //根据拼音名字排序
        Collections.sort(list, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                if (o1.get("pinyin").equals(o2.get("pinyin"))) {
                    return -1;
                }
                return o1.get("pinyin").compareTo(o2.get("pinyin"));
            }
        });
        return list;
    }

    private static boolean saveImage(Bitmap head, String newFileName) {
        Bitmap bitmap = head;
        String strPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myImage";

        try {
            File destDir = new File(strPath);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            File imageFile = new File(strPath + "/" + newFileName);
            if (imageFile.exists()) {
                imageFile.delete();
            }
            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }


}
