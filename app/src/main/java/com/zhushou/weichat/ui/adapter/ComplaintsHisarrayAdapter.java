package com.zhushou.weichat.ui.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.ComplaintsArrayInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/29.
 */

public class ComplaintsHisarrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mActivity;

    private List<ComplaintsArrayInfo> arrayInfos;

    public ComplaintsHisarrayAdapter(Activity mActivity){
        this.mActivity = mActivity;
        arrayInfos = new ArrayList<>();
    }

    public void setArrayData(List<ComplaintsArrayInfo> arrayInfos){
        if (arrayInfos==null)
            this.arrayInfos = new ArrayList<>();
        else
            this.arrayInfos.clear();
        this.arrayInfos.addAll(arrayInfos);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_complaints_hisarray,parent,false);
        return new ComplaintsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ComplaintsViewHolder){
            ((ComplaintsViewHolder) holder).initToView(arrayInfos.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return arrayInfos.size();
    }

    public class ComplaintsViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_date_time,tv_complaints_type,tv_complaints_status;

        public ComplaintsViewHolder(View itemView) {
            super(itemView);
            tv_date_time = (TextView) itemView.findViewById(R.id.tv_date_time);
            tv_complaints_type = (TextView) itemView.findViewById(R.id.tv_complaints_type);
            tv_complaints_status = (TextView) itemView.findViewById(R.id.tv_complaints_status);
        }

        public void initToView(ComplaintsArrayInfo complaintsArrayInfo){
            if (complaintsArrayInfo!=null){
                tv_date_time.setText(complaintsArrayInfo.getDate());
                tv_complaints_type.setText(complaintsArrayInfo.getId());

                if (!complaintsArrayInfo.getResponse().equals("")){
                    tv_complaints_status.setClickable(true);
                    tv_complaints_status.setTextColor(0xff3595C7);
                    tv_complaints_status.setText("点击查看结果");
                }else{
                    tv_complaints_status.setTextColor(0xff333333);
                    tv_complaints_status.setText("处理中");
                    tv_complaints_status.setClickable(false);
                }

                tv_complaints_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!complaintsArrayInfo.getResponse().equals("")){
                            new AlertDialog.Builder(mActivity)
                                    .setTitle("反馈结果")
                                    .setMessage(complaintsArrayInfo.getResponse())
                                    .setCancelable(true)
                                    .setNegativeButton("复制内容", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            setClipboard(complaintsArrayInfo.getResponse());
                                        }
                                    })
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    }).show();
                        }
                    }
                });
                tv_date_time.setFocusable(true);
                tv_date_time.setFocusableInTouchMode(true);
                tv_date_time.setSelected(true);
            }

        }

    }

    private void setClipboard(String content){
        if (android.os.Build.VERSION.SDK_INT > 11) {
            android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) mActivity.getSystemService(mActivity.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(ClipData.newPlainText("weichatNumber", content));
        } else {
            ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
            // 将文本内容放到系统剪贴板里。
            cm.setText(content);
        }
        Toast.makeText(mActivity, "复制成功!", Toast.LENGTH_SHORT).show();
    }

}
