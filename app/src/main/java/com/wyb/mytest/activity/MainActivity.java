package com.wyb.mytest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.wyb.common.log.MyLog;
import com.wyb.common.utils.AndroidVersionUtils;
import com.wyb.common.view.image.CircleImageView;
import com.wyb.mytest.R;
import com.wyb.pictureupload.constants.Constants;
import com.wyb.pictureupload.impl.PuImpl;
import com.wyb.pictureupload.interfaces.OnGetHeadListener;
import com.wyb.pictureupload.view.BottomButtonDialog;

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
            }
        });
        getIntentMsg();
        userHead = findViewById(R.id.user_head);
        userHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomButtonDialog bottomButtonDialogView = new BottomButtonDialog(MainActivity.this);
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
