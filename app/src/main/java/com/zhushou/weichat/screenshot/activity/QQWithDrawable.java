package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhushou.weichat.R;


/**
 * Created by zhanglinkai on 2017/4/14.
 * 功能:
 */

public class QQWithDrawable extends Activity implements View.OnClickListener{
    private ImageView qq_withdrawable_title_iv;
    private TextView qq_withdrawable_from_money;
    private TextView qq_withdrawable_to_money;
    private EditText qq_withdrawable_money;
    private EditText qq_withdrawable_name;
    private Button qq_withdrawable_btn_preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_qq_withdrawable);
        iniUI();
    }

    private void iniUI() {
        qq_withdrawable_title_iv=(ImageView)findViewById(R.id.qq_withdrawable_title_iv);
        qq_withdrawable_from_money=(TextView)findViewById(R.id.qq_withdrawable_from_money);
        qq_withdrawable_to_money=(TextView)findViewById(R.id.qq_withdrawable_to_money);
        qq_withdrawable_money=(EditText)findViewById(R.id.qq_withdrawable_money);
        qq_withdrawable_name=(EditText)findViewById(R.id.qq_withdrawable_name);
        qq_withdrawable_btn_preview=(Button)findViewById(R.id.qq_withdrawable_btn_preview); 

        qq_withdrawable_title_iv.setOnClickListener(this);
        qq_withdrawable_from_money.setOnClickListener(this);
        qq_withdrawable_to_money.setOnClickListener(this);
        qq_withdrawable_btn_preview.setOnClickListener(this);
    }
    String type="from";
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qq_withdrawable_title_iv:
                finish();
                break;
            case R.id.qq_withdrawable_from_money:
                qq_withdrawable_from_money.setTextColor(getResources().getColor(R.color.color_white));
                qq_withdrawable_from_money.setBackgroundResource(R.drawable.blue_shape);
                qq_withdrawable_to_money.setTextColor(getResources().getColor(R.color.blue));
                qq_withdrawable_to_money.setBackgroundResource(R.drawable.white_shape);
                type="from";
                qq_withdrawable_name.setVisibility(View.GONE);
                break;
            case R.id.qq_withdrawable_to_money:
                qq_withdrawable_to_money.setTextColor(getResources().getColor(R.color.color_white));
                qq_withdrawable_to_money.setBackgroundResource(R.drawable.blue_shape);
                qq_withdrawable_from_money.setTextColor(getResources().getColor(R.color.blue));
                qq_withdrawable_from_money.setBackgroundResource(R.drawable.white_shape);
                type="to";
                qq_withdrawable_name.setVisibility(View.VISIBLE);
                break;
            case R.id.qq_withdrawable_btn_preview:
                
                String money=qq_withdrawable_money.getText().toString().trim();
                String name=qq_withdrawable_name.getText().toString().trim();
                if (!money.isEmpty()&&type.equals("from")||!money.isEmpty()&&!name.isEmpty()&&type.equals("to")) {
                    Intent intent = new Intent(this, QQWithDrawablePreview.class);
                    intent.putExtra("type", type);
                    intent.putExtra("money", money);
                    if (type.equals("to")) {
                        intent.putExtra("name", name);
                    }
                    startActivity(intent);
                }else{
                    Toast.makeText(this, getString(R.string.qqbalance_toast), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
