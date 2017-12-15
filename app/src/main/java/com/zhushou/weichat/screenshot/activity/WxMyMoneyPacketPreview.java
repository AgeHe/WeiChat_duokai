package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;

/**
 * Created by zhanglinkai on 2017/3/31.
 * 功能:
 */

public class WxMyMoneyPacketPreview extends Activity {
    private ImageView mymoneypacket_preview_title_iv;
    private TextView mymoneypacket_preview_money_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wxmymoneypacketpreview);
        iniUI();
    }

    private void iniUI() {
        String money=getIntent().getStringExtra("money");
        mymoneypacket_preview_title_iv=(ImageView)findViewById(R.id.mymoneypacket_preview_title_iv);
        mymoneypacket_preview_money_tv=(TextView)findViewById(R.id.mymoneypacket_preview_money_tv);

        mymoneypacket_preview_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mymoneypacket_preview_money_tv.setText(getResources().getString(R.string.renminbi)+ StaticMethod.keepTwoDecimalNo(money));
    }
}
