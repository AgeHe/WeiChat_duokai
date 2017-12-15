package com.zhushou.weichat.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.db.ta.sdk.NonStandardTm;
import com.db.ta.sdk.NsTmListener;
import com.lbsw.stat.LBStat;
import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.lody.virtual.remote.InstalledAppInfo;
import com.umeng.analytics.MobclickAgent;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.VCommends;
import com.zhushou.weichat.abs.ui.VUiKit;
import com.zhushou.weichat.addfriends.ui.AddFriendsFunctionA;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryConstans;
import com.zhushou.weichat.auxiliary.sp.SPAuxiliaryPayUtils;
import com.zhushou.weichat.auxiliary.ui.HBActivity;
import com.zhushou.weichat.auxiliary.ui.HFActivity;
import com.zhushou.weichat.bean.ApkItemInfo;
import com.zhushou.weichat.bean.AppUsersInfo;
import com.zhushou.weichat.bean.OrderDBInfo;
import com.zhushou.weichat.bean.ScreenShotPreview;
import com.zhushou.weichat.bean.StatisticsConstans;
import com.zhushou.weichat.bean.UMengStatisticalKey;
import com.zhushou.weichat.bean.UpdateInfo;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.constant.PayConstant;
import com.zhushou.weichat.screenshot.activity.QQBalance;
import com.zhushou.weichat.screenshot.activity.QQMOneyPacket;
import com.zhushou.weichat.screenshot.activity.QQRedPacket;
import com.zhushou.weichat.screenshot.activity.QQWithDrawable;
import com.zhushou.weichat.screenshot.activity.TransferAccounts;
import com.zhushou.weichat.screenshot.activity.WxFriendCircle;
import com.zhushou.weichat.screenshot.activity.WxMultiTalk;
import com.zhushou.weichat.screenshot.activity.WxPocketMoney;
import com.zhushou.weichat.screenshot.activity.WxRedPacket;
import com.zhushou.weichat.screenshot.activity.WxWithDrawals;
import com.zhushou.weichat.screenshot.activity.Wxtalk;
import com.zhushou.weichat.screenshot.activity.ZfbBalance;
import com.zhushou.weichat.screenshot.activity.ZfbRedPacket;
import com.zhushou.weichat.screenshot.activity.ZfbTransferAccount;
import com.zhushou.weichat.screenshot.activity.ZfbWithDrawable;
import com.zhushou.weichat.ui.adapter.InstallAppsAdapter;
import com.zhushou.weichat.ui.adapter.MakeApkRecyclerViewAdapter;
import com.zhushou.weichat.ui.dialog.ApkNameDialog;
import com.zhushou.weichat.ui.dialog.ExitAppDialog;
import com.zhushou.weichat.ui.dialog.MyTipDialog;
import com.zhushou.weichat.ui.dialog.PayFailDialog;
import com.zhushou.weichat.ui.dialog.ShareDialog;
import com.zhushou.weichat.ui.dialog.ShareWcDialog;
import com.zhushou.weichat.ui.dialog.UpdateDialog;
import com.zhushou.weichat.ui.models.AppDataSource;
import com.zhushou.weichat.ui.models.AppModel;
import com.zhushou.weichat.ui.models.AppRepository;
import com.zhushou.weichat.ui.pay.MyDialog;
import com.zhushou.weichat.ui.pay.PayActivity;
import com.zhushou.weichat.ui.pay.PayHelper;
import com.zhushou.weichat.ui.pay.SmallMerchantPayDialog;
import com.zhushou.weichat.ui.pay.WsScreenFunctionPayDialog;
import com.zhushou.weichat.ui.view.GifView;
import com.zhushou.weichat.ui.view.ImageViewFriendDot;
import com.zhushou.weichat.ui.view.MarqueeView;
import com.zhushou.weichat.ui.view.MyHiddenTextView;
import com.zhushou.weichat.ui.view.MyVIPScrollView;
import com.zhushou.weichat.ui.view.PagerView;
import com.zhushou.weichat.uinew.dialog.PayFailureDialog;
import com.zhushou.weichat.uinew.info.ComsuptionInfo;
import com.zhushou.weichat.utils.CountUtil;
import com.zhushou.weichat.utils.DebugLog;
import com.zhushou.weichat.utils.HistoryPayShareUtil;
import com.zhushou.weichat.utils.LauncherUtils;
import com.zhushou.weichat.utils.MyBackCheckService;
import com.zhushou.weichat.utils.MyDownloadManager;
import com.zhushou.weichat.utils.MyToast;
import com.zhushou.weichat.utils.SharedPreferencesUtils;
import com.zhushou.weichat.utils.UMengTJUtils;
import com.zhushou.weichat.utils.UserHelper;
import com.zhushou.weichat.utils.VirtualAppsSpUtil;
import com.zhushou.weichat.utils.database.DBLibs;
import com.zhushou.weichat.utils.database.OperationDB;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;
import com.zhushou.weichat.utils.sqlite.SqliteDo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import zhy.com.highlight.HighLight;
import zhy.com.highlight.shape.RectLightShape;

public class MainActivity extends PayActivity implements PayHelper.RequreClick, ListAppContract.ListAppView, MakeApkRecyclerViewAdapter.CLickCallback, UpdateDialog.UpdateCallback, View.OnClickListener {


    public static final int notType = 0xc1;
    public static final int singleType = 0xc2;
    public static final int lifeTimeType = 0xc3;
    public static final int allAppsType = 0x4;
    public static final int rewardAppType = 0x5;
    public static final int svipAllType = 0xc4;
    public static final int wsPayResultType = 0x12;
    public static final int wx_fsPayResultType = 0x13;
    private static final String TAG = "MainActivity";
    public static AlertDialog angin_query_dialog;
    public SqliteDo sqliteDo;
    public PayHelper payHelper;
    public MyDialog mDialog;
    public WsScreenFunctionPayDialog wsPayDialog;
    public TextView tv_svip_status, tv_ffh_status, tv_qgg_status;
    public ImageView iv_svip_status;
    public LinearLayout fl_ffh_is_visibility, fl_hb_is_visibility, ll_qgg_is_visibility,
            ll_jhy_visibility,
            ll_zdhf_isvisibility;
    RecyclerView rcv_recyclerview;
    MakeApkRecyclerViewAdapter rvAdapter;
    LinearLayout pb_loading_app;
    LinearLayout ll_network_request_failure;
    FloatingActionButton fab_add_weichat;
    ImageViewFriendDot iv_friend;
    GifView gif_reward;
    //    TMAwView tv_tuia_TMAw1; // 推啊 浮标广告
    List<ScreenShotPreview> screenShotPreviews;
    FrameLayout rl_title_adv;
    private NonStandardTm mNonStandardTm;
    String successApkname = "";
    int installedNumber = 0;
    int i = 0;
    private int waretype = notType;
    private MyHiddenTextView tv_hidden_background;
    private MyVIPScrollView my_scroll_vg;
    private AppRepository mRepo;
    private AppDataSource mRepository;
    private UpdateDialog updateDialog;
    private Context mContext;
    private CountUtil countUtil;
    private String payNum = "0.01";
    private String firstVipBaoyue = "8.8";
    private String vipBaoyue = "10.00";
    private ShareDialog shareDialog;
    private UpdatePriceInfo updateMessageInfo;
    private Resources mResources;

    private SmallMerchantPayDialog smDialog; // 微商支付弹窗
    private ListAppContract.StartCheck startCheck;
    private UMengTJUtils uMengTJUtils;
    private PayFailDialog payFailDialog;
    private MyTipDialog myTipDialog;
    private VirtualAppsSpUtil virtualAppsSpUtil;
    private PagerView pv_installApps;
    private InstallAppsAdapter installAppsAdapter;
    private boolean isCheckPayOrderShow = false;
    private HighLight mHightLight;
    private TextView tv_yindao_create, tv_yindao_vip;
    private ImageView tioa_image;

