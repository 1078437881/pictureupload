package com.wyb.common.log;

import android.util.Log;

public class MyLog {

    public static final String LOGTAG = "wybtest";

    public static void e(String msg){
        Log.e(LOGTAG,msg);
    }

    public static void d(String msg){
        Log.d(LOGTAG,msg);
    }
    public static void i(String msg){
        Log.i(LOGTAG,msg);
    }
    public static void v(String msg){
        Log.v(LOGTAG,msg);
    }
}
