package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.ListDataSave;

/**
 * Created by zhanglinkai on 2017/3/31.
 * 功能:
 */

public class WxMyNewFriendAdd extends Activity implements View.OnClickListener{
    private ImageView mynewfriendadd_title_iv;
    private RelativeLayout newfriendadd_role_layout;
    private TextView newfriend_add_name_tv;
    private ImageView newfriend_add_icon_iv;
    private EditText newfriend_add_des;
    private RelativeLayout newfriend_add_tag_layout;
    private Button newfriend_add_save_btn;
    private ImageView newfriend_add_tag_iv;
    private boolean isSelect=false;
    private ListDataSave save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wxmynewfriendadd);
        save=new ListDataSave(this,"tt");
        iniUI();
    }

    private void iniUI() {
        mynewfriendadd_title_iv=(ImageView)findViewById(R.id.mynewfriendadd_title_iv);
        newfriendadd_role_layout=(RelativeLayout)findViewById(R.id.newfriendadd_role_layout);
        newfriend_add_name_tv=(TextView)findViewById(R.id.newfriend_add_name_tv);
        newfriend_add_icon_iv=(ImageView)findViewById(R.id.newfriend_add_icon_iv);
        newfriend_add_des=(EditText)findViewById(R.id.newfriend_add_des);
        newfriend_add_tag_layout=(RelativeLayout)findViewById(R.id.newfriend_add_tag_layout);
        newfriend_add_save_btn=(Button)findViewById(R.id.newfriend_add_save_btn);
        newfriend_add_tag_iv=(ImageView)findViewById(R.id.newfriend_add_tag_iv);

        newfriendadd_role_layout.setOnClickListener(this);
        newfriend_add_tag_layout.setOnClickListener(this);
        newfriend_add_save_btn.setOnClickListener(this);
        mynewfriendadd_title_iv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mynewfriendadd_title_iv:
                finish();
                break;
            case R.id.newfriendadd_role_layout:
                //角色
                Intent intent=new Intent(this,RoleActivity.class);
                intent.putExtra("tag","newfriend");
                startActivityForResult(intent,1);
                break;
            case R.id.newfriend_add_tag_layout:
                //标志
                if (isSelect==false){
                    isSelect=true;
                    newfriend_add_tag_iv.setImageResource(R.mipmap.webviewtab_back_disable);
                }else{
                    isSelect=false;
                    newfriend_add_tag_iv.setImageResource(R.mipmap.webviewtab_back_normal);
                }
                break;
            case R.id.newfriend_add_save_btn:
                //保存
                Intent intent1=new Intent();
                intent1.putExtra("name",newfriend_add_name_tv.getText().toString().trim());
                intent1.putExtra("image",image);
                intent1.putExtra("des",newfriend_add_des.getText().toString().trim());
                intent1.putExtra("isselect",isSelect);
                setResult(2,intent1);
                finish();
                break;
        }
    }
    String image="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==2){
                int p=data.getIntExtra("position",10000);
                if (p!=10000){
                    List<Map<String,String>> list=save.getDataList("tt");
                    String name=list.get(p).get("name");
                    image=list.get(p).get("image");
                    newfriend_add_name_tv.setText(name);
                    newfriend_add_icon_iv.setImageBitmap(BitmapFactory.decodeFile(image));
                    newfriend_add_des.setText(getString(R.string.woshi)+name);
                }
            }
        }
    }
}
