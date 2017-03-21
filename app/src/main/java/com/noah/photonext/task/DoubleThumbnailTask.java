package com.noah.photonext.task;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;

import com.noah.photonext.adapter.DoubleThumbnailAdapter;
import com.noah.photonext.model.DoubleThumbnailObject;

import java.util.ArrayList;

import static com.noah.photonext.util.Utils.xfermodes;

/**
 * Created by huylv on 16-Mar-17.
 */

public class DoubleThumbnailTask extends AsyncTask<Bitmap, Void, Void> {
    DoubleThumbnailAdapter adapter;
    private ArrayList<DoubleThumbnailObject> doubleList;

    public DoubleThumbnailTask(ArrayList<DoubleThumbnailObject> s, DoubleThumbnailAdapter d) {
        doubleList = s;
        adapter = d;
    }

    @Override
    protected Void doInBackground(Bitmap... params) {
        Bitmap bm1 = params[0];
        Bitmap bm2 = params[1];
        Paint p = new Paint();

        for (int i = 0; i < 3; i++) {
            doubleList.get(i).bitmap = bm1.copy(bm1.getConfig(), true);
            Canvas c = new Canvas(doubleList.get(i).bitmap);
            p.setXfermode(xfermodes[i]);
            c.drawBitmap(bm2, 0, 0, p);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        adapter.notifyDataSetChanged();
    }
}
