package com.zhushou.weichat.auxiliary.sp;

import android.content.Context;

import com.zhushou.weichat.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2017/3/16.
 */

public class SPAuxiliaryPayUtils {

    public static final String AuxiliaryPayKey = "auxiliary.pay";
    public static final String QGG_INDEX = "qgg.index";


    /**
     * 保存去广告控制标识
     * @param mContext
     * @param index
     */
    public static void saveQggIndex(Context mContext,boolean index){
        SharedPreferencesUtils.setParam(mContext,QGG_INDEX,index);
    }

    /**
     * 获取去广告控制标识
     * @param mContext
     */
    public static boolean isQggIndex(Context mContext){
        return (boolean) SharedPreferencesUtils.getParam(mContext,QGG_INDEX,false);
    }
    /**
     * 付费成功  保存该功能标识
     * @param mContext
     * @param value 功能标识
     */
    public static void setAuxiliaryMerchant(Context mContext, String value){

        String getSmallMerchantkey = (String) SharedPreferencesUtils.getParam(mContext,AuxiliaryPayKey,"");
        if (getSmallMerchantkey!=null&&!getSmallMerchantkey.equals("")){
            getSmallMerchantkey+=","+value;
            SharedPreferencesUtils.setParam(mContext,AuxiliaryPayKey,getSmallMerchantkey);
        }else{
            SharedPreferencesUtils.setParam(mContext,AuxiliaryPayKey,value);
        }
    }

    /**
     * 开启微信自动抢红包所有付费功能
     * @param mContext
     */
    public static void setHBFunctionALL(Context mContext){
        String valueStr = AuxiliaryConstans.C_HB_SVIP+","+AuxiliaryConstans.C_HB_SQ
                +","+AuxiliaryConstans.C_HB_DB
                +","+AuxiliaryConstans.C_HB_XB
                +","+AuxiliaryConstans.C_HB_GR
                +","+AuxiliaryConstans.C_HB_JSD
                +","+AuxiliaryConstans.C_XPHB
                +","+AuxiliaryConstans.C_DX;
        String getSmallMerchantkey = (String) SharedPreferencesUtils.getParam(mContext,AuxiliaryPayKey,"");
        if (getSmallMerchantkey!=null&&!getSmallMerchantkey.equals("")){
            getSmallMerchantkey+=","+valueStr;
            SharedPreferencesUtils.setParam(mContext,AuxiliaryPayKey,getSmallMerchantkey);
        }else{
            SharedPreferencesUtils.setParam(mContext,AuxiliaryPayKey,valueStr);
        }
    }

    /**
     * SVIP开启微商所有功能
     * @param mContext
     */
    public static void setWsSVIPFunctionAll(Context mContext){
        String valueStr = AuxiliaryConstans.C_XPHF+","+
                AuxiliaryConstans.C_LYHF+","+
                AuxiliaryConstans.C_HB_SVIP+","+
                AuxiliaryConstans.C_HB_SQ+","+
                AuxiliaryConstans.C_HB_DB+","+
                AuxiliaryConstans.C_HB_XB+","+
                AuxiliaryConstans.C_HB_JSD+","+
                AuxiliaryConstans.C_HB_GR+","+
                AuxiliaryConstans.C_XPHB+","+
                AuxiliaryConstans.C_DX+","+
                AuxiliaryConstans.C_QUJHY+","+
                AuxiliaryConstans.C_HDJHY+","+
                AuxiliaryConstans.C_QGG+","+
                AuxiliaryConstans.C_FFH+","+
                AuxiliaryConstans.C_SVIP+","+
                AuxiliaryConstans.C_TIXIAN+","+
                AuxiliaryConstans.C_LINGQIAN+","+
                AuxiliaryConstans.C_HONGBAO+","+
                AuxiliaryConstans.C_PY_QUAN+","+
                AuxiliaryConstans.C_DUIHUA+","+
                AuxiliaryConstans.C_QUNLIAO+","+
                AuxiliaryConstans.C_ZFB_HB+","+
                AuxiliaryConstans.C_ZFB_TX+","+
                AuxiliaryConstans.C_ZFB_YE+","+
                AuxiliaryConstans.C_ZFB_ZZ+","+
                AuxiliaryConstans.C_QQ_HB+","+
                AuxiliaryConstans.C_QQ_YE+","+
                AuxiliaryConstans.C_QQ_QB+","+
                AuxiliaryConstans.C_WX_ZZ+","+
                AuxiliaryConstans.C_QQ_ZZ;
        String getSmallMerchantkey = (String) SharedPreferencesUtils.getParam(mContext,AuxiliaryPayKey,"");
        if (getSmallMerchantkey!=null&&!getSmallMerchantkey.equals("")){
            getSmallMerchantkey+=","+valueStr;
            SharedPreferencesUtils.setParam(mContext,AuxiliaryPayKey,getSmallMerchantkey);
        }else{
            SharedPreferencesUtils.setParam(mContext,AuxiliaryPayKey,valueStr);
        }
    }

