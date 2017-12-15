package com.zhushou.weichat.uinew.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhushou.weichat.R;
import com.zhushou.weichat.uinew.info.ComsuptionInfo;
import com.zhushou.weichat.utils.database.DBLibs;

import java.util.List;

/**
 * Created by Administrator on 2017/1/15.
 */

public class ExceptionOrderRVAdapter extends RecyclerView.Adapter<ExceptionOrderRecyclerHolder> {

    private Activity mActivity;
    List<ComsuptionInfo> listData;

    public ExceptionOrderRVAdapter(Activity mActivity){
        this.mActivity = mActivity;
    }

    public void setAdapterData(List<ComsuptionInfo> listData){
        this.listData = listData;
    }

    @Override
    public ExceptionOrderRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_exception_order_rv,parent,false);
        return new ExceptionOrderRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(ExceptionOrderRecyclerHolder holder, int position) {
        holder.bindToData(mActivity,listData.get(position),position);
        holder.setArrayListDataFsh(new ExceptionOrderRecyclerHolder.ArrayListDataFsh() {
            @Override
            public void refresh(int position) {
                listData.get(position).setPayComplateStatus(DBLibs.Pay_status_failure_finish);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
