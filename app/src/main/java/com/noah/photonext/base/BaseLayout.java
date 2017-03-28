package com.noah.photonext.base;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.noah.photonext.custom.ShapedImageView;
import com.noah.photonext.custom.StandardLayout;
import com.noah.photonext.model.BackgroundValue;

import java.util.ArrayList;

/**
 * Created by HuyLV-CT on 21-Nov-16.
 */

public abstract class BaseLayout extends FrameLayout {

    public ArrayList<ShapedImageView> currentIVlist;

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

    public abstract void setImageForUnassignedView(int unassignPos);

    public void changeBackgroundColor(BackgroundValue backgroundValue) {
        if (backgroundValue.color != null) { // color
//            if (this instanceof StandardLayout) {
            for (LinearLayout ll : ((StandardLayout) this).getBackgroundList()) {
                ll.setBackgroundColor(Color.parseColor(backgroundValue.color));
            }
//            }
        } else {//photo
            for (LinearLayout ll : ((StandardLayout) this).getBackgroundList()) {
                BitmapDrawable b = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), backgroundValue.imageId));
                b.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                ll.setBackground(b);
            }

        }
    }
}
