package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.adapter.WxNewFriendAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanglinkai on 2017/3/31.
 * 功能:
 */

public class MyNewFriend extends Activity implements View.OnClickListener,WxNewFriendAdapter.itemClear{
    private ImageView mynewfriend_title_iv;
    private TextView mynewfriend_btn_add;
    private TextView mynewfriend_btn_preview;
    private ListView mynewfriend_list;
    private LinearLayout mynewfriend_sample_layout;
    private WxNewFriendAdapter adapter;
    private List<Map<String,Object>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mynewfriend);
        iniUI();

    }

    private void iniUI() {
        list=new ArrayList<>();
        mynewfriend_title_iv=(ImageView)findViewById(R.id.mynewfriend_title_iv);
        mynewfriend_btn_add=(TextView)findViewById(R.id.mynewfriend_btn_add);
        mynewfriend_btn_preview=(TextView)findViewById(R.id.mynewfriend_btn_preview);
        mynewfriend_list=(ListView)findViewById(R.id.mynewfriend_list);
        mynewfriend_sample_layout=(LinearLayout)findViewById(R.id.mynewfriend_sample_layout);

        mynewfriend_title_iv.setOnClickListener(this);
        mynewfriend_btn_add.setOnClickListener(this);
        mynewfriend_btn_preview.setOnClickListener(this);

        adapter=new WxNewFriendAdapter(this,list,this);
        mynewfriend_list.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mynewfriend_title_iv:
                finish();
                break;
            case R.id.mynewfriend_btn_add:
                Intent addIntent=new Intent(this,WxMyNewFriendAdd.class);
                startActivityForResult(addIntent,1);
                break;
            case R.id.mynewfriend_btn_preview:
                if (list.size()!=0) {
                    Intent intent = new Intent(this, MyNewFriendPreview.class);

                    intent.putExtra("list", (Serializable) list);

                    startActivity(intent);
                }else{
                    Toast.makeText(this, R.string.newfriend_toast, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==2){
                mynewfriend_sample_layout.setVisibility(View.GONE);
                Map<String,Object> map=new HashMap<>();
                String name=data.getStringExtra("name");
                String image=data.getStringExtra("image");
                String des=data.getStringExtra("des");
                boolean isFriend=data.getBooleanExtra("isselect",false);
                map.put("name",name);
                map.put("image",image);
                map.put("des",des);
                map.put("isfriend",isFriend);
                list.add(map);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void itemClick(int p) {
        list.remove(p);
        adapter.notifyDataSetChanged();
    }
}
