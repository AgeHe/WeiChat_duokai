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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.PublicMethod;
import com.zhushou.weichat.screenshot.adapter.MultiTalkSelectPeopleDialogAdapter;


/**
 * Created by zhanglinkai on 2017/3/19.
 * 功能:
 */

public class WxTalkaddPhoto extends Activity implements View.OnClickListener,MultiTalkSelectPeopleDialogAdapter.itemClick{

    private ImageView add_photo_title_iv;
    private TextView addphoto_sendtype_m;
    private TextView addphoto_sendtype_h;
    private EditText addphoto_time_et;
    private RelativeLayout addphoto_bg_layout;
    private ImageView addphoto_bg_iv;
    private Button addphoto_sure_btn;
    private String sendType="m";
    private boolean isSelectPhoto=false;
    private View view;
    private ImageView multi_talk_send_icon;
    private TextView multi_talk_send_name;
    private LinearLayout single_talk_photo_send_layout;
    private String type="";
    private PublicMethod m;
    private List<Map<String,String>> namelist;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_talk_add_photo);
        m=new PublicMethod(this);
        iniUI();
    }

    private void iniUI() {

        type=getIntent().getStringExtra("singleOrmulti");

        add_photo_title_iv=(ImageView)findViewById(R.id.add_photo_title_iv);
        addphoto_sendtype_m=(TextView)findViewById(R.id.addphoto_sendtype_m);
        addphoto_sendtype_h=(TextView)findViewById(R.id.addphoto_sendtype_h);
        addphoto_time_et=(EditText)findViewById(R.id.addphoto_time_et);
        addphoto_bg_layout=(RelativeLayout)findViewById(R.id.addphoto_bg_layout);
        addphoto_bg_iv=(ImageView)findViewById(R.id.addphoto_bg_iv);
        addphoto_sure_btn=(Button)findViewById(R.id.addphoto_sure_btn);

        view=findViewById(R.id.multi_talk_photo_send_layout);
        multi_talk_send_icon=(ImageView)view.findViewById(R.id.multi_talk_send_icon);
        multi_talk_send_name=(TextView)view.findViewById(R.id.multi_talk_send_name);
        single_talk_photo_send_layout=(LinearLayout)findViewById(R.id.single_talk_photo_send_layout);

        add_photo_title_iv.setOnClickListener(this);
        addphoto_sendtype_m.setOnClickListener(this);
        addphoto_sendtype_h.setOnClickListener(this);
        addphoto_time_et.setOnClickListener(this);
        addphoto_bg_layout.setOnClickListener(images_click);
        addphoto_sure_btn.setOnClickListener(this);
        view.setOnClickListener(this);


        if (type.equals("single")){
            view.setVisibility(View.GONE);
            single_talk_photo_send_layout.setVisibility(View.VISIBLE);
        }else if (type.equals("multi")){
            view.setVisibility(View.VISIBLE);
            single_talk_photo_send_layout.setVisibility(View.GONE);
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
            case R.id.add_photo_title_iv:
                finish();
                break;
            case R.id.addphoto_sendtype_m:
                sendType="m";
                addphoto_sendtype_m.setBackgroundColor(getResources().getColor(R.color.blue));
                addphoto_sendtype_h.setBackgroundColor(getResources().getColor(R.color.color_white));
                break;
            case R.id.addphoto_sendtype_h:
                sendType="h";
                addphoto_sendtype_m.setBackgroundColor(getResources().getColor(R.color.color_white));
                addphoto_sendtype_h.setBackgroundColor(getResources().getColor(R.color.blue));
                break;
            case R.id.addphoto_sure_btn:
                if (isSelectPhoto) {
                    Intent intent = new Intent();
                    intent.putExtra("sendtype", sendType);

                    if (type.equals("multi")){
                        intent.putExtra("icon", image);
                        intent.putExtra("name", name);
                    }

                    String time = addphoto_time_et.getText().toString().trim();
                    intent.putExtra("time", time);
                    intent.putExtra("bitmap", iconPath);
                    setResult(6, intent);
                    finish();
                }else{
                    Toast.makeText(this, R.string.qingxuanzetupian, Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.multi_talk_photo_send_layout:
                isSelectPeople=false;
                dialog.show();
                break;
        }
    }


    //从相册获取头像事件
    private ArrayList<String> mResults = new ArrayList<>();
    private View.OnClickListener images_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isSelectPhoto=false;
            PhotoPickerIntent intent = new PhotoPickerIntent(WxTalkaddPhoto.this);
            intent.setSelectModel(SelectModel.SINGLE);
            intent.setShowCarema(false); // 是否显示拍照
            intent.setSelectedPaths(mResults); // 已选中的照片地址， 用于回显选中状态
            startActivityForResult(intent, 2);
        }
    };
    //获取照片后的处理事件
    String iconPath="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    mResults = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    iconPath = mResults.get(0);
                    Picasso.with(this).load(new File(iconPath)).resize(100,100).into(addphoto_bg_iv);
                    isSelectPhoto=true;
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
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
