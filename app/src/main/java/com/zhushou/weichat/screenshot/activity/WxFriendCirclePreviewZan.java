package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2017/4/11.
 * 功能:
 */

public class WxFriendCirclePreviewZan extends Activity {
    private ImageView wxfriendcircle_preview_zan_title_iv;
    private TextView wxfriendcircle_preview_zan_title_btn;
    private EditText wxfriendcircle_preview_zan_et;
    private Button wxfriendcircle_preview_zan_random;
    private String[] name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wxfriendcircle_preview_zan);
        iniUI();
    }

    private void iniUI() {
        name=getResources().getStringArray(R.array.name);
        final StringBuffer stringBuffer=new StringBuffer(getString(R.string.testname));
        wxfriendcircle_preview_zan_title_iv=(ImageView)findViewById(R.id.wxfriendcircle_preview_zan_title_iv);
        wxfriendcircle_preview_zan_title_btn=(TextView)findViewById(R.id.wxfriendcircle_preview_zan_title_btn);
        wxfriendcircle_preview_zan_et=(EditText)findViewById(R.id.wxfriendcircle_preview_zan_et);
        wxfriendcircle_preview_zan_random=(Button)findViewById(R.id.wxfriendcircle_preview_zan_random);
        wxfriendcircle_preview_zan_et.setText(stringBuffer);
        wxfriendcircle_preview_zan_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wxfriendcircle_preview_zan_random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r=new Random();
                int i=r.nextInt(name.length);
                stringBuffer.append(getString(R.string.douhao)+name[i]);
                wxfriendcircle_preview_zan_et.setText(stringBuffer);
            }
        });

        wxfriendcircle_preview_zan_title_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("zan",wxfriendcircle_preview_zan_et.getText().toString().trim());
                setResult(2,intent);
                finish();
            }
        });
    }
}
