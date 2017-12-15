package com.zhushou.weichat.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ImageView;

import com.zhushou.weichat.R;


/**
 * Created by Administrator on 2017/1/7.
 */

public class ShareOrPayDialog extends Dialog {

    private ImageView close;//关闭
    private Button share,pay;//分享，支付
    private ShareOrPayListener mListener;

    public ShareOrPayDialog(@NonNull Context context,ShareOrPayListener listener) {
        super(context,R.style.MyDialog);
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_shareorpay);
        close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(view ->{
                dismiss();
        });
        share = (Button) findViewById(R.id.share);
        share.setOnClickListener(view ->{
            mListener.shareClick();
            dismiss();
        });
        pay = (Button) findViewById(R.id.pay);
        pay.setOnClickListener(view ->{
            mListener.payClick();
            dismiss();
        });

        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

    }

   public interface ShareOrPayListener{
        void shareClick();
        void payClick();
   }


}
