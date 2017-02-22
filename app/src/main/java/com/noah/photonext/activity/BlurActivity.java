package com.noah.photonext.activity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
public class BlurActivity extends BaseEditActivity implements View.OnClickListener {

    @BindView(R.id.blur_blur)
    LLayout blur_blur;
    @BindView(R.id.blur_circular)
    LLayout blur_circular;
    @BindView(R.id.blur_linear)
    LLayout blur_linear;

    SMat srcMat, dstMat;
    @BindView(R.id.blur_control_dash1)
    ImageView blur_control_dash1;
    @BindView(R.id.blur_control_line1)
    ImageView blur_control_line1;
    @BindView(R.id.blur_control_dot)
    ImageView blur_control_dot;
    @BindView(R.id.blur_control_line2)
    ImageView blur_control_line2;
    @BindView(R.id.blur_control_dash2)
    ImageView blur_control_dash2;
    @BindView(R.id.blur_control)
    FrameLayout blur_control;
    @BindView(R.id.blur_control_background)
    FrameLayout blur_control_background;
    @BindView(R.id.blur_root_layout)
    FrameLayout blur_root_layout;

    private Bitmap tempBitmap;
    private int _xDelta;
    private int _yDelta;
    private double mCurrAngle, mPrevAngle, mStartAngle, mPickAngle;
    private int distance1, distance2;
    private int lineWidth;
    private double imgCurrAngle;
    private double imgPrevAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        blur_blur.setOnClickListener(this);
        blur_circular.setOnClickListener(this);
        blur_linear.setOnClickListener(this);
        blur_blur.setSelected();
        navigation_seek_bar.setAbsoluteMinMaxValue(0, 100);
        navigation_seek_bar.setProgress(0);

        edit_main_iv.setImageBitmap(historyBitmaps.get(currentHistoryPos));
        srcMat = new SMat();
        Utils.bitmapToMat(historyBitmaps.get(currentHistoryPos), srcMat);
        dstMat = new SMat(srcMat.size(), srcMat.type(), new Scalar(0, 0, 255, 255));
        tempBitmap = historyBitmaps.get(currentHistoryPos).copy(historyBitmaps.get(currentHistoryPos).getConfig(), true);

        showToolbarButton();

        blur_control_dot.setOnTouchListener(new MoveControlListener() {
            @Override
            void onMove(View view, int x, int y) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) blur_control
                        .getLayoutParams();
                layoutParams.leftMargin = x - _xDelta;
                layoutParams.topMargin = y - _yDelta;
                blur_control.setLayoutParams(layoutParams);
            }
        });
        blur_control.setOnTouchListener(new RotateTouchListener());
        int xx[] = new int[2];
        blur_control_dot.getLocationOnScreen(xx);
        Log.e("cxz", "location " + xx[0] + " " + xx[1]);
    }

    @Override
    protected void onSeekBarStopTracking(int value) {
        super.onSeekBarStopTracking(value);
        if (value % 2 == 0) value++;
        Imgproc.GaussianBlur(srcMat, dstMat, new Size(value, value), 0);
        Utils.matToBitmap(dstMat, tempBitmap);
        edit_main_iv.setImageBitmap(tempBitmap);
    }

    @Override
    protected void onNavigationDone() {
        super.onNavigationDone();
        historyBitmaps.add(0, tempBitmap);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        resetColor(blur_blur, blur_circular, blur_linear);
        switch (v.getId()) {
            case R.id.blur_blur:
                blur_blur.setSelected();
                navigation_seek_bar.setVisibility(View.VISIBLE);
                blur_control_background.setVisibility(View.GONE);
                break;
            case R.id.blur_circular:
                blur_circular.setSelected();
//                navigation_seek_bar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.blur_linear:
                blur_linear.setSelected();
                blur_control_background.setVisibility(View.VISIBLE);
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

    private void startBlurring() {
        int dot[] = new int[2];
        blur_control_dot.getLocationOnScreen(dot);
        int imageInScreen[] = new int[2];
        edit_main_iv.getLocationOnScreen(imageInScreen);
        Drawable d = edit_main_iv.getDrawable();
        Matrix m = edit_main_iv.getImageMatrix();
        float f[] = new float[9];
        m.getValues(f);
        int imagew = (int) (d.getIntrinsicWidth() * f[Matrix.MSCALE_X]);
        int imageh = (int) (d.getIntrinsicHeight() * f[Matrix.MSCALE_Y]);

        float a = (float) d.getIntrinsicWidth() / d.getIntrinsicHeight() - (float) edit_main_iv.getWidth() / edit_main_iv.getHeight();
        if ((float) d.getIntrinsicWidth() / d.getIntrinsicHeight() < (float) edit_main_iv.getWidth() / edit_main_iv.getHeight()) {
            //b/a<c/a
            dot[0] = dot[0] - (edit_main_iv.getWidth() - imagew) / 2 + 20;
            dot[1] = dot[1] - imageInScreen[1] + 20;

            Log.e("cxz", "dot position" + dot[0] + " " + dot[1] + " dash1 " + (dot[1] - 20 - 100));

        } else {
            dot[0] += 20;
            dot[1] = dot[1] - imageInScreen[1] - ((edit_main_iv.getHeight() - imageh) / 2) + 20;
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
/*        edit_draw.setScreenSize(edit_draw.getWidth(), edit_draw.getHeight());*/

        int sw = blur_root_layout.getWidth();
        int sh = blur_root_layout.getHeight();
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) blur_control.getLayoutParams();
        lp.leftMargin = (sw - blur_control.getWidth()) / 2;
        lp.topMargin = (sh - blur_control.getHeight()) / 2;
        blur_control.setLayoutParams(lp);
    }

    private abstract class MoveControlListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent event) {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) blur_control.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                    break;
                case MotionEvent.ACTION_MOVE:
                    onMove(view, X, Y);
                    break;
                case MotionEvent.ACTION_UP:
                    startBlurring();
                    break;
            }
            blur_root_layout.invalidate();
            return true;
        }

        abstract void onMove(View view, int x, int y);
    }

    class RotateTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final float xc = v.getWidth() / 2;
            final float yc = v.getHeight() / 2;

            final float x = event.getX();
            final float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    mPrevAngle = mCurrAngle;
                    mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
                    if (mPrevAngle > mCurrAngle) {
                        double angleRotate = mPrevAngle - mCurrAngle;
                        imgCurrAngle = imgPrevAngle - angleRotate;
                        animate(imgPrevAngle, imgPrevAngle - angleRotate, 0);
                    } else {
                        double angleRotate = mCurrAngle - mPrevAngle;
                        imgCurrAngle = imgPrevAngle + angleRotate;
                        animate(imgPrevAngle, imgPrevAngle + angleRotate, 0);
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    mPrevAngle = mCurrAngle;
                    break;
                }
            }

            return true;
        }


        private void animate(double fromDegrees, double toDegrees, long durationMillis) {
            final RotateAnimation rotate = new RotateAnimation((float) fromDegrees, (float) toDegrees,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(durationMillis);
            rotate.setFillEnabled(true);
            rotate.setFillAfter(true);
            rotate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    imgPrevAngle = imgCurrAngle;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            blur_control.startAnimation(rotate);
        }

    }

}
