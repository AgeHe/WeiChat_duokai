package com.zhushou.weichat.ui.adapter;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.lbsw.stat.LBStat;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.VLog;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.bean.ApkItemInfo;
import com.zhushou.weichat.bean.AppUsersInfo;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.ui.LoadingActivity;
import com.zhushou.weichat.ui.dialog.ApkNameDialog;
import com.zhushou.weichat.ui.dialog.ShareOrPayDialog;
import com.zhushou.weichat.ui.dialog.ShareWcDialog;
import com.zhushou.weichat.uinew.info.HomeItemInfo;
import com.zhushou.weichat.utils.AddShortcutToDesktop;
import com.zhushou.weichat.utils.ToastUtil;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/1/7.
 */

public class ApkOperator {

    private static final String TAG = "ApkOperator";

    private AddShortcutToDesktop desktop;

    private Activity mActivity;
    private RemoveCallback removeCallback;
    private ChangeApkNameCallback changeApkNameCallback;
    private Resources mResources;

    public ApkOperator(Activity mActivity,RemoveCallback removeCallback,ChangeApkNameCallback changeApkNameCallback){
        this.mActivity = mActivity;
        mResources = this.mActivity.getResources();
        this.removeCallback = removeCallback;
        this.changeApkNameCallback = changeApkNameCallback;
    }


    public void deleteApk(final ApkItemInfo itemInfo){

    }

