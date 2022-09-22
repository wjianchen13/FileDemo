package com.example.filedemo.picture;
        

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.filedemo.R;

import java.util.HashMap;
import java.util.List;

class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context context;
    private List<Image> imageList;
    private HashMap<String, Image> checkedImages;
    private int imageSize;
    private boolean pickFiles;

    public AlbumAdapter(Context context, List<Image> imageList, HashMap<String, Image> checkedImages, int imageSize, boolean pickFiles) {
        this.context = context;
        this.imageList = imageList;
        this.checkedImages = checkedImages;
        this.imageSize = imageSize;
        this.pickFiles = pickFiles;
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.album_image_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        if (pickFiles) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    Image image = imageList.get(position);
                    image.checked = !image.checked;
                    if (image.checked) {
                        checkedImages.put(image.uri.toString(), image);
                    } else {
                        checkedImages.remove(image.uri.toString());
                    }
                    notifyItemChanged(position);
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewGroup.LayoutParams param = holder.imageView.getLayoutParams();
        param.width = imageSize;
        param.height = imageSize;
        holder.imageView.setLayoutParams(param);
        Image image = imageList.get(position);
        if (image.checked) {
            holder.checkedView.setVisibility(View.VISIBLE);
        } else {
            holder.checkedView.setVisibility(View.INVISIBLE);
        }
        RequestOptions options = new RequestOptions().placeholder(R.drawable.album_loading_bg).override(imageSize, imageSize);
        Glide.with(context).load(image.uri).apply(options).into(holder.imageView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public ImageView checkedView;
        
        public ViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            checkedView = view.findViewById(R.id.checkedView);
        }



    }
    
}