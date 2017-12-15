package com.zhushou.weichat.ui.pay;

/**
 * Created by cxh on 17/2/6.
 */

public class Order
{
    private String device_info;
    private String body;
    private String total_fee;
    private String para_id;
    private String app_id;
    private String para_tradeNo;
    private String nofity_url;
    private String imsi;
    private String devicecode;
    private String platform;
    private String attach;
    private String sign;
    private String child_para_id;
    private String pay;
    private int pay_type;
    private int type;
    private static Order mOrder = null;

    public void setChild_para_id(String child_para_id)
    {
        this.child_para_id = child_para_id; }

    public String getChild_para_id() {
        return this.child_para_id; }

    public void setType(int type) {
        this.type = type; }

    public int getType() {
        return this.type; }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type; }

    public int getPay_type() {
        return this.pay_type; }

    public void setPay(String pay) {
        this.pay = pay; }

    public String getPay() {
        return this.pay; }

    public void setSign(String sign) {
        this.sign = sign; }

    public String getSign() {
        return this.sign; }

    public void setAttach(String attach) {
        this.attach = attach; }

    public String getAttach() {
        return this.attach; }

    public void setDevicecode(String devicecode) {
        this.devicecode = devicecode; }

    public String getDevicecode() {
        return this.devicecode; }

    public void setImsi(String imsi) {
        this.imsi = imsi; }

    public String getImsi() {
        return this.imsi; }

    public void setPlatform(String platform) {
        this.platform = platform; }

    public String getPlatform() {
        return this.platform; }

    public static Order getmOrder() {
        return mOrder;
    }

    public static Order getInstance() {
        if (mOrder == null) {
            mOrder = new Order();
        }

        return mOrder; }

    public String getDevice_info() {
        return this.device_info; }

    public void setDevice_info(String device_info) {
        this.device_info = device_info; }

    public String getBody() {
        return this.body; }

    public void setBody(String body) {
        this.body = body; }

    public String getTotal_fee() {
        return this.total_fee; }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee; }

    public String getPara_id() {
        return this.para_id; }

    public void setPara_id(String para_id) {
        this.para_id = para_id; }

    public String getApp_id() {
        return this.app_id; }

    public void setApp_id(String app_id) {
        this.app_id = app_id; }

    public String getPara_tradeNo() {
        return this.para_tradeNo; }

    public void setPara_tradeNo(String para_tradeNo) {
        this.para_tradeNo = para_tradeNo; }

    public String getNofity_url() {
        return this.nofity_url; }

    public void setNofity_url(String nofity_url) {
        this.nofity_url = nofity_url;
    }
}