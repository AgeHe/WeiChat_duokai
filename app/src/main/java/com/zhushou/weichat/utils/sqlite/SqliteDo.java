package com.zhushou.weichat.utils.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zhushou.weichat.bean.OrderDBInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/19.
 * 数据库操作类
 */

public class SqliteDo {
    private static final String TAG = "SqliteDo";
    private OrderDBHelper helper;
    private Context mContext;
    private SQLiteDatabase db;
    public SqliteDo(Context context) {
        mContext = context;
        helper = new OrderDBHelper(mContext);

    }

    /**
     * 生成一条订单信息
     * @param orderNum 订单号
     * @param payType 支付类型
     * @param wareType 商品类型标识
     * @param functionkey 商品类型key
     * @param orderPrice 商品价格
     * @return
     */
    public OrderDBInfo getOrderInfoParams(String orderNum,String payType,int wareType,String functionkey,float orderPrice,String otherData){
        OrderDBInfo info = new OrderDBInfo();
        info.setOrdernum(orderNum);
        info.setPaytype(payType);
        info.setWareType(wareType);
        info.setOrderprice(orderPrice);
        info.setFunctionkey(functionkey);
        info.setNeedcheck("false");
        info.setStatus(0);
        info.setDate(getNow());
        info.setOtherdata(otherData);
        return info;
    }

