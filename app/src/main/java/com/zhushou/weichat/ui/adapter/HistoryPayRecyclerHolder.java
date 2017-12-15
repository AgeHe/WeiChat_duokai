package com.zhushou.weichat.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.bean.HistoryPayInfo;

/**
 * Created by Administrator on 2017/1/15.
 */

public class HistoryPayRecyclerHolder extends RecyclerView.ViewHolder {


    ImageView iv_pay_icon;
    TextView tv_pay_name;
    TextView tv_pay_date;
    TextView tv_pay_number;

    public HistoryPayRecyclerHolder(View itemView) {
        super(itemView);
        findView(itemView);
    }

    private void findView(View view){
        iv_pay_icon = (ImageView) view.findViewById(R.id.iv_pay_icon);
        tv_pay_name = (TextView) view.findViewById(R.id.tv_pay_name);
        tv_pay_date = (TextView) view.findViewById(R.id.tv_pay_date);
        tv_pay_number = (TextView) view.findViewById(R.id.tv_pay_number);
    }

    public void bindToData(Activity mActivity, HistoryPayInfo itemInfo){
        if (itemInfo==null)
            return;
        if (itemInfo.getPayType().equals(VApp.PAYTYPE_WX)){
            iv_pay_icon.setImageResource(R.mipmap.ic_pay_weichat);
        }else{
            iv_pay_icon.setImageResource(R.mipmap.ic_pay_ali);
        }
        tv_pay_name.setText(itemInfo.getPayName());
        tv_pay_date.setText(itemInfo.getPayDate());
        tv_pay_number.setText("- "+itemInfo.getPayNumber()+mActivity.getResources().getString(R.string.rmb_yuan));
    }

}
