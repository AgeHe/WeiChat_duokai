package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.ListDataSave;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;

/**
 * Created by zhanglinkai on 2017/3/16.
 * 功能:
 */

public class WxTalkInforamationSet extends Activity implements View.OnClickListener {

    private ImageView wxtalk_info_title_iv;
    private RelativeLayout talk_info_myself_icon_layout;
    private ImageView talk_info_myself_iv;
    private TextView talk_info_myself_tv;
    private RelativeLayout talk_info_herself_layout;
    private ImageView talk_info_herself_iv;
    private TextView talk_info_herself_name_tv;
    private ListDataSave save;
    private RelativeLayout talk_info_bgpicture_layout;
    private ImageView talk_info_bgpicture;
    private Button talk_info_save_btn;
    private boolean isSelectbg=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_talk_information_set);
        iniUI();
    }

    private void iniUI() {
        float index=getSharedPreferences("index", Context.MODE_PRIVATE).getFloat("index",1);
        wxtalk_info_title_iv = (ImageView) findViewById(R.id.wxtalk_info_title_iv);
        talk_info_myself_icon_layout=(RelativeLayout)findViewById(R.id.talk_info_myself_icon_layout);
        talk_info_myself_iv=(ImageView)findViewById(R.id.talk_info_myself_iv);
        talk_info_myself_tv=(TextView)findViewById(R.id.talk_info_myself_tv);
        talk_info_herself_layout=(RelativeLayout)findViewById(R.id.talk_info_herself_layout);
        talk_info_herself_iv=(ImageView)findViewById(R.id.talk_info_herself_iv);
        talk_info_herself_name_tv=(TextView)findViewById(R.id.talk_info_herself_name_tv);
        talk_info_bgpicture_layout=(RelativeLayout)findViewById(R.id.talk_info_bgpicture_layout);
        talk_info_bgpicture=(ImageView)findViewById(R.id.talk_info_bgpicture);
        talk_info_save_btn=(Button)findViewById(R.id.talk_info_save_btn);


        StaticMethod.adaptiveView(talk_info_myself_iv,120,120,index);
        StaticMethod.adaptiveView(talk_info_herself_iv,120,120,index);
        StaticMethod.adaptiveView(talk_info_bgpicture,120,120,index);

        wxtalk_info_title_iv.setOnClickListener(this);
        talk_info_myself_icon_layout.setOnClickListener(this);
        talk_info_herself_layout.setOnClickListener(this);
        talk_info_bgpicture_layout.setOnClickListener(this);
        talk_info_save_btn.setOnClickListener(this);

        save=new ListDataSave(this,"tt");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wxtalk_info_title_iv:
                finish();
                break;
            case R.id.talk_info_myself_icon_layout:
                Intent intent=new Intent(this,RoleActivity.class);
                intent.putExtra("tag","singletalk");
                startActivityForResult(intent,1);
                break;
            case R.id.talk_info_herself_layout:
                Intent intent2=new Intent(this,RoleActivity.class);
                intent2.putExtra("tag","singletalk");
                startActivityForResult(intent2,2);
                break;
            case R.id.talk_info_bgpicture_layout:
                isSelectbg=false;
                PhotoPickerIntent intent3 = new PhotoPickerIntent(WxTalkInforamationSet.this);
                intent3.setSelectModel(SelectModel.SINGLE);
                intent3.setShowCarema(false); // 是否显示拍照
                //intent.setMaxTotal(1); // 最多选择照片数量，默认为9
                intent3.setSelectedPaths(mResults); // 已选中的照片地址， 用于回显选中状态
                startActivityForResult(intent3, 3);
                break;

            case R.id.talk_info_save_btn:

                Intent intent1=new Intent();
                intent1.putExtra("mname",mName);
                intent1.putExtra("mbitmap",mImage);
                intent1.putExtra("hname",hName);
                intent1.putExtra("hbitmap",hImage);
                intent1.putExtra("bgimage",bgImage);
                intent1.putExtra("selectbg",isSelectbg);
                setResult(2,intent1);
                finish();
                break;
        }
    }
    private ArrayList<String> mResults = new ArrayList<>();
    String bgImage="";
    String mName="";
    String mImage="";
    String hName="";
    String hImage="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Map<String,String>> list=save.getDataList("tt");
        if (requestCode==1){
            if (resultCode==2){
                int p=data.getIntExtra("position",10000);
                if (p!=10000){
                    mName=list.get(p).get("name");
                    mImage=list.get(p).get("image");
                    Picasso.with(this).load(new File(mImage)).resize(100,100).into(talk_info_myself_iv);
                    talk_info_myself_tv.setText(mName);
                }
            }
        }
        if (requestCode==2){
            if (resultCode==2){
                int p=data.getIntExtra("position",10000);
                if (p!=10000){
                    hName=list.get(p).get("name");
                    hImage=list.get(p).get("image");
                    Picasso.with(this).load(new File(hImage)).resize(100,100).into(talk_info_herself_iv);
                    talk_info_herself_name_tv.setText(hName);
                }
            }
        }
        if (requestCode==3){
            if (resultCode == RESULT_OK) {
                isSelectbg=true;
                mResults = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                bgImage = mResults.get(0);
                Picasso.with(this).load(new File(bgImage)).resize(100,100).into(talk_info_bgpicture);
            }
        }
    }
}
