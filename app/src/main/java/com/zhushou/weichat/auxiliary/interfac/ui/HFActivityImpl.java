package com.zhushou.weichat.auxiliary.interfac.ui;

import android.widget.Switch;

/**
 * Created by Administrator on 2017/3/16.
 */

public class HFActivityImpl {

    public interface MyHFImpl{
        void functionClick(String functionKey,Switch switchView);
        void paySuccess(String payFunctionKey);
        void pauFailure(String payFunctionKey);
    }
    public interface  MyHFActivityImpl{
        void functionPay(String payName,String payPrice,String payFunctionKey);
        void functionClickBack(String functionKey,boolean payStatus);
    }

}
