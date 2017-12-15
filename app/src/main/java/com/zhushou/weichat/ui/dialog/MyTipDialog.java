package com.zhushou.weichat.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lbsw.stat.LBStat;
import com.umeng.analytics.MobclickAgent;
import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.NewPayDialogInfo;
import com.zhushou.weichat.bean.StatisticsConstans;
import com.zhushou.weichat.bean.UMengStatisticalKey;
import com.zhushou.weichat.bean.UpdatePriceInfo;
import com.zhushou.weichat.constant.PayConstant;
import com.zhushou.weichat.ui.pay.PayHelper;
import com.zhushou.weichat.ui.pay.PayLodingDialog;
import com.zhushou.weichat.utils.CountUtil;
import com.zhushou.weichat.utils.HistoryPayShareUtil;
import com.zhushou.weichat.utils.sp.APPUsersShareUtil;
import com.zhushou.weichat.utils.sqlite.SqliteDo;

import java.text.DecimalFormat;
import java.util.Random;


/**
 * Created by Administrator on 2017/5/3.
 * 打赏窗口
 */

public class MyTipDialog extends Dialog implements View.OnClickListener {
    private CountUtil countUtil;
    private EditText money;
    //    private Clickinterface clickinterface;
    private Random random;
    private String[] dsNums;
    private Activity activity;
    private DecimalFormat fnum;
    private PayHelper helper;//配套使用
    private PayHelper.RequreClick r;
    private static int TYPE_WX = 1;
    private static int TYPE_ZFB = 2;
    private static int TYPE_QQ= 3;
    private PayLodingDialog lodingDialog;
    private SqliteDo sqliteDo;
    private float num = 0.00f;

    private UpdatePriceInfo updatePriceInfo;

    public MyTipDialog(Activity activity, PayHelper.RequreClick r, PayHelper helper) {
        super(activity, R.style.MyDialog);
        this.activity = activity;
        this.r = r;
        this.helper = helper;
        this.helper.setClick(r);
        dsNums = new String[]{"5.18", "5.20", "5.21", "5.28", "6.18", "6.66", "8.88", "12.88", "16.88", "18.88", "51.80", "52.00", "52.80", "61.8", "66.6", "88.88"};
        random = new Random();
        fnum = new DecimalFormat("##0.00");
        sqliteDo = new SqliteDo(activity);
        lodingDialog = new PayLodingDialog(activity);
        countUtil = new CountUtil(activity);
        updatePriceInfo  = APPUsersShareUtil.getUpdateMessage(activity);
    }

    LinearLayout layout_3,layout_4,layout_5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reward);

        money = (EditText) findViewById(R.id.money);
