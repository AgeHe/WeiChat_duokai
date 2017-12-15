package com.zhushou.weichat.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.NewPayDialogInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class NewPayDialog extends Dialog implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {


    private static final String TAG = "NewPayDialog";
    private Context context;
    private List<NewPayDialogInfo> listData;
    private PayDialogInterface payDialogInterface;
    private int payType = 1;
    private int dataLocation = 0;
    private String zhekou;
    private boolean wxPayIsVisibility = true;
    private boolean aliPayIsVisibility = true;
    private boolean qqPayIsVisibility = true;

    public NewPayDialog(@NonNull Context context, List<NewPayDialogInfo> listData, String zhekou) {
        super(context, R.style.MyDialog);
        if (this.listData == null) {
            this.listData = new ArrayList<>();
        }
        this.listData.clear();
        this.listData.addAll(listData);
        this.zhekou = String.valueOf(Double.valueOf(zhekou)*10);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setpaydialogSeting();
    }

    public void setpayDialogInterface(PayDialogInterface payDialogInterface) {
        this.payDialogInterface = payDialogInterface;
    }

    public void setPayTypeIsVisibility(boolean aliPay, boolean wxPay, boolean qqPay) {
        wxPayIsVisibility = wxPay;
        aliPayIsVisibility = aliPay;
        qqPayIsVisibility = qqPay;
    }

    private RelativeLayout rl_function_type1, rl_function_type2, rl_function_type3, rl_paytype1, rl_paytype2, rl_paytype3;
    private TextView tv_price_1, tv_price_2, tv_price_3, tv_zfb_zk,tv_qq_zk, tv_go_pay;
    private RadioButton rb_function_1, rb_function_2, rb_function_3, rb_zfb, rb_wx, rb_qq;

    private void setpaydialogSeting() {

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pay, null);
        rl_function_type1 = (RelativeLayout) dialogView.findViewById(R.id.rl_function_type1);
        rl_function_type2 = (RelativeLayout) dialogView.findViewById(R.id.rl_function_type2);
        rl_function_type3 = (RelativeLayout) dialogView.findViewById(R.id.rl_function_type3);
        rl_paytype1 = (RelativeLayout) dialogView.findViewById(R.id.rl_paytype1);
        rl_paytype2 = (RelativeLayout) dialogView.findViewById(R.id.rl_paytype2);
        rl_paytype3 = (RelativeLayout) dialogView.findViewById(R.id.rl_paytype3);
        tv_price_1 = (TextView) dialogView.findViewById(R.id.tv_price_1);
        tv_price_2 = (TextView) dialogView.findViewById(R.id.tv_price_2);
        tv_price_3 = (TextView) dialogView.findViewById(R.id.tv_price_3);
        tv_zfb_zk = (TextView) dialogView.findViewById(R.id.tv_zfb_zk);
        tv_qq_zk = (TextView) dialogView.findViewById(R.id.tv_qq_zk);
        rb_function_1 = (RadioButton) dialogView.findViewById(R.id.rb_function_1);
        rb_function_2 = (RadioButton) dialogView.findViewById(R.id.rb_function_2);
        rb_function_3 = (RadioButton) dialogView.findViewById(R.id.rb_function_3);
        rb_zfb = (RadioButton) dialogView.findViewById(R.id.rb_zfb);
        rb_wx = (RadioButton) dialogView.findViewById(R.id.rb_wx);
        rb_qq = (RadioButton) dialogView.findViewById(R.id.rb_qq);
        tv_go_pay = (TextView) dialogView.findViewById(R.id.tv_go_pay);

        rb_function_1.setOnCheckedChangeListener(this);
        rb_function_2.setOnCheckedChangeListener(this);
        rb_function_3.setOnCheckedChangeListener(this);
        rb_function_1.setOnClickListener(this);
        rb_function_2.setOnClickListener(this);
        rb_function_3.setOnClickListener(this);
        rb_zfb.setOnCheckedChangeListener(this);
        rb_wx.setOnCheckedChangeListener(this);
        rb_qq.setOnCheckedChangeListener(this);
//        rb_function_1.setEnabled(false);
//        rb_function_2.setEnabled(false);
//        rb_function_3.setEnabled(false);
//        rb_zfb.setEnabled(false);
//        rb_wx.setEnabled(false);


        rl_function_type1.setOnClickListener(this);
        rl_function_type2.setOnClickListener(this);
        rl_function_type3.setOnClickListener(this);
        rl_paytype1.setOnClickListener(this);
        rl_paytype2.setOnClickListener(this);
        rl_paytype3.setOnClickListener(this);
        tv_go_pay.setOnClickListener(this);

        super.setContentView(dialogView);
        if (listData == null || listData.size() <= 0)
            return;
//        setPrice(1);
        rb_zfb.setChecked(true);
        rb_function_1.setChecked(true);
        if (zhekou != null) {
            tv_zfb_zk.setText(Html.fromHtml(String.format(context.getResources().getString(R.string.pay_select_zfb_str),"支付宝",zhekou)));
            tv_qq_zk.setText(Html.fromHtml(String.format(context.getResources().getString(R.string.pay_select_zfb_str),"QQ支付",zhekou)));
        }
    }

    public void setPrice(int type) {
        if (listData.size() <= 0)
            return;
        NewPayDialogInfo newPayDialogInfo = listData.get(0);
        NewPayDialogInfo newPayDialogInfo1 = null;
        NewPayDialogInfo newPayDialogInfo2 = null;

        if (listData.size() >= 2) {
            newPayDialogInfo1 = listData.get(1);
        }

        if (listData.size() >= 3) {
            newPayDialogInfo2 = listData.get(2);
        }
        switch (listData.size()) {
            case 1:
                if (listData.size() > 0) {
                    rl_function_type1.setVisibility(View.VISIBLE);
                    rl_function_type2.setVisibility(View.GONE);
                    rl_function_type3.setVisibility(View.GONE);
                    tv_price_1.setText(setTextView(type, newPayDialogInfo));
                }
                break;
            case 2:
                if (listData.size() > 1) {
                    rl_function_type1.setVisibility(View.VISIBLE);
                    rl_function_type2.setVisibility(View.VISIBLE);
                    rl_function_type3.setVisibility(View.GONE);
                    tv_price_1.setText(setTextView(type, newPayDialogInfo));
                    tv_price_2.setText(setTextView(type, newPayDialogInfo1));
                }
                break;
            case 3:
                if (listData.size() > 2) {
                    rl_function_type1.setVisibility(View.VISIBLE);
                    rl_function_type2.setVisibility(View.VISIBLE);
                    rl_function_type3.setVisibility(View.VISIBLE);
                    tv_price_1.setText(setTextView(type, newPayDialogInfo));
                    tv_price_2.setText(setTextView(type, newPayDialogInfo1));
                    tv_price_3.setText(setTextView(type, newPayDialogInfo2));
                }

                break;
            default:
                if (listData.size() > 2) {
                    rl_function_type1.setVisibility(View.VISIBLE);
                    rl_function_type2.setVisibility(View.VISIBLE);
                    rl_function_type3.setVisibility(View.VISIBLE);
                    tv_price_1.setText(setTextView(type, newPayDialogInfo));
                    tv_price_2.setText(setTextView(type, newPayDialogInfo1));
                    tv_price_3.setText(setTextView(type, newPayDialogInfo2));
                }
                break;
        }
    }

    public CharSequence setTextView(int paytype, NewPayDialogInfo info) {
        String zhekouPrice = "99";
        try {
            zhekouPrice = String.valueOf(discount(Double.valueOf(info.getPrice()),Double.valueOf(zhekou)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (paytype != 2) {
                return setPayTextView(info.getPrice(),zhekouPrice, info.getFunctionName());
        } else {
            return Html.fromHtml(String.format(context.getResources().getString(R.string.pay_type_wx), zhekouPrice, info.getFunctionName()));
        }
    }

    private String discount(double price, double discount) throws Exception {
        DecimalFormat df = new DecimalFormat("####0.00");
        double resultPrice = price / (discount/10);
        return String.valueOf(df.format(resultPrice));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_function_type1:
                rb_function_1.setChecked(true);
                rb_function_2.setChecked(false);
                rb_function_3.setChecked(false);
                break;
            case R.id.rl_function_type2:
                rb_function_1.setChecked(false);
                rb_function_2.setChecked(true);
                rb_function_3.setChecked(false);
                break;
            case R.id.rl_function_type3:
                rb_function_1.setChecked(false);
                rb_function_2.setChecked(false);
                rb_function_3.setChecked(true);
                break;
            case R.id.rl_paytype1:
                rb_zfb.setChecked(true);
                break;
            case R.id.rl_paytype2:
                rb_wx.setChecked(true);
                break;
            case R.id.rl_paytype3:
                rb_qq.setChecked(true);
                break;
            case R.id.tv_go_pay:
                if (payDialogInterface != null) {
                    String selectPrice = "99";
                    try {
                        selectPrice = String.valueOf(payType != 2 ? listData.get(dataLocation).getPrice():discount(Double.valueOf(listData.get(dataLocation).getPrice()), Double.valueOf(zhekou)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        selectPrice = listData.get(dataLocation).getPrice();
                    }
                    payDialogInterface.paySelectCallback(listData.get(dataLocation),
                            listData.get(dataLocation).getFunctionKey(),
                            payType,
                            selectPrice);
                }


                break;
        }
    }

    @Override
    public void show() {
        super.show();
        rl_paytype1.setVisibility(aliPayIsVisibility ? View.VISIBLE : View.GONE); //支付宝
        rl_paytype2.setVisibility(wxPayIsVisibility ? View.VISIBLE : View.GONE); //微信
        rl_paytype3.setVisibility(qqPayIsVisibility?View.VISIBLE:View.GONE);//QQ

        rb_qq.setChecked(true);
        if (aliPayIsVisibility||qqPayIsVisibility) {
            if (aliPayIsVisibility&&!qqPayIsVisibility){
                rb_zfb.setChecked(true);
            }else{
                rb_qq.setChecked(true);
            }
        }

        if (!aliPayIsVisibility&&!qqPayIsVisibility){
            rb_wx.setChecked(true);
        }

        if (!aliPayIsVisibility&&!wxPayIsVisibility&&!qqPayIsVisibility){
            dismiss();
        }


        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        /////////获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        ;
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        /////////设置高宽
        lp.width = (screenWidth / 10) * 9; // 宽度
        dialogWindow.setAttributes(lp);
    }

    public CharSequence setPayTextView(String a1, String a2, String function) {
        String jiage = String.format(context.getResources().getString(R.string.pay_price), a1, a2);
        String functionName = String.format(context.getResources().getString(R.string.pay_function), function);
        ForegroundColorSpan userSpan = new ForegroundColorSpan(Color.parseColor("#000000"));
        ForegroundColorSpan giftSpan = new ForegroundColorSpan(Color.parseColor("#ff0024"));
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(jiage);
        builder.setSpan(giftSpan, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StrikethroughSpan(), jiage.length() - (a2.length() + 2), builder.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(functionName);
        builder.setSpan(userSpan, builder.length() - functionName.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.rb_function_1:
                if (b) {
                    dataLocation = 0;
                    rb_function_2.setChecked(false);
                    rb_function_3.setChecked(false);
                }
                break;
            case R.id.rb_function_2:
                if (b) {
                    dataLocation = 1;
                    rb_function_1.setChecked(false);
                    rb_function_3.setChecked(false);
                }
                break;
            case R.id.rb_function_3:
                if (b) {
                    dataLocation = 2;
                    rb_function_1.setChecked(false);
                    rb_function_2.setChecked(false);
                }
                break;
            case R.id.rb_zfb:
                Log.i(TAG, "newpaydialog_zfb_onCheckedChanged: " + b);
                if (b) {
                    setPrice(1);
                    payType = 1;
                    rb_wx.setChecked(false);
                    rb_qq.setChecked(false);
                }
                break;
            case R.id.rb_wx:
                Log.i(TAG, "newpaydialog_wx_onCheckedChanged: " + b);
                if (b) {
                    setPrice(2);
                    payType = 2;
                    rb_zfb.setChecked(false);
                    rb_qq.setChecked(false);
                }
                break;
            case R.id.rb_qq:
                if (b) {
                    setPrice(3);
                    payType = 3;
                    rb_wx.setChecked(false);
                    rb_zfb.setChecked(false);
                }
                break;

        }
    }

    public void payDismiss() {
        dismiss();
    }

    public interface PayDialogInterface {
        void paySelectCallback(NewPayDialogInfo dateInfo, String functionKey, int payType, String selectPrice);
    }

}
