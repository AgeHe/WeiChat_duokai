package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.zhushou.weichat.screenshot.Utils.PublicMethod;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;
import com.zhushou.weichat.screenshot.adapter.AddTalkAdapter;

/**
 * Created by zhanglinkai on 2017/4/6.
 * 功能:
 */

public class WxMultiTalk extends Activity implements View.OnClickListener,AddTalkAdapter.clearclick{
    private ImageView multi_talk_title_iv;
    private RelativeLayout multi_talk_info_layout;
    private ImageView multi_talk_info_hicon;
    private ImageView multi_talk_info_micon;
    private TextView multi_talk_btn_addtalk;
    private TextView multi_talk_btn_preview;
    private ListView mulri_talk_listview;
    private PublicMethod m;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_multi_talk);
        iniUI();
    }

    private void iniUI() {
        float index=getSharedPreferences("index", Context.MODE_PRIVATE).getFloat("index",1);
        multi_talk_title_iv=(ImageView)findViewById(R.id.multi_talk_title_iv);
        multi_talk_info_layout=(RelativeLayout)findViewById(R.id.multi_talk_info_layout);
        multi_talk_info_hicon=(ImageView)findViewById(R.id.multi_talk_info_hicon);
        multi_talk_info_micon=(ImageView)findViewById(R.id.multi_talk_info_micon);
        multi_talk_btn_addtalk=(TextView)findViewById(R.id.multi_talk_btn_addtalk);
        multi_talk_btn_preview=(TextView)findViewById(R.id.multi_talk_btn_preview);
        mulri_talk_listview=(ListView)findViewById(R.id.mulri_talk_listview);

        StaticMethod.adaptiveView(multi_talk_info_hicon,120,120,index);
        StaticMethod.adaptiveView(multi_talk_info_micon,120,120,index);

        multi_talk_title_iv.setOnClickListener(this);
        multi_talk_info_layout.setOnClickListener(this);
        multi_talk_btn_addtalk.setOnClickListener(this);
        multi_talk_btn_preview.setOnClickListener(this);

        m=new PublicMethod(this);
        adapter=new AddTalkAdapter(this,sendlist,this);
        mulri_talk_listview.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.multi_talk_title_iv:
                finish();
                break;
            case R.id.multi_talk_info_layout:
                isSet=false;
                Intent intent=new Intent(this,WxMultiTalkInfoSet.class);
                startActivityForResult(intent,1);
                break;
            case R.id.multi_talk_btn_addtalk:
                if (isSet) {
                    dialog.show();
                }else{
                    Toast.makeText(this, R.string.qqbalance_toast, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.multi_talk_btn_preview:

                Intent previewIntent=new Intent(this,WxTalkPreview.class);

                previewIntent.putExtra("herselfname",titlename+String.format("(%s)",number));
                previewIntent.putExtra("list", (Serializable) sendlist);
                previewIntent.putExtra("bbitmap", bgimage);
                previewIntent.putExtra("isSelectBg", isSelectBg);
                previewIntent.putExtra("tag", "multi");

                startActivity(previewIntent);

                break;
        }
    }
    List<Map<String,String>> list;
    private List<HashMap<String,Object>> sendlist=new ArrayList<>();
    private AddTalkAdapter adapter;
    boolean isSet=false;
    boolean isSelectBg=false;
    String titlename="";
    String number="";
    String bgimage="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==2){
                list= (List<Map<String, String>>) data.getSerializableExtra("list");
                dialog=m.addTalk("multi",list);
                titlename=data.getStringExtra("name");
                number=data.getStringExtra("number");
                bgimage=data.getStringExtra("bgimage");
                isSelectBg=data.getBooleanExtra("isSelectBg",false);
                for (int i = 0; i < list.size(); i++) {
                    if (i==0){
                        Picasso.with(this).load(new File(list.get(i).get("image"))).resize(100,100).into(multi_talk_info_micon);
                    }else if (i==1){
                        Picasso.with(this).load(new File(list.get(i).get("image"))).resize(100,100).into(multi_talk_info_hicon);
                    }
                }
                isSet=true;
            }
        }
        if (requestCode==3){
            if (resultCode==4){
                String sendtype=data.getStringExtra("sendtype");
                String time=data.getStringExtra("time");
                String context=data.getStringExtra("context");
                String name=data.getStringExtra("name");
                String icon=data.getStringExtra("icon");
                //显示
                HashMap<String,Object> map=new HashMap<String,Object>();
                map.put("type","txt");
                map.put("sendtype",sendtype);
                map.put("time",time);
                map.put("context",context);
                map.put("bcontext",context);
                map.put("icon",icon);
                map.put("name",name);
                map.put("isSelectBg",isSelectBg);
                sendlist.add(map);
                adapter.notifyDataSetChanged();
            }
        }
        //返回的photo
        if (requestCode==5){
            if (resultCode==6){
                String sendtype=data.getStringExtra("sendtype");
                String time=data.getStringExtra("time");
                String bitmap=data.getStringExtra("bitmap");
                String name=data.getStringExtra("name");
                String icon=data.getStringExtra("icon");
                //显示
                HashMap<String,Object> map=new HashMap<String,Object>();
                map.put("type","photo");
                map.put("sendtype",sendtype);
                map.put("time",time);
                map.put("bcontext",getString(R.string.tupian));
                map.put("bitmap",bitmap);
                map.put("icon",icon);
                map.put("name",name);
                map.put("isSelectBg",isSelectBg);
                sendlist.add(map);
                adapter.notifyDataSetChanged();
            }
        }
        //返回的voice
        if (requestCode==7){
            if(resultCode==8){
                String sendtype=data.getStringExtra("sendtype");
                String time=data.getStringExtra("time");
                String listenlength=data.getStringExtra("listenlength");
                boolean isselectlisten=data.getBooleanExtra("isselect",true);
                String name=data.getStringExtra("name");
                String icon=data.getStringExtra("icon");
                //显示

                HashMap<String,Object> map=new HashMap<String,Object>();
                map.put("type","voice");
                map.put("sendtype",sendtype);
                map.put("time",time);
                map.put("listenlength",listenlength);
                map.put("isselectlisten",isselectlisten);
                map.put("bcontext",getString(R.string.yuyin)+listenlength+getString(R.string.miao));
                map.put("icon",icon);
                map.put("name",name);
                map.put("isSelectBg",isSelectBg);
                sendlist.add(map);
                adapter.notifyDataSetChanged();
            }
        }
        //返回的redpacket
        if (requestCode==9){
            if (resultCode==10){
                String sendtype=data.getStringExtra("sendtype");
                String time=data.getStringExtra("time");
                String context=data.getStringExtra("context");
                boolean isselectlisten=data.getBooleanExtra("isselect",false);
                String name=data.getStringExtra("name");
                String icon=data.getStringExtra("icon");

                //显示
                HashMap<String,Object> map=new HashMap<String,Object>();
                map.put("type","redpacket");
                map.put("sendtype",sendtype);
                map.put("time",time);
                map.put("isselectlisten",isselectlisten);
                map.put("icon",icon);
                map.put("name",name);
                map.put("hname",name);
                if (isselectlisten){
                    String hname=data.getStringExtra("hname");
                    map.put("hname",hname);
                }
                map.put("isSelectBg",isSelectBg);
                if (context.isEmpty()){
                    map.put("bcontext",getString(R.string.gongxifacaidajidali));
                }else{
                    map.put("bcontext",context);
                }
                sendlist.add(map);
                adapter.notifyDataSetChanged();
            }
        }

        //返回的transfer
        if (requestCode==11){
            if (resultCode==12){
                String sendtype=data.getStringExtra("sendtype");
                String time=data.getStringExtra("time");
                String money=data.getStringExtra("money");
                boolean isselectlisten=data.getBooleanExtra("isselect",false);
                String name=data.getStringExtra("name");
                String icon=data.getStringExtra("icon");

                //显示

                HashMap<String,Object> map=new HashMap<String,Object>();
                map.put("type","transfer");
                map.put("sendtype",sendtype);
                map.put("time",time);
                map.put("money",money);
                map.put("isselectlisten",isselectlisten);
                map.put("bcontext",getString(R.string.zhuanzhang)+money+getString(R.string.yuan));
                map.put("icon",icon);
                map.put("name",name);
                map.put("isSelectBg",isSelectBg);
                if (isselectlisten){
                    String hname=data.getStringExtra("hname");
                    map.put("hname",hname);
                }
                sendlist.add(map);
                adapter.notifyDataSetChanged();

            }
        }
        if (dialog!=null){
            dialog.dismiss();
        }

    }

    @Override
    public void clear(int position) {
        sendlist.remove(position);
        adapter.notifyDataSetChanged();
    }
}
