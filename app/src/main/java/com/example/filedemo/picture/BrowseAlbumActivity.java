package com.example.filedemo.picture;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filedemo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://guolin.blog.csdn.net/article/details/105419420
 */
public class BrowseAlbumActivity extends AppCompatActivity {

    private List<Image> imageList = new ArrayList<Image>();

    private HashMap<String, Image> checkedImages = new HashMap<String, Image>();
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private boolean pickFiles;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_album);
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        pickFiles = getIntent().getBooleanExtra("pick_files", false);
        String title = "";
        if (pickFiles) {
            title = "Pick Images";
        } else {
            title = "Browse Album";
        }
        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                int columns = 3;
                int imageSize = recyclerView.getWidth() / columns;
                AlbumAdapter  adapter = new AlbumAdapter(BrowseAlbumActivity.this, imageList, checkedImages, imageSize, pickFiles);
                recyclerView.setLayoutManager(new GridLayoutManager(BrowseAlbumActivity.this, columns));
                recyclerView.setAdapter(adapter);
                loadImages(adapter);
                return false;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkedImages.isEmpty()) {
                    Toast.makeText(BrowseAlbumActivity.this, "You didn't choose any image", Toast.LENGTH_SHORT).show();
                    return;
                }
                List checkedUris = new ArrayList<Uri>();

                for (Object key: checkedImages.keySet()) {
                    System.out.println("第二种:" + checkedImages.get(key));
                    checkedUris.add(checkedImages.get(key).uri);
                }

                Intent data = new Intent();
                data.putExtra("checked_uris", (Parcelable) checkedUris);
                setResult(RESULT_OK, data);
                finish();
            }
        });
        if(pickFiles) {
            fab.setVisibility(View.VISIBLE);
        }
    }

    private void loadImages(AlbumAdapter adapter) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                        null, null, MediaStore.MediaColumns.DATE_ADDED + " desc");
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                        imageList.add(new Image(uri, false));
                    }
                    cursor.close();
                }
                BrowseAlbumActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();    
                    }
                }); 
            }
        }).start();
    }
    
    
    
}
