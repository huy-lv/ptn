package com.noah.photonext.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noah.photonext.R;
import com.noah.photonext.activity.DoubleActivity;
import com.noah.photonext.custom.LLayout;
import com.noah.photonext.model.DoubleThumbnailObject;

import java.util.ArrayList;

/**
 * Created by huylv on 16-Mar-17.
 */

public class DoubleThumbnailAdapter extends RecyclerView.Adapter<DoubleThumbnailAdapter.DoubleViewHolder> {
    DoubleActivity c;
    private ArrayList<DoubleThumbnailObject> doubleList;

    public DoubleThumbnailAdapter(DoubleActivity context, ArrayList<DoubleThumbnailObject> l) {
        c = context;
        doubleList = l;
    }

    @Override
    public DoubleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_double, parent, false);
        return new DoubleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DoubleViewHolder holder, final int position) {
        DoubleThumbnailObject d = doubleList.get(position);
        if (d.bitmap != null) holder.layout.setImageBitmap(d.bitmap);
        holder.layout.setText(d.text);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.currentType = position;
                c.redraw();
            }
        });
    }

    @Override
    public int getItemCount() {
        return doubleList.size();
    }

    class DoubleViewHolder extends RecyclerView.ViewHolder {
        LLayout layout;

        DoubleViewHolder(View itemView) {
            super(itemView);
            layout = (LLayout) itemView;
        }
    }
}
