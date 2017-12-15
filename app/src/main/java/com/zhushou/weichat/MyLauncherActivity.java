package com.zhushou.weichat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lbsw.stat.LBStat;
import com.squareup.picasso.Picasso;
import com.zhushou.weichat.addfriends.ui.WebActivity;
import com.zhushou.weichat.api.ZhuShouApi;
import com.zhushou.weichat.auxiliary.sp.AuxiliaryConstans;
import com.zhushou.weichat.auxiliary.sp.SPAuxiliaryPayUtils;
import com.zhushou.weichat.bean.AdverInfo;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.bean.StatisticsConstans;
import com.zhushou.weichat.ui.BaseMainActivity;
import com.zhushou.weichat.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/1/13.
 */

public class MyLauncherActivity extends BaseMainActivity implements View.OnClickListener {

    private static final String TAG = "MyLauncherActivity";
    private TextView tv_count_down;
    private ImageView iv_adver_image;
    private TextView tv_intent_web;
    private AdverInfo adverInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        tv_count_down = (TextView) findViewById(R.id.tv_count_down);
        iv_adver_image = (ImageView) findViewById(R.id.iv_adver_image);
        tv_intent_web = (TextView) findViewById(R.id.tv_intent_web);
        iv_adver_image.setOnClickListener(this);
        tv_intent_web.setOnClickListener(this);
        if (SPAuxiliaryPayUtils.isAuxiliaryMerchantOpen(this, AuxiliaryConstans.C_QGG)){
            startActivity(new Intent(MyLauncherActivity.this, MainActivity.class));
            finish();
        }else{
//            new Thread(new StartRunnable()).start();
            ZhuShouApi.appBaseInfoGet(0,startHandler);
        }
    }

    @Override
    public void onClick(View view) {
        LBStat.click(StatisticsConstans.TJ_P_ADVER);
        if (adverInfo==null)
            return;
        if (adverInfo.getAppAddress()==null||adverInfo.getTitlename()==null)
            return;
        Bundle mBundle = new Bundle();
        mBundle.putString("url",adverInfo.getAppAddress());
        mBundle.putString("title_name",adverInfo.getTitlename());
        switch (view.getId()){
            case R.id.iv_adver_image:
                startActivity(new Intent(MyLauncherActivity.this, WebActivity.class).putExtras(mBundle));
                if (cdt!=null)
                    cdt.cancel();
                finish();
                break;
            case R.id.tv_intent_web:
                startActivity(new Intent(MyLauncherActivity.this, WebActivity.class).putExtras(mBundle));
                if (cdt!=null)
                    cdt.cancel();
                finish();
                break;
        }
    }





//    class StartRunnable implements Runnable {
//
//        @Override
//        public void run() {
//            long startTime = System.currentTimeMillis();
//
//            //处理网络请求或者耗时操作
//            OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
//            OkHttpResult result = okHttpUtil.requestGetBySyn(VApp.URL, null, null);
////            long spend = System.currentTimeMillis() - startTime;
////            if (spend < 2000) {
////                try {
////                    Thread.sleep(2000 - spend);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
//            Message msg = new Message();
//            msg.obj = result;
//            startHandler.sendMessage(msg);
//        }
//    }

    private Handler startHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            OkHttpResult result = (OkHttpResult) msg.obj;
            if (result != null && result.isSuccess) {
                try {
                    JSONObject jo = new JSONObject(result.msg);
                    JSONArray ja = jo.getJSONArray("adver");
                    if (ja.length()<=0){
                        startActivity(new Intent(MyLauncherActivity.this, MainActivity.class));
                        finish();
                        return;
                    }
                    adverInfo = getAdverMsg(ja);
                    adverInfo.setAd_lenght(jo.getInt("ad_length"));
                    Log.i(TAG, "handleMessage: "+adverInfo.getAd_lenght());
                    if (adverInfo == null) {
                        startActivity(new Intent(MyLauncherActivity.this, MainActivity.class));
                        finish();
                    }else{
                        tv_count_down.setVisibility(View.VISIBLE);
//                        tv_intent_web.setVisibility(View.VISIBLE);
                        Picasso.with(MyLauncherActivity.this).load(adverInfo.getIconAddress()).into(iv_adver_image);
                        startAdverCountDown();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public AdverInfo getAdverMsg(JSONArray ja) {
        List<AdverInfo> listData = DecodeAdverJson(ja);
        Random random = new Random();
        if (listData == null || listData.size() <= 0) {
            return null;
        } else {
            int index = random.nextInt(listData.size());
            return listData.get(index);
        }
    }

    public static ArrayList<AdverInfo> DecodeAdverJson(JSONArray httpJa) {

        ArrayList<AdverInfo> list = new ArrayList<>();
        try {
            for (int i = 0; i < httpJa.length(); i++) {
                JSONObject object = (JSONObject) httpJa.get(i);
                AdverInfo info = new AdverInfo();
                info.setName(object.getString("name"));
                info.setAppAddress(object.getString("appaddress"));
                info.setIconAddress(object.getString("iconaddress"));
                info.setTitlename(object.getString("titlename"));
                list.add(info);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }
    CountDownTimer cdt;
    private void startAdverCountDown() {
         cdt = new CountDownTimer((adverInfo.getAd_lenght()*1000)+300, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_count_down.setText(String.format(getResources().getString(R.string.launcher_count_down),
                        String.valueOf(millisUntilFinished/1000)));
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(MyLauncherActivity.this, MainActivity.class));
                finish();
            }
        };
        cdt.start();
    }

}
