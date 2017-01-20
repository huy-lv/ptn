package com.noah.photonext.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.noah.photonext.R;
import com.noah.photonext.util.Utils;
import com.noah.photonext.base.BaseActivityToolbar;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by HuyLV-CT on 10-Nov-16.
 */
public class ResultActivity extends BaseActivityToolbar{

    @BindView(R.id.result_iv)
    ImageView result_iv;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            path = intent.getStringExtra(Utils.INTENT_KEY_IMAGE_PATH);
            Picasso.with(this).load(Utils.PREPATH+path).into(result_iv);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_result;
    }

    @Override
    public void onClickNext() {

    }

    @OnClick(R.id.result_share_bt)
    void shareLink(View v)
    {
        Intent share = new Intent(Intent.ACTION_SEND);

        // If you want to share a png image only, you can do:
        // setType("image/png"); OR for jpeg: setType("image/jpeg");
        share.setType("image/*");

        // Make sure you put example png image named myImage.png in your
        // directory


        Log.d("", "PATH:" + path);

        File imageFileToShare = new File(path);
        Uri uri = Uri.fromFile(imageFileToShare);

        share.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(share, "Share Image!"));
    }
}
