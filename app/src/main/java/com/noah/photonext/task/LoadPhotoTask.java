package com.noah.photonext.task;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.noah.photonext.activity.PickPhotoActivity;
import com.noah.photonext.adapter.GalleryAdapter;

/**
 * Created by huylv on 21-Mar-17.
 */

public class LoadPhotoTask extends AsyncTask<Void, Void, Void> {
    PickPhotoActivity pickPhotoActivity;
    GalleryAdapter galleryAdapter;
    ProgressBar pb;

    public LoadPhotoTask(PickPhotoActivity ac, GalleryAdapter g, ProgressBar p) {
        galleryAdapter = g;
        pickPhotoActivity = ac;
        pb = p;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        galleryAdapter.addAll(pickPhotoActivity.getGalleryPhotos());
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pb.setVisibility(View.INVISIBLE);
        galleryAdapter.notifyDataSetChanged();

        if (galleryAdapter.isEmpty()) {
            pickPhotoActivity.pick_iv_no_media.setVisibility(View.VISIBLE);
        } else {
            pickPhotoActivity.pick_iv_no_media.setVisibility(View.GONE);
        }
    }
}
