package com.zhushou.weichat.uinew.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lbsw.stat.LBStat;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.bean.ApkItemInfo;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.ui.ItemVirtualAppsActivity;
import com.zhushou.weichat.ui.LoadingActivity;
import com.zhushou.weichat.ui.TuiABrowserActivity;
import com.zhushou.weichat.ui.adapter.ApkOperator;
import com.zhushou.weichat.ui.dialog.ShareOrPayDialog;
import com.zhushou.weichat.ui.dialog.ShareWcDialog;
import com.zhushou.weichat.uinew.VIPActivity;
import com.zhushou.weichat.uinew.info.HomeItemInfo;
import com.zhushou.weichat.uinew.info.HomeItemTopInfo;
import com.zhushou.weichat.uinew.interfac.HomeRecyclerItemContract;
import com.zhushou.weichat.utils.DebugLog;
import com.zhushou.weichat.utils.ToastUtil;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ApkOperator.RemoveCallback, ApkOperator.ChangeApkNameCallback,
        HomeRecyclerItemContract.ItemView {

    private List<HomeItemInfo> arrayData;
    private List<HomeItemTopInfo> arrayHomitemTop;
    private HomeItemInfo freeApkItemInfo;
    private Activity mActivity;
    public static final int TopType = 0x01;
    public static final int NormalType = 0x02;
    private int currenType = NormalType;
    private HomeRecyclerItemContract.ItemContract itemContract;

    public HomeRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        arrayData = new ArrayList<>();
        itemContract = new HomeRecyclerItemImpl(mActivity, this);
    }

    public void setAllArrayData(List<HomeItemInfo> arrayData) {
        if (arrayData == null)
            return;
        if (this.arrayData == null)
            this.arrayData = new ArrayList<>();
        if (this.arrayData.size() > 0) {
            this.arrayData.clear();
            this.arrayData.addAll(arrayData);
        } else {
            this.arrayData.addAll(arrayData);
        }
        notifyDataSetChanged();
    }

    public void addItemData(HomeItemInfo info) {
        this.arrayData.add(info);
        notifyItemInserted(arrayData.size() + 1);
    }


    public void setBannerData(List<HomeItemTopInfo> arrayHomitemTop) {
        this.arrayHomitemTop = arrayHomitemTop;
        notifyItemChanged(0);
    }

    public void setFreeApkItemInfo(HomeItemInfo freeApkItemInfo) {
        if (freeApkItemInfo!=null)
            this.freeApkItemInfo = freeApkItemInfo;
        notifyItemChanged(0);
    }
    public HomeItemInfo getFreeAppItemInfo(){
        return  this.freeApkItemInfo;
    }

    public void addAdvItemInfo(HomeItemInfo advHomeItemInfo){
        this.arrayData.add(1,advHomeItemInfo);
//        if (arrayData.size()>1){
//            notifyItemChanged(1);
//        }else{
            notifyDataSetChanged();
//        }
    }





    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TopType;
        } else {
            return NormalType;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TopType) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.layout_home_top, parent, false);
            return new HomeRecyclerTopHolder(view, mActivity, this, this);
        } else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.layout_home_recyclerview, parent, false);
            return new HomeRecyclerHolder(view, mActivity);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HomeRecyclerTopHolder) {
            ((HomeRecyclerTopHolder) holder).intoTopHolder(arrayHomitemTop, freeApkItemInfo, position);
        }

        if (holder instanceof HomeRecyclerHolder) {
            ((HomeRecyclerHolder) holder).initTo(arrayData.get(position - 1), position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        HomeItemInfo homeItemInfo = arrayData.get(position - 1);
                        if (homeItemInfo.adInfo != null) {
                            TuiABrowserActivity.intentTuiABrowserA(mActivity, homeItemInfo.adInfo.getJumpUrl(),homeItemInfo.adInfo.getName()==null?"":homeItemInfo.adInfo.getName());
                            LBStat.collect("广告",homeItemInfo.adInfo.getName());
                        } else if (homeItemInfo.isVip()) {
                            // TODO: 2017/11/28 跳转微商VIP界面
                            mActivity.startActivity(new Intent(mActivity, VIPActivity.class));
                        } else if (homeItemInfo.getLauncherType() == HomeItemInfo.Launcher_ALL_APP) {
                            if (homeItemInfo.appModel == null)
                                return;
                            ItemVirtualAppsActivity.openVirtualAppArray(homeItemInfo.appModel, mActivity, position);
                        } else {
                            launchApp(homeItemInfo);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        notifyDataSetChanged();
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    HomeItemInfo homeItemInfo = arrayData.get(position - 1);
                    if (homeItemInfo != null && homeItemInfo.adInfo == null && !homeItemInfo.isVip()) {
                        if (homeItemInfo.getLauncherType() == HomeItemInfo.Launcher_ALL_APP){
//                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//                            builder.setTitle("提示");
//                            builder.setCancelable(false);
//                            builder.setPositiveButton("取消", (dialogInterface, i) -> {
//                            });
//                            builder.setNegativeButton("确定", (dialogInterface, i) -> {
//
//                            });
//                            builder.setMessage("是否删除该应用？");
//                            builder.show();

                        }else{
                            if (itemContract != null)
                                itemContract.onLongClick(homeItemInfo, position);
                        }
                    }
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return arrayData.size() + 1;
    }


    @Override
    public void removeItem(ApkItemInfo itemInfo) {

    }

    @Override
    public void changeItem(ApkItemInfo itemInfo, int position) {
//        notifyItemChanged(position);
    }

    public void launchApp(HomeItemInfo itemInfo) throws Exception {
        if (itemInfo.isFreeTrialType()) {
            UpdatePriceInfo updateMessageInfo = APPUsersShareUtil.getUpdateMessage(mActivity);
            if (updateMessageInfo.getMfSj() == null)
                return;

            //判断分身是否过期

            long playTime = (Integer.valueOf(updateMessageInfo.getMfSj()) * 60) * 1000; //可以免费试用的时长（分钟）*60*1000 = 可以试用的毫秒时长

            long nowTime = System.currentTimeMillis(); // 系统当前时间

            long overTime = itemInfo.getCreateTiem() + playTime; //试用结束时间 = 应用创建时间 + 可以免费试用的时长

            boolean isSY = (overTime - nowTime) > 0 ? true : false; // 结束时间 - 当前时间   >0 还未过期 反之 过期
            if (!isSY) {
                new ShareOrPayDialog(mActivity, new ShareOrPayDialog.ShareOrPayListener() {
                    @Override
                    public void shareClick() {
                        new ShareWcDialog(mActivity);
                    }

                    @Override
                    public void payClick() {
                        if (VApp.getInstance().getaActivity() != null)
                            VApp.getInstance().getaActivity().addWeiChatBottom();
                    }
                }).show();
            } else {
                try {
                    LoadingActivity.launch(mActivity, itemInfo.getAppModel(), itemInfo.getUserInfo().id, itemInfo.getTitle().toString());
                    LBStat.click(VApp.TJ_QDFS); // 分身启动统计
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (checkIsInstalledVersion(itemInfo.getAppModel().versionCode, "com.tencent.mm")) {
                try {
                    LoadingActivity.launch(mActivity, itemInfo.getAppModel(), itemInfo.getUserInfo().id, itemInfo.getTitle().toString());
                    LBStat.click(VApp.TJ_QDFS); // 分身启动统计
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtil.centerToast(mActivity, "该分身版本已升级");
            }

        }
    }

    private boolean checkIsInstalledVersion(int versionCode, String packageName) {
        List<PackageInfo> pakageinfos = mActivity.getPackageManager().getInstalledPackages(0);
        for (PackageInfo info : pakageinfos) {
            if (info.packageName.equals(packageName)) {
                if (info.versionCode == versionCode) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void myNotifyDataSetChanged() {
        notifyDataSetChanged();
    }

    @Override
    public void myNotifyItemInserted(int position) {
        try {
            notifyItemChanged(position);
        } catch (Exception e) {
            e.printStackTrace();
            DebugLog.e(e.getMessage());
        }
    }

    @Override
    public void myRemoveItem(int position) {
        if (arrayData.size() > position - 1)
            arrayData.remove(position - 1);
        try {
            notifyItemRemoved(position - 2);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            DebugLog.e(e.getMessage());
            notifyDataSetChanged();
        }

    }

}
