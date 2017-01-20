package com.noah.photonext.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.noah.photonext.util.FontManager;

/**
 * Created by HuyLV-CT on 06-Dec-16.
 */

public class FontText extends TextView {
    public FontText(Context context) {
        this(context, null);
    }

    public FontText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
        if (isInEditMode())
            return;


    }

    private void init() {
        setTextColor(Color.WHITE);
        String fontAsset = "fonts/utmavo.ttf" ;
        if (!fontAsset.equals("")) {
            FontManager.init(getContext().getAssets());
            Typeface tf = FontManager.getInstance().getFont(fontAsset);
            int style = Typeface.NORMAL;

            if (getTypeface() != null)
                style = getTypeface().getStyle();

            if (tf != null)
                setTypeface(tf, style);
            else
                Log.d("FontText", String.format("Could not create a font from asset: %s", fontAsset));
        }
    }

}