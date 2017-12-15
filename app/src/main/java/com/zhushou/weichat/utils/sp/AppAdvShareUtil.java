package com.zhushou.weichat.utils.sp;

import android.content.Context;

import com.zhushou.weichat.uinew.info.AdInfo;
import com.zhushou.weichat.uinew.info.HomeItemInfo;
import com.zhushou.weichat.uinew.info.HomeItemTopInfo;
import com.zhushou.weichat.utils.CountUtil;
import com.zhushou.weichat.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 版本回退  已废弃
 * Created by Administrator on 2017/11/28.
 */
@Deprecated
public class AppAdvShareUtil {

    public static final String App_item_Adv_Sp = "app.adv.sp";
    public static final String App_banner_Adv_Sp = "app.banner.adv.sp";

    /**
     * 保存界面广告数据
     * 版本回退  已废弃
     * @param context
     * @param jaStr
     * @return
     * @throws JSONException
     */
    @Deprecated
    public static List<AdInfo> saveAppAdv(Context context,String  jaStr) throws JSONException {
        List<AdInfo> arrayAdInfo = new ArrayList<>();
        CountUtil countUtil = new CountUtil(context);
        JSONArray ja = new JSONArray(jaStr);
        JSONArray adcJa = null;
        for (int i=0;i<ja.length();i++){
            JSONObject jo = ja.getJSONObject(i);
            if (jo.getString("app_index").equals(countUtil.getApp())){
                adcJa = jo.getJSONArray("homearray_adv");
                SharedPreferencesUtils.setParam(context,App_item_Adv_Sp,adcJa.toString());
                SharedPreferencesUtils.setParam(context,App_banner_Adv_Sp,jo.getJSONArray("hometoparray_adv").toString());
                break;
            }
        }
        if (adcJa!=null){
            for (int i=0;i<adcJa.length();i++){
                JSONObject adjo = adcJa.getJSONObject(i);
                AdInfo adinfo = new AdInfo();
                adinfo.setName(adjo.getString("name"));
                adinfo.setCornerUrl(adjo.getString("corner_url"));
                adinfo.setIconUrl(adjo.getString("icon_url"));
                adinfo.setJumpType(adjo.getInt("jump_type"));
                adinfo.setJumpUrl(adjo.getString("jump_url"));
                arrayAdInfo.add(adinfo);
            }
        }
        return arrayAdInfo;
    }

    /**
     * 版本回退  已废弃
     * @param context
     * @return
     */
    @Deprecated
    public static List<HomeItemInfo> getAppItemAdv(Context context){
        List<HomeItemInfo> arraList = new ArrayList<>();
        String advJaStr = (String) SharedPreferencesUtils.getParam(context,App_item_Adv_Sp,"");
        if (advJaStr!=null&&!advJaStr.equals("")&&!advJaStr.isEmpty()){
            try {
                JSONArray ja = new JSONArray(advJaStr);
                for (int i=0;i<ja.length();i++){
                    JSONObject adjo = ja.getJSONObject(i);
                    HomeItemInfo homeItemInfo = new HomeItemInfo();
                    AdInfo adinfo = new AdInfo();
                    adinfo.setName(adjo.getString("name"));
                    adinfo.setCornerUrl(adjo.getString("corner_url"));
                    adinfo.setIconUrl(adjo.getString("icon_url"));
                    adinfo.setJumpType(adjo.getInt("jump_type"));
                    adinfo.setJumpUrl(adjo.getString("jump_url"));
                    homeItemInfo.setAdInfo(adinfo);
                    arraList.add(homeItemInfo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arraList;
    }

    /**
     * 版本回退  已废弃
     * @param context
     * @return
     */
    @Deprecated
    public static List<HomeItemTopInfo> getBannerAdv(Context context){
        List<HomeItemTopInfo> arrayAdv = new ArrayList<>();
        String advJaStr = (String) SharedPreferencesUtils.getParam(context,App_banner_Adv_Sp,"");
        if (advJaStr!=null&&!advJaStr.equals("")&&!advJaStr.isEmpty()){
            try {
                JSONArray ja  = new JSONArray(advJaStr);
                for (int i=0;i<ja.length();i++){
                    JSONObject adjo = ja.getJSONObject(i);
                    HomeItemTopInfo adinfo = new HomeItemTopInfo();
                    adinfo.setAdName(adjo.getString("adname"));
                    adinfo.setImageUrl(adjo.getString("image_url"));
                    adinfo.setJumpType(adjo.getInt("jump_type"));

                    adinfo.setJumpAddress(adjo.getString("jump_address"));
                    arrayAdv.add(adinfo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return arrayAdv;
    }

}
