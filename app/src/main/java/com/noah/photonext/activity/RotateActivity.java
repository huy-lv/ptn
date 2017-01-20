package com.noah.photonext.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noah.photonext.R;
import com.noah.photonext.base.BaseEditActivity;
import com.noah.photonext.custom.LLayout;
import com.noah.photonext.custom.StartSeekBar;

import butterknife.BindView;

import static com.noah.photonext.util.Utils.currentHistoryPos;
import static com.noah.photonext.util.Utils.historyBitmaps;

/**
 * Created by HuyLV-CT on 09-Dec-16.
 */

public class RotateActivity extends BaseEditActivity implements View.OnClickListener {

    @BindView(R.id.rotate_main_iv)
    ImageView rotate_main_iv;
    @BindView(R.id.rotate_straighten)
    LLayout rotate_straighten;
    @BindView(R.id.rotate_horizontal)
    LLayout rotate_horizontal;
    @BindView(R.id.rotate_vertical)
    LLayout rotate_vertical;
    @BindView(R.id.rotate_rotate)
    LLayout rotate_rotate;
    @BindView(R.id.adjustment_seekbar_tv)
    TextView adjustment_slider_tv;


    private Matrix mMatrix;
    private float mScale;
    private float newX;
    private float newY;
    private float angle;
    private Matrix matrix;
    private Matrix rotate_animation;

    Bitmap currentBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentBitmap = historyBitmaps.get(currentHistoryPos).copy(historyBitmaps.get(currentHistoryPos).getConfig(),true);
        rotate_main_iv.setImageBitmap(currentBitmap);

        rotate_straighten.setSelected();
        rotate_horizontal.setOnClickListener(this);
        rotate_rotate.setOnClickListener(this);
        rotate_straighten.setOnClickListener(this);
        rotate_vertical.setOnClickListener(this);
//        matrix = rotate_main_iv.getImageMatrix();
//        if (mMatrix == null) {
//            mMatrix = new Matrix(matrix);
//        }
//        matrix = new Matrix(mMatrix);

