package com.zhushou.weichat.screenshot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.zhushou.weichat.R;


/**
 * Created by zhanglinkai on 2017/3/17.
 * 功能:
 */

public class FaceRecycleAdapter extends RecyclerView.Adapter<FaceRecycleAdapter.MyViewHolder> {
    private faceClick click;
    private List<String> list;
    private Context context;
    public FaceRecycleAdapter(Context context, List<String> list, faceClick click){
        this.context=context;
        this.list=list;
        this.click=click;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.face_recycle_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Picasso.with(context).load(list.get(position)).into(holder.face_iv);
        holder.face_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.click(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView face_iv;
        public MyViewHolder(View view) {
            super(view);
            face_iv=(ImageView)view.findViewById(R.id.face_iv);
        }
    }

    public interface faceClick{
        void click(int position);
    }
}
