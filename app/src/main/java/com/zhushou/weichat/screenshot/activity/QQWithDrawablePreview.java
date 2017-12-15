package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;

/**
 * Created by zhanglinkai on 2017/4/14.
 * 功能:
 */

public class QQWithDrawablePreview extends Activity {
    private LinearLayout qq_withdrawable_preview_title_layout;
    private TextView qq_withdrawable_preview_status_tv;
    private TextView qq_withdrawable_preview_money_tv;
    private TextView qq_withdrawable_preview_des_tv;
    private LinearLayout qq_withdrawable_preview_from_layout;
    private ImageView qq_withdrawable_preview_to_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_qq_withdrawable_preview);
        iniUI();
    }

    private void iniUI() {
        float index=getSharedPreferences("index", Context.MODE_PRIVATE).getFloat("index",1);
        qq_withdrawable_preview_title_layout=(LinearLayout)findViewById(R.id.qq_withdrawable_preview_title_layout);
        qq_withdrawable_preview_title_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        qq_withdrawable_preview_status_tv=(TextView)findViewById(R.id.qq_withdrawable_preview_status_tv);
        qq_withdrawable_preview_money_tv=(TextView)findViewById(R.id.qq_withdrawable_preview_money_tv);
        qq_withdrawable_preview_des_tv=(TextView)findViewById(R.id.qq_withdrawable_preview_des_tv);
        qq_withdrawable_preview_from_layout=(LinearLayout)findViewById(R.id.qq_withdrawable_preview_from_layout);
        qq_withdrawable_preview_to_layout=(ImageView)findViewById(R.id.qq_withdrawable_preview_to_layout);


        //StaticMethod.adaptiveView(qq_withdrawable_preview_to_layout,1080,600,index);
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int h=display.getHeight();
        if (h>2000){
            p.topMargin=300;
            qq_withdrawable_preview_to_layout.setLayoutParams(p);
        }



        String type=getIntent().getStringExtra("type");
        String money=getIntent().getStringExtra("money");
        if (type.equals("from")){
            qq_withdrawable_preview_status_tv.setText(getString(R.string.skcg));
            qq_withdrawable_preview_des_tv.setText(getString(R.string.yzrndye));
            qq_withdrawable_preview_from_layout.setVisibility(View.VISIBLE);
            qq_withdrawable_preview_to_layout.setVisibility(View.GONE);
        }else if (type.equals("to")){
            String name=getIntent().getStringExtra("name");
            qq_withdrawable_preview_status_tv.setText(getString(R.string.zzcg));
            qq_withdrawable_preview_des_tv.setText(getString(R.string.yzr)+name+getString(R.string.dye));
            qq_withdrawable_preview_from_layout.setVisibility(View.GONE);
            qq_withdrawable_preview_to_layout.setVisibility(View.VISIBLE);
        }

        qq_withdrawable_preview_money_tv.setText(StaticMethod.keepTwoDecimal(money));

    }
}
