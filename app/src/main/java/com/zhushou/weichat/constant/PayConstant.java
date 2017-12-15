package com.zhushou.weichat.constant;

/**
 * Created by Administrator on 2017/11/30.
 */

public class PayConstant {

    public static final String PAY_TYPE_WX = "weixin";
    public static final String PAY_TYPE_ZFB = "alipay";
    public static final String PAY_TYPE_QQ = "qqpay";

    public static final String Pay_S_WX_DG = "dg.wx"; //单个微信标识
    public static final String Pay_S_WX_ZS = "zs.wx"; //终身微信标识
    public static final String Pay_S_ALL_APPS = "allapps.wx"; //所有应用多开标识
    public static final String Pay_S_SVIP = "svip.wx"; //SVIP功能
    public static final String Pay_S_SVIP_JHY = "svip.jhy.wx"; //加好友
    public static final String Pay_S_SVIP_HF = "svip.hf.wx"; //自动回复
    public static final String Pay_S_SVIP_FFH = "svip.ffh.wx"; //防封号

    //12.5新增 jyh
    public static final String Pay_S_WX_TX = "svip.tx.wx"; //微信提现生成器
    public static final String Pay_S_WX_LQ = "svip.lq.wx"; //微信零钱生成器
    public static final String Pay_S_WX_ZZ = "svip.zz.wx";//微信转账生成器
    public static final String Pay_S_WX_HB = "svip.hb.wx"; //微信红包生成器
    public static final String Pay_S_WX_PYQ = "svip.pyq.wx"; //微信朋友圈生成器
    public static final String Pay_S_WX_DH = "svip.dh.wx"; //微信聊天对话生成器
    public static final String Pay_S_WX_QL = "svip.ql.wx";//微信群聊生成器
    public static final String Pay_S_ZFB_HB = "svip.hb.zfb";//支付宝红包生成器
    public static final String Pay_S_ZFB_TX = "svip.tx.zfb";//支付宝体现生成器
    public static final String Pay_S_ZFB_YE = "svip.ye.zfb";//支付宝零钱生成器
    public static final String Pay_S_ZFB_ZZ = "svip.zz.zfb";//支付宝转账生成器
    public static final String Pay_S_QQ_HB = "svip.hb.qq";//QQ红包生成器
    public static final String Pay_S_QQ_YE = "svip.ye.qq";//QQ提现生成器
    public static final String Pay_S_QQ_QB = "svip.qb.qq";//QQ零钱生成器
    public static final String Pay_S_QQ_ZZ = "svip.zz.qq";//QQ转账生成器



    public static final String Pay_S_DA_SHANG = "da.shang"; //打赏支付

