package com.noah.photonext.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.noah.photonext.R;
import com.noah.photonext.adapter.EffectToolAdapter;
import com.noah.photonext.base.BaseActivityToolbar;
import com.noah.photonext.model.EffectToolObject;
import com.noah.photonext.util.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huylv on 17-Mar-17.
 */

public class EffectActivity extends BaseActivityToolbar {
    @BindView(R.id.edit_main_iv)
    ImageView edit_main_iv;

    @BindView(R.id.effect_list_rv)
    RecyclerView effect_list_rv;
    EffectToolAdapter effectToolAdapter;
    ArrayList<EffectToolObject> effectList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_effect;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        edit_main_iv.setImageBitmap(Utils.historyBitmaps.get(Utils.currentHistoryPos));
        effectToolAdapter = new EffectToolAdapter(this, Utils.createEffectToolList(getResources()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        effect_list_rv.setLayoutManager(linearLayoutManager);
        effect_list_rv.setAdapter(effectToolAdapter);


    }
}
