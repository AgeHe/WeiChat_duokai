package com.zhushou.weichat.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhushou.weichat.R;
import com.zhushou.weichat.api.ZhuShouApi;
import com.zhushou.weichat.bean.AdvDfttNewsInfo;
import com.zhushou.weichat.bean.ApkItemInfo;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.utils.DebugLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * notifyItemChanged(int position) 更新列表position位置上的数据可以调用
 * notifyItemInserted(int position) 列表position位置添加一条数据时可以调用，伴有动画效果
 * notifyItemRemoved(int position) 列表position位置移除一条数据时调用，伴有动画效果
 * notifyItemMoved(int fromPosition, int toPosition) 列表fromPosition位置的数据移到toPosition位置时调用，伴有动画效果
 * notifyItemRangeChanged(int positionStart, int itemCount) 列表从positionStart位置到itemCount数量的列表项进行数据刷新
 * notifyItemRangeInserted(int positionStart, int itemCount) 列表从positionStart位置到itemCount数量的列表项批量添加数据时调用，伴有动画效果
 * notifyItemRangeRemoved(int positionStart, int itemCount) 列表从positionStart位置到itemCount数量的列表项批量删除数据时调用，伴有动画效果
 * Created by Administrator on 2017/1/7.
 */

public class MakeApkRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ApkOperator.RemoveCallback, ApkOperator.ChangeApkNameCallback {
    private static final String TAG = "MakeApkRecyclerViewAdapter";
    //    private ArrayList<ApkItemInfo> mApkItems;
    private ArrayList<ApkItemInfo> mQQItems;
    private ArrayList<ApkItemInfo> mWeiChatItems;
    private ArrayList<AdvDfttNewsInfo> mDfttNewsItems;//东方头条列表
    private Activity mActivity;
    private CLickCallback cLickCallback;

    private final int contentType = 0x01;//分身列表
    private final int bottomType = 0x02; //分身列表底部添加按钮
    private final int dfttType = 0x03;//东方头条新闻数据

    public static final int QQ_TYPE = 0xa1;
    public static final int WEICHAT_TYPE = 0xa2;
    public int itemType = 0xa2;


    public MakeApkRecyclerViewAdapter(Activity activity, CLickCallback cLickCallback) {
        this.mActivity = activity;
        this.cLickCallback = cLickCallback;
//        mApkItems = new ArrayList<>();
        mQQItems = new ArrayList<>();
        mWeiChatItems = new ArrayList<>();
        mDfttNewsItems = new ArrayList<>();
    }

//    public void setApkItems(ArrayList<ApkItemInfo> apkItems) {
//        mApkItems = apkItems;
//        notifyDataSetChanged();
//    }

    public void setQQApkItems(ArrayList<ApkItemInfo> apkItems) {
        mQQItems = apkItems;
//        notifyDataSetChanged();
    }

    public void setWeiChatItems(ArrayList<ApkItemInfo> apkItems) {
        mWeiChatItems = apkItems;
        notifyItemRangeChanged(0, getItemCount());
        ZhuShouApi.getArrayDFTTAdv(0, dfttHandler);
    }

    private void refreshDfttNewsItems(){
        notifyItemRangeChanged(getAdapterApkList().size()+1,getAdapterApkList().size()+1+mDfttNewsItems.size());
    }

    public void selectType(int selectItemType) {
//        itemType = selectItemType;
        notifyDataSetChanged();
    }

    public void addApkItem(ApkItemInfo apkItem, int itemType) {
        if (itemType == QQ_TYPE) {
            mQQItems.add(apkItem);
            this.itemType = itemType;
            notifyItemInserted(mQQItems.size() + 1);
        }
        if (itemType == WEICHAT_TYPE) {
            mWeiChatItems.add(apkItem);
            this.itemType = itemType;
            notifyItemInserted(mWeiChatItems.size() + 1);
        }
    }

    @Override
    public void changeItem(ApkItemInfo itemInfo, int position) {
//        notifyDataSetChanged();
        notifyItemChanged(position);
    }

    @Override
    public void removeItem(ApkItemInfo itemInfo) {
        if (itemType == QQ_TYPE) {
            mQQItems.remove(itemInfo);
        }
        if (itemType == WEICHAT_TYPE) {
            mWeiChatItems.remove(itemInfo);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getAdapterApkList().size()) {
            return bottomType;
        } else if (position > getAdapterApkList().size() + 1) {
            return dfttType;
        } else {
            return contentType;
        }
    }

