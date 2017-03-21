package com.noah.photonext.model;

/**
 * Created by huylv on 14-Mar-17.
 */

public class LayoutObject {
    //    public int layoutId;
    public int imageId;
    public int first;
    public int second;
    public boolean standard;
    public boolean selected;

    public LayoutObject(int imageId) {
        this.imageId = imageId;
    }

    public LayoutObject(int imageId, int f, int s, boolean ss) {
        this.imageId = imageId;
        first = f;
        second = s;
        standard = ss;
    }

}
