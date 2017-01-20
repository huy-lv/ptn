package com.noah.photonext.custom;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import static com.noah.photonext.util.Utils.canvasHeight;
import static com.noah.photonext.util.Utils.canvasWidth;

public class Touch2 implements OnTouchListener {
    public Touch2(){

    }

    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int currentMode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    double oldDist = 1f;
    float d = 0f;
    float[] lastEvent = null;
    float newRot = 0f;
    public static float degree = 0.0f;
    private int actW;
    private int actH;
    private int origW;
    private int origH;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;


        view.setScaleType(ImageView.ScaleType.MATRIX);
        // Dump touch event to log
        dumpEvent(event);
        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                canvasWidth = view.getWidth();
                canvasHeight = view.getHeight();
//                Log.e("cxz", "down" + view.getWidth() + " " + view.getHeight());
                start.set(event.getX(), event.getY());
                currentMode = DRAG;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    currentMode = ZOOM;
                }

                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);

                break;

            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_POINTER_UP:
                currentMode = NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                if (currentMode == DRAG) {
                    float[] temp2 = new float[9];
                    float deltaX = event.getX() - start.x;
                    float deltaY = event.getY() - start.y;;
                    savedMatrix.getValues(temp2);
                    matrix.set(savedMatrix);

                    matrix.postTranslate(deltaX, deltaY);
                } else if (currentMode == ZOOM && event.getPointerCount() == 2) {
                    double newDist = spacing(event);
                    Log.v("SS", "Count=" + event.getPointerCount());
                    Log.v("SS", "newDist=" + newDist);
                    matrix.set(savedMatrix);
                    if (newDist > 10f) {
                        float oldDist = (float) this.oldDist;
                        float scale = (float) newDist / oldDist;
                        Drawable d  = view.getDrawable();
                        origW = d.getIntrinsicWidth();
                        origH = d.getIntrinsicHeight();
                        // Calculate the actual dimensions
                        actW = Math.round(origW * scale);
                        actH = Math.round(origH * scale);
                        System.out.println(newDist + " : " + oldDist + " : " + scale + " : " + mid.x + " : " + mid.y +"ow"+origW+ " oh"+origH+" w" + actW + " h" + actH);
                        System.out.println("### Scale Value :: " + scale);
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                    if (lastEvent != null) {
                        newRot = rotation(event);
                        Log.v("Degreeeeeeeeeee", "!!!!!!!!! newRot=" + (newRot));
                        degree = Math.abs(newRot);
                        float r = newRot - d;
                        Log.v("Degreeeeeeeeeee rrrrr ", "!!!!!!!!!!! rotate :: " + r);
                        matrix.postRotate(r, v.getMeasuredWidth() / 2, v.getMeasuredHeight() / 2);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix);
        return true; // indicate event was handled
    }

    /**
     * Show an event in the LogCat view, for debugging
     */
    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Determine the degree between the first two fingers
     */
    private static float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        // if (Constant.TRACE) Log.d("Rotation ~~~~~~~~~~~~~~~~~",
        // delta_x+" ## "+delta_y+" ## "+radians+" ## "+Math.toDegrees(radians));
        return (float) Math.toDegrees(radians);
    }
}