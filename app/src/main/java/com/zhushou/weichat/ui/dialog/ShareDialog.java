package com.zhushou.weichat.ui.dialog;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;
import com.zhushou.weichat.utils.TwoBarCodes;

import java.io.File;


/**
 * Created by Administrator on 2016-11-01.
 */

public class ShareDialog extends Dialog implements View.OnClickListener{
    private TextView share_url,share;
    private Context mContext;
    private ImageView iv_close,image;
    UpdatePriceInfo messageInfo;
    private Bitmap imageBitmap;
    final String filePath;
    private Resources mResources;
    private IWXAPI wxApi;

    public ShareDialog(Context context) {
        super(context);
        mContext = context;
        mResources = context.getResources();
        messageInfo = APPUsersShareUtil.getUpdateMessage(mContext);
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
                + "qr_share_twobarcodes.jpg";

        //实例化
        wxApi = WXAPIFactory.createWXAPI(context, VApp.APP_ID);
        wxApi.registerApp(VApp.APP_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        init();
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new MyRunnable()).start();
    }


    private void init() {
        share_url = (TextView) findViewById(R.id.share_url);
        share = (TextView) findViewById(R.id.share_btn);
        image = (ImageView) findViewById(R.id.image);
        share.setOnClickListener(this);
        share_url.setOnClickListener(this);
        share_url.setText(messageInfo.getShareaddress());
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            image.setImageBitmap(imageBitmap);
        }
    };

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {

        String shareContent = String.format(mContext.getResources().getString(R.string.share_content),messageInfo.getShareaddress());
        switch (v.getId()) {
            case R.id.share_btn:
                shareMsg(mContext,mResources.getString(R.string.app_name),mResources.getString(R.string.app_name),shareContent);

                shareWX(0,shareContent,mResources.getString(R.string.app_name),mResources.getString(R.string.app_name));
                break;
            case R.id.share_url:
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
//                cm.setText(share_url.getText());
                cm.setText(shareContent);
                Toast.makeText(mContext, mResources.getString(R.string.fuzhi_chenggong), Toast.LENGTH_LONG).show();
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }
    /**
     * 分享功能
     *
     * @param context
     *            上下文
     * @param activityTitle
     *            Activity的名字
     * @param msgTitle
     *            消息标题
     * @param msgText
     *            消息内容
     */
    public void shareMsg(Context context,String activityTitle, String msgTitle, String msgText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); // 纯文本
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, activityTitle));
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
        Bitmap thumb = BitmapFactory.decodeResource(mResources,R.mipmap.ic_launcher);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        //WXSceneFavorite 微信收藏 WXSceneSession 发给朋友 WXSceneTimeline 发到朋友圈
        req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }

    public class MyRunnable implements Runnable {
        @Override
        public void run(){
            boolean success = TwoBarCodes.createQRImage(messageInfo.getShareaddress(), 800, 800,
                    null, filePath);
            if (success){
                imageBitmap = BitmapFactory.decodeFile(filePath);
                mHandler.sendEmptyMessage(0);
            }
        }
    }



}
