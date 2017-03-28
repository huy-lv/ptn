package com.noah.photonext.model;

/**
 * Created by huylv on 28-Mar-17.
 */

public class BackgroundValue {
    public int imageId;
    public String color;

    public BackgroundValue(String color) {
        this.color = color;
    }

    public BackgroundValue(int imageId) {

        this.imageId = imageId;
    }
}
