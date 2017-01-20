package com.noah.photonext.custom;

import com.noah.photonext.MainActivity;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

/**
 * Created by HuyLV-CT on 26-Dec-16.
 */

public class SMat extends Mat {
    private float exposure=1;
    private int temperature=0;
    private float contrast=1;
    private int brightnesss=0;
    private int vibrance=0;

    public SMat(){}

    public SMat(Size size, int type, Scalar scalar) {
        super(size,type,scalar);
    }

    public void adjustMat(Mat out){
        MainActivity.native_adjustMat(nativeObj,out.getNativeObjAddr(),exposure,temperature,contrast,brightnesss,vibrance);
    }

    public void adjustMat(Mat out,int exp,int temp,int cont,int bri,int vib){
        exposure = ((float)exp+100)/100;
        temperature=temp;
        contrast = ((float)cont+100)/100;
        brightnesss = bri;
        vibrance = vib;
        MainActivity.native_adjustMat(nativeObj,out.getNativeObjAddr(),exposure,temperature,contrast,brightnesss,vibrance);
    }



    public void autofix(Mat m, float clip){
        MainActivity.native_autofix(nativeObj,m.nativeObj,clip);
    }

    public void adjustTemperature(Mat out,int adjustValue){
        temperature=adjustValue;
        MainActivity.native_adjustTemperature(nativeObj,out.getNativeObjAddr(),temperature);
    }

    public void adjustContrast(Mat out,int adjustValue){
        contrast = ((float)adjustValue+100)/100;
        MainActivity.native_adjustContrast(nativeObj,out.getNativeObjAddr(),contrast,brightnesss);
    }

    public void adjustBrightness(Mat out,int v){
        brightnesss = v;
//        convertTo(out,-1, exposure, brightnesss);
        MainActivity.native_adjustBrightness(nativeObj,out.getNativeObjAddr(),contrast,brightnesss);
    }

    public void adjustExposure(Mat out,int v){
        exposure  = ((float)v+100)/100;
//        convertTo(out,-1, exposure, brightnesss);
        MainActivity.native_adjustBrightness(nativeObj,out.getNativeObjAddr(),exposure,brightnesss);
    }


    public void adjustVibrance(SMat out, int value) {
        vibrance = value;
        MainActivity.native_adjustVibrance(nativeObj,out.getNativeObjAddr(),value);
    }

    public void clearBackground(SMat out){
        MainActivity.native_clearBackground(nativeObj,out.getNativeObjAddr());
    }

    public int getExposureToProgress(){
        return (int) (exposure*100-100);
    }

    public int getContrastToProgress(){
        return (int) (contrast*100-100);
    }

    public float getExposure() {
        return exposure;
    }

    public void setExposure(float exposure) {
        this.exposure = exposure;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public float getContrast() {
        return contrast;
    }

    public void setContrast(float contrast) {
        this.contrast = contrast;
    }

    public int getBrightnesss() {
        return brightnesss;
    }

    public void setBrightnesss(int brightnesss) {
        this.brightnesss = brightnesss;
    }

    public int getVibrance() {
        return vibrance;
    }

    public void setVibrance(int vibrance) {
        this.vibrance = vibrance;
    }


}
