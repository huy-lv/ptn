package com.noah.photonext.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.isseiaoki.simplecropview.CropImageView;
import com.noah.photonext.R;
import com.noah.photonext.base.BaseEditActivity;
import com.noah.photonext.custom.LLayout;

import butterknife.BindView;

import static com.noah.photonext.util.Utils.currentHistoryPos;
import static com.noah.photonext.util.Utils.historyBitmaps;

public class CropActivity extends BaseEditActivity implements View.OnClickListener{

    @BindView(R.id.crop_main_civ)
    CropImageView crop_main_civ;

    @BindView(R.id.crop_ratio_free)
    LLayout crop_ratio_free;
    @BindView(R.id.crop_ratio_original)
    LLayout crop_ratio_original;
    @BindView(R.id.crop_ratio_1x1)
    LLayout crop_ratio_1x1;
    @BindView(R.id.crop_ratio_4x3)
    LLayout crop_ratio_4x3;
    @BindView(R.id.crop_ratio_16x9)
    LLayout crop_ratio_16x9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        crop_main_civ.setImageBitmap(historyBitmaps.get(currentHistoryPos));

        crop_ratio_1x1.setOnClickListener(this);
        crop_ratio_4x3.setOnClickListener(this);
        crop_ratio_16x9.setOnClickListener(this);
        crop_ratio_free.setOnClickListener(this);
        crop_ratio_original.setOnClickListener(this);


        crop_ratio_free.setSelected();
        crop_main_civ.setCropMode(CropImageView.CropMode.FREE);
    }

    @Override
    protected int getToolbarTitleId() {
        return R.string.crop;
    }
    @Override
    public int isShowSlider() {
        return View.INVISIBLE;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        resetColor();
        switch (v.getId()){
            case R.id.crop_ratio_1x1:
                crop_ratio_1x1.setSelected();
                crop_main_civ.setCropMode(CropImageView.CropMode.SQUARE);
                break;
            case R.id.crop_ratio_4x3:
                crop_ratio_4x3.setSelected();
                crop_main_civ.setCropMode(CropImageView.CropMode.RATIO_4_3);
                break;
            case R.id.crop_ratio_16x9:
                crop_ratio_16x9.setSelected();
                crop_main_civ.setCropMode(CropImageView.CropMode.RATIO_16_9);
                break;
            case R.id.crop_ratio_free:
                crop_ratio_free.setSelected();
                crop_main_civ.setCropMode(CropImageView.CropMode.FREE);
                break;
            case R.id.crop_ratio_original:
                crop_ratio_original.setSelected();
                crop_main_civ.setCropMode(CropImageView.CropMode.FIT_IMAGE);
                break;
        }
    }

    @Override
    protected void onNavigationDone() {
        super.onNavigationDone();
        historyBitmaps.add(0,crop_main_civ.getCroppedBitmap());
        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    private void resetColor() {
        crop_ratio_free.clearColor();
        crop_ratio_16x9.clearColor();
        crop_ratio_original.clearColor();
        crop_ratio_4x3.clearColor();
        crop_ratio_1x1.clearColor();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_crop;
    }

}
