package com.zhushou.weichat;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.PowerManager;

import com.db.ta.sdk.TaSDK;
import com.lbsw.stat.LBStat;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.ServiceManagerNative;
import com.lody.virtual.client.stub.StubManifest;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.CacheType;
import com.okhttplib.cookie.PersistentCookieJar;
import com.okhttplib.cookie.cache.SetCookieCache;
import com.okhttplib.cookie.persistence.SharedPrefsCookiePersistor;
import com.umeng.analytics.MobclickAgent;
import com.zhushou.weichat.addfriends.ui.AddFriendsFunctionA;
import com.zhushou.weichat.auxiliary.ui.HBActivity;
import com.zhushou.weichat.auxiliary.ui.HFActivity;
import com.zhushou.weichat.constant.AppDebug;
import com.zhushou.weichat.ui.MainActivity;
import com.zhushou.weichat.ui.models.AppModel;
import com.zhushou.weichat.ui.pay.CrashHandler;
import com.zhushou.weichat.uinew.fragment.HomeFragment;
import com.zhushou.weichat.uinew.interfac.WxShareListener;
import com.zhushou.weichat.utils.AuthLoginListener;
import com.zhushou.weichat.utils.CountUtil;
import com.zhushou.weichat.utils.MyExceptionHandler;

import jonathanfinerty.once.Once;

/**
 * Created by Administrator on 2017/1/9.
 */

public class VApp extends BaseApplication {
    private static final java.lang.String TAG = "VApp";
    private static VApp application;
    public static AuthLoginListener authLoginListener;
    public static WxShareListener wxShareListener;

    public static VApp getInstance(){
        if (application == null){
            application = new VApp();
        }
        return application;
    }

    public static boolean IS_DEBUG = false;
    public static final String Share_WeiChat_User = "share.weichat.user";
    public static final String Share_QQ_User = "share.qq.user";

    public  static final String PAY_WIIPAY = "wiipay";
    public  static final String PAY_BFB = "bfb";
    public  static final String PAY_WFT = "wft";
    public  static final String PAY_WAP = "wap";
    public  static final String PAY_WAPLLQ = "wap_llq";
    public  static final String PAY_WXSC = "wxsc";
    public static final String PAY_BFBSCAN = "bfbscan";
    public static final String PAY_WXSDK = "wxsdk";
    public static final String SCAN_PNG = "bfbscan.jpg";

    private MainActivity mainActivity = null;
    private HomeFragment homeFragment = null;

    private AppModel weiChatModel = null;

    private AppModel QQModel = null;

    public AppModel getQQModel() {
        return QQModel;
    }

    public void setQQModel(AppModel QQModel) {
        this.QQModel = QQModel;
    }

    public AppModel getWeiChatModel() {
        return weiChatModel;
    }

    public void setWeiChatModel(AppModel weiChatModel) {
        this.weiChatModel = weiChatModel;
    }


    public static final String PAY_URL = "http://api.duokai.mxitie.com/cgi/pay.ashx/pay.do?"; // 支付下单baseurl
    public static final String PAY_CHECK_URL = "http://api.duokai.mxitie.com/cgi/pay.ashx/order/info.json?"; // 支付查询baseurl
    public static final String URLCX_PAY = "http://boxconfig.mxitie.com/open/duokai.wxdk.json"; // 支付方式 获取

//    public static final String ADV_TYPE = "http://boxconfig.mxitie.com/open/duokai.weibo.update.json"; //广告配置获取
    public static final String ADV_TYPE = "http://boxconfig.mxitie.com/open/duokai.adv.json"; //广告配置获取

    public final static String TJURL = "http://004.mxitie.com/count.do?sc=";
    private final static String SECURITYKEY = "youxun^&*($#@$";
    public final static String TYPE_INSTALL = "install";
    public final static String TYPE_PAYMENT = "pay_count";
    public final static String TYPE_STARTUP = "start";
    public static final String PAYTYPE_WX = "weixin";
    public static final String PAYTYPE_ZFB = "alipay";
    public static final String PAYTYPE_QQ = "qqpay";
    public static final String PAY_XGSDK = "xgsdk";
    public  static final String PAY_TLWAP = "tlwap";
    public static final String PAY_DIANWU = "dianwu";
    public static final int DATE_NOSECOND = 0x00;
    public static final int DATE_HMS = 0x01; //时分秒 精确到秒

