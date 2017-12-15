package com.zhushou.weichat.utils.database;

/**
 * Created by Administrator on 2017/8/17.
 */

public class DBLibs {

    public static final String DATABASE_NAME = "DivinationDB"; //数据库名称
    public static final int DATABASE_VERSION = 5;


    public static final String tableName_comsuption_record = "complate_table";
    /**
     * 用户消费表
     */
    public static final String TABLE_USER_COMSUPTION_RECORD = "create table "+tableName_comsuption_record+"(id integer PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "payType varchar(20)," +//支付类型
            "orderNumber varchar(50)," +//订单号
            "insertDate varchar(20)," +//数据记录时间
            "price varchar(10)," +//价格
            "commodityName varchar(50)," +//商品名称
            "commodityFunctionType varchar(30),"+ //商品功能类型
            "payComplateStatus int," + //目前的付费状态
            "payChannelType varchar(20),"+ //支付渠道类型
            "other TEXT" +//其他 文本类型 自定义JSON数据
            ")";

    public static final String tablename_lunBo_record = "lunbo_table";

    public static final String TABLE_LUNBO = "create table "+tablename_lunBo_record+"(id integer PRIMARY KEY AUTOINCREMENT NOT NULL,"+
            "nickName varchar(50),"+
            "open varchar(20),"+
            "get varchar(20)"+")";


    public static final String tableName_appBase_data = "base_table";

    /**
     * 应用基础信息表
     */
    public static final String TABLE_USER_APPBASE_DATA = "create table "+tableName_appBase_data+"(id integer PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "prices text,customerService text," +//客服
            "paymentTypeSwitch text," + //支付方式开关
            "adv text" + //广告配置
            ")";

    public static final int Pay_status_Finish = 0x01; //订单完成
    public static final int Pay_status_Successful = 0x02; //支付成功
    public static final int Pay_status_Failure = 0x03; //支付失败
    public static final int Pay_status_E_Successful = 0x04; //支付成功但是执行异常 功能开启异常 暂时不做处理交由客服退款）
    public static final int Pay_status_failure_finish = 0x05; //支付失败 并且用户已经放弃该订单，
    public static final int Pay_status_Unknown = 0x06;//未知订单 （在支付调起得时候标记该订单）
    public static final int Pay_status_failure_E = 0x07;//失败异常订单 （属于程序执行错误订单  暂时不做处理交由客服退款）
    public static final int Pay_status_bd = 0x08;//补单
    public static final int pay_status_bd_s = 0x09;//补单完成
    public static final int pay_open_redpacket_f = 0x10;//开启红包失败


    public static String getOrderStatusName(int orderStatus){
        String resultStr = "";
        switch (orderStatus){
            case Pay_status_Finish:
                resultStr = "订单完成";
                break;
            case Pay_status_Failure:
                resultStr = "支付失败";
                break;
            case Pay_status_E_Successful:
                resultStr = "支付成功，功能开启异常";
                break;
            case Pay_status_failure_finish:
                resultStr = "支付失败，已放弃";
                break;
            case Pay_status_Unknown:
                resultStr = "未知的订单状态";
                break;
            case Pay_status_failure_E:
                resultStr = "订单异常失败";
                break;
        }
        return resultStr;
    }

}
