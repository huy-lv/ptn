package com.noah.photonext.custom;

import android.graphics.Point;

/**
 * Created by HuyLV-CT on 11-Feb-17.
 */

public class Line {
    public Point start;
    public Point end;
    public Point start_delta;
    public Point end_delta;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
        start_delta = new Point();
        end_delta = new Point();
    }
}
