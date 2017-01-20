package com.noah.photonext.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;

import com.noah.photonext.custom.PhotoList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by HuyLV-CT on 03-Nov-16.
 */

public class Utils {
    public static final String INTENT_KEY_PICK_ONE = "INTENT_KEY_PICK_ONE";
    public static final String INTENT_KEY_PICK_POS = "INTENT_KEY_PICK_POS";
    public static final int REQUEST_CODE_PICK_ONE = 101;
    public static final String INTENT_KEY_IMAGE_PATH = "INTENT_KEY_IMAGE_PATH";
    public static final String INTENT_KEY_PICK_ONE_EDIT = "INTENT_KEY_PICK_ONE_EDIT";
    public static final int REQUEST_CODE_CROP = 102;
    public static final int REQUEST_CODE_ROTATE = 103;
    public static final int REQUEST_CODE_AUTOFIX = 105;
    public static final int REQUEST_CODE_ADJUSTMENT = 104;

    public static final int BRIGHTNESS_ALPHA = 1;
    public static final int REQUEST_CODE_BLUR = 106;
    public static final int REQUEST_CODE_AUTOCONTRAST = 107;
    public static final int REQUEST_CODE_PICK_DOUBLE = 108;
    public static final String INTENT_KEY_DOUBLE_PATH = "INTENT_KEY_DOUBLE_PATH";
    public static final String INTENT_KEY_PICK_DOUBLE = "INTENT_KEY_PICK_DOUBLE";

    public static int numOfPhoto =0;
    public static PhotoList currentPhotos = new PhotoList();
    public static String PREPATH = "file://";
    public static int canvasWidth,canvasHeight;
    public static int frameWidth,frameHeight, originCollageWidth, originCollageHeidht,currentCollageWidth,currentCollageHeight;
    public static String FolderName = "Pnext";

    public static Bitmap currentBitmap;
    public static ArrayList<Bitmap> historyBitmaps = new ArrayList<>();
    public static int currentHistoryPos = 0;
    public static Bitmap newBitmap;

//    public static ArrayList<Integer> origW;
//    public static ArrayList<Integer> origH;
//    public static ArrayList<Integer> actH;

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private static int getExifOrientation(String src) throws IOException {
        int orientation = 1;

        try {
            /**
             * if your are targeting only api level >= 5
             * ExifInterface exif = new ExifInterface(src);
             * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class.forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass.getConstructor(new Class[] { String.class });
                Object exifInstance = exifConstructor.newInstance(new Object[] { src });
                Method getAttributeInt = exifClass.getMethod("getAttributeInt", new Class[] { String.class, int.class });
                Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance, new Object[] { tagOrientation, 1});
            }
        } catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return orientation;
    }
    public static Bitmap fixRotateBitmap(String src) {
        Bitmap bitmap = BitmapFactory.decodeFile(src);
        try {
            int orientation = getExifOrientation(src);

            if (orientation == 1) {
                return bitmap;
            }

            Matrix matrix = new Matrix();
            switch (orientation) {
                case 2:
                    matrix.setScale(-1, 1);
                    break;
                case 3:
                    matrix.setRotate(180);
                    break;
                case 4:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case 5:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case 6:
                    matrix.setRotate(90);
                    break;
                case 7:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case 8:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;
            }

            try {
                Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return oriented;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static void showAlertDialog(final Activity context,String title,String message,DialogInterface.OnClickListener onPositive){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes,onPositive)
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
