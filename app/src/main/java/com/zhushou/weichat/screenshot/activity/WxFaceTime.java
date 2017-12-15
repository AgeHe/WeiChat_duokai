package com.zhushou.weichat.screenshot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.ListDataSave;

/**
 * Created by zhanglinkai on 2017/4/1.
 * 功能:
 */

public class WxFaceTime extends AppCompatActivity implements View.OnClickListener,OnDateSetListener{
    private ImageView wxface_time_title_iv;
    private TextView wx_facetime_type_nocall;
    private TextView wx_facetime_type_calling;
    private RelativeLayout wx_facetime_norcall_layout;
    private TextView wx_facetime_norcall_name_tv;
    private ImageView wx_facetime_norcall_icon;
    private LinearLayout wx_facetime_calling_layout;
    private RelativeLayout wx_facetime_calling_time_layout;
    private TextView wx_facetime_calling_time_tv;
    private RelativeLayout wx_facetime_calling_her_layout;
    private ImageView wx_facetime_calling_her_iv;
    private RelativeLayout wx_facetime_calling_my_layout;
    private ImageView wx_facetime_calling_my_iv;
    private Button wx_facetime_preview_btn;
    private String callType="nocall";
    private String photoType="";
    private ListDataSave save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wxfacetime);
        save=new ListDataSave(this,"tt");
        iniUI();
    }

    private void iniUI() {
        wxface_time_title_iv=(ImageView)findViewById(R.id.wxface_time_title_iv);
        wx_facetime_type_nocall=(TextView)findViewById(R.id.wx_facetime_type_nocall);
        wx_facetime_type_calling=(TextView)findViewById(R.id.wx_facetime_type_calling);
        wx_facetime_norcall_layout=(RelativeLayout)findViewById(R.id.wx_facetime_norcall_layout);
        wx_facetime_norcall_name_tv=(TextView)findViewById(R.id.wx_facetime_norcall_name_tv);
        wx_facetime_norcall_icon=(ImageView)findViewById(R.id.wx_facetime_norcall_icon);
        wx_facetime_calling_layout=(LinearLayout)findViewById(R.id.wx_facetime_calling_layout);
        wx_facetime_calling_time_layout=(RelativeLayout)findViewById(R.id.wx_facetime_calling_time_layout);
        wx_facetime_calling_time_tv=(TextView)findViewById(R.id.wx_facetime_calling_time_tv);
        wx_facetime_calling_her_layout=(RelativeLayout)findViewById(R.id.wx_facetime_calling_her_layout);
        wx_facetime_calling_her_iv=(ImageView)findViewById(R.id.wx_facetime_calling_her_iv);
        wx_facetime_calling_my_layout=(RelativeLayout)findViewById(R.id.wx_facetime_calling_my_layout);
        wx_facetime_calling_my_iv=(ImageView)findViewById(R.id.wx_facetime_calling_my_iv);
        wx_facetime_preview_btn=(Button)findViewById(R.id.wx_facetime_preview_btn);

        wxface_time_title_iv.setOnClickListener(this);
        wx_facetime_type_nocall.setOnClickListener(this);
        wx_facetime_type_calling.setOnClickListener(this);
        wx_facetime_norcall_layout.setOnClickListener(this);
        wx_facetime_calling_time_layout.setOnClickListener(this);
        wx_facetime_calling_her_layout.setOnClickListener(this);
        wx_facetime_calling_my_layout.setOnClickListener(this);
        wx_facetime_preview_btn.setOnClickListener(this);

        selectTime();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wxface_time_title_iv:
                finish();
                break;
            case R.id.wx_facetime_type_nocall:
                callType="nocall";
                wx_facetime_type_nocall.setTextColor(getResources().getColor(R.color.color_white));
                wx_facetime_type_nocall.setBackgroundResource(R.drawable.blue_shape);
                wx_facetime_type_calling.setTextColor(getResources().getColor(R.color.blue));
                wx_facetime_type_calling.setBackgroundResource(R.drawable.white_shape);
                wx_facetime_norcall_layout.setVisibility(View.VISIBLE);
                wx_facetime_calling_layout.setVisibility(View.GONE);
                break;
            case R.id.wx_facetime_type_calling:
                callType="calling";
                wx_facetime_type_calling.setTextColor(getResources().getColor(R.color.color_white));
                wx_facetime_type_calling.setBackgroundResource(R.drawable.blue_shape);
                wx_facetime_type_nocall.setTextColor(getResources().getColor(R.color.blue));
                wx_facetime_type_nocall.setBackgroundResource(R.drawable.white_shape);
                wx_facetime_calling_layout.setVisibility(View.VISIBLE);
                wx_facetime_norcall_layout.setVisibility(View.GONE);
                break;
            case R.id.wx_facetime_norcall_layout:
                Intent intent=new Intent(this,RoleActivity.class);
                intent.putExtra("tag","call");
                startActivityForResult(intent,1);
                break;
            case R.id.wx_facetime_calling_time_layout:
                mDialogAll.show(getSupportFragmentManager(),"hour_minute");
                break;
            case R.id.wx_facetime_calling_her_layout:
                photoType="her";
                selectPhoto();
                break;
            case R.id.wx_facetime_calling_my_layout:
                photoType="my";
                selectPhoto();
                break;
            case R.id.wx_facetime_preview_btn:

                Intent intent1=new Intent(this,WxFaceTimePreView.class);
                intent1.putExtra("type",callType);
                if (callType.equals("nocall")){
                    intent1.putExtra("name",name);
                    intent1.putExtra("image",image);
                    if (!name.isEmpty()&&!image.isEmpty()){
                        startActivity(intent1);
                    }else{
                        Toast.makeText(this, getResources().getString(R.string.qqbalance_toast), Toast.LENGTH_SHORT).show();
                    }
                }else if (callType.equals("calling")){
                    intent1.putExtra("time",wx_facetime_calling_time_tv.getText().toString().trim());
                    intent1.putExtra("myphotopath",myphotopath);
                    intent1.putExtra("herphotopath",herphotopath);
                    if (!myphotopath.isEmpty()&&!herphotopath.isEmpty()){
                        startActivity(intent1);
                    }else{
                        Toast.makeText(this, getResources().getString(R.string.qqbalance_toast), Toast.LENGTH_SHORT).show();
                    }
                }



                break;
        }
    }

    /**
     *功能:时间选择
     *参数:
     */
    TimePickerDialog mDialogAll;
    private void selectTime(){
        mDialogAll = new TimePickerDialog.Builder()
                .setType(Type.HOURS_MINS)
                .setCallBack(this)
                .setTitleStringId(getString(R.string.shijianxuanzeqi))
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .build();
    }
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = getDateToString(millseconds);
        wx_facetime_calling_time_tv.setText(text);
    }
    SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }
    /**
    *功能://照片处理
    *参数:
    */
    private ArrayList<String> mResults = new ArrayList<>();
    private void selectPhoto(){
        PhotoPickerIntent intent = new PhotoPickerIntent(WxFaceTime.this);
        intent.setSelectModel(SelectModel.SINGLE);
        intent.setShowCarema(false); // 是否显示拍照
        //intent.setMaxTotal(1); // 最多选择照片数量，默认为9
        intent.setSelectedPaths(mResults); // 已选中的照片地址， 用于回显选中状态
        startActivityForResult(intent, 3);
    }

    String myphotopath="";
    String herphotopath="";
    String name="";
    String image="";
    String iconPath="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //未接通时选择的通话对象
        if (requestCode==1){
            if(resultCode==2){
                int p=data.getIntExtra("position",10000);
                if (p!=10000){
                    List<Map<String,String>> list=save.getDataList("tt");
                    name=list.get(p).get("name");
                    image=list.get(p).get("image");
                    wx_facetime_norcall_name_tv.setText(name);
                    Picasso.with(this).load(new File(image)).resize(100,100).into(wx_facetime_norcall_icon);
                    //wx_facetime_norcall_icon.setImageBitmap(BitmapFactory.decodeFile(image));
                }
            }
        }
        //选取接通是的显示画面
        if (requestCode==3) {
            if (resultCode == RESULT_OK) {
                //从相册获取后的处理事件
                mResults = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                iconPath = mResults.get(0);
                if (photoType.equals("her")) {
                    herphotopath = iconPath;
                    Picasso.with(this).load(new File(iconPath)).resize(100,100).into(wx_facetime_calling_her_iv);
                } else if (photoType.equals("my")) {
                    myphotopath = iconPath;
                    Picasso.with(this).load(new File(iconPath)).resize(100,100).into(wx_facetime_calling_my_iv);
                }
            }
        }
    }
}
