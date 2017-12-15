package com.zhushou.weichat.auxiliary.ui.impl;

import android.app.Activity;
import android.content.res.Resources;
import android.widget.Switch;

import com.zhushou.weichat.R;
import com.zhushou.weichat.auxiliary.interfac.ui.HFActivityImpl;
import com.zhushou.weichat.auxiliary.sp.SPAuxiliaryPayUtils;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

/**
 * Created by Administrator on 2017/3/16.
 */

public class HFImpl implements HFActivityImpl.MyHFImpl {

    private Activity mActivity;
    private HFActivityImpl.MyHFActivityImpl myHFActivityImpl;
    private Resources mResources;
    private UpdatePriceInfo updateMessageInfo;
    public HFImpl(Activity activity,HFActivityImpl.MyHFActivityImpl HFActivity){
        this.mActivity = activity;
        this.myHFActivityImpl = HFActivity;
        this.mResources = mActivity.getResources();
        updateMessageInfo = APPUsersShareUtil.getUpdateMessage(mActivity);
    }


    @Override
    public void functionClick(String functionKey, Switch switchView) {
        if (switchView==null){
            myHFActivityImpl.functionClickBack(functionKey,true);
            return;
        }

        if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(mActivity,functionKey)){
            myHFActivityImpl.functionClickBack(functionKey,true);
        }else{
            myHFActivityImpl.functionPay(getPayName(functionKey),getPayPrice(functionKey),functionKey);
        }
    }

    @Override
    public void paySuccess(String payFunctionKey) {

    }

    @Override
    public void pauFailure(String payFunctionKey) {

    }

    private String getPayName(String functionKey){
        String payname = "";
        if (functionKey.equals("C_XPHF")){
            payname = mResources.getString(R.string.weichat_hf_xphf);
        }else if(functionKey.equals("C_LYHF")){
            payname = mResources.getString(R.string.weichat_hf_hfnr);
        }else if(functionKey.equals("C_HF")){
            payname = mResources.getString(R.string.weichat_hf_hf);
        }
        return payname;
    }

    private String getPayPrice(String functionKey){
        String payname = "0";
        if (functionKey.equals("C_XPHF")){
//            payname = updateMessageInfo.getWeichat_xphf_price();
        }else if(functionKey.equals("C_LYHF")){
//            payname = updateMessageInfo.getWeichat_hfnr_price();
        }else if(functionKey.equals("C_HF")){
//            payname = mResources.getString(R.string.weichat_hf_hf);
            payname = "0";
        }
        return payname;
    }

}
