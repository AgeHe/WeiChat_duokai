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
import java.util.Random;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.ListDataSave;


/**
 * Created by zhanglinkai on 2017/3/14.
 * 功能:
 */

public class WxRedPacket extends Activity implements View.OnClickListener{

    private Button redpacket_preview;
    private ImageView wxredpacket_titile;
    private ImageView redpacket_name_iv;
    private TextView redpacket_name_tv;
    private RelativeLayout redpacket_name_iv_layout;
    private EditText redpacket_money;
    private EditText redpacket_des;
    private ListDataSave save;
    private int iniPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wxredpacket);

        save=new ListDataSave(this,"tt");
        iniUI();
    }
    List<Map<String,String>> locationData;
    private void iniUI() {
        redpacket_preview=(Button)findViewById(R.id.redpacket_preview);
        wxredpacket_titile=(ImageView)findViewById(R.id.wxredpacket_titile);
        redpacket_name_iv=(ImageView)findViewById(R.id.redpacket_name_iv);
        redpacket_name_tv=(TextView)findViewById(R.id.redpacket_name_tv);
        redpacket_name_iv_layout=(RelativeLayout)findViewById(R.id.redpacket_name_iv_layout);
        redpacket_money=(EditText)findViewById(R.id.redpacket_money);
        redpacket_des=(EditText)findViewById(R.id.redpacket_des);

        //随机显示
        locationData=save.getDataList("tt");
        Random r=new Random();
        iniPosition=r.nextInt(locationData.size());
        redpacket_name_tv.setText(locationData.get(iniPosition).get("name"));
        Picasso.with(this).load(new File(locationData.get(iniPosition).get("image"))).resize(100,100).into(redpacket_name_iv);
        //redpacket_name_iv.setImageBitmap(BitmapFactory.decodeFile(locationData.get(iniPosition).get("image")));

        redpacket_preview.setOnClickListener(this);
        wxredpacket_titile.setOnClickListener(this);
        redpacket_name_iv_layout.setOnClickListener(this);
        redpacket_money.setOnClickListener(this);
        redpacket_des.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.redpacket_preview:
                String money=redpacket_money.getText().toString().trim();
                if (!money.isEmpty()){
                    Intent intent=new Intent(WxRedPacket.this,RedPacketPreView.class);
                    intent.putExtra("money",money);
                    if (redpacket_des.getText().toString().trim().isEmpty()){
                        intent.putExtra("des",getResources().getString(R.string.gongxifacaidajidali));
                    }else{
                        intent.putExtra("des",redpacket_des.getText().toString().trim());
                    }
                    if (isSelect){
                        intent.putExtra("bitmap",imagepath);
                    }else{
                        intent.putExtra("bitmap",locationData.get(iniPosition).get("image"));
                    }
                    intent.putExtra("name",redpacket_name_tv.getText().toString().trim());
                    startActivity(intent);
                }else{
                    Toast.makeText(this, R.string.qqbalance_toast, Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.wxredpacket_titile:
                finish();
                break;
            case R.id.redpacket_name_iv_layout:
                isSelect=false;
                Intent infor_intent=new Intent(WxRedPacket.this,RoleActivity.class);
                infor_intent.putExtra("tag","redpacket");
                startActivityForResult(infor_intent,1);
                break;
            case R.id.redpacket_money:
                redpacket_money.setFocusable(true);
                redpacket_money.setFocusableInTouchMode(true);
                redpacket_money.requestFocusFromTouch();
                break;
            case R.id.redpacket_des:
                redpacket_des.setFocusable(true);
                redpacket_des.setFocusableInTouchMode(true);
                redpacket_des.requestFocusFromTouch();
                break;
        }
    }
    String imagepath="";
    boolean isSelect=false;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==2){
            List<Map<String,String>> locationData=save.getDataList("tt");
            int position=data.getIntExtra("position",10000);
            if (position!=10000){
                String name=locationData.get(position).get("name");
                imagepath=locationData.get(position).get("image");
                redpacket_name_tv.setText(name);
                Picasso.with(this).load(new File(imagepath)).resize(100,100).into(redpacket_name_iv);
                //redpacket_name_iv.setImageBitmap(BitmapFactory.decodeFile(imagepath));
                isSelect=true;
            }
        }
    }
}