        navigation_seek_bar.setAbsoluteMinMaxValue(-50,50);
        navigation_seek_bar.setOnSeekBarChangeListener(new StartSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onSeekBarValueChange(StartSeekBar bar, int angle) {
                adjustment_slider_tv.setText(String.valueOf(angle));

                float width = rotate_main_iv.getDrawable().getIntrinsicWidth();
                float height = rotate_main_iv.getDrawable().getIntrinsicHeight();

                if (width > height) {
                    width = rotate_main_iv.getDrawable().getIntrinsicHeight();
                    height = rotate_main_iv.getDrawable().getIntrinsicWidth();
                }

                float a = (float) Math.atan(height / width);

                // the length from the center to the corner of the green
                float len1 = (width / 2) / (float) Math.cos(a - Math.abs(Math.toRadians(angle)));
                // the length from the center to the corner of the black
                float len2 = (float) Math.sqrt(Math.pow(width / 2, 2) + Math.pow(height / 2, 2));
                // compute the scaling factor
                mScale = len2 / len1;

                matrix = rotate_main_iv.getImageMatrix();
                if (mMatrix == null) {
                    mMatrix = new Matrix(matrix);
                }
                matrix = new Matrix(mMatrix);

                newX = (rotate_main_iv.getWidth() / 2) * (1 - mScale);
                newY = (rotate_main_iv.getHeight() / 2) * (1 - mScale);
                matrix.postScale(mScale, mScale);
                matrix.postTranslate(newX, newY);
                matrix.postRotate(angle, rotate_main_iv.getWidth() / 2, rotate_main_iv.getHeight() / 2);
                rotate_main_iv.setImageMatrix(matrix);
            }

            @Override
            public void onStartTrackingTouch(StartSeekBar bar) {
                adjustment_slider_tv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(StartSeekBar bar, int angle) {
                adjustment_slider_tv.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //resize imageview
        float width = rotate_main_iv.getDrawable().getIntrinsicWidth();
        float height = rotate_main_iv.getDrawable().getIntrinsicHeight();
        float imagevieww = rotate_main_iv.getWidth();
        float imageviewh = rotate_main_iv.getHeight();
        Log.e("cxz", "imagew " + width + "h " + height + " " + imagevieww + " " + imageviewh);
        float scale;

        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) rotate_main_iv.getLayoutParams();
        if (width / height > imagevieww / imageviewh) {
            p.height = (int) (height * imagevieww / width);
            scale = imagevieww / width;
        } else {
            p.width = (int) (width * imageviewh / height);
            scale = imageviewh / height;
        }
        rotate_main_iv.setLayoutParams(p);
        Matrix m = new Matrix();
        m.postScale(scale, scale);
        rotate_main_iv.setImageMatrix(m);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rotate_horizontal:
                Matrix mm = new Matrix();
                mm.preScale(-1, 1);
                currentBitmap = Bitmap.createBitmap(currentBitmap, 0, 0, currentBitmap.getWidth(), currentBitmap.getHeight(), mm, true);
                rotate_main_iv.setImageBitmap(currentBitmap);
//                matrix.postScale(-1,1);
//                rotate_main_iv.setImageMatrix(matrix);
                break;
            case R.id.rotate_vertical:
                Matrix mmm = new Matrix();
                mmm.preScale(1, -1);
                currentBitmap = Bitmap.createBitmap(currentBitmap, 0, 0, currentBitmap.getWidth(), currentBitmap.getHeight(), mmm, true);
                rotate_main_iv.setImageBitmap(currentBitmap);
                break;
            case R.id.rotate_rotate:
                rotate_animation = new Matrix();
                rotate_animation.postRotate(90);
                currentBitmap = Bitmap.createBitmap(currentBitmap, 0, 0, currentBitmap.getWidth(), currentBitmap.getHeight(), rotate_animation, true);

                RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) rotate_main_iv.getLayoutParams();
                if(rotate_main_iv.getWidth()!=rotate_main_iv.getHeight()){
                    p.height = rotate_main_iv.getWidth();
                    p.width = rotate_main_iv.getHeight();
                }
                rotate_main_iv.setLayoutParams(p);
                rotate_main_iv.setImageBitmap(currentBitmap);


//                Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
//                rotate.setFillAfter(true);
//                rotate.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//                    }
//                });
//                rotate_main_iv.startAnimation(rotate);
                break;
            case R.id.rotate_straighten:

                break;
        }
    }

    @Override
    protected void onNavigationDone() {
        super.onNavigationDone();

        int w = currentBitmap.getWidth();
        int h = currentBitmap.getHeight();

        if(navigation_seek_bar.getProgress()!=0) {
        /*//rotate bitmap?
        Bitmap rotatedBitmap = rotateBitmap(currentBitmap, (int) angle);
//        Bitmap zoomedBitmap = zoomBitmap(rotatedBitmap,mScale);
        int left = (rotatedBitmap.getWidth()-w)/2;
        int top = (rotatedBitmap.getHeight()-h)/2;
        Bitmap croppedBitmap = Bitmap.createBitmap(rotatedBitmap,left,top,w,h);*/

            Bitmap temp = Bitmap.createBitmap(currentBitmap, 0, 0, w, h, matrix, true);
            int neww = temp.getWidth();
            int newh = temp.getHeight();
            angle = (float) Math.toRadians(angle);

            double a1a2[] = calculatea1a2(neww, newh);
//        Log.e("cxz","e"+a1a2[0] + " "+ a1a2[1] + " "+ w + " "+h);
            double b1 = a1a2[1] * Math.tan(angle);
            double b2 = a1a2[0] / Math.tan(angle);
            double e = Math.sqrt(a1a2[1] * a1a2[1] + b1 * b1);
            double f = Math.sqrt(a1a2[0] * a1a2[0] + b2 * b2);

            float beta = (float) Math.atan(e / f);
//        Log.e("cxz","beta = "+beta);
            double cd[] = calculateCD(angle, beta, e);
//        Log.e("cxz","cd = "+cd[0] +" "+ cd[1] +" "+ (cd[0]/cd[1]) +" " );

            int left = (int) ((neww - cd[0]) / 2);
            int top = (int) ((newh - cd[1]) / 2);
            currentBitmap = Bitmap.createBitmap(temp, left, top, (int) cd[0], (int) cd[1]);
        }

        historyBitmaps.add(0,currentBitmap);
        setResult(RESULT_OK);
        finish();
    }

    private Bitmap zoomBitmap(Bitmap m,float scale){
        int neww = (int) (m.getWidth()*scale);
        int newh = (int) (m.getHeight()*scale);
        return Bitmap.createScaledBitmap(m,neww,newh,false);
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int rotationAngleDegree){
        double ww = bitmap.getWidth()*mScale;
        double hh = bitmap.getHeight()*mScale;
        bitmap  = Bitmap.createScaledBitmap(bitmap,(int)ww,(int)hh,false);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int newW=w, newH=h;
        if (rotationAngleDegree==90 || rotationAngleDegree==270){
            newW = h;
            newH = w;
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(newW,newH, bitmap.getConfig());
        Canvas canvas = new Canvas(rotatedBitmap);

        Rect rect = new Rect(0,0,newW, newH);
        float px = rect.exactCenterX();
        float py = rect.exactCenterY();
        Matrix matrix = new Matrix();
        matrix.postTranslate(-bitmap.getWidth()/2, -bitmap.getHeight()/2);
        matrix.postRotate(angle);
        matrix.postTranslate(px, py);
        canvas.drawBitmap(bitmap, matrix, new Paint( Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG ));
//        matrix.reset();

        return rotatedBitmap;
    }

    private double[] calculateCD(float angle, float beta, double e) {
        double c= (e*Math.tan(beta))/(Math.sin(angle+beta)*Math.sqrt(Math.pow(Math.tan(beta),2)+1));
        double d = c/Math.tan(beta);
        return new double[]{c,d};
    }


    double[] calculatea1a2(double a, double b){
        double []xy = new double[2];
        double cot = 1/Math.tan(angle);
        //x
        xy[0] = (b*cot-a)/(cot*cot-1);
        //y
        xy[1] = (a*cot*cot -b*cot)/(cot*cot-1);

        return xy;
    }
    @Override
    protected int getToolbarTitleId() {
        return R.string.rotate;
    }
    @Override
    public int isShowSlider() {
        return View.VISIBLE;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rotate;
    }
}
