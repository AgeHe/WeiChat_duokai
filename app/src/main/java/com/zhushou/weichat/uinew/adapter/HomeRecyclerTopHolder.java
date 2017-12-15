package com.zhushou.weichat.uinew.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.ui.TuiABrowserActivity;
import com.zhushou.weichat.ui.adapter.ApkOperator;
import com.zhushou.weichat.uinew.info.HomeItemInfo;
import com.zhushou.weichat.uinew.info.HomeItemTopInfo;
import com.zhushou.weichat.uinew.widget.BannerView;
import com.zhushou.weichat.utils.DisplayUtil;
import com.zhushou.weichat.utils.HistoryPayShareUtil;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class HomeRecyclerTopHolder extends RecyclerView.ViewHolder {

    private ApkOperator mApkOperator;
    private HomeItemInfo apkItemInfo;
    private Activity mActivity;
    private BannerView bv_bannner;
    private RelativeLayout rl_free_avatar;
    private ImageView iv_free_icon;
    private TextView tv_free_remaining_time;
    private TextView tv_free_btn;
    private int screenWidth = 0;

    public HomeRecyclerTopHolder(View itemView, Activity mActivity, ApkOperator.RemoveCallback callback,ApkOperator.ChangeApkNameCallback changeCallback) {
        super(itemView);
        this.mActivity = mActivity;
        this.bv_bannner = (BannerView) itemView.findViewById(R.id.bv_bannner);
        this.rl_free_avatar = (RelativeLayout) itemView.findViewById(R.id.rl_free_avatar);
        this.iv_free_icon = (ImageView) itemView.findViewById(R.id.iv_free_icon);
        this.tv_free_remaining_time = (TextView) itemView.findViewById(R.id.tv_free_remaining_time);
        this.tv_free_btn = (TextView) itemView.findViewById(R.id.tv_free_btn);
        mApkOperator = new ApkOperator(mActivity, callback,changeCallback);
        screenWidth = DisplayUtil.getScreenWidth(mActivity);
    }

    public void intoTopHolder(List<HomeItemTopInfo> arrayHomitemTop, HomeItemInfo apkItemInfo, int position) {
        this.apkItemInfo = apkItemInfo;
        if (arrayHomitemTop == null || arrayHomitemTop.size() <= 0){
            bv_bannner.setVisibility(View.GONE);
        } else {
            bv_bannner.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (screenWidth/2.75f));
            bv_bannner.setLayoutParams(layoutParams);
            bv_bannner.setViewList(arrayHomitemTop);
            bv_bannner.setBannerClickLisener(new BannerView.BannerClickLisener() {
                @Override
                public void itemClick(HomeItemTopInfo homeItemTopInfo) {
                    TuiABrowserActivity.intentTuiABrowserA(mActivity,homeItemTopInfo.getJumpAddress(),homeItemTopInfo.getAdName());
                }
            });
            bv_bannner.startLoop(true);
        }
        if (apkItemInfo!=null){
            iv_free_icon.setImageDrawable(apkItemInfo.appModel!=null?apkItemInfo.appModel.icon:null);
        }

        rl_free_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (apkItemInfo!=null)
                mApkOperator.launchApp(apkItemInfo);
            }
        });
        tv_free_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (apkItemInfo!=null)
                mApkOperator.launchApp(apkItemInfo);
            }
        });
        isRunThread = true;
        updateTime();
        start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                updateTime();
            }
        }
    };
    private Thread thread;
    private boolean isRunThread = true;

    public void start() {
        thread = new Thread() {
            public void run() {
                while (isRunThread) {
                    try {
                        if (null == apkItemInfo) {
                            break;
                        }
                        sleep(1000);
                        if (null != apkItemInfo) {
                            handler.sendEmptyMessage(1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    /**
     * 用来更新时间，只有当前的item可见时，才去跟新倒计时
     */
    private void updateTime() {
        UpdatePriceInfo updateMessageInfo = APPUsersShareUtil.getUpdateMessage(mActivity);
        if (updateMessageInfo == null || updateMessageInfo.getMfSj() == null||apkItemInfo==null) {
            return;
        }
        long playTime = (Integer.valueOf(updateMessageInfo.getMfSj()) * 60) * 1000; //可以免费试用的时长（分钟）*60*1000 = 可以试用的毫秒时长
        long nowTime = System.currentTimeMillis(); // 系统当前时间
        long overTime = apkItemInfo.getCreateTiem() + playTime; // 试用结束时间 = 应用创建时间 + 可以免费试用的时长
        boolean isSY = (overTime - nowTime) > 0 ? true : false; // 结束时间 - 当前时间   >0 还未过期 反之 过期
        if (isSY) {
            if (apkItemInfo != null && apkItemInfo.isFreeTrialType()) {
                tv_free_remaining_time.setText(Html.fromHtml(String.format(mActivity.getResources().getString(R.string.make_apk_holder_mfsy), HistoryPayShareUtil.shareTimeChange(overTime - nowTime))));
            }
        } else {// 分身已过期
            tv_free_remaining_time.setText(mActivity.getResources().getString(R.string.mianfei_shiyong_guoqi));
            isRunThread = false;
        }
//        tv_change_name.setVisibility(View.GONE);
//        tv_delete_apk.setVisibility(View.GONE);
    }


}
