package com.zhushou.weichat.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.api.OpenRedRequest;
import com.zhushou.weichat.bean.LoginMsgInfo;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.bean.UpdateInfo;
import com.zhushou.weichat.bean.WxUserInfo;
import com.zhushou.weichat.ui.dialog.UpdateDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/10/30.
 */

public class LoginControllerImpl implements LoginActivityInterface.ViewControllerImpl {

    Activity mActivity;
    LoginActivityInterface.ViewImpl viewImpl;
    private CountUtil countUtil;
    private UpdateDialog updateDialog;
    private ProgressDialog progressDialog;
    WxUserInfo wxUserInfo;
    private UpdateInfo updateVersionInfo;

    public LoginControllerImpl(Activity mActivity, LoginActivityInterface.ViewImpl viewImpl){
        this.mActivity = mActivity;
        this.viewImpl = viewImpl;
        this.countUtil = new CountUtil(mActivity);
    }



    @Override
    public void getWxLoginInfo(String code) {
        OpenRedRequest.getWxAccess_token(VApp.WXAPP_ID, VApp.WXAPP_SECRET, code, 0, mHandler);
    }

    @Override
    public void start() {
        try {
            LoginMsgInfo loginMsgInfo = LoginSP.getLoginMsg(mActivity);
            if (loginMsgInfo!=null){
                viewImpl.onResultUserInfo(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void rafreshLoginmsg(String openId) {
        OpenRedRequest.loginGet(openId, 3, mHandler);
    }

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0:
                    try {
                        JSONObject accessTokenJo = new JSONObject(((OkHttpResult) msg.obj).msg);
                        String access_token = accessTokenJo.getString("access_token");
                        String user_openid = accessTokenJo.getString("openid");
                        OpenRedRequest.getWxUserInfo(user_openid, access_token, 1, mHandler);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        viewImpl.onResultUserInfo(false);
                    }
                    break;
                case 1:
                    wxUserInfo = new WxUserInfo();
                    try {
                        JSONObject userInfoJo = new JSONObject(((OkHttpResult) msg.obj).msg);
                        wxUserInfo.setOpenId(userInfoJo.getString("openid"));
                        wxUserInfo.setNickname(userInfoJo.getString("nickname"));
                        wxUserInfo.setSex(userInfoJo.getInt("sex"));
                        wxUserInfo.setProvince(userInfoJo.getString("province"));
                        wxUserInfo.setCity(userInfoJo.getString("city"));
                        wxUserInfo.setCountry(userInfoJo.getString("country"));
                        wxUserInfo.setHeadimageurl(userInfoJo.getString("headimgurl"));
                        wxUserInfo.setUnionid(userInfoJo.getString("unionid"));
                        OpenRedRequest.registerGet("weixin",
                                wxUserInfo.getOpenId(),
                                wxUserInfo.getNickname(),
                                wxUserInfo.getHeadimageurl(),
                                "device",
                                "1",
                                2,mHandler);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        viewImpl.onResultUserInfo(false);
                    }
                    break;
                case 2:
                    try {
//                        JSONObject userInfoJo = new JSONObject(((OkHttpResult) msg.obj).msg);
                        JSONObject jo = new JSONObject(((OkHttpResult) msg.obj).msg);
                        int resultcode = jo.getInt("code");
                        if (resultcode == 1) {
                            JSONObject resultjo = jo.getJSONObject("content");
                            //用户登录成功 统计
//                            MobclickAgent.onProfileSignIn("WX", openId);
//                            HashMap<String, String> hashMap = new HashMap<>();
//                            hashMap.put("用户名", nickname);
//                            hashMap.put("登录平台", "微信");
//                            hashMap.put("openID", openId);
//                            TJConstant.sendMobclickAgent(hashMap, "login_success", mActivity);
                            LoginSP.saveLoginMsg(mActivity, resultjo.toString());
//                            LBStat.click(TJConstant.login_successful);
                            viewImpl.onResultUserInfo(true);
                        } else if (resultcode == -1) {
                            if (wxUserInfo!=null){
                                OpenRedRequest.loginGet(wxUserInfo.getOpenId(), 3, mHandler);
                            }else{
                                ToastUtil.centerToast(mActivity, "登录失败");
                            }
                        } else {
                            ToastUtil.centerToast(mActivity, "登录失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        viewImpl.onResultUserInfo(false);
                    }
                    break;
                case 3:
                    JSONObject jo = null;
                    try {
                        jo = new JSONObject(((OkHttpResult) msg.obj).msg);
                        int resultcode = jo.getInt("code");
                        if (resultcode==1){
                            JSONObject resultjo = jo.getJSONObject("content");
                            LoginSP.saveLoginMsg(mActivity, resultjo.toString());
                            viewImpl.onResultUserInfo(true);
                        }else{
                            ToastUtil.centerToast(mActivity, "登录失败");
                            viewImpl.onResultUserInfo(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        viewImpl.onResultUserInfo(false);
                    }

                    break;

                case 4:
                    OkHttpResult result = (OkHttpResult) msg.obj;
                    JSONObject updateJo = null;
                    if (progressDialog!=null&&progressDialog.isShowing())
                        progressDialog.dismiss();
                    try {
                        if (!result.isSuccess){
                            return;
                        }
                        updateJo = new JSONObject(result.msg);
                        updateVersionInfo = UpdateUtil.DecodeUpdateJson(updateJo,countUtil.getFrom(),countUtil.getApp(),countUtil.getVersion());
                        if (updateVersionInfo != null) {
                            if (updateVersionInfo.isUpdate()) {
                                String address = updateVersionInfo.getUpdateAddress();
                                String version = updateVersionInfo.getVersionCode();
                                if (address != null && !address.isEmpty()) {
                                    updateDialog = new UpdateDialog(mActivity,version, address, updateVersionInfo.isForceUpdate());
                                    updateDialog.show();
                                }

                            }else {
                                Toast.makeText(mActivity, "已经是最新版本！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


    public void checkVersionUpdate(){
        if (progressDialog==null){
            progressDialog = new ProgressDialog(mActivity);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(true);
            progressDialog.getWindow().setWindowAnimations(R.style.dialog_JB_SF);
        }
        progressDialog.setMessage("请稍后...");
        progressDialog.show();
        OpenRedRequest.requestUpdateStatus(4,mHandler);
    }


}
