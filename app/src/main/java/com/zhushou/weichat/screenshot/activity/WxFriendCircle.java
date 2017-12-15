package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.adapter.WxFriendCircleAdapter;

/**
 * Created by zhanglinkai on 2017/4/10.
 * 功能:
 */

public class WxFriendCircle extends Activity implements View.OnClickListener,WxFriendCircleAdapter.itemClick{
    private ImageView wx_friendcircle_title_iv;
    private RelativeLayout wx_friendcircle_info_layout;
    private ImageView wx_friendcircle_info_iv;
    private TextView wx_friendcircle_info_tv;
    private TextView wx_friendcircle_btn_add;
    private TextView wx_friendcircle_btn_preview;
    private ListView wx_friendcircle_listview;
    private WxFriendCircleAdapter adapter;
    private boolean isSelectInfo=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wxfriendcircle);
        iniUI();
    }

    private void iniUI() {
        wx_friendcircle_title_iv=(ImageView)findViewById(R.id.wx_friendcircle_title_iv);
        wx_friendcircle_info_layout=(RelativeLayout)findViewById(R.id.wx_friendcircle_info_layout);
        wx_friendcircle_info_iv=(ImageView)findViewById(R.id.wx_friendcircle_info_iv);
        wx_friendcircle_info_tv=(TextView)findViewById(R.id.wx_friendcircle_info_tv);
        wx_friendcircle_btn_add=(TextView)findViewById(R.id.wx_friendcircle_btn_add);
        wx_friendcircle_btn_preview=(TextView)findViewById(R.id.wx_friendcircle_btn_preview);
        wx_friendcircle_listview=(ListView)findViewById(R.id.wx_friendcircle_listview);

        wx_friendcircle_title_iv.setOnClickListener(this);
        wx_friendcircle_info_layout.setOnClickListener(this);
        wx_friendcircle_btn_add.setOnClickListener(this);
        wx_friendcircle_btn_preview.setOnClickListener(this);

        adapter=new WxFriendCircleAdapter(this,dataList,this);
        wx_friendcircle_listview.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wx_friendcircle_title_iv:
                finish();
                break;
            case R.id.wx_friendcircle_info_layout:
                isSelectInfo=false;
                Intent intent=new Intent(this,WxFriendCircleInfoset.class);
                startActivityForResult(intent,1);
                break;
            case R.id.wx_friendcircle_btn_add:
                if (isSelectInfo) {
                    Intent intent1 = new Intent(this, WxFriendCircleAdd.class);
                    startActivityForResult(intent1, 3);
                }else{
                    Toast.makeText(this, getResources().getString(R.string.qqbalance_toast), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.wx_friendcircle_btn_preview:
                if (dataList.size()!=0) {
                    Intent intent2 = new Intent(this, WxFriendCirclePreview.class);

                    intent2.putExtra("bgimage", bgimage);
                    intent2.putExtra("name", wx_friendcircle_info_tv.getText().toString().trim());
                    intent2.putExtra("roleimage", roleimage);
                    intent2.putExtra("infoimage", infoimage);
                    intent2.putExtra("number", number);
                    intent2.putExtra("list", (Serializable) dataList);
                    startActivity(intent2);
                }else{
                    Toast.makeText(this, getResources().getString(R.string.qingtianjiashuju), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private  List<Map<String,Object>> dataList=new ArrayList<>();
    String bgimage="";
    String roleimage="";
    String infoimage="";
    String number="";
    List<Map<String,String>> commitList=new ArrayList<>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==2){
                String rolename=data.getStringExtra("rolename");
                roleimage=data.getStringExtra("roleimage");
                bgimage=data.getStringExtra("bgimage");
                String infoname=data.getStringExtra("infoname");
                infoimage=data.getStringExtra("infoimage");
                number=data.getStringExtra("number");

                Picasso.with(this).load(new File(roleimage)).resize(100,100).into(wx_friendcircle_info_iv);
                wx_friendcircle_info_tv.setText(rolename);
                isSelectInfo=true;
            }
        }
        if (requestCode==3){
            if (resultCode==4){
                String name=data.getStringExtra("name");
                String image=data.getStringExtra("image");
                String context=data.getStringExtra("context");
                List<String> list= (List<String>) data.getSerializableExtra("list");
                String time=data.getStringExtra("time");
                String location=data.getStringExtra("location");
                Map<String,Object> map=new HashMap<>();
                map.put("name",name);
                map.put("image",image);
                map.put("context",context);
                map.put("list",list);
                map.put("time",time);
                map.put("location",location);
                map.put("zan","");
                map.put("commit",commitList);
                dataList.add(map);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void itemclick(int p) {
        dataList.remove(p);
        adapter.notifyDataSetChanged();
    }
}
