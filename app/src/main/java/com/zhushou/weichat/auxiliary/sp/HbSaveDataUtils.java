package com.zhushou.weichat.auxiliary.sp;

import android.content.Context;

import com.zhushou.weichat.auxiliary.info.HbMainMessageInfo;
import com.zhushou.weichat.auxiliary.info.HbRecordInfo;
import com.zhushou.weichat.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class HbSaveDataUtils {

    private Context mContext;
    private String number_key = "number.key";
    private String money_key = "money.key";
    private String HB_Record_Key = "hb.record.key";
    private String HB_USER_ID = "hb.user.id";

    public HbSaveDataUtils(Context mContext){
        this.mContext = mContext;
    }



    public void saveHbUserId(String userid){
        SharedPreferencesUtils.setParam(mContext,HB_USER_ID,userid);
    }
    public String getHbUserId(){
        String userid = (String) SharedPreferencesUtils.getParam(mContext,HB_USER_ID,"");
        if (userid!=null&&!userid.equals("")){
            return userid;
        }else{
            return "";
        }
    }
    /**
     * 增加一次已抢红包数量
     */
    public void saveRedPacketsNumber(){
        String sumMoney = (String) SharedPreferencesUtils.getParam(mContext,number_key,"");
        if (sumMoney!=null&&!sumMoney.equals("")){
            float sumMoneyf = Float.valueOf(sumMoney)+1f;
            SharedPreferencesUtils.setParam(mContext,number_key,String.valueOf(sumMoneyf));
        }else{
            SharedPreferencesUtils.setParam(mContext,number_key,"1");
        }
    }

    /**
     * 保存已抢红包金额
     * @param money
     */
    public void saveRedPacketsMoney(float money){
        String sumMoney = (String) SharedPreferencesUtils.getParam(mContext,money_key,"");
        if (sumMoney!=null&&!sumMoney.equals("")){
            float sumMoneyf = Float.valueOf(sumMoney)+money;
            SharedPreferencesUtils.setParam(mContext,money_key,String.valueOf(sumMoneyf));
        }else{
            SharedPreferencesUtils.setParam(mContext,money_key,String.valueOf(money));
        }
    }

    /**
     * 获取已抢红包金额和数量
     * @return
     */
    public HbMainMessageInfo getRedPacketsNumberAndMoney(){
        HbMainMessageInfo hbMainMessageInfo = new HbMainMessageInfo();
        hbMainMessageInfo.setHbSum(Float.valueOf((String) SharedPreferencesUtils.getParam(mContext,number_key,"0")));
        hbMainMessageInfo.setHbSumMoney(Float.valueOf((String) SharedPreferencesUtils.getParam(mContext,money_key,"0.00")));
        return hbMainMessageInfo;
    }


    /**
     * 保存已抢红包信息
     * @param info
     */
    public void saveHbRecord(HbRecordInfo info){
        String spJa = (String) SharedPreferencesUtils.getParam(mContext,HB_Record_Key,"");
        JSONObject newJo = new JSONObject();
        JSONArray oldJa;
        try {
            newJo.put("boss",info.getBoss());
            newJo.put("money",info.getMoney());
            newJo.put("time",info.getHbTime());
            if (spJa!=null&&!spJa.equals("")){
                JSONArray lastJa = new JSONArray(spJa);
                oldJa = new JSONArray();
                oldJa.put(newJo);
                for (int i=0;i<lastJa.length();i++){
                    JSONObject jo = lastJa.getJSONObject(i);
                    oldJa.put(jo);
                }
            }else{
                oldJa = new JSONArray();
                oldJa.put(newJo);
            }
            SharedPreferencesUtils.setParam(mContext,HB_Record_Key,oldJa.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取已抢红包记录
     * @return
     */
    public List<HbRecordInfo> getHbRecord(){
        List<HbRecordInfo> hbRecordInfos = new ArrayList<>();
        String spJa = (String) SharedPreferencesUtils.getParam(mContext,HB_Record_Key,"");
        JSONArray oldJa;
        try {
            if (spJa!=null&&!spJa.equals("")){
                oldJa = new JSONArray(spJa);
                for (int i=0;i<=oldJa.length();i++){
                    JSONObject jo = oldJa.getJSONObject(i);
                    HbRecordInfo hbRecordInfo = new HbRecordInfo();
                    hbRecordInfo.setBoss(jo.getString("boss"));
                    hbRecordInfo.setHbTime(jo.getString("time"));
                    hbRecordInfo.setMoney(jo.getString("money"));
                    hbRecordInfos.add(hbRecordInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hbRecordInfos;
    }

}
