package com.zhushou.weichat.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.zhushou.weichat.R;

/**
 * Created by Administrator on 2017/6/7.
 */

public class PayFailDialog extends Dialog implements View.OnClickListener {

    private Context context;


    public PayFailDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PayFailDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context){
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay_fail);
        initView();
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(false);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private TextView tv_backstage_confirmed;
    private TextView tv_give_up;
    private void initView(){
        tv_backstage_confirmed = (TextView) findViewById(R.id.tv_backstage_confirmed);
        tv_give_up = (TextView) findViewById(R.id.tv_give_up);
        tv_backstage_confirmed.setOnClickListener(this);
        tv_give_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (callBackLisener==null)
            return;
        switch (view.getId()){
            case R.id.tv_backstage_confirmed:
                callBackLisener.backstageConfirmed();
                dismiss();
                break;
            case R.id.tv_give_up:
                callBackLisener.giveUp();
                dismiss();
                break;
        }
    }
    private CallBackLisener callBackLisener;
    public void setCallVackLisener(CallBackLisener callBackLisener){
        this.callBackLisener = callBackLisener;
    }
    public interface CallBackLisener{
        void backstageConfirmed();
        void giveUp();
    }
}
