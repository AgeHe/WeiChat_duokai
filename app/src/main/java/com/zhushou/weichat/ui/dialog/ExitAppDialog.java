package com.zhushou.weichat.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qq.e.ads.cfg.BrowserType;
import com.qq.e.ads.cfg.DownAPPConfirmPolicy;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;
import com.zhushou.weichat.R;
import com.zhushou.weichat.api.RequestHost;
import com.zhushou.weichat.utils.DebugLog;

import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */

public class ExitAppDialog extends Dialog implements NativeExpressAD.NativeExpressADListener {

    Activity mActivity;
    private NativeExpressADView adItem;
    private NativeExpressAD nativeAD;

    public ExitAppDialog(@NonNull Context context, Activity mActivity) {
        super(context, R.style.MyDialog);
        this.mActivity = mActivity;
        initView();
    }

    public ExitAppDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initView();
    }
    RelativeLayout rl_exit_adv;
    public void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_exit_app, null);
        rl_exit_adv = (RelativeLayout) view.findViewById(R.id.rl_exit_adv);
        TextView tv_exit = (TextView) view.findViewById(R.id.tv_exit);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        rl_exit_adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exitAppCallBack != null)
                    exitAppCallBack.exit();
                dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        loadAD();
        super.setContentView(view);
    }

    public void loadAD() {
        if (nativeAD == null) {
            this.nativeAD = new NativeExpressAD(mActivity, new ADSize(ADSize.FULL_WIDTH,ADSize.AUTO_HEIGHT), RequestHost.GDT_APPID,RequestHost.GDT_NativeAD_POSTID,this);

        }
        nativeAD.setBrowserType(BrowserType.Inner);
        nativeAD.setDownAPPConfirmPolicy(DownAPPConfirmPolicy.Default);
        int count = 1; // 一次拉取的广告条数：范围1-10
        nativeAD.loadAD(count);
    }

    ExitAppCallBack exitAppCallBack;
    public void setExitAppCallBack(ExitAppCallBack exitAppCallBack) {
        this.exitAppCallBack = exitAppCallBack;
    }

    @Override
    public void onNoAD(AdError adError) {
        DebugLog.e("广点通 Native "+adError.getErrorCode()+"//"+adError.getErrorMsg());
        if (cursorAdv<3){
            cursorAdv+=1;
            loadAD();
            return;
        }
    }

    int cursorAdv = 0;
    @Override
    public void onADLoaded(List<NativeExpressADView> list) {
        DebugLog.e("广点通 Native "+list.size());
        if (list.size()<=0&&cursorAdv<3){
            cursorAdv+=1;
            loadAD();
            return;
        }
        // 释放前一个NativeExpressADView的资源
        if (adItem != null) {
            adItem.destroy();
        }
        // 3.返回数据后，SDK会返回可以用于展示NativeExpressADView列表
        adItem = list.get(0);

        adItem.render();
        if (rl_exit_adv.getChildCount() > 0) {
            rl_exit_adv.removeAllViews();
        }
        if (isShowing()){
            // 需要保证View被绘制的时候是可见的，否则将无法产生曝光和收益。

            rl_exit_adv.addView(adItem);
        }
    }

    @Override
    public void onRenderFail(NativeExpressADView nativeExpressADView) {
        DebugLog.e("广点通 Native onRenderFail");
    }

    @Override
    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
        DebugLog.e("广点通 Native onRenderSuccess");
    }

    @Override
    public void onADExposure(NativeExpressADView nativeExpressADView) {
        DebugLog.e("广点通 Native onADExposure曝光");
    }

    @Override
    public void onADClicked(NativeExpressADView nativeExpressADView) {
        DebugLog.e("广点通 Native onADClicked 点击");
    }

    @Override
    public void onADClosed(NativeExpressADView nativeExpressADView) {
        DebugLog.e("广点通 Native onADClosed");
    }

    @Override
    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
        DebugLog.e("广点通 Native onADLeftApplication");
    }

    @Override
    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {
        DebugLog.e("广点通 Native onADOpenOverlay");
    }

    @Override
    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {
        DebugLog.e("广点通 Native onADCloseOverlay");
    }


    public interface ExitAppCallBack {
        void exit();
    }

    @Override
    public void show() {
        super.show();
        if (adItem!=null){
            // 需要保证View被绘制的时候是可见的，否则将无法产生曝光和收益。
            rl_exit_adv.addView(adItem);
        }else{
            loadAD();
        }

    }
}
