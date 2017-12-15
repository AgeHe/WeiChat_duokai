package com.zhushou.weichat.addfriends.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhushou.weichat.R;
import com.zhushou.weichat.addfriends.base.AFBaseActivity;
import com.zhushou.weichat.addfriends.base.TellSelectorMessageInfo;
import com.zhushou.weichat.addfriends.ui.dialog.QuantityChoiceDialog;
import com.zhushou.weichat.addfriends.ui.mode.InsertContactsSource;
import com.zhushou.weichat.addfriends.ui.mode.QYContacts;
import com.zhushou.weichat.addfriends.widget.ALoadingDialog;
import com.zhushou.weichat.addfriends.widget.SelectNumberDialog;

/**
 * Created by Administrator on 2017/3/30.
 */

public class HDAddFriendsActivity extends AFBaseActivity implements SelectNumberDialog.SelectNumberLisener,
        InsertContactsSource.ViewInterface{


    private EditText et_isselector_number;
    private TextView tv_selector_number;
    private ALoadingDialog aLoadingDialog;

    private TellSelectorMessageInfo tellSelectorMessageInfo;
    private InsertContactsSource.ViewController viewController;
    public QuantityChoiceDialog quantityChoiceDialog;

    @Override
    protected void init(Bundle savedInstanceState){
        super.init(savedInstanceState);
        setContentView(R.layout.activity_add_friends_hd);
    }

    @Override
    public void initView() {
        et_isselector_number = (EditText) findViewById(R.id.et_isselector_number);
        tv_selector_number = (TextView) findViewById(R.id.tv_selector_number);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_button_qy).setOnClickListener(this);

        findViewById(R.id.rl_select_hd_number).setOnClickListener(this);
        findViewById(R.id.rl_select_number).setOnClickListener(this);
        findViewById(R.id.tv_hd_web).setOnClickListener(this);

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

                if (et_isselector_number.getText().toString()==null||et_isselector_number.getText().toString().length()<7)
                    return;
                tellSelectorMessageInfo.setShortCode(et_isselector_number.getText().toString());
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
            case R.id.rl_select_hd_number:

                break;
            case R.id.rl_select_number:
                quantityChoiceDialog.showQuantityChoice(quantityChoiceDialog.QuantityChoice);
                break;
            case R.id.tv_hd_web:
                startActivity(new Intent(HDAddFriendsActivity.this,WebActivity.class));
                break;

        }
    }

    @Override
    public void number(int value) {
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
        Toast.makeText(HDAddFriendsActivity.this,mResources.getString(R.string.wx_addf_add_friends_toastS),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void endExceptionStatu() {
        aLoadingDialog.dismiss();
        Toast.makeText(HDAddFriendsActivity.this,mContext.getResources().getString(R.string.wx_addf_e_toast),Toast.LENGTH_SHORT).show();
    }
}
