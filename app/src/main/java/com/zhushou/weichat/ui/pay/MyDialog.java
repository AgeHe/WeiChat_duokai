package com.zhushou.weichat.ui.pay;

import android.app.Activity;
import android.util.Log;

import com.zhushou.weichat.bean.NewPayDialogInfo;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.constant.PayConstant;
import com.zhushou.weichat.ui.view.NewPayDialog;
import com.zhushou.weichat.utils.MyExceptionHandler;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;
import com.zhushou.weichat.utils.sqlite.SqliteDo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanglinkai on 2016/11/21.
 * ???:dialog?示
 * ????:dialog???
 */

public class MyDialog {
    private static final String TAG = "MyDialog";
    private NewPayDialog newPayDialog;
    //支付按钮点击标识
    boolean isPayStatu = true;
    private Activity activity;
    private PayHelper helper;
    public static String apkName;
    private float dcNum, zsNum, num;

    private UpdatePriceInfo updatePriceInfo;
    private SqliteDo sqliteDo;
    private long paymentProcesTime;

    public static String getApkName() {
        return apkName;
    }


    public static void setApkName(String apkName) {
        MyDialog.apkName = apkName;
    }

    public MyDialog(Activity activity, PayHelper.RequreClick r, PayHelper helper) {
        this.activity = activity;
        this.helper = helper;
        this.helper.setClick(r);
        updatePriceInfo = APPUsersShareUtil.getUpdateMessage(activity);
        sqliteDo = new SqliteDo(activity);
    }


    /**
     * 首页分身支付弹窗数据
     * @return
     */
    public List<NewPayDialogInfo> getHomeDialogData(){
        if (updatePriceInfo==null)
            updatePriceInfo = APPUsersShareUtil.getUpdateMessage(activity);
        List<NewPayDialogInfo> arrayData = new ArrayList<>();
        NewPayDialogInfo info1 = new NewPayDialogInfo();
        info1.setFunctionName("创建一个微信分身");
        info1.setPrice(updatePriceInfo.getScfy());
        info1.setFunctionKey(PayConstant.Pay_S_WX_DG);
        arrayData.add(0, info1);

        NewPayDialogInfo info2 = new NewPayDialogInfo();
        info2.setFunctionName("创建微信分身无限制");
        info2.setPrice(updatePriceInfo.getZcfy());
        info2.setFunctionKey(PayConstant.Pay_S_WX_ZS);
        arrayData.add(1, info2);

        NewPayDialogInfo info3 = new NewPayDialogInfo();
        info3.setFunctionName("开启所有应用分身");
        info3.setPrice(updatePriceInfo.getAllAppsPrice());
        info3.setFunctionKey(PayConstant.Pay_S_ALL_APPS);
        arrayData.add(2, info3);
        return arrayData;
    }

    /**
     * 首页分身支付弹窗数据
     * @return
     */
    public List<NewPayDialogInfo> getSVIPFunctionDialogData(NewPayDialogInfo newPayDialogInfo){
        if (updatePriceInfo==null)
            updatePriceInfo = APPUsersShareUtil.getUpdateMessage(activity);
        List<NewPayDialogInfo> arrayData = new ArrayList<>();
        NewPayDialogInfo info1 = new NewPayDialogInfo();
        info1.setFunctionName("SVIP开启所有功能");
        info1.setPrice(updatePriceInfo.getWeichat_svip_price());
        info1.setFunctionKey(PayConstant.Pay_S_SVIP);
        arrayData.add(0, info1);
        if (newPayDialogInfo!=null)
            arrayData.add(1, newPayDialogInfo);
        return arrayData;
    }


    public void showDialog(List<NewPayDialogInfo> listdata) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(activity));
        paymentProcesTime = System.currentTimeMillis();
        num = zsNum;
        if (updatePriceInfo.getScfy() == null)
            updatePriceInfo = APPUsersShareUtil.getUpdateMessage(activity);
        //初始化付费弹窗数据
        newPayDialog = new NewPayDialog(activity,
                listdata,updatePriceInfo.getDiscount());
        //设置显示隐藏支付类型
        newPayDialog.setPayTypeIsVisibility(updatePriceInfo.isPay_status_ali(),
                updatePriceInfo.isPay_status_wx(),updatePriceInfo.isPay_status_qq());
        //设置接收回调
        newPayDialog.setpayDialogInterface(new NewPayDialog.PayDialogInterface() {
            @Override
            public void paySelectCallback(NewPayDialogInfo dateInfo, String functionKey, int payType, String price) {
                Log.i(TAG, "newpaydialog_paySelectCallback: name="+dateInfo.getFunctionName()+"// functionKey=" + functionKey + "&payType=" + payType + "&price=" + price);
                num = Float.valueOf(price);
                switch (payType) {
                    case 1:
                        helper.doAliPay(dateInfo,dateInfo.getFunctionName(),num,newPayDialog);
                        helper.setPayType(PayConstant.PAY_TYPE_ZFB);
                        break;
                    case 2:
                        helper.doWxPay(dateInfo,dateInfo.getFunctionName(),num,newPayDialog);
                        helper.setPayType(PayConstant.PAY_TYPE_WX);
                        break;
                    case 3:
                        helper.doQQPay(dateInfo,dateInfo.getFunctionName(),num,newPayDialog);
                        helper.setPayType(PayConstant.PAY_TYPE_QQ);
                        break;
                }
            }
        });
        newPayDialog.show();
    }


    public float getZsNum() {
        return zsNum;
    }

    public void setZsNum(float zsNum) {
        this.zsNum = zsNum;
    }

    public float getDcNum() {
        return dcNum;
    }

    public void setDcNum(float dcNum) {
        this.dcNum = dcNum;
    }

    public long getPaymentProcesTime() {
        long returnTime = paymentProcesTime;
        paymentProcesTime = 0;
        return returnTime;
    }

    public void dismiss() {
        if (newPayDialog != null && newPayDialog.isShowing())
            newPayDialog.payDismiss();
    }
}
