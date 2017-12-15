package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.adapter.NewFriendPreviewAdapter;

/**
 * Created by zhanglinkai on 2017/3/31.
 * 功能:
 */

public class MyNewFriendPreview extends Activity implements View.OnClickListener{
    private ImageView mynewfriend_preview_title_iv;
    private ListView newfriend_preview_listview;
    private NewFriendPreviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_newfriend_preview);
        iniUI();
    }

    private void iniUI() {
        mynewfriend_preview_title_iv=(ImageView)findViewById(R.id.mynewfriend_preview_title_iv);
        newfriend_preview_listview=(ListView)findViewById(R.id.newfriend_preview_listview);


        List<Map<String,Object>> list= (List<Map<String, Object>>) getIntent().getSerializableExtra("list");
        adapter=new NewFriendPreviewAdapter(this,list);
        newfriend_preview_listview.setAdapter(adapter);

        mynewfriend_preview_title_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mynewfriend_preview_title_iv:
                finish();
                break;
        }
    }
}
