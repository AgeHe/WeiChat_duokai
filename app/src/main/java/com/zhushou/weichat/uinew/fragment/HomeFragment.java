package com.zhushou.weichat.uinew.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.db.ta.sdk.NonStandardTm;
import com.db.ta.sdk.NsTmListener;
import com.lbsw.stat.LBStat;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.VCommends;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryConstans;
import com.zhushou.weichat.auxiliary.sp.SPAuxiliaryPayUtils;
import com.zhushou.weichat.base.BasePayFragment;
import com.zhushou.weichat.bean.AppUsersInfo;
import com.zhushou.weichat.bean.UpdateInfo;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.constant.PayConstant;
import com.zhushou.weichat.ui.ComplaintsActivity;
import com.zhushou.weichat.ui.InstallAppsActivity;
import com.zhushou.weichat.ui.ListAppContract;
import com.zhushou.weichat.ui.dialog.ApkNameDialog;
import com.zhushou.weichat.ui.dialog.MyTipDialog;
import com.zhushou.weichat.ui.dialog.ShareWcDialog;
import com.zhushou.weichat.ui.models.AppModel;
import com.zhushou.weichat.ui.pay.MyDialog;
import com.zhushou.weichat.ui.pay.PayHelper;
import com.zhushou.weichat.ui.view.GifView;
import com.zhushou.weichat.uinew.ExceptionOrdersActivity;
import com.zhushou.weichat.uinew.VIPActivity;
import com.zhushou.weichat.uinew.adapter.HomeRecyclerViewAdapter;
import com.zhushou.weichat.uinew.dialog.PayFailureDialog;
import com.zhushou.weichat.uinew.info.AdInfo;
import com.zhushou.weichat.uinew.info.ComsuptionInfo;
import com.zhushou.weichat.uinew.info.HomeItemInfo;
import com.zhushou.weichat.uinew.interfac.HomeAppContract;
import com.zhushou.weichat.uinew.interfac.WxShareListener;
import com.zhushou.weichat.uinew.widget.RecyclerSpace;
import com.zhushou.weichat.utils.DebugLog;
import com.zhushou.weichat.utils.DisplayUtil;
import com.zhushou.weichat.utils.HistoryPayShareUtil;
import com.zhushou.weichat.utils.LauncherUtils;
import com.zhushou.weichat.utils.MyToast;
import com.zhushou.weichat.utils.VirtualAppsSpUtil;
import com.zhushou.weichat.utils.database.DBLibs;
import com.zhushou.weichat.utils.database.OperationDB;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;
import com.zhushou.weichat.utils.sp.AppAdvShareUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class HomeFragment extends BasePayFragment implements HomeAppContract.AppView ,View.OnClickListener,PayHelper.RequreClick,WxShareListener{

    private HomeRecyclerViewAdapter homeRecyclerViewAdapter; //列表适配器
    GridLayoutManager gridLayoutManager; //Recycler LayoutManager
    private HomeFragmentImpl homeFragmentImpl;
    ArrayList<HomeItemInfo> weichatItemArray;//界面列表数据
    boolean isInstallApp = false;//是否已经开启所有应用多开
    private MyDialog myDialog;//支付弹窗
    OperationDB operationDB;//支付信息数据库  用于对支付订单操作
    PayFailureDialog payFailureDialog; //订单失败处理弹窗
    private NonStandardTm mNonStandardTm; //推啊广告加载
    private MyTipDialog myTipDialog; //打赏支付弹窗

    @Override
    protected View init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    RecyclerView rv_home_content;
    LinearLayout pb_loading_app; //loading 界面加载
    LinearLayout ll_network_request_failure; // 加载异常/网络异常
    GifView gif_reward;

    @Override
    public void initView(View view) {
        rv_home_content = (RecyclerView) view.findViewById(R.id.rv_home_content);
        pb_loading_app = (LinearLayout) view.findViewById(R.id.pb_loading_app);
        ll_network_request_failure = (LinearLayout) view.findViewById(R.id.ll_network_request_failure);
        gif_reward = (GifView) view.findViewById(R.id.gif_reward);
        view.findViewById(R.id.iv_home_service).setOnClickListener(this); // 客服按钮
        view.findViewById(R.id.iv_home_share).setOnClickListener(this);//分享
        view.findViewById(R.id.iv_floating).setOnClickListener(this); // 添加按钮
        ImageView iv_home_service = (ImageView) view.findViewById(R.id.iv_home_service);
        iv_home_service.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                homeFragmentImpl.clearWxAppModel();
                return false;
            }
        });
    }

    @Override
    public void initData() {
        VApp.getInstance().setWxShareListener(this);
        VApp.getInstance().setHomeFragment(this);
        homeFragmentImpl = new HomeFragmentImpl(mActivity, this);
        homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(getActivity());
        gif_reward.setMovieResource(R.drawable.gif_reward);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemType = rv_home_content.getAdapter().getItemViewType(position);
                if (itemType == HomeRecyclerViewAdapter.TopType) {
                    return 3;
                }
                return 1;
            }
        });

        rv_home_content.setLayoutManager(gridLayoutManager);
        RecyclerSpace recyclerSpace = new RecyclerSpace(DisplayUtil.dip2px(mActivity,1));
        recyclerSpace.setColor(R.color.text_dark);
        rv_home_content.addItemDecoration(recyclerSpace);
        rv_home_content.setAdapter(homeRecyclerViewAdapter);

        isInstallApp = LauncherUtils.checkLauncherType(mActivity, ListAppContract.SET_ALL);
        homeFragmentImpl.start(isInstallApp ? ListAppContract.SET_ALL : ListAppContract.SET_WEICHAT);

        mNonStandardTm = new NonStandardTm(mActivity);

        gif_reward.setGifOnClick(new GifView.GifClickCallBack() {
            @Override
            public void onClick() {
                if (myTipDialog==null)
                    myTipDialog = new MyTipDialog(mActivity,HomeFragment.this,getHelper());
                myTipDialog.show();
            }
        });
        ll_network_request_failure.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_home_service:
                // TODO: 2017/12/1 跳转客服界面
                startActivity(new Intent(mActivity, ComplaintsActivity.class));
                break;
            case R.id.iv_home_share:
                new ShareWcDialog(mActivity);
                break;
            case R.id.iv_floating:
                //判断是否已开启所有应用多开
                if (LauncherUtils.checkLauncherType(mActivity, ListAppContract.SET_ALL)){
                    Intent intent = new Intent(mActivity, InstallAppsActivity.class);
                    startActivityForResult(intent, VCommends.REQUEST_SELECT_APP);
                }else{
                    //判断是否已购买无限制微信多开
                    if (APPUsersShareUtil.chekIsLifeTime(mActivity)){
                        new ApkNameDialog(mActivity).dialog(null, new ApkNameDialog.ApkNameCallback() {
                            @Override
                            public void confirmCallback(String apkName) {
                                homeFragmentImpl.addWeiChatApp(apkName.toString(), false);
                            }
                        });
                    }else{
                        if (myDialog==null)
                            myDialog = new MyDialog(mActivity,this,getHelper());
                        try {
                            myDialog.showDialog(myDialog.getHomeDialogData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            break;
            case R.id.ll_network_request_failure:
                isInstallApp = LauncherUtils.checkLauncherType(mActivity, ListAppContract.SET_ALL);
                homeFragmentImpl.start(isInstallApp ? ListAppContract.SET_ALL : ListAppContract.SET_WEICHAT);
                break;
        }
    }


    /**
     * 试用过期弹窗专用
     */
    public void shareShowPayDialog(){

        if (!LauncherUtils.checkLauncherType(mActivity, ListAppContract.SET_ALL)&&!APPUsersShareUtil.chekIsLifeTime(mActivity)){
            if (myDialog==null)
                myDialog = new MyDialog(mActivity,this,getHelper());
            try {
                myDialog.showDialog(myDialog.getHomeDialogData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

   @Override
    public void setPresenter(ListAppContract.ListAppPresenter presenter) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void startLoading() {
        pb_loading_app.setVisibility(View.VISIBLE);
        rv_home_content.setVisibility(View.GONE);
        ll_network_request_failure.setVisibility(View.GONE);
    }

    /**
     * 微信model 全局用  会暂保存在VPP Application中 便于应用内部拿取 /**调用时需做空处理 以防报null异常
     */
    AppModel weiChatModel = null;

    @Override
    public void loadFinish(List<AppModel> models) {

        if (weichatItemArray==null)
            weichatItemArray = new ArrayList<>();
        if (weichatItemArray.size()>0)
            weichatItemArray.clear();
        //创建VIP选项
        HomeItemInfo isVipHomeitemInfo = new HomeItemInfo();
        isVipHomeitemInfo.setVip(true);
        weichatItemArray.add(isVipHomeitemInfo);
        //添加item广告
        weichatItemArray.addAll(AppAdvShareUtil.getAppItemAdv(mActivity));
        //向Adapter添加Banner广告数据
        homeRecyclerViewAdapter.setBannerData(AppAdvShareUtil.getBannerAdv(mActivity));

        if (models == null || models.size() <= 0) {
            MyToast.mCenterToast("未检查到微信应用,请安装后或重新进入！", mActivity);
        }
        for (AppModel model : models) {
            if (model.packageName.equals("com.tencent.mm")) {
                weiChatModel = model;
                VApp.getApp().setWeiChatModel(model); // 保存到VPP application
                break;
            }
        }
        if (weiChatModel == null) {
            MyToast.mCenterToast("未检查到微信应用,请安装后或重新进入！", mActivity);
            return;
        }

        //获取本地已创建的用户列表
        List<VUserInfo> userinfos = VUserManager.get().getUsers(false);
        //取本地已创建的微信分身列表
        List<AppUsersInfo> localUsers = APPUsersShareUtil.getUserList(mActivity);
        if (localUsers != null||localUsers.size()<1) {
            for (AppUsersInfo localUsersInfo : localUsers) {
                for (VUserInfo info : userinfos) {
                    if (Integer.valueOf(localUsersInfo.getId()) == info.id&&!localUsersInfo.isFreeTrialType()) {
                        HomeItemInfo apkItemInfo = new HomeItemInfo();
                        apkItemInfo.setTitle(localUsersInfo.getAppName());
                        apkItemInfo.setAppModel(weiChatModel);
                        apkItemInfo.setUserInfo(info);
                        apkItemInfo.setOpenId(info.id);
                        apkItemInfo.setFreeTrialType(localUsersInfo.isFreeTrialType());
                        apkItemInfo.setCreateTiem(localUsersInfo.getCreateTiem());
                        //添加分身数据到Array
                        weichatItemArray.add(apkItemInfo);
                    }
                    if (Integer.valueOf(localUsersInfo.getId()) == info.id&&localUsersInfo.isFreeTrialType()){
                        HomeItemInfo apkItemInfo = new HomeItemInfo();
                        apkItemInfo.setTitle(localUsersInfo.getAppName());
                        apkItemInfo.setAppModel(weiChatModel);
                        apkItemInfo.setUserInfo(info);
                        apkItemInfo.setOpenId(info.id);
                        apkItemInfo.setFreeTrialType(localUsersInfo.isFreeTrialType());
                        apkItemInfo.setCreateTiem(localUsersInfo.getCreateTiem());
                        addItem(apkItemInfo);
                    }
                }
            }
        }
        if (localUsers!=null&&localUsers.size()<1){//第一次打开应用，创建一个免费试用分身
            homeFragmentImpl.addWeiChatApp("免费分身",true);
        }
        //将数据填充到Adapter
        homeRecyclerViewAdapter.setAllArrayData(weichatItemArray);

        //加载推啊广告
        if (mNonStandardTm==null)
            mNonStandardTm = new NonStandardTm(mActivity);
        mNonStandardTm.loadAd(3159);
        mNonStandardTm.setAdListener(new MyNsTmListener());

        if (gif_reward.getVisibility()==View.GONE)
            gif_reward.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadVirtualApps(List<AppModel> models) {

    }



    private VirtualAppsSpUtil virtualAppsSpUtil;
    /**
     * 所有应用多开
     * @param models
     */
    @Override
    public void loadInstallAllApps(List<AppModel> models) {
        mHideLoadin();
        if (weichatItemArray==null)
            weichatItemArray = new ArrayList<>();
        if (weichatItemArray.size()>0)
            weichatItemArray.clear();
        //创建VIP选项
        HomeItemInfo isVipHomeitemInfo = new HomeItemInfo();
        isVipHomeitemInfo.setVip(true);
        weichatItemArray.add(isVipHomeitemInfo);
        //添加item广告
        weichatItemArray.addAll(AppAdvShareUtil.getAppItemAdv(mActivity));
        //向Adapter添加Banner广告数据
        homeRecyclerViewAdapter.setBannerData(AppAdvShareUtil.getBannerAdv(mActivity));
        //获取用户已创建的多开应用的appmodel
        virtualAppsSpUtil = new VirtualAppsSpUtil(mActivity);
        List<AppModel> createApps = virtualAppsSpUtil.getVirtualAppsIndex(models);
        //将appmodel转成我们界面通用的实体类
        for (AppModel appModel:createApps){
            HomeItemInfo homeItemInfo = new HomeItemInfo();
            homeItemInfo.setAppModel(appModel);
            homeItemInfo.setLauncherType(HomeItemInfo.Launcher_ALL_APP);
            homeItemInfo.setIcon(appModel.icon);
            homeItemInfo.setFreeTrialType(false);
            homeItemInfo.setPackageName(appModel.packageName);
            homeItemInfo.setTitle(appModel.name);
            weichatItemArray.add(homeItemInfo);
        }
        homeRecyclerViewAdapter.setAllArrayData(weichatItemArray);


        //把首页免费微信拿出来， 因为所有应用多开打开之后在首页不需要拉取微信分身列表，免费微信分身的数据需要单独处理下
        //获取本地已创建的用户列表
        List<VUserInfo> userinfos = VUserManager.get().getUsers(false);
        //取本地已创建的微信分身列表
        List<AppUsersInfo> localUsers = APPUsersShareUtil.getUserList(mActivity);
        if (localUsers != null||localUsers.size()<1) {
            for (AppUsersInfo localUsersInfo : localUsers) {
                for (VUserInfo info : userinfos) {
                    if (Integer.valueOf(localUsersInfo.getId()) == info.id&&localUsersInfo.isFreeTrialType()){
                        HomeItemInfo apkItemInfo = new HomeItemInfo();
                        apkItemInfo.setTitle(localUsersInfo.getAppName());
                        apkItemInfo.setAppModel(weiChatModel);
                        apkItemInfo.setUserInfo(info);
                        apkItemInfo.setOpenId(info.id);
                        apkItemInfo.setFreeTrialType(localUsersInfo.isFreeTrialType());
                        apkItemInfo.setCreateTiem(localUsersInfo.getCreateTiem());
                        addItem(apkItemInfo);//将免费分身列表添加至界面RecyclerView adapter
                    }
                }
            }
        }
        //加载推啊广告
        if (mNonStandardTm==null)
            mNonStandardTm = new NonStandardTm(mActivity);
        mNonStandardTm.loadAd(3133);
        mNonStandardTm.setAdListener(new MyNsTmListener());
        //打赏按钮
        if (gif_reward.getVisibility()==View.GONE)
            gif_reward.setVisibility(View.VISIBLE);
    }

    @Override
    public void mHideLoadin() {
        pb_loading_app.setVisibility(View.GONE);
        rv_home_content.setVisibility(View.VISIBLE);
        ll_network_request_failure.setVisibility(View.GONE);
    }

    @Override
    public void addItem(HomeItemInfo info) {
        if (info.isFreeTrialType()){
            if (info.getAppModel()==null&&VApp.getInstance().getWeiChatModel()!=null){
                info.setAppModel(weiChatModel!=null?weiChatModel:VApp.getInstance().getWeiChatModel());
            }
            homeRecyclerViewAdapter.setFreeApkItemInfo(info);
        }else{
            homeRecyclerViewAdapter.addItemData(info);
        }
    }

    @Override
    public void startUpdateApp(UpdateInfo updateInfo) {

    }

    @Override
    public void netWorkRequestFailure() {
        pb_loading_app.setVisibility(View.GONE);
        rv_home_content.setVisibility(View.GONE);
        ll_network_request_failure.setVisibility(View.VISIBLE);
    }

    /**
     * 补单处理
     * @param bdCode
     */
    @Override
    public void bdHandling(String bdCode) {
        handlingSeccessOrder(bdCode,true,null);
    }

    @Override
    public void shareResult(String resultStr) {
        if (resultStr.equals("ERR_OK")) {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.share_success), Toast.LENGTH_SHORT).show();
            HomeItemInfo homeItemInfo =  homeRecyclerViewAdapter.getFreeAppItemInfo();
            if (homeItemInfo==null)
                return;
            if (HistoryPayShareUtil.shareAddTime(mActivity)) { // 判断今天是否已经加过时
                long createTime = Long.valueOf(homeItemInfo.getCreateTiem()); //应用创建时间
                if (createTime != 0) {
                    UpdatePriceInfo updateMessageInfo = APPUsersShareUtil.getUpdateMessage(mActivity);
                    long millisecond = (long) (Integer.valueOf(updateMessageInfo.getSysj()) * 60) * 1000; //正常试用时长时间
                    long addTime = (long) ((Integer.valueOf(updateMessageInfo.getShareTime()) * 60) * 60) * 1000; // 分享成功增加的时长
                    long mfsjTime = Long.valueOf((Integer.valueOf(updateMessageInfo.getMfSj() == null ? "0" : updateMessageInfo.getMfSj()) * 60) * 1000); // 试用时长 分钟转毫秒
                    long systemTime = System.currentTimeMillis(); // 系统当前时间

                    // 检查当前试用分身是否已经过期  // 后来要求试用时间结束才可以加时，没有过期不给加
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
                        APPUsersShareUtil.setUpdateMessage(mActivity, updateMessageInfo);
                        HistoryPayShareUtil.addShareCount(mActivity);
                        homeRecyclerViewAdapter.setFreeApkItemInfo(homeItemInfo);
                    }
                }
            }
        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.share_failure), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void successfulclick(String apkName, int type, int waretype) {
        DebugLog.e("支付成功");
        if (operationDB==null)
            operationDB = new OperationDB(mActivity);
        //修改订单数据库 设置该笔订单的状态为支付完成 ,顺带获取该笔成功订单数据 做统计和开启功能操作
        ComsuptionInfo comsuptionInfo = operationDB.changePayComplateStatus(getHelper().getorderNumber(), DBLibs.Pay_status_Successful);
        LBStat.pay(comsuptionInfo.getPayType(),
                comsuptionInfo.getOrderNumber(),
                true,
                PayConstant.getPayDescribeStr(comsuptionInfo.getCommodityFunctionType()),
                Float.valueOf(comsuptionInfo.getPrice()),
                comsuptionInfo.getPayType());
        //开启对应功能
        handlingSeccessOrder(comsuptionInfo.getCommodityFunctionType(),false,comsuptionInfo);
    }

    @Override
    public void failClick() {
        DebugLog.e("支付失败");
        if (operationDB==null)
            operationDB = new OperationDB(mActivity);
        handlingFailedOrders(operationDB.changePayComplateStatus(getHelper().getorderNumber(), DBLibs.Pay_status_Failure));
    }

    @Override
    public void exceprtion() {
        DebugLog.e("支付失败");
        if (operationDB==null)
            operationDB = new OperationDB(mActivity);
        handlingFailedOrders(operationDB.changePayComplateStatus(getHelper().getorderNumber(), DBLibs.Pay_status_Failure));
    }

    /**
     * 处理失败订单
     * @param comsuptionInfo
     */
    public void handlingFailedOrders(ComsuptionInfo comsuptionInfo){
        if (comsuptionInfo != null) {
            if (payFailureDialog==null)
                payFailureDialog = new PayFailureDialog(mActivity);
            //填充订单数据
            payFailureDialog.setComsuptionInfo(comsuptionInfo);
            //开始处理失败订单
            payFailureDialog.failureOrderDialog();
            //设置处理回调监听
            payFailureDialog.setCallBack(new PayFailureDialog.PayFailureDialogCallback() {
                @Override
                public void seccess(ComsuptionInfo comsuptionInfo) {
                    handlingSeccessOrder(comsuptionInfo.getCommodityFunctionType(),false,comsuptionInfo);
                }
            });
        }
    }

    /**
     * 处理支付成功订单
     * @param functionType
     * @param isBd 是否属于补单操作
     */
    public void handlingSeccessOrder(String functionType,boolean isBd,ComsuptionInfo comsuptionInfo){

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(isBd?"补单成功":"支付成功");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
        });
        builder.setNegativeButton("问题咨询", (dialogInterface, i) -> {
            // TODO: 2017/11/30 跳转客服投诉界面
            startActivity(new Intent(mActivity,ComplaintsActivity.class));
        });

        if (operationDB==null)
            operationDB = new OperationDB(mActivity);
        if (functionType.equals(PayConstant.Pay_S_WX_DG)||functionType.equals("WX_Single")){
            homeFragmentImpl.addWeiChatApp("微信分身"+APPUsersShareUtil.getUserList(mActivity).size(),false);
            builder.setMessage("微信分身已创建\n警告：支付成功后,不可卸载和清除应用数据！");
            builder.show();
            if (comsuptionInfo!=null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_WX_ZS)||functionType.equals("WX_LifeTime")){
            APPUsersShareUtil.setLifeTime(mActivity);
            builder.setMessage("支付成功！点击右下角创建微信分身;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo!=null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_ALL_APPS)||functionType.equals("WX_AllApps")){
            LauncherUtils.saveLauncherType(mActivity,ListAppContract.SET_ALL);
            homeFragmentImpl.start(ListAppContract.SET_ALL);
            builder.setMessage("支付成功！点击右下角可选择需要多开得应用;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo!=null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_SVIP)||functionType.equals("C_SVIP")){
            SPAuxiliaryPayUtils.setSVIPPermission(mActivity);
            builder.setNegativeButton("去VIP界面查看", (dialogInterface, i) -> {
                // TODO: 2017/11/30 跳转VIP界面
                startActivity(new Intent(mActivity,VIPActivity.class));
            });
            builder.setMessage("支付成功！SVIP权限已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo!=null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }
        if (functionType.equals(PayConstant.Pay_S_SVIP_JHY)||functionType.equals("C_JHTALL")){
            SPAuxiliaryPayUtils.setWsAddFriendAll(mActivity);
            builder.setNegativeButton("去VIP界面查看", (dialogInterface, i) -> {
                // TODO: 2017/11/30 跳转VIP界面
                startActivity(new Intent(mActivity,VIPActivity.class));
            });
            builder.setMessage("支付成功！微信加好友功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo!=null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }
        if (functionType.equals(PayConstant.Pay_S_SVIP_HF)||functionType.equals("C_ALLHF")){
            SPAuxiliaryPayUtils.setWsFunctionHFAll(mActivity);
            builder.setNegativeButton("去VIP界面查看", (dialogInterface, i) -> {
                // TODO: 2017/11/30 跳转VIP界面
                startActivity(new Intent(mActivity,VIPActivity.class));
            });
            builder.setMessage("支付成功！微信自动回复功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo!=null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }
        if (functionType.equals(PayConstant.Pay_S_SVIP_FFH)||functionType.equals("C_FFH")){
            SPAuxiliaryPayUtils.setAuxiliaryMerchant(mActivity, AuxiliaryConstans.C_FFH);
            builder.setNegativeButton("去VIP界面查看", (dialogInterface, i) -> {
                // TODO: 2017/11/30 跳转VIP界面
                startActivity(new Intent(mActivity,VIPActivity.class));
            });
            builder.setMessage("支付成功！微信防封号功能已开启;\n警告：支付成功后,不可卸载应用和清除应用数据！");
            builder.show();
            if (comsuptionInfo!=null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
            return;
        }

        if (functionType.equals(PayConstant.Pay_S_DA_SHANG)){
            builder.setMessage("非常感谢您对本软件的支持！我们会继续努力！");
            builder.setNegativeButton("取消", (dialogInterface, i) -> {
            });
            if (comsuptionInfo!=null)
                operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Finish);
        }

    }

    @Override
    public void anginQuery() {
        DebugLog.e("支付失败");
    }



    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //不是第一个的格子都设一个左边和底部的间距
            outRect.left = space;
            outRect.bottom = space;
            //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
            if (parent.getChildLayoutPosition(view) %3==0) {
                outRect.left = 0;
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DebugLog.e("requestCode = "+requestCode+"//resultCode="+resultCode);
        if (resultCode == mActivity.RESULT_OK && requestCode == VCommends.REQUEST_SELECT_APP && data != null) {
            try{
                AppModel appModel =  data.getParcelableExtra("appmodel");
                appModel.setContext(mActivity);
                List<PackageInfo> arrayPackageInfo = mActivity.getPackageManager().getInstalledPackages(0);
                for (PackageInfo packageInfo:arrayPackageInfo){
                    if (packageInfo.packageName.endsWith(appModel.packageName)){
                        appModel.loadData(packageInfo.applicationInfo);
                    }
                }
                HomeItemInfo homeItemInfo = new HomeItemInfo();
                homeItemInfo.setTitle(appModel.name);
                homeItemInfo.setPackageName(appModel.packageName);
                homeItemInfo.setFreeTrialType(false);
                homeItemInfo.setAppModel(appModel);
                homeItemInfo.setLauncherType(HomeItemInfo.Launcher_ALL_APP);
                homeItemInfo.setIcon(appModel.icon);
                addItem(homeItemInfo);
            }catch (Exception e){
                e.printStackTrace();
                DebugLog.e(e.getMessage());
            }
        }
        if (resultCode== Activity.RESULT_OK||requestCode==10&&data!=null){
            int position = data.getIntExtra("posistion",-1);
            if (position>=0)
                homeRecyclerViewAdapter.myNotifyItemInserted(position);
        }
    }



    /**
     * 推啊 广告数据返回监听
     */
    public class MyNsTmListener implements NsTmListener {

        @Override
        public void onReceiveAd(String s) {
            try {
                JSONObject advJo = new JSONObject(s);
                String imageUrl = advJo.getString("img_url");

//                    Picasso.with(mContext).load(imageUrl).into(tioa_image);
                mNonStandardTm.adExposed();
                HomeItemInfo tuiaHomeItemInfo = new HomeItemInfo();
                AdInfo tuiaAdInfo = new AdInfo();
                tuiaAdInfo.setJumpUrl(advJo.getString("click_url"));
                if (!TextUtils.isEmpty(imageUrl))
                    tuiaAdInfo.setIconUrl(imageUrl);
                tuiaAdInfo.setCornerUrl("");
                tuiaAdInfo.setName("开红包");
                tuiaAdInfo.setJumpType(0);
                tuiaHomeItemInfo.setAdInfo(tuiaAdInfo);
                mNonStandardTm.adClicked();
                homeRecyclerViewAdapter.addAdvItemInfo(tuiaHomeItemInfo);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailedToReceiveAd() {
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (gif_reward.isPaused()) {
            gif_reward.setPaused(false);
        }
        if (operationDB == null) {
            operationDB = new OperationDB(mActivity);
        }
        List<ComsuptionInfo> arrayComsuption = operationDB.selectComsuptionMsg(DBLibs.Pay_status_Failure);
        arrayComsuption.addAll(operationDB.selectComsuptionMsg(DBLibs.Pay_status_Unknown));
        if (arrayComsuption.size() > 0){
            new AlertDialog.Builder(mActivity)
                    .setMessage("您有"+arrayComsuption.size()+"笔订单需要核实！")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("去核实", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(mActivity, ExceptionOrdersActivity.class));
                        }
                    })
                    .show();
        }
        List<ComsuptionInfo> arrayComsuptionSuccessFul = operationDB.selectComsuptionMsg(DBLibs.Pay_status_Successful);
        if (arrayComsuptionSuccessFul.size()>0){
            ComsuptionInfo comsuptionInfo = arrayComsuptionSuccessFul.get(0);
            handlingSeccessOrder(comsuptionInfo.getCommodityFunctionType(),true,comsuptionInfo);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (gif_reward != null)
            gif_reward.setPaused(true);
    }
}
