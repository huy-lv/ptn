package com.noah.photonext.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.noah.photonext.fragment.EffectFragment;

import java.util.List;

/**
 * Created by HuyLV-CT on 09-Mar-17.
 */

public class CustomPagerAdapter extends FragmentPagerAdapter {

    private List<EffectFragment> fragments;

    public CustomPagerAdapter(FragmentManager fm, List<EffectFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

}
