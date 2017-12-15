package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.zhushou.weichat.screenshot.adapter.AddTalkAdapter;

/**
 * Created by zhanglinkai on 2017/3/16.
 * 功能:
 */

public class Wxtalk extends Activity implements View.OnClickListener,AddTalkAdapter.clearclick{

    private ImageView wxtalk_title_iv;
    private ImageView informatioin_iv1;
    private ImageView informatioin_iv2;
    private RelativeLayout wxtalk_information;
    private ListView wxtalk_list;
    private Button talk_add_btn;
    private Button talk_preview_btn;
    private boolean isSetInfo=false;
    private AddTalkAdapter adapter;
    private PublicMethod m;
    private AlertDialog dialog;
    private List<Map<String,String>> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wxtalk);
        iniUI();
    }

    private void iniUI() {
        wxtalk_title_iv=(ImageView)findViewById(R.id.wxtalk_title_iv);
        informatioin_iv1=(ImageView)findViewById(R.id.informatioin_iv1);
        informatioin_iv2=(ImageView)findViewById(R.id.informatioin_iv2);
        wxtalk_information=(RelativeLayout)findViewById(R.id.wxtalk_information);
        wxtalk_list=(ListView)findViewById(R.id.wxtalk_list);
        talk_add_btn=(Button)findViewById(R.id.talk_add_btn);
        talk_preview_btn=(Button)findViewById(R.id.talk_preview_btn);

        wxtalk_title_iv.setOnClickListener(this);
        wxtalk_information.setOnClickListener(this);
        talk_add_btn.setOnClickListener(this);
        talk_preview_btn.setOnClickListener(this);

        adapter=new AddTalkAdapter(this,sendlist,this);
        wxtalk_list.setAdapter(adapter);

        m=new PublicMethod(this);
        dialog=m.addTalk("single",list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wxtalk_title_iv:
                finish();
                break;
            case R.id.wxtalk_information:
                isSetInfo=false;
                Intent intent=new Intent(Wxtalk.this,WxTalkInforamationSet.class);
                startActivityForResult(intent,1);
                break;
            case R.id.talk_add_btn:
                if (isSetInfo) {
                    dialog.show();
                }else{
                    Toast.makeText(this, R.string.qingxianshezhiliaotianxinxi, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.talk_preview_btn:
                //生成预览
                Intent previewIntent=new Intent(this,WxTalkPreview.class);

                previewIntent.putExtra("herselfname",hName);
                previewIntent.putExtra("list", (Serializable) sendlist);
                previewIntent.putExtra("bbitmap", bBitmap);
                previewIntent.putExtra("isSelectBg", isSelectBg);
                previewIntent.putExtra("tag", "single");

                startActivity(previewIntent);

                break;
        }
    }
    private List<HashMap<String,Object>> sendlist=new ArrayList<>();
    String hName="";
    String bBitmap="";
    String mbitmap="";
    String hbitmap="";
    boolean isSelectBg=false;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //返回的设置聊天信息
        if (requestCode == 1) {
            if (resultCode==2){
                isSetInfo=true;

                String mName=data.getStringExtra("mname");
                mbitmap=data.getStringExtra("mbitmap");
                hName=data.getStringExtra("hname");
                hbitmap=data.getStringExtra("hbitmap");
                bBitmap=data.getStringExtra("bgimage");
                isSelectBg=data.getBooleanExtra("selectbg",false);

                Picasso.with(this).load(new File(mbitmap)).resize(100,100).into(informatioin_iv2);
                Picasso.with(this).load(new File(hbitmap)).resize(100,100).into(informatioin_iv1);
            }
        }
        //返回的txt
        if (requestCode==3){
            if (resultCode==4){
                String sendtype=data.getStringExtra("sendtype");
                String time=data.getStringExtra("time");
                String context=data.getStringExtra("context");
                //显示
                HashMap<String,Object> map=new HashMap<String,Object>();
                map.put("type","txt");
                map.put("sendtype",sendtype);
                map.put("time",time);
                map.put("context",context);
                map.put("bcontext",context);
                map.put("hname",hName);
                map.put("bBitmap",bBitmap);
                map.put("isSelectBg",isSelectBg);
                putIcon(sendtype,map);
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

                //显示

                HashMap<String,Object> map=new HashMap<String,Object>();
                map.put("type","photo");
                map.put("sendtype",sendtype);
                map.put("time",time);
                map.put("bcontext",getResources().getString(R.string.tupian));
                map.put("hname",hName);
                map.put("bitmap",bitmap);
                map.put("bBitmap",bBitmap);
                map.put("isSelectBg",isSelectBg);
                putIcon(sendtype,map);
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

                //显示

                HashMap<String,Object> map=new HashMap<String,Object>();
                map.put("type","voice");
                map.put("sendtype",sendtype);
                map.put("time",time);
                map.put("listenlength",listenlength);
                map.put("isselectlisten",isselectlisten);
                map.put("bcontext",getResources().getString(R.string.yuyin)+listenlength+ getResources().getString(R.string.miao));
                map.put("hname",hName);
                map.put("bBitmap",bBitmap);
                map.put("isSelectBg",isSelectBg);
                putIcon(sendtype,map);
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

                //显示
                HashMap<String,Object> map=new HashMap<String,Object>();
                map.put("type","redpacket");
                map.put("sendtype",sendtype);
                map.put("time",time);
                map.put("isselectlisten",isselectlisten);
                map.put("hname",hName);
                map.put("bBitmap",bBitmap);
                map.put("isSelectBg",isSelectBg);
                putIcon(sendtype,map);
                if (context.isEmpty()){
                    map.put("bcontext",getResources().getString(R.string.gongxifacaidajidali));
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

                //显示

                HashMap<String,Object> map=new HashMap<String,Object>();
                map.put("type","transfer");
                map.put("sendtype",sendtype);
                map.put("time",time);
                map.put("money",money);
                map.put("isselectlisten",isselectlisten);
                map.put("bcontext",getResources().getString(R.string.zhuanzhang)+money+getResources().getString(R.string.yuan));
                map.put("hname",hName);
                map.put("bBitmap",bBitmap);
                map.put("isSelectBg",isSelectBg);
                putIcon(sendtype,map);
                sendlist.add(map);
                adapter.notifyDataSetChanged();

            }
        }
        if (dialog!=null){
            dialog.dismiss();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    //put icon到map的提取
    public void putIcon(String sendtype,HashMap map){
        if (sendtype.equals("m")){
            map.put("icon",mbitmap);
        }else if(sendtype.equals("h")){
            map.put("icon",hbitmap);
        }
    }

    @Override
    public void clear(int position) {
        sendlist.remove(position);
        adapter.notifyDataSetChanged();
    }
}
