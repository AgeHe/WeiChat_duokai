package com.zhushou.weichat.utils.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/8/17.
 */

public class DDBHelper extends SQLiteOpenHelper {


    private static final String TAG = "DDBHelper";

    public DDBHelper(Context context) {
        super(context, DBLibs.DATABASE_NAME, null, DBLibs.DATABASE_VERSION);
    }

    /**
     * 第一次创建数据库的时候调用此方法
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBLibs.TABLE_USER_COMSUPTION_RECORD);
//        sqLiteDatabase.execSQL(DBLibs.TABLE_LUNBO);
    }


    /**
     *
     * 更新数据库的时候调用此方法
     *
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        switch (i1) {
//            case 2:
//                sqLiteDatabase.execSQL("alter table " + DBLibs.tableName_comsuption_record + " add column testtest varchar(20)");
//                break;
//            case 3:
//                sqLiteDatabase.execSQL("alter table " + DBLibs.tableName_comsuption_record + " add column testtest varchar(20)");
//                break;
//            case 4:
//                try {
//                    sqLiteDatabase.execSQL("alter table " + DBLibs.tableName_comsuption_record + " add column testtest varchar(20)");
//                } catch (Exception e) {
//                    Log.e(TAG, "onUpgrade: " + e.getMessage());
//                }
//                sqLiteDatabase.execSQL("alter table " + DBLibs.tableName_comsuption_record + " add column test123 varchar(20)");
//                break;
//            case 5:
//                try {
//                    sqLiteDatabase.execSQL("alter table " + DBLibs.tableName_comsuption_record + " add column testtest varchar(20)");
//                } catch (Exception e) {
//                    Log.e(TAG, "onUpgrade: " + e.getMessage());
//                }
//                try {
//                    sqLiteDatabase.execSQL("alter table " + DBLibs.tableName_comsuption_record + " add column test123 varchar(20)");
//                } catch (Exception e) {
//                    Log.e(TAG, "onUpgrade: " + e.getMessage());
//                }
//                sqLiteDatabase.execSQL("alter table " + DBLibs.tableName_comsuption_record + " add column 123test varchar(20)");
//                break;
            default:

        }
    }
}