    //友盟 点击计数 ID
    public static final String ClickStartApp = "start_weichat_app"; //分身启动点击
    public static final String ClickMakeApp = "click_make_app"; // 制作分身点击
    public static final String WeiChatPay = "weichat_pay"; //微信支付
    public static final String AliPay = "AliPay"; //支付宝支付
    public static final String PaymentSuccess = "PaymentSuccess";//支付成功
    public static final String ShareApp = "share_app"; // 分享点击
    public static final String ShareChenggong = "share_chenggong";//分享成功
    public static final String PayDanGe = "Pay_DanGe"; // 单个支付成功
    public static final String PayZhongShen = "Pay_ZhongShen"; //终身支付成功

    //OPPO广告SDK appId
    public static final String OPPO_ADV_APPID = "3543761";
    //微信分享 wxc14f8f05a1603031
    public static  String APP_ID = "";
    public static  String WXAPP_ID = "wxc14f8f05a1603031";//微信多开助手
//    public static  String WXAPP_ID = "wx251eb8f1335da387";//多开神器
    public static  String WXAPP_SECRET = "d523c35f68900e6bfd88ea2b7ad4c18b"; // 微信多开助手

//    public static  String WXAPP_SECRET = "cec5977e779c8a65153433c207b58562"; //多开神器

    public static final int WXSceneSession = 0;//发给朋友
    public static final int WXSceneTimeline = 1;//发到朋友圈
    public static final int WXSceneFavorite = 2;//微信收藏

    //后台统计
    public static final String TJ_QDFS = "1000";//分身启动点击
    public static final String TJ_FX = "1004";//分享点击
    public static final String TJ_FX_CG = "1005";//分享成功
    public static final String TJ_PAY_WEICHAT = "1002";//统计微信支付
    public static final String TJ_PAY_ALI = "1003";//支付宝支付
    public static final String TJ_PAY_ZS = "1007";//终身微信商品支付成功
    public static final String TJ_PAY_DG = "1006";//单个微信商品支付成功
    public static final String TJ_HB = "1011";//微信抢红包下载点击
    public static final String TJ_SYYYDK = "20953";//所有应用多开支付成功

    //统计商品支付描述
    public static final String SP_DG = "dgwxfs";//单个微信分身
    public static final String SP_ZS = "zswxfs";//终身微信分身
    public static final String SP_syyydk = "syyydk";//所有应用多开
    public static final String SP_QHB = "wxqhb";//微信自动抢红包
    public static final String SP_REWARD = "rewardpay";//打赏支付

    private static VApp gDefault;

    public static VApp getApp() {
        return gDefault;
    }

    public static String getSecurityKey() {
        return SECURITYKEY;
    }

