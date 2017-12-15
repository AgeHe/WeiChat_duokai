package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.ListDataSave;
import com.zhushou.weichat.screenshot.roleIndex.ListViewAdapter;
import com.zhushou.weichat.screenshot.roleIndex.StringHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

//import static com.umeng.analytics.b.g.R;

/**
 * Created by zhanglinkai on 2017/3/24.
 * 功能:
 */

public class RoleActivity extends Activity implements ListViewAdapter.editItem{
    private HashMap<String, Integer> selector;// 存放含有索引字母的位置
    private ListView listView;
    private LinearLayout layoutIndex;
    private TextView tv_show;
    private ListViewAdapter adapter;
    private String[] indexStr = {"↑", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z","#"};
    //private List<Map<String,String>> persons = null;
    private List<Map<String,String>> t = null;
    private int height;// 字体高度
    private boolean flag = false;
    private ImageView role_title_iv;
    private ImageView role_add_iv;
    private ListDataSave save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_role);

        save=new ListDataSave(this,"tt");

        listView = (ListView) findViewById(R.id.listView);
        layoutIndex = (LinearLayout) this.findViewById(R.id.layout);
        layoutIndex.setBackgroundColor(Color.parseColor("#00ffffff"));
        tv_show = (TextView) findViewById(R.id.tv);
        tv_show.setVisibility(View.GONE);
        role_title_iv = (ImageView) findViewById(R.id.role_title_iv);
        role_add_iv = (ImageView) findViewById(R.id.role_add_iv);
        role_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        role_add_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoleActivity.this, RoleAddActivity.class);
                startActivityForResult(intent,4);
            }
        });


        t=addIndex(setData());
        adapter = new ListViewAdapter(this,this);
        adapter.setList(t);
        listView.setAdapter(adapter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // 在oncreate里面执行下面的代码没反应，因为oncreate里面得到的getHeight=0
        if (!flag) {
            height = layoutIndex.getMeasuredHeight() / indexStr.length;
            getIndexView();
            flag = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==4){
            if (resultCode==5){

                String name=data.getStringExtra("name");
                String iconpath=data.getStringExtra("iconpath");
                Map<String,String> map=new HashMap<String, String>();
                map.put("name",name);
                map.put("image",iconpath);
                List<Map<String,String>> l=save.getDataList("tt");
                l.add(map);
                List<Map<String,String>> m=sortIndex(l);
                save.setDataList("tt",m);
                adapter.setList(addIndex(m));
                adapter.notifyDataSetChanged();
            }
        }
        if (requestCode==6){
            if (resultCode==7){
                String tag=data.getStringExtra("tag");
                switch (tag){
                    case "sure":
                        int p=data.getIntExtra("position",10000);
                        String name=data.getStringExtra("name");
                        String image=data.getStringExtra("image");
                        List<Map<String,String>> l=save.getDataList("tt");
                        if (p!=10000){
                            l.get(p).put("name",name);
                            l.get(p).put("image",image);
                        }
                        List<Map<String,String>> m=sortIndex(l);
                        save.setDataList("tt",l);
                        adapter.setList(addIndex(m));
                        adapter.notifyDataSetChanged();
                        break;
                    case "clear":

                        int po=data.getIntExtra("position",10000);
                        List<Map<String,String>> n=save.getDataList("tt");
                        if (po!=10000){
                           n.remove(po);
                        }
                        List<Map<String,String>> h=sortIndex(n);
                        save.setDataList("tt",h);
                        adapter.setList(addIndex(h));
                        adapter.notifyDataSetChanged();

                        break;
                }
            }
        }
    }

    /**
     * 获取排序后的新数据
     *
     * @param
     * @return
     */
    //将索引的字母添加到list
    private List<Map<String,String>> addIndex(List<Map<String,String>> persons){
        List<Map<String,String>> list=new ArrayList<>();
        TreeSet<String> set1 = new TreeSet<String>();
        for (int i = 0; i < persons.size(); i++) {
            list.add(persons.get(i));
        }
        // 获取初始化数据源中的首字母，添加到set中
        for (Map<String,String> person : list) {
            String h=StringHelper.getPinYinHeadChar(person.get("name")).substring(0, 1);
            set1.add(h);
        }
        //将索引字母加入到list
        for (String s:set1){
            Map<String,String> map=new HashMap<>();
            map.put("name",s);
            map.put("image","");
            list.add(map);
        }
        //获取拼音名字
        for (int i = 0; i < list.size(); i++) {
            String pinyin=StringHelper.getPingYin(list.get(i).get("name")).toLowerCase();
            list.get(i).put("pinyin",pinyin);
        }
        //根据拼音名字排序
        Collections.sort(list, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                if (o1.get("pinyin").equals(o2.get("pinyin"))){
                    return -1;
                }
                return o1.get("pinyin").compareTo(o2.get("pinyin"));
            }
        });
        return list;
    }


    public List<Map<String,String>> sortIndex(List<Map<String,String>> persons) {
        List<Map<String,String>> list=new ArrayList<>();

        for (int i = 0; i < persons.size(); i++) {
            list.add(persons.get(i));
        }

        //获取拼音名字
        for (int i = 0; i < list.size(); i++) {
            String pinyin=StringHelper.getPingYin(list.get(i).get("name")).toLowerCase();
            list.get(i).put("pinyin",pinyin);
        }
        //根据拼音名字排序
        Collections.sort(list, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                if (o1.get("pinyin").equals(o2.get("pinyin"))){
                    return -1;
                }
                return o1.get("pinyin").compareTo(o2.get("pinyin"));
            }
        });
        return list;
    }

    /**
     * 绘制索引列表
     */
    public void getIndexView() {
        float index = getSharedPreferences("index", Context.MODE_PRIVATE).getFloat("index", 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, height);
        for (int i = 0; i < indexStr.length; i++) {
            final TextView tv = new TextView(this);
            tv.setLayoutParams(params);
            tv.setText(indexStr[i]);
            tv.setPadding(10, 0, 10, 0);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(11 * index);
            layoutIndex.addView(tv);
            layoutIndex.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event)

                {
                    //点击索引框

                    selector = new HashMap<String, Integer>();
                    for (int j = 0; j < indexStr.length; j++) {// 循环字母表，找出newPersons中对应字母的位置
                        for (int i = 0; i < t.size(); i++) {
                            if (t.get(i).get("name").equals(indexStr[j])) {
                                selector.put(indexStr[j], i);
                            }
                        }
                    }

                    float y = event.getY();
                    int index = (int) (y / height);
                    if (index > -1 && index < indexStr.length) {// 防止越界
                        String key = indexStr[index];
                        if (selector.containsKey(key)) {
                            int pos = selector.get(key);
                            if (listView.getHeaderViewsCount() > 0) {// 防止ListView有标题栏，本例中没有。
                                listView.setSelectionFromTop(
                                        pos + listView.getHeaderViewsCount(), 0);
                            } else {
                                listView.setSelectionFromTop(pos, 0);// 滑动到第一项
                            }
                            tv_show.setVisibility(View.VISIBLE);
                            tv_show.setText(indexStr[index]);
                        }
                    }
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            layoutIndex.setBackgroundColor(Color
                                    .parseColor("#606060"));
                            break;

                        case MotionEvent.ACTION_MOVE:

                            break;
                        case MotionEvent.ACTION_UP:
                            layoutIndex.setBackgroundColor(Color
                                    .parseColor("#00ffffff"));
                            tv_show.setVisibility(View.GONE);
                            break;
                    }
                    return true;
                }
            });
        }
    }

    /**
     * 设置数据
     */
    private List<Map<String,String>> setData() {
        List<Map<String,String>> persons=new ArrayList<>();
        persons= save.getDataList("tt");
        return sortIndex(persons);
    }
    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }

    @Override
    public void edititem(int position,int l) {
            Intent intent = new Intent(this, RoleEditActivity.class);
            intent.putExtra("name", t.get(position).get("name"));
            intent.putExtra("image", t.get(position).get("image"));
            intent.putExtra("position", l);
            startActivityForResult(intent, 6);

    }

    @Override
    public void selectitem(int position, int l) {
        String tag=getIntent().getStringExtra("tag");
        if (tag==null){
            tag="";
        }
        if (tag.equals("redpacket")) {
            intentToResult(l);
        }else if (tag.equals("newfriend")){
            intentToResult(l);
        }else if (tag.equals("call")){
            intentToResult(l);
        }else if (tag.equals("voice")){
            intentToResult(l);
        }else if (tag.equals("singletalk")){
            intentToResult(l);
        }
    }

    private void intentToResult(int l){
        Intent intent=new Intent();
        intent.putExtra("position",l);
        setResult(2,intent);
        finish();
    }
}