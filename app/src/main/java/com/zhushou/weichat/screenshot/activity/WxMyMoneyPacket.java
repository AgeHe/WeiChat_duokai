package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2017/3/31.
 * 功能:
 */

public class WxMyMoneyPacket extends Activity {
    private ImageView mymoneypacket_title_iv;
    private EditText mymoneypacket_money_et;
    private Button mymoneypacket_preview_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wxmymoneypacket);
        iniUI();
    }

    private void iniUI() {
        mymoneypacket_title_iv=(ImageView)findViewById(R.id.mymoneypacket_title_iv);
        mymoneypacket_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mymoneypacket_money_et=(EditText)findViewById(R.id.mymoneypacket_money_et);

        mymoneypacket_preview_btn=(Button)findViewById(R.id.mymoneypacket_preview_btn);
        mymoneypacket_preview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money=mymoneypacket_money_et.getText().toString().trim();
                if (!money.isEmpty()) {
                    Intent intent = new Intent(WxMyMoneyPacket.this, WxMyMoneyPacketPreview.class);
                    intent.putExtra("money",money);
                    startActivity(intent);
                }else{
                    Toast.makeText(WxMyMoneyPacket.this, R.string.qqbalance_toast, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
