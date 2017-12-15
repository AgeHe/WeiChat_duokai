package com.zhushou.weichat.screenshot.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2017/3/29.
 * 功能:
 */

public class TransferAccounts extends AppCompatActivity implements View.OnClickListener,OnDateSetListener{
    private ImageView transfer_account_title_iv;
    private TextView transfer_tranfs_money_tv;
    private TextView transfer_collect_money_tv;
    private EditText transfer_money_et;
    private EditText transfer_name_et;
    private RelativeLayout transfer_status_layout;
    private TextView transfer_status_tv;
    private RelativeLayout transfer_time_layout;
    private TextView transfer_time_tv;
    private RelativeLayout transfer_collect_time_layout;
    private TextView transfer_collect_time_tv;
    private Button transfer_preview_btn;
    private Resources resource;
    private boolean isSelectStatus=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_transfer_account);
        resource = (Resources) getBaseContext().getResources();
        white = (ColorStateList) resource.getColorStateList(R.color.color_white);
        blue = (ColorStateList) resource.getColorStateList(R.color.blue);
        iniUI();
    }

    private void iniUI() {
        transfer_account_title_iv=(ImageView)findViewById(R.id.transfer_account_title_iv);
        transfer_tranfs_money_tv=(TextView)findViewById(R.id.transfer_tranfs_money_tv);
        transfer_collect_money_tv=(TextView)findViewById(R.id.transfer_collect_money_tv);
        transfer_money_et=(EditText)findViewById(R.id.transfer_money_et);
        transfer_name_et=(EditText)findViewById(R.id.transfer_name_et);
        transfer_status_layout=(RelativeLayout)findViewById(R.id.transfer_status_layout);
        transfer_status_tv=(TextView)findViewById(R.id.transfer_status_tv);
        transfer_time_layout=(RelativeLayout)findViewById(R.id.transfer_time_layout);
        transfer_time_tv=(TextView)findViewById(R.id.transfer_time_tv);
        transfer_collect_time_layout=(RelativeLayout)findViewById(R.id.transfer_collect_time_layout);
        transfer_collect_time_tv=(TextView)findViewById(R.id.transfer_collect_time_tv);
        transfer_preview_btn=(Button)findViewById(R.id.transfer_preview_btn);


        selectTime();//时间dialog


        transfer_account_title_iv.setOnClickListener(this);
        transfer_tranfs_money_tv.setOnClickListener(this);
        transfer_collect_money_tv.setOnClickListener(this);
        transfer_status_layout.setOnClickListener(this);
        transfer_time_layout.setOnClickListener(this);
        transfer_collect_time_layout.setOnClickListener(this);
        transfer_preview_btn.setOnClickListener(this);


    }
    ColorStateList white;
    ColorStateList blue;
    private String timeTag="";
    private String typeTag="collect";
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.transfer_account_title_iv:
                finish();
                break;
            case R.id.transfer_tranfs_money_tv:
                typeTag="transfer";
                transfer_collect_money_tv.setTextColor(blue);
                transfer_tranfs_money_tv.setTextColor(white);
                transfer_collect_money_tv.setBackgroundResource(R.drawable.white_shape);
                transfer_tranfs_money_tv.setBackgroundResource(R.drawable.blue_shape);
                transfer_name_et.setVisibility(View.VISIBLE);
                break;
            case R.id.transfer_collect_money_tv:
                typeTag="collect";
                transfer_tranfs_money_tv.setTextColor(blue);
                transfer_collect_money_tv.setTextColor(white);
                transfer_tranfs_money_tv.setBackgroundResource(R.drawable.white_shape);
                transfer_collect_money_tv.setBackgroundResource(R.drawable.blue_shape);
                transfer_name_et.setVisibility(View.GONE);
                break;

            case R.id.transfer_status_layout:
                if (isSelectStatus==true){
                    isSelectStatus=false;
                    transfer_status_tv.setText(getString(R.string.wsq));
                }else{
                    isSelectStatus=true;
                    transfer_status_tv.setText(getString(R.string.ysq));
                }
                break;
            case R.id.transfer_time_layout:
                timeTag="zzsj";
                mDialogAll.show(getSupportFragmentManager(),"all");
                break;
            case R.id.transfer_collect_time_layout:
                timeTag="sqsj";
                mDialogAll.show(getSupportFragmentManager(),"all");
                break;
            case R.id.transfer_preview_btn:
                Intent intent=new Intent(this,WxTransferAccountPreview.class);
                String money=transfer_money_et.getText().toString().trim();
                String transfertime=transfer_time_tv.getText().toString().trim();
                String collecttime=transfer_collect_time_tv.getText().toString().trim();
                boolean isCollectMoney=isSelectStatus;

                intent.putExtra("money",money);
                intent.putExtra("transfertime",transfertime);
                intent.putExtra("collecttime",collecttime);
                intent.putExtra("isCollectMoney",isCollectMoney);
                intent.putExtra("type",typeTag);
                if (typeTag.equals("transfer")){
                    //转账
                    intent.putExtra("name",transfer_name_et.getText().toString().trim());
                }
                if (!money.isEmpty()&&!transfertime.isEmpty()&&!collecttime.isEmpty()) {
                    startActivity(intent);
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
        if (timeTag.equals("zzsj")){
            transfer_time_tv.setText(text);
        }else if (timeTag.equals("sqsj")){
            transfer_collect_time_tv.setText(text);
        }
    }
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }
}
