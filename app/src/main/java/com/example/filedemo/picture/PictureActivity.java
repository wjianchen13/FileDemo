package com.example.filedemo.picture;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filedemo.R;

import java.io.OutputStream;

/**
 * andorid 11 相册操作
 */
public class PictureActivity extends AppCompatActivity {

    private static final String TAG = PictureActivity.class.getSimpleName();
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
    }

    /**
     * 获取相册图片
     * @param v
     */
    public void onGet(View v) {
        startActivity(new Intent(this, BrowseAlbumActivity.class));
    }

    /**
     * 保存图标到相册中
     * @param v
     */
    public void onSave1(View v) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        String displayName = System.currentTimeMillis() + ".jpg";
        String mimeType = "image/jpeg";
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
        addBitmapToAlbum(bitmap, displayName, mimeType, compressFormat);
    }

    private void addBitmapToAlbum(Bitmap bitmap, String displayName, String mimeType, Bitmap.CompressFormat compressFormat) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
        } else {
            values.put(MediaStore.MediaColumns.DATA, Environment.getExternalStorageDirectory().getPath() + "/" + Environment.DIRECTORY_DCIM + "/" + displayName);
        }
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (uri != null) {
            try {
                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                if (outputStream != null) {
                    bitmap.compress(compressFormat, 100, outputStream);
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 
     * @param v
     */
    public void onSave2(View v) {
    }



}