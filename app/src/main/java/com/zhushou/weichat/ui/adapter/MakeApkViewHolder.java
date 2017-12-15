package com.zhushou.weichat.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.ApkItemInfo;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;
import com.zhushou.weichat.utils.HistoryPayShareUtil;

/**
 * Created by Administrator on 2017/1/7.
 */
public class MakeApkViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "MakeApkViewHolder";
    ImageView iv_apk_icon; //??????
    TextView tv_apk_name; //???????
    TextView tv_apk_createdate; // ??????????
    TextView tv_change_name; // ?????????
    TextView tv_delete_apk; // ??????
    LinearLayout ll_item;
    UpdatePriceInfo updateMessageInfo;



    private void initView(View view){
        ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
        iv_apk_icon = (ImageView) view.findViewById(R.id.iv_apk_icon);
        tv_apk_name = (TextView) view.findViewById(R.id.tv_apk_name);
        tv_apk_createdate = (TextView) view.findViewById(R.id.tv_apk_createdate);
        tv_change_name = (TextView) view.findViewById(R.id.tv_change_name);
        tv_delete_apk = (TextView) view.findViewById(R.id.tv_delete_apk);
    }

    private ApkItemInfo mApkItem; // Apk??
    private Context mContext; // ??????
    private ApkOperator mApkOperator; // Apk????
    private int listArrayPsition;

    public MakeApkViewHolder(Activity activity, View itemView, ApkOperator.RemoveCallback callback,ApkOperator.ChangeApkNameCallback changeCallback) {
        super(itemView);
        initView(itemView);
        mContext = activity.getApplicationContext();
        mApkOperator = new ApkOperator(activity, callback,changeCallback); // Apk????
    }

    public void bindTo(ApkItemInfo ApkItem,int position,int itemType){
        this.listArrayPsition = position;
        this.mApkItem = ApkItem;
        if (mApkItem==null)
            return;

        if (mApkItem.getTitle()!=null)
            tv_apk_name.setText(mApkItem.getTitle());

//        if (ApkItem.getAppModel()!=null&&ApkItem.getAppModel().icon!=null){
//            Drawable iconDeawable = ApkItem.getAppModel().icon;
//            iv_apk_icon.setImageDrawable(iconDeawable);
//        }else{
            iv_apk_icon.setImageDrawable(mContext.getResources()
                    .getDrawable(R.mipmap.icon_weichat));
//            iv_apk_icon.setVisibility(View.GONE);
//        }


        String cjsj = mContext.getResources().getString(R.string.chuang_jian_shi_jian);
        if (ApkItem.isFreeTrialType()){
            isRunThread = true;
            updateTime();
//            UpdatePriceInfo updateMessageInfo = APPUsersShareUtil.getUpdateMessage(mContext);
//            long millisecond = (long) (Integer.valueOf(updateMessageInfo.getSysj())*60*60)*1000;
//            long thisMillisecond = millisecond+mApkItem.getCreateTiem()-System.currentTimeMillis();
//            if (thisMillisecond>0){
//                cjsj = mContext.getResources().getString(R.string.chuang_jian_shi_jian);
//                if (mApkItem !=null&&mApkItem.isFreeTrialType()){
//                    tv_apk_name.setText(Html.fromHtml(mContext.getResources().getString(R.string.mianfei_shiyong)));
//                    if (updateMessageInfo.getScfy()==null)
//                        return;
////                    cjsj+mApkItem.getCreateDate()+
//                    tv_apk_createdate.setText(Html.fromHtml(String.format(mContext.getResources().getString(R.string.make_apk_holder_mfsy), HistoryPayShareUtil.getFormatedDateTime("HH:mm:ss",thisMillisecond))));
////                    tv_change_name.setVisibility(View.GONE);
////                    tv_delete_apk.setVisibility(View.GONE);
//                }
//            }else{ // 分身已过期
//                tv_apk_createdate.setText(mContext.getResources().getString(R.string.mianfei_shiyong_guoqi));
//            }
//            tv_change_name.setVisibility(View.GONE);
//            tv_delete_apk.setVisibility(View.GONE);
            start();
        }else{
            tv_change_name.setVisibility(View.VISIBLE);
            tv_delete_apk.setVisibility(View.VISIBLE);
            tv_apk_createdate.setVisibility(View.GONE);
            if (mApkItem.getCreateDate()!=null){
                tv_apk_createdate.setText(cjsj+mApkItem.getCreateDate());
            }
        }

        //?????????????
        tv_change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApkOperator.changeApkName(mApkItem,listArrayPsition);
            }
        });

        //???????????
        tv_delete_apk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApkOperator.deleteApk(mApkItem);
            }
        });

        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApkOperator.launchApp(mApkItem);
            }
        });
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what ==1) {
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
                        if (null == mApkItem ) {
                            break;
                        }
                        sleep(1000);
                        if(null != mApkItem ) {
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

    /**用来更新时间，只有当前的item可见时，才去跟新倒计时*/
    private void updateTime(){
        if (mApkItem.isFreeTrialType()){
            if(updateMessageInfo==null){
                updateMessageInfo = APPUsersShareUtil.getUpdateMessage(mContext);
            }

//        updateMessageInfo.getSysj()
            if (updateMessageInfo==null||updateMessageInfo.getMfSj()==null){
//                Toast.makeText(mContext,mContext.getString(R.string.lianjie_shibai),Toast.LENGTH_SHORT).show();
                return;
            }

//            long millisecond = (long) (Integer.valueOf(updateMessageInfo.getSysj())*60)*1000;
//            long thisMillisecond =  millisecond+mApkItem.getCreateTiem()-System.currentTimeMillis();

            long playTime = (Integer.valueOf(updateMessageInfo.getMfSj())*60)*1000; //可以免费试用的时长（分钟）*60*1000 = 可以试用的毫秒时长

            long nowTime = System.currentTimeMillis(); // 系统当前时间

            long overTime = mApkItem.getCreateTiem()+playTime; // 试用结束时间 = 应用创建时间 + 可以免费试用的时长

            boolean isSY = (overTime - nowTime)>0?true:false; // 结束时间 - 当前时间   >0 还未过期 反之 过期

            if (isSY){
                if (mApkItem !=null&&mApkItem.isFreeTrialType()){
                    tv_apk_name.setText(Html.fromHtml(mContext.getResources().getString(R.string.mianfei_shiyong)));
//                    if (updateMessageInfo.getScfy()==null)
//                        return;
//               cjsj+mApkItem.getCreateDate()+
                    tv_apk_createdate.setText(Html.fromHtml(String.format(mContext.getResources().getString(R.string.make_apk_holder_mfsy), HistoryPayShareUtil.shareTimeChange(overTime - nowTime))));
//                tv_change_name.setVisibility(View.GONE);
//                tv_delete_apk.setVisibility(View.GONE);
                }
            }else{// 分身已过期
                tv_apk_createdate.setText(mContext.getResources().getString(R.string.mianfei_shiyong_guoqi));
//            thread.interrupt();
                isRunThread = false;
            }
            tv_change_name.setVisibility(View.GONE);
            tv_delete_apk.setVisibility(View.GONE);
        }
    }
}
