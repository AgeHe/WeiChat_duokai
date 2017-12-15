package com.zhushou.weichat.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.CommonProblemInfo;

/**
 * Created by Administrator on 2017/1/13.
 */

public class CommonProblemViewHolder  extends RecyclerView.ViewHolder{
    private static final String TAG = "CommonProblemViewHolder";
    TextView tv_title;
    ImageView iv_logo;
    TextView tv_content;
    LinearLayout ll_click;
    public CommonProblemViewHolder(View itemView) {
        super(itemView);
        findView(itemView);
    }

    private Activity mActivity;
    private ClickCallback clickCallback;
    private void findView(View view){
        tv_title = (TextView) view.findViewById(R.id.tv_title);

        iv_logo = (ImageView) view.findViewById(R.id.iv_logo);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        ll_click = (LinearLayout) view.findViewById(R.id.ll_click);
    }

    public void bindTo(CommonProblemInfo itemInfo,int position,Activity mActivity,ClickCallback clickCallback){
        this.mActivity = mActivity;
        this.clickCallback = clickCallback;

        if (itemInfo==null)
            return;
        tv_title.setText(itemInfo.getTitle());

        tv_content.setText(itemInfo.getContent());
        if (itemInfo.isOpen()){
            tv_content.setVisibility(View.VISIBLE);
            tv_title.setFocusable(true);
            tv_title.setFocusableInTouchMode(true);
            tv_title.setSelected(true);
            iv_logo.setImageResource(R.mipmap.ic_arrow_top);
        }else{
            tv_title.setFocusable(false);
            tv_title.setFocusableInTouchMode(false);
            tv_title.setSelected(false);
            tv_content.setVisibility(View.GONE);
            iv_logo.setImageResource(R.mipmap.ic_arrow_bottom);
        }

        ll_click.setOnClickListener(view -> {
            clickCallback.click(itemInfo,position);
        });
    }

    public interface ClickCallback{
        void click(CommonProblemInfo itemInfo, int position);
    }

}
