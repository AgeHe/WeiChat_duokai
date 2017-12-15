package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;

/**
 * Created by zhanglinkai on 2017/4/13.
 * 功能:
 */

public class ZfbBalancePreiew extends Activity {
    private TextView zfb_balance_preview_money;
    private ImageView zfb_balance_preview_title_iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_zfb_balance_preview);
        iniUI();
    }

    private void iniUI() {
        zfb_balance_preview_money=(TextView)findViewById(R.id.zfb_balance_preview_money);
        zfb_balance_preview_title_iv=(ImageView)findViewById(R.id.zfb_balance_preview_title_iv);
        zfb_balance_preview_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String money=getIntent().getStringExtra("money");
        zfb_balance_preview_money.setText(StaticMethod.keepTwoDecimal(money));
    }
}
