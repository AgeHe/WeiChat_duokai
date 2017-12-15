package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2017/3/14.
 * 功能:
 */

public class WxWithDrawals extends Activity implements View.OnClickListener{

    private Button withdrawals_preview_btn;
    private RelativeLayout wxwithdrawals_bank;
    private RelativeLayout withdrawals_poundage;
    private TextView withdrawals_bank_tv;
    private EditText withdrawals_bankcard_et;
    private EditText withdrawals_money_et;
    private ImageView withdrawals_poundage_iv;
    private ImageView title_wxwithdrawals_iv;
    private boolean isshow=false;//手续费显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wxwithdrawals);
        iniUI();
    }

    private void iniUI() {
        withdrawals_preview_btn=(Button)findViewById(R.id.withdrawals_preview_btn);
        wxwithdrawals_bank=(RelativeLayout)findViewById(R.id.wxwithdrawals_bank);
        withdrawals_poundage=(RelativeLayout)findViewById(R.id.withdrawals_poundage);
        withdrawals_bank_tv=(TextView)findViewById(R.id.withdrawals_bank_tv);
        withdrawals_bankcard_et=(EditText)findViewById(R.id.withdrawals_bankcard_et);
        withdrawals_money_et=(EditText)findViewById(R.id.withdrawals_money_et);
        withdrawals_poundage_iv=(ImageView)findViewById(R.id.withdrawals_poundage_iv);
        title_wxwithdrawals_iv=(ImageView)findViewById(R.id.title_wxwithdrawals_iv);

        withdrawals_preview_btn.setOnClickListener(this);
        wxwithdrawals_bank.setOnClickListener(this);
        withdrawals_bankcard_et.setOnClickListener(this);
        withdrawals_money_et.setOnClickListener(this);
        withdrawals_poundage.setOnClickListener(this);
        title_wxwithdrawals_iv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.withdrawals_preview_btn:
                String bank=withdrawals_bank_tv.getText().toString().trim();
                String bankcard=withdrawals_bankcard_et.getText().toString().trim();
                String money=withdrawals_money_et.getText().toString().trim();
                if (!bank.isEmpty()&&!bankcard.isEmpty()&&!money.isEmpty()) {
                    Intent intent = new Intent(WxWithDrawals.this, WxWithDrawalsPreView.class);
                    intent.putExtra("bank", bank);
                    intent.putExtra("bankcard", bankcard);
                    intent.putExtra("money", money);
                    intent.putExtra("poundage", isshow);
                    startActivity(intent);
                }else {
                    Toast.makeText(this, R.string.qqbalance_toast, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.wxwithdrawals_bank:
                selectBank();
                break;
            case R.id.withdrawals_bankcard_et:
                withdrawals_bankcard_et.setFocusable(true);
                withdrawals_bankcard_et.setFocusableInTouchMode(true);
                withdrawals_bankcard_et.requestFocusFromTouch();
                break;
            case R.id.withdrawals_money_et:
                withdrawals_money_et.setFocusable(true);
                withdrawals_money_et.setFocusableInTouchMode(true);
                withdrawals_money_et.requestFocusFromTouch();
                break;
            case R.id.withdrawals_poundage:
                if (isshow==false){
                    isshow=true;
                    withdrawals_poundage_iv.setBackgroundResource(R.mipmap.webviewtab_back_disable);
                }else{
                    isshow=false;
                    withdrawals_poundage_iv.setBackgroundResource(R.mipmap.webviewtab_back_normal);
                }
                break;
            case R.id.title_wxwithdrawals_iv:
                finish();
                break;
        }
    }
    private void selectBank(){
        final String[] s=getResources().getStringArray(R.array.bank);
        String s1=withdrawals_bank_tv.getText().toString().trim();
        int m=0;
        if (!s1.isEmpty()){
            for (int i=0;i<s.length;i++){
                if (s1.equals(s[i])){
                    m=i;
                    break;
                }
            }
        }
        final AlertDialog dialog=new AlertDialog.Builder(this).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        View view= LayoutInflater.from(this).inflate(R.layout.layout_select_bank,null);
        dialog.setView(view);
        TextView selctbank_dialog_cancle=(TextView)view.findViewById(R.id.selctbank_dialog_cancle);
        TextView selctbank_dialog_sure=(TextView)view.findViewById(R.id.selctbank_dialog_sure);
        selctbank_dialog_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final NumberPicker picker_bank=(NumberPicker)view.findViewById(R.id.picker_bank);
        picker_bank.setDisplayedValues(s);
        picker_bank.setMinValue(0);
        picker_bank.setValue(m);
        picker_bank.setMaxValue(s.length-1);
        selctbank_dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawals_bank_tv.setText(s[picker_bank.getValue()]);
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
