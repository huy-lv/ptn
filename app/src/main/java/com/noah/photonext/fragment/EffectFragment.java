package com.noah.photonext.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noah.photonext.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.noah.photonext.util.Utils.BUNDLE_FRAGMENT_KEY;

/**
 * Created by HuyLV-CT on 09-Mar-17.
 */

public class EffectFragment extends Fragment {

    @BindView(R.id.tvvvvv)
    TextView t;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_effect_group, container, false);
        ButterKnife.bind(this, v);

        t.setText("sssssssssss" + getArguments().getInt(BUNDLE_FRAGMENT_KEY));


        return v;
    }
}
