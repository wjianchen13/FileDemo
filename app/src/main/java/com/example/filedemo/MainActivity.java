package com.example.filedemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filedemo.base.BaseActivity;
import com.example.filedemo.base.BaseActivity1;
import com.example.filedemo.camera.CameraActivity;
import com.example.filedemo.compat.CompatActivity;
import com.example.filedemo.download.DownloadActivity;
import com.example.filedemo.picture.PictureActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1024;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void onBase(View v) {
        startActivity(new Intent(this, BaseActivity.class));
    }

    public void onBase1(View v) {
        startActivity(new Intent(this, BaseActivity1.class));
    }

    public void onCamera(View v) {
        startActivity(new Intent(this, CameraActivity.class));
    }

    public void onCompat(View v) {
        startActivity(new Intent(this, CompatActivity.class));
    }

    public void onPicture(View v) {
        startActivity(new Intent(this, PictureActivity.class));
    }

    public void onDownload(View v) {
        startActivity(new Intent(this, DownloadActivity.class));
    }
    
}