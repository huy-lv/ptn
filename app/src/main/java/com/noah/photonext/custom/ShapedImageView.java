package com.noah.photonext.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by HuyLV-CT on 23-Nov-16.
 */

public abstract class ShapedImageView extends ImageView {
    private Paint mPathPaint;
    private Path mPath;
    private boolean isAssigned;
    private Paint mBorderPaint;
    private Path mBorderPath;
    private boolean showBorder;
    private int borderWidth;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    private int pos;

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public ShapedImageView(Context context) {
        super(context);
        init();
    }

    public ShapedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShapedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_HARDWARE, null);
        }
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setFilterBitmap(true);
        mPathPaint.setColor(Color.BLACK);
        mPathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        mPath = new Path();

        borderWidth = 10;
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setColor(Color.WHITE);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(borderWidth);

        mBorderPath = new Path();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            buildPath(mPath,getWidth(),getHeight());
            buildBorderPath(mBorderPath,getWidth(),getHeight());
        }
    }

    protected abstract void buildBorderPath(Path mBorderPath, int width, int height);

    protected abstract void buildPath(Path mPath,int w,int h);


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        buildPath(mPath,canvas.getWidth(),canvas.getHeight());
        canvas.drawPath(mPath, mPathPaint);
        if(showBorder) {
            buildBorderPath(mBorderPath,canvas.getWidth(),canvas.getHeight());
            canvas.drawPath(mBorderPath, mBorderPaint);
        }
    }

    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
        invalidate();
    }

    public boolean isShowBorder() {
        return showBorder;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }
}
