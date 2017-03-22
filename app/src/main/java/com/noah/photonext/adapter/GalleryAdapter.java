package com.noah.photonext.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.noah.photonext.R;
import com.noah.photonext.activity.PickPhotoActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.refactor.library.SmoothCheckBox;

import static com.noah.photonext.util.Utils.PREPATH;
import static java.util.ResourceBundle.clearCache;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PhotoViewHolder> {

    public ArrayList<Photo> data = new ArrayList<Photo>();
    private PickPhotoActivity mContext;
    private LayoutInflater infalter;
    private boolean PICK_ONE;
    private ArrayList<Photo> selectedPhoto;

    public GalleryAdapter(PickPhotoActivity c, ArrayList<Photo> sp) {
        infalter = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = c;
        clearCache();
        selectedPhoto = sp;
    }

    public void setPickOne(boolean isPickOne) {
        PICK_ONE = isPickOne;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, final int position) {
        holder.item_photo_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChanged(position);
            }
        });
        holder.item_photo_fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChanged(position);
            }
        });
//        if (data.get(position).isSeleted) {
//            if (!holder.item_photo_checkbox.isChecked())
//                holder.item_photo_checkbox.setChecked(true, true);
//        } else holder.item_photo_checkbox.setChecked(false);
        if (data.get(position).isSeleted) holder.item_photo_checkbox.setChecked(true, true);
        else holder.item_photo_checkbox.setChecked(false);

        try {
            Picasso.with(mContext).load(PREPATH + data.get(position).sdcardPath).placeholder(R.mipmap.no_media).fit().centerCrop().into(holder.item_photo_image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ArrayList<Photo> getSelectedItem() {
        ArrayList<Photo> dataT = new ArrayList<Photo>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                dataT.add(data.get(i));
            }
        }

        return dataT;
    }

    public void clearSelection() {
        for (Photo p : data) {
            p.isSeleted = false;
        }
    }

    public void addAll(ArrayList<Photo> files) {

        try {
            this.data.clear();
            this.data.addAll(files);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//
//        final ViewHolder holder;
//        if (convertView == null) {
//            convertView = infalter.inflate(R.layout.item_photo, null);
//            holder = new ViewHolder();
//            holder.item_photo_image = (ImageView) convertView
//                    .findViewById(R.id.item_photo_image);
//            holder.item_photo_checkbox = (SmoothCheckBox) convertView.findViewById(R.id.item_photo_checkbox);
//            holder.item_photo_fl = (FrameLayout) convertView.findViewById(R.id.item_photo_fl);
//            holder.item_photo_checkbox.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    checkChanged(position);
//                }
//            });
//            holder.item_photo_fl.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    checkChanged(position);
//                }
//            });
//
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        holder.item_photo_image.setTag(position);
//
//        if (data.get(position).isSeleted) {
//            if (!holder.item_photo_checkbox.isChecked())
//                holder.item_photo_checkbox.setChecked(true, true);
//        } else holder.item_photo_checkbox.setChecked(false);
//
//        try {
//            Picasso.with(mContext).load(PREPATH + data.get(position).sdcardPath).placeholder(R.mipmap.no_media).fit().centerCrop().into(holder.item_photo_image);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return convertView;
//    }

    public boolean isEmpty() {
        return data.size() == 0;
    }

    private void checkChanged(int pos){
        if (PICK_ONE) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).isSeleted = false;
            }
            data.get(pos).isSeleted =true;
            mContext.addSelectedPhoto(data.get(pos));
        }else{
            data.get(pos).isSeleted = !data.get(pos).isSeleted;
            if (data.get(pos).isSeleted) mContext.addSelectedPhoto(data.get(pos));
            else {
                mContext.removeSelectedPhoto(data.get(pos));
            }
        }

        notifyItemChanged(pos);
        mContext.updateTitle(getSelectedItem().size());
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_photo_image)
        ImageView item_photo_image;
        @BindView(R.id.item_photo_checkbox)
        SmoothCheckBox item_photo_checkbox;
        @BindView(R.id.item_photo_fl)
        FrameLayout item_photo_fl;

        PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
