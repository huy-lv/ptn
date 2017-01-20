package com.noah.photonext.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.noah.photonext.R;
import com.noah.photonext.base.BaseEditActivity;
import com.noah.photonext.custom.LLayout;
import com.noah.photonext.custom.SMat;

import org.opencv.android.Utils;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import butterknife.BindView;

import static com.noah.photonext.util.Utils.currentHistoryPos;
import static com.noah.photonext.util.Utils.historyBitmaps;

/**
 * Created by HuyLV-CT on 10-Jan-17.
 */
public class BlurActivity extends BaseEditActivity implements View.OnClickListener{

    @BindView(R.id.blur_blur)
    LLayout blur_blur;
    @BindView(R.id.blur_circular)
    LLayout blur_circular;
    @BindView(R.id.blur_linear)
    LLayout blur_linear;

    SMat srcMat,dstMat;
    private Bitmap tempBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        blur_blur.setOnClickListener(this);
        blur_circular.setOnClickListener(this);
        blur_linear.setOnClickListener(this);
        blur_blur.setSelected();
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
        if(value%2==0) value++;
        Imgproc.GaussianBlur(srcMat,dstMat,new Size(value,value),0);
        Utils.matToBitmap(dstMat,tempBitmap);
        edit_main_iv.setImageBitmap(tempBitmap);
    }

    @Override
    protected void onNavigationDone() {
        super.onNavigationDone();
        historyBitmaps.add(0,tempBitmap);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        resetColor(blur_blur,blur_circular,blur_linear);
        switch (v.getId()){
            case R.id.blur_blur:
                blur_blur.setSelected();
                navigation_seek_bar.setVisibility(View.VISIBLE);
                break;
            case R.id.blur_circular:
                blur_circular.setSelected();
                navigation_seek_bar.setVisibility(View.INVISIBLE);
                Toast.makeText(this,"Coming soon",Toast.LENGTH_SHORT).show();
                break;
            case R.id.blur_linear:
                blur_linear.setSelected();
                navigation_seek_bar.setVisibility(View.INVISIBLE);
                Toast.makeText(this,"Coming soon",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected int getToolbarTitleId() {
        return R.string.blur;
    }
    @Override
    protected int isShowSlider() {
        return View.VISIBLE;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_blur;
    }
}
