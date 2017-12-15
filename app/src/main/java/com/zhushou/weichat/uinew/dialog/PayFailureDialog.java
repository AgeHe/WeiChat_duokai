package com.zhushou.weichat.uinew.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;

import com.lbsw.stat.LBStat;
import com.zhushou.weichat.api.RequestHost;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.constant.PayConstant;
import com.zhushou.weichat.ui.ComplaintsActivity;
import com.zhushou.weichat.uinew.info.ComsuptionInfo;
import com.zhushou.weichat.utils.CountUtil;
import com.zhushou.weichat.utils.DebugLog;
import com.zhushou.weichat.utils.OkHttpUtil;
import com.zhushou.weichat.utils.database.DBLibs;
import com.zhushou.weichat.utils.database.OperationDB;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/22.
 */

public class PayFailureDialog {

    private static final String TAG = "PayFailureDialog";
    ComsuptionInfo comsuptionInfo;
    Context context;
    OperationDB operationDB;
    String mOrderNumber;
    CountUtil countUtil;

    public PayFailureDialog(Context context) {
        this.context = context;
        this.operationDB = new OperationDB(context);
        this.countUtil = new CountUtil(context);
    }
    public void setComsuptionInfo(ComsuptionInfo comsuptionInfo){
        this.comsuptionInfo = comsuptionInfo;
        this.mOrderNumber = comsuptionInfo.getOrderNumber();
    }


