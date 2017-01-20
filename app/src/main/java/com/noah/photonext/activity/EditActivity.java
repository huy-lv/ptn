package com.noah.photonext.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import com.imagezoom.ImageViewTouch;
import com.imagezoom.ImageViewTouchBase;
import com.noah.photonext.R;
import com.noah.photonext.base.BaseActivityToolbar;
import com.noah.photonext.custom.LLayout;
import com.noah.photonext.util.Utils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

import static com.noah.photonext.R.id.collage_indicator2;
import static com.noah.photonext.R.id.collage_indicator3;
import static com.noah.photonext.R.id.collage_indicator4;
import static com.noah.photonext.R.id.collage_indicator5;
import static com.noah.photonext.util.Utils.INTENT_KEY_PICK_ONE_EDIT;
import static com.noah.photonext.util.Utils.PREPATH;
import static com.noah.photonext.util.Utils.currentBitmap;
import static com.noah.photonext.util.Utils.currentHistoryPos;
import static com.noah.photonext.util.Utils.historyBitmaps;


/**
 * Created by HuyLV-CT on 30-Nov-16.
 */

public class EditActivity extends BaseActivityToolbar implements View.OnClickListener {
    @BindView(R.id.edit_main_iv)
    ImageViewTouch edit_main_iv;

    @BindView(R.id.bottom_bar_second1)
    HorizontalScrollView bottom_bar_second1;
    @BindView(R.id.edit_tool_iv)
    ImageView edit_tool_iv;
    @BindView(R.id.collage_indicator1)
    View collage_indicator1;

    @BindView(R.id.edit_brushes_iv)
    ImageView edit_brushes_iv;
    @BindView(collage_indicator2)
    View collageIndicator2;

    @BindView(R.id.edit_effect_iv)
    ImageView edit_effect_iv;
    @BindView(collage_indicator3)
    View collageIndicator3;

    @BindView(R.id.edit_frame_iv)
    ImageView edit_frame_iv;
    @BindView(collage_indicator4)
    View collageIndicator4;

    @BindView(R.id.edit_text_iv)
    ImageView edit_text_iv;
    @BindView(collage_indicator5)
    View collageIndicator5;

    @BindView(R.id.edit_f1_crop)
    LLayout edit_f1_crop;
    @BindView(R.id.edit_f2_rotate)
    LLayout edit_f2_rotate;
    @BindView(R.id.edit_f3_double)
    LLayout edit_f3_double;
    @BindView(R.id.edit_f4_adjustment)
    LLayout edit_f4_adjustment;
    @BindView(R.id.edit_f5_autofix)
    LLayout edit_f5_autofix;
    @BindView(R.id.edit_f6_auto_contrast)
    LLayout edit_f6_auto_contrast;
    @BindView(R.id.edit_f7_blur)
    LLayout edit_f7_blur;
    @BindView(R.id.edit_f8_smooth)
    LLayout edit_f8_smooth;
    @BindView(R.id.edit_f9_sharpen)
    LLayout edit_f9_sharpen;
    @BindView(R.id.edit_f10_splash)
    LLayout edit_f10_splash;

    String currentImagePath;

