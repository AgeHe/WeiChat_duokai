package com.zhushou.weichat.ui.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lbsw.stat.LBStat;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.VLog;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.bean.ApkItemInfo;
import com.zhushou.weichat.bean.AppUsersInfo;
import com.zhushou.weichat.ui.LoadingActivity;
import com.zhushou.weichat.ui.dialog.ApkNameDialog;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;
import com.zhushou.weichat.utils.AddShortcutToDesktop;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */

public class ItemVirtualAppsViewHolder extends RecyclerView.ViewHolder {

    ImageView iv_apk_icon;
    TextView tv_apk_name;
    TextView tv_apk_createdate;
    TextView tv_change_name;
    TextView tv_delete_apk;
    LinearLayout ll_item;
    private Activity mActivity;
    private Resources mResources;
    private ChangeApkNameCallback changeNameCallBack;

    public ItemVirtualAppsViewHolder(View itemView, Activity mActivity,ChangeApkNameCallback changeNameCallBack) {
        super(itemView);
        initView(itemView);
        this.mActivity = mActivity;
        mResources = this.mActivity.getResources();
        this.changeNameCallBack = changeNameCallBack;
    }

    private void initView(View view) {
        ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
        iv_apk_icon = (ImageView) view.findViewById(R.id.iv_apk_icon);
        tv_apk_name = (TextView) view.findViewById(R.id.tv_apk_name);
        tv_apk_createdate = (TextView) view.findViewById(R.id.tv_apk_createdate);
        tv_change_name = (TextView) view.findViewById(R.id.tv_change_name);
        tv_delete_apk = (TextView) view.findViewById(R.id.tv_delete_apk);
    }

    private AddShortcutToDesktop desktop;
    private ApkItemInfo mApkItem;

    public void initTo(ApkItemInfo apkItem, int position) {
        this.mApkItem = apkItem;
        if (mApkItem == null)
            return;

        iv_apk_icon.setImageDrawable(apkItem.appModel.icon);
        tv_apk_name.setText(apkItem.getTitle());
        tv_apk_createdate.setVisibility(View.GONE);

        tv_change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ApkNameDialog(mActivity).dialog(mApkItem, new ApkNameDialog.ApkNameCallback() {
                    @Override
                    public void confirmCallback(String apkName) {
                        boolean isCreate = true;
                        List<AppUsersInfo> userList = APPUsersShareUtil.getItemApps(mActivity,mApkItem.getPackageName());
                        for (AppUsersInfo info : userList) {
                            if (apkName.equals(info.getAppName())) {
                                isCreate = false;
                            }
                        }
                        if (isCreate) {
                            desktop = new AddShortcutToDesktop(mActivity);
                            VLog.d("jyh changeApkName itemInfo.getTitle().toString()", mApkItem.getTitle().toString());
                            VLog.d("jyh changeApkName itemInfo.getAppModel().name", mApkItem.getAppModel().name);
                            if (!mApkItem.getTitle().toString().equals(mApkItem.getAppModel().name) && desktop.isShortcutInstalled(mApkItem.getTitle().toString())) {
                                VirtualCore.OnEmitShortcutListener listener = new VirtualCore.OnEmitShortcutListener() {
                                    @Override
                                    public Bitmap getIcon(Bitmap originIcon) {
                                        return originIcon;
                                    }

                                    @Override
                                    public String getName(String originName) {
                                        return mApkItem.getTitle().toString();
                                    }
                                };
                                //先删除原来的快捷图标
                                VirtualCore.get().removeShortcut(mApkItem.getUserInfo().id, mApkItem.getAppModel().packageName, null, listener);
                                //更改name
                                mApkItem.setTitle(apkName);
                                //                        //创建现在的图标
                                VirtualCore.get().createShortcut(mApkItem.getUserInfo().id, mApkItem.getAppModel().packageName, listener);
                            }
                            mApkItem.setTitle(apkName);
                            APPUsersShareUtil.updateItemAppsName(mActivity,mApkItem.getCreateTimeIdCard(),apkName);
                            changeNameCallBack.changeItem(mApkItem, position);
                            VLog.d("jyh changeApkName", "success");
                        } else {
                            Toast.makeText(mActivity, mResources.getString(R.string.appName_chongfu), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LoadingActivity.launch(mActivity, mApkItem.getAppModel(), mApkItem.getUserInfo().id, mApkItem.getTitle().toString());
                    LBStat.click(VApp.TJ_QDFS); // 分身启动统计
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public interface ChangeApkNameCallback{
        void changeItem(ApkItemInfo itemInfo,int position);
    }

}
