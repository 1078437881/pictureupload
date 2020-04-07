package com.yshare.mytest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.yshare.common.log.MyLog;
import com.yshare.common.utils.AndroidVersionUtils;
import com.yshare.common.utils.SharedPreferencesUtils;
import com.yshare.common.view.image.CircleImageView;
import com.yshare.mytest.R;
import com.yshare.pictureupload.constants.Constants;
import com.yshare.pictureupload.impl.PuImpl;
import com.yshare.pictureupload.interfaces.OnGetHeadListener;
import com.yshare.pictureupload.view.BottomButtonDialog;

import static com.yshare.mytest.commom.Constants.SHAREDPREFS_USER;
import static com.yshare.mytest.commom.Constants.SHAREDPREFS_USER_HEAD_IMAGE;

public class MainActivity extends AppCompatActivity {


    private CircleImageView userHead;

    //用于保存拍照图片的uri
    private Uri mCameraUri;
    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.d("MainActivity onCreate");
        setContentView(R.layout.activity_main);


        PuImpl.init(this, new OnGetHeadListener() {
            @Override
            public void onBack(Bitmap bitmap) {
                userHead.setImageBitmap(bitmap);
                SharedPreferencesUtils.saveBitmap(MainActivity.this, SHAREDPREFS_USER, bitmap, SHAREDPREFS_USER_HEAD_IMAGE);
            }
        });
        getIntentMsg();
        userHead = findViewById(R.id.user_head);
//        Drawable drawable= SharedPreferencesUtils.getDrawableByKey(this, SHAREDPREFS_USER, USER_AVATAR);
        //showImv.setBackgroundResource(R.mipmap.ic_launcher);
        Bitmap bitmap = SharedPreferencesUtils.getBitmapByKey(this, SHAREDPREFS_USER, SHAREDPREFS_USER_HEAD_IMAGE);
        MyLog.d("ReadShareperfs bitmap"+bitmap);
        if (bitmap != null) {
            userHead.setImageBitmap(bitmap);
        }

        userHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomButtonDialog bottomButtonDialogView = new BottomButtonDialog(MainActivity.this, userHead);
                bottomButtonDialogView.isCanCelable(true);
                bottomButtonDialogView.show();
            }
        });


    }

    private void getIntentMsg() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.BUNDLE_MESSAGE_KEY);
        if (bundle != null) {
            if (AndroidVersionUtils.isToAndroidQ()) {
                mCameraUri = bundle.getParcelable(Constants.HEAD_IMAGE_URI_KEY);
                if (mCameraUri != null) {
                    userHead.setImageURI(mCameraUri);
                }
            } else {
                mCameraImagePath = bundle.getString(Constants.HEAD_IMAGE_PATH_KEY);
                if (mCameraImagePath != null) {
                    userHead.setImageBitmap(BitmapFactory.decodeFile(mCameraImagePath));
                }
            }
        }
    }

}
