package com.yshare.common.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class BottomDialog extends Dialog {


    private boolean isCanCelable;
    private View layoutView;
    private int layoutResId;

    public BottomDialog(@NonNull Context context, View view, int styleResId) {
        super(context,styleResId);
        layoutView = view;
    }

    public BottomDialog(@NonNull Context context, int resId, int styleResId) {
        super(context,styleResId);
        layoutResId = resId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (layoutView != null) {
            setContentView(layoutView);
        }
        if (layoutResId != 0) {
            setContentView(layoutResId);
        }//这行一定要写在前面
        setCancelable(isCanCelable);//点击外部不可dismiss
        setCanceledOnTouchOutside(isCanCelable);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    public void isCanCelable(boolean isCanCelable) {
        this.isCanCelable = isCanCelable;
    }
}
