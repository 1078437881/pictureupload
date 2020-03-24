package com.wyb.pictureupload.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wyb.common.view.dialog.BottomDialog;
import com.wyb.pictureupload.activity.PhotoActivity;
import com.wyb.pictureupload.constants.Constants;
import com.wyb.pictureupload.constants.ResId;

public class BottomButtonDialog extends BottomDialog implements View.OnClickListener {

    Context mContext;
    TextView photographTv, albumTv, canncelTv;

    public BottomButtonDialog(@NonNull Context context) {
        super(context, ResId.layout.buttom_dialog_view, ResId.style.BottomDialogStyle);
        mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();

    }

    protected void initViews() {
        photographTv = findViewById(ResId.id.photograph_tv);
        albumTv = findViewById(ResId.id.album_tv);
        canncelTv = findViewById(ResId.id.cancel_tv);
        photographTv.setOnClickListener(this);
        albumTv.setOnClickListener(this);
        canncelTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == ResId.id.photograph_tv) {
            startPhotoActivity(Constants.ENTER_PHOTHGRAPH);
            dismiss();
        } else if (id == ResId.id.album_tv) {
            startPhotoActivity(Constants.ENTER_ALBUM);
            dismiss();
        } else if (id == ResId.id.cancel_tv) {
            dismiss();
        }
    }

    void startPhotoActivity(int type){
        Intent intent = new Intent(mContext, PhotoActivity.class);
        intent.putExtra(Constants.ENTER_PHOTO_TYPE,type);
        mContext.startActivity(intent);
    }

}
