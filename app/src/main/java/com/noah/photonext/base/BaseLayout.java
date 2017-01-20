package com.noah.photonext.base;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.noah.photonext.custom.ShapedImageView;

import java.util.ArrayList;

/**
 * Created by HuyLV-CT on 21-Nov-16.
 */

public abstract class BaseLayout extends FrameLayout {

    public ArrayList<ShapedImageView> currentIV;

    public BaseLayout(Context context) {
        super(context);
    }

    public BaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public abstract void setImageForUnassignView(int unassignPos);

}