    @Override
    protected void attachBaseContext(Context base) {
        StubManifest.STUB_CP_AUTHORITY = BuildConfig.APPLICATION_ID + "." + StubManifest.STUB_DEF_AUTHORITY;
        ServiceManagerNative.SERVICE_CP_AUTH = BuildConfig.APPLICATION_ID + "." + ServiceManagerNative.SERVICE_DEF_AUTH;
        //
        VirtualCore.get().setComponentDelegate(new MyComponentDelegate());
        super.attachBaseContext(base);
        try {
            VirtualCore.get().startup(base);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate() {
        gDefault = this;
        application = this;
        super.onCreate();

        IS_DEBUG = new AppDebug(this).IS_DEBUG; //获取应用当前运行模式
        setAppId(this.getResources().getString(R.string.wx_appid));
        CountUtil countUtil = new CountUtil(this);
        MyExceptionHandler.create(this);

        //公司统计接入
        LBStat.init(this, "dksq", countUtil.getApp(), countUtil.getFrom(), "009.mxitie.com");

        //友盟统计接入
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        TaSDK.init(this);

        if (VirtualCore.get().isMainProcess()){
            Once.initialise(this);
//            installGms();
        } else if (VirtualCore.get().isVAppProcess()){
            VirtualCore.get().setPhoneInfoDelegate(new MyPhoneInfoDelegate());
        }

        OkHttpUtil.init(this)
                .setConnectTimeout(10)//连接超时时间
                .setWriteTimeout(10)//写超时时间
                .setReadTimeout(10)//读超时时间
                .setMaxCacheSize(10 * 1024 * 1024)//缓存空间大小
//                .setCacheLevel(CacheLevel.FIRST_LEVEL)//缓存等级
                .setCacheType(CacheType.FORCE_NETWORK)//缓存类型
                .setShowHttpLog(false)//显示请求日志
                .setShowLifecycleLog(false)//显示Activity销毁日志
                .setRetryOnConnectionFailure(false)//失败后不自动重连
                .setCookieJar(new PersistentCookieJar(new SetCookieCache(),
                        new SharedPrefsCookiePersistor(this)))//持久化cookie
                .build();

        //鑫广支付初始化
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(getApplicationContext());

        /**
         * OPPO
         *打开sdk日志开关，便于排查问题；在应用发布正式版本的时候请屏蔽掉这行代码。
         */
//        MobAdManager.getInstance(getApplicationContext()).enableDebugLog();

//        MobAdManager.getInstance(getApplicationContext()).init(VApp.OPPO_ADV_APPID);

    }

    @Override
    public void onTerminate(){
        MobclickAgent.onKillProcess(this);
        // 程序终止的时候执行
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        MobclickAgent.onKillProcess(this);
        // 低内存的时候执行
        super.onLowMemory();
    }
    @Override
    public void onTrimMemory(int level) {
        MobclickAgent.onKillProcess(this);
        // 程序在内存清理的时候执行
        super.onTrimMemory(level);
    }

    public MainActivity getMainActivity(){
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /**
     * 锁屏解锁 点亮 熄灭 屏幕 相关
     */
    public  KeyguardManager.KeyguardLock kl = null;
    public PowerManager.WakeLock wl = null;
    private DevicePolicyManager policyManager; // 设备管理器
    private boolean isLockScreenStatus = true;  //监听屏幕是否处于打开状态


    public KeyguardManager.KeyguardLock getKl() {
        return kl;
    }

    public void setKl(KeyguardManager.KeyguardLock kl) {
        this.kl = kl;
    }

    public PowerManager.WakeLock getWl() {
        return wl;
    }

    public void setWl(PowerManager.WakeLock wl) {
        this.wl = wl;
    }

    public DevicePolicyManager getPolicyManager() {
        return policyManager;
    }

    public void setPolicyManager(DevicePolicyManager policyManager) {
        this.policyManager = policyManager;
    }

    public boolean isLockScreenStatus() {
        return isLockScreenStatus;
    }

    public void setLockScreenStatus(boolean lockScreenStatus) {
        isLockScreenStatus = lockScreenStatus;
    }

    private MainActivity a;
    public void setaActivity(MainActivity mainActivity) {
        this.a = mainActivity;
    }
    public MainActivity getaActivity() {
        return a;
    }

    private HFActivity hfActivity;
    public HFActivity getHfActivity() {
        return hfActivity;
    }
    public void setHfActivity(HFActivity hfActivity) {
        this.hfActivity = hfActivity;
    }
    public void removeHFActivity(){
        hfActivity = null;
    }

    private AddFriendsFunctionA addFriendsFunctionA;

    public AddFriendsFunctionA getaddFriendsFunctionA() {
        return addFriendsFunctionA;
    }

    public void setaddFriendsFunctionA(AddFriendsFunctionA addFriendsFunctionA) {
        this.addFriendsFunctionA = addFriendsFunctionA;
    }
    public  void removeAddFriendsFunctionA(){
        this.addFriendsFunctionA = null;
    }

    private HBActivity hbActivity;

    public HBActivity getHbActivity() {
        return hbActivity;
    }
    public void setHbActivity(HBActivity hbActivity) {
        this.hbActivity = hbActivity;
    }
    public void removeHbActivity(){
        this.hbActivity = null;
    }

    public static String getAppId() {
        return APP_ID;
    }

    public static void setAppId(String appId) {
        APP_ID = appId;
    }

    public void setAuthLoginListener(AuthLoginListener authLoginListener) {
//        if (authLoginListener instanceof AuthorizeLoginActivity){
        this.authLoginListener = authLoginListener;
//        }

    }

    public void setWxShareListener(WxShareListener wxShareListener){
        this.wxShareListener = wxShareListener;
    }

    public HomeFragment getHomeFragment() {
        return homeFragment;
    }

    public void setHomeFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }
}
