package com.noah.photonext.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.noah.photonext.R;
import com.noah.photonext.base.BaseEditActivity;
import com.noah.photonext.custom.SMat;

import org.opencv.android.Utils;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static com.noah.photonext.util.Utils.currentHistoryPos;
import static com.noah.photonext.util.Utils.historyBitmaps;

/**
 * Created by HuyLV-CT on 10-Jan-17.
 */
public class SmoothActivity extends BaseEditActivity implements View.OnClickListener{

    private SMat srcMat,dstMat;
    private Bitmap tempBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigation_seek_bar.setAbsoluteMinMaxValue(0,100);
        navigation_seek_bar.setProgress(0);

        edit_main_iv.setImageBitmap(historyBitmaps.get(currentHistoryPos));
        srcMat = new SMat();
        Utils.bitmapToMat(historyBitmaps.get(currentHistoryPos),srcMat);
        dstMat = new SMat(srcMat.size(),srcMat.type(),new Scalar(0,0,255,255));
        tempBitmap = historyBitmaps.get(currentHistoryPos).copy(historyBitmaps.get(currentHistoryPos).getConfig(),true);

    }

    @Override
    protected void onSeekBarStopTracking(int value) {
        super.onSeekBarStopTracking(value);
        value /= 10;
        if(value%2==0)value++;
        Imgproc.medianBlur(srcMat,dstMat,value);
        Utils.matToBitmap(dstMat,tempBitmap);
        edit_main_iv.setImageBitmap(tempBitmap);
    }

    @Override
    protected void onNavigationDone() {
        super.onNavigationDone();
        historyBitmaps.add(0,tempBitmap);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected int getToolbarTitleId() {
        return R.string.smooth;
    }

    @Override
    protected int isShowSlider() {
        return View.VISIBLE;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_smooth;
    }
}
