package com.noah.photonext.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.noah.photonext.R;
import com.noah.photonext.activity.CollageActivity;
import com.noah.photonext.custom.SquareLayout;
import com.noah.photonext.model.LayoutObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huylv on 14-Mar-17.
 */

public class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.LayoutViewHolder> {
    CollageActivity c;
    private ArrayList<LayoutObject> layoutArrayList;

    public LayoutAdapter(CollageActivity context, ArrayList<LayoutObject> ls) {
        c = context;
        layoutArrayList = ls;
        layoutArrayList.get(0).selected = true;
    }


    @Override
    public LayoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_layout, parent, false);
        return new LayoutViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LayoutViewHolder holder, final int position) {
        final LayoutObject l = layoutArrayList.get(position);
        holder.item_layout_photo.setImageResource(l.imageId);
        holder.item_layout_photo.setColorFilter(l.selected ? ContextCompat.getColor(c, R.color.colorAccent) : Color.WHITE);
        holder.item_layout_sl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    for (LayoutObject ll : layoutArrayList) {
                        ll.selected = false;
                    }
                    l.selected = true;
                    c.changeLayout(l.first, l.second, l.standard);
                    notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return layoutArrayList.size();
    }

    class LayoutViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_layout_photo)
        ImageView item_layout_photo;
        @BindView(R.id.item_layout_sl)
        SquareLayout item_layout_sl;

        LayoutViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
