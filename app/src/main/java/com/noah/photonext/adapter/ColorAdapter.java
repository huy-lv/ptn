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
    public ArrayList<String> colors;
    CollageActivity context;
    boolean typeColor;

    public ColorAdapter(CollageActivity c, boolean typeColor) {
        this.typeColor = typeColor;
        context = c;
        colors = new ArrayList<>();
        if (typeColor) {
            colors.add("#FFFFFF");
            colors.add("#ffc0cb");
            colors.add("#ffd700");
            colors.add("#40e0d0");
            colors.add("#0000ff");
            colors.add("#ffa500");
            colors.add("#ff0000");
            colors.add("#000000");
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
            holder.iv.setBackgroundColor(Color.parseColor(colors.get(position)));
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.changeBackground(new BackgroundValue(colors.get(position)));
                }
            });
        } else {
            final int id = context.getResources().getIdentifier("rule_" + position, "mipmap", context.getPackageName());
            holder.iv.setImageResource(id);
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.changeBackground(new BackgroundValue(id));
                }
            });
        }
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
