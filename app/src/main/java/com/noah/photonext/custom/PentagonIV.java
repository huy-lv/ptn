package com.noah.photonext.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;

import com.noah.photonext.R;


/**
 * Created by HuyLV-CT on 23-Nov-16.
 */

public class PentagonIV extends ShapedImageView {
    private static final int DEFAULT_RANGE = 100;
    private int mRange;
    private DIRECTION mDirection;
    private DIRECTION mHalf;
    private int half;


    public int getmRange() {
        return mRange;
    }

    public void setmRange(int mRange) {
        this.mRange = mRange;
    }

    public PentagonIV(Context context) {
        super(context);
        mRange = DEFAULT_RANGE;
        mDirection = DIRECTION.LEFT;
    }

    public PentagonIV(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PentagonIV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PentagonIV);
            mRange =(int) typedArray.getDimension(R.styleable.PentagonIV_pen_range, 100);
            switch (typedArray.getInt(R.styleable.PentagonIV_pen_direction, 4)) {
                case 1:
                    mDirection = DIRECTION.UP;
                    break;
                case 2:
                    mDirection = DIRECTION.DOWN;
                    break;
                case 3:
                    mDirection = DIRECTION.LEFT;
                    break;
                case 4:
                    mDirection = DIRECTION.RIGHT;
                    break;
            }
            switch (typedArray.getInt(R.styleable.PentagonIV_pen_half, 0)) {
                case 1:
                    mHalf = DIRECTION.UP;
                    break;
                case 2:
                    mHalf = DIRECTION.DOWN;
                    break;
                case 3:
                    mHalf = DIRECTION.LEFT;
                    break;
                case 4:
                    mHalf = DIRECTION.RIGHT;
                    break;
                default:
                    mHalf = DIRECTION.NONE;
            }
            typedArray.recycle();
        } else {
            mRange = DEFAULT_RANGE;
        }

        half = getBorderWidth()/2;
    }

    @Override
    protected void buildPath(Path mPath, int width, int height) {
        Point p1, p2, p3, p4, p5;
        mRange = width/4;
        if (mHalf == DIRECTION.NONE) {
            switch (mDirection) {
                case UP:
                    p1 = new Point(0, 0);
                    p2 = new Point(0, mRange);
                    p3 = new Point(width/2, 0);
                    p4 = new Point(width, mRange);
                    p5 = new Point(width,0);
                    break;
                case DOWN:
                    p1 = new Point(0, (height-mRange));
                    p2 = new Point(width/2, height);
                    p3 = new Point(width , height-mRange);
                    p4 = new Point(width, height );
                    p5 = new Point( 0,height);
                    break;
                case LEFT:
                    p1 = new Point(0, 0);
                    p2 = new Point(mRange, 0);
                    p3 = new Point(0,height/2);
                    p4 = new Point(mRange, height);
                    p5 = new Point( 0,height);
                    break;
                case RIGHT:
                default:
                    p1 = new Point((width - mRange), 0);
                    p2 = new Point(width, 0);
                    p3 = new Point(width, height);
                    p4 = new Point((width - mRange), height);
                    p5 = new Point(width, height / 2);
                    break;
            }
            mPath.reset();
            mPath.moveTo(p1.x, p1.y);
            mPath.lineTo(p2.x, p2.y);
            mPath.lineTo(p3.x, p3.y);
            mPath.lineTo(p4.x, p4.y);
            mPath.lineTo(p5.x, p5.y);
            mPath.close();
        } else {
            switch (mHalf) {
                case UP:
                    if (mDirection == DIRECTION.LEFT) {
                        p1 = new Point(0, 0);
                        p2 = new Point(mRange,0);
                        p3 = new Point(0, height);
                    } else {
                        p1 = new Point(width-mRange, 0);
                        p2 = new Point(width, 0);
                        p3 = new Point(width, height);
                    }
                    break;
                case DOWN:
                default:
                    if (mDirection == DIRECTION.LEFT) {
                        p1 = new Point(0, 0);
                        p2 = new Point(mRange, height);
                        p3 = new Point(0, height);
                    } else {
                        p1 = new Point(width, 0);
                        p2 = new Point(width, height);
                        p3 = new Point(width - mRange, height);
                    }
                    break;
            }
            mPath.reset();
            mPath.moveTo(p1.x, p1.y);
            mPath.lineTo(p2.x, p2.y);
            mPath.lineTo(p3.x, p3.y);
            mPath.close();
        }
    }

    @Override
    protected void buildBorderPath(Path mBorderPath, int w, int h) {
        Point p1, p2, p3, p4, p5;
        if (mHalf == DIRECTION.NONE) {
            switch (mDirection) {
                case UP:
                    p1 = new Point(w/2+half, 0);
                    p2 = new Point(w-half, mRange);
                    p3 = new Point(w-half, h-half);
                    p4 = new Point(half, h-half);
                    p5 = new Point(half,mRange);
                    break;
                case DOWN:
                    p1 = new Point(half, half);
                    p2 = new Point(w-half, half);
                    p3 = new Point(w-half , h-mRange);
                    p4 = new Point(w/2, h-half );
                    p5 = new Point(half,h-mRange);
                    break;
                case LEFT:
                    p1 = new Point(mRange, half);
                    p2 = new Point(w-half, half);
                    p3 = new Point(w-half,h-half);
                    p4 = new Point(mRange, h-half);
                    p5 = new Point(half,h/2);
                    break;
                case RIGHT:
                default:
                    p1 = new Point((w - mRange), half);
                    p2 = new Point(w-half, h/2);
                    p3 = new Point(w-mRange, h-half);
                    p4 = new Point(half, h-half);
                    p5 = new Point(half,half);
                    break;
            }
            mBorderPath.reset();
            mBorderPath.moveTo(p1.x, p1.y);
            mBorderPath.lineTo(p2.x, p2.y);
            mBorderPath.lineTo(p3.x, p3.y);
            mBorderPath.lineTo(p4.x, p4.y);
            mBorderPath.lineTo(p5.x, p5.y);
            mBorderPath.close();
        } else {
            switch (mHalf) {
                case UP:
                    if (mDirection == DIRECTION.LEFT) {
                        p1 = new Point(w-half, half);
                        p2 = new Point(mRange,half);
                        p3 = new Point(half, h-half);
                        p4 = new Point(w-half,h-half);
                    } else {
                        p1 = new Point(w-mRange, half);
                        p2 = new Point(half,half);
                        p3 = new Point(half, h-half);
                        p4 = new Point(w-half,h-half);
                    }
                    break;
                case DOWN:
                default:
                    if (mDirection == DIRECTION.LEFT) {
                        p1 = new Point(half, half);
                        p2 = new Point(mRange, h-half);
                        p3 = new Point(w-half, h-half);
                        p4 = new Point(w-half,half);
                    } else {
                        p1 = new Point(half, half);
                        p2 = new Point(w-half, half);
                        p3 = new Point(w - mRange, h-half);
                        p4 = new Point(half,h-half);
                    }
                    break;
            }
            mBorderPath.reset();
            mBorderPath.moveTo(p1.x, p1.y);
            mBorderPath.lineTo(p2.x, p2.y);
            mBorderPath.lineTo(p3.x, p3.y);
            mBorderPath.lineTo(p4.x,p4.y);
            mBorderPath.close();
        }
    }


    private enum DIRECTION {
        UP, DOWN, LEFT, RIGHT, NONE
    }


}
