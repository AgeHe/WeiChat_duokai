package com.zhushou.weichat.utils;

import android.content.Context;
import android.util.Log;

import com.zhushou.weichat.ui.models.AppModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/4.
 */

public class VirtualAppsSpUtil {

    public final String virtual_apps = "virtual.apps";
    public Context context;

    public VirtualAppsSpUtil(Context context) {
        this.context = context;
    }

    public void setVirtualApps(List<AppModel> listData, boolean isSort) {
        if (listData == null || listData.size() <= 0)
            return;
        List<AppModel> newArrayData = null;
        JSONArray ja = new JSONArray();
        String jsonstr = (String) SharedPreferencesUtils.getParam(context, virtual_apps, "");
        if (isSort){
            for (int i = 0; i < listData.size(); i++) {
                JSONObject jo = new JSONObject();
                AppModel appModel = listData.get(i);
                try {
                    jo.put("index", i);
                    jo.put("packagename", appModel.packageName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ja.put(jo);
            }
            SharedPreferencesUtils.setParam(context, virtual_apps, ja.toString());
            return;
        }

        if (jsonstr != null && !jsonstr.equals("")) {
            try {
                JSONArray oldJa = new JSONArray(jsonstr);
                if (newArrayData==null){
                    newArrayData = new ArrayList<>();
                }
                for (int i = 0; i < listData.size(); i++){
                    AppModel appModel = listData.get(i);
                    boolean isAdd = false;
                    for (int j = 0; j < oldJa.length(); j++){
                        JSONObject jo = oldJa.getJSONObject(j);
                        if (appModel.packageName.equals(jo.getString("packagename"))){
                            isAdd = true;
                            break;
                        }
                    }
                    if (!isAdd){
                        newArrayData.add(appModel);
                    }
                }

                for (int i = 0; i < newArrayData.size(); i++) {
                    JSONObject jo = new JSONObject();
                    AppModel appModel = newArrayData.get(i);
                    try {
                        jo.put("index", oldJa.length()+i);
                        jo.put("packagename", appModel.packageName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    oldJa.put(jo);
                }
                ja = oldJa;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0; i < listData.size(); i++) {
                JSONObject jo = new JSONObject();
                AppModel appModel = listData.get(i);
                try {
                    jo.put("index", i);
                    jo.put("packagename", appModel.packageName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ja.put(jo);
            }
        }
        SharedPreferencesUtils.setParam(context, virtual_apps, ja.toString());
    }

    public List<AppModel> getVirtualAppsIndex(List<AppModel> listdate) {
        List<AppModel> appModels = new ArrayList<>();
        String jsonStr = (String) SharedPreferencesUtils.getParam(context, virtual_apps, "");
        if (jsonStr != null && !jsonStr.equals("") && listdate != null) {
            try {
                JSONArray ja = new JSONArray(jsonStr);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    for (int a = 0; a < listdate.size(); a++) {
                        try {
                            AppModel appModel = listdate.get(a);
                            if (jo.getString("packagename").equals(appModel.packageName)) {
                                appModels.add(appModel);
                                break;
                            }
                        } catch (Exception e) {
                            Log.i("hh", "getVirtualAppsIndex: " + e.getMessage());
                            continue;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return appModels;
        } else {
            return listdate;
        }
    }
}
