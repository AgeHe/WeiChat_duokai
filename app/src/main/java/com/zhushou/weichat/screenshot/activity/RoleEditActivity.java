package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2017/3/28.
 * 功能:
 */

public class RoleEditActivity extends Activity implements View.OnClickListener{
    private ImageView role_edit_title_iv;
    private TextView role_edit_sure_tv;
    private RelativeLayout add_role_edit_icon_layout;
    private ImageView add_role_edit_icon_iv;
    private RelativeLayout role_edit_name_layout;
    private TextView role_edit_name_tv;
    private TextView role_add_edit_clear_tv;
    private int p;
    private String image;
    private boolean isChange=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_role);
        iniUI();
    }

    private void iniUI() {

        String name=getIntent().getStringExtra("name");
        image=getIntent().getStringExtra("image");
        p=getIntent().getIntExtra("position",0);

        role_edit_title_iv=(ImageView)findViewById(R.id.role_edit_title_iv);
        role_edit_sure_tv=(TextView)findViewById(R.id.role_edit_sure_tv);
        add_role_edit_icon_layout=(RelativeLayout)findViewById(R.id.add_role_edit_icon_layout);
        add_role_edit_icon_iv=(ImageView)findViewById(R.id.add_role_edit_icon_iv);
        role_edit_name_layout=(RelativeLayout)findViewById(R.id.role_edit_name_layout);
        role_edit_name_tv=(TextView)findViewById(R.id.role_edit_name_tv);
        role_add_edit_clear_tv=(TextView)findViewById(R.id.role_add_edit_clear_tv);

        add_role_edit_icon_iv.setImageBitmap(BitmapFactory.decodeFile(image));
        role_edit_name_tv.setText(name);
        nameDialog();

        role_edit_title_iv.setOnClickListener(this);
        role_edit_sure_tv.setOnClickListener(this);
        role_edit_name_layout.setOnClickListener(this);
        role_add_edit_clear_tv.setOnClickListener(this);
        add_role_edit_icon_layout.setOnClickListener(images_click);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.role_edit_title_iv:
                finish();
                break;
            case R.id.role_edit_sure_tv:
                Intent sureIntent=new Intent();
                sureIntent.putExtra("tag","sure");
                sureIntent.putExtra("position", p);
                if (isChange) {
                    sureIntent.putExtra("image", bgImage);
                }else{
                    sureIntent.putExtra("image", image);
                }
                sureIntent.putExtra("name",role_edit_name_tv.getText().toString().trim());
                setResult(7,sureIntent);
                finish();
                break;
            case R.id.role_edit_name_layout:
                dialog.show();
                break;
            case R.id.role_add_edit_clear_tv:
                Intent clearIntent=new Intent();
                clearIntent.putExtra("tag","clear");
                clearIntent.putExtra("position",p);
                setResult(7,clearIntent);
                finish();
                break;
            case R.id.role_add_dialog_cancle:
                dialog.dismiss();
                break;
            case R.id.role_add_dialog_sure:
                String name=role_add_dialog_et.getText().toString().trim();
                if (!name.isEmpty()){
                    role_edit_name_tv.setText(name);
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
    private ArrayList<String> mResults = new ArrayList<>();
    //从相册获取头像事件
    private View.OnClickListener images_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isChange=false;
            PhotoPickerIntent intent3 = new PhotoPickerIntent(RoleEditActivity.this);
            intent3.setSelectModel(SelectModel.SINGLE);
            intent3.setShowCarema(false); // 是否显示拍照
            //intent.setMaxTotal(1); // 最多选择照片数量，默认为9
            intent3.setSelectedPaths(mResults); // 已选中的照片地址， 用于回显选中状态
            startActivityForResult(intent3, 3);

        }
    };
    //获取照片后的处理事件
    private Bitmap head;
    static Uri uri;
    long name;
    String bgImage="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==3){
            if (resultCode == RESULT_OK) {
                isChange=true;
                mResults = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                bgImage = mResults.get(0);
                Picasso.with(this).load(new File(bgImage)).resize(100,100).into(add_role_edit_icon_iv);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
