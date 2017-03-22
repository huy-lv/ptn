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

import butterknife.ButterKnife;

import static com.noah.photonext.util.Utils.PREPATH;
import static com.noah.photonext.util.Utils.numOfPhoto;

/**
 * Created by HuyLV-CT on 02-Dec-16.
 */

public class CustomLayout4and1 extends BaseLayout implements View.OnClickListener {
    private CollageActivity context;
    private Touch touchListener;

    public CustomLayout4and1(CollageActivity context, int first, int second){
        super(context);
        this.context = context;
        init(first,second);

    }

    public CustomLayout4and1(Context context) {
        super(context);
    }

    public CustomLayout4and1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLayout4and1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomLayout4and1(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    void init(int first,int second) {
        int layoutId = context.getResources().getIdentifier("custom" + first + "_" + second, "layout", context.getPackageName());
        View view = inflate(context, layoutId, null);
        addView(view);
        touchListener = new Touch(context);

        currentIVlist = new ArrayList<>();

        for (int i = 0; i < numOfPhoto; i++) {
            int resId = getResources().getIdentifier("iv" + (i + 1), "id", context.getPackageName());
            PentagonIV p = ButterKnife.findById(view, resId);
            currentIVlist.add(p);
            p.setPos(i);

            if (i < Utils.currentPhotos.realSize()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(Utils.currentPhotos.get(i).sdcardPath, options);
                int newheight = 1080 * options.outHeight / options.outWidth;
                Picasso.with(context).load(PREPATH + Utils.currentPhotos.get(i).sdcardPath)
//                        .centerCrop()
                        .resize(1080, newheight)
                        .into(p);
                p.setOnTouchListener(touchListener);
                p.setAssigned(true);
            } else {
                p.setOnClickListener(this);
                p.setAssigned(false);
            }
        }
    }

    @Override
    public void setImageForUnassignedView(int unassignPos) {
        Picasso.with(context).load(PREPATH + Utils.currentPhotos.get(unassignPos).sdcardPath).into(currentIVlist.get(unassignPos));
        currentIVlist.get(unassignPos).setOnClickListener(null);
        currentIVlist.get(unassignPos).setOnTouchListener(touchListener);
    }

    @Override
    public void onClick(View v) {
        if (!((PentagonIV)v).isAssigned()) {
            Intent ii = new Intent(context, PickPhotoActivity.class);
            ii.putExtra(Utils.INTENT_KEY_PICK_ONE, true);
            ii.putExtra(Utils.INTENT_KEY_PICK_POS, ((PentagonIV)v).getPos());
            context.startActivityForResult(new Intent(ii), Utils.REQUEST_CODE_PICK_ONE);
        }
    }
}
