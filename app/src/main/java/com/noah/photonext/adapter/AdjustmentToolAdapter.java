package com.noah.photonext.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noah.photonext.R;
import com.noah.photonext.activity.AdjustmentActivity;
import com.noah.photonext.custom.LLayout;
import com.noah.photonext.model.AdjustmentToolObject;

import java.util.ArrayList;

/**
 * Created by huylv on 16-Mar-17.
 */

public class AdjustmentToolAdapter extends RecyclerView.Adapter<AdjustmentToolAdapter.AdjustmentViewHolder> {
    AdjustmentActivity c;
    private ArrayList<AdjustmentToolObject> editToolList;

    public AdjustmentToolAdapter(AdjustmentActivity context, ArrayList<AdjustmentToolObject> editToolObjects) {
        c = context;
        editToolList = editToolObjects;
    }

    @Override
    public AdjustmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_edit_tool, parent, false);
        return new AdjustmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AdjustmentViewHolder holder, final int position) {
        final AdjustmentToolObject e = editToolList.get(position);
        holder.lLayout.setIconImage(c.currentMode == position ? e.iconIdSelected : e.iconId);
        holder.lLayout.setText(e.text);
        holder.lLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.currentMode = position;
                c.setSlidingProgress();
            }
        });
    }

    @Override
    public int getItemCount() {
        return editToolList.size();
    }

    class AdjustmentViewHolder extends RecyclerView.ViewHolder {
        LLayout lLayout;

        AdjustmentViewHolder(View itemView) {
            super(itemView);
            lLayout = (LLayout) itemView;
        }
    }
}
