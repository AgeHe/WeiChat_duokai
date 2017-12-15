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
 * Created by zhanglinkai on 2017/4/13.
 * 功能:
 */

public class ZfbBalance extends Activity {
    private ImageView zfb_balance_title_iv;
    private EditText zfb_balance_money_et;
    private Button zfb_balance_preview_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_zfb_balance);
        iniUI();
    }

    private void iniUI() {
        zfb_balance_title_iv=(ImageView)findViewById(R.id.zfb_balance_title_iv);
        zfb_balance_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        zfb_balance_money_et=(EditText)findViewById(R.id.zfb_balance_money_et);
        zfb_balance_preview_btn=(Button)findViewById(R.id.zfb_balance_preview_btn);
        zfb_balance_preview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zfb_balance_money_et.getText().toString().trim().isEmpty()){
                    Toast.makeText(ZfbBalance.this,R.string.qqbalance_toast, Toast.LENGTH_SHORT).show();
              }else{
                    Intent intent=new Intent(ZfbBalance.this,ZfbBalancePreiew.class);
                    intent.putExtra("money",zfb_balance_money_et.getText().toString().trim());
                    startActivity(intent);
                }
            }
        });
    }
}