    private OperationDB operationDB;//支付信息数据库  用于对支付订单操作
    private PayFailureDialog payFailureDialog; //订单失败处理弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VApp.getInstance().setMainActivity(this);
        VApp.getInstance().setaActivity(this);
        mContext = this;
        countUtil = new CountUtil(mContext);
        mResources = this.getResources();
        findView();
        findSmallMerchantView();
        anginQueryDialog();//angin query dialog
        uMengTJUtils = new UMengTJUtils(this);


        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        float index = screenWidth / 750;
        SharedPreferences preferences = getSharedPreferences("index", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putFloat("index", index);
        ed.commit();

//        findViewById(R.id.iv_actionbar_add_weichat).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new ApkNameDialog(MainActivity.this).dialog(null, new ApkNameDialog.ApkNameCallback() {
//                    @Override
//                    public void confirmCallback(String apkName) {
//                        mPresenter.addWeiChatApp(apkName.toString(), "0", HistoryPayShareUtil.getSystemTime(VApp.DATE_NOSECOND), false);
//                    }
//                });
//            }
//        });
        /**
         * 添加微信分身
         */
        fab_add_weichat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, InstallAppsActivity.class);
                startActivityForResult(intent, VCommends.REQUEST_SELECT_APP);

//                if (APPUsersShareUtil.chekIsLifeTime(mContext)) {
//                    new ApkNameDialog(MainActivity.this).dialog(null, new ApkNameDialog.ApkNameCallback() {
//                        @Override
//                        public void confirmCallback(String apkName) {
//                            mPresenter.addWeiChatApp(apkName.toString(), "0", HistoryPayShareUtil.getSystemTime(VApp.DATE_NOSECOND), false);
//                        }
//                    });
//                } else {
//                    try {
//                        mDialog.showDialog(mDialog.getHomeDialogData());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }

            }
        });
    }

    MarqueeView tv_scrolltext;
    RelativeLayout ll_scrolltext;
    TextView tv_close_scrollText;

    private void findView() {
        payHelper = getHelper();

        payFailDialog = new PayFailDialog(this);
        sqliteDo = new SqliteDo(mContext);

        gif_reward = (GifView) findViewById(R.id.gif_reward);
        gif_reward.setMovieResource(R.drawable.gif_reward);

        tv_scrolltext = (MarqueeView) findViewById(R.id.tv_scrolltext);
        ll_scrolltext = (RelativeLayout) findViewById(R.id.ll_scrolltext);
        tv_close_scrollText = (TextView) findViewById(R.id.tv_close_scrollText);
        //引导
        tv_yindao_create = (TextView) findViewById(R.id.tv_yindao_create);
        tv_yindao_vip = (TextView) findViewById(R.id.tv_yindao_vip);

        fab_add_weichat = (FloatingActionButton) findViewById(R.id.fab_add_weichat);

        rcv_recyclerview = (RecyclerView) findViewById(R.id.rcv_recyclerview);

        pb_loading_app = (LinearLayout) findViewById(R.id.pb_loading_app);

        ll_network_request_failure = (LinearLayout) findViewById(R.id.ll_network_request_failure);

        tv_hidden_background = (MyHiddenTextView) findViewById(R.id.tv_hidden_background);

        my_scroll_vg = (MyVIPScrollView) findViewById(R.id.my_scroll_vg);

        rl_title_adv = (FrameLayout) findViewById(R.id.rl_title_adv);
        tioa_image = (ImageView) findViewById(R.id.tioa_image);
        rl_title_adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO TUIA_ADV_CLICK 推啊广告点击
                if (!TextUtils.isEmpty(tuiAAdvUrl) && !tuiAAdvUrl.equals("")) {
                    mNonStandardTm.adClicked();
                    if (updateMessageInfo == null) {
                        updateMessageInfo = APPUsersShareUtil.getUpdateMessage(mContext);
                    }
                    LBStat.click(22293);//推啊广告点击曝光统计tuia_click
                    MobclickAgent.onEvent(mContext, "tuia_click");
                    if (updateMessageInfo.isTuia_adv_isbrowser()) {
                        MobclickAgent.onEvent(mContext, "tuia_click");
                        Uri uri = Uri.parse(tuiAAdvUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(uri);
                        startActivity(intent);
                    } else {
                        TuiABrowserActivity.intentTuiABrowserA(mContext, tuiAAdvUrl);
                    }
                }
            }
        });
        mNonStandardTm = new NonStandardTm(this);
        mNonStandardTm.loadAd(3159);
        mNonStandardTm.setAdListener(new MyNsTmListener());

        iv_friend = (ImageViewFriendDot) findViewById(R.id.iv_friend);
        iv_friend.addDot();
        findViewById(R.id.fl_friend).setOnClickListener(view -> {
            iv_friend.clearDot();
            LBStat.click(StatisticsConstans.TJ_C_PYQ);
            LBStat.collect("广告", "小说");
            startActivity(new Intent(MainActivity.this, StoryActivity.class));
        });
        // TODO: 2017/12/14 屏蔽 小说
//        iv_friend.setVisibility(View.GONE);

        startCheck = new ListAppPresenterImpl(MainActivity.this, this);
        boolean isInstallApp = LauncherUtils.checkLauncherType(mContext, ListAppContract.SET_ALL);
        mPresenter.start(isInstallApp ? ListAppContract.SET_ALL : ListAppContract.SET_WEICHAT);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_recyclerview.setLayoutManager(llm);
        rvAdapter = new MakeApkRecyclerViewAdapter(this, this);
        rcv_recyclerview.setAdapter(rvAdapter);

        my_scroll_vg.setViewOpenStatuLisener(statuBon -> {
            tv_hidden_background.setChangeStatu(statuBon);
        });
