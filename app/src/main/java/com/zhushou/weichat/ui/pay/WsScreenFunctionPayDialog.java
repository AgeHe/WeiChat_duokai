package com.zhushou.weichat.ui.pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.widget.Toast;

import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.NewPayDialogInfo;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.constant.PayConstant;
import com.zhushou.weichat.ui.MainActivity;
import com.zhushou.weichat.ui.view.NewPayDialog;
import com.zhushou.weichat.utils.CountUtil;
import com.zhushou.weichat.utils.HistoryPayShareUtil;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;
import com.zhushou.weichat.utils.sqlite.SqliteDo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */

public class WsScreenFunctionPayDialog {

    private static final String TAG = "WsScreenFunctionPayDialog";
    // 是否显示全部功能价格选择 （默认显示）
    private boolean isAllPrice = true;
    private boolean isSvipPrice = true; // 默认不显示
    public boolean isSvipDialog = false;
    public boolean isSvipOne = false;

    //支付按钮点击标识
    boolean isPayStatu = true;
    private CountUtil countUtil;
    private Activity activity;
    private static AlertDialog dialog;
    private PayHelper helper;
    private PayHelper.RequreClick r;
    private float aPrice,allPrice,svipPrice,price;
    private Resources mResources;
    private int waretype; // 选择的商品类型

    private String singleSelectorStr = "";

    private String dialogTitleName = "";

    private String dialogTitleNameAll = "";

    private String saveCommondityKey = "";
    private PayLodingDialog payLodingDialog;

    private UpdatePriceInfo updatePriceInfo;
    private NewPayDialog newPayDialog;
    private SqliteDo sqliteDo;

    public WsScreenFunctionPayDialog(Activity activity, PayHelper.RequreClick r, PayHelper helper){
        this.activity = activity;
        mResources = activity.getResources();
        this.r=r;
        this.helper=helper;
        countUtil = new CountUtil(activity);
        this.updatePriceInfo = APPUsersShareUtil.getUpdateMessage(activity);
        payLodingDialog = new PayLodingDialog(activity);
        sqliteDo = new SqliteDo(activity);
    }


    private String selectTitleName(int waretype){
        String titleName = "";
        if (waretype==MainActivity.lifeTimeType){
            titleName = dialogTitleNameAll;
        }else if (waretype==MainActivity.svipAllType){
            titleName = mResources.getString(R.string.weichat_svip_all_function_title);
        }else{
            titleName = dialogTitleName;
        }
        return titleName;
    }

