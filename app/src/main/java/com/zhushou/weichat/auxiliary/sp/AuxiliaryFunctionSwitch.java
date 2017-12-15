package com.zhushou.weichat.auxiliary.sp;

import android.content.Context;

import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.HistoryPayInfo;
import com.zhushou.weichat.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/16.
 */

public class AuxiliaryFunctionSwitch {

    public static final String FuncitonSwitchKey = "auxiliary.function.switch";
    public static final String LeavingMessageKey = "leaving.message";

    private Context mContext;
    public AuxiliaryFunctionSwitch(Context context){
        this.mContext = context;
    }


    public static class FunctionWeitchInfo{
        private String functionkey;
        private boolean functionStatus;
        public String getFunctionkey() {
            return functionkey;
        }
        public void setFunctionkey(String functionkey) {
            this.functionkey = functionkey;
        }
        public boolean isFunctionStatus() {
            return functionStatus;
        }
        public void setFunctionStatus(boolean functionStatus) {
            this.functionStatus = functionStatus;
        }
    }
//
//    /**
//     *
//     * @return
//     */
//    public  List<FunctionWeitchInfo> getFunctionSwitchList(){
//        List<FunctionWeitchInfo> listData = new ArrayList<>();
//        String historyPayStr = (String) SharedPreferencesUtils.getParam(mContext,FuncitonSwitchKey,"");
//        if (historyPayStr!=null&&!historyPayStr.equals("")){
//            try {
//                JSONArray ja = new JSONArray(historyPayStr);
//                for (int i=0;i<ja.length();i++) {
//                    JSONObject jo = ja.getJSONObject(i);
//                    FunctionWeitchInfo info = new FunctionWeitchInfo();
//                    info.setFunctionkey(jo.getString("functionkey"));
//                    info.setFunctionStatus(jo.getBoolean("functionStatus"));
//                    listData.add(info);
//                }
//            } catch (JSONException e){
//                e.printStackTrace();
//            }
//        }
//        return listData;
//    }


    /**
     * 保存 开关状态
     * @param functionKey
     * @param functionStatu
     */
    public void setFuncitonSwitchInfo(String functionKey,boolean functionStatu){
        AuxiliaryFunctionSwitch.FunctionWeitchInfo functionInfo = new AuxiliaryFunctionSwitch.FunctionWeitchInfo();
        functionInfo.setFunctionkey(functionKey);
        functionInfo.setFunctionStatus(functionStatu);
        setFunctionSwitchInfo(functionInfo);
    }

    /**
     * 功能开关状态 写入本地
     * @param info
     */
    public  void setFunctionSwitchInfo(FunctionWeitchInfo info){
        String historyPayStr = (String) SharedPreferencesUtils.getParam(mContext,FuncitonSwitchKey,"");
        JSONArray newJa = null;
        if (historyPayStr!=null&&!historyPayStr.equals("")){
            try {
                newJa = new JSONArray();
                JSONArray oldJa= new JSONArray(historyPayStr);
                boolean checkIsNewInfo = true;
                for (int i=0;i<oldJa.length();i++){
                    JSONObject jo = oldJa.getJSONObject(i);
                    if (jo.getString("functionkey").equals(info.getFunctionkey())){
                        JSONObject itemJo = new JSONObject();
                        itemJo.put("functionkey",info.getFunctionkey());
                        itemJo.put("functionStatus",info.isFunctionStatus());
                        newJa.put(itemJo);
                        checkIsNewInfo = false;
                    }else{
                        newJa.put(jo);
                    }
                }
                if (checkIsNewInfo){
                    JSONObject itemJo = new JSONObject();
                    itemJo.put("functionkey",info.getFunctionkey());
                    itemJo.put("functionStatus",info.isFunctionStatus());
                    newJa.put(itemJo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            newJa = new JSONArray();
            try {
                JSONObject newJo = new JSONObject();
                newJo.put("functionkey",info.getFunctionkey());
                newJo.put("functionStatus",info.isFunctionStatus());
                newJa.put(newJo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SharedPreferencesUtils.setParam(mContext,FuncitonSwitchKey,newJa.toString());
    }


    /**
     * 检查功能开关状态
     * @param functionKey
     * @return
     */
    public  boolean checkFunctionSwitchStatus(String functionKey){
        boolean resultBooelan = false;
        String historyPayStr = (String) SharedPreferencesUtils.getParam(mContext,FuncitonSwitchKey,"");
        if (historyPayStr!=null&&!historyPayStr.equals("")){
            try {
                JSONArray ja = new JSONArray(historyPayStr);
                for (int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    if (jo.getString("functionkey").equals(functionKey)){
                        resultBooelan = jo.getBoolean("functionStatus");
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            FunctionWeitchInfo info = new FunctionWeitchInfo();
            info.setFunctionkey(functionKey);
            info.setFunctionStatus(false);
            setFunctionSwitchInfo(info);
        }
        return resultBooelan;
    }


    /**
     * 自动回复内容保存
     * @param leavingMessage
     */
    public void saveLeavingMessage(String leavingMessage){
        if (leavingMessage!=null&&!leavingMessage.equals(""))
            SharedPreferencesUtils.setParam(mContext,LeavingMessageKey,leavingMessage);
    }

    /**
     *自动回复内容获取
     * @return
     */
    public String getLeavingMessage(){
        String historyPayStr = (String) SharedPreferencesUtils.getParam(mContext,LeavingMessageKey,"");
        if (historyPayStr!=null&&!historyPayStr.equals("")){
            return historyPayStr;
        }else{
            return mContext.getResources().getString(R.string.weichat_hf_content);
        }
    }

}
