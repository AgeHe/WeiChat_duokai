package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2017/4/17.
 * 功能:
 */

public class QQMOneyPacket extends Activity implements View.OnClickListener{
    private ImageView qq_moneypacket_title_iv;
    private EditText qq_moneypacket_qb_et;
    private EditText qq_moneypacket_money_et;
    private RelativeLayout qq_moneypacket_selectbank_layout;
    private TextView qq_moneypacket_selectbank_tv;
    private RelativeLayout qq_moneypacket_cardtype_layout;
    private TextView qq_moneypacket_cardtype_tv;
    private EditText qq_moneypacket_cardnumber_tv;
    private Button qq_moneypacket_btn_preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_qq_moneypacket);
        iniUI();
    }

    private void iniUI() {
        qq_moneypacket_title_iv=(ImageView)findViewById(R.id.qq_moneypacket_title_iv);
        qq_moneypacket_qb_et=(EditText)findViewById(R.id.qq_moneypacket_qb_et);
        qq_moneypacket_money_et=(EditText)findViewById(R.id.qq_moneypacket_money_et);
        qq_moneypacket_selectbank_layout=(RelativeLayout)findViewById(R.id.qq_moneypacket_selectbank_layout);
        qq_moneypacket_selectbank_tv=(TextView)findViewById(R.id.qq_moneypacket_selectbank_tv);
        qq_moneypacket_cardtype_layout=(RelativeLayout)findViewById(R.id.qq_moneypacket_cardtype_layout);
        qq_moneypacket_cardtype_tv=(TextView)findViewById(R.id.qq_moneypacket_cardtype_tv);
        qq_moneypacket_cardnumber_tv=(EditText)findViewById(R.id.qq_moneypacket_cardnumber_tv);
        qq_moneypacket_btn_preview=(Button)findViewById(R.id.qq_moneypacket_btn_preview);

        qq_moneypacket_title_iv.setOnClickListener(this);
        qq_moneypacket_selectbank_layout.setOnClickListener(this);
        qq_moneypacket_cardtype_layout.setOnClickListener(this);
        qq_moneypacket_btn_preview.setOnClickListener(this);
    }
    private boolean isSelect=false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qq_moneypacket_title_iv:
                finish();
                break;
            case R.id.qq_moneypacket_selectbank_layout:
                selectBank();
                break;
            case R.id.qq_moneypacket_cardtype_layout:
                if (isSelect){
                    isSelect=false;
                    qq_moneypacket_cardtype_tv.setText(getString(R.string.cxk));
                }else{
                    isSelect=true;
                    qq_moneypacket_cardtype_tv.setText(getString(R.string.xyk));
                }


                break;
            case R.id.qq_moneypacket_btn_preview:
                String qb=qq_moneypacket_qb_et.getText().toString().trim();
                String money=qq_moneypacket_money_et.getText().toString().trim();
                String card=qq_moneypacket_selectbank_tv.getText().toString().trim();
                String cardtype=qq_moneypacket_cardtype_tv.getText().toString().trim();
                String cn=qq_moneypacket_cardnumber_tv.getText().toString().trim();
                if (!qb.isEmpty()&&!money.isEmpty()&&!card.isEmpty()&&!cardtype.isEmpty()&&!cn.isEmpty()){

                    Intent intent=new Intent(this,QQMOneyPacketPreview.class);
                    intent.putExtra("qb",qb);
                    intent.putExtra("money",money);
                    intent.putExtra("card",card);
                    intent.putExtra("cardtype",cardtype);
                    intent.putExtra("cn",cn);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, getString(R.string.qqbalance_toast), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    private void selectBank(){
        final String[] s={getString(R.string.gsyh),getString(R.string.yzcx),getString(R.string.pfyh),getString(R.string.xys),
                getString(R.string.hxyh),
                getString(R.string.nyyh),getString(R.string.zgyh),getString(R.string.jsyh),getString(R.string.zsyh),
                getString(R.string.msyh),getString(R.string.jtyh)};
        String s1=qq_moneypacket_selectbank_tv.getText().toString().trim();
        int m=0;
        if (!s1.isEmpty()){
            for (int i=0;i<s.length;i++){
                if (s1.equals(s[i])){
                    m=i;
                    break;
                }
            }
        }
        final AlertDialog dialog=new AlertDialog.Builder(this).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        View view= LayoutInflater.from(this).inflate(R.layout.layout_select_bank,null);
        dialog.setView(view);
        TextView selctbank_dialog_cancle=(TextView)view.findViewById(R.id.selctbank_dialog_cancle);
        TextView selctbank_dialog_sure=(TextView)view.findViewById(R.id.selctbank_dialog_sure);
        selctbank_dialog_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final NumberPicker picker_bank=(NumberPicker)view.findViewById(R.id.picker_bank);
        picker_bank.setDisplayedValues(s);
        picker_bank.setMinValue(0);
        picker_bank.setValue(m);
        picker_bank.setMaxValue(s.length-1);
        selctbank_dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qq_moneypacket_selectbank_tv.setText(s[picker_bank.getValue()]);
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
