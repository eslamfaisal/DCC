package com.ibnsaad.thedcc.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.model.Photo;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.List;

public class ProfileImagesAdapter extends RecyclerView.Adapter<ProfileImagesAdapter.Holder> {

    private static final String TAG = "ProfileImagesAdapter";
    private List<Photo> photoList;
    private Context context;

    public ProfileImagesAdapter(List<Photo> photoList, Context context) {
        this.photoList = photoList;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_images_item, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Photo photo = photoList.get(position);
        holder.image.setImageURI(photo.getUrl());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImageViewer.Builder<>(context, photoList).setFormatter(new ImageViewer.Formatter<Photo>() {
                    @Override
                    public String format(Photo customImage) {
                        return customImage.getUrl();
                    }
                }).setStartPosition(position)
                        .hideStatusBar(false)
                        .allowZooming(true)
                        .allowSwipeToDismiss(true)
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList =(photoList);

        Log.d(TAG, "setPhotoList: " + photoList.size());
    }

    class Holder extends RecyclerView.ViewHolder {

        SimpleDraweeView image;

        public Holder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_item);
        }
    }
}
