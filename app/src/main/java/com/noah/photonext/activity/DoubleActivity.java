package com.noah.photonext.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.noah.photonext.R;
import com.noah.photonext.adapter.DoubleThumbnailAdapter;
import com.noah.photonext.base.BaseEditActivity;
import com.noah.photonext.custom.BlurDialogFragment;
import com.noah.photonext.custom.LLayout;
import com.noah.photonext.model.DoubleThumbnailObject;
import com.noah.photonext.task.DoubleThumbnailTask;
import com.noah.photonext.util.Utils;

import java.util.ArrayList;

import butterknife.BindView;

import static com.noah.photonext.util.Utils.INTENT_KEY_DOUBLE_PATH;
import static com.noah.photonext.util.Utils.xfermodes;

/**
 * Created by HuyLV-CT on 12-Jan-17.
 */

public class DoubleActivity extends BaseEditActivity implements View.OnClickListener, View.OnTouchListener {

    public int currentType;
    @BindView(R.id.double_main_iv)
    ImageView double_main_iv;
    //    @BindView(R.id.double_top_iv)
//    ImageView double_top_iv;
//    @BindView(R.id.double_normal)
//    LLayout double_normal;
//    @BindView(R.id.double_darken)
//    LLayout double_darken;
//    @BindView(R.id.double_multiply)
//    LLayout double_multiply;
//    @BindView(R.id.double_screen)
//    LLayout double_screen;
//    @BindView(R.id.double_lighten)
//    LLayout double_lighten;
    @BindView(R.id.double_browse)
    LLayout double_browse;
    //rv
    @BindView(R.id.double_list_rv)
    RecyclerView double_list_rv;
    DoubleThumbnailAdapter doubleThumbnailAdapter;
    ArrayList<DoubleThumbnailObject> doubleList;
    boolean dragging;
    float scaleImage;
    private Bitmap topBitmap;
    private Paint paint;
    private Canvas canvas;
    private Bitmap currentBm, srcBm, dstBm;
    private int currentX, currentY, currentW, currentH, startX, startY;
    private BlurDialogFragment dialog;
    private boolean firstTime;
    private DoubleThumbnailTask doubleThumbnailTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigation_seek_bar.setAbsoluteMinMaxValue(0, 100);
        navigation_seek_bar.setProgress(100);
        currentBm = Utils.historyBitmaps.get(Utils.currentHistoryPos);
        dstBm = currentBm.copy(currentBm.getConfig(), true);
        double_main_iv.setImageBitmap(currentBm);

        currentType = -1;
        paint = new Paint();
        currentX = 0;
        currentY = 0;
        firstTime = true;

        double_browse.setOnClickListener(this);
        double_main_iv.setOnTouchListener(this);

        //create thumbnail


        //rv
        doubleList = Utils.createDoubleList(getResources());
        doubleThumbnailAdapter = new DoubleThumbnailAdapter(this, doubleList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        double_list_rv.setAdapter(doubleThumbnailAdapter);
        double_list_rv.setLayoutManager(linearLayoutManager);
    }

    void createThumbnail() {
        Bitmap thumb1 = Bitmap.createScaledBitmap(currentBm, 400, 400, true);
        Bitmap thumb2 = Bitmap.createScaledBitmap(topBitmap, 400, 400, true);
        doubleThumbnailTask = new DoubleThumbnailTask(doubleList, doubleThumbnailAdapter);
        doubleThumbnailTask.execute(thumb1, thumb2);
    }

    @Override
    public void onClickNext() {
        super.onClickNext();
    }

    void createPickPhotoDialog() {
        dialog = new BlurDialogFragment();
        dialog.fadeIn(DoubleActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPickPhoto();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) (event.getX() / scaleImage);
        int y = (int) (event.getY() / scaleImage);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x < currentX + currentW && x > currentX && y < currentY + currentH && y > currentY) {
                    dragging = true;
                    startX = x - currentX;
                    startY = y - currentY;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (dragging) {
                    currentX = x - startX;
                    currentY = y - startY;
                    redraw();
                }
                break;
            case MotionEvent.ACTION_UP:
                dragging = false;
                break;
        }
        return true;
    }

    void openPickPhoto() {
        Intent i = new Intent(DoubleActivity.this, PickPhotoActivity.class);
        i.putExtra(Utils.INTENT_KEY_PICK_DOUBLE, "1");
        startActivityForResult(i, Utils.REQUEST_CODE_PICK_DOUBLE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.double_browse:
                openPickPhoto();
                break;
        }
        if (currentType != -1) redraw();
    }

    public void redraw() {
        if (topBitmap != null) {
            dstBm = currentBm.copy(currentBm.getConfig(), true);
            canvas = new Canvas(dstBm);
            paint.setXfermode(xfermodes[currentType]);
            canvas.drawBitmap(topBitmap, currentX, currentY, paint);
            double_main_iv.setImageBitmap(dstBm);
        }
    }


    @Override
    protected void onSeekBarTracking(int value) {
        super.onSeekBarTracking(value);
        paint.setAlpha(value);
        redraw();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.REQUEST_CODE_PICK_DOUBLE) {
            if (resultCode == Activity.RESULT_OK) {
                topBitmap = BitmapFactory.decodeFile(data.getStringExtra(INTENT_KEY_DOUBLE_PATH));
                dialog.dismiss();
                //do normal
                currentType = 0;
                currentX = (dstBm.getWidth() - topBitmap.getWidth()) / 2;
                currentY = (dstBm.getHeight() - topBitmap.getHeight()) / 2;
                currentW = topBitmap.getWidth();
                currentH = topBitmap.getHeight();
                dstBm = currentBm.copy(currentBm.getConfig(), true);
                canvas = new Canvas(dstBm);
                canvas.drawBitmap(topBitmap, currentX, currentY, paint);
                double_main_iv.setImageBitmap(dstBm);

                createThumbnail();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //resize imageview
        float width = double_main_iv.getDrawable().getIntrinsicWidth();
        float height = double_main_iv.getDrawable().getIntrinsicHeight();
        float imagevieww = double_main_iv.getWidth();
        float imageviewh = double_main_iv.getHeight();
        Log.e("cxz", "screen w " + width + "h " + height + " image " + imagevieww + " " + imageviewh);

        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) double_main_iv.getLayoutParams();
        if (width / height > imagevieww / imageviewh) {
            p.height = (int) (height * imagevieww / width);
            scaleImage = imagevieww / width;
        } else {
            p.width = (int) (width * imageviewh / height);
            scaleImage = imageviewh / height;
        }
        double_main_iv.setLayoutParams(p);
        Matrix m = new Matrix();
        m.postScale(scaleImage, scaleImage);
        double_main_iv.setImageMatrix(m);


        //create dialog
        if (firstTime) {
            createPickPhotoDialog();
            firstTime = false;
        }
    }

    @Override
    protected int getToolbarTitleId() {
        return R.string.doublee;
    }

    @Override
    protected int isShowSlider() {
        return View.VISIBLE;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_double;
    }
}
