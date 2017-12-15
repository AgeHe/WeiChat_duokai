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
import com.zhushou.weichat.screenshot.Utils.ListDataSave;

import static com.zhushou.weichat.R.id.multi_talk_infoset_dialog_cancle;

/**
 * Created by zhanglinkai on 2017/4/10.
 * 功能:
 */

public class WxFriendCircleInfoset extends Activity implements View.OnClickListener{
    private ImageView wx_friendcircle_infoset_title_iv;
    private RelativeLayout wx_friendcircle_role_layout;
    private ImageView wx_friendcircle_infoset_icon_iv;
    private TextView wx_friendcircle_infoset_name_tv;
    private RelativeLayout wx_friendcircle_infoset_bg_layout;
    private ImageView wx_friendcircle_infoset_bg_iv;
    private RelativeLayout wx_friendcircle_infoset_infomation_layout;
    private ImageView wx_friendcircle_infoset_no_iv;
    private TextView wx_friendcircle_infoset_no_tv;
    private RelativeLayout wx_friendcircle_infoset_number_layout;
    private TextView wx_friendcircle_infoset_number_tv;
    private Button wx_friendcircle_infoset_sure_btn;
    private ListDataSave save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wx_friendcircle_info);
        save=new ListDataSave(this,"tt");
        iniUI();
    }

    private void iniUI() {
        wx_friendcircle_infoset_title_iv=(ImageView)findViewById(R.id.wx_friendcircle_infoset_title_iv);
        wx_friendcircle_role_layout=(RelativeLayout)findViewById(R.id.wx_friendcircle_role_layout);
        wx_friendcircle_infoset_icon_iv=(ImageView)findViewById(R.id.wx_friendcircle_infoset_icon_iv);
        wx_friendcircle_infoset_name_tv=(TextView)findViewById(R.id.wx_friendcircle_infoset_name_tv);
        wx_friendcircle_infoset_bg_layout=(RelativeLayout)findViewById(R.id.wx_friendcircle_infoset_bg_layout);
        wx_friendcircle_infoset_bg_iv=(ImageView)findViewById(R.id.wx_friendcircle_infoset_bg_iv);
        wx_friendcircle_infoset_infomation_layout=(RelativeLayout)findViewById(R.id.wx_friendcircle_infoset_infomation_layout);
        wx_friendcircle_infoset_no_iv=(ImageView)findViewById(R.id.wx_friendcircle_infoset_no_iv);
        wx_friendcircle_infoset_no_tv=(TextView)findViewById(R.id.wx_friendcircle_infoset_no_tv);
        wx_friendcircle_infoset_number_layout=(RelativeLayout)findViewById(R.id.wx_friendcircle_infoset_number_layout);
        wx_friendcircle_infoset_number_tv=(TextView)findViewById(R.id.wx_friendcircle_infoset_number_tv);
        wx_friendcircle_infoset_sure_btn=(Button)findViewById(R.id.wx_friendcircle_infoset_sure_btn);

        wx_friendcircle_infoset_title_iv.setOnClickListener(this);
        wx_friendcircle_role_layout.setOnClickListener(this);
        wx_friendcircle_infoset_bg_layout.setOnClickListener(this);
        wx_friendcircle_infoset_infomation_layout.setOnClickListener(this);
        wx_friendcircle_infoset_number_layout.setOnClickListener(this);
        wx_friendcircle_infoset_sure_btn.setOnClickListener(this);


        dialog();
    }
    private ArrayList<String> mResults = new ArrayList<>();
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wx_friendcircle_infoset_title_iv:
                finish();
                break;
            case R.id.wx_friendcircle_role_layout:
                selectTag="role";
                selectRole();
                break;
            case R.id.wx_friendcircle_infoset_bg_layout:
                PhotoPickerIntent intent3 = new PhotoPickerIntent(this);
                intent3.setSelectModel(SelectModel.SINGLE);
                intent3.setShowCarema(false); // 是否显示拍照
                intent3.setSelectedPaths(mResults); // 已选中的照片地址， 用于回显选中状态
                startActivityForResult(intent3, 3);
                break;
            case R.id.wx_friendcircle_infoset_infomation_layout:
                selectTag="info";
                selectRole();
                break;
            case R.id.wx_friendcircle_infoset_number_layout:
                dialog.show();
                break;
            case multi_talk_infoset_dialog_cancle:
                dialog.dismiss();
                break;
            case R.id.multi_talk_infoset_dialog_sure:

                wx_friendcircle_infoset_number_tv.setText(multi_talk_infoset_dialog_et.getText().toString().trim());
                dialog.dismiss();

                break;
            case R.id.wx_friendcircle_infoset_sure_btn:
                String rolename=wx_friendcircle_infoset_name_tv.getText().toString().trim();
                String infoname=wx_friendcircle_infoset_no_tv.getText().toString().trim();
                if (!rolename.isEmpty()&&!bgImage.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putExtra("rolename", rolename);
                    intent.putExtra("roleimage", roleimage);
                    intent.putExtra("bgimage", bgImage);
                    intent.putExtra("infoname", infoname);
                    intent.putExtra("infoimage", infoimage);
                    intent.putExtra("number", wx_friendcircle_infoset_number_tv.getText().toString().trim());
                    setResult(2, intent);
                    finish();
                }else{
                    Toast.makeText(this, getString(R.string.qqbalance_toast), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    /**
    *功能:选择角色
    *参数:
    */
    private String selectTag="";
    private void selectRole(){
        Intent intent=new Intent(this,RoleActivity.class);
        intent.putExtra("tag","call");
        startActivityForResult(intent,1);
    }

    String bgImage="";
    String infoimage="";
    String roleimage="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==2){
                List<Map<String,String>> list=save.getDataList("tt");
                int p=data.getIntExtra("position",10000);
                if (p!=10000){
                    if (selectTag.equals("info")){
                        String name=list.get(p).get("name");
                        infoimage=list.get(p).get("image");
                        Picasso.with(this).load(new File(infoimage)).resize(100,100).into(wx_friendcircle_infoset_no_iv);
                        wx_friendcircle_infoset_no_tv.setText(name);
                    }else if (selectTag.equals("role")){
                        String name=list.get(p).get("name");
                        roleimage=list.get(p).get("image");
                        Picasso.with(this).load(new File(roleimage)).resize(100,100).into(wx_friendcircle_infoset_icon_iv);
                        wx_friendcircle_infoset_name_tv.setText(name);
                    }
                }
            }
        }
        if (requestCode==3){
            if (resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                bgImage = mResults.get(0);
                Picasso.with(this).load(new File(bgImage)).resize(100,100).into(wx_friendcircle_infoset_bg_iv);
            }
        }
    }
    /**
    *功能:未读信息数dialog
    *参数:
    */
    AlertDialog dialog;
    EditText multi_talk_infoset_dialog_et;
    private void dialog(){
        dialog=new AlertDialog.Builder(this).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        View view= LayoutInflater.from(this).inflate(R.layout.layout_multi_talk_infoset_name_dialog,null);
        TextView multi_talk_infoset_dialog_title=(TextView)view.findViewById(R.id.multi_talk_infoset_dialog_title);
        multi_talk_infoset_dialog_title.setText(getString(R.string.shezhiweiduxinxi));
        multi_talk_infoset_dialog_et=(EditText)view.findViewById(R.id.multi_talk_infoset_dialog_et);
        multi_talk_infoset_dialog_et.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextView multi_talk_infoset_dialog_cancle=(TextView)view.findViewById(R.id.multi_talk_infoset_dialog_cancle);
        TextView multi_talk_infoset_dialog_sure=(TextView)view.findViewById(R.id.multi_talk_infoset_dialog_sure);
        multi_talk_infoset_dialog_cancle.setOnClickListener(this);
        multi_talk_infoset_dialog_sure.setOnClickListener(this);
        dialog.setView(view);

    }
}
