//package com.noah.photonext.activity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.SeekBar;
//
//import com.noah.photonext.R;
//import com.noah.photonext.util.Utils;
//import com.noah.photonext.adapter.Photo;
//import com.noah.photonext.base.BaseActivityToolbar;
//import com.noah.photonext.base.BaseLayout;
//import com.noah.photonext.custom.CustomLayout4and1;
//import com.noah.photonext.custom.RectangleIV;
//import com.noah.photonext.custom.ShapedImageView;
//import com.noah.photonext.custom.SquareLayout;
//import com.noah.photonext.custom.StandardLayout;
//import com.noah.photonext.custom.Touch;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//
//import butterknife.BindView;
//
//import static com.noah.photonext.util.Utils.FolderName;
//import static com.noah.photonext.util.Utils.INTENT_KEY_PICK_POS;
//import static com.noah.photonext.util.Utils.currentCollageHeight;
//import static com.noah.photonext.util.Utils.currentCollageWidth;
//import static com.noah.photonext.util.Utils.currentHistoryPos;
//import static com.noah.photonext.util.Utils.currentPhotos;
//import static com.noah.photonext.util.Utils.historyBitmaps;
//import static com.noah.photonext.util.Utils.numOfPhoto;
//import static com.noah.photonext.util.Utils.originCollageHeidht;
//import static com.noah.photonext.util.Utils.originCollageWidth;
//
///**
// * Created by HuyLV-CT on 02-Nov-16.
// */
//public class CollageActivity extends BaseActivityToolbar implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
//    private static final int REQUEST_CODE_PICK_PHOTO = 101;
//    LayoutInflater layoutInflater;
//    @BindView(R.id.collage_main)
//    FrameLayout collage_main;
//    BaseLayout collage_main_fl_container;
//    Touch touchListener;
//
//    @BindView(R.id.collage_choose_layout_iv)
//    ImageView collage_choose_layout_iv;
//    @BindView(R.id.collage_padding_iv)
//    ImageView collage_padding_iv;
//    @BindView(R.id.collage_corner_iv)
//    ImageView collage_corner_iv;
//    @BindView(R.id.collage_change_size_iv)
//    ImageView collage_change_size_iv;
//    @BindView(R.id.collage_border_color_iv)
//    ImageView collage_border_color_iv;
//
//    @BindView(R.id.bottom_bar_second1)
//    LinearLayout bottom_bar_second1;
//    @BindView(R.id.bottom_bar_second2)
//    LinearLayout bottom_bar_second2;
//    @BindView(R.id.bottom_bar_second3)
//    LinearLayout bottom_bar_second3;
//    @BindView(R.id.bottom_bar_second2)
//    LinearLayout bottom_bar_second2;
//
//    @BindView(R.id.collage_square_layout)
//    SquareLayout collage_square_layout;
//    @BindView(R.id.collage_change_size_sb)
//    SeekBar collage_change_size_sb;
//    @BindView(R.id.collage_padding_sb)
//    SeekBar collage_padding_sb;
//    @BindView(R.id.collage_corner_sb)
//    SeekBar collage_corner_sb;
//    private Resources res;
//    private int mCurrentBackgroundColor = Color.GRAY;
//
////    @BindView(R.id.collage_indicator1)
////    View collage_indicator1;
////    @BindView(R.id.collage_indicator2)
////    View collage_indicator2;
////    @BindView(R.id.collage_indicator3)
////    View collage_indicator3;
////    @BindView(R.id.collage_indicator4)
////    View collage_indicator4;
////    @BindView(R.id.collage_indicator5)
////    View collage_indicator5;
//
//    @BindView(R.id.choose_layout2_1)
//    ImageView choose_layout2_1;
//    @BindView(R.id.choose_layout3_1)
//    ImageView choose_layout3_1;
//    @BindView(R.id.choose_layout4_1)
//    ImageView choose_layout4_1;
//    @BindView(R.id.choose_layout4_2)
//    ImageView choose_layout4_2;
//
//
//    ArrayList<Integer> unassignPhotosId;
//    private File imagePath;
//    private int currentLayoutId;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        //listener
//        touchListener = new Touch(this);
//        res = getResources();
//        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        unassignPhotosId = new ArrayList<>();
//        if (numOfPhoto == 4) {
//            changeLayout(numOfPhoto, 1, false);
//        } else {
//            changeLayout(numOfPhoto, 1, true);
//        }
//        showHidePanel(-1);
//        collage_change_size_sb.setOnSeekBarChangeListener(this);
//        collage_padding_sb.setOnSeekBarChangeListener(this);
//        collage_corner_sb.setOnSeekBarChangeListener(this);
//
//        collage_border_color_iv.setOnClickListener(this);
//        collage_change_size_iv.setOnClickListener(this);
//        collage_corner_iv.setOnClickListener(this);
//        collage_choose_layout_iv.setOnClickListener(this);
//        collage_padding_iv.setOnClickListener(this);
//
//        choose_layout2_1.setOnClickListener(this);
//        choose_layout3_1.setOnClickListener(this);
//        choose_layout4_1.setOnClickListener(this);
//        choose_layout4_2.setOnClickListener(this);
//    }
//
//
//    void changeLayout(int first, int second, boolean standard) {
//        if (first != numOfPhoto || currentLayoutId != second) {
//            if(first > numOfPhoto){
//                for(int i=0;i<first-numOfPhoto;i++) {
//                    currentPhotos.add(new Photo(null));
//                }
//            }else{
//                for(int i=0;i<numOfPhoto-first;i++){
//                    currentPhotos.remove(currentPhotos.size()-1);
//                }
//            }
//
//            numOfPhoto = first;
//            currentLayoutId = second;
//            collage_main.removeAllViews();
//            if (standard) {
//                collage_main_fl_container = new StandardLayout(this, first, second);
//            } else {
//                collage_main_fl_container = new CustomLayout4and1(this, first, second);
//            }
//            collage_main.addView(collage_main_fl_container);
//        }
//    }
//
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_collage1;
//    }
//
//
//    void showHidePanel(int n) {
//        bottom_bar_second1.setVisibility(n == 0 ? View.VISIBLE : View.GONE);
////        collage_indicator1.setVisibility(n == 0 ? View.VISIBLE : View.GONE);
//        bottom_bar_second2.setVisibility(n == 1 ? View.VISIBLE : View.GONE);
////        collage_indicator2.setVisibility(n == 1 ? View.VISIBLE : View.GONE);
//        bottom_bar_second3.setVisibility(n == 2 ? View.VISIBLE : View.GONE);
////        collage_indicator3.setVisibility(n == 2 ? View.VISIBLE : View.GONE);
//        bottom_bar_second2.setVisibility(n == 3 ? View.VISIBLE : View.GONE);
////        collage_indicator4.setVisibility(n == 3 ? View.VISIBLE : View.GONE);
////        collage_indicator5.setVisibility(n == 4 ? View.VISIBLE : View.GONE);
//    }
//
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        showHideBorder(-1);
//        switch (seekBar.getId()) {
//            case R.id.collage_change_size_sb:
//                int currentRatio = progress - 80;
//
//                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) collage_main.getLayoutParams();
//                if (currentRatio >= 0) {
//                    lp.width = originCollageWidth - (currentRatio * 10);
//                    lp.height = originCollageWidth;
//                } else {
//                    lp.width = originCollageWidth;
//                    lp.height = originCollageWidth + (currentRatio * 10);
//                }
//                collage_main.setLayoutParams(lp);
//                break;
//            case R.id.collage_padding_sb:
//                for (ShapedImageView iv : collage_main_fl_container.currentIVlist) {
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.WRAP_CONTENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT
//                    );
//                    params.setMargins(progress, progress, progress, progress);
//                    iv.setLayoutParams(params);
//                }
//
//
//                break;
//            case R.id.collage_corner_sb:
//                for (int i = 0; i < collage_main_fl_container.currentIVlist.size(); i++) {
//                    ((RectangleIV) collage_main_fl_container.currentIVlist.get(i)).setCornerRadius(progress);
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//    }
//
//    private void showColorDialog() {
//    }
//
//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//        showHideBorder(-1);
//        switch (v.getId()) {
//            case R.id.collage_choose_layout_iv:
//                showHidePanel(0);
//                break;
//            case R.id.collage_padding_iv:
//                showHidePanel(1);
//                break;
//            case R.id.collage_corner_iv:
//                showHidePanel(2);
//                break;
//            case R.id.collage_change_size_iv:
//                showHidePanel(3);
//                break;
//            case R.id.collage_border_color_iv:
//                showColorDialog();
//                break;
//            case R.id.choose_layout2_1:
//                changeLayout(2, 1, true);
//                break;
//            case R.id.choose_layout3_1:
//                changeLayout(3, 1, true);
//                break;
//            case R.id.choose_layout4_1:
//                changeLayout(4, 1, false);
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == Utils.REQUEST_CODE_PICK_ONE) {
//            if (resultCode == Activity.RESULT_OK) {
//                int unassignPos = data.getIntExtra(INTENT_KEY_PICK_POS, -1);
//                collage_main_fl_container.setImageForUnassignedView(unassignPos);
//            }
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_pick_activity, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//        if (item.getItemId() == R.id.menu_next) {
//            Bitmap bitmap = takeScreenshot();
//            saveBitmap(bitmap);
//            Intent intenEffect = new Intent(getApplicationContext(), ResultActivity.class);
//            intenEffect.putExtra(Utils.INTENT_KEY_IMAGE_PATH, imagePath.toString());
//            startActivity(intenEffect);
//        }
//        return true;
//    }
//
//    @Override
//    public void onClickNext() {
//
//    }
//
//    @Override
//    public void onClickBack() {
//        Utils.showAlertDialog(this, "Photo not saved!", "Are you sure to delete this photo!", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                historyBitmaps.clear();
//                currentHistoryPos = 0;
//                finish();
//            }
//        });
//    }
//
//    public Bitmap takeScreenshot() {
//        Log.e("", "call takeScreenshot");
//
//        collage_main.setDrawingCacheEnabled(true);
//        getWindow().getDecorView().getRootView();
//        return collage_main.getDrawingCache();
//    }
//
//    public void saveBitmap(Bitmap bitmap) {
//        getOutputMediaFile();
//        FileOutputStream fos;
//        try {
//            fos = new FileOutputStream(imagePath);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.flush();
//            fos.close();
//
//        } catch (FileNotFoundException e) {
//            Log.e("GREC", e.getMessage(), e);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getOutputMediaFile() {
//        File fileDir;
//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            fileDir = new File(Environment.getExternalStorageDirectory(),
//                    Utils.FolderName);
//        } else {
//            fileDir = new File(getFilesDir(), FolderName);
//        }
//        if (!fileDir.exists()) {
//            if (!fileDir.mkdir()) {
//                Log.e("cxz", "error2");
//                return;
//            }
//        }
//
//        String time = String.valueOf(System.currentTimeMillis());
//        imagePath = new File(fileDir.getPath() + File.separator + time
//                + "_photo" + ".jpg");
//    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//
//        originCollageWidth = currentCollageWidth = collage_main.getWidth();
//        originCollageHeidht = currentCollageHeight = collage_main.getHeight();
//        Log.e("cxz", "w " + originCollageWidth + " " + originCollageHeidht + " " + collage_square_layout.getWidth());
//    }
//
//    public void showHideBorder(int vId) {
//        for (ShapedImageView v : collage_main_fl_container.currentIVlist) {
//            if (vId == v.getId()) v.setShowBorder(true);
//            else v.setShowBorder(false);
//        }
//    }
//}
