package com.zhushou.weichat.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhushou.weichat.R;

/**
 * Created by Administrator on 2017/12/15.
 */

public class MakeDfttRecyclerViewHolder extends RecyclerView.ViewHolder {

    LinearLayout ll_news_item_1,ll_image_container;
    RelativeLayout ll_news_item_2;
    TextView tv_news_title,tv_news_title2;//标题
    TextView tv_news_tag,tv_news_tag2; //新闻数据标识
    ImageView iv_news_image2;


    public MakeDfttRecyclerViewHolder(View itemView) {
        super(itemView);
        ll_news_item_1 = (LinearLayout) itemView.findViewById(R.id.ll_news_item_1);
        ll_image_container = (LinearLayout) itemView.findViewById(R.id.ll_image_container);
        ll_news_item_2 = (RelativeLayout) itemView.findViewById(R.id.ll_news_item_2);
        tv_news_title = (TextView) itemView.findViewById(R.id.tv_news_title);
        ll_news_item_1 = (LinearLayout) itemView.findViewById(R.id.ll_news_item_1);
        ll_news_item_1 = (LinearLayout) itemView.findViewById(R.id.ll_news_item_1);
        ll_news_item_1 = (LinearLayout) itemView.findViewById(R.id.ll_news_item_1);


    }
}
