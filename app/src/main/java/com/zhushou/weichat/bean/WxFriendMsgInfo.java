package com.zhushou.weichat.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/20.
 * 朋友圈信息实体
 */
public class WxFriendMsgInfo {

    private int id;
    private int uid;
    private int views;
    private int favs;
    private int coms;
    private String user_name;
    private String user_pp;
    private String date;
    private FData data;
    private List<FComments> comments;
    private int page;
    private int pagetotal;
    private int code;

    public static class FData{
        private String textContent;
        private String[] previews;
        private String videos;
        private String videoImage;

        public String getTextContent() {
            return textContent;
        }

        public void setTextContent(String textContent) {
            this.textContent = textContent;
        }

        public String[] getPreviews() {
            return previews;
        }

        public void setPreviews(String[] previews) {
            this.previews = previews;
        }

        public String getVideos() {
            return videos;
        }

        public void setVideos(String videos) {
            this.videos = videos;
        }

        public String getVideoImage() {
            return videoImage;
        }

        public void setVideoImage(String videoImage) {
            this.videoImage = videoImage;
        }
    }

    public static class FComments{
        private int id;
        private String uname;
        private String content;
        private String created;
        private int sort;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getFavs() {
        return favs;
    }

    public void setFavs(int favs) {
        this.favs = favs;
    }

    public int getComs() {
        return coms;
    }

    public void setComs(int coms) {
        this.coms = coms;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pp() {
        return user_pp;
    }

    public void setUser_pp(String user_pp) {
        this.user_pp = user_pp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FData getData() {
        return data;
    }

    public void setData(FData data) {
        this.data = data;
    }

    public List<FComments> getComments() {
        return comments;
    }

    public void setComments(List<FComments> comments) {
        this.comments = comments;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagetotal() {
        return pagetotal;
    }

    public void setPagetotal(int pagetotal) {
        this.pagetotal = pagetotal;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
