package com.zhushou.weichat.wxapi;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

import com.lbsw.stat.LBStat;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.utils.AuthLoginListener;
import com.zhushou.weichat.utils.ToastUtil;

/** 微信客户端回调activity示例 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private static final String TAG = "WXEntryActivity";
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	private Resources mResources;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, VApp.WXAPP_ID, false);
		api.handleIntent(getIntent(), this);
		mResources = getResources();
	}

	@Override
	public void onReq(BaseReq arg0) {

	}

	@Override
	public void onResp(BaseResp baseResp) {
		String result = "";
		switch (baseResp.getType()) {
			case ConstantsAPI.COMMAND_SENDAUTH:
				//登录回调,处理登录成功的逻辑
				switch (baseResp.errCode) {
					case com.tencent.mm.opensdk.modelbase.BaseResp.ErrCode.ERR_OK:

						String code = ((SendAuth.Resp) baseResp).code;
						/**
						 * 将你前面得到的AppID、AppSecret、code，拼接成URL 获取access_token等等的信息(微信)
						 */
//                        String get_access_token = getCodeRequest(code);
						if (VApp.authLoginListener != null) {
							VApp.authLoginListener.onResult(code);
						}
						finish();
						break;
					case com.tencent.mm.opensdk.modelbase.BaseResp.ErrCode.ERR_USER_CANCEL:
						if (VApp.authLoginListener != null) {
							VApp.authLoginListener.onCancel(AuthLoginListener.AUTH_WX);
						}
						result = "发送取消";
						Toast.makeText(this, result, Toast.LENGTH_LONG).show();
						finish();
						break;
					case com.tencent.mm.opensdk.modelbase.BaseResp.ErrCode.ERR_AUTH_DENIED:
						if (VApp.authLoginListener != null) {
							VApp.authLoginListener.onCancel(AuthLoginListener.AUTH_WX);
						}
						result = "发送被拒绝";
						Toast.makeText(this, result, Toast.LENGTH_LONG).show();
						finish();
						break;
					default:
						result = "发送返回";
						Toast.makeText(this, result, Toast.LENGTH_LONG).show();
						if (VApp.authLoginListener != null) {
							VApp.authLoginListener.onCancel(AuthLoginListener.AUTH_WX);
						}
						finish();
						break;
				}

				break;
			case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
				//分享回调,处理分享成功后的逻辑
				switch (baseResp.errCode) {
					case BaseResp.ErrCode.ERR_OK:
						//分享成功
						LBStat.click(VApp.TJ_FX_CG); // 统计分享成功
						if (VApp.getInstance().getMainActivity()!=null)
							VApp.getInstance().getMainActivity().shareCallBack("ERR_OK");
						if (VApp.wxShareListener!=null)
							VApp.wxShareListener.shareResult("ERR_OK");
						finish();
						break;
					case BaseResp.ErrCode.ERR_USER_CANCEL:
						//分享取消
						if (VApp.getInstance().getMainActivity()!=null)
							VApp.getInstance().getMainActivity().shareCallBack("ERR_USER_CANCEL");
						if (VApp.wxShareListener!=null)
							VApp.wxShareListener.shareResult("ERR_USER_CANCEL");
						finish();
						break;
					case BaseResp.ErrCode.ERR_AUTH_DENIED:
						//分享拒绝
						if (VApp.getInstance().getMainActivity()!=null)
							VApp.getInstance().getMainActivity().shareCallBack("ERR_AUTH_DENIED");
						if (VApp.wxShareListener!=null)
							VApp.wxShareListener.shareResult("ERR_AUTH_DENIED");
						finish();
						break;
					default:
						if (VApp.wxShareListener!=null)
							VApp.wxShareListener.shareResult("ERR_OK");
						finish();
				}
				break;
			default:
				break;

		}
	}
}