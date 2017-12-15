package com.zhushou.weichat.utils;

import android.util.Log;

import com.zhushou.weichat.bean.UpdateInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/4/7.
 */

public class UpdateUtil {
    //获取要升级到的下载的地址
    public static String getDownloadAddress(String addressHead,String appNameCode,String from,String updateVersionCode){
        StringBuilder address = new StringBuilder();
        address.append(addressHead).append(appNameCode).append("/").append(updateVersionCode)
                .append("/").append(appNameCode).append("_").append(from).append(".apk");//类似fssq/119/fssq_1001.apk
        return address.toString();
    }

    /**
     *
     * @param updateJson  盒子JSON
     * @param from  渠道号
     * @param appNameCode  应用号
     * @param localVersionCode 应用版本号
     * @return
     */
    public static UpdateInfo DecodeUpdateJson(JSONObject updateJson, String from, String appNameCode, String localVersionCode){
        Log.d("jyh",updateJson.toString());
        UpdateInfo info = null;
        try {
            JSONObject localInfo = updateJson.getJSONObject("updateInfo");
            if(localInfo!=null){
                boolean hasAppNameCode = localInfo.has(appNameCode);//判断应用简称
                if(hasAppNameCode){
                    JSONObject appNameCodeInfo = localInfo.getJSONObject(appNameCode);
                    if(appNameCodeInfo!=null){
                        JSONObject localVersionCodeInfo = appNameCodeInfo.getJSONObject(localVersionCode);
                        if(localVersionCodeInfo!=null){
                            String newVersionCode = localVersionCodeInfo.getString("new_v");
                            boolean hasForceUpdate = localVersionCodeInfo.has("isupdate");
                            if(newVersionCode!=null&&hasForceUpdate){
                                String addressHead = updateJson.getString("address");
                                if(addressHead!=null){
                                    info = new UpdateInfo(newVersionCode,localVersionCodeInfo.getBoolean("isupdate"),getDownloadAddress(addressHead,appNameCode,from,newVersionCode));
                                    info.setUpdate(compareVersion(localVersionCode,newVersionCode));//设置是否需要更新
                                    return info;
                                }
                            }
                        }
                    }
                }
            }
        }catch (JSONException e ){
           e.printStackTrace();
            return null;
        }
        return info;
    }

    private static boolean compareVersion(String localVersion,String newVersion) {//服务器版本大于本地版本
        boolean isUpdate = false;
        try{
            isUpdate = Integer.parseInt(localVersion) < Integer.parseInt(newVersion);
        }catch (Exception e){
              e.printStackTrace();
        }
        return isUpdate;
    }

}
