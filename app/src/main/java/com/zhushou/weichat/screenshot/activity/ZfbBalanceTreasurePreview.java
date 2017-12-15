package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;

/**
 * Created by zhanglinkai on 2017/4/13.
 * 功能:
 */

public class ZfbBalanceTreasurePreview extends Activity {
    private ImageView balance_treasure_preview_title_iv;
    private TextView balance_treasure_preview_symoney_tv;
    private TextView balance_treasure_preview_allmoney_tv;
    private TextView balance_treasure_preview_percentage_tv;
    private ImageView balance_treasure_preview_select_iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_balance_treasure_preview);
        iniUI();
    }

    private void iniUI() {
        balance_treasure_preview_title_iv=(ImageView)findViewById(R.id.balance_treasure_preview_title_iv);
        balance_treasure_preview_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        balance_treasure_preview_symoney_tv=(TextView)findViewById(R.id.balance_treasure_preview_symoney_tv);
        balance_treasure_preview_allmoney_tv=(TextView)findViewById(R.id.balance_treasure_preview_allmoney_tv);
        balance_treasure_preview_percentage_tv=(TextView)findViewById(R.id.balance_treasure_preview_percentage_tv);
        balance_treasure_preview_select_iv=(ImageView) findViewById(R.id.balance_treasure_preview_select_iv);

        String money=getIntent().getStringExtra("money");
        String percentage=getIntent().getStringExtra("percentage");
        boolean isSelect=getIntent().getBooleanExtra("isSelect",false);


        float m=Float.valueOf(percentage);
        DecimalFormat fnum  =  new  DecimalFormat("#,###.0000");
        balance_treasure_preview_percentage_tv.setText(fnum.format(m));
        if (isSelect){
            balance_treasure_preview_select_iv.setImageResource(R.mipmap.alipay_switch_on);
            balance_treasure_preview_allmoney_tv.setText("****");
        }else{
            balance_treasure_preview_select_iv.setImageResource(R.mipmap.alipay_switch_off);
            balance_treasure_preview_allmoney_tv.setText(StaticMethod.keepTwoDecimal(money));
        }


        float n=Float.valueOf(money);
        String t=StaticMethod.keepTwoDecimal(n/365*m/100+"");
        DecimalFormat f  =  new  DecimalFormat("##.00");
        float a=Float.valueOf(f.format(n/365*m/100));
        if (a<=0){
            balance_treasure_preview_symoney_tv.setText(R.string.zanwushouyi);
        }else{
            balance_treasure_preview_symoney_tv.setText(t.toString());
        }


    }
}
