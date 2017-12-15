package com.zhushou.weichat.ui.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.UMengStatisticalKey;
import com.zhushou.weichat.bean.WxFriendMsgInfo;
import com.zhushou.weichat.ui.dialog.WsFriendDialog;
import com.zhushou.weichat.ui.view.ClickAnimationTextView;
import com.zhushou.weichat.ui.view.RoundImage;
import com.zhushou.weichat.ui.view.WsFriendViewGroup;
import com.zhushou.weichat.utils.DisplayUtil;
import com.zhushou.weichat.utils.UMengTJUtils;
import com.zhushou.weichat.utils.WsFriendJsonDataUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 */

public class WsFriendRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    String[] imgArray;
    String videoPath = "";
    private int headsize = 0;
    private Context context;
    private RoundImage rimage_head;
    private TextView tv_master_name;
    private TextView tv_master_content,tv_txt_stretch;
    private WsFriendViewGroup v_photos;
    private ImageView evp_player,iv_video_play;
    private ClickAnimationTextView cat_dianzan;
    private TextView tv_c_pinglun;
    private TextView tv_oldtime;
    private TextView tv_pinglun;
    private RelativeLayout ll_video;

   private WxFriendMsgInfo wxFriendMsgInfo;

    private int liftpadding = 0;

    public WsFriendRecyclerViewHolder(View itemView,Context context){
        super(itemView);
        this.context = context;
        ll_video = (RelativeLayout) itemView.findViewById(R.id.ll_video);
        rimage_head = (RoundImage) itemView.findViewById(R.id.rimage_head);
        tv_master_name = (TextView) itemView.findViewById(R.id.tv_master_name);
        tv_master_content = (TextView) itemView.findViewById(R.id.tv_master_content);
        tv_txt_stretch = (TextView) itemView.findViewById(R.id.tv_txt_stretch);
        v_photos = (WsFriendViewGroup) itemView.findViewById(R.id.v_photos);
        evp_player = (ImageView) itemView.findViewById(R.id.evp_player);
        iv_video_play = (ImageView) itemView.findViewById(R.id.iv_video_play);
        cat_dianzan = (ClickAnimationTextView) itemView.findViewById(R.id.cat_dianzan);
        tv_c_pinglun = (TextView) itemView.findViewById(R.id.tv_c_pinglun);
        tv_oldtime = (TextView) itemView.findViewById(R.id.tv_oldtime);
        tv_pinglun = (TextView) itemView.findViewById(R.id.tv_pinglun);
        liftpadding = DisplayUtil.dip2px(context,67);
        headsize = DisplayUtil.dip2px(context,45);
    }

    public CallInputView callInputView;
    public int position;
    public void setData(WxFriendMsgInfo itemInfo,CallInputView callInputView,int position){
        this.wxFriendMsgInfo = itemInfo;
        this.callInputView = callInputView;
        this.position = position;

        WxFriendMsgInfo.FData fData = itemInfo.getData();


        Picasso.with(context)
                .load(itemInfo.getUser_pp())
//                .resize(headsize,headsize)
//                .centerCrop()
                .config(Bitmap.Config.ALPHA_8)
                .fit()
                .into(rimage_head);
        tv_master_name.setText(itemInfo.getUser_name());

        tv_master_content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setClipboard(fData.getTextContent());
                return false;
            }
        });


        if (fData.getTextContent().length()>100){
            tv_master_content.setText(fData.getTextContent().substring(0,100));
            tv_txt_stretch.setVisibility(View.VISIBLE);
            tv_txt_stretch.setOnClickListener(this);
            isTxtStretch = true;
        }else{
            tv_master_content.setText(fData.getTextContent());
            tv_txt_stretch.setVisibility(View.GONE);
            isTxtStretch = false;
        }

        try{
            int dianzanNum = WsFriendJsonDataUtil.getDZNum(context,itemInfo.getId());
            cat_dianzan.setText(dianzanNum>itemInfo.getFavs()?String.valueOf(dianzanNum):String.valueOf(itemInfo.getFavs()));
            cat_dianzan.setOnClickListener(this);
            cat_dianzan.setTextViewValue(!WsFriendJsonDataUtil.getDianzan(context,itemInfo.getId()));
            tv_c_pinglun.setText(String.valueOf(itemInfo.getComs()));
            tv_c_pinglun.setOnClickListener(this);
            tv_oldtime.setText(getTimeAgo(itemInfo.getDate()));
        }catch (Exception e){
            Log.e("hh", "setData: "+e.getMessage());
        }

        if (itemInfo.getComments()!=null&&itemInfo.getComments().size()>0){
            tv_pinglun.setVisibility(View.VISIBLE);
            String coments = "";
            List<WxFriendMsgInfo.FComments> listComments = itemInfo.getComments();
            for (int i=0;i<listComments.size();i++){
                WxFriendMsgInfo.FComments info = listComments.get(i);
                coments += "<font color=\"#576B95\">"+info.getUname()+"</font>"+"<font color=\"#454545\">:"+info.getContent()+"</font><br>";
            }
            tv_pinglun.setText(Html.fromHtml(coments));
        }else{
            tv_pinglun.setVisibility(View.GONE);
        }

        if (fData.getVideos()!=null){
            ll_video.setVisibility(View.VISIBLE);
            v_photos.setVisibility(View.GONE);
            evp_player.setOnClickListener(this);
            videoPath = fData.getVideos();

            String[] images = fData.getPreviews();
            if (images!=null&&images.length>0){
                Picasso.with(context).load(images[0])
                        .config(Bitmap.Config.ALPHA_8)
                        .placeholder(R.mipmap.ws_friends_video_bg)
                        .into(evp_player);
            }

        }else if (fData.getPreviews()!=null){
            v_photos.setVisibility(View.VISIBLE);
            ll_video.setVisibility(View.GONE);
            if (v_photos.getChildCount()>0)
                v_photos.removeAllViews();
            v_photos.setArrayImageData(fData.getPreviews(),String.valueOf(itemInfo.getId()));
            imgArray = fData.getPreviews();
        }else{
            ll_video.setVisibility(View.GONE);
            v_photos.setVisibility(View.GONE);
        }
    }

    private boolean isTxtStretch = false;
    @Override
    public void onClick(View view){

        UMengTJUtils uMengTJUtils = new UMengTJUtils(context);
        uMengTJUtils.onEventValuePay(UMengStatisticalKey.friend_item_lcick,uMengTJUtils.friendMap(String.valueOf(wxFriendMsgInfo.getId())),0);
        switch (view.getId()){
            case R.id.evp_player:
                    new WsFriendDialog(context,videoPath,WsFriendDialog.VIDEO_PATH,0).show();
                break;
            case R.id.tv_c_pinglun:
                if (callInputView!=null)
                    callInputView.Call(position);
                break;
            case R.id.cat_dianzan:
                cat_dianzan.setSelect(wxFriendMsgInfo.getId());
                break;
            case R.id.tv_txt_stretch:
                if (isTxtStretch){
                    tv_master_content.setMaxLines(5);
                    tv_master_content.setText(wxFriendMsgInfo.getData().getTextContent().substring(0,100));
                    tv_txt_stretch.setText("全部");
                    isTxtStretch = false;
                }else{
                    tv_master_content.setMaxLines(Integer.MAX_VALUE);
                    tv_master_content.setText(wxFriendMsgInfo.getData().getTextContent());
                    tv_txt_stretch.setText("收起");
                    isTxtStretch = true;
                }

                break;
        }
    }


    public String getTimeAgo(String dateTime){
        long dataUnix = 0;
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(dateTime);
            dataUnix = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long currentTime = System.currentTimeMillis();
        if (currentTime == currentTime-dataUnix||(currentTime-dataUnix)/1000/60<15){
            return "刚刚";
        }else if ((currentTime-dataUnix)/1000/60<60){
            return (currentTime-dataUnix)/1000/60+"分钟前";
        }else if ((currentTime-dataUnix)/1000/60/60<24){
            return (currentTime-dataUnix)/1000/60/60+"小时前";
        }else if ((currentTime-dataUnix)/1000/60/60/24<30){
            return (currentTime-dataUnix)/1000/60/60/24+"天前";
        }else if ((currentTime-dataUnix)/1000/60/60/24/30<12){
            return (currentTime-dataUnix)/1000/60/60/24/30+"月前";
        }else if ((currentTime-dataUnix)/1000/60/60/24/30/12<999){
            return (currentTime-dataUnix)/1000/60/60/24/30/12+"年前";
        }
        return "刚刚";
    }

    public interface CallInputView{
        void Call(int id);
    }

    private void setClipboard(String content){
        if (android.os.Build.VERSION.SDK_INT > 11) {
            android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(ClipData.newPlainText("weichatNumber", content));
        } else {
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 将文本内容放到系统剪贴板里。
            cm.setText(content);
        }
        Toast.makeText(context, "复制成功!", Toast.LENGTH_SHORT).show();
    }

}
