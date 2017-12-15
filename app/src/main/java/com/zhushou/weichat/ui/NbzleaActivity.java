package com.zhushou.weichat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.zhushou.weichat.R;
import com.zhushou.weichat.utils.HistoryPayShareUtil;


public class NbzleaActivity extends Activity implements View.OnClickListener {
//    private File file = new File(FileUtil.getRootPath() + "/"+ JApp.SCAN_PNG_NAME);
    private String codeUrl ;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_hint);
//        overridePendingTransition(R.anim.zoom_in, R.anim.no_change);
        findViewById(R.id.pay_btn).setOnClickListener(this);
        findViewById(R.id.over_btn).setOnClickListener(this);
        Intent tempIntent = getIntent();
        if(tempIntent!=null){
            Bundle bundle =tempIntent.getExtras();
            if(bundle!=null){
                codeUrl =bundle.getString("codeUrl");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.no_change, R.anim.zoom_out);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pay_btn) {
            if (runnable == null) {
                if (codeUrl!=null&&!codeUrl.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.AddFavoriteUI");
                    intent.putExtra(Intent.EXTRA_TEXT, "更新时间：" + HistoryPayShareUtil.getSystemTime("yyyy/MM/dd HH:mm:ss") +
                            "\n【" + getString(R.string.app_name) + "】点击链接即可支付\n\t" + codeUrl);
                    startActivity(intent);
                }

                runnable = new Runnable() {
                    @Override
                    public void run() {
//                        "com.tencent.mm.action.BIZSHORTCUT"
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.setClassName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        if (codeUrl!=null&&!codeUrl.equals("")) intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
                        startActivity(intent);
                    }
                };
            }
              handler.sendEmptyMessageDelayed(0,500);



//            Intent intent = new Intent("com.tencent.mm.action.BIZSHORTCUT");
//            intent.setAction(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            intent.setClassName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
//            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            if (!Config.getInstance(this).isSdcard())
//                Toast.makeText(this, "必须授权同意访问设备上的照片权限才可以支付！", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.over_btn) finish();
    }


    Handler handler =  new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(runnable!=null){
                runnable.run();
            }
        }
    };
}