    public void showDialog(String FunctionKey){
        price = isSvipPrice?svipPrice:allPrice;//默认购买全部类型商品
        waretype = isSvipPrice?MainActivity.svipAllType:MainActivity.lifeTimeType;


        dialogTitleNameAll = mResources.getString(R.string.weichat_name_all);

        List<NewPayDialogInfo> listdata = new ArrayList<NewPayDialogInfo>();
        if (isSvipDialog){
            NewPayDialogInfo info = new NewPayDialogInfo();
            info.setFunctionName("开启所有VIP推荐功能");
            info.setPrice(String.valueOf(svipPrice));
//            info.setOldPrice(discount(
//                    Double.valueOf(svipPrice),
//                    Double.valueOf(updatePriceInfo.getDiscount())
//            ));
            info.setFunctionKey(PayConstant.Pay_S_SVIP);
            listdata.add(0, info);
        }
        if (isAllPrice){
            NewPayDialogInfo info2 = new NewPayDialogInfo();
            info2.setFunctionName("开启所有生成器功能");
            info2.setPrice(String.valueOf(allPrice));
            info2.setFunctionKey(PayConstant.Pay_S_SVIP);
//            info2.setOldPrice(discount(
//                    Double.valueOf(allPrice),
//                    Double.valueOf(updatePriceInfo.getDiscount())
//            ));
            listdata.add(isSvipDialog?1:0, info2);
        }

        NewPayDialogInfo info2 = new NewPayDialogInfo();
        info2.setFunctionName(dialogTitleName);
        info2.setPrice(String.valueOf(aPrice));
        info2.setFunctionKey(FunctionKey);
//        info2.setOldPrice(discount(
//                Double.valueOf(aPrice),
//                Double.valueOf(updatePriceInfo.getDiscount())
//        ));
        if (isSvipOne){
//            listdata.add(0, info2);
        }else if (!isSvipDialog&&!isAllPrice){
            listdata.add(0, info2);
        }else if (isSvipDialog&&isAllPrice){
            listdata.add(2, info2);
        }else if (!isSvipDialog||!isAllPrice){
            listdata.add(1, info2);
        }


        newPayDialog = new NewPayDialog(activity,
                listdata,
               updatePriceInfo.getDiscount());
        newPayDialog.setPayTypeIsVisibility(updatePriceInfo.isPay_status_ali(),
                updatePriceInfo.isPay_status_wx(),updatePriceInfo.isPay_status_qq());

        newPayDialog.setpayDialogInterface(new NewPayDialog.PayDialogInterface() {
            @Override
            public void paySelectCallback(NewPayDialogInfo dateInfo, String functionKey, int payType, String selectPrice) {
                price = Float.valueOf(selectPrice);
                helper.setNum(String.valueOf(price));
                switch (payType) {
                    case 1:
                        helper.doAliPay(dateInfo,dateInfo.getFunctionName(),price,newPayDialog);
                        helper.setPayType(PayConstant.PAY_TYPE_ZFB);
                        break;
                    case 2:
                        helper.doWxPay(dateInfo,dateInfo.getFunctionName(),price,newPayDialog);
                        helper.setPayType(PayConstant.PAY_TYPE_WX);
                        break;
                    case 3:
                        helper.doQQPay(dateInfo,dateInfo.getFunctionName(),price,newPayDialog);
                        helper.setPayType(PayConstant.PAY_TYPE_QQ);
                        break;
                }
            }
        });

        newPayDialog.show();
    }





    private String discount(double price, double discount) {
        DecimalFormat df = new DecimalFormat("####0.00");
        double resultPrice = price / discount;
        return String.valueOf(df.format(resultPrice));
    }


    public boolean isShow(){
        return dialog==null?false:dialog.isShowing();
    }
    public void dismiss(){
        if (newPayDialog!=null)
            newPayDialog.dismiss();
    }

    private boolean  check (){
        if (!HistoryPayShareUtil.isWeixinAvilible(activity)){
            Toast.makeText(activity,activity.getString(R.string.anzhuang_weixin_app),Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    public float getaPrice() {
        return aPrice;
    }
    public void setaPrice(float aPrice) {
        this.aPrice = aPrice;
    }
    public float getAllPrice() {
        return allPrice;
    }
    public void setAllPrice(float allPrice) {
        this.allPrice = allPrice;
    }
    public float getSvipPrice() {
        return svipPrice;
    }
    public void setSvipPrice(float svipPrice) {
        this.svipPrice = svipPrice;
    }

    public boolean isSvipPrice() {
        return isSvipPrice;
    }

    public void setSvipPrice(boolean svipPrice) {
        isSvipPrice = svipPrice;
    }

    public String getSingleSelectorStr() {
        return singleSelectorStr;
    }

    public void setSingleSelectorStr(String singleSelectorStr) {
        this.singleSelectorStr = singleSelectorStr;
    }

    public String getDialogTitleName() {
        return dialogTitleName;
    }

    public void setDialogTitleName(String dialogTitleName) {
        this.dialogTitleName = dialogTitleName;
    }

    public String getSaveCommondityKey() {
        return saveCommondityKey;
    }

    public void setSaveCommondityKey(String saveCommondityKey) {
        this.saveCommondityKey = saveCommondityKey;
    }

    public String getDialogTitleNameAll() {
        return dialogTitleNameAll;
    }

    public void setDialogTitleNameAll(String dialogTitleNameAll) {
        this.dialogTitleNameAll = dialogTitleNameAll;
    }

    public boolean isAllPrice() {
        return isAllPrice;
    }

    public void setAllPrice(boolean allPrice) {
        isAllPrice = allPrice;
    }


}
