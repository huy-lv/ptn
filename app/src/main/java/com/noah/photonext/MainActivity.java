package com.noah.photonext;

import android.content.Intent;
import android.util.Log;

import com.noah.photonext.activity.PickPhotoActivity;
import com.noah.photonext.base.BaseActivity;
import com.noah.photonext.util.Utils;

import org.opencv.android.OpenCVLoader;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    static{
        if(OpenCVLoader.initDebug()){
            System.loadLibrary("imageprocess");
        }else{
            Log.e("cxz","opencv not loaded");
        }
    }
    public static native void native_autofix(long nativeObj, long outputObject, float clip);
    public static native void native_adjustTemperature(long nativeObj,long outputObject,int adjustValue);
    public static native void native_adjustContrast(long nativeObj, long outputObject, float adjustValue,int brightness);
    public static native void native_adjustVibrance(long nativeObj, long nativeObjAddr, int value) ;
    public static native void native_adjustBrightness(long nativeObj, long nativeObjAddr, float contrast, int brightnesss);
    public static native void native_adjustMat(long nativeObj, long nativeObjAddr, float exp,int temp,float cont, int bri,int vib);
    public static native void native_clearBackground(long nativeObj, long nativeObjAddr);
    public static native void native_clearBackground2(long nativeObj, long nativeObjAddr);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.main_to_collage)
    void pickPhoto(){
        startActivity(new Intent(this,PickPhotoActivity.class));
    }

    @OnClick(R.id.main_to_edit)
    void gotoEdit(){
        Intent i = new Intent(this,PickPhotoActivity.class);
        i.putExtra(Utils.INTENT_KEY_PICK_ONE,1);
        startActivity(i);
    }



}
