package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.adapter.MoneyDetailsAdapter;

/**
 * Created by zhanglinkai on 2017/4/1.
 * 功能:
 */

public class WxMoneyDetailsPreview extends Activity {
    private ImageView mongey_details_preview_title_iv;
    private ListView mongry_details_preview_listview;
    private MoneyDetailsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_money_details_preview);
        iniUI();
    }

    private void iniUI() {
        mongey_details_preview_title_iv=(ImageView)findViewById(R.id.mongey_details_preview_title_iv);
        mongey_details_preview_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mongry_details_preview_listview=(ListView)findViewById(R.id.mongry_details_preview_listview);
        List<Map<String,String>> list= (List<Map<String, String>>) getIntent().getSerializableExtra("list");
        adapter=new MoneyDetailsAdapter(this,list,"preview");
        mongry_details_preview_listview.setAdapter(adapter);
    }
}
