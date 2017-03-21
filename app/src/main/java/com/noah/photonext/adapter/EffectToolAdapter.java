package com.noah.photonext.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noah.photonext.R;
import com.noah.photonext.activity.EditActivity;
import com.noah.photonext.activity.EffectActivity;
import com.noah.photonext.custom.LLayout;
import com.noah.photonext.model.EffectToolObject;
import com.noah.photonext.util.Utils;

import java.util.ArrayList;

/**
 * Created by huylv on 17-Mar-17.
 */

public class EffectToolAdapter extends RecyclerView.Adapter<EffectToolAdapter.EffectToolViewHolder> {
    Activity c;
    ArrayList<EffectToolObject> effectList;

    public EffectToolAdapter(Activity context, ArrayList<EffectToolObject> ef) {
        c = context;
        effectList = ef;
    }

    @Override
    public EffectToolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_double, parent, false);
        return new EffectToolViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EffectToolViewHolder holder, final int position) {
        EffectToolObject e = effectList.get(position);
        holder.la.setText(e.text);
        holder.la.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c instanceof EditActivity) {
                    Intent i = new Intent(c, EffectActivity.class);
                    i.putExtra(Utils.INTENT_KEY_EFFECT_GROUP, position);
                    c.startActivity(i);
                } else { // instance of EffectActivity

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return effectList.size();
    }

    class EffectToolViewHolder extends RecyclerView.ViewHolder {
        LLayout la;

        public EffectToolViewHolder(View itemView) {
            super(itemView);
            la = (LLayout) itemView;
        }
    }
}
