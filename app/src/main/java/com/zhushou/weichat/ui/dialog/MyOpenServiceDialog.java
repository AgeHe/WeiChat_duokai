package com.zhushou.weichat.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.abs.BaseDialog;
import com.zhushou.weichat.auxiliary.sp.SPAuxiliaryPayUtils;
import com.zhushou.weichat.bean.OrderInfo;
import com.zhushou.weichat.utils.MyBackCheckService;
import com.zhushou.weichat.utils.sqlite.SqliteDo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/3.
 * 我的兑换提示框
 */

public class MyOpenServiceDialog extends BaseDialog implements View.OnClickListener{
    private ArrayList<OrderInfo> list;
    private TextView dialog_content;
    private SqliteDo sqliteDo;
    private Context mContext;
    public MyOpenServiceDialog(Context context, ArrayList<OrderInfo> list) {
        super(context, R.style.MyDialog);
        this.list = list;
        this.mContext =context;
        sqliteDo = new SqliteDo(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_open_service);
        dialog_content = (TextView) findViewById(R.id.dialog_content);
        dialog_content.setMovementMethod(new ScrollingMovementMethod());
        findViewById(R.id.now).setOnClickListener(this);
        findViewById(R.id.later).setOnClickListener(this);
        setData(list);
    }

    public void setData( ArrayList<OrderInfo> list){
        this.list = list;
        StringBuilder stringBuilder = new StringBuilder();
        for(OrderInfo info :list){
            String wareName = "";

            SPAuxiliaryPayUtils.getFunctionKeyName(info.getFunctionKey());

            stringBuilder.append(info.getDate()).append("的").append(wareName).append(getContext().getString(R.string.comfirm_fail)).append("\n");

        }
        dialog_content.setText(stringBuilder.toString());
    }

    @Override
    public void dismiss(){
        sqliteDo.closeDb();
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.now:
                Intent intent = new Intent(mContext, MyBackCheckService.class);
                mContext.startService(intent);
                dismiss();
                break;
            case R.id.later:
                for(OrderInfo info : list){
                  sqliteDo.changeNeedCheckAndOrderStatus(info.getOrderNum(),"false",2);
                }
                dismiss();
                break;

        }
    }
}
