package com.noah.photonext.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noah.photonext.R;
import com.noah.photonext.adapter.ColorAdapter;
import com.noah.photonext.adapter.LayoutAdapter;
import com.noah.photonext.adapter.Photo;
import com.noah.photonext.base.BaseActivityToolbar;
import com.noah.photonext.base.BaseLayout;
import com.noah.photonext.custom.CustomLayout4and1;
import com.noah.photonext.custom.LLayout;
import com.noah.photonext.custom.RectangleIV;
import com.noah.photonext.custom.ShapedImageView;
import com.noah.photonext.custom.SquareLayout;
import com.noah.photonext.custom.StandardLayout;
import com.noah.photonext.custom.StartSeekBar;
import com.noah.photonext.custom.Touch;
import com.noah.photonext.model.BackgroundValue;
import com.noah.photonext.model.LayoutObject;
import com.noah.photonext.util.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;

import static com.noah.photonext.util.Utils.FolderName;
import static com.noah.photonext.util.Utils.INTENT_KEY_PICK_POS;
import static com.noah.photonext.util.Utils.currentBitmap;
import static com.noah.photonext.util.Utils.currentCollageHeight;
import static com.noah.photonext.util.Utils.currentCollageWidth;
import static com.noah.photonext.util.Utils.currentHistoryPos;
import static com.noah.photonext.util.Utils.currentPhotos;
import static com.noah.photonext.util.Utils.historyBitmaps;
import static com.noah.photonext.util.Utils.numOfPhoto;
import static com.noah.photonext.util.Utils.originCollageHeidht;
import static com.noah.photonext.util.Utils.originCollageWidth;

/**
 * Created by HuyLV-CT on 02-Nov-16.
 */
public class CollageActivity extends BaseActivityToolbar implements StartSeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private static final int REQUEST_CODE_PICK_PHOTO = 101;
    LayoutInflater layoutInflater;
    @BindView(R.id.collage_main)
    FrameLayout collage_main;
    //layout dc add vao collage_main sau khi chon layout
    BaseLayout collage_main_fl_container;
    Touch touchListener;

    @BindView(R.id.collage_choose_layout_iv)
    ImageView collage_choose_layout_iv;
    //    @BindView(R.id.collage_padding_iv)
//    ImageView collage_padding_iv;
    @BindView(R.id.collage_corner_iv)
    ImageView collage_corner_iv;
    //    @BindView(R.id.collage_change_size_iv)
