package com.zhushou.weichat.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zhushou.weichat.R;


/**
 * Created by Administrator on 2017/1/7.
 */

public class PreviewDialog extends Dialog {

    private ImageView preview,close;//背景和关闭
    private Context mContext;
    public PreviewDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_preview);
        preview = (ImageView) findViewById(R.id.preview);
        close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dismiss();
            }
        });
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        //按空白处取消动画
        setCanceledOnTouchOutside(true);
    }


    public void previewShow(String imgeUrl){
        if (imgeUrl==null&&imgeUrl.equals(""))
            return;
        show();
        if (preview==null)
            return;
        Picasso.with(mContext).load(imgeUrl).into(preview);
    }


}