    @BindView(R.id.toolbar_undo_iv)
    ImageView toolbar_undo_iv;
    @BindView(R.id.toolbar_redo_iv)
    ImageView toolbar_redo_iv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edit_main_iv.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);
        Intent i = getIntent();
        if (i != null) {
            if (i.hasExtra(INTENT_KEY_PICK_ONE_EDIT)) {//edit one photo

                currentImagePath = getIntent().getStringExtra(INTENT_KEY_PICK_ONE_EDIT);

                Picasso.with(this).load(PREPATH + currentImagePath).into(edit_main_iv);
                currentBitmap = Utils.fixRotateBitmap(currentImagePath);
                historyBitmaps.add(currentBitmap);
            } else {//edit in collage
            }
        } else {
            Log.e("cxz", "error");

        }

        showHidePanel(-1);

        edit_tool_iv.setOnClickListener(this);
        edit_f1_crop.setOnClickListener(this);
        edit_f2_rotate.setOnClickListener(this);
        edit_f3_double.setOnClickListener(this);
        edit_f4_adjustment.setOnClickListener(this);
        edit_f5_autofix.setOnClickListener(this);
        edit_f6_auto_contrast.setOnClickListener(this);
        edit_f7_blur.setOnClickListener(this);
        edit_f8_smooth.setOnClickListener(this);
        edit_f9_sharpen.setOnClickListener(this);
        edit_f10_splash.setOnClickListener(this);

        toolbar_undo_iv.setOnClickListener(this);
        toolbar_redo_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int size = historyBitmaps.size();
        switch (v.getId()) {
            case R.id.toolbar_undo_iv:
                if (size > 1 && currentHistoryPos < size - 1) {
                    currentHistoryPos += 1;
                    Log.e("cxz", "current history pos = " + currentHistoryPos);
                    currentBitmap = historyBitmaps.get(currentHistoryPos);
                    edit_main_iv.setImageBitmap(currentBitmap);
                    updateUndoRedoButton();
                }

                break;
            case R.id.toolbar_redo_iv:
                if (size > 1 && currentHistoryPos > 0) {
                    currentHistoryPos -= 1;
                    Log.e("cxz", "current history pos = " + currentHistoryPos);
                    currentBitmap = historyBitmaps.get(currentHistoryPos);
                    edit_main_iv.setImageBitmap(currentBitmap);
                    updateUndoRedoButton();
                }
                break;
            case R.id.edit_tool_iv:
                showHidePanel(0);
                break;
            case R.id.edit_brushes_iv:
                showHidePanel(1);
                break;
            case R.id.edit_effect_iv:
                showHidePanel(2);
                break;
            case R.id.edit_frame_iv:
                showHidePanel(3);
                break;
            case R.id.edit_text_iv:
                showHidePanel(4);
                break;
            case R.id.edit_f1_crop:
                startActivityForResult(new Intent(this, CropActivity.class), Utils.REQUEST_CODE_CROP);
                break;
            case R.id.edit_f2_rotate:
                startActivityForResult(new Intent(this, RotateActivity.class), Utils.REQUEST_CODE_ROTATE);
                break;
            case R.id.edit_f3_double:
                startActivityForResult(new Intent(this, DoubleActivity.class), Utils.REQUEST_CODE_ROTATE);
                break;
            case R.id.edit_f4_adjustment:
                startActivityForResult(new Intent(this, AdjustmentActivity.class), Utils.REQUEST_CODE_ADJUSTMENT);
                break;
            case R.id.edit_f5_autofix:
                startActivityForResult(new Intent(this, AutoFixActivity.class), Utils.REQUEST_CODE_AUTOFIX);
                break;
            case R.id.edit_f6_auto_contrast:
                Toast.makeText(this,"Coming soon",Toast.LENGTH_SHORT).show();
//                startActivityForResult(new Intent(this, AutoContrastActivity.class), Utils.REQUEST_CODE_AUTOCONTRAST);
                break;
            case R.id.edit_f7_blur:
                startActivityForResult(new Intent(this, BlurActivity.class), Utils.REQUEST_CODE_BLUR);
                break;
            case R.id.edit_f8_smooth:
                startActivityForResult(new Intent(this, SmoothActivity.class), Utils.REQUEST_CODE_AUTOFIX);
                break;
            case R.id.edit_f9_sharpen:
                startActivityForResult(new Intent(this, SharpenActivity.class), Utils.REQUEST_CODE_AUTOFIX);
                break;
            case R.id.edit_f10_splash:
                startActivityForResult(new Intent(this, SharpenActivity.class), Utils.REQUEST_CODE_AUTOFIX);
                break;

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateUndoRedoButton();
    }

    public void updateUndoRedoButton() {
        int size = historyBitmaps.size();
        if (size > 1) {
            //show undo/redo
            toolbar_undo_iv.setVisibility(View.VISIBLE);
            toolbar_redo_iv.setVisibility(View.VISIBLE);
            if (currentHistoryPos > size - 2) {
                //disable undo
                toolbar_undo_iv.setEnabled(false);
                toolbar_undo_iv.setAlpha(0.5f);
            } else {
                toolbar_undo_iv.setEnabled(true);
                toolbar_undo_iv.setAlpha(1f);
            }
            if (currentHistoryPos < 1) {
                //disable redo
                toolbar_redo_iv.setEnabled(false);
                toolbar_redo_iv.setAlpha(0.5f);
            } else {
                toolbar_redo_iv.setEnabled(true);
                toolbar_redo_iv.setAlpha(1f);
            }
        } else {
            //hide undo/redo
            toolbar_undo_iv.setVisibility(View.GONE);
            toolbar_redo_iv.setVisibility(View.GONE);
        }
    }

    void showHidePanel(int n) {
        bottom_bar_second1.setVisibility(n == 0 ? View.VISIBLE : View.GONE);
        collage_indicator1.setVisibility(n == 0 ? View.VISIBLE : View.GONE);
//        bottom_bar_second2.setVisibility(n == 1 ? View.VISIBLE : View.GONE);
//        collage_indicator2.setVisibility(n == 1 ? View.VISIBLE : View.GONE);
//        bottom_bar_second3.setVisibility(n == 2 ? View.VISIBLE : View.GONE);
//        collage_indicator3.setVisibility(n == 2 ? View.VISIBLE : View.GONE);
//        bottom_bar_second4.setVisibility(n == 3 ? View.VISIBLE : View.GONE);
//        collage_indicator4.setVisibility(n == 3 ? View.VISIBLE : View.GONE);
//        collage_indicator5.setVisibility(n == 4 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == Utils.REQUEST_CODE_CROP) {
        if (resultCode == RESULT_OK) {
            edit_main_iv.setImageBitmap(historyBitmaps.get(currentHistoryPos));
        }
    }

    @Override
    public void onClickBack() {
        Utils.showAlertDialog(this, "Photo not saved!", "Are you sure to delete this photo!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                historyBitmaps.clear();
                currentHistoryPos = 0;
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        onClickBack();
    }
}
