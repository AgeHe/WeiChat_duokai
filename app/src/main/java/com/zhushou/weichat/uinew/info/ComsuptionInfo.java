package com.zhushou.weichat.uinew.info;

/**
 * Created by Administrator on 2017/8/17.
 * 付费数据bean
 */
public class ComsuptionInfo {

    private String payType; //支付类型
    private String orderNumber; //订单号
    private String insertDate; //数据时间
    private String price; //商品价格
    private String commodityName; //商品名称
    private String commodityFunctionType;//商品功能类型
    private int payComplateStatus; //付费状态
    private String payChannelType;//支付渠道类型
    private String other; //其他
    private boolean isTrap = false;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public int getPayComplateStatus() {
        return payComplateStatus;
    }



    public void setPayComplateStatus(int payComplateStatus) {
        this.payComplateStatus = payComplateStatus;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getPayChannelType() {
        return payChannelType;
    }

    public void setPayChannelType(String payChannelType) {
        this.payChannelType = payChannelType;
    }

    public String getCommodityFunctionType() {
        return commodityFunctionType;
    }

    public void setCommodityFunctionType(String commodityFunctionType) {
        this.commodityFunctionType = commodityFunctionType;
    }

    public boolean isTrap() {
        return isTrap;
    }

    public void setTrap(boolean trap) {
        isTrap = trap;
    }

    public ComsuptionInfo(){
        super();
    }

    public ComsuptionInfo(String payType, String orderNumber, String insertDate, String price, String commodityName, String commodityFunctionType, int payComplateStatus, String payChannelType, String other) {
        this.payType = payType;
        this.orderNumber = orderNumber;
        this.insertDate = insertDate;
        this.price = price;
        this.commodityName = commodityName;
        this.commodityFunctionType = commodityFunctionType;
        this.payComplateStatus = payComplateStatus;
        this.payChannelType = payChannelType;
        this.other = other;
    }
}
