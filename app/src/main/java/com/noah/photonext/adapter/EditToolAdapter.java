package com.noah.photonext.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noah.photonext.R;
import com.noah.photonext.activity.EditActivity;
import com.noah.photonext.custom.LLayout;
import com.noah.photonext.model.EditToolObject;
import com.noah.photonext.util.Utils;

import java.util.ArrayList;

/**
 * Created by huylv on 15-Mar-17.
 */

public class EditToolAdapter extends RecyclerView.Adapter<EditToolAdapter.ToolViewHolder> {
    EditActivity c;
    private ArrayList<EditToolObject> editToolList;

    public EditToolAdapter(EditActivity context, ArrayList<EditToolObject> editToolObjects) {
        c = context;
        editToolList = editToolObjects;
    }

    @Override
    public ToolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_edit_tool, parent, false);
        return new ToolViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ToolViewHolder holder, int position) {
        final EditToolObject e = editToolList.get(position);
        holder.lLayout.setIconImage(e.iconId);
        holder.lLayout.setText(e.text);
        holder.lLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.startActivityForResult(new Intent(c, e.gotoClass), Utils.REQUEST_CODE_CROP);
            }
        });
    }

    @Override
    public int getItemCount() {
        return editToolList.size();
    }

    class ToolViewHolder extends RecyclerView.ViewHolder {
        LLayout lLayout;

        ToolViewHolder(View itemView) {
            super(itemView);
            lLayout = (LLayout) itemView;
        }
    }
}
