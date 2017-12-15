package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.PublicMethod;
import com.zhushou.weichat.screenshot.adapter.MultiTalkSelectPeopleDialogAdapter;


/**
 * Created by zhanglinkai on 2017/3/19.
 * 功能:
 */

public class WxTalkaddVoice extends Activity implements View.OnClickListener,MultiTalkSelectPeopleDialogAdapter.itemClick{

    private ImageView add_voice_title_iv;
    private TextView add_voice_sendtype_m;
    private TextView add_voice_sendtype_h;
    private EditText add_voice_time_et;
    private EditText add_voice_length;
    private LinearLayout add_voice_listentype_layout;
    private ImageView add_voice_listentype_iv;
    private TextView add_voice_listentype_tv;
    private Button add_voice_sure_btn;
    private String sendType="m";
    private boolean isSelect=true;

    private View view;
    private ImageView multi_talk_send_icon;
    private TextView multi_talk_send_name;
    private LinearLayout single_talk_voice_send_layout;
    private String type="";
    private PublicMethod m;
    private List<Map<String,String>> namelist;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_talk_add_voice);
        m=new PublicMethod(this);
        iniUI();
    }

    private void iniUI() {
        type=getIntent().getStringExtra("singleOrmulti");
        add_voice_title_iv=(ImageView)findViewById(R.id.add_voice_title_iv);
        add_voice_sendtype_m=(TextView)findViewById(R.id.add_voice_sendtype_m);
        add_voice_sendtype_h=(TextView)findViewById(R.id.add_voice_sendtype_h);
        add_voice_time_et=(EditText)findViewById(R.id.add_voice_time_et);
        add_voice_length=(EditText)findViewById(R.id.add_voice_length);
        add_voice_listentype_layout=(LinearLayout)findViewById(R.id.add_voice_listentype_layout);
        add_voice_listentype_iv=(ImageView)findViewById(R.id.add_voice_listentype_iv);
        add_voice_listentype_tv=(TextView)findViewById(R.id.add_voice_listentype_tv);
        add_voice_sure_btn=(Button)findViewById(R.id.add_voice_sure_btn);

        view=findViewById(R.id.multi_talk_voice_send_layout);
        multi_talk_send_icon=(ImageView)view.findViewById(R.id.multi_talk_send_icon);
        multi_talk_send_name=(TextView)view.findViewById(R.id.multi_talk_send_name);
        single_talk_voice_send_layout=(LinearLayout)findViewById(R.id.single_talk_voice_send_layout);


        add_voice_title_iv.setOnClickListener(this);
        add_voice_sendtype_m.setOnClickListener(this);
        add_voice_sendtype_h.setOnClickListener(this);
        add_voice_listentype_layout.setOnClickListener(this);
        add_voice_sure_btn.setOnClickListener(this);
        view.setOnClickListener(this);

        if (type.equals("single")){
            view.setVisibility(View.GONE);
            single_talk_voice_send_layout.setVisibility(View.VISIBLE);
        }else if (type.equals("multi")){
            view.setVisibility(View.VISIBLE);
            single_talk_voice_send_layout.setVisibility(View.GONE);
        }

        m=new PublicMethod(this);
        namelist= (List<Map<String, String>>) getIntent().getSerializableExtra("list");
        for (int i = 0; i < namelist.size(); i++) {
            if (namelist.get(i).get("name").isEmpty()&&namelist.get(i).get("image").isEmpty()){
                namelist.remove(i);
            }
        }
        dialog=m.multiTalkDialog(namelist);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_voice_title_iv:
                finish();
                break;
            case R.id.add_voice_sendtype_m:
                sendType="m";
                add_voice_sendtype_m.setBackgroundColor(getResources().getColor(R.color.blue));
                add_voice_sendtype_h.setBackgroundColor(getResources().getColor(R.color.color_white));
                break;
            case R.id.add_voice_sendtype_h:
                sendType="h";
                add_voice_sendtype_m.setBackgroundColor(getResources().getColor(R.color.color_white));
                add_voice_sendtype_h.setBackgroundColor(getResources().getColor(R.color.blue));
                break;
            case R.id.add_voice_listentype_layout:

                if (isSelect){
                    isSelect=false;
                    add_voice_listentype_iv.setBackgroundResource(R.mipmap.webviewtab_back_normal);
                    add_voice_listentype_tv.setText(R.string.weishouting);
                }else{
                    isSelect=true;
                    add_voice_listentype_iv.setBackgroundResource(R.mipmap.webviewtab_back_disable);
                    add_voice_listentype_tv.setText(R.string.yishouting);
                }

                break;
            case R.id.add_voice_sure_btn:
                String listenlength=add_voice_length.getText().toString().trim();
                if (!listenlength.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putExtra("sendtype", sendType);
                    if (type.equals("multi")){
                        intent.putExtra("icon", image);
                        intent.putExtra("name", name);
                    }
                    String time = add_voice_time_et.getText().toString().trim();
                    intent.putExtra("time", time);
                    intent.putExtra("listenlength", listenlength);
                    intent.putExtra("isselect", isSelect);
                    setResult(8,intent);
                    finish();
                }else{
                    Toast.makeText(this, R.string.qingshuruyuyinchangdu, Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.multi_talk_voice_send_layout:
                isSelectPeople=false;
                dialog.show();
                break;
        }
    }
    String name="";
    String image="";
    boolean isSelectPeople=false;
    @Override
    public void itemclick(int p) {
        isSelectPeople=true;
        if (p==0){
            sendType="m";
        }else{
            sendType="h";
        }
        name=namelist.get(p).get("name");
        image=namelist.get(p).get("image");
        Picasso.with(this).load(new File(image)).resize(100,100).into(multi_talk_send_icon);
        multi_talk_send_name.setText(name);
        dialog.dismiss();
    }
}
