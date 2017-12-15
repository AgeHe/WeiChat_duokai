package com.zhushou.weichat.uinew.interfac;

import com.zhushou.weichat.uinew.info.HomeItemInfo;

/**
 * Created by Administrator on 2017/11/29.
 */

public class HomeRecyclerItemContract {

    public interface  ItemView{
        void myNotifyDataSetChanged();
        void myNotifyItemInserted(int position);
        void myRemoveItem(int position);
//        void setItemContract(HomeRecyclerItemContract.ItemContract itemContract);
    }

    public interface ItemContract{
        void onLongClick(HomeItemInfo homeItemInfo ,int position);


    }

}
