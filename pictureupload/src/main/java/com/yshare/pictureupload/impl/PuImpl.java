package com.yshare.pictureupload.impl;

import android.app.Activity;

import com.yshare.pictureupload.constants.ResId;
import com.yshare.pictureupload.interfaces.OnGetHeadListener;

public class PuImpl {

    public static Activity mActivity = null;
    public static int x;
    public static int y;
    public static OnGetHeadListener headListener;

    public static void init(Activity activity, OnGetHeadListener onGetHeadListener){
        mActivity = activity;
        headListener = onGetHeadListener;
        ResId.init(activity);
    }
}
