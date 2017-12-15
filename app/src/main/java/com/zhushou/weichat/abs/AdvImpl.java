package com.zhushou.weichat.abs;

import android.os.CountDownTimer;

import com.zhushou.weichat.GDTLauncherActivity;

/**
 * Created by Administrator on 2017/6/2.
 */

public class AdvImpl {

    GDTLauncherActivity gdtLauncherActivity;

    public AdvImpl(GDTLauncherActivity gdtLauncherActivity){
        this.gdtLauncherActivity = gdtLauncherActivity;
    }

    public void startDownTimer(){
        startAdverCountDown();
    }
    public void cancelCdt(){
        if (cdt!=null)
            cdt.cancel();
    }

    CountDownTimer cdt;
    CountDownTimer scdt;
    private void startAdverCountDown() {
        cdt = new CountDownTimer((3*1000)+1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                gdtLauncherActivity.onADTick(millisUntilFinished-1000);
            }

            @Override
            public void onFinish() {
//                startActivity(new Intent(MyLauncherActivity.this, MainActivity.class));
//                finish();
                gdtLauncherActivity.onADTick(0);
                gdtLauncherActivity.jumMainActivity();
                cdt.cancel();
            }
        };
        cdt.start();
    }

    public void StartLodingAdvOvertime(){
        scdt = new CountDownTimer((3*1000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                gdtLauncherActivity.sleepIntent();
                scdt.cancel();
            }
        };
        scdt.start();
    }


}
