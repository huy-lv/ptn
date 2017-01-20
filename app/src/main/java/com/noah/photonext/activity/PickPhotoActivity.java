package com.noah.photonext.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.noah.photonext.R;
import com.noah.photonext.adapter.GalleryAdapter;
import com.noah.photonext.adapter.Photo;
import com.noah.photonext.base.BaseActivityToolbar;
import com.noah.photonext.util.Utils;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;

import static com.noah.photonext.util.Utils.INTENT_KEY_PICK_DOUBLE;
import static com.noah.photonext.util.Utils.INTENT_KEY_PICK_ONE;
import static com.noah.photonext.util.Utils.INTENT_KEY_PICK_ONE_EDIT;
import static com.noah.photonext.util.Utils.currentPhotos;
import static com.noah.photonext.util.Utils.numOfPhoto;

/**
 * Created by HuyLV-CT on 02-Nov-16.
 */

public class PickPhotoActivity extends BaseActivityToolbar {
    private Handler handler;

    @BindView(R.id.pick_gv)
    GridView pick_gv;
    private GalleryAdapter adapter;
    @BindView(R.id.pick_iv_no_media)
    ImageView pick_iv_no_media;

    boolean PICK_ONE = false;
    int PICK_IMAGE_POS = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.pick_photo));
        setTitleStylePick();

        if (getIntent().hasExtra(INTENT_KEY_PICK_ONE)) { //pick one to edit
            PICK_ONE = true;
            PICK_IMAGE_POS = getIntent().getIntExtra(Utils.INTENT_KEY_PICK_POS, -1);
        }else if(getIntent().hasExtra(INTENT_KEY_PICK_DOUBLE)){ // pick one to add to double
            PICK_ONE = true;
            PICK_IMAGE_POS = -2;
        }

        new TedPermission(this).setPermissionListener(new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                init();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(PickPhotoActivity.this,"STOP",Toast.LENGTH_SHORT).show();
            }
        }).setDeniedMessage("If you reject permission,you can not use this service\\n\\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).check();


    }

    private void init() {
        handler = new Handler();
        pick_gv.setFastScrollEnabled(true);
        adapter = new GalleryAdapter(this);
        adapter.setPickOne(PICK_ONE);

        pick_gv.setAdapter(adapter);

        new Thread() {

            @Override
            public void run() {
                Looper.prepare();
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        adapter.addAll(getGalleryPhotos());
                        checkImageStatus();
                    }
                });
                Looper.loop();
            }

            ;

        }.start();
    }

    public void updateTitle(int num) {
        setTitle(num + " items selected");
    }

    private void checkImageStatus() {
        if (adapter.isEmpty()) {
            pick_iv_no_media.setVisibility(View.VISIBLE);
        } else {
            pick_iv_no_media.setVisibility(View.GONE);
        }
    }

    private ArrayList<Photo> getGalleryPhotos() {
        ArrayList<Photo> galleryList = new ArrayList<Photo>();

        try {
            final String[] columns = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;

            @SuppressWarnings("deprecation")
            Cursor imagecursor = managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                    null, null, orderBy);
            if (imagecursor != null && imagecursor.getCount() > 0) {

                while (imagecursor.moveToNext()) {
                    Photo item = new Photo();

                    int dataColumnIndex = imagecursor
                            .getColumnIndex(MediaStore.Images.Media.DATA);

                    item.sdcardPath = imagecursor.getString(dataColumnIndex);
                    galleryList.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // show newest photo at beginning of the list
        Collections.reverse(galleryList);
        return galleryList;
    }


    @Override
    public void onClickNext() {
        int temp = adapter.getSelectedItem().size();
        if (PICK_ONE) {
            if (temp == 1) {
                switch (PICK_IMAGE_POS){
                    case -1:
                        //edit photo
                        Intent i  = new Intent(this,EditActivity.class);
                        i.putExtra(INTENT_KEY_PICK_ONE_EDIT,adapter.getSelectedItem().get(0).sdcardPath);
                        startActivity(i);
                        break;
                    case -2:
                        //insert double
                        Intent returnIntent2 = new Intent();
                        returnIntent2.putExtra(Utils.INTENT_KEY_DOUBLE_PATH, adapter.getSelectedItem().get(0).sdcardPath);
                        setResult(Activity.RESULT_OK, returnIntent2);
                        finish();
                        break;
                    default:
                        //insert photo
                        currentPhotos.remove(PICK_IMAGE_POS);
                        Utils.currentPhotos.add(PICK_IMAGE_POS, adapter.getSelectedItem().get(0));
                        Log.e("cxz", "size:" + currentPhotos.size() + " realsize" + currentPhotos.realSize());
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(Utils.INTENT_KEY_PICK_POS, PICK_IMAGE_POS);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        break;
                }
            }else{
                Toast.makeText(this, "You must select 1 photo!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (temp < 2) {
                Toast.makeText(this, "You must select at least 2 photos!", Toast.LENGTH_SHORT).show();
            } else {
                numOfPhoto = temp;
                Utils.currentPhotos.clear();
                Utils.currentPhotos.addAll(adapter.getSelectedItem());
                startActivity(new Intent(this, CollageActivity.class));
            }

        }
    }

    @Override
    protected void onClickClear() {
        adapter.clearSelection();
        updateTitle(0);
        adapter.notifyDataSetChanged();
    }
}
