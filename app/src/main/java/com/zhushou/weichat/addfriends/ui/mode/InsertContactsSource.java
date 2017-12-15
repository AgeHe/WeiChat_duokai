package com.zhushou.weichat.addfriends.ui.mode;

import com.zhushou.weichat.addfriends.base.TellSelectorMessageInfo;

/**
 * Created by Administrator on 2017/3/30.
 */

public class InsertContactsSource{

    public interface ViewInterface {
        void startTackleContacts();
        void endTackleContactsLoading();
        void endExceptionStatu();
    }

    public interface ViewController{
        void insertContacts(TellSelectorMessageInfo info);
        void clearContacts();
    }

}
