package com.yshare.common.utils;

import android.os.Build;

public class AndroidVersionUtils {

    public static boolean isToAndroidQ() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    public static boolean isToAndroidN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }
}
