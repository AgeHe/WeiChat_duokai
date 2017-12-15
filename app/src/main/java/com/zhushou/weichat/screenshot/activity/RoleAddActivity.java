package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2017/3/24.
 * 功能:
 */

public class RoleAddActivity extends Activity implements View.OnClickListener{

    private ImageView role_add_activity_title_iv;
    private RelativeLayout add_role_activity_icon_layout;
    private ImageView add_role_activity_icon_iv;
    private TextView role_add_sure_tv;
    private TextView role_add_name_tv;
    private RelativeLayout role_add_name_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_role_add);
        iniUI();
    }

    private void iniUI() {
        role_add_activity_title_iv=(ImageView)findViewById(R.id.role_add_activity_title_iv);
        role_add_activity_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add_role_activity_icon_layout=(RelativeLayout)findViewById(R.id.add_role_activity_icon_layout);
        add_role_activity_icon_iv=(ImageView)findViewById(R.id.add_role_activity_icon_iv);
        role_add_sure_tv=(TextView)findViewById(R.id.role_add_sure_tv);
        role_add_name_tv=(TextView)findViewById(R.id.role_add_name_tv);
        role_add_name_layout=(RelativeLayout)findViewById(R.id.role_add_name_layout);

        add_role_activity_icon_layout.setOnClickListener(images_click);
        role_add_sure_tv.setOnClickListener(this);
        role_add_name_layout.setOnClickListener(this);

        nameDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.role_add_sure_tv:
                if (!iconPath.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putExtra("iconpath", iconPath);
                    intent.putExtra("name", role_add_name_tv.getText().toString().trim());
                    setResult(5, intent);
                    finish();
                }else{
                    Toast.makeText(this, getString(R.string.qqbalance_toast), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.role_add_name_layout:
                dialog.show();
                break;
            case R.id.role_add_dialog_cancle:
                dialog.dismiss();
                break;
            case R.id.role_add_dialog_sure:
                String name=role_add_dialog_et.getText().toString().trim();
                if (!name.isEmpty()){
                    role_add_name_tv.setText(name);
                }
                dialog.dismiss();
                break;
        }
    }
    //设置名字dialog
    EditText role_add_dialog_et;
    AlertDialog dialog;
    private void nameDialog(){
        dialog=new AlertDialog.Builder(this).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        View view= LayoutInflater.from(this).inflate(R.layout.layout_role_add_name_dialog,null);
        dialog.setView(view);
        role_add_dialog_et=(EditText)view.findViewById(R.id.role_add_dialog_et);
        TextView role_add_dialog_cancle=(TextView)view.findViewById(R.id.role_add_dialog_cancle);
        TextView role_add_dialog_sure=(TextView)view.findViewById(R.id.role_add_dialog_sure);
        role_add_dialog_cancle.setOnClickListener(this);
        role_add_dialog_sure.setOnClickListener(this);
    }

    //从相册获取头像事件
    private ArrayList<String> mResults = new ArrayList<>();
    private View.OnClickListener images_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PhotoPickerIntent intent = new PhotoPickerIntent(RoleAddActivity.this);
            intent.setSelectModel(SelectModel.SINGLE);
            intent.setShowCarema(false); // 是否显示拍照
            //intent.setMaxTotal(1); // 最多选择照片数量，默认为9
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
                    Picasso.with(this).load(new File(iconPath)).resize(100,100).into(add_role_activity_icon_iv);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
