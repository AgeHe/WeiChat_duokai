package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;

import static com.zhushou.weichat.screenshot.Utils.StaticMethod.StringPattern;

/**
 * Created by zhanglinkai on 2017/4/12.
 * 功能:
 */

public class ZfbTransferAccountPreview extends Activity {
    private ImageView zfb_transferaccount_preview_title_iv;
    private ImageView zfb_transferaccount_preview_info_icon;
    private TextView zfb_transferaccount_preview_info_name;
    private TextView zfb_transferaccount_preview_money_tv;
    private TextView zfb_transferaccount_preview_des_tv;
    private TextView zfb_transferaccount_preview_account_tv;
    private TextView zfb_transferaccount_preview_time_tv;
    private TextView zfb_transferaccount_preview_status_tv;
    private TextView zfb_transferaccount_preview_payment_tv;
    private TextView zfb_transferaccount_preview_money_tag;
    private RelativeLayout zfb_transferaccount_preview_payment_layout;
    private ImageView zfb_transferaccount_preview_info_icon_tag;
    private TextView zfb_transferaccount_preview_ddh_tv;
    private TextView zfb_transferaccount_preview_shddh_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_zfb_transferaccount_preview);

        iniUI();
    }

    private void iniUI() {
        zfb_transferaccount_preview_title_iv=(ImageView)findViewById(R.id.zfb_transferaccount_preview_title_iv);
        zfb_transferaccount_preview_info_icon=(ImageView)findViewById(R.id.zfb_transferaccount_preview_info_icon);
        zfb_transferaccount_preview_info_name=(TextView)findViewById(R.id.zfb_transferaccount_preview_info_name);
        zfb_transferaccount_preview_money_tv=(TextView)findViewById(R.id.zfb_transferaccount_preview_money_tv);
        zfb_transferaccount_preview_des_tv=(TextView)findViewById(R.id.zfb_transferaccount_preview_des_tv);
        zfb_transferaccount_preview_account_tv=(TextView)findViewById(R.id.zfb_transferaccount_preview_account_tv);
        zfb_transferaccount_preview_time_tv=(TextView)findViewById(R.id.zfb_transferaccount_preview_time_tv);
        zfb_transferaccount_preview_status_tv=(TextView)findViewById(R.id.zfb_transferaccount_preview_status_tv);
        zfb_transferaccount_preview_payment_tv=(TextView)findViewById(R.id.zfb_transferaccount_preview_payment_tv);
        zfb_transferaccount_preview_money_tag=(TextView)findViewById(R.id.zfb_transferaccount_preview_money_tag);
        zfb_transferaccount_preview_payment_layout=(RelativeLayout)findViewById(R.id.zfb_transferaccount_preview_payment_layout);
        zfb_transferaccount_preview_info_icon_tag=(ImageView)findViewById(R.id.zfb_transferaccount_preview_info_icon_tag);
        zfb_transferaccount_preview_ddh_tv=(TextView)findViewById(R.id.zfb_transferaccount_preview_ddh_tv);
        zfb_transferaccount_preview_shddh_tv=(TextView)findViewById(R.id.zfb_transferaccount_preview_shddh_tv);

        zfb_transferaccount_preview_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String type=getIntent().getStringExtra("type");
        String icon=getIntent().getStringExtra("image");
        String name=getIntent().getStringExtra("name");
        String money=getIntent().getStringExtra("money");
        String status=getIntent().getStringExtra("status");
        String payment=getIntent().getStringExtra("payment");
        String des=getIntent().getStringExtra("des");
        String account=getIntent().getStringExtra("account");
        String time=getIntent().getStringExtra("time");
        String level=getIntent().getStringExtra("level");



        if (type.equals("from")){
            zfb_transferaccount_preview_payment_layout.setVisibility(View.GONE);
            zfb_transferaccount_preview_money_tag.setText("+");
        }else if (type.equals("to")){
            zfb_transferaccount_preview_payment_layout.setVisibility(View.VISIBLE);
            zfb_transferaccount_preview_money_tag.setText("-");

        }

        Picasso.with(this).load(new File(icon)).resize(100,100).into(zfb_transferaccount_preview_info_icon);
        zfb_transferaccount_preview_info_name.setText(name);
        zfb_transferaccount_preview_money_tv.setText(StaticMethod.keepTwoDecimal(money));

        zfb_transferaccount_preview_status_tv.setText(status);
        zfb_transferaccount_preview_payment_tv.setText(payment);
        if (des.isEmpty()){
            zfb_transferaccount_preview_des_tv.setText(getString(R.string.zhuanzhang));
        }else{
            zfb_transferaccount_preview_des_tv.setText(des);
        }

        StringBuilder builder=new StringBuilder(account);
        if (account.indexOf("@")!=-1){
            //邮箱
            int a=account.indexOf("@");
            builder.replace(3,a,"****");
        }else{
            //手机
            builder.replace(3,account.length()-4,"****");
        }
        zfb_transferaccount_preview_account_tv.setText(name+" "+builder);
        zfb_transferaccount_preview_time_tv.setText(time);

        if (level.equals(getString(R.string.dazhonghuiyuan))){
            zfb_transferaccount_preview_info_icon_tag.setImageResource(R.mipmap.alipay_vip_dz);
        }else if (level.equals(getString(R.string.huangjinhuiyuan))){
            zfb_transferaccount_preview_info_icon_tag.setImageResource(R.mipmap.alipay_vip_hj);
        }else if (level.equals(getString(R.string.baijinhuiyuan))){
            zfb_transferaccount_preview_info_icon_tag.setImageResource(R.mipmap.alipay_vip_bj);
        }else if (level.equals((R.string.zhuanshihuiyuan))){
            zfb_transferaccount_preview_info_icon_tag.setImageResource(R.mipmap.alipay_vip_zs);
        }else{
            zfb_transferaccount_preview_info_icon_tag.setVisibility(View.GONE);
        }
        zfb_transferaccount_preview_ddh_tv.setText(StringPattern(time,"yyyy-MM-dd HH:mm","yyyyMMddHHmm")+StaticMethod.getRandomString(24));
        zfb_transferaccount_preview_shddh_tv.setText(StringPattern(time,"yyyy-MM-dd HH:mm","yyyyMMddHHmm")+StaticMethod.getRandomString(24));
    }



}
