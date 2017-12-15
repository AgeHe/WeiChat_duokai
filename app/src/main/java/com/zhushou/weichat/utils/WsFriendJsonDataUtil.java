package com.zhushou.weichat.utils;

import android.content.Context;
import android.util.Log;

import com.zhushou.weichat.bean.WxFriendMsgInfo;
import com.zhushou.weichat.ui.view.ClickAnimationTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/6/21.
 */

public class WsFriendJsonDataUtil {

    public static final String DZ_SP = "dz.sp";
    public static final String DZ_NUM_SP = "dz.num.sp";
    private static final String TAG = "WsFriendJsonDataUtil";

    public static List<WxFriendMsgInfo> analyticWsjson(String jsonStr){
        List<WxFriendMsgInfo> arrayData = new ArrayList<>();

        try {
            JSONObject jo = new JSONObject(jsonStr);
            JSONArray ja = jo.getJSONArray("data");

            for (int i=0;i<ja.length();i++){
                JSONObject jajo = ja.getJSONObject(i);
                WxFriendMsgInfo info = new WxFriendMsgInfo();
                info.setId(jajo.getInt("id"));
                info.setUid(jajo.getInt("uid"));
                info.setViews(jajo.getInt("views"));
                info.setFavs(jajo.getInt("favs"));
                info.setComs(jajo.getInt("coms"));
                info.setUser_name(jajo.getString("user_name"));
                info.setUser_pp(jajo.getString("user_pp"));
                info.setDate(jajo.getString("date"));

                JSONObject dataJo = jajo.getJSONObject("data");
                WxFriendMsgInfo.FData fDataInfo = new WxFriendMsgInfo.FData();
                fDataInfo.setTextContent(dataJo.getString("textContent"));
                if (dataJo.getJSONArray("previews").length()>0)
                    fDataInfo.setPreviews(dataJo.getJSONArray("previews").getString(0).split(","));
                if (dataJo.getJSONArray("videos").length()>0)
                    fDataInfo.setVideos(dataJo.getJSONArray("videos").getString(0));
                info.setData(fDataInfo);

                JSONArray commentsJa = jajo.getJSONArray("comments");
                List<WxFriendMsgInfo.FComments> commentsList = new ArrayList<>();
                for (int c=0;c<commentsJa.length();c++){
                    WxFriendMsgInfo.FComments commentInfo = new WxFriendMsgInfo.FComments();
                    JSONObject commentJo = commentsJa.getJSONObject(c);
                    commentInfo.setId(commentJo.getInt("id"));
                    commentInfo.setUname(commentJo.getString("uname"));
                    commentInfo.setContent(commentJo.getString("content"));
                    commentInfo.setCreated(commentJo.getString("created"));
                    commentsList.add(commentInfo);
                }
                Collections.sort(commentsList, new SortByAge());
                info.setComments(commentsList);
                arrayData.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "analyticWsjson: "+e.getMessage());
            return null;
        }
        return arrayData;
    }

    static class SortByAge implements Comparator {
        public int compare(Object o1, Object o2) {
            WxFriendMsgInfo.FComments s1 = (WxFriendMsgInfo.FComments) o1;
            WxFriendMsgInfo.FComments s2 = (WxFriendMsgInfo.FComments) o2;
            if (s1.getId() < s2.getId())
                return -1;
            return 1;
        }
    }
    public static List<WxFriendMsgInfo.FComments> sortList(List<WxFriendMsgInfo.FComments> commentsList){
        List<WxFriendMsgInfo.FComments> listData = new ArrayList<>();
        if (commentsList==null)
            return listData;

        for (int i=0;i<commentsList.size();i++){
            WxFriendMsgInfo.FComments itemInfo = commentsList.get(i);
            for (int s=0;s<commentsList.size();s++){
                WxFriendMsgInfo.FComments itemInfos = commentsList.get(i);
                if (i!=s){
                    if (itemInfo.getId()<itemInfos.getId()){
                        itemInfo.setSort(s);
                        itemInfos.setSort(i);
                    }
                }
            }

        }

        for (int d=0;d<commentsList.size();d++){
            listData.add(commentsList.get(d).getSort(),commentsList.get(d));
        }

        return listData;
    }


    public static int isDianzan(Context context,int msgId){
        int isSelected = ClickAnimationTextView.selected; // 默认返回选中
        String dzJsonStr = SharedPreferencesUtils.getParam(context,DZ_SP);
        String newMsgIdStr = "";
        if (dzJsonStr!=null&&!dzJsonStr.equals("")){
            String[] strArray = dzJsonStr.split(",");
            try {
                for (int i=0;i<strArray.length;i++){
                    if (Integer.valueOf(strArray[i])==msgId){
                        isSelected = ClickAnimationTextView.cancelSelect;// 已点赞，返回取消点赞
                    }else{
                        newMsgIdStr+=strArray[i]+",";
                    }
                }
            }catch (Exception e){
            }
        }
        if (isSelected==ClickAnimationTextView.selected){
            newMsgIdStr+=msgId+",";
        }
        SharedPreferencesUtils.setParam(context,DZ_SP,newMsgIdStr);
        return isSelected;
    }

    public static boolean getDianzan(Context context,int msgId){
        boolean isdianzan = false;
        String dzJsonStr = SharedPreferencesUtils.getParam(context,DZ_SP);
        if (dzJsonStr!=null&&!dzJsonStr.equals("")){
            String[] strArray = dzJsonStr.split(",");
            try {
                for (int i=0;i<strArray.length;i++){
                    if (Integer.valueOf(strArray[i])==msgId){
                        isdianzan = true;
                        break;
                    }
                }
            }catch (Exception e){
            }
        }
        return isdianzan;
    }

    public static void msgDZNum(Context context,int num,int id){
        boolean isExstence = false;
        String dzJsonStr = SharedPreferencesUtils.getParam(context,DZ_NUM_SP);
        JSONArray jsonArray = new JSONArray();
        if (dzJsonStr!=null&&!dzJsonStr.equals("")){
            try {
                JSONArray ja = new JSONArray(dzJsonStr);
                for (int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    if (jo.getInt("id")==id){
                        isExstence = true;
                        jo.put("num",num);
                        jsonArray.put(jo);
                    }else{
                        jsonArray.put(jo);
                    }
                }
                if (!isExstence){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id",id);
                    jsonObject.put("num",num);
                    jsonArray.put(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",id);
                jsonObject.put("num",num);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        SharedPreferencesUtils.setParam(context,DZ_NUM_SP,jsonArray.toString());
    }

    public static int getDZNum(Context context,int id) {
        int num = 0;
        String dzJsonStr = SharedPreferencesUtils.getParam(context, DZ_NUM_SP);
        if (dzJsonStr != null && !dzJsonStr.equals("")) {
            try {
                JSONArray ja = new JSONArray(dzJsonStr);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    if (jo.getInt("id") == id) {
                        num = jo.getInt("num");
                        break;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("hh", "getDZNum: "+e.getMessage() );
            }
        }
        return num;
    }
}
