package com.zhushou.weichat.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/18.
 */

public class UMengTJUtils {



    private Context mContext;

    public UMengTJUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void onEventValuePay(String EventId,Map<String,String> payMsg,int paymentProcesTime) {
        MobclickAgent.onEventValue(mContext, EventId, payMsg, paymentProcesTime);
    }

    public static Map<String,String> friendMap(String value){
        Map<String, String> map_value = new HashMap<>();
        map_value.put("ID_Time", value); // 支付方式
        return map_value;
    }

    public Map<String,String> getEventValuePayMap(String PayMethod,String orderNumber,String CommodityPrice){
        Map<String, String> map_value = new HashMap<>();
        map_value.put("PayMethod", PayMethod); // 支付方式
        map_value.put("CommodityPrice", CommodityPrice);//商品价格
        map_value.put("orderNumber",orderNumber);//订单号
        return map_value;
    }

    public static String longTime(long value){
        String dateStr = "";
        int ss = 0;
        int mm = 0;
        int hh = 0;
        if (value>=1000)
            ss = (int)value/1000;

        if (ss>=60)
            mm = ss/60;

        if (mm>=60)
            hh = mm/60;

        if (hh>0)
            dateStr+=hh+"小时";
        if (mm>0)
            dateStr+=mm+"分钟";
        if (ss>=0)
            dateStr+=ss+"秒";

        return dateStr;
    }

//    public static String getUmengPaySEventId(String eventKey){
//        String Tjvalue = null;
//        if (eventKey==null)
//            return Tjvalue;
//        if (eventKey.equals(AuxiliaryConstans.C_TIXIAN)){ //微信提现生成器
//            Tjvalue = TJ_PAY_TXSCQ;
//        }
//        if (eventKey.equals(AuxiliaryConstans.C_LINGQIAN)){//微信零钱生成器
//            Tjvalue = TJ_PAY_LQSCQ;
//        }
//        if (eventKey.equals(AuxiliaryConstans.C_HONGBAO)){ //微信红包生成器
//            Tjvalue = TJ_PAY_HBSCQ;
//        }
//        if (eventKey.equals(AuxiliaryConstans.C_PY_QUAN)){//微信朋友圈生成器
//            Tjvalue = TJ_PAY_PYQSCQ;
//        }
//        if (eventKey.equals(AuxiliaryConstans.C_DUIHUA)){//微信聊天对话生成器
//            Tjvalue = TJ_PAY_DHSCQ;
//        }
//    }

}
