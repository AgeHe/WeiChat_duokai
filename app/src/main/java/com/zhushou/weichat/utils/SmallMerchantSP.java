package com.zhushou.weichat.utils;

import android.content.Context;

/**
 * Created by Administrator on 2017/2/21.
 */

public class SmallMerchantSP {

    public static final String SmallMerchantKey = "small.merchant";


    /**
     * 付费成功  保存该功能标识
     * @param mContext
     * @param value 功能标识
     */
    public static void setSmallMerchant(Context mContext,String value){

        String getSmallMerchantkey = (String) SharedPreferencesUtils.getParam(mContext,SmallMerchantKey,"");
        if (getSmallMerchantkey!=null&&!getSmallMerchantkey.equals("")){
            getSmallMerchantkey+=","+value;
            SharedPreferencesUtils.setParam(mContext,SmallMerchantKey,getSmallMerchantkey);
        }else{
            SharedPreferencesUtils.setParam(mContext,SmallMerchantKey,value);
        }
    }

    /**
     * 检查该功能是否已开启
     * @param mContext
     * @param value 功能标识
     * @return
     */
    public static boolean isSmallMerchantOpen(Context mContext,String value){
        String getSmallMerchantkey = (String) SharedPreferencesUtils.getParam(mContext,SmallMerchantKey,"");
        boolean returnValue = false;
        if (getSmallMerchantkey!=null&&!getSmallMerchantkey.equals("")){
            String[] splitSm = getSmallMerchantkey.split(",");
            if (splitSm!=null&&splitSm.length>0){
                for (int i=0;i<splitSm.length;i++){
                    if (value.equals(splitSm[i])){
                        returnValue = true;
                        break;
                    }
                }
            }else{
                returnValue =  false;
            }
        }else{
            returnValue =  false;
        }
        return returnValue;
    }

}
