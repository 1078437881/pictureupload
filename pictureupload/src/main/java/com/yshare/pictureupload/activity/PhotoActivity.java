package com.yshare.pictureupload.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import com.yshare.common.log.MyLog;
import com.yshare.common.utils.AndroidVersionUtils;
import com.yshare.common.utils.PhotoAlbumUtils;
import com.yshare.pictureupload.constants.Constants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhotoActivity extends Activity {

    private final static int PERMISSION_CAMERA_REQUEST_CODE = 0x20000001;
    private final static int PERMISSION_ALBUM_REQUEST_CODE = 0x20000002;

    private final static int CAMERA_REQUEST_CODE = 0x20000001;
    private final static int ALBUM_REQUEST_CODE = 0x20000002;
    //用于保存拍照图片的uri
    private Uri mCameraUri;
    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.d("PhotoActivity.onCreate");
        Intent intent = getIntent();
        int enterType = intent.getIntExtra(Constants.ENTER_PHOTO_TYPE, 0);
        if (enterType == Constants.ENTER_PHOTHGRAPH) {
            MyLog.d("checkPermissionAndCamera");
            checkPermissionAndCamera();

        }
        if (enterType == Constants.ENTER_ALBUM) {
            MyLog.d("checkPermissionAndAlbum");
            checkPermissionAndAlbum();

        }
    }

    private void checkPermissionAndCamera() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            //有调起相机拍照。

            openCamera();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    PERMISSION_CAMERA_REQUEST_CODE);
        }
    }

    private void checkPermissionAndAlbum() {
        int hasAlbumPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasAlbumPermission == PackageManager.PERMISSION_GRANTED) {
            openAlbum();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_ALBUM_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == PERMISSION_ALBUM_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum();
            } else {
                Toast.makeText(this, "存储权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = new Bundle();
                if (AndroidVersionUtils.isToAndroidQ()) {
                    bundle.putParcelable(Constants.HEAD_IMAGE_URI_KEY, mCameraUri);
                } else {
                    bundle.putString(Constants.HEAD_IMAGE_PATH_KEY, mCameraImagePath);
                }
                Intent intent = new Intent(this, ClipImageActivity.class);
                intent.putExtra(Constants.BUNDLE_MESSAGE_KEY, bundle);
                startActivity(intent);
                PhotoActivity.this.finish();
            } else {
                Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
                PhotoActivity.this.finish();
            }
        }

        if (requestCode == ALBUM_REQUEST_CODE) {
            // 获取图片
            if (resultCode == RESULT_OK) {
                Uri CameraUri = data.getData();
                if(CameraUri!=null){
                    mCameraUri = CameraUri;
                    Bundle bundle = new Bundle();
                    if (AndroidVersionUtils.isToAndroidQ()) {
                        bundle.putParcelable(Constants.HEAD_IMAGE_URI_KEY, mCameraUri);
                    } else {
                        mCameraImagePath = PhotoAlbumUtils.getRealPathFromUri(this,mCameraUri);
                        bundle.putString(Constants.HEAD_IMAGE_PATH_KEY, mCameraImagePath);
                    }
                    Intent intent = new Intent(this, ClipImageActivity.class);
                    intent.putExtra(Constants.BUNDLE_MESSAGE_KEY, bundle);
                    startActivity(intent);
                }
                PhotoActivity.this.finish();
            } else {
                Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
                PhotoActivity.this.finish();
            }

        }
    }

    /**
     * 调起相机
     */
    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = null;

            if (AndroidVersionUtils.isToAndroidQ()) {
                // 适配android 10
                photoUri = createImageUri();
            } else {
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photoFile != null) {
                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (AndroidVersionUtils.isToAndroidN()) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                    } else {
                        photoUri = Uri.fromFile(photoFile);
                    }
                }
            }

            mCameraUri = photoUri;
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/jpeg");
        startActivityForResult(intentToPickPic, ALBUM_REQUEST_CODE);
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 创建保存图片的文件
     */
    private File createImageFile() throws IOException {
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }
}