//        findViewById(R.id.tv_create_weichat).setOnClickListener(view -> {
//
//            if (HistoryPayShareUtil.isWeixinAvilible(MainActivity.this)) {
//
//            } else {
//                Toast.makeText(MainActivity.this, "请先安装微信客户端", Toast.LENGTH_SHORT).show();
//            }
//        });
//        findViewById(R.id.tv_create_qq).setOnClickListener(view -> {
//            Toast.makeText(MainActivity.this, "QQ分身开发中，尽情期待！", Toast.LENGTH_SHORT).show();
//        });
        findViewById(R.id.iv_service).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, CustomerServiceActivity.class));
        });
        findViewById(R.id.iv_service).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
        findViewById(R.id.iv_common_problem).setOnClickListener(view -> {

        });
        findViewById(R.id.tv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareWcDialog(MainActivity.this);
            }
        });

        ll_network_request_failure.setOnClickListener(view -> {
            boolean isInstallAppC = LauncherUtils.checkLauncherType(mContext, ListAppContract.SET_ALL);
            mPresenter.start(isInstallAppC ? ListAppContract.SET_ALL : ListAppContract.SET_WEICHAT);
        });

        gif_reward.setGifOnClick(new GifView.GifClickCallBack() {
            @Override
            public void onClick() {
                LBStat.click(StatisticsConstans.TJ_C_DSDJ);
                MobclickAgent.onEvent(MainActivity.this, UMengStatisticalKey.reward_gif_click);
                myTipDialog = new MyTipDialog(MainActivity.this, MainActivity.this, payHelper);
                myTipDialog.show();

            }
        });
        updatePriceInfo = APPUsersShareUtil.getUpdateMessage(mContext);
        checkSuccessfulOrder();
        checkOrderlist();
    }

    UpdatePriceInfo updatePriceInfo;

    private void checkOrderlist() {

        ArrayList<OrderDBInfo> orderlist = sqliteDo.getProcessedOrderList();
        if (orderlist.size() > 0) {
            MobclickAgent.onEvent(mContext, UMengStatisticalKey.pay_service_show);
            payFailDialog.setCallVackLisener(new PayFailDialog.CallBackLisener() {
                @Override
                public void backstageConfirmed() {

                    for (OrderDBInfo info : orderlist) {
                        sqliteDo.changeNeedCheckStatus(info.getOrdernum(), "true");
                    }

                    if (NotificationManagerCompat.from(mContext).areNotificationsEnabled()) {
//                        Toast.makeText(mContext, getString(R.string.notification_error), Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(mContext, MyBackCheckService.class);
                    startService(intent);
                }

                @Override
                public void giveUp() {
                    MobclickAgent.onEvent(mContext, UMengStatisticalKey.pay_service_check_giveup);
                    //放弃所有订单
                    for (OrderDBInfo info : orderlist) {
                        sqliteDo.changeOrderstatus(info.getOrdernum(), 4);
                        sqliteDo.changeNeedCheckStatus(info.getOrdernum(), "false");
                    }
                }
            });
            payFailDialog.show();
        }
    }

    private void checkSuccessfulOrder() {
        ArrayList<OrderDBInfo> orderlist = sqliteDo.getSuccessfulOrderList();
        for (OrderDBInfo info : orderlist) {
            if (info.getFunctionkey().equals(AuxiliaryConstans.WX_LifeTime)) {
                APPUsersShareUtil.setLifeTime(mContext);
                MyBackCheckService.saveHistoryList(mContext, SPAuxiliaryPayUtils.getFunctionKeyName(info.getFunctionkey()),
                        String.valueOf(info.getOrderprice()),
                        info.getPaytype());
                sqliteDo.changeOrderstatus(info.getOrdernum(), 3);
                Toast.makeText(mContext, "您的订单后台已处理完成,请前往查看", Toast.LENGTH_SHORT).show();
                continue;
            }

            if (info.getFunctionkey().equals(AuxiliaryConstans.WX_Single)) {
                addWxMode(MyBackCheckService.getNowTag());
                //订单已完成
                sqliteDo.changeOrderstatus(info.getOrdernum(), 3);
                MyBackCheckService.saveHistoryList(mContext, SPAuxiliaryPayUtils.getFunctionKeyName(info.getFunctionkey()),
                        String.valueOf(info.getOrderprice()),
                        info.getPaytype());
                Toast.makeText(mContext, "您的订单后台已处理完成,请前往查看", Toast.LENGTH_SHORT).show();
                continue;
            }
        }
    }

    @Override
    public void startLoading() {
        showLoading();
    }

    @Override
    public void loadFinish(List<AppModel> models) {
        AppModel weiChatModel = null;
        if (models == null || models.size() <= 0) {
            MyToast.mCenterToast("未检查到微信应用,请安装后或重新进入！", mContext);
        }
        for (AppModel model : models) {
            if (model.packageName.equals("com.tencent.mm")) {
                weiChatModel = model;
                VApp.getApp().setWeiChatModel(model);
                break;
            }
        }
        if (weiChatModel == null) {
            MyToast.mCenterToast("未检查到微信应用,请安装后或重新进入！", mContext);
            return;
        }

        ArrayList<ApkItemInfo> weichatItemArray = new ArrayList<>();

        List<VUserInfo> userinfos = VUserManager.get().getUsers(false);

        List<AppUsersInfo> localUsers = APPUsersShareUtil.getUserList(this);

        if (localUsers != null&&userinfos!=null) {
            for (AppUsersInfo localUsersInfo : localUsers) {
                for (VUserInfo info : userinfos) {
                    try{
                        if (Integer.valueOf(localUsersInfo.getId()) == info.id){
                            ApkItemInfo apkItemInfo = new ApkItemInfo();
                            apkItemInfo.setTitle(localUsersInfo.getAppName());
                            apkItemInfo.setApkType(ApkItemInfo.WEICHAT_TYPE);
                            apkItemInfo.setAppModel(weiChatModel);
                            apkItemInfo.setCreateDate(HistoryPayShareUtil.getFormatedDateTime("yy-MM-dd", info.creationTime));
                            apkItemInfo.setUserInfo(info);
                            apkItemInfo.setOpenId(info.id);
                            apkItemInfo.setFreeTrialType(localUsersInfo.isFreeTrialType());
                            apkItemInfo.setCreateTiem(localUsersInfo.getCreateTiem());
                            weichatItemArray.add(apkItemInfo);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        rvAdapter.setWeiChatItems(weichatItemArray);


        UserHelper helper = new UserHelper(mContext);
        long longinDate = helper.getLoginDate();

        if (longinDate == 0 || (System.currentTimeMillis() - longinDate) / 1000 > 24 * 60 * 60) {//如果没有时间 或者当前时间 比上次登录时间多一天
            if (APPUsersShareUtil.checkVip(mContext)) {//如果不是vip就显示
                my_scroll_vg.firstViewLocation();
            }
        }
        helper.setLoginDate(System.currentTimeMillis());//记录下登录的时间
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            UserHelper uih = new UserHelper(mContext);
            if (!uih.getTjVersion().equals(info.versionName)) {
                countUtil.doInstallOrStartTj(VApp.TYPE_INSTALL);
            } else {
                countUtil.doInstallOrStartTj(VApp.TYPE_STARTUP);
            }
            if (longinDate <= 0 || !uih.getTjVersion().equals(info.versionName)) { // 第一次打开应用
                showKnownTipView();
            } else {
                tv_yindao_create.setVisibility(View.GONE);
                tv_yindao_vip.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_close_scrollText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_scrolltext.setVisibility(View.GONE);
            }
        });

        updatePriceInfo = APPUsersShareUtil.getUpdateMessage(mContext);
        if (updatePriceInfo.getTop_tip() != null && !updatePriceInfo.getTop_tip().equals("")) {
            tv_scrolltext.startWithText(updatePriceInfo.getTop_tip());
        } else {
            ll_scrolltext.setVisibility(View.GONE);
        }
        hideLoadin();
    }

    @Override
    public void loadVirtualApps(List<AppModel> models) {
        if (virtualAppsSpUtil == null)
            virtualAppsSpUtil = new VirtualAppsSpUtil(mContext);

        virtualAppsSpUtil.setVirtualApps(models, false);
        installAppsAdapter.setModels(virtualAppsSpUtil.getVirtualAppsIndex(models));
        pv_installApps.refreshView();
        fab_add_weichat.setVisibility(View.VISIBLE);
        pb_loading_app.setVisibility(View.GONE);
        rcv_recyclerview.setVisibility(View.GONE);
        pv_installApps.setVisibility(View.VISIBLE);
        my_scroll_vg.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadInstallAllApps(List<AppModel> models) {
        Log.i(TAG, "loadInstallAllApps: ");
        virtualAppsSpUtil = new VirtualAppsSpUtil(mContext);
        installAppsAdapter = new InstallAppsAdapter(mContext);
        pv_installApps = (PagerView) findViewById(R.id.pv_installApps);
        pv_installApps.setAdapter(installAppsAdapter);
        installAppsAdapter.setModels(virtualAppsSpUtil.getVirtualAppsIndex(models));
        pv_installApps.setOnDragChangeListener(isStart -> {
            if (isStart) {
                fab_add_weichat.setImageResource(R.mipmap.ic_crash);
                fab_add_weichat.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fe2d00")));
            } else {
                fab_add_weichat.setImageResource(R.mipmap.ic_add);
                fab_add_weichat.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#17c782")));
                List<AppModel> listdata = (List<AppModel>) pv_installApps.getAllMoveItems();
                virtualAppsSpUtil.setVirtualApps(listdata, true);
                for (int i = 0; i < listdata.size(); i++) {
                    Log.e(TAG, "loadInstallAllApps: " + listdata.get(i).name);
                }
                mPresenter.resetVirtualApps();
            }
        });
        pv_installApps.setOnItemClickListener((o, pos) -> {
            AppModel appModel = (AppModel) o;
            ItemVirtualAppsActivity.openVirtualAppArray(appModel, MainActivity.this);
        });
        pv_installApps.setOnCrashItemListener(position -> {
            Log.e(TAG, "onCrash: position=" + position);
            AppModel model = installAppsAdapter.getItem(position);
            new AlertDialog.Builder(mContext)
                    .setTitle("提示")
                    .setMessage("确定删除 " + model.name + " ?")
                    .setNeutralButton("确定", (dialogInterface, i1) -> {
                        mPresenter.deleteVirtualApp(model);
                        virtualAppsSpUtil.setVirtualApps((List<AppModel>) pv_installApps.getAllMoveItems(), true);
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        });
        fab_add_weichat.post(new Runnable() {
            @Override
            public void run() {
                int[] ints = new int[2];
                fab_add_weichat.getLocationInWindow(ints);
                pv_installApps.setBottomLine(ints[1], ints[0]);
            }
        });


        pv_installApps.refreshView();
        fab_add_weichat.setVisibility(View.VISIBLE);
        pb_loading_app.setVisibility(View.GONE);
        rcv_recyclerview.setVisibility(View.GONE);
        pv_installApps.setVisibility(View.VISIBLE);
        my_scroll_vg.setVisibility(View.VISIBLE);

        screenShotPreviews = new ArrayList<>();
        screenShotPreviews.addAll(APPUsersShareUtil.getScreenShotPreviewArray(mContext));
        ll_network_request_failure.setVisibility(View.GONE);

        tv_close_scrollText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_scrolltext.setVisibility(View.GONE);
            }
        });

        if (updatePriceInfo.getTop_tip() != null && !updatePriceInfo.getTop_tip().equals("")) {
            tv_scrolltext.startWithText(updatePriceInfo.getTop_tip());
        } else {
            ll_scrolltext.setVisibility(View.GONE);
        }
    }

    @Override
    public void shareCallBack(String ErrCode) {
        if (ErrCode.equals("ERR_OK")) {
            Toast.makeText(mContext, mResources.getString(R.string.share_success), Toast.LENGTH_SHORT).show();

            if (HistoryPayShareUtil.shareAddTime(mContext)) { // 判断今天是否已经加过时
                List<VUserInfo> userinfos = VUserManager.get().getUsers(false);
                long createTime = 0; //应用创建时间
                for (VUserInfo info : userinfos) {
                    if (info.id == 1) {
                        createTime = info.creationTime;
                    }
                }
                if (createTime != 0) {
                    UpdatePriceInfo updateMessageInfo = APPUsersShareUtil.getUpdateMessage(mContext);
                    long millisecond = (long) (Integer.valueOf(updateMessageInfo.getSysj()) * 60) * 1000; //正常试用时长时间
                    long addTime = (long) ((Integer.valueOf(updateMessageInfo.getShareTime()) * 60) * 60) * 1000; // 分享成功增加的时长
                    long mfsjTime = Long.valueOf((Integer.valueOf(updateMessageInfo.getMfSj() == null ? "0" : updateMessageInfo.getMfSj()) * 60) * 1000); // 试用时长 分钟转毫秒
                    long systemTime = System.currentTimeMillis(); // 系统当前时间

                    // 检查当前试用分身是否已经过期
                    if (systemTime - (createTime + mfsjTime) < 0) { //系统当前时间-（创建时间+可以免费试用的总时长） = 大于0 还没有过期 小于0 已过期
//                        int newSYSJ = Integer.valueOf(updateMessageInfo.getMfSj()) + (Integer.valueOf(updateMessageInfo.getShareTime()) * 60);
//                        updateMessageInfo.setMfSj(String.valueOf(newSYSJ));   // 存入免费试用时间  （分钟）
//                        APPUsersShareUtil.setUpdateMessage(mContext, updateMessageInfo);
                        return;
                    } else {
                        //已过期
                        long oldTime = systemTime - createTime; //已试用时间差 = 系统现在时间-应用创建时间
                        int newSYSJ = (int) ((oldTime + addTime) / 1000) / 60;
                        updateMessageInfo.setMfSj(String.valueOf(newSYSJ)); // 存入免费试用时间  （分钟）
                        APPUsersShareUtil.setUpdateMessage(mContext, updateMessageInfo);
                        HistoryPayShareUtil.addShareCount(mContext);
                        rvAdapter.refreshShareItem();
                    }
                }
            }
        } else {
            Toast.makeText(mContext, mResources.getString(R.string.share_failure), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void mHideLoadin() {
        pb_loading_app.setVisibility(View.GONE);
        rcv_recyclerview.setVisibility(View.VISIBLE);
        if (APPUsersShareUtil.isUserTag(mContext)) {
            addWxMode("反馈");

        }
    }

    public void addItem(ApkItemInfo info, int changeType) {
        rvAdapter.addApkItem(info, changeType);
        hideLoadin();
    }

    @Override
    public void appsinsertVirtuaFail() {

    }

    @Override
    public void installedAppList(List<AppModel> models) {
        for (AppModel model : models) {
            if (model.packageName.equals("com.tencent.mm")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        installedApp(model);
                    }
                }).start();
            }
//            if (model.packageName.equals("com.tencent.mobileqq")) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        installedApp(model);
//                    }
//                }).start();
//            }
        }
    }

//    private void addWeiChat(String apkName) {
//
//        MobclickAgent.onEvent(MainActivity.this, VApp.PaymentSuccess);
//        String tags = "";
//        mPresenter.addWeiChatApp(apkName, Float.toString(Float.parseFloat(payHelper.getNum())), HistoryPayShareUtil.getSystemTime(VApp.DATE_NOSECOND), false);
//        tags = "weixin";
//
//        HistoryPayInfo info = new HistoryPayInfo();
//        info.setPayDate(HistoryPayShareUtil.getSystemTime(VApp.DATE_NOSECOND));
//        info.setPayName(apkName);
//        info.setPayNumber(payHelper.getNum());
//        info.setPayType(payHelper.getPayType());
//        HistoryPayShareUtil.setHistoryPayList(mContext, info);
//
//        countUtil.doPayNumTj(true, payHelper.getorderNumber(), payHelper.getNum(), payHelper.getPayType(), tags);
//    }

    @Override
    public void deleteItem(ApkItemInfo apkItemInfo) {
        rvAdapter.removeItem(apkItemInfo);
        hideLoadin();
    }

    @Override
    public void startUpdateApp(UpdateInfo updateInfo) {
        updateMessageInfo = APPUsersShareUtil.getUpdateMessage(mContext);
        mDialog = new MyDialog(this, this, payHelper);
        if (updateMessageInfo == null || updateMessageInfo.getScfy() == null) {
            Toast.makeText(mContext, mContext.getString(R.string.lianjie_shibai), Toast.LENGTH_SHORT).show();
            return;
        }
        createWspayDialog();

        firstVipBaoyue = updateMessageInfo.getScfy();
        vipBaoyue = updateMessageInfo.getZcfy();
        mDialog.setDcNum((Float.parseFloat(firstVipBaoyue)));
        mDialog.setZsNum((Float.parseFloat(vipBaoyue)));

        if (updateInfo != null) {
            if (updateInfo.isUpdate()) {
                String address = updateInfo.getUpdateAddress();
                String version = updateInfo.getVersionCode();
                if (address != null && !address.isEmpty()) {
                    updateDialog = new UpdateDialog(this, version, address, updateInfo.isForceUpdate());
                    updateDialog.setCallback(this);
                    updateDialog.setmCanceledOnTouchOutside(updateInfo.isForceUpdate());
                    updateDialog.show();
                }
            }
        }
    }

    @Override
    public void netWorkRequestFailure() {
        pb_loading_app.setVisibility(View.GONE);
        ll_network_request_failure.setVisibility(View.VISIBLE);
    }

    @Override
    public void adapterItemDelete(ApkItemInfo apkItemInfo) {
        mPresenter.deleteItemApp(apkItemInfo);
    }

    /**
     * 添加一个微信
     */
    @Override
    public void addWeiChatBottom() {

        if (APPUsersShareUtil.chekIsLifeTime(mContext)) {
            new ApkNameDialog(MainActivity.this).dialog(null, new ApkNameDialog.ApkNameCallback() {
                @Override
                public void confirmCallback(String apkName) {
                    mPresenter.addWeiChatApp(apkName.toString(), "0", HistoryPayShareUtil.getSystemTime(VApp.DATE_NOSECOND), false);
                }
            });
        } else {
//          Toast.makeText(mContext,"////"+mDialog.getZsNum(),Toast.LENGTH_SHORT).show();
            if (mDialog==null)
                mDialog = new MyDialog(MainActivity.this,this,getHelper());

            mDialog.setApkName("微信多开分身");
            try {
//                mDialog.showDialog(ApkItemInfo.WEICHAT_TYPE);
                mDialog.showDialog(mDialog.getHomeDialogData());
            } catch (Exception e) {
                e.printStackTrace();
                mDialog.dismiss();
            }
        }
    }

    public void addWxMode(String tag) {
        APPUsersShareUtil.setUserTag(mContext);
        mPresenter.addWeiChatApp(mResources.getString(R.string.weixin_fenshen) + tag, Float.toString(Float.parseFloat(payHelper.getNum()) / 100), HistoryPayShareUtil.getSystemTime(VApp.DATE_NOSECOND), false);
    }

    @Override
    public void adapterItemAddDeskTop(ApkItemInfo apkItemInfo, int position) {
        mPresenter.addItemToDeskTop(apkItemInfo, position);
    }

    @Override
    public void clearWxModel() {
        new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage("是否清除微信创建数据重新加载?")
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    mPresenter.clearWxModel();
                })
                .setNegativeButton("取消", (dialogInterface, i) -> {
                }).show();
    }

    @Override
    public void setPresenter(ListAppContract.ListAppPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void destroy() {

    }

    /**
     * @param model
     */
    private void installedApp(AppModel model) {
        int flags = InstallStrategy.COMPARE_VERSION;
        if (model.fastOpen) {
            flags |= InstallStrategy.DEPEND_SYSTEM_IF_EXIST;
        }
        VirtualCore.get().installPackage(model.path, flags);

        InstalledAppInfo info = VirtualCore.get().getInstalledAppInfo(model.packageName, 0);

        if (info != null) {
//            if (info.dependSystem) {
//                myHandler.sendEmptyMessage(0);
////                mPresenter.dataChanged();
//                return;
//            }
            model.context = this;
//            ProgressDialog dialog = ProgressDialog.show(this, "Please wait", "Optimizing new Virtual App...");
            VUiKit.defer().when(() -> {
                try {
                    model.loadData(info.getApplicationInfo(VUserHandle.USER_OWNER));
                    VirtualCore.get().preOpt(info.packageName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).done((res) -> {
//                dialog.dismiss();
//                mPresenter.dataChanged();
                myHandler.sendEmptyMessage(0);
            });
        }
    }

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    installedNumber = 2;
                    if (installedNumber >= 2) {
                        if (APPUsersShareUtil.isFirstOpen(mContext)) {
                            mPresenter.addWeiChatApp(mResources.getString(R.string.mianfei_shiyong2), "0", HistoryPayShareUtil.getSystemTime(VApp.DATE_NOSECOND), true);
                        }
                        mPresenter.resetModel();
                    }
                    break;
                case 1:
                    break;
            }

        }
    };

    private void showLoading() {
        pb_loading_app.setVisibility(View.VISIBLE);
        rcv_recyclerview.setVisibility(View.INVISIBLE);
        my_scroll_vg.setVisibility(View.INVISIBLE);
        ll_network_request_failure.setVisibility(View.GONE);
    }

    private void hideLoadin() {
        screenShotPreviews = new ArrayList<>();
        screenShotPreviews.addAll(APPUsersShareUtil.getScreenShotPreviewArray(mContext));
        pb_loading_app.setVisibility(View.GONE);
        ll_network_request_failure.setVisibility(View.GONE);
        rcv_recyclerview.setVisibility(View.VISIBLE);
        my_scroll_vg.setVisibility(View.VISIBLE);
        gif_reward.setVisibility(View.VISIBLE);


    }
    @Override
    public void anginQuery() {

    }

    @Override
    public void successfulclick(String apkName, int type, int waretype) {
        DebugLog.e("支付成功");
        if (operationDB == null)
            operationDB = new OperationDB(this);
        //修改订单数据库 设置该笔订单的状态为支付完成 ,顺带获取该笔成功订单数据 做统计和开启功能操作
        ComsuptionInfo comsuptionInfo = operationDB.changePayComplateStatus(getHelper().getorderNumber(), DBLibs.Pay_status_Successful);
        LBStat.pay(comsuptionInfo.getPayType(),
                comsuptionInfo.getOrderNumber(),
                true,
                PayConstant.getPayDescribeStr(comsuptionInfo.getCommodityFunctionType()),
                Float.valueOf(comsuptionInfo.getPrice()),
                comsuptionInfo.getPayType());
        //开启对应功能
        handlingSeccessOrder(comsuptionInfo.getCommodityFunctionType(), false, comsuptionInfo);
    }

    @Override
    public void failClick() {
        DebugLog.e("支付失败");
        if (operationDB == null)
            operationDB = new OperationDB(this);
        handlingFailedOrders(operationDB.changePayComplateStatus(getHelper().getorderNumber(), DBLibs.Pay_status_Failure));
    }

    @Override
    public void exceprtion() {
        DebugLog.e("支付失败");
        if (operationDB == null)
            operationDB = new OperationDB(this);
        handlingFailedOrders(operationDB.changePayComplateStatus(getHelper().getorderNumber(), DBLibs.Pay_status_Failure));
    }


    public void anginQueryDialog() {
        angin_query_dialog = new AlertDialog.Builder(this).create();
        View view = LayoutInflater.from(this).inflate(R.layout.layout_anginquery, null);
        angin_query_dialog.setView(view);
        angin_query_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        angin_query_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        angin_query_dialog.setCanceledOnTouchOutside(false);
        angin_query_dialog.setCancelable(true);
        Button anginquery = (Button) view.findViewById(R.id.anginquery);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);
        //再次查询监听事件
        anginquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> map = SharedPreferencesUtils.getOrderInfo(MainActivity.this);
                String orderid = map.get("orderid").toString();
                String queryType = map.get("paytype").toString();
                int waretype = (int) map.get("waretype");
                String number = (String) map.get("number");
                SharedPreferencesUtils.saveOrderInfo(mContext, orderid, true, waretype, queryType, number);
                if (!orderid.isEmpty()) {
                    String requreUrl = "";
                    if (queryType.equals("weixin")) {
                        requreUrl = "http://api.duokai.mxitie.com/cgi/pay.ashx/order/info.json?" +
                                "orderno=" + orderid + "&paytype=weixin";
                    } else if (queryType.equals("alipay")) {
                        requreUrl = "http://api.duokai.mxitie.com/cgi/pay.ashx/order/info.json?" +
                                "orderno=" + orderid + "&paytype=alipay";
                    }
                    if (!requreUrl.isEmpty()) {
                        getHelper().setClick(MainActivity.this);
                        getHelper().setWaretype(waretype);
                        getHelper().setPayType(queryType);
                        getHelper().setNum(number);
                        getHelper().setorderNumber(orderid);
                        getHelper().requreHttpAsync(requreUrl, MainActivity.this);
                    }
                }

//                sqliteDo.changeNeedCheckStatus(getHelper().getorderNumber(), "true");//保存改订单为轮询状态到数据库
//                if (NotificationManagerCompat.from(mContext).areNotificationsEnabled()) {
//                    Toast.makeText(mContext, getString(R.string.notification_error), Toast.LENGTH_LONG).show();
//                }
//                Intent intent = new Intent(mContext, MyBackCheckService.class);
//                startService(intent);
                angin_query_dialog.dismiss();
            }
        });
        //再次查询dialog取消监听事件
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                sqliteDo.changeNeedCheckAndOrderStatus(getHelper().getorderNumber(), "false", 1);//确认订单失败

                Map<String, Object> map = SharedPreferencesUtils.getOrderInfo(MainActivity.this);
                String orderid = map.get("orderid").toString();
                String queryType = map.get("paytype").toString();
                int waretype = (int) map.get("waretype");
                String number = (String) map.get("number");
                SharedPreferencesUtils.saveOrderInfo(mContext, orderid, true, waretype, queryType, number);
                angin_query_dialog.dismiss();
            }
        });

        angin_query_dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
                    if (angin_query_dialog.isShowing()) {
                        Map<String, Object> map = SharedPreferencesUtils.getOrderInfo(MainActivity.this);
                        String orderid = map.get("orderid").toString();
                        String queryType = map.get("paytype").toString();
                        int waretype = (int) map.get("waretype");
                        String number = (String) map.get("number");
                        SharedPreferencesUtils.saveOrderInfo(mContext, orderid, true, waretype, queryType, number);
                    }
                }
