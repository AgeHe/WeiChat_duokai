package com.zhushou.weichat.screenshot.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.activity.WxTalkaddPhoto;
import com.zhushou.weichat.screenshot.activity.WxTalkaddRedPacket;
import com.zhushou.weichat.screenshot.activity.WxTalkaddTransfer;
import com.zhushou.weichat.screenshot.activity.WxTalkaddTxt;
import com.zhushou.weichat.screenshot.activity.WxTalkaddVoice;
import com.zhushou.weichat.screenshot.adapter.MultiTalkSelectPeopleDialogAdapter;

/**
 * Created by zhanglinkai on 2017/4/7.
 * 功能:
 */

public class PublicMethod implements View.OnClickListener{
    private Activity context;
    private String tag;
    List<Map<String,String>> list;
    public PublicMethod(Activity context) {
        this.context = context;

    }
    //选择对话类型dialog
    public AlertDialog addTalk(String tag,List<Map<String,String>> list){
        this.tag=tag;
        this.list=list;
        AlertDialog dialog=new AlertDialog.Builder(context).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        View view= LayoutInflater.from(context).inflate(R.layout.layout_addtalk_dialog,null);

        LinearLayout add_talk_txt_layout=(LinearLayout)view.findViewById(R.id.add_talk_txt_layout);
        LinearLayout add_talk_picture_layout=(LinearLayout)view.findViewById(R.id.add_talk_picture_layout);
        LinearLayout add_talk_voice_layout=(LinearLayout)view.findViewById(R.id.add_talk_voice_layout);
        LinearLayout add_talk_hongbao_layout=(LinearLayout)view.findViewById(R.id.add_talk_hongbao_layout);
        LinearLayout add_talk_transfer_layout=(LinearLayout)view.findViewById(R.id.add_talk_transfer_layout);
        if (tag.equals("multi")){
            add_talk_transfer_layout.setVisibility(View.GONE);
        }else{
            add_talk_transfer_layout.setVisibility(View.VISIBLE);
        }

        add_talk_txt_layout.setOnClickListener(this);
        add_talk_picture_layout.setOnClickListener(this);
        add_talk_voice_layout.setOnClickListener(this);
        add_talk_hongbao_layout.setOnClickListener(this);
        add_talk_transfer_layout.setOnClickListener(this);

        dialog.setView(view);
        return dialog;
    }

    public AlertDialog multiTalkDialog(List<Map<String,String>> list){
        AlertDialog selectPeopleDialog=new AlertDialog.Builder(context).create();
        selectPeopleDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectPeopleDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        selectPeopleDialog.setCanceledOnTouchOutside(true);
        View view= LayoutInflater.from(context).inflate(R.layout.layout_multi_talk_selectpeople_dialog,null);
        ListView multi_talk_selectpeople_dialog_listview=(ListView)view.findViewById(R.id.multi_talk_selectpeople_dialog_listview);
        MultiTalkSelectPeopleDialogAdapter adapter=new MultiTalkSelectPeopleDialogAdapter(context,list, (MultiTalkSelectPeopleDialogAdapter.itemClick) context);
        multi_talk_selectpeople_dialog_listview.setAdapter(adapter);

        selectPeopleDialog.setView(view);
        return selectPeopleDialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_talk_txt_layout:
                //文字

                Intent txtIntent=new Intent(context,WxTalkaddTxt.class);
                txtIntent.putExtra("singleOrmulti",tag);
                txtIntent.putExtra("list", (Serializable) list);
                context.startActivityForResult(txtIntent,3);

                break;
            case R.id.add_talk_picture_layout:
                //图片

                Intent photoIntent=new Intent(context,WxTalkaddPhoto.class);
                photoIntent.putExtra("singleOrmulti",tag);
                photoIntent.putExtra("list", (Serializable) list);
                context.startActivityForResult(photoIntent,5);

                break;
            case R.id.add_talk_voice_layout:
                //语音

                Intent voiceIntent=new Intent(context,WxTalkaddVoice.class);
                voiceIntent.putExtra("singleOrmulti",tag);
                voiceIntent.putExtra("list", (Serializable) list);
                context.startActivityForResult(voiceIntent,7);

                break;
            case R.id.add_talk_hongbao_layout:
                //红包

                Intent redpaacketIntent=new Intent(context,WxTalkaddRedPacket.class);
                redpaacketIntent.putExtra("singleOrmulti",tag);
                redpaacketIntent.putExtra("list", (Serializable) list);
                context.startActivityForResult(redpaacketIntent,9);

                break;
            case R.id.add_talk_transfer_layout:
                //转账
                Intent transferIntent=new Intent(context,WxTalkaddTransfer.class);
                transferIntent.putExtra("singleOrmulti",tag);
                transferIntent.putExtra("list", (Serializable) list);
                context.startActivityForResult(transferIntent,11);
                break;
        }
    }
}
