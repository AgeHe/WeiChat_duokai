package com.zhushou.weichat.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.ApkItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */

public class ItemVirtualAppsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemVirtualAppsViewHolder.ChangeApkNameCallback {

    private final int contentType = 0x01;
    private final int bottomType = 0x02;
    public VirtualAppsCallback virtualAppsCallback;
    public List<ApkItemInfo> dataArray = null;
    public Activity mActivity;
    public ItemVirtualAppsRecyclerAdapter(Activity mActivity){
        this.mActivity = mActivity;
        dataArray = new ArrayList<>();
    }

    public void setClickCallback(VirtualAppsCallback virtualAppsCallback){
        this.virtualAppsCallback = virtualAppsCallback;
    }

    public void setArrayData(List<ApkItemInfo> dataArray){
        if (this.dataArray==null){
            this.dataArray = new ArrayList<>();
        }
        this.dataArray.clear();
        this.dataArray.addAll(dataArray);
        notifyDataSetChanged();
    }

    public void setArrayDataItem(ApkItemInfo info){
        if (this.dataArray==null){
            this.dataArray = new ArrayList<>();
        }
        this.dataArray.add(info);
        notifyItemChanged(dataArray.size()-1);
    }


    @Override
    public int getItemViewType(int position) {
        if (position==getAdapterList().size()){
            return bottomType;
        }else{
            return contentType;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType==bottomType){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_weichat_bottom, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_make_apk_list, parent, false);
        }
        return viewType==bottomType?new AddBottomHolder(view):new ItemVirtualAppsViewHolder(view,mActivity,this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

       if (holder instanceof ItemVirtualAppsViewHolder){
            ((ItemVirtualAppsViewHolder) holder).initTo(dataArray.get(position),position);
           ((ItemVirtualAppsViewHolder) holder).tv_delete_apk.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   virtualAppsCallback.adapterItemAddDeskTop(dataArray.get(position),position);
               }
           });

        }else if (holder instanceof AddBottomHolder){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (virtualAppsCallback!=null)
                        virtualAppsCallback.addAppBottom();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getAdapterList().size()+1;
    }

    public List<ApkItemInfo> getAdapterList(){
        return dataArray;
    }

    @Override
    public void changeItem(ApkItemInfo itemInfo, int position) {
            notifyDataSetChanged();
    }

    public class AddBottomHolder extends  RecyclerView.ViewHolder {

        public AddBottomHolder(View itemView) {
            super(itemView);
        }
    }

    public interface VirtualAppsCallback{
        void addAppBottom();
        void adapterItemAddDeskTop(ApkItemInfo apkItemInfo,int position);
    }
}
