package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;
import com.zhushou.weichat.screenshot.view.CircleImageView;

import static com.zhushou.weichat.screenshot.Utils.StaticMethod.getRandomString;

/**
 * Created by zhanglinkai on 2017/4/12.
 * 功能:
 */

public class ZfbRedPacketPreview extends Activity {
    private ImageView zfb_redpacket_preview_title_iv;
    private CircleImageView zfb_redpacket_preview_icon;
    private TextView zfb_redpacket_preview_name;
    private TextView zfb_redpacket_preview_des;
    private TextView zfb_redpacket_preview_money;
    private TextView zfb_redpacket_preview_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_zfb_redpacket_preview);
        iniUI();
    }

    private void iniUI() {
        zfb_redpacket_preview_title_iv=(ImageView)findViewById(R.id.zfb_redpacket_preview_title_iv);
        zfb_redpacket_preview_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        zfb_redpacket_preview_icon=(CircleImageView)findViewById(R.id.zfb_redpacket_preview_icon);
        zfb_redpacket_preview_name=(TextView)findViewById(R.id.zfb_redpacket_preview_name);
        zfb_redpacket_preview_des=(TextView)findViewById(R.id.zfb_redpacket_preview_des);
        zfb_redpacket_preview_money=(TextView)findViewById(R.id.zfb_redpacket_preview_money);
        zfb_redpacket_preview_number=(TextView)findViewById(R.id.zfb_redpacket_preview_number);

        String name=getIntent().getStringExtra("name");
        String image=getIntent().getStringExtra("image");
        String money=getIntent().getStringExtra("money");
        String des=getIntent().getStringExtra("des");

        Picasso.with(this).load(new File(image)).resize(100,100).into(zfb_redpacket_preview_icon);
        zfb_redpacket_preview_name.setText(name);
        zfb_redpacket_preview_money.setText(StaticMethod.keepTwoDecimalNo(money));

        if (des.isEmpty()){
            zfb_redpacket_preview_des.setText(R.string.gongxifacaiwanshiruyi);
        }else{
            zfb_redpacket_preview_des.setText(des);
        }

        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmm");
        long time=System.currentTimeMillis();
        String t=sf.format(time);
        zfb_redpacket_preview_number.setText(getString(R.string.hongbaobianma)+t+getRandomString(20));

    }
}
