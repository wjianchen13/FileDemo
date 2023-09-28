package com.example.filedemo.compat;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.filedemo.R;
import com.example.filedemo.Utils;
import com.example.filedemo.camera.CameraUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 从相册选取一张图片，分别存储到内部存储和外部存储中
 */
public class CompatActivity extends AppCompatActivity {

    private static final String TAG = CompatActivity.class.getSimpleName();

    private Uri uri;
    private ImageView ivPicture;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compat);
        ivPicture = findViewById(R.id.iv_picture);
    }

    public void onPicture(View v) {
        //相册
        if (CameraUtils.checkSelectPhotoPermission(this)) {//检查权限
            //有权限，打开相册
            uri = null;
            CameraUtils.openAlbum(this);
        } else {
            //无权限，申请
            CameraUtils.requestSelectPhotoPermissions(this);
        }
    }

    /**
     * 保存到内部存储
     * /data/user/0/com.example.filedemo/files
     * @param v
     */
    public void onSave1(View v) {
        String savePath =  getFilesDir() + "/test1.JPEG";
        saveBitmap(mBitmap, savePath);
    }
    
    /**
     * 保存到外部存储
     * /storage/emulated/0/Android/data/com.example.filedemo/files
     * @param v
     */
    public void onSave2(View v) {
        String savePath =  getExternalFilesDir(null) + "/test2.JPEG";
        saveBitmap(mBitmap, savePath);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //activity跳转回调
        if (uri != null) {
            Log.e(TAG, "activity回调，uri：" + uri);
        }
        if (data != null) {
            Log.e(TAG, "activity回调，data：" + data);
        }
        if (requestCode == CameraUtils.CAMERA_SELECT_PHOTO) {
            try {
                if (data != null) {
                    uri = data.getData();
                }
                if(uri != null) {
                    ContentResolver contentResolver = getContentResolver();//内容解析器
                    ParcelFileDescriptor fd = contentResolver.openFileDescriptor(uri, "r");
                    if (fd != null) {
                        mBitmap = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor());
                        fd.close();
                        ivPicture.setImageBitmap(mBitmap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveBitmap(Bitmap bitmap, String savePath) {
        File filePic;
        try {
            filePic = new File(savePath);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Utils.log("saveBitmap: " + e.getMessage());
            return;
        }
        Utils.log( "saveBitmap success: " + filePic.getAbsolutePath());
    }




}



