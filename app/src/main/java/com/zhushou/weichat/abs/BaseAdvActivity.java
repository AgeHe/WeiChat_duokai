package com.zhushou.weichat.abs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.lbsw.stat.LBStat;
import com.zhushou.weichat.auxiliary.sp.SPAuxiliaryPayUtils;
import com.zhushou.weichat.bean.AdvGgsResultInfo;
import com.zhushou.weichat.bean.AdvTypeInfo;
import com.zhushou.weichat.bean.AdverInfo;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.utils.CountUtil;
import com.zhushou.weichat.utils.DisplayUtil;
import com.zhushou.weichat.utils.OkHttpUtil;
import com.zhushou.weichat.utils.SystemUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */

public abstract class BaseAdvActivity extends Activity {

    private static final String TAG = "BaseAdvActivity";
    private CountUtil countUtil;
    private TelephonyManager telephonyManager;
    private String getIPUrl = "http://pv.sohu.com/cityjson?ie=utf-8";
    public String getADVUrl = "http://openapi.zzad.com/index.php";

    /**
     * 国际移动用户识别码
     */
    private String IMSI;

    public String kpKey = "TFNpGQ";
    public String kpSaid = "196";
    public String systemTimes = String.valueOf(System.currentTimeMillis()/1000);
    public int device_type = 1;
    public int system_os = 2;
    public String scale = "11.7";

    public String encryption = "";

    public String screen_size = "";//设备屏幕宽高
    public String system_imei = "";//设备唯一标识
    public String system_version = "";//系统版本
    public String phone_model = "";//手机型号
    public String netWork = "";//网络信息
    public String operator = "";//手机运营商
    public String ip = "58.210.119.10";//ip地址

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        countUtil = new CountUtil(this);
    }

    public getPhoneStatus lisener;
    public void getAdvparamter(getPhoneStatus lisener){
        this.lisener = lisener;
        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        operator = getProvidersName();
        phone_model = SystemUtil.getSystemModel();
        system_version = SystemUtil.getSystemVersion();
        system_imei = SystemUtil.getIMEI(this);
        screen_size = DisplayUtil.getScreenWidth(this)+"*"+DisplayUtil.getScreenHeight(this);
        netWork = DisplayUtil.getNetWorkType(this);
        new Thread(new RequestIpStr()).start();
    }

    /**
     * Role:Telecom service providers获取手机服务商信息 <BR>
     * 需要加入权限<uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/> <BR>
     * Date:2012-3-12 <BR>
     *
     * @author CODYY)peijiangping
     */
    public String getProvidersName() {
        String ProvidersName = null;
        // 返回唯一的用户ID;这张卡的编号
        IMSI = telephonyManager.getSubscriberId();
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        System.out.println(IMSI);
        if (IMSI==null)
            return "无卡";
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        }
        return ProvidersName;
    }


    private class RequestIpStr implements Runnable{
        @Override
        public void run() {
            //处理网络请求或者耗时操作
            OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
            OkHttpResult result = okHttpUtil.requestPostBySyn(getIPUrl, null, null);
            Message msg = new Message();
            msg.obj = result.msg;
            msg.what = 0;
            myHttpHandler.sendMessage(msg);
        }
    }

    public AdvGgsResultInfo ggsResultInfo = new AdvGgsResultInfo();
    public Handler myHttpHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    try{
                        String resultStr = msg.obj.toString();
                        String resultJo = resultStr.substring(19,resultStr.length()-1);
                        Log.i(TAG, "handleMessage: "+resultJo);
                        JSONObject jo = new JSONObject(resultJo);
                        ip = jo.getString("cip");
                    }catch (Exception e){
                        Log.i(TAG, "handleMessage: "+e.getMessage());
                        ip = "58.210.119.10";
                    }
                    if (lisener!=null){
                        lisener.httpReturn();
                    }
                    break;
                case 1:
                    try{
                        String resultStr = msg.obj.toString();
                        Log.i(TAG, "handleMessage: "+resultStr);
                        JSONObject resultJo = new JSONObject(resultStr);
                        int resultCode = resultJo.getInt("code");
                        if (resultCode==200){
                            JSONArray infoJa = resultJo.getJSONArray("info");
                            if (infoJa.length()<=0)
                                throw new RuntimeException("Stub!");
                            JSONObject advJo = infoJa.getJSONObject(infoJa.length()-1);
                            ggsResultInfo.setImg_src(advJo.getString("img_src"));
                            ggsResultInfo.setTitle(advJo.getString("title"));
                            ggsResultInfo.setDesc(advJo.getString("desc"));
                            ggsResultInfo.setCurl(advJo.getString("curl"));
                            lisener.ggsresultInfo(ggsResultInfo);
                        }else{
                            lisener.ggsresultInfo(ggsResultInfo);
                        }
                    }catch (Exception e){
                        Log.i(TAG, "handleMessage: "+e.getMessage());
                    }
                    break;
            }
        }
    };


    /**
     * 解析微信多开广告配置
     * @param advJson
     * @return
     */
    public AdvTypeInfo boxAdvConfigure(String advJson){
        AdvTypeInfo advTypeInfo = new AdvTypeInfo();
        if (countUtil==null)
            countUtil = new CountUtil(this);
        try {
            JSONArray advja = new JSONArray(advJson);
            for (int i=0;i<advja.length();i++){
                JSONObject advJo = advja.getJSONObject(i);
                if (countUtil.getApp().equals(advJo.getString("app_index"))){
                    advTypeInfo.setApp_index(advJo.getString("app_index"));
                    advTypeInfo.setAdv_status(advJo.getBoolean("adv_status"));
                    advTypeInfo.setAdv_type(advJo.getString("adv_type"));
                    advTypeInfo.setAdv_pay(advJo.getBoolean("adv_pay"));
                    advTypeInfo.setAppid(advJo.getString("app_id"));
                    advTypeInfo.setPosid(advJo.getString("pos_id"));
                    JSONArray zjja = advJo.getJSONArray("my_self_adver");
                    List<AdverInfo> adverInfos = new ArrayList<>();
                    for (int a=0;a<zjja.length();a++){
                        AdverInfo info = new AdverInfo();
                        JSONObject jo = zjja.getJSONObject(a);
                        info.setTitlename(jo.getString("titlename"));
                        info.setName(jo.getString("name"));
                        info.setClickStatu(jo.getString("click_statu"));
                        info.setAppAddress(jo.getString("appaddress"));
                        info.setIconAddress(jo.getString("iconaddress"));
                        info.setPkg_name(jo.getString("pkg_name"));
                        adverInfos.add(info);
                    }
                    advTypeInfo.setMy_self_adver(adverInfos);
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        try{
            if (advTypeInfo!=null){
                SPAuxiliaryPayUtils.saveQggIndex(BaseAdvActivity.this,advTypeInfo.isAdv_status());
            }else{
                SPAuxiliaryPayUtils.saveQggIndex(BaseAdvActivity.this,false);
            }
        }catch (Exception e){
            SPAuxiliaryPayUtils.saveQggIndex(BaseAdvActivity.this,false);
        }
        return advTypeInfo;
    }

    public interface getPhoneStatus{
        void httpReturn();
        void ggsresultInfo(AdvGgsResultInfo ggsResultInfo);
    }

}
