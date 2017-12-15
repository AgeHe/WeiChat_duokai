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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhushou.weichat.R;

import static com.zhushou.weichat.R.id.multi_talk_infoset_dialog_title;

/**
 * Created by zhanglinkai on 2017/4/11.
 * 功能:
 */

public class WxFriendCirclePreviewAddCommit extends Activity {
    private ImageView wxfriendcircle_preview_addcommit_title_iv;
    private TextView wxfriendcircle_preview_addcommit_title_btn;
    private RelativeLayout wxfriendcircle_preview_addcommit_from_layout;
    private TextView wxfriendcircle_preview_addcommit_from_tv;
    private RelativeLayout wxfriendcircle_preview_addcommit_to_layout;
    private TextView wxfriendcircle_preview_addcommit_to_tv;
    private EditText wxfriendcircle_preview_addcommit_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wxfriendcircle_preview_commit);
        iniUI();
    }

    private void iniUI() {
        wxfriendcircle_preview_addcommit_title_iv=(ImageView)findViewById(R.id.wxfriendcircle_preview_addcommit_title_iv);

        wxfriendcircle_preview_addcommit_title_btn=(TextView)findViewById(R.id.wxfriendcircle_preview_addcommit_title_btn);
        wxfriendcircle_preview_addcommit_from_layout=(RelativeLayout)findViewById(R.id.wxfriendcircle_preview_addcommit_from_layout);
        wxfriendcircle_preview_addcommit_from_tv=(TextView)findViewById(R.id.wxfriendcircle_preview_addcommit_from_tv);
        wxfriendcircle_preview_addcommit_to_layout=(RelativeLayout)findViewById(R.id.wxfriendcircle_preview_addcommit_to_layout);
        wxfriendcircle_preview_addcommit_to_tv=(TextView)findViewById(R.id.wxfriendcircle_preview_addcommit_to_tv);
        wxfriendcircle_preview_addcommit_et=(EditText)findViewById(R.id.wxfriendcircle_preview_addcommit_et);

        fromDialog=iniDialog("from");
        toDialog=iniDialog("to");

        wxfriendcircle_preview_addcommit_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wxfriendcircle_preview_addcommit_from_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from_dialog_title.setText(R.string.shezhipinglunren);
                fromDialog.show();
            }
        });
        wxfriendcircle_preview_addcommit_to_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                to_dialog_title.setText(R.string.shezhipinglunduixiang);
                toDialog.show();
            }
        });

        wxfriendcircle_preview_addcommit_title_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String f=wxfriendcircle_preview_addcommit_from_tv.getText().toString().trim();
                String t=wxfriendcircle_preview_addcommit_to_tv.getText().toString().trim();
                String e=wxfriendcircle_preview_addcommit_et.getText().toString().trim();

                if (!f.isEmpty()){
                    Intent intent = new Intent();
                    if (t.isEmpty()){
                        intent.putExtra("type", "from");
                    }else{
                        intent.putExtra("type", "to");
                    }
                    intent.putExtra("from", f);
                    intent.putExtra("to", t);
                    intent.putExtra("con", e);
                    setResult(4,intent);
                    finish();
                }else{
                    Toast.makeText(WxFriendCirclePreviewAddCommit.this,R.string.qqbalance_toast, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    AlertDialog fromDialog;
    AlertDialog toDialog;
    EditText from;
    EditText to;
    TextView from_dialog_title;
    TextView to_dialog_title;
    private AlertDialog iniDialog(String tag){

        View view= LayoutInflater.from(this).inflate(R.layout.layout_multi_talk_infoset_name_dialog,null);

        if (tag.equals("from")){
            from_dialog_title=(TextView)view.findViewById(multi_talk_infoset_dialog_title);
            from=(EditText)view.findViewById(R.id.multi_talk_infoset_dialog_et);
        }else if (tag.equals("to")){
            to_dialog_title=(TextView)view.findViewById(multi_talk_infoset_dialog_title);
            to=(EditText)view.findViewById(R.id.multi_talk_infoset_dialog_et);
        }
        TextView multi_talk_infoset_dialog_cancle=(TextView)view.findViewById(R.id.multi_talk_infoset_dialog_cancle);
        TextView multi_talk_infoset_dialog_sure=(TextView)view.findViewById(R.id.multi_talk_infoset_dialog_sure);
        AlertDialog dialog=new AlertDialog.Builder(this).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);

        multi_talk_infoset_dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromDialog.isShowing()){
                    String f=from.getText().toString().trim();
                    wxfriendcircle_preview_addcommit_from_tv.setText(f);
                    fromDialog.dismiss();
                }else if (toDialog.isShowing()){
                    String t=to.getText().toString().trim();
                    wxfriendcircle_preview_addcommit_to_tv.setText(t);
                    toDialog.dismiss();
                }
            }
        });
        multi_talk_infoset_dialog_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromDialog.isShowing()){
                    fromDialog.dismiss();
                }else if (toDialog.isShowing()){
                    toDialog.dismiss();
                }
            }
        });


        return dialog;
    }
}
