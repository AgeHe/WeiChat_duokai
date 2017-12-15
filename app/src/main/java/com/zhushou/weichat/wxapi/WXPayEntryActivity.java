package com.zhushou.weichat.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.addfriends.ui.AddFriendsFunctionA;
import com.zhushou.weichat.auxiliary.ui.HFActivity;
import com.zhushou.weichat.ui.MainActivity;
import com.zhushou.weichat.ui.pay.MyDialog;
import com.zhushou.weichat.ui.pay.SmallMerchantPayDialog;

/** 微信客户端回调activity示例 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	private static final String TAG = "WXPayEntryActivity";
	private IWXAPI api;
	private static boolean isExit;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//注册API
		api = WXAPIFactory.createWXAPI(this, VApp.APP_ID);
		api.handleIntent(getIntent(), this);
	}
	@Override
	public void onReq(BaseReq baseReq) {

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	//  发送到微信请求的响应结果
	Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			switch (msg.what){
				case 0:
					if (VApp.getInstance().getHfActivity()!=null){
						HFActivity hfActivity = VApp.getInstance().getHfActivity();
						hfActivity.successfulclick(
							SmallMerchantPayDialog.getApkName()!=null?SmallMerchantPayDialog.apkName:""
							,hfActivity.getHelper().getType()
							,hfActivity.getHelper().getWaretype());
						break;
					}
					if (VApp.getInstance().getHbActivity()!=null){
						VApp.getInstance().getHbActivity().successfulclick(
								SmallMerchantPayDialog.getApkName()!=null?SmallMerchantPayDialog.apkName:""
								,VApp.getInstance().getHbActivity().getHelper().getType()
								,VApp.getInstance().getHbActivity().getHelper().getWaretype());
						break;
					}
					if (VApp.getInstance().getaddFriendsFunctionA()!=null){
						AddFriendsFunctionA addFriendsFunctionA = VApp.getInstance().getaddFriendsFunctionA();
						addFriendsFunctionA.successfulclick(
								SmallMerchantPayDialog.getApkName()!=null?SmallMerchantPayDialog.apkName:""
								,addFriendsFunctionA.getHelper().getType()
								,addFriendsFunctionA.getHelper().getWaretype());
						break;
					}
					if (VApp.getInstance().getaActivity()!=null){
						MainActivity mainActivity = VApp.getInstance().getaActivity();
						mainActivity.successfulclick(
								MyDialog.getApkName()==null?SmallMerchantPayDialog.apkName:MyDialog.getApkName()
								,VApp.getInstance().getaActivity().getHelper().getType()
								,VApp.getInstance().getaActivity().getHelper().getWaretype());
						break;
					}

				case 1:
					if (VApp.getInstance().getHfActivity()!=null){
						VApp.getInstance().getHfActivity().failClick();
						break;
					}
					if (VApp.getInstance().getHbActivity()!=null){
						VApp.getInstance().getHbActivity().failClick();
						break;
					}
					if (VApp.getInstance().getaddFriendsFunctionA()!=null){
						VApp.getInstance().getaddFriendsFunctionA().failClick();
						break;
					}
					if (VApp.getInstance().getaActivity()!=null){

						if (MyDialog.getApkName()==null){
//							if (VApp.getInstance().getaActivity().wsPayDialog!=null)
//								VApp.getInstance().getaActivity().wsPayDialog.queryWxSdkOrder();
						}else{
//							if (VApp.getInstance().getaActivity().mDialog!=null)
//								VApp.getInstance().getaActivity().mDialog.queryWxSdkOrder();
						}
						break;
					}
//					if (VApp.getInstance().getaActivity()!=null){
//						VApp.getInstance().getaActivity().failClick();
//						break;
//					}
					break;
				case 3:
//					if (VApp.getInstance().getHbActivity()!=null){
//						VApp.getInstance().getHbActivity().successfulclick(
//								SmallMerchantPayDialog.getApkName()!=null?SmallMerchantPayDialog.apkName:""
//								,VApp.getInstance().getHbActivity().getHelper().getType()
//								,VApp.getInstance().getHbActivity().getHelper().getWaretype());
//						break;
//					}
					if (VApp.getInstance().getHbActivity()!=null){
						VApp.getInstance().getHbActivity().smDialog.queryWxSdkOrder();
						break;
					}
					if (VApp.getInstance().getaActivity()!=null){

						if (MyDialog.getApkName()==null){
//							if (VApp.getInstance().getaActivity().wsPayDialog!=null)
//								VApp.getInstance().getaActivity().wsPayDialog.queryWxSdkOrder();
						}else{
//							if (VApp.getInstance().getaActivity().mDialog!=null)
//								VApp.getInstance().getaActivity().mDialog.queryWxSdkOrder();
						}
						break;
					}
					break;
			}
		}
	};



	@Override
	public void onResp(BaseResp resp) {
		Log.i(TAG, "onResp: "+resp.errCode);
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
//				Toast.makeText(WXPayEntryActivity.this,"ERR_OK",Toast.LENGTH_SHORT).show();
				mHandler.sendEmptyMessageDelayed(3,200);
				break;
			default:
//				Toast.makeText(WXPayEntryActivity.this,"default",Toast.LENGTH_SHORT).show();
				mHandler.sendEmptyMessageDelayed(1,200);
//				mHandler.post(new Runnable() {
//					@Override
//					public void run() {
//						VApp.getInstance().getaActivity().failClick();
//					}
//				});
				//发送返回
				break;
		}
		finish();

	}
}