package com.yshare.pictureupload.constants;

import android.app.Activity;

import com.yshare.common.view.idconfig.ResIdUtils;

public class ResId {

    private static Activity mActivity;
    public static void  init(Activity activity){
        mActivity = activity;
    }


    public static final class layout {
        public static int activity_clip_image = ResIdUtils.getResId(mActivity,"activity_clip_image","layout");
        public static int buttom_dialog_view= ResIdUtils.getResId(mActivity,"buttom_dialog_view","layout");
    }

    public static final class id {
        public static int photograph_tv= ResIdUtils.getResId(mActivity,"photograph_tv","id");
        public static int album_tv= ResIdUtils.getResId(mActivity,"album_tv","id");
        public static int cancel_tv= ResIdUtils.getResId(mActivity,"cancel_tv","id");

        public static int confirm_tv= ResIdUtils.getResId(mActivity,"confirm_tv","id");
        public static int clip_image_layout= ResIdUtils.getResId(mActivity,"clip_image_layout","id");
        public static int rotate_photo_tv= ResIdUtils.getResId(mActivity,"rotate_photo_tv","id");
    }

    public static final class style {
        public static int BottomDialogStyle= ResIdUtils.getResId(mActivity,"BottomDialogStyle","style");
    }

}
