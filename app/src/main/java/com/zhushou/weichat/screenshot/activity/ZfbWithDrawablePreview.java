package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;

import static com.zhushou.weichat.screenshot.Utils.StaticMethod.StringPattern;

/**
 * Created by zhanglinkai on 2017/4/13.
 * 功能:
 */

public class ZfbWithDrawablePreview extends Activity {
    private ImageView zfb_withdrawable_preview_title_iv;
    private ImageView zfb_withdrawable_preview_icon;
    private TextView zfb_withdrawable_preview_bank_tv;
    private TextView zfb_withdrawable_preview_money_tv;
    private TextView zfb_withdrawable_preview_time_tv1;
    private TextView zfb_withdrawable_preview_time_tv2;
    private TextView zfb_withdrawable_preview_time_tv3;
    private TextView zfb_withdrawable_preview_withdrawable_tv;
    private TextView zfb_withdrawable_preview_withdrawable_time;
    private TextView zfb_withdrawable_preview_fwf_tv;
    private RelativeLayout zfb_withdrawable_preview_fwf_layout;
    private TextView dingdan_tv;
    private TextView shdingdanhao_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_zfb_withdrawablepreview);
        iniUI();
    }

    private void iniUI(){
        zfb_withdrawable_preview_title_iv=(ImageView)findViewById(R.id.zfb_withdrawable_preview_title_iv);
        zfb_withdrawable_preview_icon=(ImageView)findViewById(R.id.zfb_withdrawable_preview_icon);
        zfb_withdrawable_preview_bank_tv=(TextView)findViewById(R.id.zfb_withdrawable_preview_bank_tv);
        zfb_withdrawable_preview_money_tv=(TextView)findViewById(R.id.zfb_withdrawable_preview_money_tv);
        zfb_withdrawable_preview_time_tv1=(TextView)findViewById(R.id.zfb_withdrawable_preview_time_tv1);
        zfb_withdrawable_preview_time_tv2=(TextView)findViewById(R.id.zfb_withdrawable_preview_time_tv2);
        zfb_withdrawable_preview_time_tv3=(TextView)findViewById(R.id.zfb_withdrawable_preview_time_tv3);
        zfb_withdrawable_preview_withdrawable_tv=(TextView)findViewById(R.id.zfb_withdrawable_preview_withdrawable_tv);
        zfb_withdrawable_preview_withdrawable_time=(TextView)findViewById(R.id.zfb_withdrawable_preview_withdrawable_time);
        zfb_withdrawable_preview_fwf_tv=(TextView)findViewById(R.id.zfb_withdrawable_preview_fwf_tv);
        zfb_withdrawable_preview_fwf_layout=(RelativeLayout)findViewById(R.id.zfb_withdrawable_preview_fwf_layout);
        dingdan_tv=(TextView)findViewById(R.id.dingdan_tv);
        shdingdanhao_tv=(TextView)findViewById(R.id.shdingdanhao_tv);


        String bank=getIntent().getStringExtra("bank");
        String card=getIntent().getStringExtra("card");
        String name=getIntent().getStringExtra("name");
        String money=getIntent().getStringExtra("money");
        String ttime=getIntent().getStringExtra("ttime");
        String ctime=getIntent().getStringExtra("ctime");
        boolean isSelect=getIntent().getBooleanExtra("isSelect",false);

        adapterView(bank);
        float m=Float.valueOf(money);
        DecimalFormat fnum  =  new  DecimalFormat("#,###0.00");
        zfb_withdrawable_preview_money_tv.setText(String.format("%s",fnum.format(m)));

        if (isSelect){
            if (m<100){
                zfb_withdrawable_preview_fwf_tv.setText(String.format("%s",fnum.format(0.10)));
            }else{
                zfb_withdrawable_preview_fwf_tv.setText(String.format("%s",fnum.format(m/1000)));
            }
            zfb_withdrawable_preview_fwf_layout.setVisibility(View.VISIBLE);
        }else{
            zfb_withdrawable_preview_fwf_layout.setVisibility(View.GONE);
        }

        zfb_withdrawable_preview_time_tv1.setText(strig2date(ttime));
        zfb_withdrawable_preview_time_tv2.setText(strig2date(ttime));
        zfb_withdrawable_preview_time_tv3.setText(strig2date(ctime));

        zfb_withdrawable_preview_withdrawable_tv.setText(bank+"("+card+")"+name);
        zfb_withdrawable_preview_withdrawable_time.setText(ttime);

        dingdan_tv.setText(StringPattern(ttime,"yyyy-MM-dd HH:mm","yyyyMMddHHmm")+ StaticMethod.getRandomString(24));
        shdingdanhao_tv.setText(StringPattern(ttime,"yyyy-MM-dd HH:mm","yyyyMMddHHmm")+ StaticMethod.getRandomString(24));


        zfb_withdrawable_preview_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void adapterView(String bank){
        zfb_withdrawable_preview_bank_tv.setText(bank);
        if (bank.equals(getResources().getString(R.string.gsyh))){
            zfb_withdrawable_preview_icon.setImageResource(R.mipmap.bank_icbc);
            return;
        }
        if (bank.equals(getResources().getString(R.string.yzcx))){
            zfb_withdrawable_preview_icon.setImageResource(R.mipmap.bank_psbc);
            return;
        }
        if (bank.equals(getResources().getString(R.string.pfyh))){
            zfb_withdrawable_preview_icon.setImageResource(R.mipmap.bank_spdb);
            return;
        }
        if (bank.equals(getResources().getString(R.string.xys))){
            zfb_withdrawable_preview_icon.setImageResource(R.mipmap.bank_ydrcb);
            return;
        }
        if (bank.equals(getResources().getString(R.string.hxyh))){
            zfb_withdrawable_preview_icon.setImageResource(R.mipmap.bank_hxbank);
            return;
        }
        if (bank.equals(getResources().getString(R.string.nyyh))){
            zfb_withdrawable_preview_icon.setImageResource(R.mipmap.bank_abc);
            return;
        }
        if (bank.equals(getResources().getString(R.string.zgyh))){
            zfb_withdrawable_preview_icon.setImageResource(R.mipmap.bank_boc);
            return;
        }
        if (bank.equals(getResources().getString(R.string.jsyh))){
            zfb_withdrawable_preview_icon.setImageResource(R.mipmap.bank_ccb);
            return;
        }
        if (bank.equals(getResources().getString(R.string.zsyh))){
            zfb_withdrawable_preview_icon.setImageResource(R.mipmap.bank_cmb);
            return;
        }
        if (bank.equals(getResources().getString(R.string.msyh))){
            zfb_withdrawable_preview_icon.setImageResource(R.mipmap.bank_cmbc);
            return;
        }
        if (bank.equals(getResources().getString(R.string.jtyh))){
            zfb_withdrawable_preview_icon.setImageResource(R.mipmap.bank_comm);
            return;
        }
    }


    private String strig2date(String time){

        return  time.substring(time.indexOf("-")+1,time.length());

    }

}
