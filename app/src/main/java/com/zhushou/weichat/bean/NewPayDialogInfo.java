package com.zhushou.weichat.bean;

/**
 * Created by Administrator on 2017/5/19.
 */

public class NewPayDialogInfo {

    private String Price; //价格
    private String functionName; //功能名称
    private String functionKey; //功能标识
    private boolean isDiscount = false;//是否享用折扣

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionKey() {
        return functionKey==null?"":functionKey;
    }

    public void setFunctionKey(String functionKey) {
        this.functionKey = functionKey;
    }

    public boolean isDiscount() {
        return isDiscount;
    }

    public void setDiscount(boolean discount) {
        isDiscount = discount;
    }
}
