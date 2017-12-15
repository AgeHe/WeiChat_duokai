package com.zhushou.weichat.screenshot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2017/4/1.
 * 功能:
 */

public class WxMoneyDetailsAdd extends AppCompatActivity implements View.OnClickListener,OnDateSetListener{
    private ImageView money_details_add_title_iv;
    private TextView money_details_add_sure_btn;
    private TextView money_details_add_disburse;
    private TextView money_details_add_income;
    private RelativeLayout money_details_add_time_layout;
    private TextView money_details_add_time_tv;
    private EditText money_details_add_des_et;
    private ImageView money_details_add_random;
    private EditText money_details_add_money_et;
    private String incomeOrdisburse="income";
    private String[] des;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_money_details_add);
        iniUI();
    }

    private void iniUI() {
        des=getResources().getStringArray(R.array.moneydetails);
        money_details_add_title_iv=(ImageView)findViewById(R.id.money_details_add_title_iv);
        money_details_add_sure_btn=(TextView)findViewById(R.id.money_details_add_sure_btn);
        money_details_add_disburse=(TextView)findViewById(R.id.money_details_add_disburse);
        money_details_add_income=(TextView)findViewById(R.id.money_details_add_income);
        money_details_add_time_layout=(RelativeLayout)findViewById(R.id.money_details_add_time_layout);
        money_details_add_time_tv=(TextView)findViewById(R.id.money_details_add_time_tv);
        money_details_add_des_et=(EditText)findViewById(R.id.money_details_add_des_et);
        money_details_add_random=(ImageView)findViewById(R.id.money_details_add_random);
        money_details_add_money_et=(EditText)findViewById(R.id.money_details_add_money_et);

        money_details_add_title_iv.setOnClickListener(this);
        money_details_add_sure_btn.setOnClickListener(this);
        money_details_add_disburse.setOnClickListener(this);
        money_details_add_income.setOnClickListener(this);
        money_details_add_time_layout.setOnClickListener(this);
        money_details_add_random.setOnClickListener(this);

        Random random=new Random();
        int number=random.nextInt(des.length);
        money_details_add_des_et.setText(des[number]);
        selectTime();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.money_details_add_title_iv:
                finish();
                break;
            case R.id.money_details_add_sure_btn:
                String time=money_details_add_time_tv.getText().toString().trim();
                String description=money_details_add_des_et.getText().toString().trim();
                String money=money_details_add_money_et.getText().toString().trim();
                Intent intent=new Intent();
                if (time.isEmpty()||description.isEmpty()||money.isEmpty()){
                    Toast.makeText(this, getString(R.string.qqbalance_toast), Toast.LENGTH_SHORT).show();
                }else {
                    intent.putExtra("type", incomeOrdisburse);
                    intent.putExtra("description", description);
                    intent.putExtra("money", money);
                    intent.putExtra("time", time);
                    setResult(2, intent);
                    finish();
                }
                break;
            case R.id.money_details_add_disburse:
                incomeOrdisburse="disburse";
                money_details_add_disburse.setBackgroundResource(R.drawable.blue_shape);
                money_details_add_disburse.setTextColor(this.getResources().getColor(R.color.color_white));
                money_details_add_income.setBackgroundResource(R.drawable.white_shape);
                money_details_add_income.setTextColor(getResources().getColor(R.color.wxblue));
                break;
            case R.id.money_details_add_income:
                incomeOrdisburse="income";
                money_details_add_income.setBackgroundResource(R.drawable.blue_shape);
                money_details_add_income.setTextColor(this.getResources().getColor(R.color.color_white));
                money_details_add_disburse.setBackgroundResource(R.drawable.white_shape);
                money_details_add_disburse.setTextColor(getResources().getColor(R.color.wxblue));
                break;
            case R.id.money_details_add_time_layout:
                mDialogAll.show(getSupportFragmentManager(),"all");
                break;
            case R.id.money_details_add_random:
                Random random=new Random();
                int number=random.nextInt(des.length);
                money_details_add_des_et.setText(des[number]);
                break;
        }
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
                .setSureStringId(getString(R.string.queding))
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
        money_details_add_time_tv.setText(text);
    }
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }
}
