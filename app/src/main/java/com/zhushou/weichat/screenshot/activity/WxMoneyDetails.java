package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.adapter.MoneyDetailsAdapter;

/**
 * Created by zhanglinkai on 2017/4/1.
 * 功能:
 */

public class WxMoneyDetails extends Activity implements View.OnClickListener{
    private ImageView money_details_title_iv;
    private ListView monet_details_listview;
    private TextView money_details_btn_add;
    private TextView money_details_btn_preview;
    private RelativeLayout money_details_sample_layout;
    private List<Map<String,String>> list;
    private MoneyDetailsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_money_details);
        iniUI();
    }

    private void iniUI() {
        money_details_title_iv=(ImageView)findViewById(R.id.money_details_title_iv);
        monet_details_listview=(ListView)findViewById(R.id.monet_details_listview);
        money_details_btn_add=(TextView)findViewById(R.id.money_details_btn_add);
        money_details_btn_preview=(TextView)findViewById(R.id.money_details_btn_preview);
        money_details_sample_layout=(RelativeLayout)findViewById(R.id.money_details_sample_layout);


        money_details_title_iv.setOnClickListener(this);
        money_details_btn_add.setOnClickListener(this);
        money_details_btn_preview.setOnClickListener(this);

        list=new ArrayList<>();
        adapter=new MoneyDetailsAdapter(this,list,"normal");
        monet_details_listview.setAdapter(adapter);
        monet_details_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actionDialog(position).show();
            }
        });

    }
    private AlertDialog actionDialog(final int p){
        final AlertDialog dialog=new AlertDialog.Builder(this).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view= LayoutInflater.from(this).inflate(R.layout.layout_money_details_action_dialog,null);
        TextView money_details_action_dialog_tv=(TextView)view.findViewById(R.id.money_details_action_dialog_tv);
        money_details_action_dialog_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(p);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.money_details_title_iv:
                finish();
                break;
            case R.id.money_details_btn_add:
                Intent intent=new Intent(this,WxMoneyDetailsAdd.class);
                startActivityForResult(intent,1);
                break;
            case R.id.money_details_btn_preview:
                if (list.size()!=-1) {
                    Intent intent1 = new Intent(this, WxMoneyDetailsPreview.class);
                    intent1.putExtra("list", (Serializable) list);
                    startActivity(intent1);
                }else{
                    Toast.makeText(this, R.string.qingtianjiajilu, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==2){
                String type=data.getStringExtra("type");
                String time=data.getStringExtra("time");
                String description=data.getStringExtra("description");
                String money=data.getStringExtra("money");

                Map<String,String> map=new HashMap<>();
                map.put("type",type);
                map.put("time",time);
                map.put("description",description);
                map.put("money",money);
                list.add(map);
                adapter.notifyDataSetChanged();
                money_details_sample_layout.setVisibility(View.GONE);
            }
        }
    }
}
