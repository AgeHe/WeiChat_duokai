package com.zhushou.weichat.uinew.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.base.BaseFragment;
import com.zhushou.weichat.bean.LoginMsgInfo;
import com.zhushou.weichat.ui.ComplaintsActivity;
import com.zhushou.weichat.ui.dialog.LoginDialog;
import com.zhushou.weichat.uinew.ExceptionOrdersActivity;
import com.zhushou.weichat.utils.AuthLoginListener;
import com.zhushou.weichat.utils.GlideCircleTransform;
import com.zhushou.weichat.utils.LoginActivityInterface;
import com.zhushou.weichat.utils.LoginControllerImpl;
import com.zhushou.weichat.utils.LoginSP;
import com.zhushou.weichat.utils.ToastUtil;

import org.json.JSONException;

/**
 * Created by Administrator on 2017/11/27.
 */

public class MyFragment extends BaseFragment implements View.OnClickListener,AuthLoginListener,LoginActivityInterface.ViewImpl,LoginDialog.LoginDialogCallBack{

    private Resources mResources;
    private TextView tv_viersion;
    private TextView textView3;
//    private  String[] kfqqArray,kfdhArray,kfwxArray;
    private ImageView headportrait;
    private TextView login;
    private boolean isLogin = false;
    private LoginControllerImpl viewController;
    private ProgressDialog loadingProgressDialog;
    private static IWXAPI WXapi;
    private LoginDialog loginDialog;
    private LoginMsgInfo loginMsgInfo;

    @Override
    protected View init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_my,container,false);
        return inflater.inflate(R.layout.activity_customer_service,container,false);
    }

    LinearLayout ll_service_complaints;
    @Override
    public void initView(View view) {
        mResources = this.getResources();
        findView(view);
    }

    @Override
    public void initData() {

        viewController = new LoginControllerImpl(mActivity,this);
        VApp.getInstance().setAuthLoginListener(this);
        loginDialog = new LoginDialog(mActivity);
        loginDialog.setDialogCallBack(this);
        try {
            loginMsgInfo = LoginSP.getLoginMsg(mActivity);
            if (loginMsgInfo.getOpenid()!=null&&!loginMsgInfo.getOpenid().isEmpty())
                viewController.rafreshLoginmsg(loginMsgInfo.getOpenid());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ll_service_qq://kefuqq
//                if (kfqqArray == null)
//                    return;
//                if (kfqqArray.length > 1) {
//                    Random random = new Random();
//                    int randomInt = random.nextInt(kfqqArray.length-1);
//                    Log.i("hhh", "findView: randomInt"+randomInt);
//                    try {
//                        String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + kfqqArray[randomInt] + "&version=1";
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
//                    }catch (Exception e){
//                        MyToast.mCenterToast("打开QQ异常或未安装QQ",mActivity);
//                    }
//                } else {
//                    try {
//                        String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + kfqqArray[0] + "&version=1";
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
//                    }catch (Exception e){
//                        MyToast.mCenterToast("打开QQ异常或未安装QQ",mActivity);
//                    }
//                }

                startActivity(new Intent(mActivity,ComplaintsActivity.class));
                break;
//            case R.id.ll_service_cjwt://常见问题
//                startActivity(new Intent(mActivity,CommonProblemActivity.class));
//                break;
            case R.id.ll_service_complaints://消费记录
                startActivity(new Intent(mActivity,ExceptionOrdersActivity.class));
                break;
//            case R.id.ll_service_setting:
//                viewController.checkVersionUpdate();
//                break;
            case R.id.login:
                loginDialog.show();
                break;
            case R.id.headportrait:
                break;
        }
    }

    private void findView(View view) {

//        UpdatePriceInfo info = APPUsersShareUtil.getUpdateMessage(mActivity);
//        kfqqArray = info.getKfqq() == null ? null : info.getKfqq().split(",");
//        kfdhArray = info.getKfdh() == null ? null : info.getKfdh().split(",");
//        kfwxArray = info.getKfwx() == null ? null : info.getKfwx().split(",");

        PackageInfo packageInfo = null;
        try {
            packageInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_viersion = (TextView) view.findViewById(R.id.tv_viersion);
        if (packageInfo != null) {
            tv_viersion.setText(mResources.getString(R.string.banben_versionname) + packageInfo.versionName);
        }
        //客服QQ
        view.findViewById(R.id.ll_service_qq).setOnClickListener(this);
//        view.findViewById(R.id.ll_service_cjwt).setOnClickListener(this);
//        view.findViewById(R.id.ll_service_setting).setOnClickListener(this);
        ll_service_complaints = (LinearLayout) view.findViewById(R.id.ll_service_complaints);
        login = (TextView) view.findViewById(R.id.login);
        login.setOnClickListener(this);
        headportrait = (ImageView) view.findViewById(R.id.headportrait);
        headportrait.setOnClickListener(this);
        ll_service_complaints.setOnClickListener(this);
        //客服微信
        view.findViewById(R.id.textView3).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
        setLoginInfo();//填上以获取的登录信息
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void loginPlatform() {
        wxLogin();
    }

    @Override
    public void onResultUserInfo(boolean successful) {
        if (loadingProgressDialog!=null&&loadingProgressDialog.isShowing())
            loadingProgressDialog.dismiss();
        if (successful){
            ToastUtil.centerToast(mActivity,"登录成功");
            try {
                LoginMsgInfo loginMsgInfo = LoginSP.getLoginMsg(mActivity);
                if(loginMsgInfo!=null){
                    Glide.with(mActivity).load(loginMsgInfo.getFace()).transform(new GlideCircleTransform(mActivity)).into(headportrait);
                    login.setText(loginMsgInfo.getNickname());
                    login.setOnClickListener(null);
                }
            }catch (Exception e){
                 e.printStackTrace();
            }
        }
    }

    @Override
    public void onResult(String resultUrl) {
        loadingProgressDialog.setMessage("获取用户登录信息...");
        viewController.getWxLoginInfo(resultUrl);
    }

    @Override
    public void onCancel(int authType) {
        if (loadingProgressDialog!=null&&loadingProgressDialog.isShowing())
            loadingProgressDialog.dismiss();
    }

    private void wxLogin(){
        if(loadingProgressDialog==null){
            loadingProgressDialog = new ProgressDialog(mActivity);
            loadingProgressDialog.setMessage("请稍后...");
            loadingProgressDialog.setCanceledOnTouchOutside(false);
            loadingProgressDialog.setCancelable(true);
            loadingProgressDialog.getWindow().setWindowAnimations(R.style.dialog_JB_SF);
            WXapi = WXAPIFactory.createWXAPI(mActivity, VApp.WXAPP_ID, true);
        }

        loadingProgressDialog.show();
        WXapi.registerApp(VApp.WXAPP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "shieldvpn";
        WXapi.sendReq(req);
    }



    private void setLoginInfo(){
        try {
            LoginMsgInfo loginMsgInfo = LoginSP.getLoginMsg(mActivity);
            if(loginMsgInfo!=null){
                Glide.with(mActivity).load(loginMsgInfo.getFace()).transform(new GlideCircleTransform(mActivity)).into(headportrait);
                login.setText(loginMsgInfo.getNickname());
                login.setOnClickListener(null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
