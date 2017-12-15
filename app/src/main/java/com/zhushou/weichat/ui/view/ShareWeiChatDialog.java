package com.zhushou.weichat.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.comm.util.AdError;
import com.zhushou.weichat.R;
import com.zhushou.weichat.api.RequestHost;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.utils.DebugLog;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

/**
 * Created by Administrator on 2017/2/13.
 */

public class ShareWeiChatDialog extends Dialog{

    private shareWcItemClick itemClickInterface;
    private Activity mActivity;
    UpdatePriceInfo messageInfo;

    public ShareWeiChatDialog(Activity mActivity, shareWcItemClick itemClickInterface) {
        super(mActivity,R.style.PopupDialog);
        this.mActivity = mActivity;
        this.itemClickInterface = itemClickInterface;
        this.messageInfo = APPUsersShareUtil.getUpdateMessage(mActivity);
        setShareWeiChatDialog();
    }

//    public ShareWeiChatDialog(Context context, int themeResId) {
//        super(context, themeResId);
//    }
//    protected ShareWeiChatDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//    }

    private void setShareWeiChatDialog(){

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_share_weichat,null);
        TextView iv_wc_1 = (TextView) view.findViewById(R.id.iv_wc_1);
        TextView iv_wc_2 = (TextView) view.findViewById(R.id.iv_wc_2);
        TextView iv_wc_3 = (TextView) view.findViewById(R.id.iv_wc_3);
        ImageView iv_btn_cancel = (ImageView) view.findViewById(R.id.iv_btn_cancel);
        TextView tv_hits_content = (TextView) view.findViewById(R.id.tv_hits_content);
        FrameLayout rl_share_adv = (FrameLayout) view.findViewById(R.id.rl_share_adv);
        BannerView banner = new BannerView(mActivity, ADSize.BANNER, RequestHost.GDT_APPID, RequestHost.GDT_Banner_POSTID);
        //设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
        banner.setRefresh(0);
        banner.setADListener(new AbstractBannerADListener() {

            /**
             * 广告加载失败，errCode用于描述失败原因
             */
            @Override
            public void onNoAD(AdError adError) {
                DebugLog.e("广点通 Banner 广告加载失败");
            }

            /**
             * 广告加载成功回调 表示广告相关的资源已经加载完毕
             */
            @Override
            public void onADReceiv() {
                DebugLog.e("广点通 Banner 广告加载成功回调");
            }
            /**
             * 当广告曝光时发起的回调
             */
            @Override
            public void onADExposure() {
                DebugLog.e("广点通 Banner 曝光");
            }
            /**
             * 当广告点击时发起的回调，由于点击去重等原因可能和联盟平台最终的统计数据有差异
             */
            @Override
            public void onADClosed() {
                DebugLog.e("广点通 Banner 广告点击");
            }
        });
        rl_share_adv.addView(banner);
        banner.loadAD();
        tv_hits_content.setText(String.format(mActivity.getResources().getString(R.string.share_wc_hint),messageInfo.getShareTime(),messageInfo.getFxcs()));
        if (iv_wc_1!=null) iv_wc_1.setOnClickListener(listener);
        if (iv_wc_2!=null) iv_wc_2.setOnClickListener(listener);
        if (iv_wc_3!=null) iv_wc_3.setOnClickListener(listener);
        if (iv_btn_cancel!=null) iv_btn_cancel.setOnClickListener(listener);

        this.setCanceledOnTouchOutside(true);                 //点击外部关闭窗口

        Window win = this.getWindow();
        win.setGravity(Gravity.BOTTOM);                       //从下方弹出
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //宽度填满
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;  //高度自适应
        win.setAttributes(lp);

        super.setContentView(view);
    }

    private View.OnClickListener listener = view -> {
        if (itemClickInterface==null)
            return;

        switch (view.getId()){
            case R.id.iv_wc_1:
                itemClickInterface.itemClick(0);
                dismiss();
                break;
            case R.id.iv_wc_2:
                itemClickInterface.itemClick(1);
                dismiss();
                break;
            case R.id.iv_wc_3:
                itemClickInterface.itemClick(2);
                dismiss();
                break;
            case R.id.iv_btn_cancel:
                dismiss();
                break;
        }
    };

    @Override
    public void show() {
        super.show();
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        /////////获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);;
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        /////////设置高宽
        lp.width = screenWidth; // 宽度
        dialogWindow.setAttributes(lp);
    }



    public interface shareWcItemClick{
        void itemClick(int flag);
    }

}

