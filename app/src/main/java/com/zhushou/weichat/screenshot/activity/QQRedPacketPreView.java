package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;
import com.zhushou.weichat.screenshot.view.CircleImageView;

import java.io.File;

/**
 * Created by zhanglinkai on 2017/4/14.
 * 功能:
 */

public class QQRedPacketPreView extends Activity {
    private LinearLayout qq_redpacket_preview_title_layoutm;
    private TextView gxfc;
    private TextView qq_redpacket_preview_from_money;
    private CircleImageView qq_redpacket_preview_fromicon;
    private TextView qq_redpacket_preview_fromname;
    private CircleImageView qq_redpacket_preview_toicon;
    private TextView qq_redpacket_preview_toname;
    private TextView qq_redpacket_preview_tomoney;
    private TextView qq_redpacket_preview_totime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_qq_redpacket_preview);
        iniUI();
    }

    private void iniUI() {

        qq_redpacket_preview_title_layoutm=(LinearLayout)findViewById(R.id.qq_redpacket_preview_title_layoutm);
        qq_redpacket_preview_title_layoutm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gxfc=(TextView)findViewById(R.id.gxfc);
        qq_redpacket_preview_from_money=(TextView)findViewById(R.id.qq_redpacket_preview_from_money);
        qq_redpacket_preview_fromicon=(CircleImageView)findViewById(R.id.qq_redpacket_preview_fromicon);
        qq_redpacket_preview_fromname=(TextView)findViewById(R.id.qq_redpacket_preview_fromname);
        qq_redpacket_preview_toicon=(CircleImageView)findViewById(R.id.qq_redpacket_preview_toicon);
        qq_redpacket_preview_toname=(TextView)findViewById(R.id.qq_redpacket_preview_toname);
        qq_redpacket_preview_tomoney=(TextView)findViewById(R.id.qq_redpacket_preview_tomoney);
        qq_redpacket_preview_totime=(TextView)findViewById(R.id.qq_redpacket_preview_totime);

        String fromname=getIntent().getStringExtra("fromname");
        String froimage=getIntent().getStringExtra("froimage");
        String toname=getIntent().getStringExtra("toname");
        String toimage=getIntent().getStringExtra("toimage");
        String money=getIntent().getStringExtra("money");
        String des=getIntent().getStringExtra("des");
        String time=getIntent().getStringExtra("time");
        if (des.isEmpty()){
            gxfc.setText(getString(R.string.gxfc));
        }else{
            gxfc.setText(des);
        }
        qq_redpacket_preview_from_money.setText(StaticMethod.keepTwoDecimal(money));
        qq_redpacket_preview_tomoney.setText(StaticMethod.keepTwoDecimal(money)+getString(R.string.yuan));
        Picasso.with(this).load(new File(froimage)).resize(100,100).into(qq_redpacket_preview_fromicon);
        qq_redpacket_preview_fromname.setText(fromname);
        Picasso.with(this).load(new File(toimage)).resize(100,100).into(qq_redpacket_preview_toicon);
        qq_redpacket_preview_toname.setText(toname);
        qq_redpacket_preview_totime.setText(time);
    }
}
