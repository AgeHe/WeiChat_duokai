package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2017/4/17.
 * 功能:
 */

public class QQBalance extends Activity implements View.OnClickListener{
    private ImageView qq_balance_titile_iv;
    private EditText qq_balance_money_et;
    private Button qq_balance_btn_preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_qqbalance);
        iniUI();
    }

    private void iniUI() {
        qq_balance_titile_iv=(ImageView)findViewById(R.id.qq_balance_titile_iv);
        qq_balance_money_et=(EditText)findViewById(R.id.qq_balance_money_et);
        qq_balance_btn_preview=(Button)findViewById(R.id.qq_balance_btn_preview);


        qq_balance_titile_iv.setOnClickListener(this);
        qq_balance_btn_preview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qq_balance_titile_iv:
                finish();
                break;
            case R.id.qq_balance_btn_preview:

                String money=qq_balance_money_et.getText().toString().trim();
                if (!money.isEmpty()){
                    Intent intent=new Intent(this,QQBalancePreview.class);
                    intent.putExtra("money",money);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, getString(R.string.qqbalance_toast), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
