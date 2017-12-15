package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2017/4/17.
 * 功能:
 */

public class QQMOneyPacketPreview extends Activity {
    private ImageView qq_moneypacket_preview_title_iv;
    private TextView qq_moneypacket_preview_monet_tv;
    private TextView qq_moneypacket_preview_qb_tv;
    private ImageView qq_moneypacket_preview_bankicon;
    private TextView qq_moneypacket_preview_bank_tv;
    private TextView qq_moneypacket_preview_bankcontext_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_qq_moneypacketpreview);
        iniUI();
    }

    private void iniUI() {
        qq_moneypacket_preview_title_iv=(ImageView)findViewById(R.id.qq_moneypacket_preview_title_iv);
        qq_moneypacket_preview_title_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        qq_moneypacket_preview_monet_tv=(TextView)findViewById(R.id.qq_moneypacket_preview_monet_tv);
        qq_moneypacket_preview_qb_tv=(TextView)findViewById(R.id.qq_moneypacket_preview_qb_tv);
        qq_moneypacket_preview_bankicon=(ImageView)findViewById(R.id.qq_moneypacket_preview_bankicon);
        qq_moneypacket_preview_bank_tv=(TextView)findViewById(R.id.qq_moneypacket_preview_bank_tv);
        qq_moneypacket_preview_bankcontext_tv=(TextView)findViewById(R.id.qq_moneypacket_preview_bankcontext_tv);


        String qb=getIntent().getStringExtra("qb");
        String money=getIntent().getStringExtra("money");
        String card=getIntent().getStringExtra("card");
        String cardtype=getIntent().getStringExtra("cardtype");
        String cn=getIntent().getStringExtra("cn");

        qq_moneypacket_preview_qb_tv.setText(qb);
        qq_moneypacket_preview_monet_tv.setText(money);
        qq_moneypacket_preview_bank_tv.setText(card);
        qq_moneypacket_preview_bankcontext_tv.setText(cardtype+"(****"+cn+")");
        adapterView(card);
    }
    private void adapterView(String bank){

        if (bank.equals(getResources().getString(R.string.gsyh))){
            qq_moneypacket_preview_bankicon.setImageResource(R.mipmap.bank_icbc);
            return;
        }
        if (bank.equals(getResources().getString(R.string.yzcx))){
            qq_moneypacket_preview_bankicon.setImageResource(R.mipmap.bank_psbc);
            return;
        }
        if (bank.equals(getResources().getString(R.string.pfyh))){
            qq_moneypacket_preview_bankicon.setImageResource(R.mipmap.bank_spdb);
            return;
        }
        if (bank.equals(getResources().getString(R.string.xys))){
            qq_moneypacket_preview_bankicon.setImageResource(R.mipmap.bank_ydrcb);
            return;
        }
        if (bank.equals(getResources().getString(R.string.hxyh))){
            qq_moneypacket_preview_bankicon.setImageResource(R.mipmap.bank_hxbank);
            return;
        }
        if (bank.equals(getResources().getString(R.string.nyyh))){
            qq_moneypacket_preview_bankicon.setImageResource(R.mipmap.bank_abc);
            return;
        }
        if (bank.equals(getResources().getString(R.string.zgyh))){
            qq_moneypacket_preview_bankicon.setImageResource(R.mipmap.bank_boc);
            return;
        }
        if (bank.equals(getResources().getString(R.string.jsyh))){
            qq_moneypacket_preview_bankicon.setImageResource(R.mipmap.bank_ccb);
            return;
        }
        if (bank.equals(getResources().getString(R.string.zsyh))){
            qq_moneypacket_preview_bankicon.setImageResource(R.mipmap.bank_cmb);
            return;
        }
        if (bank.equals(getResources().getString(R.string.msyh))){
            qq_moneypacket_preview_bankicon.setImageResource(R.mipmap.bank_cmbc);
            return;
        }
        if (bank.equals(getResources().getString(R.string.jtyh))){
            qq_moneypacket_preview_bankicon.setImageResource(R.mipmap.bank_comm);
            return;
        }
    }
}
