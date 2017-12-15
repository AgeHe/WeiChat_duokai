package com.zhushou.weichat.screenshot.activity;

import android.content.Intent;
import android.os.Bundle;
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
 * Created by zhanglinkai on 2017/4/12.
 * 功能:
 */

public class ZfbTransferAccount extends AppCompatActivity implements View.OnClickListener,OnDateSetListener {
    private ImageView zfb_transferaccount_title_iv;
    private RelativeLayout zfb_transferaccount_role_layout;
    private ImageView zfb_transferaccount_role_icon;
    private TextView zfb_transferaccount_role_tv;
    private TextView zfb_transferaccount_type_to_btn;
    private TextView zfb_transferaccount_type_from_btn;
    private EditText zfb_transferaccount_account_tv;
    private ImageView zfb_transferaccount_account_random_iv;
    private EditText zfb_transferaccount_account_money_et;
    private EditText zfb_transferaccount_account_des_et;
    private RelativeLayout zfb_transferaccount_account_level_layout;
    private TextView zfb_transferaccount_account_level_tv;
    private RelativeLayout zfb_transferaccount_payment_layout;
    private TextView zfb_transferaccount_payment_tv;
    private RelativeLayout zfb_transferaccount_time_layout;
    private TextView zfb_transferaccount_time_tv;
    private RelativeLayout zfb_transferaccount_status_layout;
    private TextView zfb_transferaccount_status_tv;
    private Button zfb_transferaccount_preview_btn;
    private String type="from";
    private String[] accounts;
    private String[] levels;
    private int levelNumber=0;
    private String[] payMents;
    private int paymentnumber=0;
    private String[] status;
    private int statusNumber=0;
    private ListDataSave save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_zfb_transferaccount);
        iniUI();
    }

    private void iniUI() {
        accounts=getResources().getStringArray(R.array.transferaccount);
        levels=getResources().getStringArray(R.array.transferlevel);
        payMents=getResources().getStringArray(R.array.transfertype);
        status=getResources().getStringArray(R.array.transferstatus);
        zfb_transferaccount_title_iv=(ImageView)findViewById(R.id.zfb_transferaccount_title_iv);
        zfb_transferaccount_role_layout=(RelativeLayout)findViewById(R.id.zfb_transferaccount_role_layout);
        zfb_transferaccount_role_icon=(ImageView)findViewById(R.id.zfb_transferaccount_role_icon);
        zfb_transferaccount_role_tv=(TextView)findViewById(R.id.zfb_transferaccount_role_tv);
        zfb_transferaccount_type_to_btn=(TextView)findViewById(R.id.zfb_transferaccount_type_to_btn);
        zfb_transferaccount_type_from_btn=(TextView)findViewById(R.id.zfb_transferaccount_type_from_btn);
        zfb_transferaccount_account_tv=(EditText)findViewById(R.id.zfb_transferaccount_account_tv);
        zfb_transferaccount_account_random_iv=(ImageView)findViewById(R.id.zfb_transferaccount_account_random_iv);
        zfb_transferaccount_account_money_et=(EditText)findViewById(R.id.zfb_transferaccount_account_money_et);
        zfb_transferaccount_account_des_et=(EditText)findViewById(R.id.zfb_transferaccount_account_des_et);
        zfb_transferaccount_account_level_layout=(RelativeLayout)findViewById(R.id.zfb_transferaccount_account_level_layout);
        zfb_transferaccount_account_level_tv=(TextView)findViewById(R.id.zfb_transferaccount_account_level_tv);
        zfb_transferaccount_payment_layout=(RelativeLayout)findViewById(R.id.zfb_transferaccount_payment_layout);
        zfb_transferaccount_payment_tv=(TextView)findViewById(R.id.zfb_transferaccount_payment_tv);
        zfb_transferaccount_time_layout=(RelativeLayout)findViewById(R.id.zfb_transferaccount_time_layout);
        zfb_transferaccount_time_tv=(TextView)findViewById(R.id.zfb_transferaccount_time_tv);
        zfb_transferaccount_status_layout=(RelativeLayout)findViewById(R.id.zfb_transferaccount_status_layout);
        zfb_transferaccount_status_tv=(TextView)findViewById(R.id.zfb_transferaccount_status_tv);
        zfb_transferaccount_preview_btn=(Button)findViewById(R.id.zfb_transferaccount_preview_btn);

        zfb_transferaccount_title_iv.setOnClickListener(this);
        zfb_transferaccount_role_layout.setOnClickListener(this);
        zfb_transferaccount_type_to_btn.setOnClickListener(this);
        zfb_transferaccount_type_from_btn.setOnClickListener(this);
        zfb_transferaccount_account_random_iv.setOnClickListener(this);
        zfb_transferaccount_account_level_layout.setOnClickListener(this);
        zfb_transferaccount_payment_layout.setOnClickListener(this);
        zfb_transferaccount_time_layout.setOnClickListener(this);
        zfb_transferaccount_status_layout.setOnClickListener(this);
        zfb_transferaccount_preview_btn.setOnClickListener(this);
        selectTime();
        save=new ListDataSave(this,"tt");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zfb_transferaccount_title_iv:
                finish();
                break;

            case R.id.zfb_transferaccount_role_layout://选角色

                Intent roleIntent=new Intent(this,RoleActivity.class);
                roleIntent.putExtra("tag","call");
                startActivityForResult(roleIntent,1);

                break;
            case R.id.zfb_transferaccount_type_to_btn://转出
                type="to";
                zfb_transferaccount_type_to_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_shape));
                zfb_transferaccount_type_to_btn.setTextColor(getResources().getColor(R.color.color_white));
                zfb_transferaccount_type_from_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_shape));
                zfb_transferaccount_type_from_btn.setTextColor(getResources().getColor(R.color.wxblue));
                break;
            case R.id.zfb_transferaccount_type_from_btn://转入
                type="from";
                zfb_transferaccount_type_from_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_shape));
                zfb_transferaccount_type_from_btn.setTextColor(getResources().getColor(R.color.color_white));
                zfb_transferaccount_type_to_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_shape));
                zfb_transferaccount_type_to_btn.setTextColor(getResources().getColor(R.color.wxblue));
                break;
            case R.id.zfb_transferaccount_account_random_iv://随机账号

                Random r=new Random();
                int random=r.nextInt(accounts.length);
                zfb_transferaccount_account_tv.setText(accounts[random]);
                break;
            case R.id.zfb_transferaccount_account_level_layout://对方等级
                if (levelNumber<levels.length){
                    zfb_transferaccount_account_level_tv.setText(levels[levelNumber]);
                    levelNumber++;
                }else{
                    levelNumber=0;
                    zfb_transferaccount_account_level_tv.setText(levels[levelNumber]);
                    levelNumber++;
                }
                break;
            case R.id.zfb_transferaccount_payment_layout://付款

                if (paymentnumber<payMents.length){
                    zfb_transferaccount_payment_tv.setText(payMents[paymentnumber]);
                    paymentnumber++;
                }else{
                    paymentnumber=0;
                    zfb_transferaccount_payment_tv.setText(payMents[paymentnumber]);
                    paymentnumber++;
                }
                break;
            case R.id.zfb_transferaccount_time_layout://时间
                mDialogAll.show(getSupportFragmentManager(),"all");
                break;
            case R.id.zfb_transferaccount_status_layout://状态

                if (statusNumber<status.length){
                    zfb_transferaccount_status_tv.setText(status[statusNumber]);
                    statusNumber++;
                }else{
                    statusNumber=0;
                    zfb_transferaccount_status_tv.setText(status[statusNumber]);
                    statusNumber++;
                }

                break;
            case R.id.zfb_transferaccount_preview_btn://预览
                String money=zfb_transferaccount_account_money_et.getText().toString().trim();
                String status=zfb_transferaccount_status_tv.getText().toString().trim();
                String des=zfb_transferaccount_account_des_et.getText().toString().trim();
                String account=zfb_transferaccount_account_tv.getText().toString().trim();
                String time=zfb_transferaccount_time_tv.getText().toString().trim();
                String payment=zfb_transferaccount_payment_tv.getText().toString().trim();
                String level=zfb_transferaccount_account_level_tv.getText().toString().trim();
                if (!image.isEmpty()&&!money.isEmpty()&&!status.isEmpty()&&!account.isEmpty()&&!time.isEmpty()) {
                    Intent intent = new Intent(this, ZfbTransferAccountPreview.class);
                    intent.putExtra("type", type);
                    intent.putExtra("image", image);
                    intent.putExtra("name", name);
                    intent.putExtra("money", money);
                    intent.putExtra("status", status);
                    intent.putExtra("des", des);
                    intent.putExtra("account", account);
                    intent.putExtra("time", time);
                    intent.putExtra("payment", payment);
                    intent.putExtra("level", level);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, R.string.qqbalance_toast, Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    String name="";
    String image="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==2){
                int p=data.getIntExtra("position",10000);
                List<Map<String,String>> list=save.getDataList("tt");
                if (p!=10000){
                    name=list.get(p).get("name");
                    image=list.get(p).get("image");

                    zfb_transferaccount_role_tv.setText(name);
                    Picasso.with(this).load(new File(image)).resize(100,100).into(zfb_transferaccount_role_icon);
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
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId(getResources().getString(R.string.quxiao))
                .setSureStringId(getResources().getString(R.string.queding))
                .setTitleStringId(getResources().getString(R.string.shijianxuanzeqi))
                .setYearText(getResources().getString(R.string.nian))
                .setMonthText(getResources().getString(R.string.nyue))
                .setDayText(getResources().getString(R.string.ri))
                .setHourText(getResources().getString(R.string.shi))
                .setMinuteText(getResources().getString(R.string.fen))
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
        zfb_transferaccount_time_tv.setText(text);
    }
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }
}
