package com.zhushou.weichat.ui.pay;

import android.content.Context;

import com.zhushou.weichat.VApp;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    /*
     * MD5加密
     */
    public static String getDigest(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    public static boolean isVisiblePayWindow(String PayType, Context context){
        try {
            UpdatePriceInfo info = APPUsersShareUtil.getUpdateMessage(context);
            if (PayType.equals(VApp.PAYTYPE_ZFB)){
                return info.isPay_status_ali();
            }else if (PayType.equals(VApp.PAYTYPE_WX)){
                return info.isPay_status_wx();
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
} 