    //支付描述
    public static final String TJ_PAY_DG = "dgwxfs";//单个微信分身
    public static final String TJ_PAY_ZS = "zswxfs";//终身微信分身
    public static final String TJ_PAY_syyydk = "syyydk";//所有应用多开
    public static final String TJ_PAY_REWARD = "rewardpay";//打赏支付
    public static final String TJ_PAY_HF = "wxhfall";//微信自动回复全部功能
    public static final String TJ_PAY_JHY = "alljhy";//微信批量加好友全部功能
    public static final String TJ_PAY_SVIP = "vipall";//SVIP
    public static final String TJ_PAY_FFH = "wxdkffh"; //微信防封号
    public static final String TJ_PAY_TX_WX = "wxtxscq"; //微信提现生成器
    public static final String TJ_PAY_LQ_WX = "wxlqscq"; //微信零钱生成器
    public static final String TJ_PAY_ZZ_WX = "wxzzscq";//微信转账生成器
    public static final String TJ_PAY_HB_WX = "wxhbscq"; //微信红包生成器
    public static final String TJ_PAY_PYQ_WX = "wxpyqscq"; //微信朋友圈生成器
    public static final String TJ_PAY_LT_WX = "wxltscq"; //微信聊天对话生成器
    public static final String TJ_PAY_QL_WX = "wxqlscq";//微信群聊生成器
    public static final String TJ_PAY_HB_ZFB = "zfbhbscq";//支付宝红包生成器
    public static final String TJ_PAY_TX_ZFB = "zfbtxscq";//支付宝体现生成器
    public static final String TJ_PAY_YE_ZFB = "zfbyescq";//支付宝零钱生成器
    public static final String TJ_PAY_ZZ_ZFB = "zfbzzscq";//支付宝转账生成器
    public static final String TJ_PAY_HB_QQ = "qqhbscq";//QQ红包生成器
    public static final String TJ_PAY_YE_QQ = "qqyescq";//QQ提现生成器
    public static final String TJ_PAY_QB_QQ = "qqqbscq";//QQ零钱生成器
    public static final String TJ_PAY_ZZ_QQ = "qqzzscq";//QQ转账生成器
    /**
     * 获取 功能支付统计描述字符
     * @param functionType
     * @return
     */
     public static String getPayDescribeStr(String functionType){
        String describeStr = "";
        if (functionType.equals(Pay_S_WX_DG)){
            describeStr =  TJ_PAY_DG;
        }else if (functionType.equals(Pay_S_WX_ZS)){
            describeStr =  TJ_PAY_ZS;
        }else if (functionType.equals(Pay_S_ALL_APPS)){
            describeStr =  TJ_PAY_syyydk;
        }else if (functionType.equals(Pay_S_DA_SHANG)){
            describeStr =  TJ_PAY_REWARD;
        }else if (functionType.equals(Pay_S_SVIP_JHY)){
            describeStr =  TJ_PAY_JHY;
        }else if (functionType.equals(Pay_S_SVIP_HF)){
            describeStr =  TJ_PAY_HF;
        }else if (functionType.equals(Pay_S_SVIP)){
            describeStr =  TJ_PAY_SVIP;
        }else if (functionType.equals(Pay_S_SVIP_FFH)){
            describeStr =  TJ_PAY_FFH;
        }else if (functionType.equals(Pay_S_WX_TX)){
            describeStr =  TJ_PAY_TX_WX;
        }else if (functionType.equals(Pay_S_WX_LQ)){
            describeStr =  TJ_PAY_LQ_WX;
        }else if (functionType.equals(Pay_S_WX_ZZ)){
            describeStr =  TJ_PAY_ZZ_WX;
        }else if (functionType.equals(Pay_S_WX_HB)){
            describeStr =  TJ_PAY_HB_WX;
        }else if (functionType.equals(Pay_S_WX_PYQ)){
            describeStr =  TJ_PAY_PYQ_WX;
        }else if (functionType.equals(Pay_S_WX_DH)){
            describeStr =  TJ_PAY_LT_WX;
        }else if (functionType.equals(Pay_S_WX_QL)){
            describeStr =  TJ_PAY_QL_WX;
        }else if (functionType.equals(Pay_S_ZFB_HB)){
            describeStr =  TJ_PAY_HB_ZFB;
        }else if (functionType.equals(Pay_S_ZFB_TX)){
            describeStr =  TJ_PAY_TX_ZFB;
        }else if (functionType.equals(Pay_S_ZFB_YE)){
            describeStr =  TJ_PAY_YE_ZFB;
        }else if (functionType.equals(Pay_S_ZFB_ZZ)){
            describeStr =  TJ_PAY_ZZ_ZFB;
        }else if (functionType.equals(Pay_S_QQ_HB)){
            describeStr =  TJ_PAY_HB_QQ;
        }else if (functionType.equals(Pay_S_QQ_YE)){
            describeStr =  TJ_PAY_YE_QQ;
        }else if (functionType.equals(Pay_S_QQ_QB)){
            describeStr =  TJ_PAY_QB_QQ;
        }else if (functionType.equals(Pay_S_QQ_ZZ)){
            describeStr =  TJ_PAY_ZZ_QQ;
        }
        return describeStr;
    }

}
