package com.wyb.pictureupload.impl;

import android.app.Activity;

import com.wyb.pictureupload.constants.ResId;
import com.wyb.pictureupload.interfaces.OnGetHeadListener;

public class PuImpl {

    public static Activity mActivity = null;

    public static OnGetHeadListener headListener;

    public static void init(Activity activity, OnGetHeadListener onGetHeadListener){
        mActivity = activity;
        headListener = onGetHeadListener;
        ResId.init(activity);
    }
}
