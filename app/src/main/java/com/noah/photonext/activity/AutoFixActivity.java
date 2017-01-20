package com.noah.photonext.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.imagezoom.ImageViewTouchBase;
import com.noah.photonext.R;
import com.noah.photonext.base.BaseEditActivity;
import com.noah.photonext.custom.SMat;

import org.opencv.android.Utils;

import static com.noah.photonext.util.Utils.currentHistoryPos;
import static com.noah.photonext.util.Utils.historyBitmaps;

/**
 * Created by HuyLV-CT on 28-Dec-16.
 */
public class AutoFixActivity extends BaseEditActivity {
    private Bitmap tempBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SMat mat1 = new SMat();
        SMat mat2 = new SMat();
        tempBitmap = historyBitmaps.get(currentHistoryPos).copy(historyBitmaps.get(currentHistoryPos).getConfig(),true);
        Utils.bitmapToMat(historyBitmaps.get(currentHistoryPos), mat1);

        mat1.autofix(mat2, 3);

        Utils.matToBitmap(mat2, tempBitmap);
        edit_main_iv.setImageBitmap(tempBitmap);
        edit_main_iv.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);
    }

    @Override
    protected void onNavigationDone() {
        super.onNavigationDone();
        historyBitmaps.add(0,tempBitmap);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected int getToolbarTitleId() {
        return R.string.autofix;
    }

    @Override
    protected int isShowSlider() {
        return View.INVISIBLE;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auto_fix;
    }
}