    public void changeApkName(final ApkItemInfo itemInfo, final int position){
        new ApkNameDialog(mActivity).dialog(itemInfo, new ApkNameDialog.ApkNameCallback() {
            @Override
            public void confirmCallback(String apkName) {
                boolean isCreate = true; //是否重名
                //检查是否有=重名
                List<AppUsersInfo> userList = APPUsersShareUtil.getUserList(mActivity);
                for (AppUsersInfo info:userList){
                    if (apkName.equals(info.getAppName())){
                        isCreate = false;
                    }
                }
                if (isCreate){
                    desktop = new AddShortcutToDesktop(mActivity);
                    VLog.d("jyh changeApkName itemInfo.getTitle().toString()",itemInfo.getTitle().toString());
                    VLog.d("jyh changeApkName itemInfo.getAppModel().name",itemInfo.getAppModel().name);
                    if (!itemInfo.getTitle().toString().equals(itemInfo.getAppModel().name)&&desktop.isShortcutInstalled(itemInfo.getTitle().toString())){
                        VirtualCore.OnEmitShortcutListener listener = new VirtualCore.OnEmitShortcutListener() {
                            @Override
                            public Bitmap getIcon(Bitmap originIcon) {

                                return originIcon;
                            }
                            @Override
                            public String getName(String originName) {
                                return itemInfo.getTitle().toString();
                            }
                        };
                        //先删除原来的快捷图标
                        VirtualCore.get().removeShortcut(itemInfo.getUserInfo().id, itemInfo.getAppModel().packageName, null, listener);
                        //更改name
                        itemInfo.setTitle(apkName);
                        //创建现在的图标
                        VirtualCore.get().createShortcut(itemInfo.getUserInfo().id, itemInfo.getAppModel().packageName, listener);
                    }
                    itemInfo.setTitle(apkName);
                    APPUsersShareUtil.changeUsersInfo(mActivity, itemInfo.getUserInfo().id, apkName, itemInfo.getAppModel().packageName);
                    changeApkNameCallback.changeItem(itemInfo, position);
                    VLog.d("jyh changeApkName","success");
                }else{
                    Toast.makeText(mActivity,mResources.getString(R.string.appName_chongfu),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void launchApp(HomeItemInfo itemInfo){
        if (itemInfo.isFreeTrialType()){
            UpdatePriceInfo updateMessageInfo = APPUsersShareUtil.getUpdateMessage(mActivity);
            if (updateMessageInfo.getMfSj()==null)
                return;

            //判断分身是否过期

            long playTime = (Integer.valueOf(updateMessageInfo.getMfSj())*60)*1000; //可以免费试用的时长（分钟）*60*1000 = 可以试用的毫秒时长

            long nowTime = System.currentTimeMillis(); // 系统当前时间

            long overTime = itemInfo.getCreateTiem()+playTime; //试用结束时间 = 应用创建时间 + 可以免费试用的时长

            boolean isSY = (overTime - nowTime)>0?true:false; // 结束时间 - 当前时间   >0 还未过期 反之 过期
            if (!isSY){
                new ShareOrPayDialog(mActivity, new ShareOrPayDialog.ShareOrPayListener() {
                    @Override
                    public void shareClick() {
                        new ShareWcDialog(mActivity);
                    }

                    @Override
                    public void payClick() {
                        if (VApp.getInstance().getaActivity()!=null)
                            VApp.getInstance().getaActivity().addWeiChatBottom();
                    }
                }).show();
            }else{
                try {
                    LoadingActivity.launch(mActivity, itemInfo.getAppModel(), itemInfo.getUserInfo().id,itemInfo.getTitle().toString());
                    LBStat.click(VApp.TJ_QDFS); // 分身启动统计
                } catch (Throwable e){
                    e.printStackTrace();
                }
            }
        }else{
            if (checkIsInstalledVersion(itemInfo.getAppModel().versionCode,"com.tencent.mm")){
                try {
                    LoadingActivity.launch(mActivity, itemInfo.getAppModel(), itemInfo.getUserInfo().id,itemInfo.getTitle().toString());
                    LBStat.click(VApp.TJ_QDFS); // 分身启动统计
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }else{
                ToastUtil.centerToast(mActivity,"该分身版本已升级");
            }

        }
    }

    public void launchApp(ApkItemInfo itemInfo){
        if (itemInfo.isFreeTrialType()){
            UpdatePriceInfo updateMessageInfo = APPUsersShareUtil.getUpdateMessage(mActivity);
            if (updateMessageInfo.getMfSj()==null)
                return;

            //判断分身是否过期

            long playTime = (Integer.valueOf(updateMessageInfo.getMfSj())*60)*1000; //可以免费试用的时长（分钟）*60*1000 = 可以试用的毫秒时长

            long nowTime = System.currentTimeMillis(); // 系统当前时间

            long overTime = itemInfo.getCreateTiem()+playTime; //试用结束时间 = 应用创建时间 + 可以免费试用的时长

            boolean isSY = (overTime - nowTime)>0?true:false; // 结束时间 - 当前时间   >0 还未过期 反之 过期
            if (!isSY){
                new ShareOrPayDialog(mActivity, new ShareOrPayDialog.ShareOrPayListener() {
                    @Override
                    public void shareClick() {
                        new ShareWcDialog(mActivity);
                    }
                    @Override
                    public void payClick() {

                        if (VApp.getInstance().getMainActivity()!=null){
                            VApp.getInstance().getMainActivity().addWeiChatBottom();
                        }

//                        if (VApp.getInstance().getHomeFragment()!=null)
//                            VApp.getInstance().getHomeFragment().shareShowPayDialog();
                    }
                }).show();
            }else{
                try {
                    LoadingActivity.launch(mActivity, itemInfo.getAppModel(), itemInfo.getUserInfo().id,itemInfo.getTitle().toString());
                    LBStat.click(VApp.TJ_QDFS); // 分身启动统计
                } catch (Throwable e){
                    e.printStackTrace();
                }
            }
        }else{
            if (checkIsInstalledVersion(itemInfo.getAppModel().versionCode,"com.tencent.mm")){
                try {
                    LoadingActivity.launch(mActivity, itemInfo.getAppModel(), itemInfo.getUserInfo().id,itemInfo.getTitle().toString());
                    LBStat.click(VApp.TJ_QDFS); // 分身启动统计
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }else{
                ToastUtil.centerToast(mActivity,"该分身版本已升级");
            }

        }
    }

    private boolean checkIsInstalledVersion(int versionCode,String packageName){
        List<PackageInfo> pakageinfos = mActivity.getPackageManager().getInstalledPackages(0);
        for(PackageInfo info:pakageinfos){
            if(info.packageName.equals(packageName)){
                if(info.versionCode==versionCode){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    public interface RemoveCallback {
        void removeItem(ApkItemInfo itemInfo);
    }

    public interface ChangeApkNameCallback{
        void changeItem(ApkItemInfo itemInfo,int position);
    }

}
