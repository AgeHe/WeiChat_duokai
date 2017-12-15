package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;
import com.zhushou.weichat.screenshot.view.XCRoundRectImageView;

/**
 * Created by zhanglinkai on 2017/4/5.
 * 功能:
 */

public class WxVoicePreview extends Activity {

    private ImageView wx_voice_preview_s;
    private ImageView voice_call_btn_not;
    private ImageView voice_call_btn_yes;
    private TextView wx_voice_calling_time_tv;
    private ImageView voice_preview_calling_btn_transfervoice;
    private ImageView voice_preview_calling_btn_over;
    private ImageView voice_preview_calling_btn_transfercamera;
    private LinearLayout voice_preview_calling_layout;
    private RelativeLayout voice_preview_nocall_btn_layout;
    private XCRoundRectImageView wx_voice_preview_icon;
    private TextView wx_voice_preview_name;
    private RelativeLayout wx_voice_preview_bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wx_voice_preview);
        iniUI();
    }

    private void iniUI() {
        float index=getSharedPreferences("index", Context.MODE_PRIVATE).getFloat("index",1);
        wx_voice_preview_s=(ImageView)findViewById(R.id.wx_voice_preview_s);
        voice_call_btn_not=(ImageView)findViewById(R.id.voice_call_btn_not);
        voice_call_btn_yes=(ImageView)findViewById(R.id.voice_call_btn_yes);
        wx_voice_calling_time_tv=(TextView)findViewById(R.id.wx_voice_calling_time_tv);
        voice_preview_calling_btn_transfervoice=(ImageView)findViewById(R.id.voice_preview_calling_btn_transfervoice);
        voice_preview_calling_btn_over=(ImageView)findViewById(R.id.voice_preview_calling_btn_over);
        voice_preview_calling_btn_transfercamera=(ImageView)findViewById(R.id.voice_preview_calling_btn_transfercamera);
        voice_preview_calling_layout=(LinearLayout)findViewById(R.id.voice_preview_calling_layout);
        voice_preview_nocall_btn_layout=(RelativeLayout)findViewById(R.id.voice_preview_nocall_btn_layout);
        wx_voice_preview_icon=(XCRoundRectImageView)findViewById(R.id.wx_voice_preview_icon);
        wx_voice_preview_name=(TextView)findViewById(R.id.wx_voice_preview_name);
        wx_voice_preview_bg=(RelativeLayout)findViewById(R.id.wx_voice_preview_bg);

        StaticMethod.adaptiveView(wx_voice_preview_s,105,75,index);
        StaticMethod.adaptiveView(voice_call_btn_not,200,200,index);
        StaticMethod.adaptiveView(voice_call_btn_yes,200,200,index);
        StaticMethod.adaptiveView(voice_preview_calling_btn_transfervoice,200,200,index);
        StaticMethod.adaptiveView(voice_preview_calling_btn_over,200,200,index);
        StaticMethod.adaptiveView(voice_preview_calling_btn_transfercamera,200,200,index);


        String type=getIntent().getStringExtra("type");
        String name=getIntent().getStringExtra("name");
        String image=getIntent().getStringExtra("image");
        String time=getIntent().getStringExtra("time");

        wx_voice_preview_name.setText(name);
        Picasso.with(this).load(new File(image)).resize(100,100).into(wx_voice_preview_icon);

        if (type.equals("nocall")){
            voice_preview_nocall_btn_layout.setVisibility(View.VISIBLE);
            voice_preview_calling_layout.setVisibility(View.GONE);
        }else if (type.equals("calling")){
            voice_preview_nocall_btn_layout.setVisibility(View.GONE);
            voice_preview_calling_layout.setVisibility(View.VISIBLE);
            wx_voice_calling_time_tv.setText(time);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wx_voice_preview_bg.setBackgroundDrawable(new BitmapDrawable(blurBitmap(BitmapFactory.decodeFile(image))));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Bitmap blurBitmap(Bitmap bitmap){

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(getApplicationContext());

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(25.f);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;


    }

}
