package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhushou.weichat.R;


/**
 * Created by zhanglinkai on 2017/3/14.
 * 功能:
 */

public class WxPocketMoney extends Activity implements View.OnClickListener{
    private boolean isshow=false;//收费规则
    private Button pocketmoney_preview_btn;
    private EditText moneypacket_et;
    private RelativeLayout rule_layout;
    private ImageView rule_layout_iv;
    private ImageView wxmoneypacket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wxpacketmoney);
        iniUI();
    }

    private void iniUI() {
        pocketmoney_preview_btn=(Button)findViewById(R.id.pocketmoney_preview_btn);
        moneypacket_et=(EditText)findViewById(R.id.moneypacket_et);
        rule_layout=(RelativeLayout)findViewById(R.id.rule_layout);
        rule_layout_iv=(ImageView)findViewById(R.id.rule_layout_iv);
        wxmoneypacket=(ImageView)findViewById(R.id.wxmoneypacket);


        pocketmoney_preview_btn.setOnClickListener(this);
        moneypacket_et.setOnClickListener(this);
        rule_layout.setOnClickListener(this);
        wxmoneypacket.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.pocketmoney_preview_btn:
                String money=moneypacket_et.getText().toString().trim();
                if (!money.isEmpty()){
                    Intent intent=new Intent(WxPocketMoney.this,WxPocketMoneyPreView.class);
                    intent.putExtra("money",money);
                    intent.putExtra("rule",isshow);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, R.string.qqbalance_toast, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.moneypacket_et:
                moneypacket_et.setFocusable(true);
                moneypacket_et.setFocusableInTouchMode(true);
                moneypacket_et.requestFocusFromTouch();
                break;
            case R.id.rule_layout:
                if (isshow==false){
                    isshow=true;
                    rule_layout_iv.setBackgroundResource(R.mipmap.webviewtab_back_disable);
                }else{
                    isshow=false;
                    rule_layout_iv.setBackgroundResource(R.mipmap.webviewtab_back_normal);
                }
                break;
            case R.id.wxmoneypacket:
                finish();
                break;

        }
    }
}
