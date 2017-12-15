package com.zhushou.weichat.bean;

/**
 * Created by Administrator on 2017/6/4.
 */

public class OrderDBInfo {

    private String ordernum; //订单号
    private String paytype; // 支付类型
    private int wareType;// 商品类型
    private String functionkey; //用户购买商品功能key
    private float orderprice; //商品价格
    private String needcheck; //是否需要轮询查询该订单
    private int status; //订单当前状态
    private String date;//订单创建时间
    private String otherdata;//其他

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public int getWareType() {
        return wareType;
    }

    public void setWareType(int wareType) {
        this.wareType = wareType;
    }

    public String getFunctionkey() {
        return functionkey;
    }

    public void setFunctionkey(String functionkey) {
        this.functionkey = functionkey;
    }

    public float getOrderprice() {
        return orderprice;
    }

    public void setOrderprice(float orderprice) {
        this.orderprice = orderprice;
    }

    public String getNeedcheck() {
        return needcheck;
    }

    public void setNeedcheck(String needcheck) {
        this.needcheck = needcheck;
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



    public OrderDBInfo(){

    }

    public String getOtherdata() {
        return otherdata;
    }

    public void setOtherdata(String otherdata) {
        this.otherdata = otherdata;
    }

    public OrderDBInfo(String ordernum, String paytype, int wareType, String functionkey, float orderprice, String needcheck, int status, String date, String otherdata) {
        this.ordernum = ordernum;
        this.paytype = paytype;
        this.wareType = wareType;
        this.functionkey = functionkey;
        this.orderprice = orderprice;
        this.needcheck = needcheck;
        this.status = status;
        this.date = date;
        this.otherdata = otherdata;
    }
}
