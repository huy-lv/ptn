package com.noah.photonext.base;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.noah.photonext.R;
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

    public void changeBackgroundColor(boolean isTypeColor, BackgroundValue backgroundValue) {
        if (isTypeColor) { // color
            if (backgroundValue.color != null) {
                changeBackgroundColorBy(Color.parseColor(backgroundValue.color));
            } else {
                openColorPicker();
            }
        } else {//photo
            for (LinearLayout ll : ((StandardLayout) this).getBackgroundList()) {
                BitmapDrawable b = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), backgroundValue.imageId));
                b.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                ll.setBackground(b);
            }

        }
    }

    void changeBackgroundColorBy(int color) {
        for (LinearLayout ll : ((StandardLayout) this).getBackgroundList()) {
            ll.setBackgroundColor(color);
        }
    }

    private void openColorPicker() {
        ColorPickerDialogBuilder
                .with(getContext())
                .setTitle("Choose color")
                .initialColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
//                        Toast.makeText(getContext(), "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("OK", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        changeBackgroundColorBy(selectedColor);
                    }
                })
                .setNegativeButton("CANCEL", null)
                .build()
                .show();
    }
}
