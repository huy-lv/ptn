package com.noah.photonext.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.noah.photonext.R;
import com.noah.photonext.adapter.GalleryAdapter;
import com.noah.photonext.adapter.Photo;
import com.noah.photonext.adapter.SelectedPhotoAdapter;
import com.noah.photonext.base.BaseActivityToolbar;
import com.noah.photonext.task.LoadPhotoTask;
import com.noah.photonext.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.noah.photonext.util.Utils.INTENT_KEY_PICK_DOUBLE;
import static com.noah.photonext.util.Utils.INTENT_KEY_PICK_ONE;
import static com.noah.photonext.util.Utils.INTENT_KEY_PICK_ONE_EDIT;
import static com.noah.photonext.util.Utils.calculateNoOfColumns;
import static com.noah.photonext.util.Utils.currentPhotos;
import static com.noah.photonext.util.Utils.numOfPhoto;

/**
 * Created by HuyLV-CT on 02-Nov-16.
 */

public class PickPhotoActivity extends BaseActivityToolbar {
    public GalleryAdapter galleryAdapter;
    @BindView(R.id.pick_iv_no_media)
    public LinearLayout pick_iv_no_media;
    ArrayList<Photo> selectedPhoto = new ArrayList<>();
    @BindView(R.id.pick_gv)
    GridView pick_gv;
    @BindView(R.id.pick_selected_rv)
    RecyclerView pick_selected_rv;
    @BindView(R.id.pick_loading_pb)
    ProgressBar pick_loading_pb;
    boolean PICK_ONE = false;
    int PICK_IMAGE_POS = -1;
    private SelectedPhotoAdapter selectedAdapter;
    private String capturedPhoto;

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
                Utils.createAlertDialog(PickPhotoActivity.this, "STOPP");
            }
        }).setDeniedMessage(getString(R.string.deny_message))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).check();


        selectedAdapter = new SelectedPhotoAdapter(this, selectedPhoto);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        pick_selected_rv.setLayoutManager(llm);
        pick_selected_rv.setAdapter(selectedAdapter);

    }

    private boolean hasIntent(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent it = new Intent(action);
        List<ResolveInfo> lst = packageManager.queryIntentActivities(it, PackageManager.MATCH_DEFAULT_ONLY);
        return lst.size() > 0;
    }

    @OnClick(R.id.pick_camera_iv)
    void onClickCamera() {
        if (!hasIntent(this, MediaStore.ACTION_IMAGE_CAPTURE)) {
            Toast.makeText(this, getResources().getString(R.string.camera_not_available_message), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File outPut = Utils.createImageFile(Utils.CAMERA_PICTURE_PREFIX,
                Utils.getCacheDirectory(), ".JPEG");
        if (outPut == null) {
            Toast.makeText(
                    this,
                    getResources()
                            .getString(R.string.camera_create_file_failed),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        capturedPhoto = outPut.getAbsolutePath();
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPut));
        takeIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        if (takeIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeIntent, Utils.CAMERA_TAKE_A_PICTURE);
        }
    }
    private void init() {
        pick_gv.setFastScrollEnabled(true);
        int noOfColumns = calculateNoOfColumns(this);
        pick_gv.setNumColumns(noOfColumns);
        galleryAdapter = new GalleryAdapter(this, selectedPhoto);
        galleryAdapter.setPickOne(PICK_ONE);

        pick_gv.setAdapter(galleryAdapter);

        LoadPhotoTask loadPhotoTask = new LoadPhotoTask(this, galleryAdapter, pick_loading_pb);
        loadPhotoTask.execute();
    }

    public void updateTitle(int num) {
        setTitle(num + " items selected");
    }


    public ArrayList<Photo> getGalleryPhotos() {
        ArrayList<Photo> galleryList = new ArrayList<Photo>();

        try {
            final String[] columns = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;

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
        int temp = galleryAdapter.getSelectedItem().size();
        if (PICK_ONE) {
            if (temp == 1) {
                switch (PICK_IMAGE_POS){
                    case -1:
                        //edit photo
                        Intent i  = new Intent(this,EditActivity.class);
                        i.putExtra(INTENT_KEY_PICK_ONE_EDIT, galleryAdapter.getSelectedItem().get(0).sdcardPath);
                        startActivity(i);
                        break;
                    case -2:
                        //insert double
                        Intent returnIntent2 = new Intent();
                        returnIntent2.putExtra(Utils.INTENT_KEY_DOUBLE_PATH, galleryAdapter.getSelectedItem().get(0).sdcardPath);
                        setResult(Activity.RESULT_OK, returnIntent2);
                        finish();
                        break;
                    default:
                        //insert photo
                        currentPhotos.remove(PICK_IMAGE_POS);
                        Utils.currentPhotos.add(PICK_IMAGE_POS, galleryAdapter.getSelectedItem().get(0));
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
                Utils.currentPhotos.addAll(galleryAdapter.getSelectedItem());
                startActivity(new Intent(this, CollageActivity.class));
            }

        }
    }


    @Override
    protected void onClickClear() {
        galleryAdapter.clearSelection();
        selectedPhoto.clear();
        updateTitle(0);
        selectedAdapter.notifyDataSetChanged();
        galleryAdapter.notifyDataSetChanged();
    }

    public void addSelectedPhoto(Photo photo) {
        selectedPhoto.add(photo);
        selectedAdapter.notifyDataSetChanged();
    }

    public void removeSelectedPhoto(Photo photo) {
        selectedPhoto.remove(photo);
        selectedAdapter.notifyDataSetChanged();
    }
}
