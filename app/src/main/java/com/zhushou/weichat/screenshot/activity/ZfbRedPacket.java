package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.ListDataSave;

/**
 * Created by zhanglinkai on 2017/4/12.
 * 功能:
 */

public class ZfbRedPacket extends Activity {
    private ImageView zfb_redpacket_title_iv;
    private RelativeLayout zfb_redpacket_info_layout;
    private ImageView zfb_redpacket_info_icon;
    private TextView zfb_redpacket_info_name;
    private EditText zfb_redpacket_money_et;
    private EditText zfb_redpacket_des_et;
    private Button zfb_redpacket_preview_btn;
    private ListDataSave save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_zfb_redpacket);
        iniUI();
    }

    private void iniUI() {
        save=new ListDataSave(this,"tt");
        zfb_redpacket_title_iv=(ImageView)findViewById(R.id.zfb_redpacket_title_iv);
        zfb_redpacket_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        zfb_redpacket_info_layout=(RelativeLayout)findViewById(R.id.zfb_redpacket_info_layout);
        zfb_redpacket_info_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ZfbRedPacket.this,RoleActivity.class);
                intent.putExtra("tag","call");
                startActivityForResult(intent,1);
            }
        });
        zfb_redpacket_info_icon=(ImageView)findViewById(R.id.zfb_redpacket_info_icon);
        zfb_redpacket_info_name=(TextView)findViewById(R.id.zfb_redpacket_info_name);
        zfb_redpacket_money_et=(EditText)findViewById(R.id.zfb_redpacket_money_et);
        zfb_redpacket_des_et=(EditText)findViewById(R.id.zfb_redpacket_des_et);
        zfb_redpacket_preview_btn=(Button)findViewById(R.id.zfb_redpacket_preview_btn);
        zfb_redpacket_preview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money=zfb_redpacket_money_et.getText().toString().trim();
                String des=zfb_redpacket_des_et.getText().toString().trim();
                if (!money.isEmpty()&&!image.isEmpty()) {
                    Intent intent = new Intent(ZfbRedPacket.this, ZfbRedPacketPreview.class);
                    intent.putExtra("name",zfb_redpacket_info_name.getText().toString().trim());
                    intent.putExtra("image",image);
                    intent.putExtra("money",money);
                    intent.putExtra("des",des);
                    startActivity(intent);
                }else{
                    Toast.makeText(ZfbRedPacket.this, R.string.qqbalance_toast, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    String image="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==2){
                List<Map<String,String>> list=save.getDataList("tt");
                int p=data.getIntExtra("position",10000);
                if (p!=10000){
                    String name =list.get(p).get("name");
                    image =list.get(p).get("image");
                    Picasso.with(this).load(new File(image)).resize(100,100).into(zfb_redpacket_info_icon);
                    zfb_redpacket_info_name.setText(name);
                }
            }
        }
    }
}
