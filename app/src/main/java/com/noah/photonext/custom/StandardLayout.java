package com.noah.photonext.custom;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.noah.photonext.activity.CollageActivity;
import com.noah.photonext.activity.PickPhotoActivity;
import com.noah.photonext.base.BaseLayout;
import com.noah.photonext.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.noah.photonext.util.Utils.PREPATH;
import static com.noah.photonext.util.Utils.currentPhotos;
import static com.noah.photonext.util.Utils.numOfPhoto;

/**
 * Created by HuyLV-CT on 23-Nov-16.
 */

public class StandardLayout extends BaseLayout implements View.OnClickListener {
    CollageActivity context;
    private Touch touchListener;

    public StandardLayout(CollageActivity context, int first, int second) {
        super(context);
        this.context = context;
        init(first,second);
    }

    public StandardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StandardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StandardLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    void init(int first, int second){
        int layoutId = context.getResources().getIdentifier("layout" + first + "_" + second, "layout", context.getPackageName());
        View view = inflate(context,layoutId, null);
        addView(view);
        touchListener = new Touch(context);
        currentIVlist = new ArrayList<>();

        for (int i = 0; i < numOfPhoto; i++) {
            int resId = context.getResources().getIdentifier("iv" + (i + 1), "id", context.getPackageName());
            RectangleIV iv = (RectangleIV) view.findViewById(resId);
            iv.setPos(i);

            if (i < Utils.currentPhotos.realSize() && currentPhotos.get(i).sdcardPath != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(Utils.currentPhotos.get(i).sdcardPath, options);
                int newheight = 1080 * options.outHeight / options.outWidth;
//            Log.e("Cxz","image"+i+" size:"+imageWidth+" "+imageHeight+ " newh"+newheight);
                Picasso.with(context).load(PREPATH + Utils.currentPhotos.get(i).sdcardPath)
                        .resize(1080, newheight)
                        .into(iv);
                iv.setOnTouchListener(touchListener);
                iv.setAssigned(true);
            } else {
                iv.setOnClickListener(this);
                iv.setAssigned(false);
            }
            currentIVlist.add(iv);
        }
    }

    @Override
    public void onClick(View v) {
        if(!((RectangleIV)v).isAssigned()) {
            Intent ii = new Intent(context, PickPhotoActivity.class);
            ii.putExtra(Utils.INTENT_KEY_PICK_ONE, true);
            ii.putExtra(Utils.INTENT_KEY_PICK_POS, ((RectangleIV)v).getPos());
            context.startActivityForResult(new Intent(ii), Utils.REQUEST_CODE_PICK_ONE);
        }
    }

    @Override
    public void setImageForUnassignView(int unassignPos) {
        Picasso.with(context).load(PREPATH + Utils.currentPhotos.get(unassignPos).sdcardPath).into(currentIVlist.get(unassignPos));
        currentIVlist.get(unassignPos).setOnTouchListener(touchListener);
    }
}
