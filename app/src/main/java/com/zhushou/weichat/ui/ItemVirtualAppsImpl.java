package com.zhushou.weichat.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.ApkItemInfo;
import com.zhushou.weichat.bean.AppUsersInfo;
import com.zhushou.weichat.ui.dialog.ApkNameDialog;
import com.zhushou.weichat.ui.models.AppModel;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;
import com.zhushou.weichat.utils.AddShortcutToDesktop;
import com.zhushou.weichat.utils.HistoryPayShareUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */

public class ItemVirtualAppsImpl implements ItemVirtualAppsContract.ItemAppsImpl {


    public ItemVirtualAppsContract.ActivityContract activityContract;
    public Activity mActivity;
    public AppModel appModel; //启动应用实体
    public Resources mResources;
    private AddShortcutToDesktop desktop;

    public ItemVirtualAppsImpl(Activity activity, ItemVirtualAppsContract.ActivityContract activityContract,AppModel appModel) {
        this.mActivity = activity;
        this.mResources = activity.getResources();
        this.appModel = appModel;
        this.activityContract = activityContract;
        desktop = new AddShortcutToDesktop(mActivity);
    }

    @Override
    public void startLoading() {
        activityContract.displayLoading(false);
        ArrayList<ApkItemInfo> itemAppArray = new ArrayList<>();
        List<VUserInfo> userinfos = VUserManager.get().getUsers(false);
        List<AppUsersInfo> localUsers = APPUsersShareUtil.getItemApps(mActivity, appModel.packageName);
        if (localUsers != null) {
            for (AppUsersInfo localUsersInfo : localUsers) {
                for (VUserInfo info : userinfos) {
                    if (Integer.valueOf(localUsersInfo.getId()) == info.id) {
                        ApkItemInfo apkItemInfo = new ApkItemInfo();
                        apkItemInfo.setTitle(localUsersInfo.getAppName());
                        apkItemInfo.setApkType(ApkItemInfo.WEICHAT_TYPE);
                        apkItemInfo.setAppModel(appModel);
                        apkItemInfo.setCreateDate(HistoryPayShareUtil.getFormatedDateTime("yy-MM-dd", info.creationTime));
                        apkItemInfo.setUserInfo(info);
                        apkItemInfo.setOpenId(info.id);
                        apkItemInfo.setFreeTrialType(localUsersInfo.isFreeTrialType());
                        apkItemInfo.setCreateTiem(localUsersInfo.getCreateTiem());
                        apkItemInfo.setCreateTimeIdCard(localUsersInfo.getCreateTimeIdCard());
                        apkItemInfo.setPackageName(appModel.packageName);
                        itemAppArray.add(apkItemInfo);
                    }
                }

            }
        }

        activityContract.loadingDataS(itemAppArray,appModel);

    }

    @Override
    public void addVirtualApp(AppModel appModel) {
        new ApkNameDialog(mActivity).dialog(null, new ApkNameDialog.ApkNameCallback() {
            @Override
            public void confirmCallback(String apkName) {
                createApp(apkName.toString(), appModel);
            }
        });
    }

    @Override
    public void addDeskTop(ApkItemInfo apkItemInfo) {
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
                    boolean isCreateShortcut = VirtualCore.get().createShortcut(apkItemInfo.getUserInfo().id, apkItemInfo.getAppModel().packageName, listener);
                    if (isCreateShortcut){
                        Toast.makeText(mActivity, mResources.getString(R.string.adddesktop_chenggong), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
                        alertDialog.setTitle("提示");
                        alertDialog.setMessage("部分机型需要关闭本应用之后才可以通过快捷方式进入分身应用");
                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialog.show();
                    }else{
                        Toast.makeText(mActivity,"添加失败", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(mActivity, mResources.getString(R.string.adddesktop_shibai), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createApp(String appName, AppModel appModel) {
        VUserInfo vUserInfo = VUserManager.get().createUser(appName, VUserInfo.FLAG_ADMIN);
        if (vUserInfo == null) {
            Toast.makeText(mActivity, mResources.getString(R.string.chuang_jian_shi_bai), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mActivity, mResources.getString(R.string.chuang_jian_cheng_gong), Toast.LENGTH_SHORT).show();
        }
        if (vUserInfo == null) {
            return;
        }
        boolean iss = VirtualCore.get().installPackageAsUser(vUserInfo.id, appModel.packageName);
        if (!iss) {
            return;
        }
        AppUsersInfo weichat_userInfo = new AppUsersInfo();
        weichat_userInfo.setId(String.valueOf(vUserInfo.id));
        weichat_userInfo.setAppType(ApkItemInfo.WEICHAT_TYPE);
        weichat_userInfo.setAppName(appName);
        weichat_userInfo.setPrice("0");
        weichat_userInfo.setDate("");
        weichat_userInfo.setPackageName(appModel.packageName);
        weichat_userInfo.setCreateTiem(System.currentTimeMillis());
        weichat_userInfo.setCreateTimeIdCard(String.valueOf(System.currentTimeMillis()));
        weichat_userInfo.setFreeTrialType(false);
        APPUsersShareUtil.setItemApps(mActivity, weichat_userInfo);


        ApkItemInfo apkItemInfo = new ApkItemInfo();
        apkItemInfo.setTitle(appName);
        apkItemInfo.setUserInfo(vUserInfo);
        apkItemInfo.setAppModel(appModel);
        apkItemInfo.setCreateDate(HistoryPayShareUtil.getFormatedDateTime("yy-MM-dd", vUserInfo.creationTime));
        apkItemInfo.setCreateTiem(System.currentTimeMillis());
        apkItemInfo.setFreeTrialType(false);
        activityContract.addVirtualApp(apkItemInfo);
    }
}
