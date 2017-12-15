package com.zhushou.weichat.addfriends.base;

/**
 * Created by Administrator on 2017/3/29.
 */

public class TbContacts {
    private String name;
    private String number;

    public TbContacts() {
        super();
    }

    public TbContacts(String name, String number) {
        super();
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Tb_contacts [name=" + name + ", number=" + number + "]";
    }
}
