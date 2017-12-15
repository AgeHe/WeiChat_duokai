package com.zhushou.weichat.addfriends.ui.dialog;

import android.content.Context;

import com.zhushou.weichat.addfriends.widget.SelectNumberDialog;

/**
 * Created by Administrator on 2017/3/29.
 */

public class QuantityChoiceDialog implements SelectNumberDialog.SelectNumberLisener {

    private Context mContext;
    private SelectNumberDialog selectNumberDialog;
    public int QuantityChoice = 10;

    SelectNumberDialog.SelectNumberLisener selectNumberLisener;

    public QuantityChoiceDialog(Context context,SelectNumberDialog.SelectNumberLisener selectNumberLisener){
        this.mContext = context;
        selectNumberDialog = new SelectNumberDialog(context);
        this.selectNumberLisener = selectNumberLisener;
    }

    public void showQuantityChoice(int quantityChoice){
        selectNumberDialog.setSelectedIndex(quantityChoice);
        selectNumberDialog.setonClickNumberLisener(this);
        selectNumberDialog.show();
    }

    public void dismiss(){
        if (selectNumberDialog==null)
            return;
        selectNumberDialog.dismiss();
    }

    @Override
    public void number(int value){
        QuantityChoice = value;
    if (selectNumberLisener!=null)
        selectNumberLisener.number(value);
    }
}
