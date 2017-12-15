package com.zhushou.weichat.utils.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OrderDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "myOrder.db";
    public static final String TABLE_NAME = "Orders";
    //订单表中数据行 1.orderNum 订单号 2.paytType支付类型  3.wareType 购买类型 4.functionkey 购买功能key 5.orderPrice 支付金额 6.needCheck 是否需要轮训查询 7.status 订单状态  0.未知 1.失败 2.成功 3.订单成功完成

    public OrderDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        String sql = "create table if not exists " + TABLE_NAME + " (Id integer primary key AUTOINCREMENT,orderNum text,payType text,wareType integer,functionKey text,orderPrice float, needCheck text," +
//                "status integer,date String)";
        String orderSql = "create table "+TABLE_NAME+"(orderid INTEGER PRIMARY KEY AUTOINCREMENT,ordernum varchar(50),paytype varchar(10),waretype INTEGER,functionkey varchar(15),orderprice float,needcheck varchar(10),status INTEGER,date varchar(20),otherdata text)";

        sqLiteDatabase.execSQL(orderSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

}