//        money.setOnClickListener(this);
        findViewById(R.id.dialog_close).setOnClickListener(this);
        findViewById(R.id.edit).setOnClickListener(this);
        findViewById(R.id.layout_2).setOnClickListener(this);
        layout_3 = (LinearLayout) findViewById(R.id.layout_3); //支付宝支付按钮
        layout_4 = (LinearLayout) findViewById(R.id.layout_4); //微信支付按钮
        layout_5 = (LinearLayout) findViewById(R.id.layout_5); //qq支付按钮
        layout_3.setOnClickListener(this);
        layout_4.setOnClickListener(this);
        layout_5.setOnClickListener(this);
        layout_3.setVisibility(updatePriceInfo.isPay_status_ali()?View.VISIBLE:View.GONE);
        layout_4.setVisibility(updatePriceInfo.isPay_status_wx()?View.VISIBLE:View.GONE);
        layout_5.setVisibility(updatePriceInfo.isPay_status_qq()?View.VISIBLE:View.GONE);
        Window win = this.getWindow();
        win.setGravity(Gravity.CENTER);                       //从中间弹出
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //宽度填满
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;  //高度自适应
        win.setAttributes(lp);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //手机应用多开软件,本应用免费。
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.money:

                break;
            case R.id.edit:
                LBStat.click(StatisticsConstans.TJ_C_DSBJ);
                MobclickAgent.onEvent(activity, UMengStatisticalKey.reward_exdit);
                money.setText("");
                money.setCursorVisible(true);
                money.setFocusable(true);
                money.setFocusableInTouchMode(true);
                money.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) money.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(money, 0);
                break;
            case R.id.layout_2:
                LBStat.click(StatisticsConstans.TJ_C_DSSJ);
                money.setText(dsNums[random.nextInt(dsNums.length)]);
                MobclickAgent.onEvent(activity, UMengStatisticalKey.reward_click_random);
                break;
            case R.id.layout_3:
                LBStat.click(StatisticsConstans.TJ_C_DSZFDJ);
                MobclickAgent.onEvent(activity, UMengStatisticalKey.reward_pay_click);
                try {
                    if (check(TYPE_ZFB)) {
                        num = formatMoney();
                        if (num >= 1) {
                            NewPayDialogInfo dateInfo = new NewPayDialogInfo();
                            dateInfo.setFunctionName("打赏");
                            dateInfo.setPrice(String.valueOf(num));
                            dateInfo.setFunctionKey(PayConstant.Pay_S_DA_SHANG);
                            helper.doAliPay(dateInfo, "打赏",num,this);
                        } else {
                            Toast.makeText(activity, activity.getResources().getString(R.string.tip_morethanone), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    lodingDialog.dismiss();
                }
                break;
            case R.id.layout_4:
                LBStat.click(StatisticsConstans.TJ_C_DSZFDJ);
                MobclickAgent.onEvent(activity, UMengStatisticalKey.reward_pay_click);
                try {
                    if (check(TYPE_WX)) {
                        num = formatMoney();
                        if (num >= 1) {
                            NewPayDialogInfo dateInfo = new NewPayDialogInfo();
                            dateInfo.setFunctionName("打赏");
                            dateInfo.setPrice(String.valueOf(num));
                            dateInfo.setFunctionKey(PayConstant.Pay_S_DA_SHANG);
                            helper.doWxPay(dateInfo, "打赏",num,this);
                        } else {
                            Toast.makeText(activity, activity.getResources().getString(R.string.tip_morethanone), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    lodingDialog.dismiss();
                }

                break;
            case R.id.layout_5:
                LBStat.click(StatisticsConstans.TJ_C_DSZFDJ);
                MobclickAgent.onEvent(activity, UMengStatisticalKey.reward_pay_click);
                try {
                    if (check(TYPE_QQ)) {
                        num = formatMoney();
                        if (num >= 1) {
                            NewPayDialogInfo dateInfo = new NewPayDialogInfo();
                            dateInfo.setFunctionName("打赏");
                            dateInfo.setPrice(String.valueOf(num));
                            dateInfo.setFunctionKey(PayConstant.Pay_S_DA_SHANG);
                            helper.doQQPay(dateInfo, "打赏",num,this);
                        } else {
                            Toast.makeText(activity, activity.getResources().getString(R.string.tip_morethanone), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    lodingDialog.dismiss();
                }

                break;

            case R.id.dialog_close:
                dismiss();
                break;
        }
    }

    @Override
    public void show() {



        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        /////////获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        /////////设置高宽
        lp.width = screenWidth; // 宽度
        lp.height = dm.heightPixels;
        dialogWindow.setAttributes(lp);
        super.show();
        money.setCursorVisible(false);
        money.setFocusable(false);
        money.setFocusableInTouchMode(false);
        money.clearFocus();
        money.setText(dsNums[random.nextInt(dsNums.length)]);
    }


    private float formatMoney() {
        Float num = 0.00f;
        try {
            String moneyStr = money.getText().toString();
            num = Float.parseFloat(moneyStr);
            String dd = fnum.format(num);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    private boolean check(int type) {
        if (type == TYPE_WX) {
            if (!HistoryPayShareUtil.isWeixinAvilible(activity)) {
                Toast.makeText(activity, activity.getString(R.string.anzhuang_weixin_app), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        } else if (type == TYPE_ZFB) {
            if (!HistoryPayShareUtil.checkAliPayInstalled(activity)) {
                Toast.makeText(activity, activity.getString(R.string.anzhuang_zfb_app), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        }else if (type == TYPE_QQ) {
            if (!HistoryPayShareUtil.checkQQInstalled(activity)) {
                Toast.makeText(activity, activity.getString(R.string.anzhuang_qq_app), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
