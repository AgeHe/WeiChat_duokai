package com.zhushou.weichat.ui.dialog;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lbsw.stat.LBStat;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.ui.view.ShareWeiChatDialog;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

/**
 * Created by Administrator on 2017/2/13.
 */

public class ShareWcDialog {

    private IWXAPI wxApi;
    private Resources mResources;
    private Activity mContext;
    UpdatePriceInfo messageInfo;

    public ShareWcDialog(Activity context){
        this.mContext = context;
        this.mResources = context.getResources();
        //实例化
        wxApi = WXAPIFactory.createWXAPI(context, VApp.APP_ID);
        wxApi.registerApp(VApp.APP_ID);
        show();
    }

    public void show(){
        LBStat.click(VApp.TJ_FX);
        messageInfo = APPUsersShareUtil.getUpdateMessage(mContext);
        new ShareWeiChatDialog(mContext, flag -> {
            String shareContent = mContext.getResources().getString(R.string.share_content);
            String shareTitle = mResources.getString(R.string.share_title);
            shareWX(flag,messageInfo.getShareaddress(),shareTitle,shareContent);
        }).show();
    }

    /**
     *
     * @param flag  flag == 1 分享到朋友圈  flag == 0 分享到微信会话
     * @param url
     * @param msgTitle
     * @param content
     */
    public void shareWX(int flag,String url, String msgTitle, String content){

        WXWebpageObject webpage = new WXWebpageObject();

        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = msgTitle;
        msg.description = content;

        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(mResources, R.mipmap.share_icon);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        //WXSceneFavorite 微信收藏 WXSceneSession 发给朋友 WXSceneTimeline 发到朋友圈
        switch (flag){
            case VApp.WXSceneSession:
                req.scene = SendMessageToWX.Req.WXSceneSession;
                break;
            case VApp.WXSceneTimeline:
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                break;
            case VApp.WXSceneFavorite:
                req.scene = SendMessageToWX.Req.WXSceneFavorite;
                break;
        }
        wxApi.sendReq(req);
    }
}