//    ImageView collage_change_size_iv;
    @BindView(R.id.collage_background_iv)
    ImageView collage_border_color_iv;


    @BindView(R.id.collage_square_layout)
    SquareLayout collage_square_layout;


    ////////
    @BindView(R.id.bottom_bar_layout)
    RecyclerView bottom_bar_layout;
    LayoutAdapter layoutAdapter;

    @BindView(R.id.bottom_bar_corner_content)
    LinearLayout bottom_bar_cornen_content;
    @BindView(R.id.bottom_bar_color)
    LinearLayout bottom_bar_color;

    //seekbar
    @BindView(R.id.bottom_bar_second1)
    LinearLayout bottom_bar_second1;
    @BindView(R.id.bottom_bar_second2)
    RelativeLayout bottom_bar_second2;
    @BindView(R.id.collage_change_size_sb)
    StartSeekBar collage_change_size_sb;
    @BindView(R.id.bottom_bar_padding_sb_cancel)
    ImageView bottom_bar_padding_sb_close;
    @BindView(R.id.bottom_bar_padding_sb_done)
    ImageView bottom_bar_padding_sb_done;

    //3 button change seekbar
    @BindView(R.id.bottom_bar_margin)
    LLayout bottom_bar_padding;
    @BindView(R.id.bottom_bar_corner)
    LLayout bottom_bar_corner;
    @BindView(R.id.bottom_bar_ratio)
    LLayout bottom_bar_ratio;
    @BindView(R.id.collage_seekbar_tv)
    TextView collage_seekbar_tv;

    //tab3
    @BindView(R.id.bottom_bar_color_pick)
    RecyclerView bottom_bar_color_pick_rv;
    ColorAdapter colorAdapter;
    @BindView(R.id.bottom_bar_color_background)
    RecyclerView bottom_bar_color_background_rv;
    ColorAdapter backgroundAdapter;


    ArrayList<LayoutObject> layoutList;

    ArrayList<Integer> unassignedPhotosId;
    //intial margin always 5
    int currentMargin = 5;
    int currentCorner = 0;
    int currentRatio = 0;
    int newMargin = 0;
    int newCorner = 0;
    int newRatio = 0;
    private File imagePath;
    private int currentLayoutId;
    private int seekbarType = 0;
    private boolean isStandard;
    private boolean isChangingCorner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //listener
        touchListener = new Touch(this);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        unassignedPhotosId = new ArrayList<>();
        if (numOfPhoto == 4) {
            changeLayout(numOfPhoto, 7, false);
        } else {
            changeLayout(numOfPhoto, 1, true);
        }
        showHidePanel(-1);
        collage_change_size_sb.setOnSeekBarChangeListener(this);
        collage_corner_iv.setOnClickListener(this);
        collage_choose_layout_iv.setOnClickListener(this);
        collage_border_color_iv.setOnClickListener(this);

        //layout list
        layoutList = Utils.createLayoutList();
        layoutAdapter = new LayoutAdapter(this, layoutList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
        bottom_bar_layout.setLayoutManager(gridLayoutManager);
        bottom_bar_layout.setAdapter(layoutAdapter);

        //seekbar
        bottom_bar_padding.setOnClickListener(this);
        bottom_bar_corner.setOnClickListener(this);
        bottom_bar_ratio.setOnClickListener(this);
        bottom_bar_padding_sb_close.setOnClickListener(this);
        bottom_bar_padding_sb_done.setOnClickListener(this);

        collage_change_size_sb.setProgress(currentMargin + 50);

        //tab3
        colorAdapter = new ColorAdapter(this, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        bottom_bar_color_pick_rv.setLayoutManager(linearLayoutManager);
        bottom_bar_color_pick_rv.setAdapter(colorAdapter);

        backgroundAdapter = new ColorAdapter(this, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        bottom_bar_color_background_rv.setLayoutManager(linearLayoutManager2);
        bottom_bar_color_background_rv.setAdapter(backgroundAdapter);
    }


    public void changeLayout(int first, int second, boolean standard) {
        if (first != numOfPhoto || currentLayoutId != second) {
            if (first > numOfPhoto) {
                for (int i = 0; i < first - numOfPhoto; i++) {
                    currentPhotos.add(new Photo(null));
                }
            } else {
                for (int i = 0; i < numOfPhoto - first; i++) {
                    currentPhotos.remove(currentPhotos.size() - 1);
                }
            }

            numOfPhoto = first;
            currentLayoutId = second;
            collage_main.removeAllViews();
            isStandard = standard;
            if (standard) {
                collage_main_fl_container = new StandardLayout(this, first, second);
            } else {
                collage_main_fl_container = new CustomLayout4and1(this, first, second);
            }
            collage_main.addView(collage_main_fl_container);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_collage;
    }


    void showHidePanel(int n) {
        bottom_bar_corner.setVisibility(isStandard ? View.VISIBLE : View.GONE);
        bottom_bar_layout.setVisibility(n == 0 ? View.VISIBLE : View.GONE);
        collage_choose_layout_iv.setImageResource(n == 0 ? R.mipmap.ic_collage_setup1_ac : R.mipmap.ic_collage_setup1);
        bottom_bar_cornen_content.setVisibility(n == 1 ? View.VISIBLE : View.GONE);
        collage_corner_iv.setImageResource(n == 1 ? R.mipmap.ic_collage_setup2_ac : R.mipmap.ic_collage_setup2);
        bottom_bar_color.setVisibility(n == 2 ? View.VISIBLE : View.GONE);
        collage_border_color_iv.setImageResource(n == 2 ? R.mipmap.ic_collage_setup3_ac : R.mipmap.ic_collage_setup3);
    }

    public void changeBackground(boolean isTypeColor, BackgroundValue backgroundValue) {
        collage_main_fl_container.changeBackgroundColor(isTypeColor, backgroundValue);
    }

    @Override
    public void onSeekBarValueChange(StartSeekBar bar, int value) {
        showHideBorder(-1);
        switch (seekbarType) {
            case 0:
                changeSize(value, newCorner, newRatio);
                break;
            case 1:
                changeSize(newMargin, value, newRatio);
                break;
            case 2:
                changeSize(newMargin, newCorner, value);
        }
    }

    @Override
    public void onClickNext() {
        showHideBorder(0);
        Intent i = new Intent(this, EditActivity.class);
        currentBitmap = takeScreenshot();
        historyBitmaps.add(currentBitmap);
        startActivity(i);
    }

    @Override
    public void onStartTrackingTouch(StartSeekBar bar) {
        collage_seekbar_tv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStopTrackingTouch(StartSeekBar bar, int value) {
        collage_seekbar_tv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        Utils.showAlertDialog(this, "Photo not saved!", "Are you sure to delete this photo!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                historyBitmaps.clear();
                currentHistoryPos = 0;
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        showHideBorder(-1);
        switch (v.getId()) {
            /////////////////////////////////tab 1 2 3
            case R.id.collage_choose_layout_iv:
                showHidePanel(0);
                break;
            case R.id.collage_corner_iv:
                showHidePanel(1);
                break;
            case R.id.collage_background_iv:
                showHidePanel(2);
                break;
            //////////////////////////////// in tab 2
            case R.id.bottom_bar_margin:
                goToChangingCorner(0);
                break;
            case R.id.bottom_bar_corner:
                goToChangingCorner(1);
                break;
            case R.id.bottom_bar_ratio:
                goToChangingCorner(2);
                break;


            ////sliding bar close and done
            case R.id.bottom_bar_padding_sb_cancel:
                bottom_bar_second2.setVisibility(View.INVISIBLE);
                bottom_bar_second1.setVisibility(View.VISIBLE);
                showToolbarButton();
                bottom_bar_padding.setIconImage(R.mipmap.ic_collage_setup2_1);
                bottom_bar_corner.setIconImage(R.mipmap.ic_collage_setup2);
                bottom_bar_ratio.setIconImage(R.mipmap.ic_collage_setup2_2);
                changeSize(currentMargin, currentCorner, currentRatio);
                isChangingCorner = false;
                break;
            case R.id.bottom_bar_padding_sb_done:
                bottom_bar_second2.setVisibility(View.INVISIBLE);
                bottom_bar_second1.setVisibility(View.VISIBLE);
                bottom_bar_padding.setIconImage(R.mipmap.ic_collage_setup2_1);
                bottom_bar_corner.setIconImage(R.mipmap.ic_collage_setup2);
                bottom_bar_ratio.setIconImage(R.mipmap.ic_collage_setup2_2);
                showToolbarButton();
                currentMargin = newMargin;
                currentCorner = newCorner;
                currentRatio = newRatio;
                isChangingCorner = false;
                break;
        }
    }

    void changeSize(int margin, int corner, int ratio) {

        //margin
        if (margin != newMargin) {
            newMargin = margin;
            collage_seekbar_tv.setText(String.valueOf(margin));
            for (ShapedImageView iv : collage_main_fl_container.currentIVlist) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
                params.setMargins(margin, margin, margin, margin);
                iv.setLayoutParams(params);
            }
        }

        //corner
        if (corner != newCorner) {
            newCorner = corner;
            collage_seekbar_tv.setText(String.valueOf(corner));
            for (int i = 0; i < collage_main_fl_container.currentIVlist.size(); i++) {
                ((RectangleIV) collage_main_fl_container.currentIVlist.get(i)).setCornerRadius(corner);
            }
        }

        //ratio
        if (ratio != newRatio) {
            newRatio = ratio;
            collage_seekbar_tv.setText(String.valueOf(ratio));
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) collage_main.getLayoutParams();
            if (newRatio >= 0) {
                lp.width = originCollageWidth - (ratio * 10);
                lp.height = originCollageWidth;
            } else {
                lp.width = originCollageWidth;
                lp.height = originCollageWidth + (ratio * 10);
            }
            collage_main.setLayoutParams(lp);
        }
    }

    private void goToChangingCorner(int i) {
        seekbarType = i;
        if (!isChangingCorner) {
            hideToolbarButton();
            bottom_bar_second2.setVisibility(View.VISIBLE);
            bottom_bar_second1.setVisibility(View.INVISIBLE);
            newMargin = currentMargin;
            newCorner = currentCorner;
            newRatio = currentRatio;
            isChangingCorner = true;
            showSeekBar();
        } else {
            showSeekBar();
        }
    }

    void showSeekBar() {
        bottom_bar_padding.setIconImage(seekbarType == 0 ? R.mipmap.ic_collage_setup2_1_ac : R.mipmap.ic_collage_setup2_1);
        bottom_bar_corner.setIconImage(seekbarType == 1 ? R.mipmap.ic_collage_setup2_ac : R.mipmap.ic_collage_setup2);
        bottom_bar_ratio.setIconImage(seekbarType == 2 ? R.mipmap.ic_collage_setup2_2_ac : R.mipmap.ic_collage_setup2_2);
        switch (seekbarType) {
            case 0:
                collage_change_size_sb.setAbsoluteMinMaxValue(0, 100);
                collage_change_size_sb.setProgress(newMargin);
                break;
            case 1:
                collage_change_size_sb.setAbsoluteMinMaxValue(0, 100);
                collage_change_size_sb.setProgress(newCorner);
                break;
            case 2:
                collage_change_size_sb.setAbsoluteMinMaxValue(-80, 80);
                collage_change_size_sb.setProgress(newRatio);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.REQUEST_CODE_PICK_ONE) {
            if (resultCode == Activity.RESULT_OK) {
                int unassignPos = data.getIntExtra(INTENT_KEY_PICK_POS, -1);
                collage_main_fl_container.setImageForUnassignedView(unassignPos);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pick_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_next) {
            Bitmap bitmap = takeScreenshot();
            saveBitmap(bitmap);
            Intent intenEffect = new Intent(getApplicationContext(), ResultActivity.class);
            intenEffect.putExtra(Utils.INTENT_KEY_IMAGE_PATH, imagePath.toString());
            startActivity(intenEffect);
        }
        return true;
    }

    @Override
    public void onClickBack() {
        Utils.showAlertDialog(this, "Photo not saved!", "Are you sure to delete this photo!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                historyBitmaps.clear();
                currentHistoryPos = 0;
                finish();
            }
        });
    }

    public Bitmap takeScreenshot() {
        Log.e("", "call takeScreenshot");

        collage_main.setDrawingCacheEnabled(true);
        getWindow().getDecorView().getRootView();
        return collage_main.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        getOutputMediaFile();
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            Log.e("cxz", e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getOutputMediaFile() {
        File fileDir;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            fileDir = new File(Environment.getExternalStorageDirectory(),
                    Utils.FolderName);
        } else {
            fileDir = new File(getFilesDir(), FolderName);
        }
        if (!fileDir.exists()) {
            if (!fileDir.mkdir()) {
                Log.e("cxz", "error2");
                return;
            }
        }

        String time = String.valueOf(System.currentTimeMillis());
        imagePath = new File(fileDir.getPath() + File.separator + time
                + "_photo" + ".jpg");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        originCollageWidth = currentCollageWidth = collage_main.getWidth();
        originCollageHeidht = currentCollageHeight = collage_main.getHeight();
        Log.e("cxz", "w " + originCollageWidth + " " + originCollageHeidht + " " + collage_square_layout.getWidth());
    }

    public void showHideBorder(int vId) {
        for (ShapedImageView v : collage_main_fl_container.currentIVlist) {
            v.setShowBorder(vId == v.getId());
        }
    }

}
