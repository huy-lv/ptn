package com.noah.photonext.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by HuyLV-CT on 10-Feb-17.
 */

public class EView extends View {
    public float rotation;
    int sw, sh;
    Point pos1;
    Line line1, dash1, line2, dash2;
    int distance1, distance2;
    Paint paint1, paint2;

    int lineWidth = 600;
    private double huyen1, huyen2;
    private double beta;
    private boolean draggingCircle, draggingLine1, draggingDash1, rotating;
    private float oneRadian = (float) Math.toRadians(1);
    private int lastX, lastY;

    public EView(Context context) {
        super(context);
        init();
    }

    public EView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {
        pos1 = new Point();
        pos1.x = 10;
        pos1.y = 10;
        line1 = new Line(new Point(100, 200), new Point(500, 200));
        dash1 = new Line(new Point(100, 200), new Point(500, 200));
        line2 = new Line(new Point(100, 200), new Point(500, 200));
        dash2 = new Line(new Point(100, 200), new Point(500, 200));

        distance1 = 100;
        distance2 = 200;

        rotation = 0;
        distance1 = 50;
        distance2 = 100;
        paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        paint1.setStrokeWidth(5);

        paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setStrokeWidth(5);
        paint2.setPathEffect(new DashPathEffect(new float[]{20, 10}, 0));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x < pos1.x + 20 && x > pos1.x - 20 && y < pos1.y + 20 && y > pos1.y - 20) {
                    //click on circle
                    draggingCircle = true;
                } else {
                    rotating = true;
                    lastX = x;
                    lastY = y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (draggingCircle) {
                    updatePosition(x, y);
                    invalidate();
                } else if (rotating) {
                    rotate(lastX, lastY, x, y);
                    lastX = x;
                    lastY = y;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                draggingCircle = false;
                rotating = false;
                break;
        }
        return true;
    }

    private void updatePosition(int x, int y) {
        pos1.x = x;
        pos1.y = y;

        line1.start.x = pos1.x - line1.start_delta.x;
        line1.start.y = pos1.y - line1.start_delta.y;

        line1.end.x = pos1.x - line1.end_delta.x;
        line1.end.y = pos1.y - line1.end_delta.y;

        line2.start.x = pos1.x - line2.start_delta.x;
        line2.start.y = pos1.y - line2.start_delta.y;

        line2.end.x = pos1.x - line2.end_delta.x;
        line2.end.y = pos1.y - line2.end_delta.y;

        dash1.start.x = pos1.x - dash1.start_delta.x;
        dash1.start.y = pos1.y - dash1.start_delta.y;

        dash1.end.x = pos1.x - dash1.end_delta.x;
        dash1.end.y = pos1.y - dash1.end_delta.y;

        dash2.start.x = pos1.x - dash2.start_delta.x;
        dash2.start.y = pos1.y - dash2.start_delta.y;

        dash2.end.x = pos1.x - dash2.end_delta.x;
        dash2.end.y = pos1.y - dash2.end_delta.y;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(line1.start.x, line1.start.y, line1.end.x, line1.end.y, paint1);
        canvas.drawLine(dash1.start.x, dash1.start.y, dash1.end.x, dash1.end.y, paint2);

        canvas.drawLine(line2.start.x, line2.start.y, line2.end.x, line2.end.y, paint1);
        canvas.drawLine(dash2.start.x, dash2.start.y, dash2.end.x, dash2.end.y, paint2);

        canvas.drawCircle(pos1.x, pos1.y, 20, paint1);
    }

    private void rotate(int lastX, int lastY, int x, int y) {
        Point v1 = new Point(x - pos1.x, y - pos1.y);
        Point v2 = new Point(lastX - pos1.x, lastY - pos1.y);
//        float radian = Math.acos()
        if (v1.x * v2.y - v1.y * v2.x < 0) {
            rotation += oneRadian;
        } else {
            rotation -= oneRadian;
        }

        int ax = (int) (huyen1 * Math.sin(beta + rotation));
        int ay = (int) (huyen1 * Math.cos(beta + rotation));
        line1.end.x = pos1.x + ax;
        line1.end.y = pos1.y - ay;
        ax = (int) (huyen1 * Math.sin(beta - rotation));
        ay = (int) (huyen1 * Math.cos(beta - rotation));
        line1.start.x = pos1.x - ax;
        line1.start.y = pos1.y - ay;

        //update start,end delta
        line1.start_delta.x = pos1.x - line1.start.x;
        line1.start_delta.y = pos1.y - line1.start.y;
        line1.end_delta.x = pos1.x - line1.end.x;
        line1.end_delta.y = pos1.y - line1.end.y;

        line2.start_delta.x = pos1.x - line2.start.x;
        line2.start_delta.y = pos1.y - line2.start.y;
        line2.end_delta.x = pos1.x - line2.end.x;
        line2.end_delta.y = pos1.y - line2.end.y;

        dash1.start_delta.x = pos1.x - dash1.start.x;
        dash1.start_delta.y = pos1.y - dash1.start.y;
        dash1.end_delta.x = pos1.x - dash1.end.x;
        dash1.end_delta.y = pos1.y - dash1.end.y;

        dash2.start_delta.x = pos1.x - dash2.start.x;
        dash2.start_delta.y = pos1.y - dash2.start.y;
        dash2.end_delta.x = pos1.x - dash2.end.x;
        dash2.end_delta.y = pos1.y - dash2.end.y;
    }

    public void setScreenSize(int w, int h) {
        sw = w;
        sh = h;
        pos1.x = sw / 2;
        pos1.y = sh / 2;
        line1.start.x = line2.start.x = dash1.start.x = dash2.start.x = pos1.x - lineWidth / 2;

        line1.start.y = pos1.y - distance1;
        line1.end.y = line1.start.y;
        line1.end.x = line2.end.x = dash1.end.x = dash2.end.x = line1.start.x + lineWidth;

        dash1.start.y = pos1.y - distance2;
        dash1.end.y = dash1.start.y;

        line2.start.y = pos1.y + distance1;
        line2.end.y = line2.start.y;
        dash2.start.y = pos1.y + distance2;
        dash2.end.y = dash2.start.y;

        huyen1 = Math.sqrt(distance1 * distance1 + Math.pow(lineWidth / 2, 2));
        huyen2 = Math.sqrt(distance2 * distance2 + Math.pow(lineWidth / 2, 2));
        rotation = 0;
        beta = Math.atan(lineWidth / (2 * distance1));

        //update start,end delta
        line1.start_delta.x = pos1.x - line1.start.x;
        line1.start_delta.y = pos1.y - line1.start.y;
        line1.end_delta.x = pos1.x - line1.end.x;
        line1.end_delta.y = pos1.y - line1.end.y;

        line2.start_delta.x = pos1.x - line2.start.x;
        line2.start_delta.y = pos1.y - line2.start.y;
        line2.end_delta.x = pos1.x - line2.end.x;
        line2.end_delta.y = pos1.y - line2.end.y;

        dash1.start_delta.x = pos1.x - dash1.start.x;
        dash1.start_delta.y = pos1.y - dash1.start.y;
        dash1.end_delta.x = pos1.x - dash1.end.x;
        dash1.end_delta.y = pos1.y - dash1.end.y;

        dash2.start_delta.x = pos1.x - dash2.start.x;
        dash2.start_delta.y = pos1.y - dash2.start.y;
        dash2.end_delta.x = pos1.x - dash2.end.x;
        dash2.end_delta.y = pos1.y - dash2.end.y;
    }
}
