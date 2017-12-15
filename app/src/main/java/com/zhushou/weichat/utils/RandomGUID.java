package com.zhushou.weichat.utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by Administrator on 2017/9/29.
 */

public class RandomGUID{


    private  final String TAG = "RandomGUID";
    public String valueBeforeMD5 = "";
    public String valueAfterMD5 = "";
    private  Random myRand;
    private  SecureRandom mySecureRand;

    private  String s_id;
    private  final int PAD_BELOW = 0x10;
    private  final int TWO_BYTES = 0xFF;

   /*
    * Static block to take care of one time secureRandom seed.
    * It takes a few seconds to initialize SecureRandom.  You might
    * want to consider removing this static block or replacing
    * it with a "time since first loaded" seed to reduce this time.
    * This block will run only once per JVM instance.
      */



    /*
     * Default constructor.  With no specification of security option,
     * this constructor defaults to lower security, high performance.
     */
    public RandomGUID(int want,Handler mHandler) {
//        mySecureRand = new SecureRandom();
//        long secureInitializer = mySecureRand.nextLong();
//        myRand = new Random(secureInitializer);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    s_id = InetAddress.getLocalHost().toString();
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                mHandler.sendEmptyMessage(want);
//            }
//        }).start();
//        getRandomGUID(false);
    }

    /*
     * Constructor with security option.  Setting secure true
     * enables each random number generated to be cryptographically
     * strong.  Secure false defaults to the standard Random function seeded
     * with a single cryptographically strong random number.
     */
    public RandomGUID(boolean secure) {
        getRandomGUID(secure);
    }

    /*
     * Method to generate the random GUID
     */
    private void getRandomGUID(boolean secure) {
        MessageDigest md5 = null;
        StringBuffer sbValueBeforeMD5 = new StringBuffer(128);

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "getRandomGUID Error: "+e.getMessage());
        }

        try {
            long time = System.currentTimeMillis();
            long rand = 0;

            if (secure) {
                rand = mySecureRand.nextLong();
            } else {
                rand = myRand.nextLong();
            }
            sbValueBeforeMD5.append(s_id);
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(Long.toString(time));
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(Long.toString(rand));

            valueBeforeMD5 = sbValueBeforeMD5.toString();
            md5.update(valueBeforeMD5.getBytes());

            byte[] array = md5.digest();
            StringBuffer sb = new StringBuffer(32);
            for (int j = 0; j < array.length; ++j) {
                int b = array[j] & TWO_BYTES;
                if (b < PAD_BELOW)
                    sb.append('0');
                sb.append(Integer.toHexString(b));
            }
            valueAfterMD5 = sb.toString();
        } catch (Exception e) {
            Log.e(TAG,"getRandomGUID Error:" + e.getMessage());
        }
    }

    /*
     * Convert to the standard format for GUID
     * (Useful for SQL Server UniqueIdentifiers, etc.)
     * Example: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
     */
    public String toString() {
        String raw = valueAfterMD5.toUpperCase();
        StringBuffer sb = new StringBuffer(64);
        sb.append(raw.substring(0, 8));
        sb.append("-");
        sb.append(raw.substring(8, 12));
        sb.append("-");
        sb.append(raw.substring(12, 16));
        sb.append("-");
        sb.append(raw.substring(16, 20));
        sb.append("-");
        sb.append(raw.substring(20));

        return sb.toString();
    }


    public static final String LOCAL_GUID_SP = "local.guid.sp";
    public  String getLocalGUID(Context context){
        String returnGUID = "";
        String localGUIDStr = (String) SharedPreferencesUtils.getParam(context,LOCAL_GUID_SP,"");
        if (localGUIDStr!=null&&!localGUIDStr.equals("")){
            returnGUID = localGUIDStr;
        }else{
//            RandomGUID myGUID = new RandomGUID();
            returnGUID = java.util.UUID.randomUUID().toString();
            SharedPreferencesUtils.setParam(context,LOCAL_GUID_SP,returnGUID);
        }
        Log.e(TAG, "getLocalGUID: "+returnGUID);
        return returnGUID;
    }
    // Demonstraton and self test of class
//    public static void main(String args[]) {
//        for (int i=0; i< 100; i++) {
//            RandomGUID myGUID = new RandomGUID();
//            System.out.println("Seeding String=" + myGUID.valueBeforeMD5);
//            System.out.println("rawGUID=" + myGUID.valueAfterMD5);
//            System.out.println("RandomGUID=" + myGUID.toString());
//        }
//    }

//    public String getIdentity() {
//        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
//        String identity = preference.getString("identity", null);
//        if (identity == null) {
//            identity = java.util.UUID.randomUUID().toString();
//            preference.edit().putString("identity", identity);
//        }
//        return identity;
//    }

}
