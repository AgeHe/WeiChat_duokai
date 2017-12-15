package com.zhushou.weichat.ui.models;

import com.qq.e.ads.banner.BannerADListener;
import com.qq.e.comm.util.GDTLogger;

/**
 * Created by Administrator on 2017/12/14.
 */

public abstract class GDTAbstractBannerADListener implements BannerADListener {

    public GDTAbstractBannerADListener() {
    }

//    public void onADExposure() {
//        GDTLogger.i("On BannerAD Exposured");
//    }

    public void onADClosed() {
        GDTLogger.i("On BannerAD Closed");
    }

    public void onADClicked() {
        GDTLogger.i("On BannerAD Clicked");
    }

    public void onADLeftApplication() {
        GDTLogger.i("On BannerAD AdLeftApplication");
    }

    public void onADOpenOverlay() {
        GDTLogger.i("On BannerAD AdOpenOverlay");
    }

    public void onADCloseOverlay() {
        GDTLogger.i("On BannerAD AdCloseOverlay");
    }
}
