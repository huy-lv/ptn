package com.noah.photonext.activity;

import android.os.Bundle;
import android.view.View;

import com.noah.photonext.R;
import com.noah.photonext.base.BaseEditActivity;
import com.noah.photonext.custom.SMat;

import static com.noah.photonext.util.Utils.currentHistoryPos;
import static com.noah.photonext.util.Utils.historyBitmaps;

/**
 * Created by HuyLV-CT on 10-Jan-17.
 */
public class AutoContrastActivity extends BaseEditActivity implements View.OnClickListener{

    private SMat srcMat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edit_main_iv.setImageBitmap(historyBitmaps.get(currentHistoryPos));
//        srcMat = new SMat();
//        Utils.bitmapToMat(historyBitmaps.get(currentHistoryPos),srcMat);



    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected int getToolbarTitleId() {
        return R.string.auto_contrast;
    }

    @Override
    protected int isShowSlider() {
        return View.INVISIBLE;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auto_contrast;
    }
}
