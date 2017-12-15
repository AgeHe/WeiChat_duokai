package com.zhushou.weichat.addfriends.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhushou.weichat.R;
import com.zhushou.weichat.addfriends.base.AFBaseActivity;
import com.zhushou.weichat.addfriends.base.TellSelectorMessageInfo;
import com.zhushou.weichat.addfriends.ui.dialog.QuantityChoiceDialog;
import com.zhushou.weichat.addfriends.ui.mode.InsertContactsSource;
import com.zhushou.weichat.addfriends.ui.mode.QYContacts;
import com.zhushou.weichat.addfriends.widget.ALoadingDialog;
import com.zhushou.weichat.addfriends.widget.LocationSelectorDialogBuilder;
import com.zhushou.weichat.addfriends.widget.SelectNumberDialog;

/**
 * Created by Administrator on 2017/3/28.
 */

public  class QYAddFriendsActivity extends AFBaseActivity implements LocationSelectorDialogBuilder.OnSaveLocationLister,
        SelectNumberDialog.SelectNumberLisener,InsertContactsSource.ViewInterface {

    public QuantityChoiceDialog quantityChoiceDialog;
    //选中的 区域 和 添加数量 显示View
    private TextView tv_isselector_locality,tv_selector_number;
    private LocationSelectorDialogBuilder locationBuilder;
    private TellSelectorMessageInfo tellSelectorMessageInfo;
    private InsertContactsSource.ViewController viewController;
    private ALoadingDialog aLoadingDialog;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setContentView(R.layout.activity_add_friends_qy);
    }

    @Override
    public void initView() {
        tv_selector_number = (TextView) findViewById(R.id.tv_selector_number);
        tv_isselector_locality = (TextView) findViewById(R.id.tv_isselector_locality);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_button_qy).setOnClickListener(this);

        findViewById(R.id.rl_select_locality).setOnClickListener(this);
        findViewById(R.id.rl_select_number).setOnClickListener(this);
    }

    @Override
    public void initData() {
        viewController = new QYContacts(this,this);
        tellSelectorMessageInfo = new TellSelectorMessageInfo();
        quantityChoiceDialog = new QuantityChoiceDialog(mContext, this);
        aLoadingDialog = new ALoadingDialog(mContext);

    }
    @Override
    protected void viewOnClick(View view) {
        super.viewOnClick(view);
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_button_qy:
                if (tellSelectorMessageInfo==null)
                    return;
//                if (tellSelectorMessageInfo.getCityName()==null)
//                    return;
                if (tellSelectorMessageInfo.getContactsSum()<=0)
                    return;
                if (tellSelectorMessageInfo.getShortCode()==null)
                    return;

                viewController.insertContacts(tellSelectorMessageInfo);
                break;
            case R.id.rl_select_locality:
                if (locationBuilder == null) {
                    locationBuilder = LocationSelectorDialogBuilder.getInstance(this);
                    locationBuilder.setOnSaveLocationLister(this);
                }
                locationBuilder.show();
                break;
            case R.id.rl_select_number:
                quantityChoiceDialog.showQuantityChoice(quantityChoiceDialog.QuantityChoice);
                break;
        }
    }

    @Override
    public void onSaveLocation(String location, String provinceId, String cityId) {
        tellSelectorMessageInfo.setCityName(location);
        tellSelectorMessageInfo.setShortCode(cityId);
//        Toast.makeText(QYAddFriendsActivity.this,
//                "location="+location+"// provinceId="+provinceId+"// cityId="+cityId,
//                Toast.LENGTH_SHORT).show();
        tv_isselector_locality.setText(location);
    }

    @Override
    public void number(int value) {
        if (value>0)
        tellSelectorMessageInfo.setContactsSum(value);
        tv_selector_number.setText(String.valueOf(value));
    }

    @Override
    public void startTackleContacts() {
        aLoadingDialog.setMessage(mResources.getString(R.string.wx_addf_add_friends_toastA));
        aLoadingDialog.loadingShow();
    }

    @Override
    public void endTackleContactsLoading() {
        aLoadingDialog.dismiss();
        Toast.makeText(QYAddFriendsActivity.this,mResources.getString(R.string.wx_addf_add_friends_toastS),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void endExceptionStatu() {
        aLoadingDialog.dismiss();
        Toast.makeText(QYAddFriendsActivity.this,mContext.getResources().getString(R.string.wx_addf_e_toast),Toast.LENGTH_SHORT).show();

    }
}
