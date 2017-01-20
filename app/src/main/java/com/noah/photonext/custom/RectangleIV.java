package com.noah.photonext.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;

import com.noah.photonext.R;


/**
 * Created by HuyLV-CT on 01-Dec-16.
 */

public class RectangleIV extends ShapedImageView {

    private int cornerRadius;
    private int half;

    public RectangleIV(Context context) {
        super(context);
        init(null);
    }

    public RectangleIV(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RectangleIV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RectangleIV);
            cornerRadius = (int) typedArray.getDimension(R.styleable.RectangleIV_rect_corner, 0);
            typedArray.recycle();
        }

    }

    @Override
    protected void buildBorderPath(Path mBorderPath, int w, int h) {
        Point p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12;
        half = getBorderWidth()/2;
        p1 = new Point(half, half);
        p2 = new Point(half,cornerRadius);
        p3 = new Point(cornerRadius,half);

        p4 = new Point(w-half,half);
        p5 = new Point(w-cornerRadius,half);
        p6 = new Point(w-half,cornerRadius);

        p7 = new Point(w-half,h-half);
        p8 = new Point(w-cornerRadius,h-half);
        p9 = new Point(w-half,h-cornerRadius);

        p10 = new Point(half,h-half);
        p11 = new Point(half,h-cornerRadius);
        p12 = new Point(cornerRadius,h-half);
        mBorderPath.reset();
        mBorderPath.moveTo(p2.x,p2.y);
        mBorderPath.quadTo(p1.x,p1.y,p3.x,p3.y);
        mBorderPath.lineTo(p5.x,p5.y);
        mBorderPath.quadTo(p4.x,p4.y,p6.x,p6.y);
        mBorderPath.lineTo(p9.x,p9.y);
        mBorderPath.quadTo(p7.x,p7.y,p8.x,p8.y);
        mBorderPath.lineTo(p12.x,p12.y);
        mBorderPath.quadTo(p10.x,p10.y,p11.x,p11.y);
        mBorderPath.close();

    }

    @Override
    protected void buildPath(Path mPath, int w, int h) {
        Point p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12;
        p1 = new Point(0, 0);
        p2 = new Point(0,cornerRadius);
        p3 = new Point(cornerRadius,0);

        p4 = new Point(w,0);
        p5 = new Point(w-cornerRadius,0);
        p6 = new Point(w,cornerRadius);

        p7 = new Point(w,h);
        p8 = new Point(w-cornerRadius,h);
        p9 = new Point(w,h-cornerRadius);

        p10 = new Point(0,h);
        p11 = new Point(0,h-cornerRadius);
        p12 = new Point(cornerRadius,h);

        mPath.reset();
        mPath.moveTo(p1.x, p1.y);
        mPath.lineTo(p2.x, p2.y);
        mPath.quadTo(p1.x,p1.y,p3.x,p3.y);
        mPath.close();

        mPath.moveTo(p4.x,p4.y);
        mPath.lineTo(p5.x,p5.y);
        mPath.quadTo(p4.x,p4.y,p6.x,p6.y);
        mPath.close();

        mPath.moveTo(p7.x,p7.y);
        mPath.lineTo(p8.x,p8.y);
        mPath.quadTo(p7.x,p7.y,p9.x,p9.y);
        mPath.close();

        mPath.moveTo(p10.x,p10.y);
        mPath.lineTo(p11.x,p11.y);
        mPath.quadTo(p10.x,p10.y,p12.x,p12.y);
        mPath.close();

    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int r) {
        cornerRadius = r;
    }

}
