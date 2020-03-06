package com.wyb.common.view.idconfig;

import android.content.Context;

public class ResIdUtils {

    public static int getResId(Context context,String idNmae,String type){
       return context.getResources().getIdentifier(idNmae,type,context.getPackageName());
    }

}
