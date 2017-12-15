package com.zhushou.weichat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.zhushou.weichat.R;
import com.zhushou.weichat.auxiliary.base.BaseActivity;
import com.zhushou.weichat.bean.ApkItemInfo;
import com.zhushou.weichat.bean.AppUsersInfo;
import com.zhushou.weichat.ui.adapter.ItemVirtualAppsRecyclerAdapter;
import com.zhushou.weichat.ui.models.AppModel;
import com.zhushou.weichat.utils.DebugLog;
import com.zhushou.weichat.utils.HistoryPayShareUtil;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/4.
 */

public class ItemVirtualAppsActivity extends BaseActivity implements ItemVirtualAppsRecyclerAdapter.VirtualAppsCallback,
ItemVirtualAppsContract.ActivityContract{


    private ItemVirtualAppsContract.ItemAppsImpl itemApps;
    private static AppModel appModel;
    public static void  openVirtualAppArray(AppModel intentAppModel, Activity activity){
        appModel = intentAppModel;
        activity.startActivity(new Intent(activity,ItemVirtualAppsActivity.class));
    }
    public static void  openVirtualAppArray(AppModel intentAppModel, Activity activity,int position){
        appModel = intentAppModel;
        Bundle mBundle = new Bundle();
        mBundle.putInt("position",position);
        activity.startActivityForResult(new Intent(activity,ItemVirtualAppsActivity.class).putExtras(mBundle),10);
    }

    int appPosition = -1;
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setContentView(R.layout.activity_item_virtualapps);
        try {
            appPosition = getIntent().getExtras().getInt("position");
        }catch (Exception e){
            e.printStackTrace();
            DebugLog.e(e.getMessage());
        }
        Intent data = new Intent();
        data.putExtra("posistion",appPosition);
        setResult(Activity.RESULT_OK, data);
    }

    private RecyclerView rv_item_apps;
    private LinearLayout ll_pb_loading;
    private ItemVirtualAppsRecyclerAdapter recyclerAdapter;
    @Override
    public void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        rv_item_apps = (RecyclerView) findViewById(R.id.rv_item_apps);
        ll_pb_loading = (LinearLayout) findViewById(R.id.ll_pb_loading);

    }

    @Override
    public void initData() {
        try{
            if (appModel==null)
                throw new Exception("未知错误");
        }catch (Exception e){
            Toast.makeText(this,"未知错误",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        itemApps = new ItemVirtualAppsImpl(this,this,appModel);
        recyclerAdapter = new ItemVirtualAppsRecyclerAdapter(this);
        recyclerAdapter.setClickCallback(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_item_apps.setLayoutManager(llm);
        rv_item_apps.setAdapter(recyclerAdapter);
        itemApps.startLoading();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void addAppBottom() {
        itemApps.addVirtualApp(appModel);
    }

    @Override
    public void adapterItemAddDeskTop(ApkItemInfo apkItemInfo, int position) {
        itemApps.addDeskTop(apkItemInfo);
    }

    @Override
    public void loadingDataS(List<ApkItemInfo> dataArray,AppModel appModel) {
        if (dataArray==null){
            hideLoading(false);
            return;
        }

        List<ApkItemInfo> addArrayItem = new ArrayList<>();
        if (appModel.packageName.equals("com.tencent.mm")){
            List<VUserInfo> userinfos = VUserManager.get().getUsers(false);
            List<AppUsersInfo> localUsers = APPUsersShareUtil.getUserList(this);
            for (AppUsersInfo localUsersInfo : localUsers) {
                for (VUserInfo userinfo:userinfos){
                    if (Integer.valueOf(localUsersInfo.getId()) == userinfo.id){
                        ApkItemInfo apkItemInfo = new ApkItemInfo();
                        apkItemInfo.setTitle(localUsersInfo.getAppName());
                        apkItemInfo.setApkType(ApkItemInfo.WEICHAT_TYPE);
                        apkItemInfo.setAppModel(appModel);
                        apkItemInfo.setCreateDate(HistoryPayShareUtil.getFormatedDateTime("yy-MM-dd", userinfo.creationTime));
                        apkItemInfo.setUserInfo(userinfo);
                        apkItemInfo.setOpenId(userinfo.id);
                        apkItemInfo.setFreeTrialType(localUsersInfo.isFreeTrialType());
                        apkItemInfo.setCreateTiem(localUsersInfo.getCreateTiem());
                        if (!localUsersInfo.isFreeTrialType())
                            addArrayItem.add(apkItemInfo);
                    }
                }
            }
        }
        if (addArrayItem.size()>0){
            addArrayItem.addAll(dataArray);
            recyclerAdapter.setArrayData(addArrayItem);
        }else{
            recyclerAdapter.setArrayData(dataArray);
        }
        hideLoading(false);
    }

    @Override
    public void hideLoading(boolean isDialog) {
        ll_pb_loading.setVisibility(View.GONE);
    }

    @Override
    public void displayLoading(boolean isDialog) {
        ll_pb_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void addVirtualApp(ApkItemInfo apkItemInfo) {
        recyclerAdapter.setArrayDataItem(apkItemInfo);
    }
}
