package com.zhushou.weichat.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.zhushou.weichat.VApp;
import com.zhushou.weichat.bean.HistoryPayInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */

public class HistoryPayShareUtil {

    public static final String PAY_HISTORY_SHARE_KEY = "weichat.message.check";
    public static final String SHARE_APP_ADD_TIME_SP = "share.add.time";

    /**
     * ��ȡ���Ѽ�¼�б�
     *
     * @return List<HistoryPayInfo>
     */
    public static List<HistoryPayInfo> getHistoryPayList(Context context) {
        List<HistoryPayInfo> listData = new ArrayList<>();
        String historyPayStr = (String) SharedPreferencesUtils.getParam(context, PAY_HISTORY_SHARE_KEY, "");
        if (historyPayStr != null && !historyPayStr.equals("")) {
            try {
                JSONArray ja = new JSONArray(historyPayStr);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    HistoryPayInfo info = new HistoryPayInfo();
                    info.setPayName(jo.getString("payName"));
                    info.setPayDate(jo.getString("payDate"));
                    info.setPayNumber(jo.getString("payNumber"));
                    info.setPayType(jo.getString("payType"));
                    listData.add(info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }

    /**
     * ��¼������Ϣ
     *
     * @param info HistoryPayInfo
     */
    public static void setHistoryPayList(Context context, HistoryPayInfo info) {
        String historyPayStr = (String) SharedPreferencesUtils.getParam(context, PAY_HISTORY_SHARE_KEY, "");
        JSONArray newJa = new JSONArray();
        if (historyPayStr != null && !historyPayStr.equals("")) {
            try {
                JSONArray ja = new JSONArray(historyPayStr);
                JSONObject newJo = new JSONObject();
                newJo.put("payName", info.getPayName());
                newJo.put("payDate", info.getPayDate());
                newJo.put("payNumber", info.getPayNumber());
                newJo.put("payType", info.getPayType());
                newJa.put(newJo);
                for (int i = 0; i < ja.length(); i++) {
                    newJa.put(ja.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                JSONObject newJo = new JSONObject();
                newJo.put("payName", info.getPayName());
                newJo.put("payDate", info.getPayDate());
                newJo.put("payNumber", info.getPayNumber());
                newJo.put("payType", info.getPayType());
                newJa.put(newJo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SharedPreferencesUtils.setParam(context, PAY_HISTORY_SHARE_KEY, newJa.toString());
    }


    public static String getSystemTime(int type) {
        long time = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat(type == VApp.DATE_NOSECOND ? "yyyy-MM-dd HH:mm" : "yyyy-MM-dd");
        Date d1 = new Date(time);
        return format.format(d1);
    }

    public static String getSystemTime() {
        long time = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = new Date(time);
        return format.format(d1);
    }

    public static String getFormatedDateTime(String pattern, long dateTime) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//        Date date = new Date(dateTime);
//        String timeStr = simpleDateFormat.format(date);
//        return timeStr;
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date(dateTime + 0));
    }

    public static String getSystemTime(String pattern){
        long time = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date d1 = new Date(time);
        return format.format(d1);
    }


    @NonNull
    public static String shareTimeChange(long dateTime) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = dateTime / dd;
        Long hour = (dateTime - day * dd) / hh;
        Long minute = (dateTime - day * dd - hour * hh) / mi;
        Long second = (dateTime - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = dateTime - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(hour + "小时");
        } else {
            sb.append("00小时");
        }
        if (minute > 0) {
            sb.append(minute + "分");
        } else {
            sb.append("00分");
        }
        if (second > 0) {
            sb.append(second + "秒");
        } else {
            sb.append("00秒");
        }
        if (milliSecond > 0) {
//            sb.append(milliSecond+"����");
        } else {
//            sb.append(milliSecond+"����");
        }
        return sb.toString();
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }
    public static boolean checkQQInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void addShareCount(Context context) {
        String historyPayStr = (String) SharedPreferencesUtils.getParam(context, SHARE_APP_ADD_TIME_SP, "");
        if (historyPayStr != null && !historyPayStr.equals("")) {
            JSONObject jo;
            int count = 0;
            try {
                jo = new JSONObject(historyPayStr);
                count = jo.getInt("count");
                jo.put("count", count + 1);
                SharedPreferencesUtils.setParam(context, SHARE_APP_ADD_TIME_SP, jo.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 每天仅加时一次 检查今天是否已经加时过
     *
     * @param context
     * @return
     */
    public static boolean shareAddTime(Context context) {
        String historyPayStr = (String) SharedPreferencesUtils.getParam(context, SHARE_APP_ADD_TIME_SP, "");
        if (historyPayStr != null && !historyPayStr.equals("")) {
            JSONObject jo;
            String date = "";
            int count = 0;
            try {
                jo = new JSONObject(historyPayStr);
                date = jo.getString("date");
                count = jo.getInt("count");
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

            if (date.equals(getSystemTime(VApp.DATE_HMS))) { //判断是否是同一天
                //这里的getSystemTime方法中参数 VApp.DATE_HMS 在方法中操作是反的 标记一下， 方法返回的是年-月-日
                if (count >= 3) // 判断当日是分享加时次数是否大于规定次数
                    return false;
//                try {
//                    jo.put("count", count + 1);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    return false;
//                }
                SharedPreferencesUtils.setParam(context, SHARE_APP_ADD_TIME_SP, jo.toString());
                return true;
            } else {
                //不是同一天 重新记录
                JSONObject newDataJo = new JSONObject();
                try {
                    newDataJo.put("date", getSystemTime(VApp.DATE_HMS));
                    newDataJo.put("count", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
                SharedPreferencesUtils.setParam(context, SHARE_APP_ADD_TIME_SP, newDataJo.toString());
                return true;
            }
        } else {
            //用户第一次记录
            JSONObject jo = new JSONObject();
            try {
                jo.put("date", getSystemTime(VApp.DATE_HMS));
                jo.put("count", 1);
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            SharedPreferencesUtils.setParam(context, SHARE_APP_ADD_TIME_SP, jo.toString());
            return true;
        }
    }
}
