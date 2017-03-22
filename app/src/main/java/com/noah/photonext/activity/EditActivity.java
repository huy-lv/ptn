package com.noah.photonext.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.imagezoom.ImageViewTouch;
import com.imagezoom.ImageViewTouchBase;
import com.noah.photonext.R;
import com.noah.photonext.adapter.EditToolAdapter;
import com.noah.photonext.adapter.EffectToolAdapter;
import com.noah.photonext.base.BaseActivityToolbar;
import com.noah.photonext.model.EditToolObject;
import com.noah.photonext.model.EffectToolObject;
import com.noah.photonext.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;

import static com.noah.photonext.util.Utils.INTENT_KEY_PICK_ONE_EDIT;
import static com.noah.photonext.util.Utils.PREPATH;
import static com.noah.photonext.util.Utils.createEditToolList;
import static com.noah.photonext.util.Utils.createEffectToolList;
import static com.noah.photonext.util.Utils.currentBitmap;
import static com.noah.photonext.util.Utils.currentHistoryPos;
import static com.noah.photonext.util.Utils.historyBitmaps;


/**
 * Created by HuyLV-CT on 30-Nov-16.
 */

public class EditActivity extends BaseActivityToolbar implements View.OnClickListener {
    @BindView(R.id.edit_main_iv)
    ImageViewTouch edit_main_iv;
    @BindView(R.id.edit_tool_list)
    LinearLayout edit_tool_list;

    //tab1
    @BindView(R.id.edit_tool_iv)
    ImageView edit_tool_iv;
    @BindView(R.id.edit_content_tool_1_rv)
    RecyclerView edit_content_tool_1_rv;
    EditToolAdapter editToolAdapter;
    ArrayList<EditToolObject> editToolList;


    //tab2
    @BindView(R.id.edit_brushes_iv)
    ImageView edit_brushes_iv;
    @BindView(R.id.edit_content_tool_2_ll)
    LinearLayout edit_content_tool_2_ll;

    //tab3
    @BindView(R.id.edit_effect_iv)
    ImageView edit_effect_iv;
    @BindView(R.id.edit_content_tool_3_rv)
    RecyclerView edit_content_tool_3_rv;
    EffectToolAdapter effectToolAdapter;
    //tab4
    @BindView(R.id.edit_sticker_iv)
    ImageView edit_sticker_iv;
    @BindView(R.id.edit_content_tool_4_rv)
    RecyclerView edit_content_tool_4_rv;
    //tab5
    @BindView(R.id.edit_text_iv)
    ImageView edit_text_iv;
    @BindView(R.id.edit_content_tool_5_rv)
    RecyclerView edit_content_tool_5_rv;
    @BindView(R.id.toolbar_undo_iv)
    ImageView toolbar_undo_iv;
    @BindView(R.id.toolbar_redo_iv)
    ImageView toolbar_redo_iv;
    String currentImagePath;
    private ArrayList<EffectToolObject> effectToolList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edit_main_iv.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        Intent i = getIntent();
        if (i != null) {
            if (i.hasExtra(INTENT_KEY_PICK_ONE_EDIT)) {//edit one photo
                currentImagePath = getIntent().getStringExtra(INTENT_KEY_PICK_ONE_EDIT);

                Picasso.with(this).load(PREPATH + currentImagePath)/*.centerInside()*/.into(edit_main_iv);
                currentBitmap = Utils.fixRotateBitmap(currentImagePath);
                historyBitmaps.add(currentBitmap);
            } else {//next from collage
                edit_main_iv.setImageBitmap(currentBitmap);
            }
        } else {
            Log.e("cxz", "error");

        }

        showHidePanel(-1);

        edit_tool_iv.setOnClickListener(this);
        edit_brushes_iv.setOnClickListener(this);
        edit_effect_iv.setOnClickListener(this);
        edit_sticker_iv.setOnClickListener(this);
        edit_text_iv.setOnClickListener(this);

        toolbar_undo_iv.setOnClickListener(this);
        toolbar_redo_iv.setOnClickListener(this);


        //tab1 edit tool
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
        edit_content_tool_1_rv.setLayoutManager(gridLayoutManager);
        editToolList = createEditToolList(getResources());
        editToolAdapter = new EditToolAdapter(this, editToolList);
        edit_content_tool_1_rv.setAdapter(editToolAdapter);

        //tab2 brush


