package com.noah.photonext.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imagezoom.ImageViewTouch;
import com.imagezoom.ImageViewTouchBase;
import com.noah.photonext.R;
import com.noah.photonext.custom.LLayout;
import com.noah.photonext.custom.StartSeekBar;

import butterknife.BindView;

import static com.noah.photonext.util.Utils.currentHistoryPos;
import static com.noah.photonext.util.Utils.historyBitmaps;

/**
 * Created by HuyLV-CT on 13-Dec-16.
 */

public abstract class BaseEditActivity extends BaseActivityToolbar{
    @BindView(R.id.navigation_slider)
    protected StartSeekBar navigation_seek_bar;
    @BindView(R.id.adjustment_seekbar_tv)
    @Nullable
    protected TextView adjustment_slider_tv;
    @Nullable
    @BindView(R.id.edit_main_iv)
    protected ImageViewTouch edit_main_iv;
    protected StartSeekBar.OnSeekBarChangeListener seekBarListener;
    @BindView(R.id.navigation_cancel_iv)
    ImageView navigation_cancel_iv;
    @BindView(R.id.navigation_done_iv)
    ImageView navigation_done_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(getToolbarTitleId()));
        hideToolbarButton();
        //hideSlider
        navigation_seek_bar.setVisibility(isShowSlider());

        navigation_cancel_iv.setOnClickListener(this);
        navigation_done_iv.setOnClickListener(this);

        seekBarListener = new StartSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onSeekBarValueChange(StartSeekBar bar, int value) {
                adjustment_slider_tv.setText(String.valueOf(value));
                onSeekBarTracking(value);
            }

            @Override
            public void onStartTrackingTouch(StartSeekBar bar) {
                adjustment_slider_tv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(StartSeekBar bar, int value) {
                adjustment_slider_tv.setVisibility(View.INVISIBLE);
                onSeekBarStopTracking(value);
            }
        };
        navigation_seek_bar.setOnSeekBarChangeListener(seekBarListener);

        if (edit_main_iv != null)
            edit_main_iv.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
    }

    protected void onSeekBarTracking(int value){

    }

    protected void onSeekBarStopTracking(int value) {

    }

    protected abstract int getToolbarTitleId();
    protected abstract int isShowSlider();
    protected void resetColor(LLayout... v) {
        for(LLayout vv:v){
            vv.clearColor();
        }
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.navigation_cancel_iv:
                onNavigationCancel();
                break;
            case R.id.navigation_done_iv:
                onNavigationDone();
                break;
        }
    }

    protected void onNavigationDone() {
        if(currentHistoryPos>0) {
            for(int i=0;i<currentHistoryPos;i++){
                historyBitmaps.remove(0);
            }
            currentHistoryPos=0;
        }
    }

    protected void onNavigationCancel() {
        finish();
    }
}
