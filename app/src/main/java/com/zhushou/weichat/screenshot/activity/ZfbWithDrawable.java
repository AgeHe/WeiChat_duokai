package com.zhushou.weichat.screenshot.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2017/4/13.
 * 功能:
 */

public class ZfbWithDrawable extends AppCompatActivity implements View.OnClickListener,OnDateSetListener {
    private ImageView zfb_withdrawable_title_iv;
    private RelativeLayout zfb_withdrawable_selectbank_layout;
    private TextView zfb_withdrawable_selectbank_tv;
    private EditText zfb_withdrawable_bankcard_et;
    private EditText zfb_withdrawable_name_et;
    private EditText zfb_withdrawable_money_et;
    private RelativeLayout zfb_withdrawable_tag_layout;
    private ImageView zfb_withdrawable_tag_iv;
    private RelativeLayout zfb_withdrawable_transfermoney_time_layout;
    private TextView zfb_withdrawable_transfermoney_time_tv;
    private RelativeLayout zfb_withdrawable_collectmoney_time_layout;
    private TextView zfb_withdrawable_collectmoney_time_tv;
    private Button zfb_withdrawable_preview_btn;
    private boolean isSelect=false;
    private String selectTime="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_zfb_withdrawable);
        iniUI();
    }

    private void iniUI() {
        zfb_withdrawable_title_iv=(ImageView)findViewById(R.id.zfb_withdrawable_title_iv);
        zfb_withdrawable_selectbank_layout=(RelativeLayout)findViewById(R.id.zfb_withdrawable_selectbank_layout);
        zfb_withdrawable_selectbank_tv=(TextView)findViewById(R.id.zfb_withdrawable_selectbank_tv);
        zfb_withdrawable_bankcard_et=(EditText)findViewById(R.id.zfb_withdrawable_bankcard_et);
        zfb_withdrawable_name_et=(EditText)findViewById(R.id.zfb_withdrawable_name_et);
        zfb_withdrawable_money_et=(EditText)findViewById(R.id.zfb_withdrawable_money_et);
        zfb_withdrawable_tag_layout=(RelativeLayout)findViewById(R.id.zfb_withdrawable_tag_layout);
        zfb_withdrawable_tag_iv=(ImageView)findViewById(R.id.zfb_withdrawable_tag_iv);
        zfb_withdrawable_transfermoney_time_layout=(RelativeLayout)findViewById(R.id.zfb_withdrawable_transfermoney_time_layout);
        zfb_withdrawable_transfermoney_time_tv=(TextView)findViewById(R.id.zfb_withdrawable_transfermoney_time_tv);
        zfb_withdrawable_collectmoney_time_layout=(RelativeLayout)findViewById(R.id.zfb_withdrawable_collectmoney_time_layout);
        zfb_withdrawable_collectmoney_time_tv=(TextView)findViewById(R.id.zfb_withdrawable_collectmoney_time_tv);
        zfb_withdrawable_preview_btn=(Button)findViewById(R.id.zfb_withdrawable_preview_btn);

        zfb_withdrawable_title_iv.setOnClickListener(this);
        zfb_withdrawable_selectbank_layout.setOnClickListener(this);
        zfb_withdrawable_tag_layout.setOnClickListener(this);
        zfb_withdrawable_transfermoney_time_layout.setOnClickListener(this);
        zfb_withdrawable_collectmoney_time_layout.setOnClickListener(this);
        zfb_withdrawable_preview_btn.setOnClickListener(this);

        selectTime();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zfb_withdrawable_title_iv:
                finish();
                break;
            case R.id.zfb_withdrawable_selectbank_layout:
                selectBank();
                break;
            case R.id.zfb_withdrawable_tag_layout:

                if (isSelect){
                    isSelect=false;
                    zfb_withdrawable_tag_iv.setImageResource(R.mipmap.webviewtab_back_normal);
                }else{
                    isSelect=true;
                    zfb_withdrawable_tag_iv.setImageResource(R.mipmap.webviewtab_back_disable);
                }
                break;
            case R.id.zfb_withdrawable_transfermoney_time_layout:
                selectTime="transfer";
                mDialogAll.show(getSupportFragmentManager(),"all");
                break;
            case R.id.zfb_withdrawable_collectmoney_time_layout:
                selectTime="collect";
                mDialogAll.show(getSupportFragmentManager(),"all");
                break;
            case R.id.zfb_withdrawable_preview_btn:

                String bank=zfb_withdrawable_selectbank_tv.getText().toString().trim();
                String card=zfb_withdrawable_bankcard_et.getText().toString().trim();
                String name=zfb_withdrawable_name_et.getText().toString().trim();
                String money=zfb_withdrawable_money_et.getText().toString().trim();
                String ttime=zfb_withdrawable_transfermoney_time_tv.getText().toString().trim();
                String ctime=zfb_withdrawable_collectmoney_time_tv.getText().toString().trim();
                if (!bank.isEmpty()&&!card.isEmpty()&&!name.isEmpty()&&!money.isEmpty()&&!ttime.isEmpty()&&!ctime.isEmpty()){

                    Intent intent=new Intent(this,ZfbWithDrawablePreview.class);
                    intent.putExtra("bank",bank);
                    intent.putExtra("card",card);
                    intent.putExtra("name",name);
                    intent.putExtra("money",money);
                    intent.putExtra("ttime",ttime);
                    intent.putExtra("ctime",ctime);
                    intent.putExtra("isSelect",isSelect);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "请完善信息", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    private void selectBank(){
        final String[] s=getResources().getStringArray(R.array.bank);
        String s1=zfb_withdrawable_selectbank_tv.getText().toString().trim();
        int m=0;
        if (!s1.isEmpty()){
            for (int i=0;i<s.length;i++){
                if (s1.equals(s[i])){
                    m=i;
                    break;
                }
            }
        }
        final AlertDialog dialog=new AlertDialog.Builder(this).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        View view= LayoutInflater.from(this).inflate(R.layout.layout_select_bank,null);
        dialog.setView(view);
        TextView selctbank_dialog_cancle=(TextView)view.findViewById(R.id.selctbank_dialog_cancle);
        TextView selctbank_dialog_sure=(TextView)view.findViewById(R.id.selctbank_dialog_sure);
        selctbank_dialog_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final NumberPicker picker_bank=(NumberPicker)view.findViewById(R.id.picker_bank);
        picker_bank.setDisplayedValues(s);
        picker_bank.setMinValue(0);
        picker_bank.setValue(m);
        picker_bank.setMaxValue(s.length-1);
        selctbank_dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zfb_withdrawable_selectbank_tv.setText(s[picker_bank.getValue()]);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    /**
     *功能:时间选择
     *参数:
     */
    TimePickerDialog mDialogAll;
    private void selectTime(){
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId(getString(R.string.quxiao))
                .setSureStringId(getString(R.string.queren))
                .setTitleStringId(getString(R.string.shijianxuanzeqi))
                .setYearText(getString(R.string.nian))
                .setMonthText(getString(R.string.nyue))
                .setDayText(getString(R.string.ri))
                .setHourText(getString(R.string.shi))
                .setMinuteText(getString(R.string.fen))
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis()-tenYears)
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build();
    }
    long t=0;
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        t=millseconds;
        String text = getDateToString(millseconds);
        if (selectTime.equals("transfer")) {
            zfb_withdrawable_transfermoney_time_tv.setText(text);
        }else if(selectTime.equals("collect")) {
            zfb_withdrawable_collectmoney_time_tv.setText(text);
        }
    }
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }
}
