package com.noah.photonext.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.noah.photonext.MainActivity;
import com.noah.photonext.R;
import com.noah.photonext.base.BaseEditActivity;
import com.noah.photonext.custom.LLayout;
import com.noah.photonext.custom.SMat;
import com.noah.photonext.custom.Touch2;
import com.noah.photonext.util.Utils;

import org.opencv.core.Scalar;

import butterknife.BindView;

import static com.noah.photonext.util.Utils.INTENT_KEY_DOUBLE_PATH;

/**
 * Created by HuyLV-CT on 12-Jan-17.
 */

public class DoubleActivity extends BaseEditActivity implements View.OnClickListener {

    @BindView(R.id.double_main_iv)
    ImageView double_main_iv;
    @BindView(R.id.double_top_iv)
    ImageView double_top_iv;
    @BindView(R.id.double_pick_photo_iv)
    ImageView double_pick_photo_iv;
    @BindView(R.id.double_normal)
    LLayout double_normal;
    @BindView(R.id.double_white)
    LLayout double_white;
    @BindView(R.id.double_black)
    LLayout double_black;
    private Bitmap tempBitmap;
    private Bitmap temp2Bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigation_seek_bar.setAbsoluteMinMaxValue(0, 100);
        navigation_seek_bar.setProgress(100);
        double_main_iv.setImageBitmap(Utils.historyBitmaps.get(Utils.currentHistoryPos));

        double_pick_photo_iv.setOnClickListener(this);
        double_normal.setOnClickListener(this);
        double_black.setOnClickListener(this);
        double_white.setOnClickListener(this);
    }

    @Override
    protected void onSeekBarTracking(int value) {
        super.onSeekBarTracking(value);
        double_top_iv.setAlpha((float) value / 100);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.double_pick_photo_iv:
            case R.id.double_browse:
                Intent i = new Intent(DoubleActivity.this, PickPhotoActivity.class);
                i.putExtra(Utils.INTENT_KEY_PICK_DOUBLE, "1");
                startActivityForResult(i, Utils.REQUEST_CODE_PICK_DOUBLE);
                break;
            case R.id.double_black:

                break;
            case R.id.double_white:
                SMat mat1 = new SMat();
                org.opencv.android.Utils.bitmapToMat(tempBitmap, mat1);
                temp2Bitmap = tempBitmap.copy(Bitmap.Config.ARGB_8888,true);
                SMat mat2 = new SMat(mat1.size(),mat1.type(),new Scalar(0,0,255,255));
//                mat1.clearBackground(mat2);
                MainActivity.native_clearBackground2(mat1.nativeObj,mat2.nativeObj);
                org.opencv.android.Utils.matToBitmap(mat2, temp2Bitmap);
                temp2Bitmap.setHasAlpha(true);
                double_top_iv.setImageBitmap(temp2Bitmap);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.REQUEST_CODE_PICK_DOUBLE) {
            if (resultCode == Activity.RESULT_OK) {
                double_pick_photo_iv.setVisibility(View.GONE);
//                Picasso.with(this).load(PREPATH + data.getStringExtra(INTENT_KEY_DOUBLE_PATH)).into(double_top_iv);
                tempBitmap = BitmapFactory.decodeFile(data.getStringExtra(INTENT_KEY_DOUBLE_PATH));
                double_top_iv.setImageBitmap(tempBitmap);
                double_top_iv.setOnTouchListener(new Touch2());
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //resize imageview
        float width = double_main_iv.getDrawable().getIntrinsicWidth();
        float height = double_main_iv.getDrawable().getIntrinsicHeight();
        float imagevieww = double_main_iv.getWidth();
        float imageviewh = double_main_iv.getHeight();
        Log.e("cxz", "imagew " + width + "h " + height + " " + imagevieww + " " + imageviewh);
        float scale;

        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) double_main_iv.getLayoutParams();
        if (width / height > imagevieww / imageviewh) {
            p.height = (int) (height * imagevieww / width);
            scale = imagevieww / width;
        } else {
            p.width = (int) (width * imageviewh / height);
            scale = imageviewh / height;
        }
        double_main_iv.setLayoutParams(p);
        double_top_iv.setLayoutParams(p);
        Matrix m = new Matrix();
        m.postScale(scale, scale);
        double_main_iv.setImageMatrix(m);
    }

    @Override
    protected int getToolbarTitleId() {
        return R.string.doublee;
    }

    @Override
    protected int isShowSlider() {
        return View.VISIBLE;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_double;
    }

}
