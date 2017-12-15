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

public class WxTalkaddRedPacket extends Activity implements View.OnClickListener,MultiTalkSelectPeopleDialogAdapter.itemClick {

    private ImageView add_redpacket_title_iv;
    private TextView add_redpacket_sendtype_m;
    private TextView add_redpacket_sendtype_h;
    private EditText add_redpacket_time_et;
    private EditText add_redpacket_context_et;
    private LinearLayout add_redpacket_receive_layout;
    private ImageView add_redpacket_receive_iv;
    private TextView add_redpacket_receive_tv;
    private Button add_redpacket_sure_btn;
    private boolean isSelect=false;
    private String sendType="m";

    private View view;
    private View view2;
    private ImageView multi_talk_send_icon;
    private TextView multi_talk_send_name;
    private ImageView multi_talk_send_icon2;
    private TextView multi_talk_send_name2;
    private LinearLayout single_talk_redpacket_send_layout;
    private String type="";
    private PublicMethod m;
    private List<Map<String,String>> namelist;
    private AlertDialog dialog;
    private String selectType="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_talk_add_redpacket);
        m=new PublicMethod(this);
        iniUI();
    }

    private void iniUI() {
        type=getIntent().getStringExtra("singleOrmulti");
        add_redpacket_title_iv=(ImageView)findViewById(R.id.add_redpacket_title_iv);
        add_redpacket_sendtype_m=(TextView)findViewById(R.id.add_redpacket_sendtype_m);
        add_redpacket_sendtype_h=(TextView)findViewById(R.id.add_redpacket_sendtype_h);
        add_redpacket_time_et=(EditText)findViewById(R.id.add_redpacket_time_et);
        add_redpacket_context_et=(EditText)findViewById(R.id.add_redpacket_context_et);
        add_redpacket_receive_layout=(LinearLayout)findViewById(R.id.add_redpacket_receive_layout);
        add_redpacket_receive_iv=(ImageView)findViewById(R.id.add_redpacket_receive_iv);
        add_redpacket_receive_tv=(TextView)findViewById(R.id.add_redpacket_receive_tv);
        add_redpacket_sure_btn=(Button)findViewById(R.id.add_redpacket_sure_btn);

        view=findViewById(R.id.multi_talk_redpacket_send_layout);
        view2=findViewById(R.id.multi_talk_redpacket_collect_layout);
        multi_talk_send_icon=(ImageView)view.findViewById(R.id.multi_talk_send_icon);
        multi_talk_send_name=(TextView)view.findViewById(R.id.multi_talk_send_name);
        multi_talk_send_icon2=(ImageView)view2.findViewById(R.id.multi_talk_send_icon);
        multi_talk_send_name2=(TextView)view2.findViewById(R.id.multi_talk_send_name);
        TextView multi_talk_send_tag=(TextView)view2.findViewById(R.id.multi_talk_send_tag);
        multi_talk_send_tag.setText(R.string.lingquren);
        single_talk_redpacket_send_layout=(LinearLayout)findViewById(R.id.single_talk_redpacket_send_layout);

        add_redpacket_title_iv.setOnClickListener(this);
        add_redpacket_sendtype_m.setOnClickListener(this);
        add_redpacket_sendtype_h.setOnClickListener(this);
        add_redpacket_receive_layout.setOnClickListener(this);
        add_redpacket_sure_btn.setOnClickListener(this);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectPeople=false;
                selectType="send";
                dialog.show();
            }
        });
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectPeople2=false;
                selectType="collect";
                dialog.show();
            }
        });

        if (type.equals("single")){
            view.setVisibility(View.GONE);
            single_talk_redpacket_send_layout.setVisibility(View.VISIBLE);
        }else if (type.equals("multi")){
            view.setVisibility(View.VISIBLE);
            single_talk_redpacket_send_layout.setVisibility(View.GONE);
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
            case R.id.add_redpacket_title_iv:
                finish();
                break;
            case R.id.add_redpacket_sendtype_m:
                sendType="m";
                add_redpacket_sendtype_m.setBackgroundColor(getResources().getColor(R.color.blue));
                add_redpacket_sendtype_h.setBackgroundColor(getResources().getColor(R.color.color_white));
                break;
            case R.id.add_redpacket_sendtype_h:
                sendType="h";
                add_redpacket_sendtype_m.setBackgroundColor(getResources().getColor(R.color.color_white));
                add_redpacket_sendtype_h.setBackgroundColor(getResources().getColor(R.color.blue));
                break;
            case R.id.add_redpacket_receive_layout:
                if (isSelect){
                    isSelect=false;
                    add_redpacket_receive_iv.setBackgroundResource(R.mipmap.webviewtab_back_normal);
                    add_redpacket_receive_tv.setText(R.string.weilingquhongbao);
                    if (type.equals("multi")) {
                        view2.setVisibility(View.GONE);
                    }
                }else{
                    isSelect=true;
                    add_redpacket_receive_iv.setBackgroundResource(R.mipmap.webviewtab_back_disable);
                    add_redpacket_receive_tv.setText(R.string.yilingquhongbao);
                    if (type.equals("multi")) {
                        view2.setVisibility(View.VISIBLE);
                    }

                }
                break;
            case R.id.add_redpacket_sure_btn:
                if (type.equals("multi")){
                    if (isSelect){
                        if (isSelectPeople&&isSelectPeople2) {
                            Intent intent = new Intent();
                            intent.putExtra("sendtype", sendType);
                            if (type.equals("multi")) {
                                intent.putExtra("icon", image);
                                intent.putExtra("name", name);
                                if (selectType.equals("collect")) {
                                    intent.putExtra("hname", multi_talk_send_name2.getText().toString().trim());
                                }
                            }
                            String time = add_redpacket_time_et.getText().toString().trim();
                            intent.putExtra("time", time);
                            String context = add_redpacket_context_et.getText().toString().trim();
                            intent.putExtra("context", context);
                            intent.putExtra("isselect", isSelect);
                            setResult(10, intent);
                            finish();
                        }else{
                        Toast.makeText(this, R.string.qqbalance_toast, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if (isSelectPeople) {
                            Intent intent = new Intent();
                            intent.putExtra("sendtype", sendType);
                            if (type.equals("multi")) {
                                intent.putExtra("icon", image);
                                intent.putExtra("name", name);
                                if (selectType.equals("collect")) {
                                    intent.putExtra("hname", multi_talk_send_name2.getText().toString().trim());
                                }
                            }
                            String time = add_redpacket_time_et.getText().toString().trim();
                            intent.putExtra("time", time);
                            String context = add_redpacket_context_et.getText().toString().trim();
                            intent.putExtra("context", context);
                            intent.putExtra("isselect", isSelect);
                            setResult(10, intent);
                            finish();
                        }else{
                            Toast.makeText(this, R.string.qqbalance_toast, Toast.LENGTH_SHORT).show();
                        }
                    }

                }else if (type.equals("single")){
                    Intent intent = new Intent();
                    intent.putExtra("sendtype", sendType);
                    String time = add_redpacket_time_et.getText().toString().trim();
                    intent.putExtra("time", time);
                    String context = add_redpacket_context_et.getText().toString().trim();
                    intent.putExtra("context", context);
                    intent.putExtra("isselect", isSelect);
                    setResult(10, intent);
                    finish();
                }
                break;
        }
    }
    String name="";
    String image="";
    boolean isSelectPeople=false;
    boolean isSelectPeople2=false;
    @Override
    public void itemclick(int p) {
        if (p==0){
            sendType="m";
        }else{
            sendType="h";
        }
        name=namelist.get(p).get("name");
        image=namelist.get(p).get("image");
        if (selectType.equals("send")) {
            Picasso.with(this).load(new File(image)).resize(100, 100).into(multi_talk_send_icon);
            multi_talk_send_name.setText(name);
            isSelectPeople=true;
        }else if (selectType.equals("collect")){
            Picasso.with(this).load(new File(image)).resize(100, 100).into(multi_talk_send_icon2);
            multi_talk_send_name2.setText(name);
            isSelectPeople2=true;
        }
        dialog.dismiss();
    }
}
