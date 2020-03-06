package com.wyb.pictureupload.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.wyb.common.log.MyLog;
import com.wyb.common.utils.AndroidVersionUtils;
import com.wyb.common.view.image.ClipImageLayout;
import com.wyb.pictureupload.R;
import com.wyb.pictureupload.constants.Constants;
import com.wyb.pictureupload.constants.ResId;
import com.wyb.pictureupload.impl.PuImpl;

import java.io.FileNotFoundException;

public class ClipImageActivity extends Activity implements View.OnClickListener {

    //用于保存拍照图片的uri
    private Uri mCameraUri;
    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;

    private Bitmap mCameraBitmap;

    private ClipImageLayout clipImageLayout;
    private TextView canncelTv, confirmTv, rotatePhotoTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResId.layout.activity_clip_image);
        MyLog.d("ClipImageActivity.onCreate");
        initViews();
        mCameraBitmap = getIntentMsg();
        if (mCameraBitmap != null) {
            clipImageLayout.setImageBitmap(mCameraBitmap);
        } else {
            finish();
        }
    }

    private void initViews() {
        clipImageLayout = findViewById(ResId.id.clip_image_layout);
        canncelTv = findViewById(ResId.id.cancel_tv);
        confirmTv = findViewById(ResId.id.confirm_tv);
        rotatePhotoTv = findViewById(ResId.id.rotate_photo_tv);

        clipImageLayout.setOnClickListener(this);
        canncelTv.setOnClickListener(this);
        confirmTv.setOnClickListener(this);
        rotatePhotoTv.setOnClickListener(this);
    }

    private Bitmap getIntentMsg() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.BUNDLE_MESSAGE_KEY);
        if (bundle != null) {
            if (AndroidVersionUtils.isToAndroidQ()) {
                mCameraUri = bundle.getParcelable(Constants.HEAD_IMAGE_URI_KEY);
                if (mCameraUri != null) {
                    try {
                        return BitmapFactory.decodeStream(getContentResolver().openInputStream(mCameraUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                mCameraImagePath = bundle.getString(Constants.HEAD_IMAGE_PATH_KEY);
                if (mCameraImagePath != null) {
                    return BitmapFactory.decodeFile(mCameraImagePath);
                }
            }
        }
        return null;
    }


    /**
     * // 旋转图片
     *
     * @param angle  角度
     * @param bitmap
     * @return
     */
    private Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return resizedBitmap;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == ResId.id.confirm_tv) {
            Bitmap bitmap = clipImageLayout.clip();
            PuImpl.headListener.onBack(bitmap);
        } else if (id == ResId.id.rotate_photo_tv) {
            mCameraBitmap = rotateBitmap(90, mCameraBitmap);
            clipImageLayout.setImageBitmap(mCameraBitmap);
        } else if (id == ResId.id.cancel_tv) {
            finish();
        }
    }
}