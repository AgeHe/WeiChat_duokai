package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;

/**
 * Created by zhanglinkai on 2017/4/17.
 * 功能:
 */

public class QQBalancePreview extends Activity {
    private ImageView qqbalance_preview_title_iv;
    private TextView qqbalance_preview_money_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_qqbalance_preview);
        iniUI();
    }

    private void iniUI() {
        qqbalance_preview_title_iv=(ImageView)findViewById(R.id.qqbalance_preview_title_iv);
        qqbalance_preview_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        qqbalance_preview_money_tv=(TextView)findViewById(R.id.qqbalance_preview_money_tv);
        String money=getIntent().getStringExtra("money");
        qqbalance_preview_money_tv.setText(StaticMethod.keepTwoDecimal(money)+getString(R.string.yuan));
    }
}
