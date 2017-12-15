package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.ListDataSave;
import com.zhushou.weichat.screenshot.adapter.WxMultiTalkInfosetAdapter;
import com.zhushou.weichat.screenshot.view.NewGridView;

import static com.zhushou.weichat.R.id.multi_talk_infoset_dialog_et;

/**
 * Created by zhanglinkai on 2017/4/6.
 * 功能:
 */

public class WxMultiTalkInfoSet extends Activity implements View.OnClickListener,WxMultiTalkInfosetAdapter.addItem {
    private ImageView multi_talk_infoset_title_iv;
    private NewGridView multi_talk_infoset_icon_gridview;
    private RelativeLayout multi_talk_infoset_bgimage_layout;
    private RelativeLayout multi_talk_infoset_talkname_layout;
    private TextView multi_talk_infoset_talkname;
    private RelativeLayout multi_talk_infoset_talknumber_layout;
    private TextView multi_talk_infoset_talknumber;
    private List<Map<String,String>> list;
    private WxMultiTalkInfosetAdapter adapter;
    private ListDataSave save;
    private AlertDialog nameDialog;
    private AlertDialog numberDialog;
    private Button multi_talk_infoset_save;
    private ImageView multi_talk_infoset_bgimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_multi_talk_infoset);
        iniUI();
    }

    private void iniUI() {
        multi_talk_infoset_title_iv=(ImageView)findViewById(R.id.multi_talk_infoset_title_iv);
        multi_talk_infoset_icon_gridview=(NewGridView)findViewById(R.id.multi_talk_infoset_icon_gridview);
        multi_talk_infoset_bgimage_layout=(RelativeLayout)findViewById(R.id.multi_talk_infoset_bgimage_layout);
        multi_talk_infoset_talkname_layout=(RelativeLayout)findViewById(R.id.multi_talk_infoset_talkname_layout);
        multi_talk_infoset_talkname=(TextView)findViewById(R.id.multi_talk_infoset_talkname);
        multi_talk_infoset_talknumber_layout=(RelativeLayout)findViewById(R.id.multi_talk_infoset_talknumber_layout);
        multi_talk_infoset_talknumber=(TextView)findViewById(R.id.multi_talk_infoset_talknumber);
        multi_talk_infoset_save=(Button)findViewById(R.id.multi_talk_infoset_save);
        multi_talk_infoset_bgimage=(ImageView)findViewById(R.id.multi_talk_infoset_bgimage);


        multi_talk_infoset_title_iv.setOnClickListener(this);
        multi_talk_infoset_bgimage_layout.setOnClickListener(this);
        multi_talk_infoset_talkname_layout.setOnClickListener(this);
        multi_talk_infoset_talknumber_layout.setOnClickListener(this);
        multi_talk_infoset_save.setOnClickListener(this);

        nameDialog=talkNameDialog("name");
        numberDialog=talkNameDialog("number");
        save=new ListDataSave(this,"tt");
        list=new ArrayList<>();
        addItemini();
        adapter=new WxMultiTalkInfosetAdapter(this,list,this);
        multi_talk_infoset_icon_gridview.setAdapter(adapter);

    }
    private ArrayList<String> mResults = new ArrayList<>();
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.multi_talk_infoset_title_iv:
                finish();
                break;
            case R.id.multi_talk_infoset_bgimage_layout:
                isSelectBg=false;
                PhotoPickerIntent intent = new PhotoPickerIntent(this);
                intent.setSelectModel(SelectModel.SINGLE);
                intent.setShowCarema(false); // 是否显示拍照
                //intent.setMaxTotal(1); // 最多选择照片数量，默认为9
                intent.setSelectedPaths(mResults); // 已选中的照片地址， 用于回显选中状态
                startActivityForResult(intent, 3);

                break;
            case R.id.multi_talk_infoset_talkname_layout:
                name_et.setText("");
                nameDialog.show();
                break;
            case R.id.multi_talk_infoset_talknumber_layout:
                number_et.setText("");
                numberDialog.show();
                break;
            case R.id.multi_talk_infoset_dialog_cancle:
                if (numberDialog.isShowing()){
                    numberDialog.dismiss();
                }else if (nameDialog.isShowing()){
                    nameDialog.dismiss();
                }
                break;
            case R.id.multi_talk_infoset_dialog_sure:

                if (numberDialog.isShowing()){
                    String result=number_et.getText().toString().trim();
                    multi_talk_infoset_talknumber.setText(result);
                    numberDialog.dismiss();
                }else if (nameDialog.isShowing()){
                    String result=name_et.getText().toString().trim();
                    multi_talk_infoset_talkname.setText(result);
                    nameDialog.dismiss();
                }
                break;
            case R.id.multi_talk_infoset_save:

                Intent resultIntent=new Intent();
                resultIntent.putExtra("list", (Serializable) list);
                resultIntent.putExtra("name",multi_talk_infoset_talkname.getText().toString().trim() );
                resultIntent.putExtra("number", multi_talk_infoset_talknumber.getText().toString().trim());
                resultIntent.putExtra("bgimage", iconPath);
                resultIntent.putExtra("isSelectBg", isSelectBg);
                setResult(2,resultIntent);
                finish();

                break;
        }
    }
    /**
    *功能:设置群聊名字和成员人数的dialog
    *参数:
    */
    EditText name_et;
    EditText number_et;
    private AlertDialog talkNameDialog(String s){
        AlertDialog dialog=new AlertDialog.Builder(this).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view= LayoutInflater.from(this).inflate(R.layout.layout_multi_talk_infoset_name_dialog,null);

        TextView multi_talk_infoset_dialog_title=(TextView)view.findViewById(R.id.multi_talk_infoset_dialog_title);

        TextView multi_talk_infoset_dialog_cancle=(TextView)view.findViewById(R.id.multi_talk_infoset_dialog_cancle);
        TextView multi_talk_infoset_dialog_sure=(TextView)view.findViewById(R.id.multi_talk_infoset_dialog_sure);

        if (s.equals("name")){
            name_et=(EditText)view.findViewById(multi_talk_infoset_dialog_et);
            multi_talk_infoset_dialog_title.setText(R.string.shezhiqunliaomingchen);
            name_et.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (s.equals("number")) {
            number_et=(EditText)view.findViewById(multi_talk_infoset_dialog_et);
            multi_talk_infoset_dialog_title.setText(R.string.shezhiqunchengyuanrenshu);
            number_et.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        multi_talk_infoset_dialog_cancle.setOnClickListener(this);
        multi_talk_infoset_dialog_sure.setOnClickListener(this);


        dialog.setView(view);
        return dialog;
    }

    @Override
    public void additem() {
        //添加成员
        Intent intent=new Intent(this,RoleActivity.class);
        intent.putExtra("tag","singletalk");
        startActivityForResult(intent,1);
    }
    String iconPath="";
    boolean isSelectBg=false;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==2){
                int p=data.getIntExtra("position",10000);
                List<Map<String,String>> mapList=save.getDataList("tt");
                if (p!=10000) {
                    list.remove(list.size()-1);
                    String icon = mapList.get(p).get("image");
                    String name = mapList.get(p).get("name");
                    Map<String,String> map=new HashMap<>();
                    map.put("image",icon);
                    map.put("name",name);
                    list.add(map);
                    addItemini();
                    adapter.notifyDataSetChanged();
                }
            }
        }
        if (requestCode==3){
            if (resultCode == RESULT_OK) {
                isSelectBg=true;
                mResults = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                iconPath = mResults.get(0);
                Picasso.with(this).load(new File(iconPath)).resize(100,100).into(multi_talk_infoset_bgimage);
            }
        }
    }
    private void addItemini(){
        Map<String,String> map=new HashMap<>();
        map.put("image","");
        map.put("name","");
        list.add(map);
    }
}
