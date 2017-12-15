package com.zhushou.weichat.utils.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhushou.weichat.uinew.info.ComsuptionInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 */

public class OperationDB {
    DDBHelper dbHelper;

    public OperationDB(Context context) {
        dbHelper = new DDBHelper(context);
    }

    /**
     * 通过订单状态获取消费记录
     * @param comsuptionStatus
     * @return
     */
    public List<ComsuptionInfo> selectComsuptionMsg(int comsuptionStatus){
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        List<ComsuptionInfo> resultArrayData = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+DBLibs.tableName_comsuption_record+" where payComplateStatus="+comsuptionStatus,null);
        while (cursor.moveToNext()){
            resultArrayData.add(new ComsuptionInfo(cursor.getString(cursor.getColumnIndex("payType")),
                    cursor.getString(cursor.getColumnIndex("orderNumber")),
                    cursor.getString(cursor.getColumnIndex("insertDate")),
                    cursor.getString(cursor.getColumnIndex("price")),
                    cursor.getString(cursor.getColumnIndex("commodityName")),
                    cursor.getString(cursor.getColumnIndex("commodityFunctionType")),
                    cursor.getInt(cursor.getColumnIndex("payComplateStatus")),
                    cursor.getString(cursor.getColumnIndex("payChannelType")),
                    cursor.getString(cursor.getColumnIndex("other"))));
        }
        db.close();
        return resultArrayData;
    }

    /**
     * 通过订单号修改订单状态
     * @param orderNumber
     * @param chengeStatus
     */
    public ComsuptionInfo changePayComplateStatus(String orderNumber,int chengeStatus){

        SQLiteDatabase db =dbHelper.getReadableDatabase();
        db.execSQL("update "+DBLibs.tableName_comsuption_record+" set payComplateStatus=? where orderNumber=?",
                new Object[]{chengeStatus,orderNumber});
        ComsuptionInfo comsuptionInfo = null;
        Cursor cursor = db.rawQuery("select * from complate_table where orderNumber='"+orderNumber+"'",null);
        while (cursor.moveToNext()){
            comsuptionInfo = new ComsuptionInfo(cursor.getString(cursor.getColumnIndex("payType")),
                    cursor.getString(cursor.getColumnIndex("orderNumber")),
                    cursor.getString(cursor.getColumnIndex("insertDate")),
                    cursor.getString(cursor.getColumnIndex("price")),
                    cursor.getString(cursor.getColumnIndex("commodityName")),
                    cursor.getString(cursor.getColumnIndex("commodityFunctionType")),
                    cursor.getInt(cursor.getColumnIndex("payComplateStatus")),
                    cursor.getString(cursor.getColumnIndex("payChannelType")),
                    cursor.getString(cursor.getColumnIndex("other")));
        }
        db.close();
        return comsuptionInfo;
    }
    /**
     * 通过订单号查询订单信息
     * @param orderNumber
     */
    public ComsuptionInfo selectOerder(String orderNumber){
        ComsuptionInfo comsuptionInfo = null;
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from complate_table where orderNumber='"+orderNumber+"'",null);
        while (cursor.moveToNext()){
            comsuptionInfo = new ComsuptionInfo(cursor.getString(cursor.getColumnIndex("payType")),
                    cursor.getString(cursor.getColumnIndex("orderNumber")),
                    cursor.getString(cursor.getColumnIndex("insertDate")),
                    cursor.getString(cursor.getColumnIndex("price")),
                    cursor.getString(cursor.getColumnIndex("commodityName")),
                    cursor.getString(cursor.getColumnIndex("commodityFunctionType")),
                    cursor.getInt(cursor.getColumnIndex("payComplateStatus")),
                    cursor.getString(cursor.getColumnIndex("payChannelType")),
                    cursor.getString(cursor.getColumnIndex("other")));
        }
        db.close();
        return comsuptionInfo;
    }

    /**
     * 添加一条消费信息
     * @param info
     */
    public void insertComsuptionInfo(ComsuptionInfo info){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL("insert into "+DBLibs.tableName_comsuption_record+"(payType,orderNumber,insertDate,price,commodityName,commodityFunctionType,payComplateStatus,payChannelType,other) values(?,?,?,?,?,?,?,?,?)",
                new Object[]{info.getPayType(),info.getOrderNumber(),info.getInsertDate(),info.getPrice(),info.getCommodityName(),
                info.getCommodityFunctionType(),info.getPayComplateStatus(),info.getPayChannelType(),info.getOther()});
        db.close();

    }


    /**
     * 获取消费记录表所有数据
     * @return
     */
    public List<ComsuptionInfo> selectOrderTable(){
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        List<ComsuptionInfo> resultArrayData = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+DBLibs.tableName_comsuption_record,null);
        while (cursor.moveToNext()){
            resultArrayData.add(new ComsuptionInfo(cursor.getString(cursor.getColumnIndex("payType")),
                    cursor.getString(cursor.getColumnIndex("orderNumber")),
                    cursor.getString(cursor.getColumnIndex("insertDate")),
                    cursor.getString(cursor.getColumnIndex("price")),
                    cursor.getString(cursor.getColumnIndex("commodityName")),
                    cursor.getString(cursor.getColumnIndex("commodityFunctionType")),
                    cursor.getInt(cursor.getColumnIndex("payComplateStatus")),
                    cursor.getString(cursor.getColumnIndex("payChannelType")),
                    cursor.getString(cursor.getColumnIndex("other"))));
        }
        db.close();
        Collections.reverse(resultArrayData);
        return resultArrayData;
    }

}
