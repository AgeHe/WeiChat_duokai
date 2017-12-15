package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;

/**
 * Created by zhanglinkai on 2017/3/30.
 * 功能:
 */

public class WxTransferAccountPreview extends Activity {
    private ImageView transfer_account_preview_title_iv;
    private ImageView transfer_account_preview_iv;
    private TextView transfer_account_preview_context;
    private TextView transfer_account_preview_money;
    private TextView transfer_account_preview_transfer_time;
    private TextView transfer_account_preview_collect_time;
    private LinearLayout transfer_norcollect_layout;
    private LinearLayout transfer_collect_context_layout;
    private LinearLayout transfer_context_layout;
    private TextView transfer_collect_context_tv;
    private ImageView transfer_account_previewno_iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_transfer_account_preview);
        iniUI();
    }

    private void iniUI() {
        float index=getSharedPreferences("index", Context.MODE_PRIVATE).getFloat("index",1);
        transfer_account_preview_title_iv=(ImageView)findViewById(R.id.transfer_account_preview_title_iv);
        transfer_account_preview_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        transfer_account_preview_iv=(ImageView)findViewById(R.id.transfer_account_preview_iv);
        transfer_account_preview_context=(TextView)findViewById(R.id.transfer_account_preview_context);
        transfer_account_preview_money=(TextView)findViewById(R.id.transfer_account_preview_money);
        transfer_account_preview_transfer_time=(TextView)findViewById(R.id.transfer_account_preview_transfer_time);
        transfer_account_preview_collect_time=(TextView)findViewById(R.id.transfer_account_preview_collect_time);
        transfer_norcollect_layout=(LinearLayout)findViewById(R.id.transfer_norcollect_layout);
        transfer_collect_context_layout=(LinearLayout)findViewById(R.id.transfer_collect_context_layout);
        transfer_context_layout=(LinearLayout)findViewById(R.id.transfer_context_layout);
        transfer_collect_context_tv=(TextView)findViewById(R.id.transfer_collect_context_tv);
        transfer_account_previewno_iv=(ImageView)findViewById(R.id.transfer_account_previewno_iv);


        //StaticMethod.adaptiveView(transfer_account_preview_iv,270,270,index);


        String type=getIntent().getStringExtra("type");
        String money=getIntent().getStringExtra("money");
        boolean isCollectMoney=getIntent().getBooleanExtra("isCollectMoney",true);
        String transfertime=getIntent().getStringExtra("transfertime");
        String collecttime=getIntent().getStringExtra("collecttime");

        if (type.equals("transfer")){
            String name=getIntent().getStringExtra("name");
            if (isCollectMoney){
                //收钱
                StaticMethod.adaptiveView(transfer_account_preview_iv,280,280,index);
                transfer_norcollect_layout.setVisibility(View.GONE);
                transfer_collect_context_layout.setVisibility(View.GONE);
                transfer_account_preview_collect_time.setVisibility(View.VISIBLE);
                //transfer_account_previewno_iv.setVisibility(View.GONE);
                transfer_account_preview_iv.setImageResource(R.mipmap.wechat_transfer_success);
                transfer_context_layout.setVisibility(View.GONE);
                transfer_collect_context_tv.setVisibility(View.VISIBLE);
                transfer_account_preview_context.setText(name+R.string.yishouqian);
            }else{
                //未收钱
                StaticMethod.adaptiveView(transfer_account_preview_iv,240,240,index);
                transfer_norcollect_layout.setVisibility(View.GONE);
                transfer_collect_context_layout.setVisibility(View.GONE);
                transfer_account_preview_collect_time.setVisibility(View.GONE);
                //transfer_account_preview_iv.setVisibility(View.GONE);
                transfer_account_preview_iv.setImageResource(R.mipmap.wechat_transfer_receiving);
                transfer_context_layout.setVisibility(View.VISIBLE);
                transfer_collect_context_tv.setVisibility(View.GONE);
                transfer_account_preview_context.setText(getString(R.string.dai)+name+getString(R.string.querenshouqian));
            }
        }else if (type.equals("collect")){

            if (isCollectMoney){
                //收钱
                StaticMethod.adaptiveView(transfer_account_preview_iv,280,280,index);
                transfer_norcollect_layout.setVisibility(View.GONE);
                transfer_collect_context_layout.setVisibility(View.VISIBLE);
                transfer_account_preview_collect_time.setVisibility(View.VISIBLE);
                transfer_collect_context_tv.setVisibility(View.GONE);
                //transfer_account_previewno_iv.setVisibility(View.GONE);
                transfer_account_preview_iv.setImageResource(R.mipmap.wechat_transfer_success);
                transfer_account_preview_context.setText(R.string.yishouqian);
                transfer_context_layout.setVisibility(View.GONE);
            }else{
                //未收钱
                StaticMethod.adaptiveView(transfer_account_preview_iv,240,240,index);
                transfer_norcollect_layout.setVisibility(View.VISIBLE);
                transfer_collect_context_layout.setVisibility(View.GONE);
                transfer_account_preview_collect_time.setVisibility(View.GONE);
                transfer_collect_context_tv.setVisibility(View.GONE);
                //transfer_account_preview_iv.setVisibility(View.GONE);
                transfer_account_preview_iv.setImageResource(R.mipmap.wechat_transfer_receiving);
                transfer_account_preview_context.setText(R.string.daishouqian);
            }
        }
        transfer_account_preview_money.setText(getResources().getString(R.string.renminbi)+StaticMethod.keepTwoDecimalNo(money));

        transfer_account_preview_transfer_time.setText(getString(R.string.zhuanzhangshijian)+transfertime);
        transfer_account_preview_collect_time.setText(getString(R.string.shouqianshijian)+collecttime);




    }
}
