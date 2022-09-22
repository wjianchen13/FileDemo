package com.example.filedemo.picture;

import android.net.Uri;

public class Image {

    public Uri uri; 
    public Boolean checked;

    public Image(Uri uri, Boolean checked) {
        this.uri = uri;
        this.checked = checked;
    }
}
