package com.example.filedemo.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.filedemo.R;
import com.example.filedemo.Utils;

import java.io.File;

public class BaseActivity1 extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base1);
    }

    /**
     * getFilesDir
     * /data/user/0/com.example.filedemo/files
     * /data/user/0/com.example.filedemo/files/myFile
     */
    public void onTest1(View v) {
        File fileDir = getFilesDir();
        Utils.log(fileDir.getAbsolutePath());
        File myFile = new File(fileDir, "myFile");
        Utils.log(myFile.getAbsolutePath());
    }

    /**
     * getCacheDir
     * /data/user/0/com.example.filedemo/cache
     */
    public void onTest2(View v) {
        File fileDir = getCacheDir();
        Utils.log(fileDir.getAbsolutePath());
    }

    /**
     * getDatabasePath
     * /data/user/0/com.example.filedemo/databases/myDB
     */
    public void onTest3(View v) {
        File fileDir = getDatabasePath("myDB");
        Utils.log(fileDir.getAbsolutePath());
    }

    /**
     * getDatabasePath
     * /data/user/0/com.example.filedemo/code_cache
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onTest4(View v) {
        File fileDir = getCodeCacheDir();
        Utils.log(fileDir.getAbsolutePath());
    }

    /**
     * getDatabasePath
     * /data/user/0/com.example.filedemo
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onTest5(View v) {
        File fileDir = getDataDir();
        Utils.log(fileDir.getAbsolutePath());
    }

    /**
     * 外部存储空间
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onTest6(View v) {
        testAppDir(this);
    }

    /**
     * /storage/emulated/0/Android/data/com.example.filedemo/files
     * /storage/emulated/0/Android/data/com.example.filedemo/cache
     * /storage/emulated/0/Android/data/com.example.filedemo/files/DCIM
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void testAppDir(Context context) {
        //4个基本方法
        File fileDir = context.getExternalFilesDir(null);
        Utils.log(fileDir.getAbsolutePath());
        //API>=19
        File[] fileList = context.getExternalFilesDirs(null);

        File cacheDir = context.getExternalCacheDir();
        Utils.log(cacheDir.getAbsolutePath());
        //API>=19
        File[] cacheList = context.getExternalCacheDirs();

        //指定目录，自动生成对应的子目录
        File fileDir2 = context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        Utils.log(fileDir2.getAbsolutePath());
    }

}
