package com.zhushou.weichat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/21.
 */
public class SharedPreferencesUtils {

    /**
     */
    public static final String FILE_NAME = "duokai_share_date";

    /**
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context , String key, Object object){

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }

        editor.commit();
    }

    /**
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context , String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        if (context==null)
            return null;
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }

        return null;
    }

    public static String getParam(Context context , String key){
        if (context==null)
            return null;
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    /*public static void saveOrderId(Context context,String orderId){
        SharedPreferences save=context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=save.edit();
        editor.putString("orderid",orderId);
        editor.commit();
    }
    public static String getSaveOrderId(Context context){
        SharedPreferences save=context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String orderid=save.getString("orderid","");
        return orderid;
    }
    public static void cancleSaveOrderId(Context context){
        SharedPreferences save=context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=save.edit();
        editor.putString("orderid","");
        editor.commit();
    }
    public static void saveOrderStatus(Context context,boolean isQuery){
        SharedPreferences save=context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=save.edit();
        editor.putBoolean("orderstatus",isQuery);
        editor.commit();
    }
    public static boolean getOrderStatus(Context context){
        SharedPreferences save=context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        boolean orderstatus=save.getBoolean("orderstatus",false);
        return orderstatus;
    }*/

    public static void saveOrderInfo(Context context,String orderId,boolean isQuery,int waretype,String payType,String number){
        SharedPreferences save=context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=save.edit();
        editor.putString("orderid",orderId);
        editor.putBoolean("orderstatus",isQuery);
        editor.putInt("waretype",waretype);
        editor.putString("paytype",payType);
        editor.putString("number",number);
        editor.commit();
    }
    public static Map<String,Object> getOrderInfo(Context context){
        SharedPreferences save=context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        save.getString("orderid","");
        save.getBoolean("orderstatus",false);
        save.getInt("waretype",0);
        save.getString("paytype","");
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("orderid",save.getString("orderid",""));
        map.put("orderstatus",save.getBoolean("orderstatus",false));
        map.put("waretype",save.getInt("waretype",0));
        map.put("paytype",save.getString("paytype",""));
        map.put("number",save.getString("number",""));
        return map;
    }
    public static void saveSecondOrderInfo(Context context,String orderId,boolean isQuery,int waretype,String payType){
        SharedPreferences save=context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=save.edit();
        editor.putString("orderid2",orderId);
        editor.putBoolean("orderstatus2",isQuery);
        editor.putInt("waretype2",waretype);
        editor.putString("paytype2",payType);
        editor.commit();
    }
    public static void saveSecondOrderInfo(Context context,String orderId,boolean isQuery,int waretype,String payType,String apkName){
        SharedPreferences save=context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=save.edit();
        editor.putString("orderid2",orderId);
        editor.putBoolean("orderstatus2",isQuery);
        editor.putInt("waretype2",waretype);
        editor.putString("paytype2",payType);
        editor.putString("apkname",apkName);
        editor.commit();
    }
    public static Map<String,Object> getSecondOrderInfo(Context context){
        SharedPreferences save=context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        save.getString("orderid2","");
//        save.getBoolean("orderstatus2",false);
//        save.getInt("waretype2",0);
//        save.getString("paytype2","");
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("orderid2",save.getString("orderid2",""));
        map.put("orderstatus2",save.getBoolean("orderstatus2",false));
        map.put("waretype2",save.getInt("waretype2",0));
        map.put("paytype2",save.getString("paytype2",""));
        map.put("apkname",save.getString("apkname",""));
        return map;
    }


}
