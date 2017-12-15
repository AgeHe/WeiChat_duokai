package com.zhushou.weichat.auxiliary.base;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;

import com.zhushou.weichat.auxiliary.interfac.BaseViewInterface;
import com.zhushou.weichat.auxiliary.service.DevicePolicyManagerReceiver;
import com.zhushou.weichat.auxiliary.ui.dialog.AuxiliarySystemSettingDialog;
import com.zhushou.weichat.auxiliary.utils.CheckAuxiliarySwitch;
import com.zhushou.weichat.ui.pay.PayActivity;

/**
 * Created by Administrator on 2017/3/15.
 */

public abstract class BaseAuxiliaryActivity extends PayActivity implements BaseViewInterface,View.OnClickListener,AuxiliarySystemSettingDialog.AuxiliaryLisener {
    public static final String TAG = "BaseAuxiliaryActivity";
    public Resources mResources;
    public Context mContext;
    public AuxiliarySystemSettingDialog auxiliarySystemSettingDialog;
    private DevicePolicyManager policyManager; // 设备管理器
    private ComponentName componentName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mResources = this.getResources();
        this.mContext = this;
        init(savedInstanceState);
        auxiliarySystemSettingDialog = new AuxiliarySystemSettingDialog(this);
        initView();
        //获取设备管理服务
        policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        //AdminReceiver 继承自 DeviceAdminReceiver
        componentName = new ComponentName(this, DevicePolicyManagerReceiver.class);

        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAuxiliarySetting();
    }

    protected void init(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View view) {
        viewOnClick(view);
    }

    protected void viewOnClick(View view){}

    @Override
    public void onAuxiliaryClick() {
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }

    public boolean checkPolicymanager(){
        boolean active = policyManager.isAdminActive(componentName);
        if (!active){
            return false;
        }else{
            return true;
        }
    }

    public boolean checkAuxiliarySetting(){
        if (!CheckAuxiliarySwitch.isAccessibilitySettingsOn(this)&&auxiliarySystemSettingDialog!=null){
            auxiliarySystemSettingDialog.readioDialog(this);
            return false;
        }else{
            return true;
        }
    }


    /**
     * 获取设备管理者权限
     */
    public void activeManage() {
        // 启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        //权限列表
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        //描述(additional explanation)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, Html.fromHtml("<p><h4><font color=\"#e32a2a\">该应用仅使用锁屏功能。</h4></p>"));
        startActivityForResult(intent, 303);
    }
}
