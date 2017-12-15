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
 * Created by zhanglinkai on 2017/4/13.
 * 功能:
 */

public class ZfbBalanceTreasure extends Activity {
    private ImageView balance_treasure_title_iv;
    private EditText balance_treasure_money;
    private EditText balance_treasure_percentage;
    private RelativeLayout balance_treasure_tag_layout;
    private ImageView balance_treasure_tag_iv;
    private Button balance_treasure_preview_btn;
    private boolean isSelect=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_balance_treasure);
        iniUI();
    }

    private void iniUI() {
        balance_treasure_title_iv=(ImageView)findViewById(R.id.balance_treasure_title_iv);
        balance_treasure_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        balance_treasure_money=(EditText)findViewById(R.id.balance_treasure_money);
        balance_treasure_percentage=(EditText)findViewById(R.id.balance_treasure_percentage);
        balance_treasure_tag_layout=(RelativeLayout)findViewById(R.id.balance_treasure_tag_layout);
        balance_treasure_tag_iv=(ImageView)findViewById(R.id.balance_treasure_tag_iv);
        balance_treasure_preview_btn=(Button)findViewById(R.id.balance_treasure_preview_btn);
        balance_treasure_tag_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelect){
                    isSelect=false;
                    balance_treasure_tag_iv.setImageResource(R.mipmap.webviewtab_back_normal);
                }else{
                    isSelect=true;
                    balance_treasure_tag_iv.setImageResource(R.mipmap.webviewtab_back_disable);
                }
            }
        });
        balance_treasure_preview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money=balance_treasure_money.getText().toString().trim();
                String percentage=balance_treasure_percentage.getText().toString().trim();
                if (!money.isEmpty()&&!percentage.isEmpty()){
                    Intent intent=new Intent(ZfbBalanceTreasure.this,ZfbBalanceTreasurePreview.class);
                    intent.putExtra("money",money);
                    intent.putExtra("percentage",percentage);
                    intent.putExtra("isSelect",isSelect);
                    startActivity(intent);
                }else{
                    Toast.makeText(ZfbBalanceTreasure.this, R.string.qqbalance_toast, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
