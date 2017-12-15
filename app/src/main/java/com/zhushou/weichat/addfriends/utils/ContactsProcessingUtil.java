package com.zhushou.weichat.addfriends.utils;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;

import com.zhushou.weichat.addfriends.base.TbContacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/29.
 */

public class ContactsProcessingUtil {
    /**
     * 批量添加通讯录
     *
     * @throws OperationApplicationException
     * @throws RemoteException
     */
    public static void BatchAddContact(List<TbContacts> list,Context context)
            throws RemoteException, OperationApplicationException {
//        GlobalConstants.PrintLog_D("[GlobalVariables->]BatchAddContact begin");
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = 0;
        for (TbContacts contact : list) {
            rawContactInsertIndex = ops.size(); // 有了它才能给真正的实现批量添加

            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .withYieldAllowed(true).build());

            // 添加姓名
            ops.add(ContentProviderOperation
                    .newInsert(
                            android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
                    .withYieldAllowed(true).build());
            // 添加号码
            ops.add(ContentProviderOperation
                    .newInsert(
                            android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(CommonDataKinds.Phone.NUMBER, contact.getNumber())
                    .withValue(CommonDataKinds.Phone.TYPE, CommonDataKinds.Phone.TYPE_MOBILE)
                    .withValue(CommonDataKinds.Phone.LABEL, "").withYieldAllowed(true).build());
        }
        if (ops != null) {
            // 真正添加
            ContentProviderResult[] results = context.getContentResolver()
                    .applyBatch(ContactsContract.AUTHORITY, ops);
            // for (ContentProviderResult result : results) {
            // GlobalConstants
            // .PrintLog_D("[GlobalVariables->]BatchAddContact "
            // + result.uri.toString());
            // }
        }
    }

    /**
     * 清空系统通信录数据
     */
    public static void clearContact(Context context)throws Exception {
        ContentResolver cr = context.getContentResolver();
        // 查询contacts表的所有记录
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        // 如果记录不为空
        if (cursor.getCount() > 0) {
            // 游标初始指向查询结果的第一条记录的上方，执行moveToNext函数会判断
            // 下一条记录是否存在，如果存在，指向下一条记录。否则，返回false。
            while (cursor.moveToNext()){
                //  String rawContactId = "";
                // 从Contacts表当中取得ContactId
//                  String id = cursor.getString(cursor
//                          .getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (name==null||!name.contains("DK_")){
                    continue;
                }
                //根据姓名求id
                Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");

                Cursor cursor1 = cr.query(uri, new String[]{ContactsContract.Contacts.Data._ID},"display_name=?", new String[]{name}, null);
                if(cursor1.moveToFirst()){
                    int id = cursor1.getInt(0);
                    //根据id删除data中的相应数据
                    cr.delete(uri, "display_name=?", new String[]{name});
                    uri = Uri.parse("content://com.android.contacts/data");
                    cr.delete(uri, "raw_contact_id=?", new String[]{id+""});
                }
            }
        }
    }
}
