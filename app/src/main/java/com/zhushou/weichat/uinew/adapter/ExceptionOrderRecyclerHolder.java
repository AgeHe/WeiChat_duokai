package com.zhushou.weichat.uinew.adapter;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.constant.PayConstant;
import com.zhushou.weichat.uinew.dialog.PayFailureDialog;
import com.zhushou.weichat.uinew.info.ComsuptionInfo;
import com.zhushou.weichat.utils.database.DBLibs;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Administrator on 2017/1/15.
 */

public class ExceptionOrderRecyclerHolder extends RecyclerView.ViewHolder {


    ImageView iv_pay_icon;
    TextView tv_pay_name;
    TextView tv_pay_date;
    TextView tv_pay_number;
    TextView tv_history_hs;//核实
    View view_dividing_line;//核实分割线\
    TextView tv_order_status;
    LinearLayout ll_history;

    PayFailureDialog payFailureDialog;

    public ExceptionOrderRecyclerHolder(View itemView) {
        super(itemView);
        findView(itemView);

    }

    private void findView(View view){
        iv_pay_icon = (ImageView) view.findViewById(R.id.iv_pay_icon);
        tv_pay_name = (TextView) view.findViewById(R.id.tv_pay_name);
        tv_pay_date = (TextView) view.findViewById(R.id.tv_pay_date);
        tv_pay_number = (TextView) view.findViewById(R.id.tv_pay_number);
        ll_history = (LinearLayout) view.findViewById(R.id.ll_history);
        tv_history_hs = (TextView) view.findViewById(R.id.tv_history_hs);
        view_dividing_line = view.findViewById(R.id.view_dividing_line);
        tv_order_status = (TextView) view.findViewById(R.id.tv_order_status);

    }

    public int dataIndexPos;
    public void bindToData(final Activity mActivity, final ComsuptionInfo itemInfo, int position){
        this.dataIndexPos = position;
        if (this.payFailureDialog==null)
            this.payFailureDialog = new PayFailureDialog(mActivity);
        this.payFailureDialog.setComsuptionInfo(itemInfo);
        if (itemInfo==null)
            return;
        if (itemInfo.getPayType().equals(PayConstant.PAY_TYPE_WX)){
            iv_pay_icon.setImageResource(R.mipmap.pay_type_icon_wx);
        }else if (itemInfo.getPayType().equals(PayConstant.PAY_TYPE_ZFB)){
            iv_pay_icon.setImageResource(R.mipmap.pay_type_icon_zfb);
        }else if (itemInfo.getPayType().equals(PayConstant.PAY_TYPE_QQ)){
            iv_pay_icon.setImageResource(R.mipmap.icon_qq);
        }else{
            iv_pay_icon.setImageResource(R.mipmap.ic_launcher);
        }
        tv_pay_name.setText(itemInfo.getCommodityName());
        String date = "";
        try{
             date = timeToType(Long.valueOf(itemInfo.getInsertDate()));
        }catch (Exception e){
        }
        tv_pay_date.setText(date);
        tv_pay_number.setText("- "+itemInfo.getPrice()+"元");
        ll_history.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(mActivity)
                        .setMessage(itemInfo.getOrderNumber())
                        .setTitle("订单号")
                        .setNegativeButton("确定",null)
                        .show();
                return false;
            }
        });
        tv_order_status.setText(DBLibs.getOrderStatusName(itemInfo.getPayComplateStatus()));

        if (itemInfo.getPayComplateStatus()== DBLibs.Pay_status_Failure||itemInfo.getPayComplateStatus()==DBLibs.Pay_status_Unknown){
            tv_history_hs.setVisibility(View.VISIBLE);
            view_dividing_line.setVisibility(View.VISIBLE);
        }else{
            tv_history_hs.setVisibility(View.GONE);
            view_dividing_line.setVisibility(View.GONE);
        }
        tv_history_hs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payFailureDialog.requestCheckOrderHint( itemInfo.getOrderNumber(),new PayFailureDialog.OrderHint() {
                    @Override
                    public void isOrderGiveUp(boolean bon) {
                        if (bon&&myArrayListDataFsh!=null){
                            myArrayListDataFsh.refresh(dataIndexPos);
                        }
                    }
                });
            }
        });

    }
    public static String timeToType(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = new Date(time);
        return format.format(d1);
    }

    ArrayListDataFsh myArrayListDataFsh;
    public void setArrayListDataFsh(ArrayListDataFsh arrayListDataFsh){
        this.myArrayListDataFsh = arrayListDataFsh;

    }
    public interface ArrayListDataFsh{
        void refresh(int position);
    }
}
