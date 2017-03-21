package com.noah.photonext.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.noah.photonext.process.ITransformation;

/**
 * Created by huylv on 17-Mar-17.
 */

public class TransformTask extends AsyncTask<Void, Void, Void> {
    ITransformation transformation;
    Bitmap srcBm, dstBm;
    OnProcessDone listener;

    public TransformTask(Bitmap bm, ITransformation t, OnProcessDone l) {
        transformation = t;
        listener = l;
        srcBm = bm;
    }

    @Override
    protected Void doInBackground(Void... params) {

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        listener.onProcessDone(dstBm);
    }

    interface OnProcessDone {
        void onProcessDone(Bitmap bm);
    }
}
