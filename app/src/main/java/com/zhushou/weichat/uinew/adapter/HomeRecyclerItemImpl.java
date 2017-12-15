package com.zhushou.weichat.uinew.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.lody.virtual.client.core.VirtualCore;
import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.AppUsersInfo;
import com.zhushou.weichat.ui.dialog.ApkNameDialog;
import com.zhushou.weichat.uinew.dialog.HomeLongClickDialog;
import com.zhushou.weichat.uinew.info.HomeItemInfo;
import com.zhushou.weichat.uinew.interfac.HomeRecyclerItemContract;
import com.zhushou.weichat.utils.AddShortcutToDesktop;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/11/29.
 */

public class HomeRecyclerItemImpl implements HomeRecyclerItemContract.ItemContract {


    Activity mActivity;
    HomeRecyclerItemContract.ItemView itemView;
    private AddShortcutToDesktop desktop;

    public HomeRecyclerItemImpl(Activity mActivity,HomeRecyclerItemContract.ItemView itemView){
        this.mActivity = mActivity;
        this.itemView = itemView;
    }

    @Override
    public void onLongClick(HomeItemInfo homeItemInfo, int position) {
        desktop = new AddShortcutToDesktop(mActivity);
        new HomeLongClickDialog(mActivity).setHomeLongClickLisener(new HomeLongClickDialog.HomeLongClickLisener() {
            @Override
            public void changename() {
                if (homeItemInfo==null||homeItemInfo.getTitle()==null)
                    return;
                new ApkNameDialog(mActivity).changenameOrCreateNameDialog(homeItemInfo, new ApkNameDialog.ApkNameCallback() {
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
                            if (!homeItemInfo.getTitle().toString().equals(homeItemInfo.getAppModel().name)&&desktop.isShortcutInstalled(homeItemInfo.getTitle().toString())){
                                VirtualCore.OnEmitShortcutListener listener = new VirtualCore.OnEmitShortcutListener() {
                                    @Override
                                    public Bitmap getIcon(Bitmap originIcon) {

                                        return originIcon;
                                    }
                                    @Override
                                    public String getName(String originName) {
                                        return homeItemInfo.getTitle().toString();
                                    }
                                };
                                //先删除原来的快捷图标
                                VirtualCore.get().removeShortcut(homeItemInfo.getUserInfo().id, homeItemInfo.getAppModel().packageName, null, listener);
                                //更改name
                                homeItemInfo.setTitle(apkName);
                                //创建现在的图标
                                VirtualCore.get().createShortcut(homeItemInfo.getUserInfo().id, homeItemInfo.getAppModel().packageName, listener);
                            }
                            homeItemInfo.setTitle(apkName);
                            APPUsersShareUtil.changeUsersInfo(mActivity, homeItemInfo.getUserInfo().id, apkName, homeItemInfo.getAppModel().packageName);
                            itemView.myNotifyItemInserted(position);
                        }else{
                            Toast.makeText(mActivity,mActivity.getResources().getString(R.string.appName_chongfu),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void createShortcut() {
                new ApkNameDialog(mActivity).myAddDeskAlertDialog(homeItemInfo.getTitle().toString(), new ApkNameDialog.ApkNameCallback() {
                    @Override
                    public void confirmCallback(String apkName) {
                        if (!desktop.isShortcutInstalled(homeItemInfo.getTitle().toString())) {
                            VirtualCore.OnEmitShortcutListener listener = new VirtualCore.OnEmitShortcutListener() {
                                @Override
                                public Bitmap getIcon(Bitmap originIcon) {
                                    return originIcon;
                                }

                                @Override
                                public String getName(String originName) {
                                    return homeItemInfo.getTitle().toString();
                                }

                            };
                            boolean isCreateShortcut = VirtualCore.get().createShortcut(homeItemInfo.getUserInfo().id, homeItemInfo.getAppModel().packageName, listener);
                            if (isCreateShortcut){
                                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.adddesktop_chenggong), Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
                                alertDialog.setTitle("提示");
                                alertDialog.setMessage("部分机型需要关闭本应用之后才可以通过快捷方式进入分身应用");
                                alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i){

                                    }
                                });
                                alertDialog.show();
                            }else{
                                Toast.makeText(mActivity,"添加失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.adddesktop_shibai), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void deleteApp() {
                itemView.myRemoveItem(position);
            }

        }).show();
    }


}
