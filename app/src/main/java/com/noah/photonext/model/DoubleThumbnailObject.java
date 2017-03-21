package com.noah.photonext.model;

import android.graphics.Bitmap;

/**
 * Created by huylv on 16-Mar-17.
 */

public class DoubleThumbnailObject {
    public Bitmap bitmap;
    public String text;

    public DoubleThumbnailObject(Bitmap bitmap, String text) {
        this.bitmap = bitmap;
        this.text = text;
    }
}