    /**
     * 1.4.5 版本 多开SVIP权限开通
     * @param context
     */
    public static void setSVIPPermission(Context context){
        setWsFunctionHFAll(context);
        setWsAddFriendAll(context);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_SVIP);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_FFH);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_TIXIAN);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_LINGQIAN);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_WX_ZZ);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_HONGBAO);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_PY_QUAN);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_QUNLIAO);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_ZFB_HB);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_ZFB_TX);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_ZFB_YE);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_ZFB_ZZ);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_TIXIAN);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_QQ_HB);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_QQ_YE);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_QQ_QB);
        setAuxiliaryMerchant(context,AuxiliaryConstans.C_QQ_ZZ);

    }

    /**
     * 开通所有微商自动回复功能
     * @param mContext
     */
    public static void setWsFunctionHFAll(Context mContext){
        String valueStr = AuxiliaryConstans.C_XPHF+","+AuxiliaryConstans.C_LYHF;
        String getSmallMerchantkey = (String) SharedPreferencesUtils.getParam(mContext,AuxiliaryPayKey,"");
        if (getSmallMerchantkey!=null&&!getSmallMerchantkey.equals("")){
            getSmallMerchantkey+=","+valueStr;
            SharedPreferencesUtils.setParam(mContext,AuxiliaryPayKey,getSmallMerchantkey);
        }else{
            SharedPreferencesUtils.setParam(mContext,AuxiliaryPayKey,valueStr);
        }
    }
    /**
     * 开通所有微商截图功能
     * @param mContext
     */
    public static void setWsFunctionScreenAll(Context mContext){
        String valueStr = AuxiliaryConstans.C_TIXIAN+","+
                AuxiliaryConstans.C_LINGQIAN+","
                +AuxiliaryConstans.C_HONGBAO+","+
                AuxiliaryConstans.C_PY_QUAN+","+
                AuxiliaryConstans.C_DUIHUA+","+
                AuxiliaryConstans.C_QUNLIAO+","+
                AuxiliaryConstans.C_ZFB_HB+","+
                AuxiliaryConstans.C_ZFB_TX+","+
                AuxiliaryConstans.C_ZFB_YE+","+
                AuxiliaryConstans.C_ZFB_ZZ+","+
                AuxiliaryConstans.C_QQ_HB+","+
                AuxiliaryConstans.C_QQ_YE+","+
                AuxiliaryConstans.C_QQ_QB+","+
                AuxiliaryConstans.C_WX_ZZ+","+
                AuxiliaryConstans.C_QQ_ZZ;
        String getSmallMerchantkey = (String) SharedPreferencesUtils.getParam(mContext,AuxiliaryPayKey,"");
        if (getSmallMerchantkey!=null&&!getSmallMerchantkey.equals("")){
            getSmallMerchantkey+=","+valueStr;
            SharedPreferencesUtils.setParam(mContext,AuxiliaryPayKey,getSmallMerchantkey);
        }else{
            SharedPreferencesUtils.setParam(mContext,AuxiliaryPayKey,valueStr);
        }
    }

    /**
     * 批量自动加好友支付开启
     * @param mContext
     */
    public static void setWsAddFriendAll(Context mContext){
        String valueStr = AuxiliaryConstans.C_QUJHY+","+AuxiliaryConstans.C_HDJHY;
        String getSmallMerchantkey = (String) SharedPreferencesUtils.getParam(mContext,AuxiliaryPayKey,"");
        if (getSmallMerchantkey!=null&&!getSmallMerchantkey.equals("")){
            getSmallMerchantkey+=","+valueStr;
            SharedPreferencesUtils.setParam(mContext,AuxiliaryPayKey,getSmallMerchantkey);
        }else{
            SharedPreferencesUtils.setParam(mContext,AuxiliaryPayKey,valueStr);
        }
    }

    /**
     * 检查该功能是否已开启
     * @param mContext
     * @param value 功能标识
     * @return
     */
    public static boolean isAuxiliaryMerchantOpen(Context mContext,String value){
        String getSmallMerchantkey = (String) SharedPreferencesUtils.getParam(mContext,AuxiliaryPayKey,"");
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

//    public static final String C_HB_SVIP = "C_HB_SVIP";//微信抢红包所有功能
//    public static final String C_HB_SQ = "C_HB_SQ"; //提高手气最佳概率
//    public static final String C_HB_DB = "C_HB_DB"; //提高抢大包概率
//    public static final String C_HB_JSD = "C_HB_JSD"; //抢红包加速度
//    public static final String C_HB_GR = "C_HB_GR"; // 智能干扰竞争者
//    public static final String C_XPHB = "C_XPQHB"; //息屏自动红包
//    public static final String C_DX = "C_ZDDX";//自动答谢
    /**
     * 检查用户是否对自动抢红包功能有过付费
     * @param mContext
     * @return
     */
    public static boolean isVisibilyHBFunction(Context mContext){
        String getSmallMerchantkey = (String) SharedPreferencesUtils.getParam(mContext,AuxiliaryPayKey,"");
        String[] hbkeyArray = new String[]{"C_HB_SVIP","C_HB_SQ","C_HB_DB","C_HB_XB","C_HB_JSD","C_HB_GR","C_XPQHB","C_ZDDX"};
        boolean returnValue = false;
        if (getSmallMerchantkey!=null&&!getSmallMerchantkey.equals("")){
            String[] splitSm = getSmallMerchantkey.split(",");
            if (splitSm!=null&&splitSm.length>0){
                for (int i=0;i<splitSm.length;i++){
                    for (int k=0;k<hbkeyArray.length;k++){
                        if (hbkeyArray[k].equals(splitSm[i])){
                            returnValue = true;
                            i=splitSm.length;
                            break;
                        }
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

    String valueStr = AuxiliaryConstans.C_XPHF+","+
            AuxiliaryConstans.C_LYHF+","+
            AuxiliaryConstans.C_HB_SVIP+","+
            AuxiliaryConstans.C_HB_SQ+","+
            AuxiliaryConstans.C_HB_DB+","+
            AuxiliaryConstans.C_HB_XB+","+
            AuxiliaryConstans.C_HB_JSD+","+
            AuxiliaryConstans.C_HB_GR+","+
            AuxiliaryConstans.C_XPHB+","+
            AuxiliaryConstans.C_DX+","+
            AuxiliaryConstans.C_QUJHY+","+
            AuxiliaryConstans.C_HDJHY+","+
            AuxiliaryConstans.C_QGG+","+
            AuxiliaryConstans.C_FFH+","+
            AuxiliaryConstans.C_SVIP+","+
            AuxiliaryConstans.C_TIXIAN+","+
            AuxiliaryConstans.C_LINGQIAN+","+
            AuxiliaryConstans.C_HONGBAO+","+
            AuxiliaryConstans.C_PY_QUAN+","+
            AuxiliaryConstans.C_DUIHUA+","+
            AuxiliaryConstans.C_QUNLIAO+","+
            AuxiliaryConstans.C_ZFB_HB+","+
            AuxiliaryConstans.C_ZFB_TX+","+
            AuxiliaryConstans.C_ZFB_YE+","+
            AuxiliaryConstans.C_ZFB_ZZ+","+
            AuxiliaryConstans.C_QQ_HB+","+
            AuxiliaryConstans.C_QQ_YE+","+
            AuxiliaryConstans.C_QQ_QB+","+
            AuxiliaryConstans.C_WX_ZZ+","+
            AuxiliaryConstans.C_QQ_ZZ;
    public static String getFunctionKeyName(String functionKey){
        if (functionKey==null||functionKey.equals(""))
            return "";
        String functionName = "";

        if (functionKey.equals("WX_Single")){
            functionName = "单个分身";
            return functionName;
        }
        if (functionKey.equals("WX_LifeTime")){
            functionName = "终生分身";
            return functionName;
        }
        if (functionKey.equals("WX_AllApps")){
            functionName = "所有应用多开";
            return functionName;
        }
        if (functionKey.equals("C_ALLHF")){
            functionName = "自动回复功能";
            return functionName;
        }
        if (functionKey.equals("C_JHYALL")){
            functionKey = "批量自动加好友";
            return functionName;
        }
        if (functionKey.equals("C_XPHF")){
            functionName = "息屏自动回复";
            return functionName;
        }
        if (functionKey.equals("C_LYHF")){
            functionName = "自定义回复内容";
            return functionName;
        }
        if (functionKey.equals("C_HB_SVIP")){
            functionName = "微信抢红包所有功能";
            return functionName;
        }
        if (functionKey.equals("C_HB_SQ")){
            functionName = "提高手气最高概率";
            return functionName;
        }
        if (functionKey.equals("C_HB_DB")){
            functionName = "提高抢大包概率";
            return functionName;
        }
        if (functionKey.equals("C_HB_XB")){
            functionName = "最大概率躲避最小包";
            return functionName;
        }
        if (functionKey.equals("C_HB_JSD")){
            functionName = "抢红包加速度";
            return functionName;
        }
        if (functionKey.equals("C_HB_GR")){
            functionName = "智能干扰竞争者";
            return functionName;
        }
        if (functionKey.equals("C_XPQHB")){
            functionName = "息屏自动红包";
            return functionName;
        }
        if (functionKey.equals("C_ZDDX")){
            functionName = "自动答谢";
            return functionName;
        }
        if (functionKey.equals("C_JHTALL")){
            functionName = "微信一键加好友全部功能";
            return functionName;
        }
        if (functionKey.equals("C_QGG")){
            functionName = "启动页去广告";
            return functionName;
        }
        if (functionKey.equals("C_FFH")){
            functionName = "微信分身防封号";
            return functionName;
        }
        if (functionKey.equals("C_SVIP")){
            functionName = "开启所有微商功能svip";
            return functionName;
        }
        if (functionKey.equals("C_TIXIAN")){
            functionName = "微信提现生成器";
            return functionName;
        }
        if (functionKey.equals("C_LINGQIAN")){
            functionName = "微信零钱生成器";
            return functionName;
        }
        if (functionKey.equals("C_WX_ZZ")){
            functionName = "微信转账生成器";
            return functionName;
        }
        if (functionKey.equals("C_HONGBAO")){
            functionName = "微信红包生成器";
            return functionName;
        }
        if (functionKey.equals("C_PY_QUAN")){
            functionName = "微信朋友圈生成器";
            return functionName;
        }
        if (functionKey.equals("C_DUIHUA")){
            functionName = "微信聊天对话生成器";
            return functionName;
        }
        if (functionKey.equals("C_QUNLIAO")){
            functionName = "微信群聊生成器";
            return functionName;
        }
        if (functionKey.equals("C_ZFB_HB")){
            functionName = "支付宝红包生成器";
            return functionName;
        }
        if (functionKey.equals("C_ZFB_TX")){
            functionName = "支付宝体现生成器";
            return functionName;
        }
        if (functionKey.equals("C_ZFB_YE")){
            functionName = "支付宝零钱生成器";
            return functionName;
        }
        if (functionKey.equals("C_ZFB_ZZ")){
            functionName = "支付宝转账生成器";
            return functionName;
        }
        if (functionKey.equals("C_QQ_HB")){
            functionName = "QQ红包生成器";
            return functionName;
        }
        if (functionKey.equals("C_QQ_YE")){
            functionName = "QQ提现生成器";
            return functionName;
        }
        if (functionKey.equals("C_QQ_QB")){
            functionName = "QQ零钱生成器";
            return functionName;
        }
        if (functionKey.equals("C_QQ_ZZ")){
            functionName = "QQ转账生成器";
            return functionName;
        }

        return "";
    }

}
