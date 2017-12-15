package com.zhushou.weichat.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.WxFriendMsgInfo;
import com.zhushou.weichat.ui.WsFriendActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 */

public class WsFriendRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements WsFriendActivity.InputInterfac,
WsFriendRecyclerViewHolder.CallInputView{

    public StartInput startInput;
    private List<WxFriendMsgInfo> listData;
    public int TYPE_CONTENT = 0xa1;
    public int TYPE_DOWN_REFRESH = 0xa2;
    public boolean noMore = false;

    public WsFriendRecyclerAdapter(StartInput startInput){
        listData = new ArrayList<>();
        this.startInput = startInput;
    }

    public WsFriendActivity.InputInterfac getInputInterFac(){
        return this;
    }

    @Override
    public int getItemViewType(int position) {
        if (listData==null)
            return TYPE_CONTENT;
        if (listData.size()==position){
            return TYPE_DOWN_REFRESH;
        }else{
            return TYPE_CONTENT;
        }
    }

    public void setListData(List<WxFriendMsgInfo> arrayData){
        if (listData==null){
            listData = new ArrayList<>();
        }else{
            listData.clear();
        }
        listData.addAll(arrayData);
        notifyDataSetChanged();

    }

    public void addListData(List<WxFriendMsgInfo> arrayDatas){
        if (listData==null)
            listData = new ArrayList<>();
        if (arrayDatas.size()==0){
            noMore = true;
            notifyDataSetChanged();
        }else{
            listData.addAll(arrayDatas);
            notifyDataSetChanged();
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType==TYPE_DOWN_REFRESH){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_without,parent,false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wxfriend,parent,false);
        }
        return viewType==TYPE_CONTENT?new WsFriendRecyclerViewHolder(view,parent.getContext()):new LoadingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WsFriendRecyclerViewHolder){
            ((WsFriendRecyclerViewHolder) holder).setData(listData.get(position),this,position);
        }
        if (holder instanceof LoadingViewHolder){
            if (noMore&&listData.size()>=7){
                ((LoadingViewHolder) holder).setData(loading_n);
            }else{
                ((LoadingViewHolder) holder).setData(listData.size()>=7?loading:loading_s);
            }

        }
    }

    @Override
    public int getItemCount(){
        if (listData==null||listData.size()<=0)
            return 0;
        return listData.size()+1;
    }

    public final int loading = 0x01;
    public final int loading_s = 0x02;
    public final int loading_n = 0x03;

    @Override
    public void getInputData(int id, String input) {
        if (input==null||input.equals(""))
            return;
        WxFriendMsgInfo info =  listData.get(id);
        WxFriendMsgInfo.FComments fInfo = new WxFriendMsgInfo.FComments();
        fInfo.setUname("游客");
        fInfo.setContent(input);
        info.getComments().add(fInfo);
        listData.remove(id);
        listData.add(id,info);
        notifyDataSetChanged();
    }

    @Override
    public void Call(int id) {
        if (startInput!=null)
            startInput.startInput(id);
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder{

        ProgressBar pb_loading;
        TextView tv_loading_content;
        LinearLayout ll_loading;
        public LoadingViewHolder(View itemView){
            super(itemView);
            pb_loading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
            tv_loading_content = (TextView) itemView.findViewById(R.id.tv_loading_content);
            ll_loading = (LinearLayout) itemView.findViewById(R.id.ll_loading);
        }

        public void setData(int lodingType){

            switch (lodingType){
                case loading:
                    pb_loading.setVisibility(View.VISIBLE);
                    tv_loading_content.setText("加载中...");
                    break;
                case loading_s:
                    pb_loading.setVisibility(View.GONE);
                    tv_loading_content.setVisibility(View.GONE);
                    ll_loading.setVisibility(View.GONE);
                    break;
                case loading_n:
                    pb_loading.setVisibility(View.GONE);
                    tv_loading_content.setText("没有更多");
                    break;
            }

        }
    }

    public interface StartInput{
        void startInput(int listIndex);
    }
}
