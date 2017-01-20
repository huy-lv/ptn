package com.noah.photonext.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.imagezoom.ImageViewTouchBase;
import com.noah.photonext.R;
import com.noah.photonext.base.BaseEditActivity;
import com.noah.photonext.custom.LLayout;
import com.noah.photonext.custom.SMat;

import org.opencv.android.Utils;
import org.opencv.core.Scalar;

import butterknife.BindView;
import butterknife.OnClick;

import static com.noah.photonext.activity.AdjustmentActivity.MODE.BRIGHTNESS;
import static com.noah.photonext.activity.AdjustmentActivity.MODE.CONTRAST;
import static com.noah.photonext.activity.AdjustmentActivity.MODE.EXPOSURE;
import static com.noah.photonext.activity.AdjustmentActivity.MODE.TEMPERATURE;
import static com.noah.photonext.activity.AdjustmentActivity.MODE.VIBRANCE;
import static com.noah.photonext.util.Utils.currentHistoryPos;
import static com.noah.photonext.util.Utils.historyBitmaps;

/**
 * Created by HuyLV-CT on 22-Dec-16.
 */
public class AdjustmentActivity extends BaseEditActivity {

    @BindView(R.id.adjustment_exposure)
    LLayout adjustment_exposure;
    @BindView(R.id.adjustment_temperature)
    LLayout adjustment_temperature;
    @BindView(R.id.adjustment_contrast)
    LLayout adjustment_contrast;
    @BindView(R.id.adjustment_brightness)
    LLayout adjustment_brightness;
    @BindView(R.id.adjustment_vibrance)
    LLayout adjustment_vibrance;
    MODE currentMode = EXPOSURE;
    private int currentModeId;

    int exp = 0, temp = 0, cont = 0, bri = 0, vib = 0;

    enum MODE {EXPOSURE, TEMPERATURE, CONTRAST, BRIGHTNESS, VIBRANCE}

    SMat currentMat, newMat;
    Bitmap tempBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edit_main_iv.setImageBitmap(historyBitmaps.get(currentHistoryPos));
        edit_main_iv.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        currentMat = new SMat();
        adjustment_exposure.setSelected();
        currentModeId = R.id.adjustment_exposure;

        Utils.bitmapToMat(historyBitmaps.get(currentHistoryPos), currentMat);
        newMat = new SMat(currentMat.size(), currentMat.type(), new Scalar(0, 0, 255, 255));
        tempBitmap = Bitmap.createBitmap(historyBitmaps.get(currentHistoryPos));


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

    @OnClick({R.id.adjustment_exposure, R.id.adjustment_temperature, R.id.adjustment_contrast, R.id.adjustment_brightness, R.id.adjustment_vibrance})
    public void onClick(View view) {
        super.onClick(view);
        resetColor(adjustment_exposure,adjustment_brightness,adjustment_contrast,adjustment_temperature,adjustment_vibrance);
        switch (view.getId()) {
            case R.id.adjustment_exposure:
                currentMode = EXPOSURE;
                navigation_seek_bar.setProgress(currentMat.getExposureToProgress());
                adjustment_exposure.setSelected();
                break;
            case R.id.adjustment_temperature:
                currentMode = TEMPERATURE;
                navigation_seek_bar.setProgress(currentMat.getTemperature());
                adjustment_temperature.setSelected();
                break;
            case R.id.adjustment_contrast:
                adjustment_contrast.setSelected();
                navigation_seek_bar.setProgress(currentMat.getContrastToProgress());
                currentMode = CONTRAST;
                break;
            case R.id.adjustment_brightness:
                adjustment_brightness.setSelected();
                currentMode = BRIGHTNESS;
                navigation_seek_bar.setProgress(currentMat.getBrightnesss());
                break;
            case R.id.adjustment_vibrance:
                adjustment_vibrance.setSelected();
                currentMode = VIBRANCE;
                navigation_seek_bar.setProgress(currentMat.getVibrance());
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