    /**
     * 处理失败订单
     */
    public void failureOrderDialog() {
        if (comsuptionInfo==null)
            return;
        this.mOrderNumber = comsuptionInfo.getOrderNumber();
        ComsuptionInfo info = operationDB.selectOerder(mOrderNumber);
        if (info == null) {
            //失败异常订单
            operationDB.changePayComplateStatus(mOrderNumber, DBLibs.Pay_status_failure_E);
            return;
        }
        this.comsuptionInfo = info;
        new AlertDialog.Builder(context)
                .setTitle("支付结果")
                .setMessage("是否已完成付款？")
                .setCancelable(false)
                .setNegativeButton("未付款", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        operationDB.changePayComplateStatus(mOrderNumber, DBLibs.Pay_status_failure_finish);
                    }
                })
                .setPositiveButton("已付款", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestCheckOrder();
                    }
                }).show();
    }

    ProgressDialog progressDialog;
    int progressIndex = 0;
    boolean progressDialogDismiss = false;

    OrderHint myOrderHint;

    public void requestCheckOrderHint(String orderNumber, final OrderHint orderHint) {
        this.mOrderNumber = orderNumber;
        this.myOrderHint = orderHint;
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("确认进行订单核实？")
                .setCancelable(true)
                .setNegativeButton("放弃订单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        operationDB.changePayComplateStatus(mOrderNumber, DBLibs.Pay_status_failure_finish);
                        if (myOrderHint != null) {
                            myOrderHint.isOrderGiveUp(true);
                        }
                    }
                })
                .setPositiveButton("核实订单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestCheckOrder();
                    }
                }).show();
    }

    public interface OrderHint {
        void isOrderGiveUp(boolean bon);
    }

    public void requestCheckOrder() {
        progressIndex = 0;
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("查询订单");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMax(100);
        progressDialog.setMessage("请稍等，正在为您查询订单...");
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                progressDialogDismiss = true;
            }
        });
        progressDialog.show();
        new Thread(new CheckOrderRunnable()).start();
    }

    class CheckOrderRunnable implements Runnable {
        @Override
        public void run() {
            if (progressDialogDismiss)
                return;
            long startTime = System.currentTimeMillis();
            String requreUrl = RequestHost.AppInfoPayHost +
                    "/order/info.json?orderno=" + mOrderNumber + "&paytype=" + comsuptionInfo.getPayChannelType();

            OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
            OkHttpResult result = okHttpUtil.requestGetBySyn(requreUrl, null, null);
            Message msg = new Message();
            msg.obj = result;
            msg.what = 0;
            long differenceTime = System.currentTimeMillis() - startTime;
            if (differenceTime < 2000) {
                try {
                    Thread.sleep(2000 - differenceTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            requestOrderHandler.sendMessage(msg);
        }
    }

    public Handler requestOrderHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    OkHttpResult result = (OkHttpResult) msg.obj;
                    if (result != null && result.isSuccess) {
                        try {
                            JSONObject object = new JSONObject(result.msg);
                            String responseCode = object.getString("responseCode");
                            String transStatus = object.getString("transStatus");
                            if (responseCode.equals("A001") && transStatus.equals("A001")) {
                                sendEmptyMessage(2);
                            } else {
                                sendEmptyMessage(1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            sendEmptyMessage(1);
                        }
                    } else {
                        sendEmptyMessage(1);
                    }
                    break;
                case 1:
                    if (progressIndex <= 100) {
                        progressIndex += 5;
                        progressDialog.setProgress(progressIndex);
                        new Thread(new CheckOrderRunnable()).start();
                    } else {
                        progressDialog.dismiss();
                        //订单查询失败，提示用户联系客服
                        new AlertDialog.Builder(context).setTitle("支付结果")
                                .setMessage("经过查询您的这笔订单未付款\n请问您对此单是否有疑问？")
                                .setCancelable(true)
                                .setNegativeButton("没有,放弃订单", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //更改订单标识，设置为放弃订单
                                        operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_failure_finish);
                                        if (myOrderHint != null)
                                            myOrderHint.isOrderGiveUp(true);
                                    }
                                })
                                .setPositiveButton("有,去联系客服", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // TODO: 2017/11/30 跳转客服界面
                                        context.startActivity(new Intent(context, ComplaintsActivity.class));
                                    }
                                })
                                .show();
                    }
                    break;
                case 2:
                    if (comsuptionInfo != null && !comsuptionInfo.getCommodityFunctionType().equals("")) {
                        //开启功能，保存功能标识
                        comsuptionInfo = operationDB.changePayComplateStatus(comsuptionInfo.getOrderNumber(), DBLibs.Pay_status_Successful);
                        LBStat.pay(comsuptionInfo.getPayType(),
                                comsuptionInfo.getOrderNumber(),
                                true,
                                PayConstant.getPayDescribeStr(comsuptionInfo.getCommodityFunctionType()),
                                Float.valueOf(comsuptionInfo.getPrice()),
                                comsuptionInfo.getPayType());
                        DebugLog.e(TAG, "paySelectCallback: ordernumber=" + comsuptionInfo.getOrderNumber() +
                                " //functionkey=" + comsuptionInfo.getCommodityFunctionType() +
                                " //price=" + comsuptionInfo.getPrice());
                    }
                    progressDialog.dismiss();
                    new AlertDialog.Builder(context).setTitle("查询结果")
                            .setMessage(comsuptionInfo != null ?
                                    "\t\t付款成功!\n\t\t功能：" + comsuptionInfo.getCommodityName() + "\n\t\t已成功支付：" + comsuptionInfo.getPrice() + "\n\t\t请返回主界面查看！" :
                                    "\t\t付款成功！\n\t\t功能异常，请联系客服了解情况。十分抱歉！")
                            .setNeutralButton("确定", null)
                            .show();
                    if (payFailureDialogCallback != null && comsuptionInfo != null)
                        payFailureDialogCallback.seccess(comsuptionInfo);
                    break;
            }


        }
    };


    public PayFailureDialogCallback payFailureDialogCallback;

    public void setCallBack(PayFailureDialogCallback payFailureDialogCallback) {
        this.payFailureDialogCallback = payFailureDialogCallback;
    }

    public interface PayFailureDialogCallback {
        void seccess(ComsuptionInfo comsuptionInfo);
    }
}
