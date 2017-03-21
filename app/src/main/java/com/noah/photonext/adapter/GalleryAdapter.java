package com.noah.photonext.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.noah.photonext.R;
import com.noah.photonext.activity.PickPhotoActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;

import static com.noah.photonext.util.Utils.PREPATH;
import static java.util.ResourceBundle.clearCache;


public class GalleryAdapter extends BaseAdapter {

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
    public int getCount() {
        return data.size();
    }

    @Override
    public Photo getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = infalter.inflate(R.layout.item_photo, null);
            holder = new ViewHolder();
            holder.item_photo_image = (ImageView) convertView
                    .findViewById(R.id.item_photo_image);
            holder.item_photo_checkbox = (SmoothCheckBox) convertView.findViewById(R.id.item_photo_checkbox);
            holder.item_photo_fl = (FrameLayout) convertView.findViewById(R.id.item_photo_fl);
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

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_photo_image.setTag(position);

        if (data.get(position).isSeleted) {
            if (!holder.item_photo_checkbox.isChecked())
                holder.item_photo_checkbox.setChecked(true, true);
        } else holder.item_photo_checkbox.setChecked(false);

        try {
            Picasso.with(mContext).load(PREPATH + data.get(position).sdcardPath).placeholder(R.mipmap.no_media).fit().centerCrop().into(holder.item_photo_image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
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

        notifyDataSetChanged();
        mContext.updateTitle(getSelectedItem().size());
    }

    private class ViewHolder {
        ImageView item_photo_image;
        SmoothCheckBox item_photo_checkbox;
        FrameLayout item_photo_fl;
    }

}