    public void refreshShareItem() {
        if (mWeiChatItems.size() > 0)
            notifyItemChanged(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == bottomType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_weichat_bottom, parent, false);
        }else if (viewType == dfttType){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_weichat_bottom, parent, false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_make_apk_list, parent, false);
        }
        return viewType == bottomType ? new AddWeiChatBottomHolder(view) : viewType == bottomType ? new MakeApkViewHolder(mActivity, view, this, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof MakeApkViewHolder && getAdapterApkList().size() > 0) {
                ((MakeApkViewHolder) holder).bindTo(getAdapterApkList().get(position), position, itemType);
                ((MakeApkViewHolder) holder).tv_delete_apk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cLickCallback != null)
                            cLickCallback.adapterItemAddDeskTop(getAdapterApkList().get(position), position);
                        //                        cLickCallback.adapterItemDelete(getAdapterList().get(position));
                    }
                });
            }
            if (holder instanceof AddWeiChatBottomHolder) {

                ((AddWeiChatBottomHolder) holder).itemView.setOnClickListener(view -> {
                    if (cLickCallback != null)
                        cLickCallback.addWeiChatBottom();

                });
                ((AddWeiChatBottomHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (cLickCallback != null)
                            cLickCallback.clearWxModel();
                        return false;
                    }
                });

            }
        } catch (Exception e) {
            Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (holder instanceof MakeApkListTabHolder) {
            TextView tv_weichat = ((MakeApkListTabHolder) holder).tv_tab_weichat;
            TextView tv_qq = ((MakeApkListTabHolder) holder).tv_tab_qq;
            tv_weichat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectType(WEICHAT_TYPE);
                }
            });

            tv_qq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectType(QQ_TYPE);
                }
            });

            if (itemType == QQ_TYPE) {
                GradientDrawable tv_tab_qq_grad = (GradientDrawable) tv_qq.getBackground();
                tv_tab_qq_grad.setColor(Color.parseColor("#17c782"));
                tv_qq.setTextColor(Color.parseColor("#ffffff"));

                GradientDrawable tv_tab_weichat_grad = (GradientDrawable) tv_weichat.getBackground();
                tv_tab_weichat_grad.setColor(Color.parseColor("#ffffff"));
                tv_weichat.setTextColor(Color.parseColor("#17c782"));
            } else if (itemType == WEICHAT_TYPE) {
                GradientDrawable tv_tab_qq_grad = (GradientDrawable) tv_qq.getBackground();
                tv_tab_qq_grad.setColor(Color.parseColor("#ffffff"));
                tv_qq.setTextColor(Color.parseColor("#17c782"));

                GradientDrawable tv_tab_weichat_grad = (GradientDrawable) tv_weichat.getBackground();
                tv_tab_weichat_grad.setColor(Color.parseColor("#17c782"));
                tv_weichat.setTextColor(Color.parseColor("#ffffff"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mWeiChatItems.size()+getAdapterApkList().size() + 1;
    }

    public ArrayList<ApkItemInfo> getAdapterApkList() {
        return mWeiChatItems;
    }

    public interface CLickCallback {
        void adapterItemDelete(ApkItemInfo apkItemInfo);

        void addWeiChatBottom();

        void adapterItemAddDeskTop(ApkItemInfo apkItemInfo, int position);

        void clearWxModel();
    }


    /**
     * Type Holder
     */
    public class MakeApkListTabHolder extends RecyclerView.ViewHolder {
        public TextView tv_tab_weichat;
        public TextView tv_tab_qq;

        public MakeApkListTabHolder(View itemView) {
            super(itemView);
            tv_tab_weichat = (TextView) itemView.findViewById(R.id.tv_tab_weichat);
            tv_tab_qq = (TextView) itemView.findViewById(R.id.tv_tab_qq);
        }
    }

    public class AddWeiChatBottomHolder extends RecyclerView.ViewHolder {

        public AddWeiChatBottomHolder(View itemView) {
            super(itemView);
        }
    }


    private Handler dfttHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    OkHttpResult okHttpResult = (OkHttpResult) msg.obj;
                    if (okHttpResult.isSuccess) {

                        try {
                            JSONObject dfttJo = new JSONObject(okHttpResult.msg);
                            JSONArray ttja = dfttJo.getJSONArray("data");
                            for (int i = 0; i < ttja.length(); i++) {
                                JSONObject jo = ttja.getJSONObject(i);
                                AdvDfttNewsInfo advDfttNewsInfo = new AdvDfttNewsInfo();
                                advDfttNewsInfo.setPk(jo.getString("pk"));
                                advDfttNewsInfo.setTitle(jo.getString("title"));
                                advDfttNewsInfo.setDate(jo.getString("date"));
                                advDfttNewsInfo.setCategory(jo.getString("category"));
                                advDfttNewsInfo.setAuthor_name(jo.getString("author_name"));
                                advDfttNewsInfo.setUrl(jo.getString("url"));
                                advDfttNewsInfo.setThumbnail_pic_s(jo.getString("thumbnail_pic_s"));
                                try {
                                    advDfttNewsInfo.setThumbnail_pic_s2(jo.getString("thumbnail_pic_s2"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    DebugLog.e(e.getMessage());
                                }
                                try {
                                    advDfttNewsInfo.setThumbnail_pic_s3(jo.getString("thumbnail_pic_s3"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    DebugLog.e(e.getMessage());
                                }
                                advDfttNewsInfo.setThumbnail_pic_h(jo.getInt("thumbnail_pic_w"));
                                advDfttNewsInfo.setThumbnail_pic_w(jo.getInt("thumbnail_pic_h"));

                                if (mDfttNewsItems==null)
                                    mDfttNewsItems = new ArrayList<>();
                                mDfttNewsItems.add(advDfttNewsInfo);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }

        }
    };

}
