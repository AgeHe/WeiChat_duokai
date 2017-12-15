package com.zhushou.weichat.bean;

/**
 * Created by Administrator on 2017/1/13.
 */

public class CommonProblemInfo {

    private String title;
    private String content;
    private boolean isOpen;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
