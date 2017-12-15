package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.ListDataSave;
import com.zhushou.weichat.screenshot.adapter.WxFriendCircleAddAdapter;
import com.zhushou.weichat.screenshot.view.NewGridView;

/**
 * Created by zhanglinkai on 2017/4/10.
 * 功能:
 */

public class WxFriendCircleAdd extends Activity implements View.OnClickListener,WxFriendCircleAddAdapter.itemClick{
    private ImageView wx_friendcircle_add_title_iv;
    private Button wx_friendcircle_add_title_btn;
    private RelativeLayout wx_friendcircle_add_role_layout;
    private ImageView wx_friendcircle_add_role_iv;
    private TextView wx_friendcircle_add_role_tv;
    private EditText wx_friendcircle_add_context_et;
    private NewGridView wx_friendcircle_add_image_gridview;
    private RelativeLayout wx_friendcircle_add_time_layout;
    private TextView wx_friendcircle_add_time;
    private RelativeLayout wx_friendcircle_add_location_layout;
    private TextView wx_friendcircle_add_location;
    private ListDataSave save;
    private AlertDialog timeDialog;
    private AlertDialog locationDialog;
    private List<String> list;
    private WxFriendCircleAddAdapter addAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wx_friendcircle_add);
        iniUI();
    }

    private void iniUI() {
        save=new ListDataSave(this,"tt");
        wx_friendcircle_add_title_iv=(ImageView)findViewById(R.id.wx_friendcircle_add_title_iv);
        wx_friendcircle_add_title_btn=(Button)findViewById(R.id.wx_friendcircle_add_title_btn);
        wx_friendcircle_add_role_layout=(RelativeLayout)findViewById(R.id.wx_friendcircle_add_role_layout);
        wx_friendcircle_add_role_iv=(ImageView)findViewById(R.id.wx_friendcircle_add_role_iv);
        wx_friendcircle_add_role_tv=(TextView)findViewById(R.id.wx_friendcircle_add_role_tv);
        wx_friendcircle_add_context_et=(EditText)findViewById(R.id.wx_friendcircle_add_context_et);
        wx_friendcircle_add_image_gridview=(NewGridView)findViewById(R.id.wx_friendcircle_add_image_gridview);
        wx_friendcircle_add_time_layout=(RelativeLayout)findViewById(R.id.wx_friendcircle_add_time_layout);
        wx_friendcircle_add_time=(TextView)findViewById(R.id.wx_friendcircle_add_time);
        wx_friendcircle_add_location_layout=(RelativeLayout)findViewById(R.id.wx_friendcircle_add_location_layout);
        wx_friendcircle_add_location=(TextView)findViewById(R.id.wx_friendcircle_add_location);



        wx_friendcircle_add_title_iv.setOnClickListener(this);
        wx_friendcircle_add_title_btn.setOnClickListener(this);
        wx_friendcircle_add_role_layout.setOnClickListener(this);
        wx_friendcircle_add_time_layout.setOnClickListener(this);
        wx_friendcircle_add_location_layout.setOnClickListener(this);

        timeDialog=iniDialog("time",getString(R.string.shezhifabushijian));
        locationDialog=iniDialog("location",getString(R.string.shezhisuozaiweizhi));

        list=new ArrayList<>();
        list.add("");
        addAdapter=new WxFriendCircleAddAdapter(this,list,this);
        wx_friendcircle_add_image_gridview.setAdapter(addAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wx_friendcircle_add_title_iv:
                finish();
                break;
            case R.id.wx_friendcircle_add_title_btn:

                Intent sendIntent=new Intent();
                String name=wx_friendcircle_add_role_tv.getText().toString().trim();
                String context=wx_friendcircle_add_context_et.getText().toString().trim();
                sendIntent.putExtra("name",name);
                sendIntent.putExtra("image",image);
                sendIntent.putExtra("context",context);
                for (int i=0;i<list.size();i++){
                    if (list.get(i).isEmpty()){
                        list.remove(i);
                    }
                }
                sendIntent.putExtra("list", (Serializable) list);
                sendIntent.putExtra("time", wx_friendcircle_add_time.getText().toString().trim());
                sendIntent.putExtra("location", wx_friendcircle_add_location.getText().toString().trim());
                setResult(4,sendIntent);
                finish();
                break;
            case R.id.wx_friendcircle_add_role_layout:
                Intent intent=new Intent(this,RoleActivity.class);
                intent.putExtra("tag","call");
                startActivityForResult(intent,1);
                break;
            case R.id.wx_friendcircle_add_time_layout:
                timeDialog.show();
                break;
            case R.id.wx_friendcircle_add_location_layout:
                locationDialog.show();
                break;
            case R.id.multi_talk_infoset_dialog_cancle:
                if (timeDialog.isShowing()){
                    timeDialog.dismiss();
                }else if(locationDialog.isShowing()){
                    locationDialog.dismiss();
                }
                break;
            case R.id.multi_talk_infoset_dialog_sure:
                if (timeDialog.isShowing()){
                    wx_friendcircle_add_time.setText(multi_talk_infoset_dialog_et_time.getText().toString().trim());
                    timeDialog.dismiss();
                }else if(locationDialog.isShowing()){
                    wx_friendcircle_add_location.setText(multi_talk_infoset_dialog_et_location.getText().toString().trim());
                    locationDialog.dismiss();
                }
                break;
        }
    }
    String image="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==2){
                List<Map<String,String>> list=save.getDataList("tt");
                int p=data.getIntExtra("position",10000);
                if (p!=10000){
                    String name=list.get(p).get("name");
                    image=list.get(p).get("image");
                    wx_friendcircle_add_role_tv.setText(name);
                    Picasso.with(this).load(new File(image)).resize(100,100).into(wx_friendcircle_add_role_iv);
                }
            }
        }
        if (requestCode==3){
            if (resultCode == RESULT_OK) {
                list.clear();
                mResults = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                for (int i=0;i<mResults.size();i++){
                    list.add(mResults.get(i));
                }
                list.add("");
                addAdapter.notifyDataSetChanged();
            }
        }
    }
    /**
    *功能:初始化dialog
    *参数:
    */
    EditText multi_talk_infoset_dialog_et_time;
    EditText multi_talk_infoset_dialog_et_location;
    private AlertDialog iniDialog(String tag,String s){
        AlertDialog dialog=new AlertDialog.Builder(this).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        View view= LayoutInflater.from(this).inflate(R.layout.layout_multi_talk_infoset_name_dialog,null);
        TextView multi_talk_infoset_dialog_title=(TextView)view.findViewById(R.id.multi_talk_infoset_dialog_title);
        multi_talk_infoset_dialog_title.setText(s);
        if (tag.equals("time")){
            multi_talk_infoset_dialog_et_time=(EditText)view.findViewById(R.id.multi_talk_infoset_dialog_et);
        }else if (tag.equals("location")){
            multi_talk_infoset_dialog_et_location=(EditText)view.findViewById(R.id.multi_talk_infoset_dialog_et);
        }

        TextView multi_talk_infoset_dialog_cancle=(TextView)view.findViewById(R.id.multi_talk_infoset_dialog_cancle);
        TextView multi_talk_infoset_dialog_sure=(TextView)view.findViewById(R.id.multi_talk_infoset_dialog_sure);
        multi_talk_infoset_dialog_cancle.setOnClickListener(this);
        multi_talk_infoset_dialog_sure.setOnClickListener(this);
        dialog.setView(view);
        return dialog;
    }

    @Override
    public void clearItem(final int p) {
        new AlertDialog.Builder(this).setTitle(R.string.caozuo).setMessage(R.string.shanchu).setNegativeButton(R.string.quxiaoi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton(R.string.queding, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mResults.remove(p);
                list.remove(p);
                addAdapter.notifyDataSetChanged();
            }
        }).show();
    }
    private ArrayList<String> mResults = new ArrayList<>();
    @Override
    public void addItem() {
        PhotoPickerIntent intent3 = new PhotoPickerIntent(this);
        intent3.setSelectModel(SelectModel.MULTI);
        intent3.setShowCarema(false); // 是否显示拍照
        intent3.setMaxTotal(9);
        intent3.setSelectedPaths(mResults); // 已选中的照片地址， 用于回显选中状态
        startActivityForResult(intent3, 3);
    }
}
