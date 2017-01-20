package com.noah.photonext.base;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noah.photonext.R;

import butterknife.ButterKnife;


/**
 * Created by HuyLV-CT on 19-Aug-16.
 */
public abstract class BaseActivityToolbar extends BaseActivity implements View.OnClickListener {
    @Nullable
    protected RelativeLayout toolBar;
    TextView toolbar_title_tv;
    ImageView toolbar_back_iv;
    ImageView toolbar_next_iv;
    ImageView toolbar_clear_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolBar = ButterKnife.findById(this,R.id.toolbar);
//        toolbar_back_iv = ButterKnife.findById(toolBar,R.id.toolbar_back_iv);
//        toolbar_next_iv = ButterKnife.findById(toolBar,R.id.toolbar_next_iv);

        toolbar_back_iv = ButterKnife.findById(toolBar,R.id.toolbar_back_iv);
        toolbar_next_iv = ButterKnife.findById(toolBar,R.id.toolbar_next_iv);
        toolbar_title_tv = ButterKnife.findById(toolBar,R.id.toolbar_title_tv);
        toolbar_clear_iv = ButterKnife.findById(toolBar,R.id.toolbar_clear_iv);

        if(toolBar == null){
            Log.e("cxz","toolbar null");
        }else {
            toolbar_title_tv = (TextView)toolBar.findViewById(R.id.toolbar_title_tv);
            toolbar_title_tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/utmavo.ttf"));

            toolbar_back_iv.setOnClickListener(this);
            toolbar_next_iv.setOnClickListener(this);
            toolbar_clear_iv.setOnClickListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.toolbar_title_tv.setText(title);
    }

    public void setTitleStylePick(){
//        toolbar_title_tv.setTypeface(Typeface.create("sans-serif-medium",Typeface.NORMAL));
        toolbar_title_tv.setTextSize(20);
//        toolbar_title_tv.setTextAlignment();

        toolbar_clear_iv.setVisibility(View.VISIBLE);
    }

    public void onClickBack(){
        finish();
    }

    public void onClickNext(){}

    protected void hideToolbarButton(){
        toolbar_back_iv.setVisibility(View.GONE);
        toolbar_next_iv.setVisibility(View.GONE);

        toolbar_title_tv.setGravity(Gravity.CENTER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_back_iv:
                onClickBack();
                break;
            case R.id.toolbar_next_iv:
                onClickNext();
                break;
            case R.id.toolbar_clear_iv:
                onClickClear();
                break;
        }
    }

    protected void onClickClear() {    }
}
