package com.zhushou.weichat.screenshot.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.PublicMethod;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;
import com.zhushou.weichat.screenshot.adapter.FaceRecycleAdapter;
import com.zhushou.weichat.screenshot.adapter.MultiTalkSelectPeopleDialogAdapter;

import static com.zhushou.weichat.R.id.multi_talk_txt_send_layout;


/**
 * Created by zhanglinkai on 2017/3/17.
 * 功能:
 */

public class WxTalkaddTxt extends AppCompatActivity implements View.OnClickListener,FaceRecycleAdapter.faceClick,MultiTalkSelectPeopleDialogAdapter.itemClick{

    private TextView addtxt_sendtype_m;
    private TextView addtxt_sendtype_h;
    private EditText addtxt_time_et;
    private EditText addtxt_context_et;
    private Button addtxt_sure_btn;
    private String sendType="m";
    private RecyclerView addtalk_recyclerview;
    private ImageView add_picture_title_iv;
    //private RelativeLayout multi_talk_txt_send_layout;
    private LinearLayout single_talk_txt_send_layout;
    private ImageView multi_talk_send_icon;
    private TextView multi_talk_send_name;
    private String type="";
    private PublicMethod m;
    private AlertDialog dialog;
    private List<Map<String,String>> namelist;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_talk_add_txt);
        m=new PublicMethod(this);
        iniUI();
    }

    private void iniUI() {

        type=getIntent().getStringExtra("singleOrmulti");

        addtxt_sendtype_m=(TextView)findViewById(R.id.addtxt_sendtype_m);
        addtxt_sendtype_h=(TextView)findViewById(R.id.addtxt_sendtype_h);
        addtxt_time_et=(EditText)findViewById(R.id.addtxt_time_et);
        addtxt_context_et=(EditText)findViewById(R.id.addtxt_context_et);
        addtxt_sure_btn=(Button)findViewById(R.id.addtxt_sure_btn);
        addtalk_recyclerview=(RecyclerView)findViewById(R.id.addtalk_recyclerview);
        add_picture_title_iv=(ImageView)findViewById(R.id.add_picture_title_iv);

        view=findViewById(multi_talk_txt_send_layout);
        single_talk_txt_send_layout=(LinearLayout)findViewById(R.id.single_talk_txt_send_layout);
        multi_talk_send_icon=(ImageView)view.findViewById(R.id.multi_talk_send_icon);
        multi_talk_send_name=(TextView)view.findViewById(R.id.multi_talk_send_name);

        addtxt_sendtype_m.setOnClickListener(this);
        addtxt_sendtype_h.setOnClickListener(this);
        addtxt_sure_btn.setOnClickListener(this);
        add_picture_title_iv.setOnClickListener(this);
        view.setOnClickListener(this);

        if (type.equals("single")){
            view.setVisibility(View.GONE);
            single_talk_txt_send_layout.setVisibility(View.VISIBLE);
        }else if (type.equals("multi")){
            view.setVisibility(View.VISIBLE);
            single_talk_txt_send_layout.setVisibility(View.GONE);
        }

        m=new PublicMethod(this);
        namelist= (List<Map<String, String>>) getIntent().getSerializableExtra("list");
        for (int i = 0; i < namelist.size(); i++) {
            if (namelist.get(i).get("name").isEmpty()&&namelist.get(i).get("image").isEmpty()){
                namelist.remove(i);
            }
        }
        dialog=m.multiTalkDialog(namelist);

        List<String> list =new ArrayList<>();
        List<String> faceName = StaticMethod.faceNameList(this);
        for (int i = 0; i < faceName.size(); i++) {
            list.add("file:///android_asset/"+faceName.get(i));
        }

        FaceRecycleAdapter adapter=new FaceRecycleAdapter(this,list,this);
        addtalk_recyclerview.setLayoutManager(new GridLayoutManager(this,3,0,false));
        addtalk_recyclerview.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.addtxt_sendtype_m:
                sendType="m";
                addtxt_sendtype_m.setBackgroundResource(R.drawable.blue_shape);
                addtxt_sendtype_h.setBackgroundResource(R.drawable.white_shape);
                break;
            case R.id.addtxt_sendtype_h:
                sendType="h";
                addtxt_sendtype_m.setBackgroundResource(R.drawable.white_shape);
                addtxt_sendtype_h.setBackgroundResource(R.drawable.blue_shape);
                break;
            case R.id.addtxt_sure_btn:
                String context=addtxt_context_et.getText().toString().trim();
                if (!context.isEmpty()&&isSelectPeople&&type.equals("multi")||!context.isEmpty()&&type.equals("single")) {
                    Intent intent = new Intent();

                    intent.putExtra("sendtype", sendType);
                    if (type.equals("multi")){
                        intent.putExtra("icon", image);
                        intent.putExtra("name", name);
                    }
                    String time = addtxt_time_et.getText().toString().trim();
                    intent.putExtra("time", time);
                    intent.putExtra("context", context);
                    WxTalkaddTxt.this.setResult(4, intent);
                    WxTalkaddTxt.this.finish();
                }else{
                    Toast.makeText(this, R.string.qqbalance_toast, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.add_picture_title_iv:
                finish();
                break;
            case R.id.multi_talk_txt_send_layout:
                isSelectPeople=false;
                dialog.show();
                break;
        }
    }


    @Override
    public void click(int position) {
        //输入表情
        Bitmap bitmap=StaticMethod.getImageFromAssetsFile(this,StaticMethod.faceNameList(this).get(position));
        ImageSpan imageSpan = new ImageSpan(this,bitmap);
        SpannableString spannableString = new SpannableString("["+position+"]");
        spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_MARK_MARK);
        addtxt_context_et.append(spannableString);
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
