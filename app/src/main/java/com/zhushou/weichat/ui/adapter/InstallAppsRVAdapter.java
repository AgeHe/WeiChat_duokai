package com.zhushou.weichat.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.ui.models.AppModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/4.
 */

public class InstallAppsRVAdapter extends RecyclerView.Adapter<InstallAppsRVAdapter.InstallAppsHolder> {

    private List<AppModel> arrayDatas;

    private Context context;
    private ClickCallback clickCallback;
    public InstallAppsRVAdapter(Context context){
        this.context = context;
    }

    public void setArrayData(List<AppModel> arrayDatas){
        if (this.arrayDatas==null)
            this.arrayDatas = new ArrayList<>();
        else
            this.arrayDatas.clear();

        this.arrayDatas.addAll(arrayDatas);
        notifyDataSetChanged();
    }

    public void setCallBack(ClickCallback clickCallback){
        this.clickCallback = clickCallback;
    }

    @Override
    public InstallAppsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_install_apps,parent,false);
        return new InstallAppsHolder(view);
    }

    @Override
    public void onBindViewHolder(InstallAppsHolder holder, int position) {
        AppModel appModel = arrayDatas.get(position);
        holder.iv_app_icon.setImageDrawable(appModel.icon);
        holder.tv_app_name.setText(appModel.name);
        if (position==arrayDatas.size()-1){
            holder.view_line.setVisibility(View.GONE);
        }else{
            holder.view_line.setVisibility(View.VISIBLE);
        }
        holder.tv_app_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickCallback!=null)
                    clickCallback.callback(appModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (arrayDatas==null)
            arrayDatas = new ArrayList<>();
        return arrayDatas.size();
    }

    class InstallAppsHolder extends RecyclerView.ViewHolder{
        ImageView iv_app_icon;
        TextView  tv_app_add;
        TextView tv_app_name;
        View view_line;
        public InstallAppsHolder(View itemView) {
            super(itemView);
            iv_app_icon = (ImageView) itemView.findViewById(R.id.iv_app_icon);
            tv_app_add = (TextView) itemView.findViewById(R.id.tv_app_add);
            tv_app_name = (TextView) itemView.findViewById(R.id.tv_app_name);
            view_line = itemView.findViewById(R.id.view_line);
        }
    }

    public interface ClickCallback{
        void callback(AppModel itemModel);
    }
}
