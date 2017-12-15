package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;

import java.text.DecimalFormat;

/**
 * Created by zhanglinkai on 2017/3/14.
 * 功能:
 */

public class WxWithDrawalsPreView extends Activity {

    private TextView withdrawals_preview_bank;
    private TextView withdrawals_preview_money;
    private RelativeLayout poundage_layout;
    private TextView poundage_tv;
    private ImageView withdrawals_iv;
    private Button withdrawals_preview_btnbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_withdrawals_preview);
        iniUI();
    }

    private void iniUI() {
        Float index=getSharedPreferences("index", Context.MODE_PRIVATE).getFloat("index",0);
        String bank=getIntent().getStringExtra("bank");
        String bankcard=getIntent().getStringExtra("bankcard");
        String money=getIntent().getStringExtra("money");
        boolean isshow=getIntent().getBooleanExtra("poundage",false);

        withdrawals_preview_bank=(TextView)findViewById(R.id.withdrawals_preview_bank);
        withdrawals_preview_money=(TextView)findViewById(R.id.withdrawals_preview_money);
        poundage_layout=(RelativeLayout)findViewById(R.id.poundage_layout);
        poundage_tv=(TextView)findViewById(R.id.poundage_tv);
        withdrawals_iv=(ImageView)findViewById(R.id.withdrawals_iv);
        withdrawals_preview_btnbtn=(Button)findViewById(R.id.withdrawals_preview_btnbtn);

        StaticMethod.adaptiveView(withdrawals_iv,280,280,index);

        withdrawals_preview_bank.setText(bank+" "+getString(R.string.weihao)+bankcard);
        withdrawals_preview_money.setText(getResources().getString(R.string.renminbi)+StaticMethod.keepTwoDecimalNo(money));
        if (isshow){
            poundage_layout.setVisibility(View.VISIBLE);
            float m=Float.valueOf(money);
            DecimalFormat fnum  =  new  DecimalFormat("##0.00");
            if (m<100){
                poundage_tv.setText(getResources().getString(R.string.renminbi)+fnum.format(0.10));
            }else{
                poundage_tv.setText(getResources().getString(R.string.renminbi)+fnum.format(m/1000));
            }

        }

        withdrawals_preview_btnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