        //effect tab
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        edit_content_tool_3_rv.setLayoutManager(linearLayoutManager);
        effectToolList = createEffectToolList(getResources());
        effectToolAdapter = new EffectToolAdapter(this, effectToolList);
        edit_content_tool_3_rv.setAdapter(effectToolAdapter);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int size = historyBitmaps.size();
        switch (v.getId()) {
            case R.id.toolbar_undo_iv:
                if (size > 1 && currentHistoryPos < size - 1) {
                    currentHistoryPos += 1;
                    Log.e("cxz", "current history pos = " + currentHistoryPos);
                    currentBitmap = historyBitmaps.get(currentHistoryPos);
                    edit_main_iv.setImageBitmap(currentBitmap);
                    updateUndoRedoButton();
                }

                break;
            case R.id.toolbar_redo_iv:
                if (size > 1 && currentHistoryPos > 0) {
                    currentHistoryPos -= 1;
                    Log.e("cxz", "current history pos = " + currentHistoryPos);
                    currentBitmap = historyBitmaps.get(currentHistoryPos);
                    edit_main_iv.setImageBitmap(currentBitmap);
                    updateUndoRedoButton();
                }
                break;
            case R.id.edit_tool_iv:
                showHidePanel(0);
                break;
            case R.id.edit_brushes_iv:
                showHidePanel(1);
                break;
            case R.id.edit_effect_iv:
                showHidePanel(2);
                break;
            case R.id.edit_sticker_iv:
                showHidePanel(3);
                break;
            case R.id.edit_text_iv:
                showHidePanel(4);
                break;
//            case R.id.edit_effect_group1:
//                reselect(0);
//                edit_viewpager_effect_group.setCurrentItem(0,true);
//                break;
//            case R.id.edit_effect_group2:
//                reselect(1);
//                edit_viewpager_effect_group.setCurrentItem(1,true);
//                break;
//            case R.id.edit_effect_group3:
//                reselect(2);
//                edit_viewpager_effect_group.setCurrentItem(2,true);
//                break;
//            case R.id.edit_effect_group4:
//                reselect(3);
//                edit_viewpager_effect_group.setCurrentItem(3,true);
//                break;
//            case R.id.edit_effect_group5:
//                reselect(4);
//                edit_viewpager_effect_group.setCurrentItem(4,true);
//                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateUndoRedoButton();
    }

    public void updateUndoRedoButton() {
        int size = historyBitmaps.size();
        if (size > 1) {
            //show undo/redo
            toolbar_undo_iv.setVisibility(View.VISIBLE);
            toolbar_redo_iv.setVisibility(View.VISIBLE);
            if (currentHistoryPos > size - 2) {
                //disable undo
                toolbar_undo_iv.setEnabled(false);
                toolbar_undo_iv.setAlpha(0.5f);
            } else {
                toolbar_undo_iv.setEnabled(true);
                toolbar_undo_iv.setAlpha(1f);
            }
            if (currentHistoryPos < 1) {
                //disable redo
                toolbar_redo_iv.setEnabled(false);
                toolbar_redo_iv.setAlpha(0.5f);
            } else {
                toolbar_redo_iv.setEnabled(true);
                toolbar_redo_iv.setAlpha(1f);
            }
        } else {
            //hide undo/redo
            toolbar_undo_iv.setVisibility(View.GONE);
            toolbar_redo_iv.setVisibility(View.GONE);
        }
    }

    void showHidePanel(int n) {
        edit_content_tool_1_rv.setVisibility(n == 0 ? View.VISIBLE : View.GONE);
        edit_tool_iv.setImageResource(n == 0 ? R.mipmap.ic_tools_ac : R.mipmap.ic_tools);
        edit_content_tool_2_ll.setVisibility(n == 1 ? View.VISIBLE : View.GONE);
        edit_brushes_iv.setImageResource(n == 1 ? R.mipmap.ic_brushes_ac : R.mipmap.ic_brushes);
        edit_content_tool_3_rv.setVisibility(n == 2 ? View.VISIBLE : View.GONE);
        edit_effect_iv.setImageResource(n == 2 ? R.mipmap.ic_effect_ac : R.mipmap.ic_effect);
        edit_content_tool_4_rv.setVisibility(n == 3 ? View.VISIBLE : View.GONE);
        edit_sticker_iv.setImageResource(n == 3 ? R.mipmap.ic_sticker_ac : R.mipmap.ic_sticker);
        edit_content_tool_5_rv.setVisibility(n == 4 ? View.VISIBLE : View.GONE);
        edit_text_iv.setImageResource(n == 4 ? R.mipmap.ic_text_ac : R.mipmap.ic_text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == Utils.REQUEST_CODE_CROP) {
        if (resultCode == RESULT_OK) {
            edit_main_iv.setImageBitmap(historyBitmaps.get(currentHistoryPos));
        }
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

    @Override
    public void onClickNext() {
        //finish...
    }

    @Override
    public void onBackPressed() {
        onClickBack();
    }
}
