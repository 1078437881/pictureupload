package com.yshare.pictureupload.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yshare.common.view.dialog.BottomDialog;
import com.yshare.pictureupload.activity.PhotoActivity;
import com.yshare.pictureupload.constants.Constants;
import com.yshare.pictureupload.constants.ResId;
import com.yshare.pictureupload.impl.PuImpl;

public class BottomButtonDialog extends BottomDialog implements View.OnClickListener {

    Context mContext;
    TextView photographTv, albumTv, canncelTv;
    View imageView;
    public BottomButtonDialog(@NonNull Context context,View view) {
        super(context, ResId.layout.buttom_dialog_view, ResId.style.BottomDialogStyle);
        mContext = context;
        imageView = view;
        PuImpl.x = imageView.getWidth();
        PuImpl.y = imageView.getHeight();

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
