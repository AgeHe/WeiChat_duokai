package com.zhushou.weichat.abs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/11/17.
 */

public class TuiTaskA extends AsyncTask<String, Integer, String> {

    private Context d;
    private PowerManager.WakeLock e;
    ProgressDialog a;
    String b = "file.apk";
    String c = "/sdcard/";

    public TuiTaskA(Context var1, ProgressDialog var2) {
        this.d = var1;
        this.a = var2;
    }

    protected String doInBackground(String... var1) {
        InputStream var2 = null;
        FileOutputStream var3 = null;
        HttpURLConnection var4 = null;

        try {
            String var6;
            try {
                URL var5 = new URL(var1[0]);
                var4 = (HttpURLConnection)var5.openConnection();
                var4.connect();
                if(var4.getResponseCode() != 200) {
                    var6 = "Server returned HTTP " + var4.getResponseCode() + " " + var4.getResponseMessage();
                    return var6;
                } else {
                    int var17 = var4.getContentLength();
                    var2 = var4.getInputStream();
                    var3 = new FileOutputStream(this.c + this.b);
                    byte[] var7 = new byte[8192];

                    int var10;
                    for(long var8 = 0L; (var10 = var2.read(var7)) != -1; var3.write(var7, 0, var10)) {
                        if(this.isCancelled()) {
                            var2.close();
                            Object var11 = null;
                            return (String)var11;
                        }

                        var8 += (long)var10;
                        if(var17 > 0) {
                            this.publishProgress(new Integer[]{Integer.valueOf((int)(var8 * 100L / (long)var17))});
                        }
                    }

                    if(var3 != null) {
                        var3.close();
                    }

                    if(var2 != null) {
                        var2.close();
                    }

                    this.a(new File(this.c + this.b));
                    return null;
                }
            } catch (Exception var15) {
                var6 = var15.toString();
                return var6;
            }
        } finally {
            if(var4 != null) {
                var4.disconnect();
            }

        }
    }

    protected void onPreExecute() {
        super.onPreExecute();
        PowerManager var1 = (PowerManager)this.d.getSystemService(Context.POWER_SERVICE);
        this.e = var1.newWakeLock(1, this.getClass().getName());
        this.e.acquire();
        this.a.show();
    }

    protected void a(Integer... var1) {
        super.onProgressUpdate(var1);
        this.a.setIndeterminate(false);
        this.a.setMax(100);
        this.a.setProgress(var1[0].intValue());
    }

    protected void a(String var1) {
        this.e.release();
        this.a.dismiss();
    }

    private void a(File var1) {
        Intent var2 = new Intent();
        var2.addFlags(268435456);
        var2.setAction("android.intent.action.VIEW");
        String var3 = "application/vnd.android.package-archive";
        var2.setDataAndType(Uri.fromFile(var1), var3);
        this.d.startActivity(var2);
    }

}
