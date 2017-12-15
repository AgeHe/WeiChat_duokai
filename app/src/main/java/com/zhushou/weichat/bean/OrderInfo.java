package com.zhushou.weichat.bean;

/**
 * Created by Administrator on 2017/5/19.
 */

public class OrderInfo {
    private String orderNum;//订单号
    private String payType;//支付方式
    private int wareType;//商品类别
    private String functionKey;//支付功能key
    private float orderPrice;//支付金额
    private String needCheck;//是否需要后台查询
    private int status;//订单状态
    private String date;//日期

    public OrderInfo() {
    }

    public OrderInfo(String orderNum, String payType, int wareType,String functionKey, float orderPrice, String needCheck, int status, String date) {
        this.orderNum = orderNum;
        this.payType = payType;
        this.wareType = wareType;
        this.functionKey = functionKey;
        this.orderPrice = orderPrice;
        this.needCheck = needCheck;
        this.status = status;
        this.date = date;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public int getWareType() {
        return wareType;
    }

    public void setWareType(int wareType) {
        this.wareType = wareType;
    }

    public float getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(float orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getNeedCheck() {
        return needCheck;
    }

    public void setNeedCheck(String needCheck) {
        this.needCheck = needCheck;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFunctionKey() {
        return functionKey;
    }

    public void setFunctionKey(String functionKey) {
        this.functionKey = functionKey;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "orderNum='" + orderNum + '\'' +
                ", payType='" + payType + '\'' +
                ", wareType=" + wareType +
                ", functionKey='" + functionKey + '\'' +
                ", orderPrice=" + orderPrice +
                ", needCheck='" + needCheck + '\'' +
                ", status=" + status +
                ", date='" + date + '\'' +
                '}';
    }
}