//                if (angin_query_dialog.isShowing() && isCheckPayOrderShow) {
//                    angin_query_dialog.dismiss();
//                }
                angin_query_dialog.dismiss();
                return false;

            }
        });
    }

    //    CommonToast toast;
    @Override
    protected void onStart() {
        super.onStart();
        if (gif_reward.isPaused()) {
            gif_reward.setPaused(false);
        }
        isCheckPayOrderShow = true;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(500);
//                    myHandler.sendEmptyMessage(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }


    /**
     * 处理失败订单
     *
     * @param comsuptionInfo
     */
    public void handlingFailedOrders(ComsuptionInfo comsuptionInfo) {
        if (comsuptionInfo != null) {
            if (payFailureDialog == null)
                payFailureDialog = new PayFailureDialog(this);
            //填充订单数据
            payFailureDialog.setComsuptionInfo(comsuptionInfo);
            //开始处理失败订单
            payFailureDialog.failureOrderDialog();
            //设置处理回调监听
            payFailureDialog.setCallBack(new PayFailureDialog.PayFailureDialogCallback() {
                @Override
                public void seccess(ComsuptionInfo comsuptionInfo) {
                    handlingSeccessOrder(comsuptionInfo.getCommodityFunctionType(), false, comsuptionInfo);
                }
            });
        }
    }

    /**
     * 处理支付成功订单
     *
     * @param functionType
     * @param isBd         是否属于补单操作
     */
    public void handlingSeccessOrder(String functionType, boolean isBd, ComsuptionInfo comsuptionInfo) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle(isBd ? "补单成功" : "支付成功");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
        });
        builder.setNegativeButton("问题咨询", (dialogInterface, i) -> {
            // TODO: 2017/11/30 跳转客服投诉界面
            startActivity(new Intent(this, ComplaintsActivity.class));
        });

        if (operationDB == null)
            operationDB = new OperationDB(this);
        if (functionType.equals(PayConstant.Pay_S_WX_DG) || functionType.equals("WX_Single")) {
            if (comsuptionInfo == null) {
                mPresenter.addWeiChatApp("微信分身" + APPUsersShareUtil.getUserList(this).size(), "", "", false);
            } else {
                mPresenter.addWeiChatApp("微信分身" + APPUsersShareUtil.getUserList(this).size(), comsuptionInfo.getPrice(), comsuptionInfo.getInsertDate(), false);
            }

            builder.setMessage("微信分身已创建\n警告：支付成功后,不可卸载和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_WX_ZS) || functionType.equals("WX_LifeTime")) {
            APPUsersShareUtil.setLifeTime(this);
            builder.setMessage("支付成功！点击右下角创建微信分身;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_ALL_APPS) || functionType.equals("WX_AllApps")) {
            LauncherUtils.saveLauncherType(this, ListAppContract.SET_ALL);
            mPresenter.start(ListAppContract.SET_ALL);
            builder.setMessage("支付成功！点击右下角可选择需要多开得应用;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_SVIP) || functionType.equals("C_SVIP")) {
            SPAuxiliaryPayUtils.setSVIPPermission(this);
//            builder.setNegativeButton("去VIP界面查看", (dialogInterface, i) -> {
//                // TODO: 2017/11/30 跳转VIP界面
//                startActivity(new Intent(this,VIPActivity.class));
//            });
            builder.setMessage("支付成功！SVIP权限已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            tv_ffh_status.setText(mResources.getString(R.string.ws_gunction_status_true));
            iv_svip_status.setVisibility(View.GONE);
            tv_svip_status.setVisibility(View.VISIBLE);
            tv_svip_status.setText(mResources.getString(R.string.ws_gunction_status_true));

            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }
        if (functionType.equals(PayConstant.Pay_S_SVIP_JHY) || functionType.equals("C_JHTALL")) {
            SPAuxiliaryPayUtils.setWsAddFriendAll(this);
//            builder.setNegativeButton("去VIP界面查看", (dialogInterface, i) -> {
//                // TODO: 2017/11/30 跳转VIP界面
//                startActivity(new Intent(this,VIPActivity.class));
//            });
            builder.setMessage("支付成功！微信加好友功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }
        if (functionType.equals(PayConstant.Pay_S_SVIP_HF) || functionType.equals("C_ALLHF")) {
            SPAuxiliaryPayUtils.setWsFunctionHFAll(this);
//            builder.setNegativeButton("去VIP界面查看", (dialogInterface, i) -> {
//                // TODO: 2017/11/30 跳转VIP界面
//                startActivity(new Intent(this,VIPActivity.class));
//            });
            builder.setMessage("支付成功！微信自动回复功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }
        if (functionType.equals(PayConstant.Pay_S_SVIP_FFH) || functionType.equals("C_FFH")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_FFH);
//            builder.setNegativeButton("去VIP界面查看", (dialogInterface, i) -> {
//                // TODO: 2017/11/30 跳转VIP界面
//                startActivity(new Intent(this,VIPActivity.class));
//            });
            builder.setMessage("支付成功！微信防封号功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        //12.5新添加

        if (functionType.equals(PayConstant.Pay_S_WX_LQ) || functionType.equals("C_LINGQIAN")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_LINGQIAN);
            builder.setMessage("支付成功！微信转账生成器功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }


        if (functionType.equals(PayConstant.Pay_S_WX_ZZ) || functionType.equals("C_WX_ZZ")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_TIXIAN);
            builder.setMessage("支付成功！微信提现生成器功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_WX_HB) || functionType.equals("C_HONGBAO")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_HONGBAO);
            builder.setMessage("支付成功！微信红包生成器功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }


        if (functionType.equals(PayConstant.Pay_S_WX_PYQ) || functionType.equals("C_PY_QUAN")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_PY_QUAN);
            builder.setMessage("支付成功！微信朋友圈生成器功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_WX_DH) || functionType.equals("C_DUIHUA")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_DUIHUA);
            builder.setMessage("支付成功！微信聊天对话生成器功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_WX_QL) || functionType.equals("C_QUNLIAO")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_QUNLIAO);
            builder.setMessage("支付成功！微信群聊生成器功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }


        if (functionType.equals(PayConstant.Pay_S_ZFB_HB) || functionType.equals("C_ZFB_HB")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_ZFB_HB);
            builder.setMessage("支付成功！支付宝红包生成器功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_ZFB_TX) || functionType.equals("C_ZFB_TX")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_ZFB_TX);
            builder.setMessage("支付成功！支付宝提现生成器功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_ZFB_YE) || functionType.equals("C_ZFB_YE")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_ZFB_YE);
            builder.setMessage("支付成功！支付宝零钱生成器功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_ZFB_ZZ) || functionType.equals("C_ZFB_ZZ")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_ZFB_ZZ);
            builder.setMessage("支付成功！支付宝转账生成器功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }


        if (functionType.equals(PayConstant.Pay_S_QQ_HB) || functionType.equals("C_QQ_HB")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_QQ_HB);
            builder.setMessage("支付成功！QQ红包生成器功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_QQ_YE) || functionType.equals("C_QQ_YE")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_QQ_YE);
            builder.setMessage("支付成功！QQ提现生成器功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_QQ_QB) || functionType.equals("C_QQ_QB")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_QQ_QB);
            builder.setMessage("支付成功！QQ零钱生成器功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_QQ_ZZ) || functionType.equals("C_QQ_ZZ")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_QQ_ZZ);
            builder.setMessage("支付成功！QQ转账生成器功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }


        if (functionType.equals(PayConstant.Pay_S_WX_TX) || functionType.equals("C_TIXIAN")) {
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(this, AuxiliaryConstans.C_TIXIAN);
            builder.setMessage("支付成功！微信提现生成器功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }


        if (functionType.equals(PayConstant.Pay_S_DA_SHANG)) {
            builder.setMessage("非常感谢您对本软件的支持！我们会继续努力！");
            builder.setNegativeButton("取消", (dialogInterface, i) -> {
            });
            if (comsuptionInfo != null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
        }

    }


    @Override
    public void bdHandling(String bdCode) {
        handlingSeccessOrder(bdCode, true, null);
    }


    //二次查询
    private void exitAnginQuery() {
        Map<String, Object> map = SharedPreferencesUtils.getOrderInfo(MainActivity.this);
        boolean status = (boolean) map.get("orderstatus");
        String orderid = map.get("orderid").toString();
        if (status == false && !orderid.isEmpty()) {
            angin_query_dialog.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!my_scroll_vg.viewLocationbln) { //微商功能界面处于顶端  点击返回键 回到底部
            my_scroll_vg.setViewLocationbln(true);
            return true;
        } else {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                ExitAppDialog exitAppDialog = new ExitAppDialog(mContext,MainActivity.this);
                exitAppDialog.setExitAppCallBack(new ExitAppDialog.ExitAppCallBack() {
                    @Override
                    public void exit() {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// ???
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                });
                exitAppDialog.show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void update() {
        Toast.makeText(mContext, mResources.getString(R.string.download_description), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void later() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        isCheckPayOrderShow = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
            if (data == null) return;
            String resultStr = data.getStringExtra("share");
            if (resultStr.equals("ERR_OK")) {
                Log.i(TAG, "onActivityResult: true");
            } else {
                Log.i(TAG, "onActivityResult: false");
            }
        }
        if (resultCode == RESULT_OK && requestCode == VCommends.REQUEST_SELECT_APP && data != null) {
            mPresenter.resetVirtualApps();
        }
    }

//    findViewById(R.id.rl_imag_wx_qunliao).setOnClickListener(this);
//    findViewById(R.id.rl_imag_wx_zfb_hb).setOnClickListener(this);
//    findViewById(R.id.rl_imag_wx_zfb_tixian).setOnClickListener(this);
//    findViewById(R.id.rl_imag_wx_zfb_lingqian).setOnClickListener(this);
//    findViewById(R.id.rl_imag_wx_zfb_zhuanzhang).setOnClickListener(this);
//    findViewById(R.id.rl_imag_wx_qq_hb).setOnClickListener(this);
//    findViewById(R.id.rl_imag_wx_qq_tixian).setOnClickListener(this);
//    findViewById(R.id.rl_imag_wx_qq_lingqian).setOnClickListener(this);
//    findViewById(R.id.rl_imag_wx_qq_zhuanzhang).setOnClickListener(this);

    private void findSmallMerchantView() {
        tv_svip_status = (TextView) findViewById(R.id.tv_svip_status);
        tv_qgg_status = (TextView) findViewById(R.id.tv_qgg_status);
        tv_ffh_status = (TextView) findViewById(R.id.tv_ffh_status);
        iv_svip_status = (ImageView) findViewById(R.id.iv_svip_status);
        fl_ffh_is_visibility = (LinearLayout) findViewById(R.id.fl_ffh_is_visibility);
        fl_hb_is_visibility = (LinearLayout) findViewById(R.id.fl_hb_is_visibility);
        ll_qgg_is_visibility = (LinearLayout) findViewById(R.id.ll_qgg_is_visibility);
        ll_jhy_visibility = (LinearLayout) findViewById(R.id.ll_jhy_visibility);
        ll_zdhf_isvisibility = (LinearLayout) findViewById(R.id.ll_zdhf_isvisibility);
        if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_QGG)) {
            tv_qgg_status.setText(mResources.getString(R.string.ws_gunction_status_true));
        }
        if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_FFH)) {
            tv_ffh_status.setText(mResources.getString(R.string.ws_gunction_status_true));
        }
        if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_SVIP)) {
            iv_svip_status.setVisibility(View.GONE);
            tv_svip_status.setVisibility(View.VISIBLE);
            tv_svip_status.setText(mResources.getString(R.string.ws_gunction_status_true));
        }
        findViewById(R.id.rl_hongbao).setOnClickListener(this);
        findViewById(R.id.rl_huifu).setOnClickListener(this);
        findViewById(R.id.rl_imag_tixian).setOnClickListener(this);
        findViewById(R.id.rl_imag_lingqian).setOnClickListener(this);
        findViewById(R.id.rl_imag_hongbao_sc).setOnClickListener(this);
        findViewById(R.id.rl_imag_pengyouquan).setOnClickListener(this);
        findViewById(R.id.rl_imag_wx_duihua).setOnClickListener(this);
        findViewById(R.id.rl_add_friends).setOnClickListener(this);
        findViewById(R.id.rl_svip_function).setOnClickListener(this);
        findViewById(R.id.rl_qgg_function).setOnClickListener(this);
        findViewById(R.id.rl_ffh_function).setOnClickListener(this);
        findViewById(R.id.rl_imag_wx_qunliao).setOnClickListener(this);
        findViewById(R.id.rl_imag_wx_zfb_hb).setOnClickListener(this);
        findViewById(R.id.rl_imag_wx_zfb_tixian).setOnClickListener(this);
        findViewById(R.id.rl_imag_wx_zfb_lingqian).setOnClickListener(this);
        findViewById(R.id.rl_imag_wx_zfb_zhuanzhang).setOnClickListener(this);
        findViewById(R.id.rl_imag_wx_qq_hb).setOnClickListener(this);
        findViewById(R.id.rl_imag_wx_qq_tixian).setOnClickListener(this);
        findViewById(R.id.rl_imag_wx_qq_lingqian).setOnClickListener(this);
        findViewById(R.id.rl_imag_wx_qq_zhuanzhang).setOnClickListener(this);
        findViewById(R.id.rl_imag_wx_zz).setOnClickListener(this);

        findViewById(R.id.tv_preview_tixian).setOnClickListener(this);
        findViewById(R.id.tv_preview_lingqian).setOnClickListener(this);
        findViewById(R.id.tv_preview_hongbao).setOnClickListener(this);
        findViewById(R.id.tv_preview_dongtai).setOnClickListener(this);
        findViewById(R.id.tv_preview_duihua).setOnClickListener(this);
        findViewById(R.id.tv_preview_qunliao).setOnClickListener(this);
        findViewById(R.id.tv_preview_zfb_hb).setOnClickListener(this);
        findViewById(R.id.tv_preview_zfb_tixian).setOnClickListener(this);
        findViewById(R.id.tv_preview_zfb_lingqian).setOnClickListener(this);
        findViewById(R.id.tv_preview_zfb_zhuanzhang).setOnClickListener(this);
        findViewById(R.id.tv_preview_qq_hb).setOnClickListener(this);
        findViewById(R.id.tv_preview_qq_tixian).setOnClickListener(this);
        findViewById(R.id.tv_preview_qq_lingqian).setOnClickListener(this);
        findViewById(R.id.tv_preview_qq_zhuanzhang).setOnClickListener(this);
        findViewById(R.id.tv_preview_wx_zz).setOnClickListener(this);
    }

    private void createWspayDialog() {
        wsPayDialog = new WsScreenFunctionPayDialog(this, this, payHelper);

        if (updateMessageInfo == null) {
            updateMessageInfo = APPUsersShareUtil.getUpdateMessage(mContext);
        }

        ll_zdhf_isvisibility.setVisibility(updateMessageInfo.isZdhf() || SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_ALLHF) ? View.VISIBLE : View.GONE);
        ll_jhy_visibility.setVisibility(updateMessageInfo.isJhy() || SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_JHYALL) ? View.VISIBLE : View.GONE);
//        ll_qgg_is_visibility.setVisibility(SPAuxiliaryPayUtils.isQggIndex(mContext) || SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_QGG) ? View.VISIBLE : View.GONE);
        fl_ffh_is_visibility.setVisibility(updateMessageInfo.isFfh() || SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_FFH) ? View.VISIBLE : View.GONE);
//        fl_hb_is_visibility.setVisibility(updateMessageInfo.isWxqhb() || SPAuxiliaryPayUtils.isVisibilyHBFunction(mContext) ? View.VISIBLE : View.GONE);
        fl_hb_is_visibility.setVisibility(View.GONE);

        wsPayDialog.setSvipPrice(Float.valueOf(updateMessageInfo.getWeichat_svip_price()));
        wsPayDialog.setSvipPrice(true);
//        wsPayDialog.setaPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_dg_price()));//9.9
//        wsPayDialog.setAllPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_all_price()));//28.8
//        wsPayDialog.setDialogTitleName(mResources.getString(R.string.weichat_ws_screen_function));
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_preview_wx_zz:
                LBStat.click(StatisticsConstans.TJ_C_PREVIEW);
                mPresenter.showPreviewDialog(screenShotPreviews, AuxiliaryConstans.C_WX_ZZ);
                break;
            case R.id.tv_preview_tixian:
                LBStat.click(StatisticsConstans.TJ_C_PREVIEW);
                mPresenter.showPreviewDialog(screenShotPreviews, AuxiliaryConstans.C_TIXIAN);
                break;
            case R.id.tv_preview_lingqian:
                LBStat.click(StatisticsConstans.TJ_C_PREVIEW);
                mPresenter.showPreviewDialog(screenShotPreviews, AuxiliaryConstans.C_LINGQIAN);
                break;
            case R.id.tv_preview_hongbao:
                LBStat.click(StatisticsConstans.TJ_C_PREVIEW);
                mPresenter.showPreviewDialog(screenShotPreviews, AuxiliaryConstans.C_HONGBAO);
                break;
            case R.id.tv_preview_dongtai:
                LBStat.click(StatisticsConstans.TJ_C_PREVIEW);
                mPresenter.showPreviewDialog(screenShotPreviews, AuxiliaryConstans.C_PY_QUAN);
                break;
            case R.id.tv_preview_duihua:
                LBStat.click(StatisticsConstans.TJ_C_PREVIEW);
                mPresenter.showPreviewDialog(screenShotPreviews, AuxiliaryConstans.C_DUIHUA);
                break;
            case R.id.tv_preview_qunliao:
                LBStat.click(StatisticsConstans.TJ_C_PREVIEW);
                mPresenter.showPreviewDialog(screenShotPreviews, AuxiliaryConstans.C_QUNLIAO);
                break;
            case R.id.tv_preview_zfb_hb:
                LBStat.click(StatisticsConstans.TJ_C_PREVIEW);
                mPresenter.showPreviewDialog(screenShotPreviews, AuxiliaryConstans.C_ZFB_HB);
                break;
            case R.id.tv_preview_zfb_tixian:
                LBStat.click(StatisticsConstans.TJ_C_PREVIEW);
                mPresenter.showPreviewDialog(screenShotPreviews, AuxiliaryConstans.C_ZFB_TX);
                break;
            case R.id.tv_preview_zfb_lingqian:
                LBStat.click(StatisticsConstans.TJ_C_PREVIEW);
                mPresenter.showPreviewDialog(screenShotPreviews, AuxiliaryConstans.C_ZFB_YE);
                break;
            case R.id.tv_preview_zfb_zhuanzhang:
                LBStat.click(StatisticsConstans.TJ_C_PREVIEW);
                mPresenter.showPreviewDialog(screenShotPreviews, AuxiliaryConstans.C_ZFB_ZZ);
                break;
            case R.id.tv_preview_qq_hb:
                LBStat.click(StatisticsConstans.TJ_C_PREVIEW);
                mPresenter.showPreviewDialog(screenShotPreviews, AuxiliaryConstans.C_QQ_HB);
                break;
            case R.id.tv_preview_qq_tixian:
                LBStat.click(StatisticsConstans.TJ_C_PREVIEW);
                mPresenter.showPreviewDialog(screenShotPreviews, AuxiliaryConstans.C_QQ_YE);
                break;
            case R.id.tv_preview_qq_lingqian:
                LBStat.click(StatisticsConstans.TJ_C_PREVIEW);
                mPresenter.showPreviewDialog(screenShotPreviews, AuxiliaryConstans.C_QQ_QB);
                break;
            case R.id.tv_preview_qq_zhuanzhang:
                LBStat.click(StatisticsConstans.TJ_C_PREVIEW);
                mPresenter.showPreviewDialog(screenShotPreviews, AuxiliaryConstans.C_QQ_ZZ);
                break;
            case R.id.rl_svip_function:


                LBStat.click(StatisticsConstans.TJ_C_SVIP);
                if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_SVIP)) {

                } else {
                    if (wsPayDialog == null)
                        return;
                    wsPayDialog.setAllPrice(false); // 设置不显示支付全部功能的ItemView
                    wsPayDialog.isSvipDialog = true;
                    wsPayDialog.isSvipOne = true;
//                    wsPayDialog.setAllPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_all_price()));
                    wsPayDialog.setSaveCommondityKey(AuxiliaryConstans.C_SVIP);
//                    wsPayDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.wx_addf_main_allpay),String.valueOf(wsPayDialog.getaPrice())));
                    wsPayDialog.setDialogTitleName(mResources.getString(R.string.weichat_svip_all_function_title));
                    wsPayDialog.showDialog(PayConstant.Pay_S_SVIP);
                    wsPayDialog.isSvipDialog = false;
                    wsPayDialog.isSvipOne = false;
                }
                break;
            case R.id.rl_qgg_function:
                LBStat.click(StatisticsConstans.TJ_C_QGG);
                if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_QGG)) {

                } else {
                    if (wsPayDialog == null)
                        return;
                    wsPayDialog.setAllPrice(false); // 设置不显示支付全部功能的ItemView
                    wsPayDialog.isSvipDialog = true;
                    wsPayDialog.setaPrice(Float.valueOf(updateMessageInfo.getWeichat_qgg_price()));
//                    wsPayDialog.setAllPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_all_price()));
                    wsPayDialog.setSaveCommondityKey(AuxiliaryConstans.C_QGG);
                    wsPayDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.weichat_qgg_function), String.valueOf(wsPayDialog.getaPrice())));
                    wsPayDialog.setDialogTitleName(mResources.getString(R.string.weichat_qgg_function_title));
//                    wsPayDialog.showDialog();
                }
                break;
            case R.id.rl_ffh_function:
                LBStat.click(StatisticsConstans.TJ_C_FFH);
                if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_FFH)) {

                } else {
                    if (wsPayDialog == null)
                        return;
                    wsPayDialog.setAllPrice(false); // 设置不显示支付全部功能的ItemView
                    wsPayDialog.isSvipDialog = true;
                    wsPayDialog.setaPrice(Float.valueOf(updateMessageInfo.getWeichat_ffh_price()));
//                    wsPayDialog.setAllPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_all_price()));
                    wsPayDialog.setSaveCommondityKey(AuxiliaryConstans.C_FFH);
                    wsPayDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.weichat_ffh_function), String.valueOf(wsPayDialog.getaPrice())));
                    wsPayDialog.setDialogTitleName(mResources.getString(R.string.weichat_ffh_function_title));
                    wsPayDialog.showDialog(PayConstant.Pay_S_SVIP_FFH);
                }
                break;
            case R.id.rl_add_friends: //微信一键添加好友
                LBStat.click(StatisticsConstans.TJ_C_JHY);
                if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_HDJHY) || SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_QUJHY)) {
                    startActivity(new Intent(MainActivity.this, AddFriendsFunctionA.class));
                } else {
                    if (wsPayDialog == null)
                        return;
                    wsPayDialog.setAllPrice(false); // 设置不显示支付全部功能的ItemView
                    wsPayDialog.isSvipDialog = true;
                    wsPayDialog.setaPrice(Float.valueOf(updateMessageInfo.getWeichat_jf_all_price()));//9.9
//                    wsPayDialog.setAllPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_all_price()));//28.8
                    wsPayDialog.setSaveCommondityKey(AuxiliaryConstans.C_JHYALL);
                    wsPayDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.wx_addf_main_allpay), String.valueOf(wsPayDialog.getaPrice())));
                    wsPayDialog.setDialogTitleName(mResources.getString(R.string.wx_addf_main_allpay_title));
                    wsPayDialog.showDialog(PayConstant.Pay_S_SVIP_JHY);
                }
                break;
            case R.id.rl_hongbao: //微信自动抢红包
                LBStat.click(StatisticsConstans.TJ_C_WXQHB);
                startActivity(new Intent(MainActivity.this, HBActivity.class));
                break;
            case R.id.rl_huifu:
                LBStat.click(StatisticsConstans.TJ_C_ZDHF);
                if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_XPHF) || SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_LYHF)) {
                    startActivity(new Intent(MainActivity.this, HFActivity.class));
                } else {
                    if (wsPayDialog == null)
                        return;
                    wsPayDialog.setAllPrice(false);
                    wsPayDialog.isSvipDialog = true;
                    wsPayDialog.setaPrice(Float.valueOf(updateMessageInfo.getWeichat_hf_all_price()));//9.9
//                    wsPayDialog.setAllPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_all_price()));//28.8
                    wsPayDialog.setSaveCommondityKey(AuxiliaryConstans.C_ALLHF);
                    wsPayDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.wx_hf_main_allpay), String.valueOf(wsPayDialog.getaPrice())));
                    wsPayDialog.setDialogTitleName(mResources.getString(R.string.wx_hf_main_allpay_title));
                    wsPayDialog.showDialog(PayConstant.Pay_S_SVIP_HF);
                }
                break;
            case R.id.rl_imag_tixian:  // 微信提现生成器
                LBStat.click(StatisticsConstans.TJ_C_TXSCQ);
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_TIXIAN)) {
                    if (wsPayDialog == null)
                        return;
                    wsPayDialog.setAllPrice(false);
                    wsPayDialog.isSvipDialog = true;
                    wsPayDialog.setaPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_dg_price()));//9.9
                    wsPayDialog.setAllPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_all_price()));//28.8
                    wsPayDialog.setSaveCommondityKey(AuxiliaryConstans.C_TIXIAN);
                    wsPayDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.pay_ws_funciton_tixian), String.valueOf(wsPayDialog.getaPrice())));
                    wsPayDialog.setDialogTitleName(mResources.getString(R.string.weichat_name_tixian));
                    wsPayDialog.showDialog(PayConstant.Pay_S_WX_TX);
                } else {
                    intent = new Intent(MainActivity.this, WxWithDrawals.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_imag_lingqian: // 微信零钱生成器
                LBStat.click(StatisticsConstans.TJ_C_LQSCQ);
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_LINGQIAN)) {
                    if (wsPayDialog == null)
                        return;
                    wsPayDialog.setAllPrice(false);
                    wsPayDialog.isSvipDialog = true;
                    wsPayDialog.setaPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_dg_price()));//9.9
                    wsPayDialog.setAllPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_all_price()));//28.8
                    wsPayDialog.setDialogTitleName(mResources.getString(R.string.weichat_ws_screen_function));
                    wsPayDialog.setSaveCommondityKey(AuxiliaryConstans.C_LINGQIAN);
                    wsPayDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.pay_ws_funciton_lingqian), String.valueOf(wsPayDialog.getaPrice())));
                    wsPayDialog.showDialog(PayConstant.Pay_S_WX_LQ);
                } else {
                    intent = new Intent(MainActivity.this, WxPocketMoney.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_imag_hongbao_sc: // 微信红包生成器
                LBStat.click(StatisticsConstans.TJ_C_HBSCQ);
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_HONGBAO)) {
                    if (wsPayDialog == null)
                        return;
                    wsPayDialog.setAllPrice(false);
                    wsPayDialog.isSvipDialog = true;
                    wsPayDialog.setaPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_dg_price()));//9.9
                    wsPayDialog.setAllPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_all_price()));//28.8
                    wsPayDialog.setDialogTitleName(mResources.getString(R.string.weichat_ws_screen_function));
                    wsPayDialog.setSaveCommondityKey(AuxiliaryConstans.C_HONGBAO);
                    wsPayDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.pay_ws_funciton_hongbao), String.valueOf(wsPayDialog.getaPrice())));
                    wsPayDialog.showDialog(PayConstant.Pay_S_WX_HB);
                } else {
                    intent = new Intent(MainActivity.this, WxRedPacket.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_imag_pengyouquan: // 微信朋友圈
                LBStat.click(StatisticsConstans.TJ_C_PYQSCQ);
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_PY_QUAN)) {
                    if (wsPayDialog == null)
                        return;
                    wsPayDialog.setAllPrice(false);
                    wsPayDialog.isSvipDialog = true;
                    wsPayDialog.setaPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_dg_price()));//9.9
                    wsPayDialog.setAllPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_all_price()));//28.8
                    wsPayDialog.setDialogTitleName(mResources.getString(R.string.weichat_ws_screen_function));
                    wsPayDialog.setSaveCommondityKey(AuxiliaryConstans.C_PY_QUAN);
                    wsPayDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.pay_ws_funciton_pyquan), String.valueOf(wsPayDialog.getaPrice())));
                    wsPayDialog.showDialog(PayConstant.Pay_S_WX_PYQ);
                } else {
                    intent = new Intent(MainActivity.this, WxFriendCircle.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_imag_wx_duihua: // 微信对话生成器
                LBStat.click(StatisticsConstans.TJ_C_DHSCQ);
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_DUIHUA)) {
                    if (wsPayDialog == null)
                        return;
                    wsPayDialog.setAllPrice(false);
                    wsPayDialog.isSvipDialog = true;
                    wsPayDialog.setaPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_dg_price()));//9.9
                    wsPayDialog.setAllPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_all_price()));//28.8
                    wsPayDialog.setSaveCommondityKey(AuxiliaryConstans.C_DUIHUA);
                    wsPayDialog.setSingleSelectorStr(String.format(mResources.getString(R.string.pay_ws_funciton_duihua), String.valueOf(wsPayDialog.getaPrice())));
                    wsPayDialog.setDialogTitleName(mResources.getString(R.string.weichat_name_duihua));
                    wsPayDialog.showDialog(PayConstant.Pay_S_WX_DH);
                } else {
                    intent = new Intent(MainActivity.this, Wxtalk.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_imag_wx_qunliao://微信群聊
                LBStat.click(StatisticsConstans.TJ_C_WXQLSCQ);
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_QUNLIAO)) {
                    showWsJTPayDialog(PayConstant.Pay_S_WX_QL,
                            mResources.getString(R.string.weichat_wx_qunliao),
                            mResources.getString(R.string.weichat_wx_qunliao_title));
                } else {
                    intent = new Intent(MainActivity.this, WxMultiTalk.class);
                    startActivity(intent);
                }

                break;
            case R.id.rl_imag_wx_zfb_hb://支付宝红包
                LBStat.click(StatisticsConstans.TJ_C_ZFBHBSCQ);
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_ZFB_HB)) {
                    showWsJTPayDialog(PayConstant.Pay_S_ZFB_HB,
                            mResources.getString(R.string.weichat_zfb_hb),
                            mResources.getString(R.string.weichat_zfb_hb_title));
                } else {
                    intent = new Intent(MainActivity.this, ZfbRedPacket.class);
                    startActivity(intent);
                }

                break;
            case R.id.rl_imag_wx_zfb_tixian://支付宝提现
                LBStat.click(StatisticsConstans.TJ_C_ZFBTXSCQ);
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_ZFB_TX)) {
                    showWsJTPayDialog(PayConstant.Pay_S_ZFB_TX,
                            mResources.getString(R.string.weichat_zfb_tx),
                            mResources.getString(R.string.weichat_zfb_tx_title));
                } else {
                    intent = new Intent(MainActivity.this, ZfbWithDrawable.class);
                    startActivity(intent);
                }

                break;
            case R.id.rl_imag_wx_zfb_lingqian://支付宝余额
                LBStat.click(StatisticsConstans.TJ_C_ZFBYESCQ);
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_ZFB_YE)) {
                    showWsJTPayDialog(PayConstant.Pay_S_ZFB_YE,
                            mResources.getString(R.string.weichat_zfb_lq),
                            mResources.getString(R.string.weichat_zfb_lq_title));
                } else {
                    intent = new Intent(MainActivity.this, ZfbBalance.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_imag_wx_zfb_zhuanzhang://支付宝转账
                LBStat.click(StatisticsConstans.TJ_C_ZFBZZSCQ);
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_ZFB_ZZ)) {
                    showWsJTPayDialog(PayConstant.Pay_S_ZFB_ZZ,
                            mResources.getString(R.string.weichat_zfb_zz),
                            mResources.getString(R.string.weichat_zfb_zz_title));
                } else {
                    intent = new Intent(MainActivity.this, ZfbTransferAccount.class);
                    startActivity(intent);
                }

                break;
            case R.id.rl_imag_wx_qq_hb://QQ红包
                LBStat.click(StatisticsConstans.TJ_C_QQHBSCQ);
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_QQ_HB)) {
                    showWsJTPayDialog(PayConstant.Pay_S_QQ_HB,
                            mResources.getString(R.string.weichat_qq_hb),
                            mResources.getString(R.string.weichat_qq_hb_title));
                } else {
                    intent = new Intent(MainActivity.this, QQRedPacket.class);
                    startActivity(intent);
                }

                break;
            case R.id.rl_imag_wx_qq_tixian://QQ余额
                LBStat.click(StatisticsConstans.TJ_C_QQYESCQ);
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_QQ_YE)) {
                    showWsJTPayDialog(PayConstant.Pay_S_QQ_YE,
                            mResources.getString(R.string.weichat_qq_tx),
                            mResources.getString(R.string.weichat_qq_tx_title));
                } else {
                    intent = new Intent(MainActivity.this, QQBalance.class);
                    startActivity(intent);
                }

                break;
            case R.id.rl_imag_wx_qq_lingqian://QQ钱包
                LBStat.click(StatisticsConstans.TJ_C_QQQBSCQ);
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_QQ_QB)) {
                    showWsJTPayDialog(PayConstant.Pay_S_QQ_QB,
                            mResources.getString(R.string.weichat_qq_lq),
                            mResources.getString(R.string.weichat_qq_lq_title));
                } else {
                    intent = new Intent(MainActivity.this, QQMOneyPacket.class);
                    startActivity(intent);
                }

                break;
            case R.id.rl_imag_wx_qq_zhuanzhang://QQ转账
                LBStat.click(StatisticsConstans.TJ_C_QQZZSCQ);
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_QQ_ZZ)) {
                    showWsJTPayDialog(PayConstant.Pay_S_QQ_ZZ,
                            mResources.getString(R.string.weichat_qq_zz),
                            mResources.getString(R.string.weichat_qq_zz_title));
                } else {
                    intent = new Intent(MainActivity.this, QQWithDrawable.class);
                    startActivity(intent);
                }

                break;
            case R.id.rl_imag_wx_zz:
                LBStat.click(StatisticsConstans.TJ_C_WXZZSCQ);
                if (!SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mContext, AuxiliaryConstans.C_WX_ZZ)) {
                    showWsJTPayDialog(PayConstant.Pay_S_WX_ZZ,
                            mResources.getString(R.string.weichat_wx_zz),
                            mResources.getString(R.string.weichat_wx_zz_title));
                } else {
                    intent = new Intent(MainActivity.this, TransferAccounts.class);
                    startActivity(intent);
                }
                break;
        }
    }

    public void showWsJTPayDialog(String functionKey, String priceStr, String functionName) {
        if (wsPayDialog == null)
            return;
        wsPayDialog.setAllPrice(false);
        wsPayDialog.isSvipDialog = true;
        wsPayDialog.setaPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_dg_price()));//9.9
        wsPayDialog.setAllPrice(Float.valueOf(updateMessageInfo.getWeichat_jt_all_price()));//28.8
        wsPayDialog.setSaveCommondityKey(functionKey);
        wsPayDialog.setSingleSelectorStr(String.format(priceStr, String.valueOf(wsPayDialog.getaPrice())));
        wsPayDialog.setDialogTitleName(functionName);
        wsPayDialog.showDialog(functionKey);
    }

    private void downLoadDialog(String downLoadUrl) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(mContext)
                .setTitle(mResources.getString(R.string.ti_shi))
                .setMessage(mResources.getString(R.string.download_taost))
                .setPositiveButton(mResources.getString(R.string.que_ding), (dialogInterface, i) -> {
                    downloadApk(downLoadUrl);
                })
                .setCancelable(true)
                .setNegativeButton(mResources.getString(R.string.qu_xiao), (dialogInterface, i) -> {

                });
        alertDialog.create();
        alertDialog.show();
    }

    private void downloadApk(String downloadUrl) {
        Toast.makeText(mContext, mContext.getString(R.string.download_apk), Toast.LENGTH_SHORT).show();
        MyDownloadManager manager = new MyDownloadManager(mContext);
        manager.download(downloadUrl, mContext.getResources().getString(R.string.download_apk_title), mContext.getResources().getString(R.string.download_apk));
    }

    @Override
    protected void onDestroy() {
        if (mNonStandardTm != null) {
            mNonStandardTm.destroy();
        }
        super.onDestroy();
        mPresenter.onDestroy();
    }

    public void showKnownTipView() {

        i = 0;
        mHightLight = new HighLight(this)//
                .anchor(findViewById(R.id.activity_main))//如果是Activity上增加引导层，不需要设置anchor
                .enableNext()//开启next模式并通过show方法显示 然后通过调用next()方法切换到下一个提示布局，直到移除自身
                .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
                .setClickCallback(new HighLight.OnClickCallback() {
                    @Override
                    public void onClick() {
                        if (mHightLight.isShowing() && mHightLight.isNext())//如果开启next模式
                        {
                            mHightLight.next();
                            i++;
                        }
                        if (i == 3) {
                            tv_yindao_create.setVisibility(View.GONE);
                            tv_yindao_vip.setVisibility(View.GONE);
                        }
                    }
                })
//                .anchor(findViewById(R.id.id_container))//如果是Activity上增加引导层，不需要设置anchor
//                .addHighLight(R.id.tv_share, R.layout.yindao_share, new OnBottomPosCallback(5), new RectLightShape())
                .addHighLight(R.id.fl_friend, R.layout.yindao_friend, new HighLight.OnPosCallback() {
                    @Override
                    public void getPos(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.leftMargin = rectF.left;
                        marginInfo.topMargin = rectF.top + rectF.height();
                    }
                }, new RectLightShape()).addHighLight(R.id.tv_yindao_create, R.layout.yindao_create, new HighLight.OnPosCallback() {
                    @Override
                    public void getPos(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.leftMargin = rectF.left;
                        marginInfo.topMargin = rectF.top + rectF.height();
                    }
                }, new RectLightShape())
                .addHighLight(R.id.tv_yindao_vip, R.layout.yindao_vip, new HighLight.OnPosCallback() {
                    @Override
                    public void getPos(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.leftMargin = rectF.left;
                        marginInfo.bottomMargin = bottomMargin + rectF.height();
                    }
                }, new RectLightShape());
        mHightLight.show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (gif_reward != null)
            gif_reward.setPaused(true);
    }

    public String tuiAAdvUrl = "";

    /**
     * 推啊 广告数据返回监听
     */
    public class MyNsTmListener implements NsTmListener {

        @Override
        public void onReceiveAd(String s) {
            Log.e(TAG, "onReceiveAd: " + s);
            try {
                JSONObject advJo = new JSONObject(s);
                tuiAAdvUrl = advJo.getString("click_url");
//                String imageUrl = advJo.getString("img_url");
//                if (!TextUtils.isEmpty(imageUrl))
//                    Picasso.with(mContext).load(imageUrl).into(tioa_image);
                mNonStandardTm.adExposed();
                rl_title_adv.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailedToReceiveAd() {
        }
    }

}
