package com.zhushou.weichat.uinew.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.uinew.info.AdInfo;
import com.zhushou.weichat.uinew.info.HomeItemInfo;
import com.zhushou.weichat.utils.DisplayUtil;
import com.zhushou.weichat.utils.LoadImageUtil;

/**
 * Created by Administrator on 2017/11/27.
 */

public class HomeRecyclerHolder extends RecyclerView.ViewHolder {


    private ImageView iv_app_icon,iv_corner;
    private TextView tv_app_name;
    private RelativeLayout rl_home_item;
    private Activity mActivity;
    private int screenWidth = 0;


    public HomeRecyclerHolder(View itemView, Activity mActivity) {
        super(itemView);
        this.mActivity = mActivity;
        iv_app_icon = (ImageView) itemView.findViewById(R.id.iv_app_icon);
        iv_corner = (ImageView) itemView.findViewById(R.id.iv_corner);
        tv_app_name = (TextView) itemView.findViewById(R.id.tv_app_name);
        rl_home_item = (RelativeLayout) itemView.findViewById(R.id.rl_home_item);
        screenWidth = DisplayUtil.getScreenWidth(mActivity);
    }

    public void initTo(HomeItemInfo homeItemInfo,int position){
        int lenth = (screenWidth-DisplayUtil.dip2px(mActivity,3))/3;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,lenth);
        rl_home_item.setLayoutParams(layoutParams);

        int cornerLenth = (int) (lenth*0.4f);
        RelativeLayout.LayoutParams cornerLayoutParams = new RelativeLayout.LayoutParams(cornerLenth,cornerLenth);
        iv_corner.setLayoutParams(cornerLayoutParams);

        if (homeItemInfo==null)
            return;

//        iv_app_icon.setScaleType(ImageView.ScaleType.FIT_XY);
        if (homeItemInfo.adInfo!=null){ //广告类型
            iv_corner.setVisibility(View.VISIBLE);
            AdInfo adInfo = homeItemInfo.adInfo;
            String cornerurl = adInfo.getCornerUrl();
            String iconUrl = adInfo.getIconUrl();
            if (cornerurl.endsWith(".gif")){
                LoadImageUtil.loadImageGif(mActivity,cornerurl,iv_corner);
            }else{
                LoadImageUtil.loadImage(mActivity,cornerurl,iv_corner);
            }
            if (iconUrl.endsWith(".gif")){
                LoadImageUtil.loadImageGif(mActivity,iconUrl,iv_app_icon);
            }else{
                LoadImageUtil.loadImage(mActivity,iconUrl,iv_app_icon);
            }
            tv_app_name.setText(adInfo.getName());
        }else if (homeItemInfo.isVip()){ //VIP类型
            iv_app_icon.setImageResource(R.mipmap.ic_homepage_vip);
            tv_app_name.setText("VIP推荐");
            iv_corner.setImageResource(R.mipmap.icon_lable_vip);
            iv_corner.setVisibility(View.VISIBLE);
        }else if (homeItemInfo.getLauncherType()==HomeItemInfo.Launcher_ALL_APP){
            if (homeItemInfo.appModel==null)
                return;
            iv_app_icon.setImageDrawable(homeItemInfo.appModel.icon);
            tv_app_name.setText(homeItemInfo.appModel.name);
            iv_corner.setVisibility(View.GONE);
        }else{ //分身
            iv_app_icon.setImageDrawable(homeItemInfo.appModel!=null&&homeItemInfo.appModel.icon!=null?homeItemInfo.appModel.icon:null);
            tv_app_name.setText(homeItemInfo.getTitle());
            iv_corner.setVisibility(View.GONE);
        }

        //广告或者分身名字过长 做滚动处理
        if (tv_app_name.getText().length()>7){
            tv_app_name.setFocusable(true);
            tv_app_name.setFocusableInTouchMode(true);
            tv_app_name.setSelected(true);
        }else{
            tv_app_name.setFocusable(false);
            tv_app_name.setFocusableInTouchMode(false);
            tv_app_name.setSelected(false);
        }
    }

}
