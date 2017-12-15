package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;


/**
 * Created by zhanglinkai on 2017/3/14.
 * 功能:
 */

public class WxPocketMoneyPreView extends Activity {
    private TextView moneypacket_preview_money;
    private TextView rule_preview_layout;
    private ImageView packetmoney_preview_iv;
    private ImageView packetmoney_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wxpacketmoney_preview);

        iniUI();
    }

    private void iniUI() {
        Float index=getSharedPreferences("index", Context.MODE_PRIVATE).getFloat("index",0);
        moneypacket_preview_money=(TextView)findViewById(R.id.moneypacket_preview_money);
        rule_preview_layout=(TextView)findViewById(R.id.rule_preview_layout);
        packetmoney_preview_iv=(ImageView)findViewById(R.id.packetmoney_preview_iv);
        packetmoney_title=(ImageView)findViewById(R.id.packetmoney_title);

        //StaticMethod.adaptiveView(packetmoney_preview_iv,300,300,index);

        String money=getIntent().getStringExtra("money").toString();
        boolean rule=getIntent().getBooleanExtra("rule",false);

        moneypacket_preview_money.setText(getString(R.string.renminbi)+StaticMethod.keepTwoDecimalNo(money));

        if (rule){
            rule_preview_layout.setVisibility(View.VISIBLE);
        }else{
            rule_preview_layout.setVisibility(View.GONE);
        }

        packetmoney_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
