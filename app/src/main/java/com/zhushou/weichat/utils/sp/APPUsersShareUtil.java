package com.zhushou.weichat.utils.sp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.lody.virtual.helper.utils.VLog;
import com.zhushou.weichat.bean.AppUsersInfo;
import com.zhushou.weichat.bean.ScreenShotPreview;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.bean.VipBaoYueInfo;
import com.zhushou.weichat.utils.CountUtil;
import com.zhushou.weichat.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/15.
 */

public class APPUsersShareUtil {

    public static final String USERS_SHARE_KEY = "users.share.key";
    public static final String ITEM_APPS_ARRAY = "item.apps.array";
    private static final String TAG = "APPUsersShareUtil";


    /**
     * 所有应用多开 获取对应包名分身信息
     * @param context
     * @param packagename 包名
     * @return
     */
    public static List<AppUsersInfo> getItemApps(Context context,String packagename){
        List<AppUsersInfo> dataArray = new ArrayList<>();
        String str = (String) SharedPreferencesUtils.getParam(context,ITEM_APPS_ARRAY,"");
        if (str!=null&&!str.equals("")){
            try {
                JSONArray ja = new JSONArray(str);
                for (int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    if (jo.getString("packageName").equals(packagename)){
                        AppUsersInfo info = new AppUsersInfo();
                        info.setAppName(jo.getString("apkName"));
                        info.setId(jo.getString("id"));
                        info.setAppType(jo.getInt("appType"));
                        info.setDate(jo.getString("date"));
                        info.setPrice(jo.getString("price"));
                        info.setCreateTiem(jo.getLong("createTiem"));
                        info.setFreeTrialType(jo.getBoolean("freeTrialType"));
                        info.setPackageName(jo.getString("packageName"));
                        info.setCreateTimeIdCard(jo.getString("createTimeIdCard"));
                        dataArray.add(info);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataArray;
    }

    /**
     * 所有应用多开 修改分身信息
     * @param context
     * @param CreateTimeIdCard
     * @param name
     */
    public static void updateItemAppsName(Context context,String CreateTimeIdCard,String name){
        String str = (String) SharedPreferencesUtils.getParam(context,ITEM_APPS_ARRAY,"");
        JSONArray newJa = new JSONArray();
        if (str!=null&&!str.equals("")){
            try {
                JSONArray ja = new JSONArray(str);
                for (int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    if (jo.getString("createTimeIdCard").equals(CreateTimeIdCard)){
                        JSONObject newjo = new JSONObject();
                        newjo.put("apkName",name);
                        newjo.put("id",jo.getString("id"));
                        newjo.put("appType",jo.getInt("appType"));
                        newjo.put("date",jo.getString("date"));
                        newjo.put("price",jo.getString("price"));
                        newjo.put("createTiem",jo.getLong("createTiem"));
                        newjo.put("freeTrialType",jo.getBoolean("freeTrialType"));
                        newjo.put("packageName",jo.getString("packageName"));
                        newjo.put("createTimeIdCard",jo.getString("createTimeIdCard"));
                        newJa.put(newjo);
                    }else{
                        newJa.put(jo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SharedPreferencesUtils.setParam(context,ITEM_APPS_ARRAY,newJa.toString());
    }

    /**
     * 所有应用多开 删除分身
     * @param context
     * @param itemIdCard 每个分身 独立编号
     */
    public static List<AppUsersInfo> delItemApps(Context context,String itemIdCard){
        String str = (String) SharedPreferencesUtils.getParam(context,ITEM_APPS_ARRAY,"");
        JSONArray newJa = new JSONArray();
        List<AppUsersInfo> appsArray = new ArrayList<>();
        if (str!=null&&!str.equals("")){
            try {
                JSONArray ja = new JSONArray(str);
                for (int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    if (!jo.getString("createTimeIdCard").equals(itemIdCard)){
                        AppUsersInfo info = new AppUsersInfo();
                        newJa.put(jo);
                        info.setAppName(jo.getString("apkName"));
                        info.setId(jo.getString("id"));
                        info.setAppType(jo.getInt("appType"));
                        info.setDate(jo.getString("date"));
                        info.setPrice(jo.getString("price"));
                        info.setCreateTiem(jo.getLong("createTiem"));
                        info.setFreeTrialType(jo.getBoolean("freeTrialType"));
                        info.setPackageName(jo.getString("packageName"));
                        info.setCreateTimeIdCard(jo.getString("createTimeIdCard"));
                        appsArray.add(info);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return appsArray;
    }

    /**
     * 所有应用多开 添加应用
     * @param context
     * @param userInfo
     */
    public static void setItemApps(Context context,AppUsersInfo userInfo){
        String str = (String) SharedPreferencesUtils.getParam(context,ITEM_APPS_ARRAY,"");
        JSONArray newJa = new JSONArray();
        if (str!=null&&!str.equals("")){
            try {
                JSONArray ja = new JSONArray(str);
                for (int i=0;i<ja.length();i++){
                    newJa.put(ja.getJSONObject(i));
                }
                JSONObject newJo = new JSONObject();
                newJo.put("apkName",userInfo.getAppName());
                newJo.put("id",userInfo.getId());
                newJo.put("appType",userInfo.getAppType());
                newJo.put("date",userInfo.getDate());
                newJo.put("price",userInfo.getPrice());
                newJo.put("createTiem",userInfo.getCreateTiem());
                newJo.put("freeTrialType",userInfo.isFreeTrialType());
                newJo.put("packageName",userInfo.getPackageName());
                newJo.put("createTimeIdCard",userInfo.getCreateTimeIdCard());
                newJa.put(newJo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                JSONObject newJo = new JSONObject();
                newJo.put("apkName",userInfo.getAppName());
                newJo.put("id",userInfo.getId());
                newJo.put("appType",userInfo.getAppType());
                newJo.put("date",userInfo.getDate());
                newJo.put("price",userInfo.getPrice());
                newJo.put("createTiem",userInfo.getCreateTiem());
                newJo.put("freeTrialType",userInfo.isFreeTrialType());
                newJo.put("packageName",userInfo.getPackageName());
                newJo.put("createTimeIdCard",userInfo.getCreateTimeIdCard());
                newJa.put(newJo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SharedPreferencesUtils.setParam(context,ITEM_APPS_ARRAY,newJa.toString());
    }

    /**
     * 获取用户列表
     * @return List<AppUsersInfo>
     */
    public static List<AppUsersInfo> getUserList(Context context){
        List<AppUsersInfo> listData = new ArrayList<>();
        String historyPayStr = (String) SharedPreferencesUtils.getParam(context,USERS_SHARE_KEY,"");
        if (historyPayStr!=null&&!historyPayStr.equals("")){
            try {
                JSONArray ja = new JSONArray(historyPayStr);
                for (int i=0;i<ja.length();i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    AppUsersInfo info = new AppUsersInfo();
                    info.setAppName(jo.getString("apkName"));
                    info.setId(jo.getString("id"));
                    info.setAppType(jo.getInt("appType"));
                    info.setDate(jo.getString("date"));
                    info.setPrice(jo.getString("price"));
                    info.setFreeTrialType(jo.getBoolean("freeTrialType"));
                    info.setCreateTiem(jo.getLong("createTiem"));
                    info.setPackageName(jo.getString("packageName"));
                    listData.add(info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }

    /**
     * 记录新建用户信息
     * @param
     */
    public static void setUsersList(Context context,AppUsersInfo userInfo){
        String historyPayStr = (String) SharedPreferencesUtils.getParam(context,USERS_SHARE_KEY,"");
        JSONArray newJa = new JSONArray();
        if (historyPayStr!=null&&!historyPayStr.equals("")){
            try {
                JSONArray ja = new JSONArray(historyPayStr);
                for (int i=0;i<ja.length();i++){
                    newJa.put(ja.getJSONObject(i));
                }
                JSONObject newJo = new JSONObject();
                newJo.put("apkName",userInfo.getAppName());
                newJo.put("id",userInfo.getId());
                newJo.put("appType",userInfo.getAppType());
                newJo.put("date",userInfo.getDate());
                newJo.put("price",userInfo.getPrice());
                newJo.put("createTiem",userInfo.getCreateTiem());
                newJo.put("freeTrialType",userInfo.isFreeTrialType());
                newJo.put("packageName",userInfo.getPackageName());
                newJa.put(newJo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                JSONObject newJo = new JSONObject();
                newJo.put("apkName",userInfo.getAppName());
                newJo.put("id",userInfo.getId());
                newJo.put("appType",userInfo.getAppType());
                newJo.put("date",userInfo.getDate());
                newJo.put("price",userInfo.getPrice());
                newJo.put("createTiem",userInfo.getCreateTiem());
                newJo.put("freeTrialType",userInfo.isFreeTrialType());
                newJo.put("packageName",userInfo.getPackageName());
                newJa.put(newJo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SharedPreferencesUtils.setParam(context,USERS_SHARE_KEY,newJa.toString());
    }

    /**
     * 修改用户信息
     * @param userId
     * @param newName
     */
    public static void  changeUsersInfo(Context context,int  userId,String newName,String pacageName){
        List<AppUsersInfo> oldListData = getUserList(context);
        for (int i=0;i<oldListData.size();i++){
            AppUsersInfo info = oldListData.get(i);
            if (userId == Integer.valueOf(info.getId())&&pacageName.equals(info.getPackageName())){
                oldListData.get(i).setAppName(newName);
            }
        }
        Log.i(TAG, "changeUsersInfo: "+"");
        JSONArray newJa = new JSONArray();
        try {
            for (AppUsersInfo info:oldListData){
                JSONObject jo = new JSONObject();
                jo.put("apkName",info.getAppName());
                jo.put("id",info.getId());
                jo.put("appType",info.getAppType());
                jo.put("date",info.getDate());
                jo.put("price",info.getPrice());
                jo.put("createTiem",info.getCreateTiem());
                jo.put("freeTrialType",info.isFreeTrialType());
                jo.put("packageName",info.getPackageName());
                newJa.put(jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferencesUtils.setParam(context,USERS_SHARE_KEY,newJa.toString());
    }

    /**
     * 删除用户
     * @param context
     * @param userId
     */
    public static void deleteUser(Context context,int  userId){
        List<AppUsersInfo> oldListData = getUserList(context);
        for (int i=0;i<oldListData.size();i++){
            AppUsersInfo info = oldListData.get(i);
            if (info!=null&&userId == Integer.valueOf(info.getId())){
                oldListData.remove(i);
            }
        }
        Log.i(TAG, "changeUsersInfo: "+"");
        JSONArray newJa = new JSONArray();
        try {
            for (AppUsersInfo info:oldListData){
                JSONObject jo = new JSONObject();
                jo.put("apkName",info.getAppName());
                jo.put("id",info.getId());
                jo.put("appType",info.getAppType());
                jo.put("date",info.getDate());
                jo.put("price",info.getPrice());
                jo.put("createTiem",info.getCreateTiem());
                jo.put("freeTrialType",info.isFreeTrialType());
                jo.put("",info.getPackageName());
                newJa.put(jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferencesUtils.setParam(context,USERS_SHARE_KEY,newJa.toString());
    }

    /**
     * 删除所有该包名用户
     *
     * @param context
     */
    public static boolean deleteUserAll(Context context,String packageName) {
        try {
            List<AppUsersInfo> oldListData = getUserList(context);
            AppUsersInfo deleteInfo = null;
            for (int i = 0; i < oldListData.size(); i++) {
                deleteInfo =  oldListData.get(i);
                if (deleteInfo!=null&&packageName.equals(deleteInfo.getPackageName())) {
                    oldListData.remove(i);
                }
            }
            VLog.i("jyh", "deleteUserAll : " + oldListData.toString());
            JSONArray newJa = new JSONArray();
            for (AppUsersInfo info : oldListData) {
                JSONObject jo = new JSONObject();
                jo.put("apkName",info.getAppName());
                jo.put("id",info.getId());
                jo.put("appType",info.getAppType());
                jo.put("date",info.getDate());
                jo.put("price",info.getPrice());
                jo.put("createTiem",info.getCreateTiem());
                jo.put("freeTrialType",info.isFreeTrialType());
                jo.put("pacageName",info.getPackageName());
                newJa.put(jo);
            }
            SharedPreferencesUtils.setParam(context, USERS_SHARE_KEY, newJa.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断是否是第一次登陆
     * @param context
     * @return
     */
    public static boolean isFirstOpen(Context context){
        String value = (String) SharedPreferencesUtils.getParam(context,"first.open","");
        if (value!=null&&!value.equals("")){
            return false;
        }else{
            SharedPreferencesUtils.setParam(context,"first.open",String.valueOf(System.currentTimeMillis()));
            return true;
        }
    }



    // 包月会员操作

    /**
     * 购买包月 后信息记录储存
     * @param context
     * @param price
     * @param payType
     */
    public static void setBaoYue(Context context,String price,String payType){
        long startDate = System.currentTimeMillis();
        long endDate = startDate+((long)(30*24*60*60)*1000);
//        long endDate = startDate+((long)(5*60)*1000);
        JSONArray ja = null;
        String baoyueJsonStr = (String) SharedPreferencesUtils.getParam(context,"vip.baoyue","");
        if (baoyueJsonStr!=null&&!baoyueJsonStr.equals("")){
            try {
                ja = new JSONArray(baoyueJsonStr);
                JSONObject jo = new JSONObject();
                jo.put("isFirst",false);
                jo.put("startDate",startDate);
                jo.put("endDate",endDate);
                jo.put("price",price);
                jo.put("payType",payType);
                ja.put(jo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                ja = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("isFirst",true);
                jo.put("startDate",startDate);
                jo.put("endDate",endDate);
                jo.put("price",price);
                jo.put("payType",payType);
                ja.put(jo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (ja!=null){
            SharedPreferencesUtils.setParam(context,"vip.baoyue",ja.toString());
        }
    }

    /**
     * 获取包月记录
     * @param context
     * @return
     */
    public static List<VipBaoYueInfo> getBaoyueMessage(Context context){
        List<VipBaoYueInfo> listData = new ArrayList<>();
        String vipJsonStr = (String) SharedPreferencesUtils.getParam(context,"vip.baoyue","");
        if (vipJsonStr!=null&&!vipJsonStr.equals("")){
            try {
                JSONArray ja = new JSONArray(vipJsonStr);
                for (int i=0;i<ja.length();i++){
                    VipBaoYueInfo info = new VipBaoYueInfo();
                    JSONObject jo = ja.getJSONObject(i);
                    info.setFirst(jo.getBoolean("isFirst"));
                    info.setEndDate(jo.getLong("endDate"));
                    info.setStartDate(jo.getLong("startDate"));
                    info.setPayType(jo.getString("payType"));
                    info.setPrice(jo.getString("price"));
                    listData.add(info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }

    /**
     * 检查是否是首冲包月
     * @return
     */
    public static boolean checkIsFirst(Context context){
        String vipJsonStr = (String) SharedPreferencesUtils.getParam(context,"vip.baoyue","");
        if (vipJsonStr!=null&&!vipJsonStr.equals("")){
            return true;
        }
        return false;
    }

    /**
     * 检查是否需要充值会员包月
     * @param context
     * @return
     */
    public static boolean checkVip(Context context){
        long thisTime = System.currentTimeMillis();
        boolean NeedToPay = true;
        String vipJsonStr = (String) SharedPreferencesUtils.getParam(context,"vip.baoyue","");
        if (vipJsonStr!=null&&!vipJsonStr.equals("")){
            try {
                JSONArray ja = new JSONArray(vipJsonStr);
                for (int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    jo.getLong("endDate");
                    if (jo.getLong("endDate")>thisTime){
                        NeedToPay = false;
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return NeedToPay;
    }

    /**
     * 同步服务器盒子数据
     * @param context
     * @param updateInfo
     */
    public static void setUpdateMessage(Context context,UpdatePriceInfo updateInfo){
        JSONObject jo = new JSONObject();
        try {
            jo.put("versionCode",updateInfo.getVersionCode());
            jo.put("appId",updateInfo.getAppId());
            jo.put("ad_length",updateInfo.getAd_length());
            jo.put("discount",updateInfo.getDiscount());
            jo.put("tuia_adv_isbrowser",updateInfo.isTuia_adv_isbrowser());
            jo.put("kfqq",updateInfo.getKfqq());
            jo.put("kfwx",updateInfo.getKfwx());
            jo.put("kfdh",updateInfo.getKfdh());
            jo.put("top_tip",updateInfo.getTop_tip());
            jo.put("sysj",updateInfo.getSysj());
            jo.put("scfy",updateInfo.getScfy());
            jo.put("zcfy",updateInfo.getZcfy());
            jo.put("fxsj",updateInfo.getShareTime());
            jo.put("mfsj",updateInfo.getMfSj());
            jo.put("fxcs",updateInfo.getFxcs());
            jo.put("all_apps_price",updateInfo.getAllAppsPrice());
            jo.put("shareaddress",updateInfo.getShareaddress());
            jo.put("left_button_url",updateInfo.getLeft_button_url());
//            jo.put("weichat_hb_price",updateInfo.getWeichat_hb_price());
//            jo.put("weichat_xphf_price",updateInfo.getWeichat_xphf_price());
//            jo.put("weichat_hfnr_price",updateInfo.getWeichat_hfnr_price());
//            jo.put("weichat_yshb_price",updateInfo.getWeichat_yshb_price());
//            jo.put("weichat_xphb_price",updateInfo.getWeichat_xphb_price());
            jo.put("weichat_hf_all_price",updateInfo.getWeichat_hf_all_price());
            jo.put("weichat_hf_dg_price",updateInfo.getWeichat_hf_dg_price());
            jo.put("weichat_jt_all_price",updateInfo.getWeichat_jt_all_price());
            jo.put("weichat_jt_dg_price",updateInfo.getWeichat_jt_dg_price());
            jo.put("weichat_jf_all_price",updateInfo.getWeichat_jf_all_price());
            jo.put("weichat_jf_dg_price",updateInfo.getWeichat_jf_dg_price());
            jo.put("weichat_version_code",updateInfo.getWeichat_version_code());
            jo.put("weichat_qgg_price",updateInfo.getWeichat_qgg_price());
            jo.put("weichat_ffh_price",updateInfo.getWeichat_ffh_price());
            jo.put("weichat_svip",updateInfo.getWeichat_svip_price());
            jo.put("id_hb",updateInfo.getId_hb());
            jo.put("id_chb",updateInfo.getId_chb());
            jo.put("id_hf_input",updateInfo.getId_hf_input());
            jo.put("id_hf_sendbtn",updateInfo.getId_hf_sendbtn());
            jo.put("id_hb_btn_ly",updateInfo.getId_hb_btn_ly());
            jo.put("id_hb_input_ly",updateInfo.getId_hb_input_ly());
            jo.put("id_hb_send_ly",updateInfo.getId_hb_send_ly());
            jo.put("id_hb_detail_back",updateInfo.getId_hb_detail_back());
            jo.put("id_hb_grab_money",updateInfo.getId_hb_grab_money());
            jo.put("id_hb_boss_name",updateInfo.getId_hb_boss_name());
            jo.put("id_chat_item_id",updateInfo.getId_chat_item_id());
            jo.put("pay_status_wx",updateInfo.isPay_status_wx());
            jo.put("pay_status_ali",updateInfo.isPay_status_ali());
            jo.put("pay_status_qq",updateInfo.isPay_status_qq());

            jo.put("ffh",updateInfo.isFfh());
            jo.put("wxqhb",updateInfo.isWxqhb());
            jo.put("shouqi",updateInfo.isShouqi());
            jo.put("dabao",updateInfo.isDabao());
            jo.put("jiasudu",updateInfo.isJiasudu());
            jo.put("guanrao",updateInfo.isGuanrao());
            jo.put("xiaobao",updateInfo.isXiaobao());
            jo.put("qgg",updateInfo.isQgg());
            jo.put("jhy",updateInfo.isJhy());
            jo.put("zdhf",updateInfo.isZdhf());

            jo.put("pc_svip",updateInfo.getPc_svip());
            jo.put("pc_shouqi",updateInfo.getPc_shouqi());
            jo.put("pc_dabao",updateInfo.getPc_dabao());
            jo.put("pc_xiaobao",updateInfo.getPc_xiaobao());
            jo.put("pc_jiasudu",updateInfo.getPc_jiasudu());
            jo.put("pc_ganrao",updateInfo.getPc_ganrao());
            jo.put("pc_xpqhb",updateInfo.getPc_xpqhb());
            jo.put("pc_zddx",updateInfo.getPc_zddx());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferencesUtils.setParam(context,"update.message",jo.toString());
    }

    /**
     * 从本地获取服务器盒子数据
     * @param context
     * @return
     */
    public static UpdatePriceInfo getUpdateMessage(Context context){
        UpdatePriceInfo updateInfo = new UpdatePriceInfo();
        String updateJsonStr = (String) SharedPreferencesUtils.getParam(context,"update.message","");
        if (updateJsonStr!=null&&!updateJsonStr.equals("")){
            try {
                JSONObject jo = new JSONObject(updateJsonStr);
                updateInfo.setVersionCode(jo.getString("versionCode"));
                updateInfo.setAppId(jo.getString("appId"));
                updateInfo.setAd_length(jo.getInt("ad_length"));
                updateInfo.setDiscount(jo.getString("discount"));
                updateInfo.setTuia_adv_isbrowser(jo.getBoolean("tuia_adv_isbrowser"));
                updateInfo.setKfqq(jo.getString("kfqq"));
                updateInfo.setKfwx(jo.getString("kfwx"));
                updateInfo.setKfdh(jo.getString("kfdh"));
                updateInfo.setTop_tip(jo.getString("top_tip"));
                updateInfo.setSysj(jo.getString("sysj"));
                updateInfo.setScfy(jo.getString("scfy"));
                updateInfo.setZcfy(jo.getString("zcfy"));
                updateInfo.setShareTime(jo.getString("fxsj"));
                updateInfo.setMfSj(jo.getString("mfsj"));
                updateInfo.setFxcs(jo.getString("fxcs"));
                updateInfo.setAllAppsPrice(jo.getString("all_apps_price"));
                updateInfo.setShareaddress(jo.getString("shareaddress"));
                updateInfo.setLeft_button_url(jo.getString("left_button_url"));
//                updateInfo.setWeichat_hb_price(jo.getString("weichat_hb_price"));
//                updateInfo.setWeichat_xphf_price(jo.getString("weichat_xphf_price"));
//                updateInfo.setWeichat_hfnr_price(jo.getString("weichat_hfnr_price"));
//                updateInfo.setWeichat_yshb_price(jo.getString("weichat_yshb_price"));
//                updateInfo.setWeichat_xphb_price(jo.getString("weichat_xphb_price"));
                updateInfo.setWeichat_hf_all_price(jo.getString("weichat_hf_all_price"));
                updateInfo.setWeichat_hf_dg_price(jo.getString("weichat_hf_dg_price"));
                updateInfo.setWeichat_jt_all_price(jo.getString("weichat_jt_all_price"));
                updateInfo.setWeichat_jt_dg_price(jo.getString("weichat_jt_dg_price"));
                updateInfo.setWeichat_jf_all_price(jo.getString("weichat_jf_all_price"));
                updateInfo.setWeichat_jf_dg_price(jo.getString("weichat_jf_dg_price"));
                updateInfo.setWeichat_version_code(jo.getString("weichat_version_code"));
                updateInfo.setWeichat_qgg_price(jo.getString("weichat_qgg_price"));
                updateInfo.setWeichat_ffh_price(jo.getString("weichat_ffh_price"));
                updateInfo.setWeichat_svip_price(jo.getString("weichat_svip"));
                updateInfo.setId_hb(jo.getString("id_hb"));
                updateInfo.setId_chb(jo.getString("id_chb"));
                updateInfo.setId_hf_input(jo.getString("id_hf_input"));
                updateInfo.setId_hf_sendbtn(jo.getString("id_hf_sendbtn"));
                updateInfo.setId_hb_btn_ly(jo.getString("id_hb_btn_ly"));
                updateInfo.setId_hb_input_ly(jo.getString("id_hb_input_ly"));
                updateInfo.setId_hb_send_ly(jo.getString("id_hb_send_ly"));
                updateInfo.setId_hb_detail_back(jo.getString("id_hb_detail_back"));
                updateInfo.setId_hb_grab_money(jo.getString("id_hb_grab_money"));
                updateInfo.setId_hb_boss_name(jo.getString("id_hb_boss_name"));
                updateInfo.setId_chat_item_id(jo.getString("id_chat_item_id"));
                updateInfo.setPay_status_wx(jo.getBoolean("pay_status_wx"));
                updateInfo.setPay_status_ali(jo.getBoolean("pay_status_ali"));
                updateInfo.setPay_status_qq(jo.getBoolean("pay_status_qq"));

                updateInfo.setFfh(jo.getBoolean("ffh"));
                updateInfo.setWxqhb(jo.getBoolean("wxqhb"));
                updateInfo.setShouqi(jo.getBoolean("shouqi"));
                updateInfo.setDabao(jo.getBoolean("dabao"));
                updateInfo.setJiasudu(jo.getBoolean("jiasudu"));
                updateInfo.setGuanrao(jo.getBoolean("guanrao"));
                updateInfo.setXiaobao(jo.getBoolean("xiaobao"));
                updateInfo.setQgg(jo.getBoolean("qgg"));
                updateInfo.setJhy(jo.getBoolean("jhy"));
                updateInfo.setZdhf(jo.getBoolean("zdhf"));


                updateInfo.setPc_svip(jo.getString("pc_svip"));
                updateInfo.setPc_shouqi(jo.getString("pc_shouqi"));
                updateInfo.setPc_dabao(jo.getString("pc_dabao"));
                updateInfo.setPc_xiaobao(jo.getString("pc_xiaobao"));
                updateInfo.setPc_jiasudu(jo.getString("pc_jiasudu"));
                updateInfo.setPc_ganrao(jo.getString("pc_ganrao"));
                updateInfo.setPc_xpqhb(jo.getString("pc_xpqhb"));
                updateInfo.setPc_zddx(jo.getString("pc_zddx"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return updateInfo;
    }

    public static UpdatePriceInfo analysisAppBaseJson(String jsonStr, Activity mActivity) throws JSONException {
        CountUtil countUtil = new CountUtil(mActivity);
        JSONObject jo = new JSONObject(jsonStr);
        UpdatePriceInfo updateInfo = new UpdatePriceInfo();
        if (jo != null) {
            UpdatePriceInfo oldUpdateInfo = getUpdateMessage(mActivity);
//                        String addTime = oldUpdateInfo.getSysj()!=null? oldUpdateInfo.getSysj():"0";

            updateInfo.setVersionCode(jo.getString("versionCode"));
            updateInfo.setAppId(jo.getString("appId"));
            updateInfo.setAd_length(jo.getInt("ad_length"));
            updateInfo.setDiscount(jo.getString("discount"));
            updateInfo.setTuia_adv_isbrowser(jo.getBoolean("tuia_adv_isbrowser"));
            updateInfo.setKfqq(jo.getString("kfqq"));
            updateInfo.setKfwx(jo.getString("kfwx"));
            updateInfo.setKfdh(jo.getString("kfdh"));
            updateInfo.setTop_tip(jo.getString("top_tip"));
            updateInfo.setShareTime(jo.getString("fxsj"));
            updateInfo.setShareaddress(jo.getString("shareaddress"));
            updateInfo.setFxcs(jo.getString("fxcs"));


            //保存截图功能预览图片信息
            JSONArray previewJa = jo.getJSONArray("screenshot_preview");
            APPUsersShareUtil.setScreenShotPreviewArray(mActivity, previewJa.toString());


            JSONObject fzJson = jo.getJSONObject("weichat_click_id");
            updateInfo.setWeichat_version_code(fzJson.getString("weichat_version_code"));
            updateInfo.setId_hb(fzJson.getString("id_hb"));
            updateInfo.setId_chb(fzJson.getString("id_chb"));
            updateInfo.setId_hf_input(fzJson.getString("id_hf_input"));
            updateInfo.setId_hf_sendbtn(fzJson.getString("id_hf_sendbtn"));
            updateInfo.setId_hb_btn_ly(fzJson.getString("id_hb_btn_ly"));//废除
            updateInfo.setId_hb_input_ly(fzJson.getString("id_hb_input_ly"));//废除
            updateInfo.setId_hb_send_ly(fzJson.getString("id_hb_send_ly"));//废除
            updateInfo.setId_hb_detail_back(fzJson.getString("id_hb_detail_back"));
            updateInfo.setId_hb_grab_money(fzJson.getString("id_hb_grab_money"));
            updateInfo.setId_hb_boss_name(fzJson.getString("id_hb_boss_name"));
            updateInfo.setId_chat_item_id(fzJson.getString("id_chat_item_id"));//微信聊天界面Item ID

            //ui隐藏控制
            JSONObject ycJson = jo.getJSONObject("wxhb_visiblility");
            updateInfo.setFfh(ycJson.getBoolean("ffh"));
            updateInfo.setWxqhb(ycJson.getBoolean("wxqhb"));
            updateInfo.setShouqi(ycJson.getBoolean("shouqi"));
            updateInfo.setDabao(ycJson.getBoolean("dabao"));
            updateInfo.setJiasudu(ycJson.getBoolean("jiasudu"));
            updateInfo.setGuanrao(ycJson.getBoolean("guanrao"));
            updateInfo.setXiaobao(ycJson.getBoolean("xiaobao"));
            updateInfo.setQgg(ycJson.getBoolean("qgg"));
            updateInfo.setJhy(ycJson.getBoolean("jhy"));
            updateInfo.setZdhf(ycJson.getBoolean("zdhf"));

            JSONObject wxPayStatusJo = jo.getJSONObject("pay_status_wx");
            updateInfo.setPay_status_wx(wxPayStatusJo.getBoolean(countUtil.getApp()));
            JSONObject aliPayStatusJo = jo.getJSONObject("pay_status_ali");
            updateInfo.setPay_status_ali(aliPayStatusJo.getBoolean(countUtil.getApp()));
            JSONObject qqPayStatusJo = jo.getJSONObject("pay_status_qq");
            updateInfo.setPay_status_qq(qqPayStatusJo.getBoolean(countUtil.getApp()));

            //红包功能点价格
            JSONObject pcJson = jo.getJSONObject("wxqhb_function_price");
            updateInfo.setPc_svip(pcJson.getString("pc_svip"));
            updateInfo.setPc_shouqi(pcJson.getString("pc_shouqi"));
            updateInfo.setPc_dabao(pcJson.getString("pc_dabao"));
            updateInfo.setPc_xiaobao(pcJson.getString("pc_xiaobao"));
            updateInfo.setPc_jiasudu(pcJson.getString("pc_jiasudu"));
            updateInfo.setPc_ganrao(pcJson.getString("pc_ganrao"));
            updateInfo.setPc_xpqhb(pcJson.getString("pc_xpqhb"));
            updateInfo.setPc_zddx(pcJson.getString("pc_zddx"));

            updateInfo.setSysj(jo.getString("sysj"));
            updateInfo.setScfy(jo.getString("scfy"));
            updateInfo.setZcfy(jo.getString("zcfy"));
            updateInfo.setAllAppsPrice(jo.getString("all_apps_price"));
            updateInfo.setWeichat_hf_all_price(jo.getString("weichat_hf_all_price"));
            updateInfo.setWeichat_hf_dg_price(jo.getString("weichat_hf_dg_price"));
            updateInfo.setWeichat_jt_all_price(jo.getString("weichat_jt_all_price"));
            updateInfo.setWeichat_jt_dg_price(jo.getString("weichat_jt_dg_price"));
            updateInfo.setWeichat_jf_dg_price(jo.getString("weichat_jf_dg_price"));
            updateInfo.setWeichat_jf_all_price(jo.getString("weichat_jf_all_price"));
            updateInfo.setWeichat_qgg_price(jo.getString("weichat_qgg_price"));
            updateInfo.setWeichat_ffh_price(jo.getString("weichat_ffh_price"));
            updateInfo.setWeichat_svip_price(jo.getString("weichat_svip"));

//						updateInfo.setPc_svip("0.03");
//						updateInfo.setPc_shouqi("0.01");
//						updateInfo.setPc_dabao("0.01");
//						updateInfo.setPc_xiaobao("0.01");
//						updateInfo.setPc_jiasudu("0.01");
//						updateInfo.setPc_ganrao("0.01");
//						updateInfo.setPc_xpqhb("0.01");
//						updateInfo.setPc_zddx("0.01");
//
//						updateInfo.setSysj("2");
//						updateInfo.setScfy("0.01");
//						updateInfo.setZcfy("0.02");
//						updateInfo.setAllAppsPrice("0.03");
//						updateInfo.setWeichat_hf_all_price("0.02");
//						updateInfo.setWeichat_hf_dg_price("0.01");
//						updateInfo.setWeichat_jt_all_price("0.02");
//						updateInfo.setWeichat_jt_dg_price("0.01");
//						updateInfo.setWeichat_jf_dg_price("0.01");
//						updateInfo.setWeichat_jf_all_price("0.02");
//						updateInfo.setWeichat_qgg_price("0.01");
//						updateInfo.setWeichat_ffh_price("0.01");
//						updateInfo.setWeichat_svip_price("0.03");
//
//						updateInfo.setPc_svip("1");
//						updateInfo.setPc_shouqi("1");
//						updateInfo.setPc_dabao("1");
//						updateInfo.setPc_xiaobao("1");
//						updateInfo.setPc_jiasudu("1");
//						updateInfo.setPc_ganrao("1");
//						updateInfo.setPc_xpqhb("1");
//						updateInfo.setPc_zddx("1");
//
//						updateInfo.setSysj("2");
//						updateInfo.setScfy("1");
//						updateInfo.setZcfy("1");
//						updateInfo.setWeichat_hf_all_price("1");
//						updateInfo.setWeichat_hf_dg_price("1");
//						updateInfo.setWeichat_jt_all_price("1");
//						updateInfo.setWeichat_jt_dg_price("1");
//						updateInfo.setWeichat_jf_dg_price("1");
//						updateInfo.setWeichat_jf_all_price("1");
//						updateInfo.setWeichat_qgg_price("1");
//						updateInfo.setWeichat_ffh_price("1");
//						updateInfo.setWeichat_svip_price("1");

            if (oldUpdateInfo.getMfSj() != null) {
                if (Integer.valueOf(oldUpdateInfo.getMfSj()) > 0) {
                    updateInfo.setMfSj(oldUpdateInfo.getMfSj());
                } else {
                    updateInfo.setMfSj(jo.getString("sysj"));
//								updateInfo.setMfSj("2");
                }
            } else {
                updateInfo.setMfSj(jo.getString("sysj"));
//							updateInfo.setMfSj("2");
            }
            APPUsersShareUtil.setUpdateMessage(mActivity, updateInfo);
        }
        return updateInfo;
    }


    public static final String SCREEN_SHOT_PREVIEW = "update.message.screen.shot";
    public static List<ScreenShotPreview> getScreenShotPreviewArray(Context mContext){
        List<ScreenShotPreview> previewListdata = new ArrayList<>();
        String previewSpStr = (String) SharedPreferencesUtils.getParam(mContext,SCREEN_SHOT_PREVIEW,"");
        if (previewSpStr!=null&&!previewSpStr.equals("")){
            try {
                JSONArray spJa = new JSONArray(previewSpStr);
                for (int i=0;i<spJa.length();i++){
                    ScreenShotPreview screenShotPreview = new ScreenShotPreview();
                    JSONObject jo = spJa.getJSONObject(i);
                    screenShotPreview.setFunction_name(jo.getString("function_name"));
                    screenShotPreview.setImage_url(jo.getString("image_url"));
                    previewListdata.add(screenShotPreview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return previewListdata;
    }

    /**
     * 获取截图功能预览信息
     * @param arrayMsg  截图信息Array
     * @param functionKey 功能Key
     * @return
     */
    public static ScreenShotPreview getScreenShotPreviewInfo(List<ScreenShotPreview> arrayMsg,String functionKey){
        ScreenShotPreview info = new ScreenShotPreview();
        if (arrayMsg==null||arrayMsg.size()<=0)
            return info;
        for (int i=0;i<arrayMsg.size();i++){
            ScreenShotPreview forInfo = arrayMsg.get(i);
            if (forInfo.getFunction_name().equals(functionKey)){
                info.setFunction_name(forInfo.getFunction_name());
                info.setImage_url(forInfo.getImage_url());
                i=arrayMsg.size();
                break;
            }
        }
        return info;
    }
    public static void setScreenShotPreviewArray(Context mContext,String previewMessageJa){
//        String isLifeTimeStr = (String) SharedPreferencesUtils.getParam(mContext,SCREEN_SHOT_PREVIEW,"");
        if (previewMessageJa!=null&&!previewMessageJa.equals("")){
            SharedPreferencesUtils.setParam(mContext,SCREEN_SHOT_PREVIEW,previewMessageJa);
        }
    }

    /**
     * 检查是否购买终生
     * @param context
     * @return
     */
    public static boolean chekIsLifeTime(Context context){
        String isLifeTimeStr = (String) SharedPreferencesUtils.getParam(context,"life.time","");
        if (isLifeTimeStr==null){
            return false;
        }
        if (isLifeTimeStr!=null&&!isLifeTimeStr.equals("")){
            return true;
        }
        return false;
    }

    /**
     * 购买终生后信息储存
     * @param context
     */
    public static void setLifeTime(Context context){
        SharedPreferencesUtils.setParam(context,"life.time","lifeTime");
    }



    /**
     * 发送快捷方式广播
     *
     * @param context
     * @param appInfo
     * @param action
     */
//    private static void sendShortcutBroadcast(Context context, AppInfo appInfo, String action) {
//        Intent shortcut = new Intent(action);
//        // 不允许重建
//        shortcut.putExtra("duplicate", false);
//        // 设置名字
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "");
//        // 设置图标
//        try {
//            Bitmap appIcon;
//            if (appInfo.getIcon() == null) {
//                appIcon = BitmapFactory.decodeFile(appInfo.getIconPath());
//            } else {
//                appIcon = ObjectUtils.drawableToBitamp(appInfo.getIcon());
//            }
//            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, FileUtils.mergeBitmap(appIcon));
//        } catch (Exception e) {
//            e.printStackTrace();
//            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
//                    Intent.ShortcutIconResource.fromContext(BYApp.getInstance(), R.drawable.about));
//        }
//
//        // 设置意图和快捷方式关联程序
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, getShortcutExtraIntent(appInfo.getPackageName()));
//        context.sendBroadcast(shortcut);
//    }

    /**
     * 快捷方式广播扩展Intent
     *
     * @param
     * @return
     */
//    private static Intent getShortcutExtraIntent(String packageName) {
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        //点击快捷图片，运行的程序主入口
//        intent.setClass(VApp.context(), ShortcutStartActivity.class);
//        intent.putExtra("pkg", packageName);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        return intent;
//    }


    public static void setUserTag(Context context){
        SharedPreferencesUtils.setParam(context,"user.tag","tag");
    }

    public static boolean isUserTag(Context context){
        String str = SharedPreferencesUtils.getParam(context,"user.tag");
        if (str!=null&&!str.equals("")){
            return false;
        }else{
            return true;
        }
    }


}
