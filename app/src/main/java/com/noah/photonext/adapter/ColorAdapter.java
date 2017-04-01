package com.noah.photonext.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.noah.photonext.R;
import com.noah.photonext.activity.CollageActivity;
import com.noah.photonext.model.BackgroundValue;

import java.util.ArrayList;

/**
 * Created by huylv on 18-Mar-17.
 */

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorVH> {
    public ArrayList<BackgroundValue> colors;
    CollageActivity context;
    boolean typeColor;

    public ColorAdapter(CollageActivity c, boolean typeColor) {
        this.typeColor = typeColor;
        context = c;
        colors = new ArrayList<>();
        if (typeColor) {
            colors.add(new BackgroundValue("#FFFFFF"));
            colors.add(new BackgroundValue("#ffc0cb"));
            colors.add(new BackgroundValue("#ffd700"));
            colors.add(new BackgroundValue("#40e0d0"));
            colors.add(new BackgroundValue("#0000ff"));
            colors.add(new BackgroundValue("#ffa500"));
            colors.add(new BackgroundValue("#ff0000"));
            colors.add(new BackgroundValue("#000000"));
            colors.add(new BackgroundValue(R.mipmap.ic_text_color));
        } else {
            for (int i = 0; i < 15; i++) {
                int id = context.getResources().getIdentifier("rule_" + i, "mipmap", context.getPackageName());
                colors.add(new BackgroundValue(id));
            }
        }
    }

    @Override
    public ColorVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_color, parent, false);
        return new ColorVH(v);
    }

    @Override
    public void onBindViewHolder(final ColorVH holder, final int position) {
        if (typeColor) {
            if (position < colors.size() - 1)
                holder.iv.setBackgroundColor(Color.parseColor(colors.get(position).color));
            else holder.iv.setImageResource(R.mipmap.ic_text_color);
        } else {
            holder.iv.setImageResource(colors.get(position).imageId);
        }
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.changeBackground(typeColor, colors.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return typeColor ? colors.size() : 15;
    }

    class ColorVH extends RecyclerView.ViewHolder {
        ImageView iv;

        public ColorVH(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.item_color_iv);
        }
    }
}