    /**
     *插入一条订单信息
     * @param info 订单信息
     * @return
     */
    public void insertOrder (OrderDBInfo info){
        Log.i(TAG, "insertOrder:插入一条订单信息 orderNum="+info.getOrdernum());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into "+OrderDBHelper.TABLE_NAME+"(ordernum,paytype,waretype,functionkey,orderprice,needcheck,status,date,otherdata) values(?,?,?,?,?,?,?,?,?)",
                new Object[] {info.getOrdernum(),info.getPaytype(),info.getWareType(),info.getFunctionkey(),
                info.getOrderprice(),info.getNeedcheck(),info.getStatus(),info.getDate(),info.getOtherdata()});
        db.close();
    }

    /**
     * 通过订单号修改支付状态
     * @param orderNum 订单号 ‘长度最大为50个字符’
     * @param status 0.未知 1.失败 2.成功 3 订单成功完成
     * @return
     */
    public void changeOrderstatus(String orderNum,int status){
        Log.i(TAG, "changeOrderstatus:通过订单号修改支付状态 ordernum="+orderNum+"&&status="+status);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("update "+OrderDBHelper.TABLE_NAME+" set status=? where ordernum=?", new Object[] {status,orderNum});
        db.close();
    }

    /**
     * 通过订单号修改轮询状态
     * @param orderNum 订单号
     * @param needCheck 是否允许轮询
     */
    public void changeNeedCheckStatus(String orderNum,String needCheck){
        Log.i(TAG, "changeNeedCheckStatus:通过订单号修改轮询状态 ordernum="+orderNum+"&&needcheck="+needCheck);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("update "+OrderDBHelper.TABLE_NAME+" set needcheck=? where ordernum=?", new Object[] {needCheck,orderNum});
        db.close();
    }

    /**
     * 通过订单号修改支付状态和轮询状态
     * @param orderNum 订单号
     * @param needCheck 是否允许轮询
     * @param status 订单状态 0.未知 1.失败 2.成功 3 订单成功完成 4放弃订单
     */
    public void changeNeedCheckAndOrderStatus(String orderNum,String needCheck,int status){
        Log.i(TAG, "changeNeedCheckAndOrderStatus:通过订单号修改支付状态和轮询状态 orderNum="+orderNum+"&&needcheck="+needCheck+"&&status="+status);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("update "+OrderDBHelper.TABLE_NAME+" set needcheck=? and status=? where ordernum=?", new Object[] {needCheck,status,orderNum});
        db.close();
    }

    /**
     * 获取需要轮询且订单状态未知(0)的数据
     * @return 订单列表 Array OrderInfo
     */
    public ArrayList<OrderDBInfo> getNeedCheckList(){
        Log.i(TAG, "getNeedCheckList: 获取需要轮询且订单状态未知(0)的数据");
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<OrderDBInfo> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+OrderDBHelper.TABLE_NAME+" where needcheck='true' and status<=1", null);
        while (cursor.moveToNext()){
            String ordernum = cursor.getString(cursor.getColumnIndex("ordernum"));
            String paytype = cursor.getString(cursor.getColumnIndex("paytype"));
            String functionkey = cursor.getString(cursor.getColumnIndex("functionkey"));
            String needcheck = cursor.getString(cursor.getColumnIndex("needcheck"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String otherdata = cursor.getString(cursor.getColumnIndex("otherdata"));
            int wareType = cursor.getInt(cursor.getColumnIndex("waretype"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            float orderprice = cursor.getFloat(cursor.getColumnIndex("orderprice"));
            list.add(new OrderDBInfo(ordernum, paytype, wareType, functionkey, orderprice, needcheck, status, date,otherdata));
        }
        db.close();
        Log.i(TAG, "getNeedCheckList: 获取需要轮询且订单状态未知(0)的数据 listSize="+list.size());
        return list;
    }

    /**
     * 获取需要处理并且状态未知和失败的订单数据
     * @return 订单列表 Array OrderInfo
     */
    public ArrayList<OrderDBInfo> getProcessedOrderList(){
        Log.i(TAG, "getNeedCheckList: 获取需要轮询且订单状态未知(0)的数据");
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<OrderDBInfo> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+OrderDBHelper.TABLE_NAME+" where status<=1", null);
        while (cursor.moveToNext()){
            String ordernum = cursor.getString(cursor.getColumnIndex("ordernum"));
            if (ordernum==null)
                continue;
            String paytype = cursor.getString(cursor.getColumnIndex("paytype"));
            String functionkey = cursor.getString(cursor.getColumnIndex("functionkey"));
            String needcheck = cursor.getString(cursor.getColumnIndex("needcheck"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String otherdata = cursor.getString(cursor.getColumnIndex("otherdata"));
            int wareType = cursor.getInt(cursor.getColumnIndex("waretype"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            float orderprice = cursor.getFloat(cursor.getColumnIndex("orderprice"));
            list.add(new OrderDBInfo(ordernum, paytype, wareType, functionkey, orderprice, needcheck, status, date,otherdata));
        }
        db.close();
        Log.i(TAG, "getNeedCheckList: 获取需要轮询且订单状态未知(0)的数据 listSize="+list.size());
        return list;
    }

    /**
     * 获取订单已支付成功 但是未处理的数据
     * @return 订单列表 Array OrderInfo
     */
    public ArrayList<OrderDBInfo> getSuccessfulOrderList(){
        Log.i(TAG, "getSuccessfulOrderList: 获取支付成功 需要处理的订单");
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<OrderDBInfo> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+OrderDBHelper.TABLE_NAME+" where status=2", null);
        while (cursor.moveToNext()){
            String ordernum = cursor.getString(cursor.getColumnIndex("ordernum"));
            String paytype = cursor.getString(cursor.getColumnIndex("paytype"));
            String functionkey = cursor.getString(cursor.getColumnIndex("functionkey"));
            String needcheck = cursor.getString(cursor.getColumnIndex("needcheck"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String otherdata = cursor.getString(cursor.getColumnIndex("otherdata"));
            int wareType = cursor.getInt(cursor.getColumnIndex("waretype"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            float orderprice = cursor.getFloat(cursor.getColumnIndex("orderprice"));
            list.add(new OrderDBInfo(ordernum, paytype, wareType, functionkey, orderprice, needcheck, status, date,otherdata));
        }
        db.close();
        Log.i(TAG, "getSuccessfulOrderList: 获取支付成功 需要处理的订单 list.size="+list.size());
        return list;
    }



    public void closeDb(){
        if(db!=null){
            db.close();
        }
    }

    private String  getNow(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String result = formatter.format(date);
        return result ==null?"":formatter.format(date);
    }

}
