package com.noah.photonext.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.noah.photonext.R;
import com.noah.photonext.activity.PickPhotoActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.noah.photonext.util.Utils.PREPATH;

/**
 * Created by huylv on 14-Mar-17.
 */

public class SelectedPhotoAdapter extends RecyclerView.Adapter<SelectedPhotoAdapter.PhotoViewHolder> {
    PickPhotoActivity c;
    ArrayList<Photo> selectedPhoto;

    public SelectedPhotoAdapter(PickPhotoActivity context, ArrayList<Photo> s) {
        c = context;
        selectedPhoto = s;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, final int position) {
        final Photo p = selectedPhoto.get(position);
        Picasso.with(c).load(PREPATH + p.sdcardPath).placeholder(R.mipmap.no_media).fit().centerCrop().into(holder.photo);
        holder.closeButton.setVisibility(View.VISIBLE);
        holder.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexInGV = c.galleryAdapter.data.indexOf(selectedPhoto.get(position));
                c.removeSelectedPhoto(selectedPhoto.get(position));
                c.galleryAdapter.data.get(indexInGV).isSeleted = false;
                c.galleryAdapter.notifyDataSetChanged();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedPhoto.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        ImageView closeButton;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.item_photo_image);
            closeButton = (ImageView) itemView.findViewById(R.id.item_photo_close);
        }
    }
}
