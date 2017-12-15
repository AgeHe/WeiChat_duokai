package com.zhushou.weichat.screenshot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.ListDataSave;

/**
 * Created by zhanglinkai on 2017/4/5.
 * 功能:
 */

public class WxVoice extends AppCompatActivity implements View.OnClickListener,OnDateSetListener {
    private ImageView wx_voice_title_iv;
    private TextView wx_voice_type_nocall;
    private TextView wx_voice_type_calling;
    private RelativeLayout wx_voice_norcall_layout;
    private RelativeLayout wx_voice_calling_time_layout;
    private ImageView wx_voice_norcall_icon;
    private TextView wx_voice_norcall_name_tv;
    private ListDataSave  save;
    private TextView wx_voice_calling_time_tv;
    private Button wx_voice_preview_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wx_voice);
        save=new ListDataSave(this,"tt");
        iniUI();
    }

    private void iniUI() {
        wx_voice_title_iv=(ImageView)findViewById(R.id.wx_voice_title_iv);
        wx_voice_type_nocall=(TextView)findViewById(R.id.wx_voice_type_nocall);
        wx_voice_type_calling=(TextView)findViewById(R.id.wx_voice_type_calling);
        wx_voice_norcall_layout=(RelativeLayout)findViewById(R.id.wx_voice_norcall_layout);
        wx_voice_calling_time_layout=(RelativeLayout)findViewById(R.id.wx_voice_calling_time_layout);
        wx_voice_norcall_icon=(ImageView)findViewById(R.id.wx_voice_norcall_icon);
        wx_voice_norcall_name_tv=(TextView)findViewById(R.id.wx_voice_norcall_name_tv);
        wx_voice_calling_time_tv=(TextView)findViewById(R.id.wx_voice_calling_time_tv);
        wx_voice_preview_btn=(Button)findViewById(R.id.wx_voice_preview_btn);


        wx_voice_title_iv.setOnClickListener(this);
        wx_voice_type_nocall.setOnClickListener(this);
        wx_voice_type_calling.setOnClickListener(this);
        wx_voice_norcall_layout.setOnClickListener(this);
        wx_voice_calling_time_layout.setOnClickListener(this);
        wx_voice_preview_btn.setOnClickListener(this);

        selectTime();
    }
    private String callType="nocall";
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wx_voice_title_iv:
                finish();
                break;
            case R.id.wx_voice_type_nocall:
                callType="nocall";
                wx_voice_type_nocall.setBackgroundResource(R.drawable.blue_shape);
                wx_voice_type_calling.setBackgroundResource(R.drawable.white_shape);
                wx_voice_type_nocall.setTextColor(getResources().getColor(R.color.color_white));
                wx_voice_type_calling.setTextColor(getResources().getColor(R.color.blue));

                wx_voice_calling_time_layout.setVisibility(View.GONE);
                break;
            case R.id.wx_voice_type_calling:
                callType="calling";
                wx_voice_type_nocall.setBackgroundResource(R.drawable.white_shape);
                wx_voice_type_calling.setBackgroundResource(R.drawable.blue_shape);
                wx_voice_type_nocall.setTextColor(getResources().getColor(R.color.blue));
                wx_voice_type_calling.setTextColor(getResources().getColor(R.color.color_white));

                wx_voice_calling_time_layout.setVisibility(View.VISIBLE);
                break;

            case R.id.wx_voice_norcall_layout:
                Intent roleIntent=new Intent(this,RoleActivity.class);
                roleIntent.putExtra("tag","voice");
                startActivityForResult(roleIntent,1);
                break;
            case R.id.wx_voice_calling_time_layout:
                mDialogAll.show(getSupportFragmentManager(),"hour_minute");
                break;
            case R.id.wx_voice_preview_btn:

                Intent intent=new Intent(this,WxVoicePreview.class);

                intent.putExtra("type",callType);
                intent.putExtra("name",wx_voice_norcall_name_tv.getText().toString().trim());
                intent.putExtra("image",image);
                intent.putExtra("time",wx_voice_calling_time_tv.getText().toString().trim());
                if (!wx_voice_norcall_name_tv.getText().toString().trim().isEmpty()&&!image.isEmpty()){
                    startActivity(intent);
                }else{
                    Toast.makeText(this, R.string.qqbalance_toast, Toast.LENGTH_SHORT).show();
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
                if (p!=10000) {
                    String name = list.get(p).get("name");
                    image = list.get(p).get("image");
                    Picasso.with(this).load(new File(image)).resize(100, 100).into(wx_voice_norcall_icon);
                    wx_voice_norcall_name_tv.setText(name);
                }
            }
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
                .setTitleStringId(getResources().getString(R.string.shijianxuanzeqi))
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .build();
    }
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = getDateToString(millseconds);
        wx_voice_calling_time_tv.setText(text);
    }
    SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }
}
