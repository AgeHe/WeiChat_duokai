package com.zhushou.weichat.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.InstalledAppInfo;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VCommends;
import com.zhushou.weichat.abs.ui.VUiKit;
import com.zhushou.weichat.auxiliary.base.BaseActivity;
import com.zhushou.weichat.ui.adapter.InstallAppsRVAdapter;
import com.zhushou.weichat.ui.models.AppDataSource;
import com.zhushou.weichat.ui.models.AppModel;
import com.zhushou.weichat.ui.models.AppRepository;
import com.zhushou.weichat.utils.ToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/7/4.
 */

public class InstallAppsActivity extends BaseActivity implements InstallAppsRVAdapter.ClickCallback {
    private static final String TAG = "InstallAppsActivity";
    private AppDataSource mRepository;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setContentView(R.layout.activity_install_apps);
    }

    private InstallAppsRVAdapter installAppsRVAdapter;
    private RecyclerView rcv_install_apps;
    private ProgressBar pb_loading;
    private ImageView iv_back;

    @Override
    public void initView() {
        rcv_install_apps = (RecyclerView) findViewById(R.id.rcv_install_apps);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
    }

    @Override
    public void initData(){

        findViewById(R.id.iv_back).setOnClickListener(this);

        mRepository = new AppRepository(mContext);
        installAppsRVAdapter = new InstallAppsRVAdapter(mContext);
        installAppsRVAdapter.setCallBack(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_install_apps.setLayoutManager(llm);
        rcv_install_apps.setAdapter(installAppsRVAdapter);
        pb_loading.setVisibility(View.VISIBLE);
        mRepository.getInstalledApps(mContext).done(this::InstallApps);

    }

    public void InstallApps(List<AppModel> arrayData){
        pb_loading.setVisibility(View.GONE);
        if (installAppsRVAdapter != null)
            installAppsRVAdapter.setArrayData(arrayData);
        for (int i=0;i<arrayData.size();i++){
            AppModel appModel = arrayData.get(i);
            Log.e(TAG, "InstallApps: pakageName="+appModel.packageName +"//name="+appModel.name+"//versionCode="+appModel.versionCode);
        }
    }

    @Override
    public void onClick(View view){
        super.onClick(view);
        finish();
    }

    ProgressDialog createAppDialog;

    @Override
    public void callback(AppModel itemModel) {
        createAppDialog = ProgressDialog.show(this, "请等待", "正在创建...");


        class UpdateTask extends AsyncTask<String, Integer, String> {
            protected void onPreExecute() {
            }

            protected String doInBackground(String... params) {
                int flags = InstallStrategy.COMPARE_VERSION;
                if (itemModel.fastOpen) {
                    flags |= InstallStrategy.DEPEND_SYSTEM_IF_EXIST;
                }
                VirtualCore.get().installPackage(itemModel.path, flags);
                InstalledAppInfo info = VirtualCore.get().getInstalledAppInfo(itemModel.packageName, 0);
                if (info != null) {
                    if (info.dependSystem) {
//                        createAppDialog.dismiss();
//                        dataChanged();
                        return "end";
                    }
                    itemModel.context = InstallAppsActivity.this;
                    VUiKit.defer().when(() -> {
                        try {
                            itemModel.loadData(info.getApplicationInfo(VUserHandle.USER_OWNER));
                            VirtualCore.get().preOpt(info.packageName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).done((res) -> {
//                        dataChanged();
                    });
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "end";
            }

            protected void onPostExecute(String result) {
                createAppDialog.dismiss();
                if (itemModel!=null){
                    dataChanged(itemModel);
                }else{
                    ToastUtil.centerToast(mContext,"创建失败");
                }

            }

            protected void onProgressUpdate(Integer... values) {

            }
        }

        UpdateTask  task = new UpdateTask();
        task.execute("","");
    }

    private void dataChanged(AppModel itemModel) {
        Intent data = new Intent();
        data.putExtra(VCommends.EXTRA_APP_MODEL, "virtualApps");
        data.putExtra("appmodel",itemModel);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

}
