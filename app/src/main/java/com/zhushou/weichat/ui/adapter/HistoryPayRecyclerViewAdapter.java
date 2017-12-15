package com.zhushou.weichat.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.AppUsersInfo;
import com.zhushou.weichat.bean.HistoryPayInfo;
import com.zhushou.weichat.bean.VipBaoYueInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/1/15.
 */

public class HistoryPayRecyclerViewAdapter extends RecyclerView.Adapter<HistoryPayRecyclerHolder> {

    private Activity mActivity;
    List<HistoryPayInfo> listData;

    public HistoryPayRecyclerViewAdapter(Activity mActivity){
        this.mActivity = mActivity;
    }

    public void setAdapterData(List<HistoryPayInfo> listData){
        this.listData = listData;
    }

    @Override
    public HistoryPayRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_layout_pay_history,parent,false);
        return new HistoryPayRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryPayRecyclerHolder holder, int position) {
        holder.bindToData(mActivity,listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
