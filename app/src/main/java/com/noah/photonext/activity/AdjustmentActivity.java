package com.noah.photonext.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.imagezoom.ImageViewTouchBase;
import com.noah.photonext.R;
import com.noah.photonext.adapter.AdjustmentToolAdapter;
import com.noah.photonext.base.BaseEditActivity;
import com.noah.photonext.custom.SMat;

import org.opencv.android.Utils;
import org.opencv.core.Scalar;

import butterknife.BindView;

import static com.noah.photonext.util.Utils.currentHistoryPos;
import static com.noah.photonext.util.Utils.historyBitmaps;

/**
 * Created by HuyLV-CT on 22-Dec-16.
 */
public class AdjustmentActivity extends BaseEditActivity {

//    @BindView(R.id.adjustment_exposure)
//    LLayout adjustment_exposure;
//    @BindView(R.id.adjustment_temperature)
//    LLayout adjustment_temperature;
//    @BindView(R.id.adjustment_contrast)
//    LLayout adjustment_contrast;
//    @BindView(R.id.double_lighten)
//    LLayout adjustment_brightness;
//    @BindView(R.id.double_screen)
//    LLayout adjustment_vibrance;

    public static final int EXPOSURE = 0, TEMPERATURE = 1, CONTRAST = 2, BRIGHTNESS = 3, VIBRANCE = 4;
    public int currentMode = EXPOSURE;
    //rv
    @BindView(R.id.adjustment_rv)
    RecyclerView adjustment_rv;
    AdjustmentToolAdapter adjustmentToolAdapter;
    SMat currentMat, newMat;
    Bitmap tempBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edit_main_iv.setImageBitmap(historyBitmaps.get(currentHistoryPos));
        edit_main_iv.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        currentMat = new SMat();

        Utils.bitmapToMat(historyBitmaps.get(currentHistoryPos), currentMat);
        newMat = new SMat(currentMat.size(), currentMat.type(), new Scalar(0, 0, 255, 255));
        tempBitmap = Bitmap.createBitmap(historyBitmaps.get(currentHistoryPos));

        //rv
        adjustmentToolAdapter = new AdjustmentToolAdapter(this, com.noah.photonext.util.Utils.createAdjustmentToolList(getResources()));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
        adjustment_rv.setLayoutManager(gridLayoutManager);
        adjustment_rv.setAdapter(adjustmentToolAdapter);

    }

    @Override
    protected void onSeekBarStopTracking(int value) {
        switch (currentMode) {
            case EXPOSURE:
                currentMat.setExposure(((float) value + 100) / 100);
                break;
            case TEMPERATURE:
                currentMat.setTemperature(value);
                break;
            case CONTRAST:
                currentMat.setContrast(((float) value + 100) / 100);
                break;
            case BRIGHTNESS:
                currentMat.setBrightnesss(value);
                break;
            case VIBRANCE:
                currentMat.setVibrance(value);
                break;
        }
        currentMat.adjustMat(newMat);
        Utils.matToBitmap(newMat, tempBitmap);
        edit_main_iv.setImageBitmap(tempBitmap);
    }


    @Override
    protected void onNavigationDone() {
        super.onNavigationDone();
        historyBitmaps.add(0,tempBitmap);
        setResult(RESULT_OK);
        finish();
    }

//    @OnClick({R.id.adjustment_exposure, R.id.adjustment_temperature, R.id.adjustment_contrast, R.id.double_lighten, R.id.double_screen})
//    public void onClick(View view) {
//        super.onClick(view);
//        resetColor(adjustment_exposure,adjustment_brightness,adjustment_contrast,adjustment_temperature,adjustment_vibrance);
//        switch (view.getId()) {
//            case R.id.adjustment_exposure:
//                currentMode = EXPOSURE;
//                navigation_seek_bar.setProgress(currentMat.getExposureToProgress());
//                adjustment_exposure.setSelected();
//                break;
//            case R.id.adjustment_temperature:
//                currentMode = TEMPERATURE;
//                navigation_seek_bar.setProgress(currentMat.getTemperature());
//                adjustment_temperature.setSelected();
//                break;
//            case R.id.adjustment_contrast:
//                adjustment_contrast.setSelected();
//                navigation_seek_bar.setProgress(currentMat.getContrastToProgress());
//                currentMode = CONTRAST;
//                break;
//            case R.id.double_lighten:
//                adjustment_brightness.setSelected();
//                currentMode = BRIGHTNESS;
//                navigation_seek_bar.setProgress(currentMat.getBrightnesss());
//                break;
//            case R.id.double_screen:
//                adjustment_vibrance.setSelected();
//                currentMode = VIBRANCE;
//                navigation_seek_bar.setProgress(currentMat.getVibrance());
//                break;
//        }
//    }

    public void setSlidingProgress() {
        adjustmentToolAdapter.notifyDataSetChanged();
        switch (currentMode) {
            case TEMPERATURE:
                navigation_seek_bar.setProgress(currentMat.getTemperature());
                break;
            case CONTRAST:
                navigation_seek_bar.setProgress(currentMat.getContrastToProgress());
                break;
            case BRIGHTNESS:
                navigation_seek_bar.setProgress(currentMat.getBrightnesss());
                break;
            case VIBRANCE:
                navigation_seek_bar.setProgress(currentMat.getVibrance());
                break;
            default:// EXPOSURE:
                navigation_seek_bar.setProgress(currentMat.getExposureToProgress());
                break;
        }
    }

    @Override
    protected int getToolbarTitleId() {
        return R.string.adjustment;
    }

    @Override
    protected int isShowSlider() {
        return View.VISIBLE;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_adjustment;
    }


}
