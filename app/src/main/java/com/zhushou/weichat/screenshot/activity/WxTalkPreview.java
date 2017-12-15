package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.adapter.WxTalkPreViewAdapter;


/**
 * Created by zhanglinkai on 2017/3/20.
 * 功能:
 */

public class WxTalkPreview extends Activity {

    private TextView add_talk_preview_title_name;
    private ImageView add_talk_preview_title_iv;
    private ImageView wxtalk_preview_friend_tag;
    private ListView add_talk_preview_list;
    private List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
    private ImageView list_bg_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addtalk_preview);

        iniUI();
    }

    private void iniUI() {
        add_talk_preview_title_name=(TextView)findViewById(R.id.add_talk_preview_title_name);
        add_talk_preview_title_iv=(ImageView)findViewById(R.id.add_talk_preview_title_iv);
        wxtalk_preview_friend_tag=(ImageView)findViewById(R.id.wxtalk_preview_friend_tag);
        add_talk_preview_list=(ListView)findViewById(R.id.add_talk_preview_list);
        list_bg_iv=(ImageView)findViewById(R.id.list_bg_iv);

        String tag=getIntent().getStringExtra("tag");
        if (tag.equals("multi")){
            wxtalk_preview_friend_tag.setImageResource(R.mipmap.wechat_qunliao);
        }else if(tag.equals("single")){
            wxtalk_preview_friend_tag.setImageResource(R.mipmap.ic_toolbar_user_24dp);
        }
        String titleName=getIntent().getStringExtra("herselfname");
        String path=getIntent().getStringExtra("bbitmap");
        boolean isSelectBg=getIntent().getBooleanExtra("isSelectBg",false);
        if (isSelectBg){
            Picasso.with(this).load(new File(path)).fit().into(list_bg_iv);
            //add_talk_preview_list.setBackgroundDrawable(new BitmapDrawable(compressImage(BitmapFactory.decodeFile(path),100,50)));
        }else{
            list_bg_iv.setBackgroundColor(getResources().getColor(R.color.ashbg));
        }


        list= (List<HashMap<String, Object>>) getIntent().getSerializableExtra("list");
        WxTalkPreViewAdapter adapter=new WxTalkPreViewAdapter(this,list);
        add_talk_preview_list.setAdapter(adapter);

        add_talk_preview_title_name.setText(titleName);

        add_talk_preview_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Bitmap compressImage(Bitmap image,int size,int options) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > size) {
            options -= 10;// 每次都减少10
            baos.reset();// 重置baos即清空baos
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }
}
