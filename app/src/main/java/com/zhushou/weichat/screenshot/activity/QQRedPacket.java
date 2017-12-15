package com.zhushou.weichat.screenshot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Random;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.ListDataSave;

/**
 * Created by zhanglinkai on 2017/4/14.
 * 功能:
 */

public class QQRedPacket extends AppCompatActivity implements View.OnClickListener,OnDateSetListener {
    private ImageView qq_redpacket_title_iv;
    private RelativeLayout qq_redpacket_from_layout;
    private ImageView qq_redpacket_from_iv;
    private TextView qq_redpacket_from_tv;
    private RelativeLayout qq_redpacket_to_layout;
    private ImageView qq_redpacket_to_iv;
    private TextView qq_redpacket_to_tv;
    private EditText qq_redpacket_money;
    private EditText qq_redpacket_des;
    private RelativeLayout qq_redpacket_time_layout;
    private TextView qq_redpacket_time_tv;
    private ListDataSave save;
    private Button qq_redpacket_btn_preview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_qq_redpacket);
        iniUI();
    }

    private void iniUI() {
        qq_redpacket_title_iv=(ImageView)findViewById(R.id.qq_redpacket_title_iv);
        qq_redpacket_from_layout=(RelativeLayout)findViewById(R.id.qq_redpacket_from_layout);
        qq_redpacket_from_iv=(ImageView)findViewById(R.id.qq_redpacket_from_iv);
        qq_redpacket_from_tv=(TextView)findViewById(R.id.qq_redpacket_from_tv);
        qq_redpacket_to_layout=(RelativeLayout)findViewById(R.id.qq_redpacket_to_layout);
        qq_redpacket_to_iv=(ImageView)findViewById(R.id.qq_redpacket_to_iv);
        qq_redpacket_to_tv=(TextView)findViewById(R.id.qq_redpacket_to_tv);
        qq_redpacket_money=(EditText)findViewById(R.id.qq_redpacket_money);
        qq_redpacket_des=(EditText)findViewById(R.id.qq_redpacket_des);
        qq_redpacket_time_layout=(RelativeLayout)findViewById(R.id.qq_redpacket_time_layout);
        qq_redpacket_time_tv=(TextView)findViewById(R.id.qq_redpacket_time_tv);
        qq_redpacket_btn_preview=(Button)findViewById(R.id.qq_redpacket_btn_preview);

        qq_redpacket_title_iv.setOnClickListener(this);
        qq_redpacket_from_layout.setOnClickListener(this);
        qq_redpacket_to_layout.setOnClickListener(this);
        qq_redpacket_time_layout.setOnClickListener(this);
        qq_redpacket_btn_preview.setOnClickListener(this);

        save=new ListDataSave(this,"tt");
        selectTime();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qq_redpacket_title_iv:
                finish();
                break;
            case R.id.qq_redpacket_from_layout:
                type="from";
                intent();
                break;
            case R.id.qq_redpacket_to_layout:
                type="to";
                intent();
                break;
            case R.id.qq_redpacket_time_layout:
                mDialogAll.show(getSupportFragmentManager(),"tt");
                break;
            case R.id.qq_redpacket_btn_preview:

                String money=qq_redpacket_money.getText().toString().trim();
                String des=qq_redpacket_des.getText().toString().trim();
                String time=qq_redpacket_time_tv.getText().toString().trim();

                if (money.isEmpty()||time.isEmpty()||fromImage.isEmpty()||toImage.isEmpty()){
                    Toast.makeText(this, getString(R.string.qqbalance_toast), Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(this,QQRedPacketPreView.class);

                    intent.putExtra("fromname",qq_redpacket_from_tv.getText().toString().trim());
                    intent.putExtra("froimage",fromImage);
                    intent.putExtra("toname",qq_redpacket_to_tv.getText().toString().trim());
                    intent.putExtra("toimage",toImage);
                    intent.putExtra("money",money);
                    intent.putExtra("des",des);
                    intent.putExtra("time",time);

                    startActivity(intent);
                }
                break;
        }
    }
    private String type="";
    private void intent(){
        Intent intent=new Intent(this,RoleActivity.class);
        intent.putExtra("tag","call");
        startActivityForResult(intent,1);
    }
    String fromImage="";
    String toImage="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==2){
                List<Map<String,String>> list=save.getDataList("tt");
                int p=data.getIntExtra("position",10000);
                if (p!=10000){
                    if (type.equals("from")){
                        String name=list.get(p).get("name");
                        fromImage=list.get(p).get("image");
                        Picasso.with(this).load(new File(fromImage)).resize(100,100).into(qq_redpacket_from_iv);
                        qq_redpacket_from_tv.setText(name);
                    }else if (type.equals("to")){
                        String name=list.get(p).get("name");
                        toImage=list.get(p).get("image");
                        Picasso.with(this).load(new File(toImage)).resize(100,100).into(qq_redpacket_to_iv);
                        qq_redpacket_to_tv.setText(name);
                    }
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
                .setTitleStringId(getString(R.string.selecttime))
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .build();
    }
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = getDateToString(millseconds);
        Random random=new Random();
        int s=random.nextInt(60);
        qq_redpacket_time_tv.setText(text+":"+String.format("%02d",s));
    }
    SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }
}
