package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;
import com.zhushou.weichat.screenshot.view.XCRoundRectImageView;

/**
 * Created by zhanglinkai on 2017/3/14.
 * 功能:
 */

public class RedPacketPreView extends Activity implements View.OnClickListener{

    private ImageView redpacket_preview_title;
    private XCRoundRectImageView redpacket_preview_icon;
    private TextView redpacket_preview_name;
    private TextView redpacket_preview_des;
    private TextView red_money_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_red_packet_preview);

        iniUI();
    }

    private void iniUI() {
        String bitmap=getIntent().getStringExtra("bitmap");
        String name=getIntent().getStringExtra("name");
        String des=getIntent().getStringExtra("des");
        String money=getIntent().getStringExtra("money");


        redpacket_preview_title=(ImageView)findViewById(R.id.redpacket_preview_title);
        redpacket_preview_icon=(XCRoundRectImageView)findViewById(R.id.redpacket_preview_icon);
        redpacket_preview_name=(TextView)findViewById(R.id.redpacket_preview_name);
        redpacket_preview_des=(TextView)findViewById(R.id.redpacket_preview_des);
        red_money_number=(TextView)findViewById(R.id.red_money_number);

        redpacket_preview_title.setOnClickListener(this);

        Picasso.with(this).load(new File(bitmap)).resize(100,100).into(redpacket_preview_icon);
        //redpacket_preview_icon.setImageBitmap(bitmap);
        redpacket_preview_name.setText(name);
        redpacket_preview_des.setText(des);
        red_money_number.setText(StaticMethod.keepTwoDecimalNo(money));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.redpacket_preview_title:
            finish();
            break;
        }
    }
}
