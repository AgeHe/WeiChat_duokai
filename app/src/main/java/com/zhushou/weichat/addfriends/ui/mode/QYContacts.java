package com.zhushou.weichat.addfriends.ui.mode;

import android.app.Activity;
import android.content.OperationApplicationException;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;

import com.zhushou.weichat.R;
import com.zhushou.weichat.addfriends.base.TbContacts;
import com.zhushou.weichat.addfriends.base.TellSelectorMessageInfo;
import com.zhushou.weichat.addfriends.utils.ContactsProcessingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/3/30.
 */

public class QYContacts implements InsertContactsSource.ViewController {

    private Activity mAcrivity;
    private InsertContactsSource.ViewInterface viewInterface;
    private List<TbContacts> listContacts;

    public QYContacts(Activity mAcrivity,InsertContactsSource.ViewInterface viewInterface){
        this.mAcrivity = mAcrivity;
        this.viewInterface = viewInterface;
        listContacts = new ArrayList<>();
    }

    @Override
    public void insertContacts(TellSelectorMessageInfo info){
        if (info.getShortCode()==null)
            return;
        viewInterface.startTackleContacts();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Random random=new Random();
                int max = 9999;
                int min = 3101;
                for (int i=0;i<=info.getContactsSum();i++){
                    int randomInt=random.nextInt(max)%(max-min+1) + min;
                    TbContacts tbContacts = new TbContacts();
                    tbContacts.setName("DK_"+String.valueOf(info.getShortCode())+randomInt);
                    tbContacts.setNumber(String.valueOf(info.getShortCode())+randomInt);
                    listContacts.add(tbContacts);
                }
                try {
                    ContactsProcessingUtil.BatchAddContact(listContacts,mAcrivity);
                    myHandler.sendEmptyMessage(0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    myHandler.sendEmptyMessage(1);
                } catch (OperationApplicationException e){
                    e.printStackTrace();
                    myHandler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    @Override
    public void clearContacts() {
        viewInterface.startTackleContacts();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ContactsProcessingUtil.clearContact(mAcrivity);
                    myHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    myHandler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);

            switch (msg.what){
                case 0: //联系人处理成功
                    viewInterface.endTackleContactsLoading();
//                    Toast.makeText(mAcrivity,"Success",Toast.LENGTH_SHORT).show();
                    break;
                case 1://联系人处理异常
                    Toast.makeText(mAcrivity,mAcrivity.getResources().getString(R.string.wx_addf_e_toast), Toast.LENGTH_SHORT).show();
                    viewInterface.endExceptionStatu();
                    break;
                case 2:

                    break;
                case 3:

                    break;
            }
        }
    };



}
