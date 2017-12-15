package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.adapter.WxFriendCirclePreviewItemAdapter;
import com.zhushou.weichat.screenshot.view.Newlistview;

/**
 * Created by zhanglinkai on 2017/4/10.
 * 功能:
 */

public class WxFriendCirclePreview extends Activity implements WxFriendCirclePreviewItemAdapter.itemClick{
    private ImageView wx_friendcircle_preview_title_iv;
    private ImageView wx_friendcircle_preview_bg;
    private ImageView wx_friendcircle_preview_infomation_iv;
    private TextView wx_friendcircle_preview_infomation_tv;
    private Newlistview wx_friendcircle_preview_listview;
    private ImageView wx_friendcircle_preview_icon;
    private TextView wx_friendcircle_preview_name;
    private LinearLayout wx_friendcircle_preview_infomation_layout;
    private WxFriendCirclePreviewItemAdapter adapter;
    private List<Map<String,Object>> list;
    private int p=1000;
    private int p1=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wx_friendcircle_preview);
        iniUI();
    }

    private void iniUI() {
        wx_friendcircle_preview_title_iv=(ImageView)findViewById(R.id.wx_friendcircle_preview_title_iv);
        wx_friendcircle_preview_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wx_friendcircle_preview_bg=(ImageView)findViewById(R.id.wx_friendcircle_preview_bg);
        wx_friendcircle_preview_infomation_iv=(ImageView)findViewById(R.id.wx_friendcircle_preview_infomation_iv);
        wx_friendcircle_preview_infomation_tv=(TextView)findViewById(R.id.wx_friendcircle_preview_infomation_tv);
        wx_friendcircle_preview_listview=(Newlistview)findViewById(R.id.wx_friendcircle_preview_listview);
        wx_friendcircle_preview_icon=(ImageView)findViewById(R.id.wx_friendcircle_preview_icon);
        wx_friendcircle_preview_name=(TextView)findViewById(R.id.wx_friendcircle_preview_name);
        wx_friendcircle_preview_infomation_layout=(LinearLayout)findViewById(R.id.wx_friendcircle_preview_infomation_layout);

        String bgimage=getIntent().getStringExtra("bgimage");
        String name=getIntent().getStringExtra("name");
        String roleimage=getIntent().getStringExtra("roleimage");
        String infoimage=getIntent().getStringExtra("infoimage");
        String number=getIntent().getStringExtra("number");
        list= (List<Map<String, Object>>) getIntent().getSerializableExtra("list");

        if (number.isEmpty()){
            wx_friendcircle_preview_infomation_layout.setVisibility(View.GONE);



        }else{
            wx_friendcircle_preview_infomation_layout.setVisibility(View.VISIBLE);
            Picasso.with(this).load(new File(infoimage)).resize(100,100).into(wx_friendcircle_preview_infomation_iv);
            wx_friendcircle_preview_infomation_tv.setText(number+"条新消息");
        }

        wx_friendcircle_preview_name.setText(name);
        Picasso.with(this).load(new File(bgimage)).centerCrop().fit().into(wx_friendcircle_preview_bg);
        Picasso.with(this).load(new File(roleimage)).resize(100,100).into(wx_friendcircle_preview_icon);
        adapter=new WxFriendCirclePreviewItemAdapter(this,list,this);
        wx_friendcircle_preview_listview.setAdapter(adapter);

    }

    @Override
    public void connitClick(View v,int position) {
        p=1000;
        p1=1000;
        commitDialog(v,position);

    }

    @Override
    public void commitCler(final int p1, final int p) {
        new AlertDialog.Builder(this).setTitle(R.string.caozuo).setMessage(R.string.shanchu).setNegativeButton(R.string.quxiao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton(R.string.queding, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Map<String,String>> l= (List<Map<String, String>>) list.get(p1).get("commit");
                l.remove(p);
                adapter.notifyDataSetChanged();
            }
        }).show();
    }

    PopupWindow selectPopupWindow;
    private void commitDialog(View parentView, final int position){
        final View view= LayoutInflater.from(this).inflate(R.layout.layout_wxfriendcircle_preview_commit_dialog,null);
        LinearLayout wxfriendcircle_preview_commit_zan_layout=(LinearLayout)view.findViewById(R.id.wxfriendcircle_preview_commit_zan_layout);
        LinearLayout wxfriendcircle_preview_commit_commit_layout=(LinearLayout)view.findViewById(R.id.wxfriendcircle_preview_commit_commit_layout);
        selectPopupWindow = new PopupWindow(view , WindowManager.LayoutParams. WRAP_CONTENT ,
                WindowManager.LayoutParams. WRAP_CONTENT , true) ;
        selectPopupWindow .setOutsideTouchable( true) ;
        ColorDrawable dw = new ColorDrawable(0xb0000000 ) ;
        selectPopupWindow .setBackgroundDrawable(dw) ;

        view .setOnTouchListener( new View.OnTouchListener() {

            public boolean onTouch (View v, MotionEvent event) {
                int y = ( int ) event.getY();
                int t = view.findViewById(R.id.wxfriendcircle_preview_commit_layout).getTop();
                int b = view.findViewById(R.id.wxfriendcircle_preview_commit_layout).getBottom();
                if (event.getAction() == MotionEvent. ACTION_UP ) {
                    if (y < t||y>b) {
                        selectPopupWindow.dismiss() ;
                    }
                }
                return true;
            }
        });
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int  width = -view.getMeasuredWidth();
        int  height = -view.getMeasuredHeight();

        wxfriendcircle_preview_commit_zan_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p=position;
                Intent intent=new Intent(WxFriendCirclePreview.this,WxFriendCirclePreviewZan.class);
                startActivityForResult(intent,1);
                selectPopupWindow.dismiss();
            }
        });
        wxfriendcircle_preview_commit_commit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p1=position;
                Intent intent=new Intent(WxFriendCirclePreview.this,WxFriendCirclePreviewAddCommit.class);
                startActivityForResult(intent,3);
                selectPopupWindow.dismiss();
            }
        });

        selectPopupWindow.showAsDropDown(parentView,width,height);
    }
    List<Map<String,String>> commitList;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==2){
                String zan=data.getStringExtra("zan");
                if (p!=1000){
                    list.get(p).put("zan",zan);
                    adapter.notifyDataSetChanged();
                }
            }
        }
        if (requestCode==3){
            if(resultCode==4){
                String trpe=data.getStringExtra("type");
                String from=data.getStringExtra("from");
                String to=data.getStringExtra("to");
                String con=data.getStringExtra("con");
                Map<String,String> map=new HashMap<>();
                map.put("type",trpe);
                map.put("from",from);
                map.put("to",to);
                map.put("con",con);
                if (p1!=1000){
                    commitList= (List<Map<String, String>>) list.get(p1).get("commit");
                    commitList.add(map);
                    list.get(p1).put("commit",commitList);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
