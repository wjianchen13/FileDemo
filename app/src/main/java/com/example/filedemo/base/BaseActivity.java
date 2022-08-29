package com.example.filedemo.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.filedemo.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class BaseActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1024;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public void onPermission(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
                requestSuccess();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                requestSuccess();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else {
            requestSuccess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                requestSuccess();
            } else {
                Toast.makeText(this, "存储权限获取失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                requestSuccess();
            } else {
                Toast.makeText(this, "存储权限获取失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 模拟文件写入
     */
    private void requestSuccess() {
        Toast.makeText(this, "存储权限获取成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 读写应用程序目录
     * @param v
     */
    public void onWrite1(View v) {
        write1("hello", "test1");
    }

    /**
     * 读写应用程序目录
     * @param v
     */
    public void onRead1(View v) {
        String str = read1("test1");
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void write1(String str, String fileName) {
        try {
            FileOutputStream out = openFileOutput(fileName, MODE_PRIVATE);
            out.write(str.getBytes());
            out.close();
            Toast.makeText(BaseActivity.this, "保存成功",Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            // TODOAuto-generated catch block
            e.printStackTrace();
        }
    }

    private String read1(String fileName) {
        try {
            FileInputStream in =openFileInput(fileName);
            int len= in.available();
            byte[]temp = new byte[len];
            in.read(temp);
            String str = new String(temp);
            in.close();
            return str;
        } catch(Exception e) {
            // TODOAuto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * SD卡写文件
     * @param v
     */
    public void onWrite2(View v) {
        write2("hello", "test2");
    }

    /**
     * SD卡读文件
     * @param v
     */
    public void onRead2(View v) {
        String str = read2("test2");
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void write2(String str, String fileName) {
        String filePath = Environment.getExternalStorageDirectory().toString()+ File.separator + "atest";
        File file = new File(filePath + File.separator +fileName); // 定义File类对象
        if(!file.getParentFile().exists()) { // 父文件夹不存在
            file.getParentFile().mkdirs(); // 创建文件夹
        }
        try {
            FileOutputStream out = new FileOutputStream(filePath + File.separator +fileName);
            out.write(str.getBytes());
            out.close();
            Toast.makeText(BaseActivity.this, "保存成功",Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            // TODOAuto-generated catch block
            e.printStackTrace();
        }
    }

    private String read2(String fileName) {
        String filePath = Environment.getExternalStorageDirectory().toString()+ File.separator + "atest";
        File file = new File(filePath + File.separator +fileName); // 定义File类对象
        try {
            FileInputStream in = new FileInputStream(filePath + File.separator +fileName);
            int len= in.available();
            byte[]temp = new byte[len];
            in.read(temp);
            String str = new String(temp);
            in.close();
            return str;
        } catch(Exception e) {
            // TODOAuto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取Raw文件
     * @param v
     */
    public void onRead3(View v) {
        String str = read3(R.raw.test3);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private String read3(int file) {
        String str = "";
        InputStream in = null;
        try{
            in = getResources().openRawResource(file);
            byte[] temp = new byte[in.available()];
            //读取数据
            in.read(temp);
            //依test.txt的编码类型选择合适的编码，如果不调整会乱码
            str = new String(temp);

            return str;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            //关闭   
            if(in != null) {
                try {
                    in.close();
                    in = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** 
     * 读取Assets文件
     * @param v
     */
    public void onRead4(View v) {
        String str = read4("test4.txt");
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private String read4(String fileName) {
        String str="";
        try{
            InputStream in =getResources().getAssets().open(fileName);
            byte []temp = new byte[in.available()];
            in.read(temp);
            in.close();
            str = new String(temp,"UTF-8");
            return str;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 使用File写文件
     * @param v
     */
    public void onWrite5(View v) {
        write5("hello5", "test5");
    }

    /**
     * 使用File读文件
     * @param v
     */
    public void onRead5(View v) {
        String str = read5("test5");
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void write5(String str,String fileName) {
        String filePath = Environment.getExternalStorageDirectory().toString()+ File.separator + "test";
        File file = new File(filePath + File.separator +fileName); // 定义File类对象
        if(!file.getParentFile().exists()) { // 父文件夹不存在
            file.getParentFile().mkdirs(); // 创建文件夹
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(str.getBytes());
            out.close();
            Toast.makeText(BaseActivity.this, "保存成功",Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            // TODOAuto-generated catch block
            e.printStackTrace();
        }
    }

    private String read5(String fileName) {
        String filePath = Environment.getExternalStorageDirectory().toString()+ File.separator + "test";
        File file = new File(filePath + File.separator +fileName); // 定义File类对象
        if(!file.getParentFile().exists()) { // 父文件夹不存在
            file.getParentFile().mkdirs(); // 创建文件夹
        }
        try {
            FileInputStream in = new FileInputStream(file);
            int len= in.available();
            byte[]temp = new byte[len];
            in.read(temp);
            String str = new String(temp,"UTF-8");
            in.close();
            return str;
        } catch(Exception e) {
            // TODOAuto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用RandomAccessFile写文件
     * @param v
     */
    public void onWrite6(View v) {
        write6("hello6", "test6.txt");
    }

    /**
     * 使用RandomAccessFile读文件
     * @param v
     */
    public void onRead6(View v) {
        String str = read6("test6.txt");
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void write6(String str, String fileName) {
        String filePath = Environment.getExternalStorageDirectory().toString()+ File.separator + "test";
        File file = new File(filePath + File.separator +fileName); // 定义File类对象
        if(!file.getParentFile().exists()) { // 父文件夹不存在
            file.getParentFile().mkdirs(); // 创建文件夹
        }
        RandomAccessFile randomFile;
        try {
            randomFile = new RandomAccessFile(filePath + File.separator + fileName,"rw");
            randomFile.write(str.getBytes());
            randomFile.close();
            Toast.makeText(BaseActivity.this, "保存成功",Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            // TODOAuto-generated catch block
            e.printStackTrace();
        }

    }

    private String read6(String fileName) {
        String filePath = Environment.getExternalStorageDirectory().toString()+ File.separator + "test";
        File file = new File(filePath + File.separator +fileName); // 定义File类对象
        if(!file.getParentFile().exists()) { // 父文件夹不存在
            file.getParentFile().mkdirs(); // 创建文件夹
        }
        try {
            RandomAccessFile randomFile = new RandomAccessFile(filePath + File.separator +fileName,"rw");
            int len= (int)randomFile.length();//取得文件长度（字节数）
            byte[]temp = new byte[len];
            randomFile.read(temp);
            String str = new String(temp,"UTF-8");
            randomFile.close();
            return str;
        } catch(Exception e) {
            // TODOAuto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 写文件
     * @param v
     */
    public void onWrite7(View v) {
        writeSeek("nihaihaoma", "test7.txt");
    }

    /**
     * 随机读文件
     * @param v
     */
    public void onRead7(View v) {
        String str = readSeek("test7.txt");
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void writeSeek(String str, String fileName) {
        String filePath = Environment.getExternalStorageDirectory().toString()+ File.separator + "test";
        File file = new File(filePath + File.separator +fileName); // 定义File类对象
        System.out.println("--------->parent path: " + file.getParent());
        if(!file.getParentFile().exists()) { // 父文件夹不存在
            file.getParentFile().mkdirs(); // 创建文件夹
        }
        try {
            FileOutputStream out = new FileOutputStream(filePath + File.separator +fileName);
            out.write(str.getBytes());
            out.close();
            Toast.makeText(BaseActivity.this, "保存成功",Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            // TODOAuto-generated catch block
            e.printStackTrace();
        }
    }

    private String readSeek(String fileName) {
        String filePath = Environment.getExternalStorageDirectory().toString()+ File.separator + "test";
        File file = new File(filePath + File.separator +fileName); // 定义File类对象
        if(!file.getParentFile().exists()) { // 父文件夹不存在
            file.getParentFile().mkdirs(); // 创建文件夹
        }
        try {
            FileInputStream in = new FileInputStream(filePath + File.separator +fileName);
            int len= in.available();
            byte[]temp = new byte[len];
            in.skip(3);
            in.read(temp, 0, 3);
            String str = new String(temp,"UTF-8");
            in.close();
            return str;
        } catch(Exception e) {
            // TODOAuto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    
}