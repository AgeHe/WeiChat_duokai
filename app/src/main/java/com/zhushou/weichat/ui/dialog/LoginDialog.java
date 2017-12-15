package com.zhushou.weichat.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhushou.weichat.R;


/**
 * Created by Administrator on 2017/10/26.
 */

public class LoginDialog extends Dialog {

    public LoginDialog(Context context) {
        super(context, R.style.MyDialog);
        initView();
    }

    public void initView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_login,null);
        TextView tv_wx_loginbtn = (TextView) view.findViewById(R.id.tv_wx_loginbtn);
        ImageView tv_login_close = (ImageView) view.findViewById(R.id.tv_login_close);

        tv_login_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_wx_loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logindialogcallback!=null)
                    logindialogcallback.loginPlatform();
                dismiss();
            }
        });
        super.setContentView(view);
    }

    @Override
    public void show() {
        super.show();
    }

    public LoginDialogCallBack logindialogcallback;
    public LoginDialog setDialogCallBack(LoginDialogCallBack logindialogcallback){
        this.logindialogcallback = logindialogcallback;
        return this;
    }

    public interface LoginDialogCallBack{
        void loginPlatform();
    }
}
