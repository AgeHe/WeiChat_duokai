package com.zhushou.weichat.ui;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhushou.weichat.R;
import com.zhushou.weichat.VApp;
import com.zhushou.weichat.base.BaseFragment;
import com.zhushou.weichat.bean.LoginMsgInfo;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.ui.dialog.LoginDialog;
import com.zhushou.weichat.utils.AuthLoginListener;
import com.zhushou.weichat.utils.GlideCircleTransform;
import com.zhushou.weichat.utils.LoginActivityInterface;
import com.zhushou.weichat.utils.LoginControllerImpl;
import com.zhushou.weichat.utils.LoginSP;
import com.zhushou.weichat.utils.MyToast;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;

import org.json.JSONException;

import java.util.Random;

/**
 * Created by Administrator on 2017/1/6.
 * 客服界面
 */
public class CustomerServiceFragment extends BaseFragment implements View.OnClickListener,AuthLoginListener,LoginActivityInterface.ViewImpl,LoginDialog.LoginDialogCallBack {

    private Resources mResources;
    private TextView tv_viersion;
    private TextView textView3;
    private  String[] kfqqArray,kfdhArray,kfwxArray;
    private ImageView headportrait;
    private TextView login;
    private boolean isLogin = false;
    private LoginControllerImpl viewController;
    private ProgressDialog loadingProgressDialog;
    private static IWXAPI WXapi;
    private LoginDialog loginDialog;

    @Override
    protected View init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_customer_service,container,false);
    }
    @Override
    public void initView(View view) {
        mResources = this.getResources();
        findView(view);

    }

    @Override
    public void initData() {
        viewController = new LoginControllerImpl(mActivity,this);
        VApp.getInstance().setAuthLoginListener(this);
    }
    private void findView(View view) {

        UpdatePriceInfo info = APPUsersShareUtil.getUpdateMessage(mActivity);
         kfqqArray = info.getKfqq() == null ? null : info.getKfqq().split(",");
         kfdhArray = info.getKfdh() == null ? null : info.getKfdh().split(",");
         kfwxArray = info.getKfwx() == null ? null : info.getKfwx().split(",");

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
        view.findViewById(R.id.ll_service_qq).setOnClickListener(this);
//        view.findViewById(R.id.ll_service_cjwt).setOnClickListener(this);
//        view.findViewById(R.id.ll_service_setting).setOnClickListener(this);
        login = (TextView) view.findViewById(R.id.login);
        login.setOnClickListener(this);
        headportrait = (ImageView) view.findViewById(R.id.headportrait);
        headportrait.setOnClickListener(this);
        //客服微信
//        findViewById(R.id.ll_service_wx).setOnClickListener(view -> {
//            if (kfwxArray == null)
//                return;
//            if (kfwxArray.length > 1) {
//                Random random = new Random();
//                int randomInt = random.nextInt(kfwxArray.length-1);
//                Log.i("hhh", "findView: randomInt"+randomInt);
//                setClipboard(kfwxArray[randomInt]);
//            } else {
//                setClipboard(kfwxArray[0]);
//            }
//        });
        //客服电话
//        findViewById(R.id.ll_service_call).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (kfdhArray == null)
//                    return;
//                if (kfdhArray.length > 1) {
//                    Random random = new Random();
//                    int randomInt = random.nextInt(kfdhArray.length);
//                    Intent dialIntent =  new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + kfdhArray[randomInt]));//跳转到拨号界面，同时传递电话号码
//                    startActivity(dialIntent);
//                }else{
//                    Intent dialIntent =  new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + kfdhArray[0]));//跳转到拨号界面，同时传递电话号码
//                    startActivity(dialIntent);
//                }
//            }
//        });

//        findViewById(R.id.tv_consumption_history).setOnClickListener(view -> {
//            startActivity(new Intent(CustomerServiceActivity.this,PayHistoryActivity.class));
//        });

        view.findViewById(R.id.textView3).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
        loginDialog = new LoginDialog(mActivity);
        loginDialog.setDialogCallBack(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ll_service_qq://kefuqq
                if (kfqqArray == null)
                    return;
                if (kfqqArray.length > 1) {
                    Random random = new Random();
                    int randomInt = random.nextInt(kfqqArray.length-1);
                    Log.i("hhh", "findView: randomInt"+randomInt);
                    try {
                        String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + kfqqArray[randomInt] + "&version=1";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                    }catch (Exception e){
                        MyToast.mCenterToast("打开QQ异常或未安装QQ",mActivity);
                    }
                } else {
                    try {
                        String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + kfqqArray[0] + "&version=1";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                    }catch (Exception e){
                        MyToast.mCenterToast("打开QQ异常或未安装QQ",mActivity);
                    }
                }
                break;
//            case R.id.ll_service_call:
//                break;
//            case R.id.ll_service_cjwt://常见问题
//                startActivity(new Intent(mActivity,CommonProblemActivity.class));
//                break;
            case R.id.ll_service_complaints://消费记录
                startActivity(new Intent(mActivity,ComplaintsActivity.class));
                break;
//            case R.id.ll_service_pay:
//                break;
//            case R.id.ll_service_setting:
//                viewController.checkVersionUpdate();
//                break;
//            case R.id.ll_service_vip:
//                break;
            case R.id.login:
                loginDialog.show();
                break;
            case R.id.headportrait:
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        checkIsLogin();
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

    private void checkIsLogin(){
        try {
            if(!isLogin){
                LoginMsgInfo loginMsgInfo = LoginSP.getLoginMsg(mActivity);
                if(loginMsgInfo!=null){
                    Log.e("jyh","进入获取登录信息");
                    Glide.with(mActivity).load(loginMsgInfo.getFace()).transform(new GlideCircleTransform(mActivity)).into(headportrait);
                    login.setText(loginMsgInfo.getNickname());
                    login.setOnClickListener(null);
                    isLogin=true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResultUserInfo(boolean successful) {
      //空实现
    }

    @Override
    public void loginPlatform() {
        wxLogin();
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
}
