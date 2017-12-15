package com.zhushou.weichat.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.CommonProblemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 */

public class CommonProblemRecyclerViewAdapter extends RecyclerView.Adapter<CommonProblemViewHolder> implements CommonProblemViewHolder.ClickCallback {

    private static final String TAG = "CommonProblemRecyclerViewAdapter";
    List<CommonProblemInfo> listData;
    Activity mActivity;
    CommonProblemInfo currentlyInfo;

    public CommonProblemRecyclerViewAdapter(Activity mActivity){
        this.mActivity = mActivity;

    }

    public void setListData(List<CommonProblemInfo> list){
        if (listData==null){
            listData = new ArrayList<>();
        }
        listData.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public CommonProblemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_common_problem_recycler,parent,false);
        return new CommonProblemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommonProblemViewHolder holder, int position) {
        holder.bindTo(listData.get(position),position,mActivity,this);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public void click(CommonProblemInfo itemInfo, int position) {
        if (currentlyInfo!=null){
            if (itemInfo!=currentlyInfo){
                currentlyInfo.setOpen(false);
            }
        }

        if (itemInfo == currentlyInfo){
            if (!itemInfo.isOpen()){
                itemInfo.setOpen(true);
                listData.set(position,itemInfo);
            }else{
                itemInfo.setOpen(false);
                listData.set(position,itemInfo);
            }
        }else{
            if (!itemInfo.isOpen()){
                itemInfo.setOpen(true);
                listData.set(position,itemInfo);
            }
        }
        currentlyInfo = itemInfo;
        notifyDataSetChanged();
    }
}
